package com.blackMarket.curation.domain.post.exception;

import com.blackMarket.curation.global.error.exception.GlobalException;

public class PostDuplicatedException extends GlobalException {
    private static final String MESSAGE = "게시글 제목이 중복됩니다.";

    public PostDuplicatedException() {
        super(MESSAGE);
    }
    @Override
    public int getStatusCode() {
        return 404;
    }
}
