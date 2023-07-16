package com.pack.v2.repositories;

import com.pack.v2.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByUserIdOrderByCreatedDateDesc(Long userId);
}
