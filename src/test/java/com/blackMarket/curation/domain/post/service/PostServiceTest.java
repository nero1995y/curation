package com.blackMarket.curation.domain.post.service;

import com.blackMarket.curation.domain.category.domain.Category;
import com.blackMarket.curation.domain.category.servcie.CategoryService;
import com.blackMarket.curation.domain.member.domain.Member;
import com.blackMarket.curation.domain.member.domain.Role;
import com.blackMarket.curation.domain.member.serivce.MemberService;
import com.blackMarket.curation.domain.post.domain.Post;
import com.blackMarket.curation.domain.post.dto.PostResponseDto;
import com.blackMarket.curation.domain.post.dto.PostSaveRequestDto;
import com.blackMarket.curation.domain.post.exception.PostDuplicatedException;
import com.blackMarket.curation.domain.post.exception.PostNotfoundException;
import com.blackMarket.curation.domain.post.repository.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Slf4j
class PostServiceTest {

    @Mock
    PostRepository postRepository;

    @Mock
    MemberService memberService;

    @Mock
    CategoryService categoryService;

    @InjectMocks
    PostService postService;

    private final Member member = Member.builder()
            .username("nero")
            .nickname("nero12")
            .password("12345")
            .role(Role.MEMBER)
            .build();

    private final Category category = Category.builder()
            .name("develop")
            .build();

    @DisplayName("게시글 저장이 실패한다 이미 존재함")
    @Test
    void createFail() {
        //given
        PostSaveRequestDto requestDto = PostSaveRequestDto.builder()
                .title("postTitle")
                .content("content")
                .memberId(-2L)
                .categoryId(-3L)
                .build();

        doReturn(Optional.of(requestDto.toEntity()))
                .when(postRepository)
                .findByTitle(requestDto.getTitle());

        //when then
        assertThatThrownBy(()-> postService.create(requestDto))
                .isInstanceOf(PostDuplicatedException.class);
    }

    @DisplayName("게시글을 저장한다")
    @Test
    void createSuccess() {
        //given
        PostSaveRequestDto requestDto = PostSaveRequestDto.builder()
                .title("postTitle")
                .content("content")
                .memberId(-2L)
                .categoryId(-3L)
                .build();

        doReturn(Optional.empty())
                .when(postRepository)
                .findByTitle(requestDto.getTitle());

        doReturn(requestDto.toEntity())
                .when(postRepository)
                .save(any());

        doReturn(member.getId())
                .when(memberService)
                .getMember(any());

        doReturn(category)
                .when(categoryService)
                .getCategory(any());

        //when
        PostResponseDto result = postService.create(requestDto);

        //then
        assertThat(result).isNotNull();
        verify(postRepository, times(1))
                .findByTitle(requestDto.getTitle());
        verify(postRepository, times(1)).save(any());

    }

    @DisplayName("게시글 목록을 조회한다")
    @Test
    void getList() {
        //given
        List<Post> list = Arrays.asList(
                Post.builder().build(),
                Post.builder().build(),
                Post.builder().build());

        doReturn(list)
                .when(postRepository)
                .findAll();

        //when
        List<PostResponseDto> result = postService.getList();


        //then
        assertThat(result.size()).isEqualTo(3);
    }


    @DisplayName("게시글 상세 조회 실패 예외 발생")
    @Test
    void postDetailNull() {
        //given
        doReturn(Optional.empty())
                .when(postRepository)
                .findById(-1L);

        //when then
        assertThatThrownBy(() -> postService.getDetail(-1L))
                .isInstanceOf(PostNotfoundException.class);
    }

    @DisplayName("게시글 상세 조회 성공")
    @Test
    void getDetailSuccess() {
        //given
        Post post = Post.builder()
            .id(-1L)
            .title("postTitle")
            .content("content")
            .member(member)
            .build();

        doReturn(Optional.of(post))
                .when(postRepository)
                .findById(-1L);

        //when
        PostResponseDto result = postService.getDetail(-1L);

        //then
        assertThat(result.getId()).isEqualTo(post.getId());
    }

    @DisplayName("게시글 삭제 실패 존재하지 않는다")
    @Test
    void deleteFail() {
        //given
        doReturn(Optional.empty())
                .when(postRepository)
                .findById(-1L);

        //when then
        assertThatThrownBy(() -> postService.remove(-1L))
                .isInstanceOf(PostNotfoundException.class);
    }

    @DisplayName("게시글 삭제")
    @Test
    void removeSuccess() {
        //given
        Post post = Post.builder()
                .member(member)
                .id(-1L)
                .title("postTitle")
                .content("content")
                .build();

        doReturn(Optional.of(post))
                .when(postRepository)
                .findById(post.getId());

        //when
        postService.remove(post.getId());

        //then
        verify(postRepository, times(1)).findById(post.getId());
        verify(postRepository, times(1)).deleteById(post.getId());
    }

    @DisplayName("게시글 업데이트 실패 존재하지 않는다")
    @Test
    void updateFail() {
        //given
        Post updatePost = Post.builder()
                .member(member)
                .id(-1L)
                .title("postTitle")
                .content("content")
                .build();


        doReturn(Optional.empty())
                .when(postRepository)
                .findById(-1L);

        //when then
        assertThatThrownBy(()-> postService.update(-1L, updatePost))
                .isInstanceOf(PostNotfoundException.class);
    }

    @DisplayName("게시글 업데이트한다")
    @Test
    void updateSuccess() {
        //given
        Post post = Post.builder()
                .member(member)
                .id(-1L)
                .title("postTitle")
                .content("content")
                .build();

        Post updatePost = Post.builder()
                .member(member)
                .id(-1L)
                .title("postTitle")
                .content("content")
                .build();

        doReturn(Optional.of(post))
                .when(postRepository)
                .findById(post.getId());

        //when
        postService.update(post.getId(), updatePost);

        //then
        PostResponseDto detail = postService.getDetail(post.getId());

        assertThat(detail.getTitle()).isEqualTo(updatePost.getTitle());

        verify(postRepository, times(2)).findById(post.getId());
    }
}