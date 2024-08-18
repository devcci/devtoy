package com.devcci.devtoy.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 1000, "서버 오류가 발생했습니다."),
    INVALID_FORMAT(HttpStatus.INTERNAL_SERVER_ERROR, 1001, "포맷 에러"),
    API_SERVER_NOT_AVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, 1002, "API 서버 오류입니다."),
    NO_RESOURCE_FOUND(HttpStatus.NOT_FOUND, 1003, "존재하지 않는 URL 입니다."),

    JWT_TOKEN_NOT_EXISTS(HttpStatus.UNAUTHORIZED, 1004, "인증 토큰이 존재하지 않습니다."),
    JWT_TOKEN_INVALID(HttpStatus.UNAUTHORIZED, 1005, "잘못된 토큰입니다."),
    JWT_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, 1006, "만료된 토큰입니다."),
    JWT_CREATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, 1007, "토큰 발행 중 문제가 발생했습니다."),

    NOT_AUTHORIZED(HttpStatus.FORBIDDEN, 1501, "권한이 없습니다."),

    INVALID_PAGING_ORDER(HttpStatus.BAD_REQUEST, 1100, "정렬을 위한 기준값이 잘못됐습니다."),
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

    MEMBER_ID_ALREADY_EXISTS(HttpStatus.CONFLICT, 2001, "이미 등록된 사용자 ID 입니다."),
    MEMBER_EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, 2002, "이미 등록된 이메일입니다."),
    MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, 2003, "존재하지 않는 사용자입니다."),
    MEMBER_PASSWORD_INCORRECT(HttpStatus.BAD_REQUEST, 2004, "잘못된 패스워드입니다."),

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
