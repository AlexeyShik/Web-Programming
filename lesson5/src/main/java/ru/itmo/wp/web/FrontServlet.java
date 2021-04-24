package ru.itmo.wp.web;

import com.google.common.base.Strings;
import freemarker.template.*;
import ru.itmo.wp.web.exception.NotFoundException;
import ru.itmo.wp.web.exception.RedirectException;
import ru.itmo.wp.web.exception.TemplateInitException;
import ru.itmo.wp.web.page.IndexPage;
import ru.itmo.wp.web.page.NotFoundPage;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class FrontServlet extends HttpServlet {
    private static final String BASE_PACKAGE = FrontServlet.class.getPackage().getName() + ".page";
    private static final String DEFAULT_ACTION = "action";

    private Configuration sourceConfiguration;
    private Configuration targetConfiguration;

    private Configuration newFreemarkerConfiguration(String templateDirName, boolean isDebug) throws ServletException {
        File templateDir = new File(templateDirName);
        if (!templateDir.isDirectory()) {
            return null;
        }

        Configuration configuration = new Configuration(Configuration.VERSION_2_3_30);
        try {
            configuration.setDirectoryForTemplateLoading(templateDir);
        } catch (IOException e) {
            throw new ServletException("Can't create freemarker configuration [templateDir=" + templateDir + "]");
        }
        configuration.setLocalizedLookup(false);
        configuration.setDefaultEncoding(StandardCharsets.UTF_8.name());
        configuration.setTemplateExceptionHandler(isDebug ? TemplateExceptionHandler.HTML_DEBUG_HANDLER :
                TemplateExceptionHandler.RETHROW_HANDLER);
        configuration.setLogTemplateExceptions(false);
        configuration.setWrapUncheckedExceptions(true);

        return configuration;
    }

    @Override
    public void init() throws ServletException {
        sourceConfiguration = newFreemarkerConfiguration(
                getServletContext().getRealPath("/") + "../../src/main/webapp/WEB-INF/templates", true);
        targetConfiguration = newFreemarkerConfiguration(getServletContext().getRealPath("WEB-INF/templates"), false);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        process(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        process(request, response);
    }

    private void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Route route = Route.newRoute(request);
        updateLanguage(request);
        try {
            process(route, request, response);
        } catch (NotFoundException e) {
            try {
                process(Route.NOT_FOUND_ROUTE, request, response);
            } catch (NotFoundException notFoundException) {
                throw new ServletException(notFoundException);
            }
        }
    }

    private void updateLanguage(HttpServletRequest request) {
        String parameter = request.getParameter("lang");
        if (parameter != null && parameter.matches("[a-z]{2}")) {
            request.getSession().setAttribute("lang", parameter);
        }
    }

    private void process(Route route, HttpServletRequest request, HttpServletResponse response) throws NotFoundException, ServletException, IOException {
        Class<?> pageClass = initPageClass(route);

        Object page = initPage(pageClass);
        Map<String, Object> view = new HashMap<>();
        Map<Class<?>, Object> typesToInstancesMap = Map.of(HttpServletRequest.class, request, Map.class, view);

        String methodName = route.getAction();
        for (Class<?> clazz = pageClass; clazz != null; clazz = clazz.getSuperclass()) {
            for (Method method : clazz.getDeclaredMethods()) {
                if (Objects.equals(methodName, method.getName())) {
                    method.setAccessible(true);
                    Class<?>[] classes = filterTypes(typesToInstancesMap, method.getParameterTypes());

                    if (classes.length == method.getParameterTypes().length
                            && invokeMethod(method, page, typesToInstancesMap, classes, response)) {

                        safeProcessTemplate(request, response, pageClass, view);
                        return;
                    }
                }
            }
        }

        throw new NotFoundException();
    }

    private boolean invokeMethod(Method method, Object page, Map<Class<?>, Object> typesToInstancesMap,
                                 Class<?>[] classes, HttpServletResponse response) throws IOException {
        try {
            method.invoke(page, mapToInstance(typesToInstancesMap, classes));
        } catch (IllegalAccessException ignored) {
            return false;
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            if (cause instanceof RedirectException) {
                RedirectException redirectException = (RedirectException) cause;
                response.sendRedirect(redirectException.getTarget());
                return true;
            }
            return false;
        }
        return true;
    }

    private void safeProcessTemplate(HttpServletRequest request, HttpServletResponse response, Class<?> pageClass,
                                     Map<String, Object> view) throws ServletException, IOException {
        try {
            processTemplate(request, response, pageClass, view);
        } catch (TemplateException ignored) {
            // no operations
        }
    }

    private void processTemplate(HttpServletRequest request, HttpServletResponse response, Class<?> pageClass,
                                 Map<String, Object> view) throws ServletException, TemplateException, IOException {
        Template template;
        try {
            template = newTemplate(initTemplateName(request.getSession(), pageClass));
        } catch (ServletException | TemplateInitException e) {
            template = newTemplate(pageClass.getSimpleName() + ".ftlh");
        }
        response.setContentType("text/html");
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        template.process(view, response.getWriter());
    }

    private String initTemplateName(HttpSession session, Class<?> pageClass) throws TemplateInitException {
        String lang = (String) session.getAttribute("lang");
        String simpleName = pageClass.getSimpleName();
        if (!Strings.isNullOrEmpty(lang) && !lang.equals("en")) {
            return simpleName + "_" + lang + ".ftlh";
        }
        throw new TemplateInitException("No special templates found, try default");
    }

    private Object initPage(Class<?> pageClass) throws ServletException {
        try {
            return pageClass.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new ServletException("Can't create page [pageClass=" + pageClass + "]");
        }
    }

    private Class<?> initPageClass(Route route) throws NotFoundException {
        try {
            return Class.forName(route.getClassName());
        } catch (ClassNotFoundException e) {
            throw new NotFoundException();
        }
    }

    private Class<?>[] filterTypes(Map<Class<?>, Object> rule, Class<?>... parameterTypes) {
        return Arrays.stream(parameterTypes).filter(rule::containsKey).toArray(Class<?>[]::new);
    }

    private Object[] mapToInstance(Map<Class<?>, Object> rule, Class<?>... parameterTypes) {
        return Arrays.stream(parameterTypes).map(rule::get).toArray();
    }

    private Template newTemplate(String templateName) throws ServletException {
        Template template = null;

        if (sourceConfiguration != null) {
            template = initTemplate(templateName, sourceConfiguration);
        }

        if (template == null && targetConfiguration != null) {
            template = initTemplate(templateName, targetConfiguration);
        }

        if (template == null) {
            throw new ServletException("Can't find template [templateName=" + templateName + "]");
        }

        return template;
    }

    private Template initTemplate(String templateName, Configuration configuration) throws ServletException {
        try {
            return configuration.getTemplate(templateName);
        } catch (TemplateNotFoundException ignored) {
            return null;
        } catch (IOException e) {
            throw new ServletException("Can't load template [templateName=" + templateName + "]", e);
        }
    }

    private static class Route {
        private final String className;
        private final String action;
        private static final Route NOT_FOUND_ROUTE = new Route(NotFoundPage.class.getName(), DEFAULT_ACTION);
        private static final Route INDEX_ROUTE = new Route(IndexPage.class.getName(), DEFAULT_ACTION);

        private Route(String className, String action) {
            this.className = className;
            this.action = action;
        }

        private String getClassName() {
            return className;
        }

        private String getAction() {
            return action;
        }

        private static Route newRoute(HttpServletRequest request) {
            String uri = request.getRequestURI();

            List<String> classNameParts = Arrays.stream(uri.split("/"))
                    .filter(part -> !Strings.isNullOrEmpty(part))
                    .collect(Collectors.toList());

            if (classNameParts.isEmpty()) {
                return INDEX_ROUTE;
            }

            StringBuilder simpleClassName = new StringBuilder(classNameParts.get(classNameParts.size() - 1));
            int lastDotIndex = simpleClassName.lastIndexOf(".");
            simpleClassName.setCharAt(lastDotIndex + 1,
                    Character.toUpperCase(simpleClassName.charAt(lastDotIndex + 1)));
            classNameParts.set(classNameParts.size() - 1, simpleClassName.toString());

            String className = BASE_PACKAGE + "." + String.join(".", classNameParts) + "Page";

            String action = request.getParameter("action");
            if (Strings.isNullOrEmpty(action)) {
                action = DEFAULT_ACTION;
            }

            return new Route(className, action);
        }
    }
}
