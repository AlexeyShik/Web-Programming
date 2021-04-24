package ru.itmo.wp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.itmo.wp.domain.Post;
import ru.itmo.wp.domain.User;

import javax.transaction.Transactional;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByOrderByCreationTimeDesc();

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO post (`title`, `text`, `user_id`) VALUES (?1, ?2, ?3)", nativeQuery = true)
    void save(String title, String text, long userId);
}
