package com.blackMarket.curation.domain.category.exception;

import com.blackMarket.curation.global.error.exception.GlobalException;

public class CategoryNotFountException extends GlobalException {

    private static final String MESSAGE = "카테고리가 존재하지 않습니다.";

    public CategoryNotFountException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}
