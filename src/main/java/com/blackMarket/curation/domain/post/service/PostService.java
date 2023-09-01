package com.blackMarket.curation.domain.post.service;

import com.blackMarket.curation.domain.category.domain.Category;
import com.blackMarket.curation.domain.category.servcie.CategoryService;
import com.blackMarket.curation.domain.member.domain.Member;
import com.blackMarket.curation.domain.member.exception.MemberNotfoundException;
import com.blackMarket.curation.domain.member.repository.MemberRepository;
import com.blackMarket.curation.domain.member.serivce.MemberService;
import com.blackMarket.curation.domain.post.domain.Post;
import com.blackMarket.curation.domain.post.dto.PostResponseDto;
import com.blackMarket.curation.domain.post.dto.PostSaveRequestDto;
import com.blackMarket.curation.domain.post.exception.PostDuplicatedException;
import com.blackMarket.curation.domain.post.exception.PostNotfoundException;
import com.blackMarket.curation.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final MemberService memberService;
    private final CategoryService categoryService;

    @Transactional
    public PostResponseDto create(PostSaveRequestDto postSaveRequestDto) {

        Post post = postSaveRequestDto.toEntity();
        Long memberId = postSaveRequestDto.getMemberId();
        Long categoryId = postSaveRequestDto.getCategoryId();

        Member member = memberService.getMember(memberId);
        post.changeMember(member);

        Category category = categoryService.getCategory(categoryId);
        post.changeCategory(category);

        postRepository
                .findByTitle(post.getTitle())
                .ifPresent(post1 -> {
                   throw new PostDuplicatedException();
                });

        Post save = postRepository.save(post);

        return new PostResponseDto(save);
    }

    public Page<PostResponseDto> getList(Pageable pageable) {

        Page<Post> list = postRepository.findAll(pageable);


        return list.map(post -> PostResponseDto.builder()
                        .id(post.getId())
                        .title(post.getTitle())
                        .content(post.getContent())
                        .modifiedDate(post.getModifiedDate())
                        .createDate(post.getCreateDate())
                        .build());
    }

    public PostResponseDto getDetail(Long postId) {

        Post post = postRepository.findById(postId)
                .orElseThrow(PostNotfoundException::new);

        return PostResponseDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .modifiedDate(post.getModifiedDate())
                .createDate(post.getCreateDate())
                .build();
    }

    @Transactional
    public void remove(Long memberId) {
        Post post = postRepository.findById(memberId)
                .orElseThrow(PostNotfoundException::new);

        postRepository.deleteById(post.getId());
    }

    @Transactional
    public void update(Long memberId, Post updatePost) {
        Post post = postRepository.findById(memberId)
                .orElseThrow(PostNotfoundException::new);

        post.update(updatePost);
    }

}
