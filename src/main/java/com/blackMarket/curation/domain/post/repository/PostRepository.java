package com.blackMarket.curation.domain.post.repository;


import com.blackMarket.curation.domain.post.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    Optional<Post> findByTitle(String title);
    Page<Post> findAll(Pageable pageable);
}
