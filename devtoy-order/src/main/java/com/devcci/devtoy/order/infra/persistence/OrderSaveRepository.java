package com.devcci.devtoy.order.infra.persistence;

import com.devcci.devtoy.order.domain.order.Order;

public interface OrderSaveRepository {

    void saveWithBulk(Order order);
}
