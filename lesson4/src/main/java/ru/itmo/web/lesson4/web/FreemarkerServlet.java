package ru.itmo.web.lesson4.web;

import freemarker.template.*;
import ru.itmo.web.lesson4.util.DataUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class FreemarkerServlet extends HttpServlet {
    private static final String UTF_8 = StandardCharsets.UTF_8.name();
    private final Configuration cfg = new Configuration(Configuration.VERSION_2_3_30);

    @Override
    public void init() throws ServletException {
        super.init();

        File dir = new File(getServletContext().getRealPath("."), "../../src/main/webapp/WEB-INF/templates");
        try {
            cfg.setDirectoryForTemplateLoading(dir);
        } catch (IOException e) {
            throw new ServletException("Unable to set template directory [dir=" + dir + "].", e);
        }

        cfg.setDefaultEncoding(UTF_8);
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);
        cfg.setLogTemplateExceptions(false);
        cfg.setWrapUncheckedExceptions(true);
        cfg.setFallbackOnNullLoopVariable(false);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding(UTF_8);
        response.setCharacterEncoding(UTF_8);

        Template template;
        try {
            String URI = URLDecoder.decode(request.getRequestURI(), UTF_8);
            if (URI.equals("/") || URI.equals("")) {
                response.sendRedirect("/index");
                return;
            }
            template = cfg.getTemplate(URI + ".ftlh");
        } catch (TemplateNotFoundException ignored) {
            response.sendRedirect("/no-page");
            return;
        }

        response.setContentType("text/html");
        try {
            template.process(getData(request), response.getWriter());
        } catch (TemplateException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private static boolean isCorrectId(String id) {
        try {
            Long.parseLong(id);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static void putParameter(Map<String, Object> data, String key, String value) {
        if (key.endsWith("_id") && isCorrectId(value)) {
            data.put(key, Long.parseLong(value));
        } else if (key.endsWith("_id")) {
            data.put(key, -1);
        } else {
            data.put(key, value);
        }
    }

    private Map<String, Object> getData(HttpServletRequest request) throws UnsupportedEncodingException {
        Map<String, Object> data = new HashMap<>();

        for (Map.Entry<String, String[]> e : request.getParameterMap().entrySet()) {
            if (e.getValue() != null && e.getValue().length == 1) {
                putParameter(data, e.getKey(), e.getValue()[0]);
            }
        }
        data.put("URI", URLDecoder.decode(request.getRequestURI(), UTF_8));

        DataUtil.addData(request, data);
        return data;
    }
}