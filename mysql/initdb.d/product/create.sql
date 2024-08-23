create table brand
(
    created_at  datetime(6),
    id          bigint      not null auto_increment,
    modified_at datetime(6),
    name        varchar(20) not null,
    primary key (id)
) engine = InnoDB
  DEFAULT CHARACTER SET utf8
  COLLATE utf8_general_ci;

create table category
(
    created_at  datetime(6),
    id          bigint      not null auto_increment,
    modified_at datetime(6),
    name        varchar(20) not null,
    primary key (id)
) engine = InnoDB
  DEFAULT CHARACTER SET utf8
  COLLATE utf8_general_ci;

create table product
(
    price          decimal(15, 0) not null,
    brand_id       bigint,
    category_id    bigint,
    created_at     datetime(6),
    id             bigint         not null auto_increment,
    modified_at    datetime(6),
    description    varchar(255) comment '상품의 간략한 설명',
    name           varchar(255),
    stock_quantity bigint         not null,
    primary key (id)
) engine = InnoDB
  DEFAULT CHARACTER SET utf8
  COLLATE utf8_general_ci;

alter table brand
    add constraint brand_name_uq unique (name);

alter table category
    add constraint category_name_uq unique (name);

create index category_price_idx
    on product (category_id, price);

alter table product
    add constraint product_brand_category_uq unique (name, brand_id, category_id);

alter table product
    add constraint product_brand_fk
        foreign key (brand_id)
            references brand (id);

alter table product
    add constraint product_category_fk
        foreign key (category_id)
            references category (id);
