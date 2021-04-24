package ru.itmo.wp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.itmo.wp.domain.Comment;
import ru.itmo.wp.domain.Post;
import ru.itmo.wp.security.Guest;

import javax.servlet.http.HttpSession;
import java.util.Arrays;

@Controller
public class PostPage extends Page {
    @Guest
    @GetMapping("/post/{id}")
    public String getPost(Model model, @PathVariable String id) {
        model.addAttribute("post", idToPost(id));
        model.addAttribute("comment", new Comment());
        return "PostPage";
    }

    @Guest
    @GetMapping("/post")
    public String getPost(Model model) {
        model.addAttribute("post", null);
        return "PostPage";
    }

    @PostMapping("/post/{id}")
    public String writeComment(String text, @PathVariable String id, HttpSession session) {
        Post post = idToPost(id);
        if (post != null) {
            Comment comment = new Comment();
            comment.setText(text);
            comment.setPost(post);
            comment.setUser(getUser(session));
            commentService.save(comment);
            if (post.getComments() == null) {
                post.setComments(Arrays.asList(comment));
            } else {
                post.getComments().add(comment);
            }
        }
        return "redirect:/post/" + id;
    }
}
