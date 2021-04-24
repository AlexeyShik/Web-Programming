package ru.itmo.wp.controller;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import ru.itmo.wp.domain.Post;
import ru.itmo.wp.exception.ValidationException;
import ru.itmo.wp.form.PostCredentials;
import ru.itmo.wp.form.validator.PostCredentialsValidator;
import ru.itmo.wp.service.PostService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/1")
public class PostController {
    private final PostService postService;
    private final PostCredentialsValidator postCredentialsValidator;

    public PostController(PostService postService, PostCredentialsValidator postCredentialsValidator) {
        this.postService = postService;
        this.postCredentialsValidator = postCredentialsValidator;
    }

    @InitBinder("postCredentials")
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(postCredentialsValidator);
    }

    @PostMapping("/posts")
    public void writePost(@Valid @RequestBody PostCredentials postCredentials, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }
        postService.save(postCredentials.getTitle(), postCredentials.getText(), postCredentials.getUser());
    }

    @GetMapping("/posts")
    public List<Post> findPosts() {
        return postService.findAll();
    }
}
