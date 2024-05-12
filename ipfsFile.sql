create table ipfsfile
(
    id            bigint auto_increment comment 'ID'
        primary key,
    HashCode      varchar(255)                       null comment '文件Hash值',
    userID        bigint                             null comment '用户ID',
    secretKey     varchar(255)                       null comment '共享密钥',
    Create_Time   datetime default CURRENT_TIMESTAMP null comment '创建时间',
    Modified_Time datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '修改时间',
    isDelete      int      default 0                 not null comment '逻辑删除'
);