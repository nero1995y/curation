package com.blackMarket.curation.domain.post.repository;

import com.blackMarket.curation.domain.post.domain.Post;
import com.blackMarket.curation.domain.post.exception.PostNotfoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
@DataJpaTest
class PostRepositoryTest {

    @Autowired
    PostRepository postRepository;

    @DisplayName("게시글 생성한다")
    @Test
    void create() {
        //given
        Post post = Post.builder()
                .title("newTitle")
                .content("newContent")
                .build();

        //when
        Post savePost = postRepository.save(post);

        //then
        Post findPost = postRepository.findById(savePost.getId())
                .orElseThrow(PostNotfoundException::new);

        assertThat(findPost).isEqualTo(savePost);
    }
    
    @DisplayName("게시글 검색한다.")
    @Test
    void findByTitle() {
        //given
        Post post = Post.builder()
                .title("newTitle")
                .content("newContent")
                .build();

        Post save = postRepository.save(post);

        //when
        Post findPost = postRepository.findByTitle(post.getTitle())
                .orElseThrow(PostNotfoundException::new);

        //then
        assertThat(findPost).isEqualTo(save);
    }
}