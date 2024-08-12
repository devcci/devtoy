package com.devcci.devtoy.product.common.util;

import java.math.BigDecimal;

public class BigDecimalUtil {
    private BigDecimalUtil() {
    }

    public static boolean equals(BigDecimal a, BigDecimal b) {
        if (a == null || b == null) {
            return false;
        }
        return a.compareTo(b) == 0;
    }
}
