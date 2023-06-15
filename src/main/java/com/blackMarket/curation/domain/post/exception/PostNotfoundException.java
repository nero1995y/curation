package com.blackMarket.curation.domain.post.exception;

import com.blackMarket.curation.global.error.exception.GlobalException;

public class PostNotfoundException extends GlobalException {
    private static final String MESSAGE = "존재하지 않는 게시글입니다.";

    public PostNotfoundException() {
        super(MESSAGE);
    }
    @Override
    public int getStatusCode() {
        return 404;
    }
}
