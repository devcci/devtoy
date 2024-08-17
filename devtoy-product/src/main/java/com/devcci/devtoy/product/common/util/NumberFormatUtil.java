package com.devcci.devtoy.product.common.util;

import com.devcci.devtoy.common.exception.ApiException;
import com.devcci.devtoy.common.exception.ErrorCode;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class NumberFormatUtil {
    private NumberFormatUtil() {
    }

    private static final String PATTERN = "###,###";

    public static String withComma(BigDecimal number) {
        DecimalFormat formatter = new DecimalFormat(PATTERN);
        try {
            return formatter.format(number);
        } catch (IllegalArgumentException e) {
            throw new ApiException(ErrorCode.INVALID_FORMAT);
        }
    }
}
