package ru.itmo.wp.servlet;

import ru.itmo.wp.util.ImageUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;

import java.nio.file.Files;
import java.nio.file.Path;

public class CaptchaFilter extends HttpFilter {
    private void sendCaptchaHtml(HttpServletResponse response, HttpSession session, boolean resetAnswer) throws IOException {
        if (resetAnswer) {
            String randomNumber = String.valueOf(((int) (Math.random() * 1000) % 900) + 100);  //  thread safe
            session.setAttribute("expectedAnswer", randomNumber);
        }
        response.setContentType("text/html");
        Files.copy(Path.of(getServletContext().getRealPath("/static/captchaForm.html")), response.getOutputStream());
    }

    private static void sendCaptchaImage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        byte[] byteImage = ImageUtils.toPng(getExpectedAnswer(request));
        response.setContentType("image/png");
        response.getOutputStream().write(byteImage);
    }

    private static String getExpectedAnswer(HttpServletRequest request) {
        return (String) request.getSession().getAttribute("expectedAnswer");
    }

    private void processSessionFirstQuery(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.getSession().setAttribute("realUri", request.getRequestURI());
        sendCaptchaHtml(response, request.getSession(), true);
    }

    private static boolean processServiceQuery(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (request.getRequestURI().equals("/img/captcha.png")) {
            sendCaptchaImage(request, response);
            return true;
        }
        return false;
    }

    private void processQuery(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (processServiceQuery(request, response)) {
            return;
        }

        String userAnswer = request.getParameter("userAnswer");
        HttpSession session = request.getSession();

        if (getExpectedAnswer(request).equals(userAnswer)) {
            session.setAttribute("captchaPassed", true);
            response.sendRedirect((String) session.getAttribute("realUri"));
        } else {
            sendCaptchaHtml(response, session, userAnswer != null);
        }
    }

    private void checkCaptcha(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (getExpectedAnswer(request) == null) {
            processSessionFirstQuery(request, response);
        } else {
            processQuery(request, response);
        }
    }

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (request.getMethod().equals("GET") && request.getSession().getAttribute("captchaPassed") == null) {
            checkCaptcha(request, response);
        } else {
            chain.doFilter(request, response);
        }
    }
}
