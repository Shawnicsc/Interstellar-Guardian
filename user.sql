create table user
(
    id            bigint auto_increment comment '用户ID'
        primary key,
    userName      varchar(255)                       null comment '用户名',
    userPassword  varchar(255)                       null comment '登录密码',
    CREATE_TIME   datetime default CURRENT_TIMESTAMP null comment '创建时间',
    MODIFIED_TIME datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '修改时间',
    isDelete      int      default 0                 not null comment '逻辑删除',
    token         varchar(255)                       null
);