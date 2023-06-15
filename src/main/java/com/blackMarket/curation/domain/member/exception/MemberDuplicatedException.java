package com.blackMarket.curation.domain.member.exception;

import com.blackMarket.curation.global.error.exception.GlobalException;

public class MemberDuplicatedException extends GlobalException {
    private static final String MESSAGE = "member id 가 중복됩니다.";

    public MemberDuplicatedException() {
        super(MESSAGE);
    }
    @Override
    public int getStatusCode() {
        return 404;
    }
}
