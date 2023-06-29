package com.blackMarket.curation.domain.post.controller;

import com.blackMarket.curation.domain.post.dto.PostResponseDto;
import com.blackMarket.curation.domain.post.dto.PostSaveRequestDto;
import com.blackMarket.curation.domain.post.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/api/post")
    public ResponseEntity<PostResponseDto> create(
            @RequestBody @Valid PostSaveRequestDto postSaveRequestDto) {

        PostResponseDto response = postService.create(postSaveRequestDto.toEntity());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/posts")
    public ResponseEntity<List<PostResponseDto>> getList() {
        return ResponseEntity.ok(postService.getList());
    }
}
