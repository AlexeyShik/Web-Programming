package ru.itmo.wp.web;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class StaticFilter extends HttpFilter {
    private File getFile(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String realPath = getServletContext().getRealPath("/");
        File file = new File(new File(realPath, "../../src/main/webapp"), uri);
        if (!file.isFile()) {
            file = new File(realPath, uri);
        }
        return file;
    }

    private void processFile(File file, HttpServletResponse response) throws IOException {
        response.setContentType(getServletContext().getMimeType(file.getCanonicalPath()));
        response.setContentLengthLong(file.length());
        Files.copy(file.toPath(), response.getOutputStream());
    }

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        File file = getFile(request);
        if (file.isFile()) {
            processFile(file, response);
        } else {
            chain.doFilter(request, response);
        }
    }
}
