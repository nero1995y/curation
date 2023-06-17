package com.blackMarket.curation.domain.post.repository;

import com.blackMarket.curation.domain.post.domain.Post;
import com.blackMarket.curation.domain.post.exception.PostNotfoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

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

    @DisplayName("게시글 조회 결과 수가 0 이다.")
    @Test
    void findSize() {
        //given

        //when
        List<Post> result = postRepository.findAll();

        //then
        assertThat(result.size()).isEqualTo(0);
    }

    @DisplayName("게시글 조회 결과가 2이다.")
    @Test
    void postSize2() {
        //given
        Post post = Post.builder()
                .title("newTitle")
                .content("newContent")
                .build();

        Post post2 = Post.builder()
                .title("newTitle")
                .content("newContent")
                .build();

        postRepository.save(post);
        postRepository.save(post2);
        //when
        List<Post> result = postRepository.findAll();

        //then
        assertThat(result.size()).isEqualTo(2);
    }

    @DisplayName("게시글이 삭제된다")
    @Test
    void remove() {
        //given
        Post post = Post.builder()
                .title("newTitle")
                .content("newContent")
                .build();

        Post save = postRepository.save(post);

        //when
        postRepository.deleteById(save.getId());

        //then
        Optional<Post> findPost = postRepository.findById(post.getId());

        assertThat(findPost).isEmpty();

    }
}