package com.blackMarket.curation.member.controller;

import com.blackMarket.curation.member.dto.MemberRequestDto;
import com.blackMarket.curation.member.dto.MemberResponseDto;
import com.blackMarket.curation.member.serivce.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("api/members")
    public ResponseEntity<MemberResponseDto> createMember(
            @RequestBody @Valid MemberRequestDto memberRequestDto) {

        memberService.create(memberRequestDto.toEntity());

        return ResponseEntity.ok().build();
    }
}
