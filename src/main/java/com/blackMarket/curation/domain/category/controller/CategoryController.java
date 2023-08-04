package com.blackMarket.curation.domain.category.controller;

import com.blackMarket.curation.domain.category.dto.CategoryResponseDto;
import com.blackMarket.curation.domain.category.dto.CategorySaveRequestDto;
import com.blackMarket.curation.domain.category.dto.CategoryUpdateRequestDto;
import com.blackMarket.curation.domain.category.servcie.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/api/category")
    public ResponseEntity<CategoryResponseDto> create(
            @RequestBody @Valid CategorySaveRequestDto categorySaveRequestDto) {

        CategoryResponseDto response = categoryService.create(categorySaveRequestDto.toEntity());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/categories")
    public ResponseEntity<List<CategoryResponseDto>> getList() {
        return ResponseEntity.ok(categoryService.getList());
    }

    @GetMapping("/api/category/{id}")
    public ResponseEntity<CategoryResponseDto> getDetail(@PathVariable Long id) {
        return ResponseEntity.ok( categoryService.getDetail(id));
    }

    @DeleteMapping("/api/category/{id}")
    public ResponseEntity<Void> remove(@PathVariable Long id) {
        categoryService.remove(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/api/category/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id,
                                       @RequestBody @Valid CategoryUpdateRequestDto categoryUpdateRequestDto) {

        categoryService.update(id, categoryUpdateRequestDto.toEntity());

        return ResponseEntity.noContent().build();
    }

}
