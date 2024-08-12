package com.devcci.devtoy.product.common.util;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.stream.Stream;

public enum PageSortType {
    ASC, DESC;

    @JsonCreator
    public static PageSortType of(String sortType) {
        return Stream.of(PageSortType.values())
            .filter(type -> type.toString().equals(sortType.toUpperCase()))
            .findFirst()
            .orElse(null);
    }
}
