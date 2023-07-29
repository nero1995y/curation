package com.blackMarket.curation.domain.post.service;

import com.blackMarket.curation.domain.member.domain.Member;
import com.blackMarket.curation.domain.member.exception.MemberNotfoundException;
import com.blackMarket.curation.domain.member.repository.MemberRepository;
import com.blackMarket.curation.domain.member.serivce.MemberService;
import com.blackMarket.curation.domain.post.domain.Post;
import com.blackMarket.curation.domain.post.dto.PostResponseDto;
import com.blackMarket.curation.domain.post.exception.PostDuplicatedException;
import com.blackMarket.curation.domain.post.exception.PostNotfoundException;
import com.blackMarket.curation.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
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

    @Transactional
    public PostResponseDto create(Post post, Long memberId) {

        Member member = memberService.getMember(memberId);
        post.changeMember(member);

        postRepository
                .findByTitle(post.getTitle())
                .ifPresent(post1 -> {
                   throw new PostDuplicatedException();
                });

        Post save = postRepository.save(post);

        return new PostResponseDto(save);
    }

    public List<PostResponseDto> getList() {

        List<Post> list = postRepository.findAll();

        return list.stream()
                .map(post -> PostResponseDto.builder()
                        .id(post.getId())
                        .title(post.getTitle())
                        .content(post.getContent())
                        .modifiedDate(post.getModifiedDate())
                        .createDate(post.getCreateDate())
                        .build()
                )
                .collect(Collectors.toList());
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
