package com.devcci.devtoy.product.domain.product.event;

import java.util.List;

public record ProductBulkDeletionEvent(List<Long> list) {

}
