package com.devcci.devtoy.product.common.util;


import com.devcci.devtoy.product.common.exception.ApiException;
import com.devcci.devtoy.product.common.exception.ErrorCode;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

public class PageableUtil {

    private PageableUtil() {
    }

    public static Pageable createPageable(Integer page, Integer size, String sort) {
        Pageable pageable;
        Direction direction;
        if (sort != null) {
            try {
                direction = Direction.valueOf(sort.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new ApiException(ErrorCode.INVALID_PAGING_ORDER);
            }
            pageable = PageRequest.of(page - 1, size,
                Sort.by(direction, "id"));
        } else {
            pageable = PageRequest.of(page - 1, size);

        }
        return pageable;
    }
}