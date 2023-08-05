package com.pack.v2.repositories;

import com.pack.v2.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByUserIdOrderByCreatedDateDesc(Long userId);
    List<Post> findAllByUserIdAndIsDeletedFalseOrderByCreatedDateDesc(Long userId);
    List<Post> findAllByUserIdAndIsDeletedTrueOrderByIsDeletedDateDesc(Long userId);
    List<Post> findAllByIsDeletedTrueAndIsDeletedDateBefore(Date date);
}

