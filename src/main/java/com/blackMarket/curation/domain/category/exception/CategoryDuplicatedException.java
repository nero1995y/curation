package com.blackMarket.curation.domain.category.exception;

import com.blackMarket.curation.global.error.exception.GlobalException;

public class CategoryDuplicatedException extends GlobalException {
    private static final String MESSAGE = "카테고리 이름이 중복됩니다.";

    public CategoryDuplicatedException() {
        super(MESSAGE);
    }
    @Override
    public int getStatusCode() {
        return 404;
    }
}
