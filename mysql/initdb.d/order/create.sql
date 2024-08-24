create table orders
(
    id          bigint auto_increment primary key,
    member_id   varchar(255)                                         not null,
    status      enum ('CREATED', 'ORDERED', 'CANCELED', 'COMPLETED') not null,
    total_price decimal(15, 0)                                       not null,
    created_at  datetime(6)                                          null,
    modified_at datetime(6)                                          null
) engine = InnoDB
  DEFAULT CHARACTER SET utf8
  COLLATE utf8_general_ci;

create table order_product
(
    id          bigint auto_increment primary key,
    order_id    bigint         null,
    product_id  bigint         not null,
    quantity    bigint         not null,
    price       decimal(15, 0) not null,
    created_at  datetime(6)    null,
    modified_at datetime(6)    null
) engine = InnoDB
  DEFAULT CHARACTER SET utf8
  COLLATE utf8_general_ci;

alter table order_product
    add constraint order_product_uq unique (order_id, product_id);

alter table order_product
    add constraint order_fk
        foreign key (order_id)
            references orders (id);
