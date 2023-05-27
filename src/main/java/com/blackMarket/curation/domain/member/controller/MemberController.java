package com.blackMarket.curation.domain.member.controller;

import com.blackMarket.curation.domain.member.dto.MemberRequestDto;
import com.blackMarket.curation.domain.member.dto.MemberResponseDto;
import com.blackMarket.curation.domain.member.serivce.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("api/member")
    public ResponseEntity<MemberResponseDto> createMember(
            @RequestBody @Valid MemberRequestDto memberRequestDto) {

        MemberResponseDto response = memberService.create(memberRequestDto.toEntity());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/members")
    public ResponseEntity<List<MemberResponseDto>> getList() {
        return ResponseEntity.ok(memberService.getList());
    }

    @GetMapping("/api/member/{id}")
    public ResponseEntity<MemberResponseDto> getDetail(
            @PathVariable Long id) {

        return ResponseEntity.ok(memberService.getDetail(id));
    }
}
