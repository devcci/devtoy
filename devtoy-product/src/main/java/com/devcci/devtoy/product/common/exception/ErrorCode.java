package com.devcci.devtoy.product.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 1500, "서버 오류가 발생했습니다."),
    INVALID_FORMAT(HttpStatus.INTERNAL_SERVER_ERROR, 1501, "포맷 에러"),
    INVALID_PAGING_ORDER(HttpStatus.BAD_REQUEST, 1950, "정렬을 위한 기준값이 잘못됐습니다."),

    BRAND_DUPLICATE(HttpStatus.CONFLICT, 1101, "이미 존재하는 브랜드입니다."),
    BRAND_NOT_FOUND(HttpStatus.BAD_REQUEST, 1102, "존재하지 않는 브랜드입니다."),
    BRAND_NOT_CHANGED(HttpStatus.OK, 1103, "브랜드의 변경 내용이 없습니다."),
    BRAND_LIST_NOT_LOADED(HttpStatus.INTERNAL_SERVER_ERROR, 1151, "브랜드 목록 조회에 오류가 있습니다."),
    BRAND_LOWEST_PRICE_LIST_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 1152,
        "브랜드 최저가 목록 조회에 오류가 있습니다."),

    CATEGORY_NOT_FOUND(HttpStatus.BAD_REQUEST, 1202, "존재하지 않는 카테고리입니다."),
    CATEGORY_LOWEST_PRICE_PRODUCT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 1251,
        "카테고리 최저가 상품 조회에 오류가 있습니다."),
    CATEGORY_HIGHEST_PRICE_PRODUCT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 1251,
        "카테고리 최고가 상품 조회에 오류가 있습니다."),

    PRODUCT_DUPLICATE(HttpStatus.CONFLICT, 1301, "해당 브랜드의 카테고리에는 이미 상품이 존재합니다."),
    PRODUCT_NOT_FOUND(HttpStatus.BAD_REQUEST, 1302, "존재하지 않는 상품입니다."),
    PRODUCT_NOT_CHANGED(HttpStatus.OK, 1303, "상품의 변경 내용이 없습니다."),

    PRODUCT_LIST_NOT_LOADED(HttpStatus.INTERNAL_SERVER_ERROR, 1351, "상품 목록 조회에 오류가 있습니다."),
    ;

    ErrorCode(HttpStatus status, int code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    private final HttpStatus status;
    private final int code;
    private final String message;
}
