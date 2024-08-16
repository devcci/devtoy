create table member
(
    id          bigint       not null auto_increment,
    created_at  datetime(6),
    created_by  varchar(255),
    modified_at datetime(6),
    modified_by varchar(255),
    email       varchar(255) not null,
    member_id   varchar(255) not null,
    name        varchar(255) not null,
    password    varchar(255) not null comment '암호화',
    primary key (id)
) engine = InnoDB
  DEFAULT CHARACTER SET utf8
  COLLATE utf8_general_ci;

create table member_role
(
    id   bigint not null,
    role enum ('ADMIN','MEMBER')
) engine = InnoDB
  DEFAULT CHARACTER SET utf8
  COLLATE utf8_general_ci;


alter table member
    add constraint member_id_uq unique (member_id);

alter table member_role
    add constraint FKncb51xmt7bt77kxy2xsite0wd
        foreign key (id)
            references member (id);