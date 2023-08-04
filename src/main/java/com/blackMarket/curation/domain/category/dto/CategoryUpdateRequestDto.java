package com.blackMarket.curation.domain.category.dto;

import com.blackMarket.curation.domain.category.domain.Category;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CategoryUpdateRequestDto {

    @NotNull
    private String name;

    @Builder
    public CategoryUpdateRequestDto(String name) {
        this.name = name;
    }

    public Category toEntity() {
        return Category.builder()
                .name(this.name)
                .build();
    }
}
