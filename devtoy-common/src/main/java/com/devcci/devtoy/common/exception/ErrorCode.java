package com.devcci.devtoy.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 1000, "서버 오류가 발생했습니다."),
    INVALID_FORMAT(HttpStatus.INTERNAL_SERVER_ERROR, 1001, "포맷 에러"),
    API_SERVER_NOT_AVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, 1002, "내부 서버와 연결 중 잠시 후 다시 시도해주세요."),
    NO_RESOURCE_FOUND(HttpStatus.NOT_FOUND, 1003, "존재하지 않는 URL 입니다."),
    METHOD_NOT_SUPPORTED(HttpStatus.METHOD_NOT_ALLOWED, 1004, "지원되지 않는 HTTP Method 입니다."),
    MISSING_REQUEST_HEADER(HttpStatus.BAD_REQUEST, 1005, "필수 헤더가 누락되었습니다."),

    JWT_TOKEN_NOT_EXISTS(HttpStatus.UNAUTHORIZED, 1100, "인증 토큰이 존재하지 않습니다."),
    JWT_TOKEN_INVALID(HttpStatus.UNAUTHORIZED, 1101, "잘못된 토큰입니다."),
    JWT_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, 1102, "만료된 토큰입니다."),
    JWT_CREATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, 1103, "토큰 발행 중 문제가 발생했습니다."),

    NOT_AUTHORIZED(HttpStatus.FORBIDDEN, 1200, "권한이 없습니다."),

    INVALID_PAGING_ORDER(HttpStatus.BAD_REQUEST, 2100, "정렬을 위한 기준값이 잘못됐습니다."),
    BRAND_DUPLICATE(HttpStatus.CONFLICT, 2101, "이미 존재하는 브랜드입니다."),
    BRAND_NOT_FOUND(HttpStatus.BAD_REQUEST, 2102, "존재하지 않는 브랜드입니다."),
    BRAND_NOT_CHANGED(HttpStatus.OK, 2103, "브랜드의 변경 내용이 없습니다."),
    BRAND_LIST_NOT_LOADED(HttpStatus.INTERNAL_SERVER_ERROR, 2151, "브랜드 목록 조회에 오류가 있습니다."),
    BRAND_LOWEST_PRICE_LIST_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 2152,
        "브랜드 최저가 목록 조회에 오류가 있습니다."),

    CATEGORY_NOT_FOUND(HttpStatus.BAD_REQUEST, 2202, "존재하지 않는 카테고리입니다."),
    CATEGORY_LOWEST_PRICE_PRODUCT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 2251,
        "카테고리 최저가 상품 조회에 오류가 있습니다."),
    CATEGORY_HIGHEST_PRICE_PRODUCT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 2251,
        "카테고리 최고가 상품 조회에 오류가 있습니다."),

    PRODUCT_DUPLICATE(HttpStatus.CONFLICT, 2301, "해당 브랜드의 카테고리에는 이미 상품이 존재합니다."),
    PRODUCT_NOT_FOUND(HttpStatus.BAD_REQUEST, 2302, "존재하지 않는 상품입니다."),
    PRODUCT_NOT_CHANGED(HttpStatus.OK, 2303, "상품의 변경 내용이 없습니다."),
    PRODUCT_LIST_NOT_LOADED(HttpStatus.INTERNAL_SERVER_ERROR, 2351, "상품 목록 조회에 오류가 있습니다."),
    PRODUCT_STOCK_QUANTITY_INVALID(HttpStatus.BAD_REQUEST, 2370, "잘못된 상품 재고 값 입니다."),
    PRODUCT_STOCK_NOT_ENOUGH(HttpStatus.CONFLICT, 2371, "상품 재고가 부족합니다."),
    PRODUCT_PRICE_INVALID(HttpStatus.BAD_REQUEST, 2372, "잘못된 상품 가격 가격 입니다."),

    ORDER_NOT_FOUND(HttpStatus.BAD_REQUEST, 2400, "존재하지 않는 주문요청입니다."),

    MEMBER_ID_ALREADY_EXISTS(HttpStatus.CONFLICT, 4001, "이미 등록된 사용자 ID 입니다."),
    MEMBER_EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, 4002, "이미 등록된 이메일입니다."),
    MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, 4003, "존재하지 않는 사용자입니다."),
    MEMBER_PASSWORD_INCORRECT(HttpStatus.BAD_REQUEST, 4004, "잘못된 패스워드입니다."),

    HTTP_CLIENT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 9000, "클라이언트에 문제가 있습니다. 점검이 필요합니다."),
    INVALID_HTTP_REQUEST(HttpStatus.BAD_REQUEST, 9001, "내부 서버 요청 입력값 오류"),
    HTTP_CLIENT_NOT_FOUND(HttpStatus.NOT_FOUND, 9002, "내부 서버 연결에 문제가 있습니다."),
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
