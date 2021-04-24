package ru.itmo.wp.form.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.itmo.wp.domain.Tag;
import ru.itmo.wp.form.PostCredentials;
import ru.itmo.wp.service.TagService;

import java.util.Arrays;

@Component
public class PostCredentialsValidator implements Validator {
    private final TagService tagService;

    public PostCredentialsValidator(TagService tagService) {
        this.tagService = tagService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return PostCredentials.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if (!errors.hasErrors()) {
            PostCredentials postForm = (PostCredentials) target;
            String[] tags = Arrays.stream(postForm.getTags().split("\\s+"))
                    .distinct().toArray(String[]::new);
            StringBuilder stringTags = new StringBuilder();
            for (String name : tags) {
                if (!name.isEmpty()) {
                    if (tagService.countByName(name) == 0){
                        Tag tag = new Tag();
                        tag.setName(name);
                        tagService.save(tag);
                    }
                    stringTags.append(name).append(" ");
                }
            }
            postForm.setTags(stringTags.toString());
        }
    }
}
