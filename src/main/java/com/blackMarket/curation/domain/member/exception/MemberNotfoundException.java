package com.blackMarket.curation.domain.member.exception;

public class MemberNotfoundException extends GlobalException {
    private static final String MESSAGE = "존재하지 않는 멤버입니다.";

    public MemberNotfoundException() {
        super(MESSAGE);
    }
    @Override
    public int getStatusCode() {
        return 404;
    }
}
