package ru.itmo.web.lesson4.web;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class StaticServlet extends HttpServlet {

    private File getSourceFile(String subUri) {
        return Path.of(getServletContext().getRealPath("."), "../../src/main/webapp", subUri).toFile();
    }

    private File getTargetFile(String subUri) {
        return new File(getServletContext().getRealPath("/"), subUri);
    }

    private File getFile(String subUri) {
        File file = getSourceFile(subUri);
        if (!file.isFile()) {
            file = getTargetFile(subUri);
        }
        return file;
    }

    private void processFile(File file, HttpServletResponse response, String subUri) throws IOException {
        if (response.getContentType() == null) {
            response.setContentType(getContentTypeFromName(subUri));
        }
        Files.copy(file.toPath(), response.getOutputStream());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String uri = request.getRequestURI();
        for (String subUri : uri.split("\\+")) {
            File file = getFile(subUri);
            if (file.isFile()) {
                processFile(file, response, subUri);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        }
    }

    private static String getContentTypeFromName(String name) {
        name = name.toLowerCase();

        if (name.endsWith(".png")) {
            return "image/png";
        }

        if (name.endsWith(".jpg")) {
            return "image/jpeg";
        }

        if (name.endsWith(".html")) {
            return "text/html";
        }

        if (name.endsWith(".css")) {
            return "text/css";
        }

        if (name.endsWith(".js")) {
            return "application/javascript";
        }

        throw new IllegalArgumentException("Can't find content type for '" + name + "'.");
    }
}