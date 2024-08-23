create table orders
(
    id          bigint auto_increment primary key,
    member_id   bigint                                  not null,
    status      enum ('CANCELED', 'ORDERED', 'PENDING') null,
    created_at  datetime(6)                             null,
    modified_at datetime(6)                             null
) engine = InnoDB
  DEFAULT CHARACTER SET utf8
  COLLATE utf8_general_ci;

create table order_product
(
    id               bigint auto_increment primary key,
    product_id       bigint           not null,
    product_quantity bigint default 1 not null,
    order_id         bigint           null,
    created_at       datetime(6)      null,
    modified_at      datetime(6)      null,
    constraint product_order_fk
        foreign key (order_id) references orders (id)
) engine = InnoDB
  DEFAULT CHARACTER SET utf8
  COLLATE utf8_general_ci;

