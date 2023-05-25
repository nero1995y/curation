package com.blackMarket.curation.domain.member.exception;

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
