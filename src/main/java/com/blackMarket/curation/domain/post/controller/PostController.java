package com.blackMarket.curation.domain.post.controller;

import com.blackMarket.curation.domain.post.dto.PostResponseDto;
import com.blackMarket.curation.domain.post.dto.PostSaveRequestDto;
import com.blackMarket.curation.domain.post.dto.PostUpdateRequestDto;
import com.blackMarket.curation.domain.post.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/api/post")
    public ResponseEntity<PostResponseDto> create(@RequestBody @Valid PostSaveRequestDto postSaveRequestDto) {

        PostResponseDto response = postService.create(postSaveRequestDto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/posts")
    public ResponseEntity<List<PostResponseDto>> getList() {
        return ResponseEntity.ok(postService.getList());
    }

    @GetMapping("/api/post/{id}")
    public ResponseEntity<PostResponseDto> getDetail(@PathVariable Long id) {
        return ResponseEntity.ok(postService.getDetail(id));
    }

    @DeleteMapping("/api/post/{id}")
    public ResponseEntity<Void> remove(@PathVariable Long id) {
        postService.remove(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/api/post/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id,
                                       @RequestBody PostUpdateRequestDto requestDto) {
        postService.update(id, requestDto.toEntity());
        return ResponseEntity.noContent().build();
    }
}
