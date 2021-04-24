package ru.itmo.wp.service;

import org.springframework.stereotype.Service;
import ru.itmo.wp.domain.Tag;
import ru.itmo.wp.repository.TagRepository;

@Service
public class TagService {
    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public int countByName(String name) {
        return tagRepository.countByName(name);
    }

    public Tag findByName(String name) {
        return tagRepository.findByName(name);
    }

    public void save(Tag tag) {
        tagRepository.save(tag);
    }
}
