package com.devcci.devtoy.product.common.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import static org.assertj.core.api.Assertions.assertThat;

class PageableUtilTest {

    @DisplayName("페이지 생성 - 정렬")
    @Test
    void createPageableWithSort() {
        // when
        Pageable pageable = PageableUtil.createPageable(1, 10, "ASC");

        // then
        assertThat(pageable).isNotNull();
        assertThat(pageable.getPageNumber()).isZero();
        assertThat(pageable.getPageSize()).isEqualTo(10);
        assertThat(pageable.getSort()).isEqualTo(Sort.by(Sort.Direction.ASC, "id"));
    }

    @DisplayName("페이지 생성 - 정렬 제외")
    @Test
    void createPageableWithoutSort() {
        // when
        Pageable pageable = PageableUtil.createPageable(1, 10, null);

        // then
        assertThat(pageable).isNotNull();
        assertThat(pageable.getPageNumber()).isZero();
        assertThat(pageable.getPageSize()).isEqualTo(10);
        assertThat(pageable.getSort().isSorted()).isFalse();
    }
}