-- security1.sys_group definition
drop table if exists sys_group;
CREATE TABLE `sys_group`
(
    `id`   bigint(20) NOT NULL AUTO_INCREMENT,
    `code` varchar(64)  NOT NULL,
    `name` varchar(128) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `sys_group_unique` (`code`),
    UNIQUE KEY `sys_group_unique_1` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='组别';


-- security1.sys_user_group definition
drop table if exists sys_user_group;
CREATE TABLE `sys_user_group`
(
    `group_id` bigint(20) NOT NULL,
    `user_id`  bigint(20) NOT NULL,
    PRIMARY KEY (`group_id`, `user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户和组别关联';


-- 访问控制诸表
-- 网络层访问控制
drop table if exists `access_ctrl_network_tcp`;
create table `access_ctrl_network_tcp`
(
    id          bigint(20)      not null auto_increment    comment 'ID',
    source_ip   varchar(64) not null comment '源Ip',
    source_port int(5)          not null                   comment '源端口',
    target_ip   varchar(64) not null comment '目的Ip',
    target_port int(5)          not null                   comment '目的端口',
    status      char(1)     not null comment '状态（0正常 1停用）',
    create_by   varchar(64)  default '' comment '创建者',
    create_time datetime comment '创建时间',
    update_by   varchar(64)  default '' comment '更新者',
    update_time datetime comment '更新时间',
    remark      varchar(500) default null comment '备注',
    primary key (id)
) engine=innodb comment = '网络层访问控制';
-- 应用层访问控制
-- HTTP访问控制
drop table if exists `access_ctrl_appl_http`;
create table `access_ctrl_appl_http`
(
    id          bigint(20)      not null auto_increment    comment 'ID',
    server_name varchar(64) not null comment '服务名称',
    port        int(5)          not null                   comment '端口号',
    status      char(1)     not null comment '状态（0正常 1停用）',
    create_by   varchar(64)  default '' comment '创建者',
    create_time datetime comment '创建时间',
    update_by   varchar(64)  default '' comment '更新者',
    update_time datetime comment '更新时间',
    remark      varchar(500) default null comment '备注',
    primary key (id)
) engine=innodb comment = '应用层访问控制-HTTP';
-- FTP访问控制
drop table if exists `access_ctrl_appl_ftp`;
create table `access_ctrl_appl_ftp`
(
    id          bigint(20)      not null auto_increment    comment 'ID',
    server_name varchar(64) not null comment '服务名称',
    port        int(5)          not null                   comment '端口号',
    directory   varchar(64) not null comment '目录',
    status      char(1)     not null comment '状态（0正常 1停用）',
    create_by   varchar(64)  default '' comment '创建者',
    create_time datetime comment '创建时间',
    update_by   varchar(64)  default '' comment '更新者',
    update_time datetime comment '更新时间',
    remark      varchar(500) default null comment '备注',
    primary key (id)
) engine=innodb comment = '应用层访问控制-FTP';
-- Telnet访问控制
drop table if exists `access_ctrl_appl_telnet`;
create table `access_ctrl_appl_telnet`
(
    id          bigint(20)      not null auto_increment    comment 'ID',
    server_name varchar(64) not null comment '服务名称',
    port        int(5)          not null                   comment '端口号',
    status      char(1)     not null comment '状态（0正常 1停用）',
    create_by   varchar(64)  default '' comment '创建者',
    create_time datetime comment '创建时间',
    update_by   varchar(64)  default '' comment '更新者',
    update_time datetime comment '更新时间',
    remark      varchar(500) default null comment '备注',
    primary key (id)
) engine=innodb comment = '应用层访问控制-Telnet';

-- 工业协议访问控制-Modbus访问控制
drop table if exists `access_ctrl_indu_modbus`;
-- Modbus访问控制表
drop table if exists `access_ctrl_indu_modbus`;
create table `access_ctrl_indu_modbus`
(
    id               bigint(20)      not null auto_increment    comment 'ID',
    device_id        varchar(64)  not null comment '设备ID',
    function_code    varchar(20)  not null comment '功能码类型',
    rw               char(1)      not null comment '读写操作（0读 1写）',
    register_address varchar(128) not null comment '寄存器地址',
    value_range      varchar(128) not null comment '控制值范围',
    status           char(1)      not null comment '状态（0正常 1停用）',
    create_by        varchar(64)  default '' comment '创建者',
    create_time      datetime comment '创建时间',
    update_by        varchar(64)  default '' comment '更新者',
    update_time      datetime comment '更新时间',
    remark           varchar(500) default null comment '备注',
    primary key (id),
    unique key `uniq_device_register` (device_id, register_address)
) engine=innodb comment = 'Modbus访问控制';




ALTER TABLE security1.access_ctrl_appl_ftp CHANGE port source_port int NOT NULL COMMENT '源端口号';
ALTER TABLE security1.access_ctrl_appl_ftp MODIFY COLUMN source_port int NOT NULL COMMENT '源端口号';
ALTER TABLE security1.access_ctrl_appl_ftp ADD source_ip varchar(100) NULL;
ALTER TABLE security1.access_ctrl_appl_ftp ADD source_port INT NULL;
ALTER TABLE security1.access_ctrl_appl_ftp ADD target_ip varchar(100) NULL COMMENT '目的IP';
ALTER TABLE security1.access_ctrl_appl_ftp ADD target_port INT NULL COMMENT '目标端口';

ALTER TABLE security1.access_ctrl_appl_http CHANGE port source_port int NOT NULL COMMENT '源端口号';
ALTER TABLE security1.access_ctrl_appl_http MODIFY COLUMN source_port int NOT NULL COMMENT '源端口号';
ALTER TABLE security1.access_ctrl_appl_http ADD source_ip varchar(100) NULL;
ALTER TABLE security1.access_ctrl_appl_http ADD source_port INT NULL;
ALTER TABLE security1.access_ctrl_appl_http ADD target_ip varchar(100) NULL COMMENT '目的IP';
ALTER TABLE security1.access_ctrl_appl_http ADD target_port INT NULL COMMENT '目标端口';

ALTER TABLE security1.access_ctrl_appl_telnet CHANGE port source_port int NOT NULL COMMENT '源端口号';
ALTER TABLE security1.access_ctrl_appl_telnet MODIFY COLUMN source_port int NOT NULL COMMENT '源端口号';
ALTER TABLE security1.access_ctrl_appl_telnet ADD source_ip varchar(100) NULL;
ALTER TABLE security1.access_ctrl_appl_telnet ADD source_port INT NULL;
ALTER TABLE security1.access_ctrl_appl_telnet ADD target_ip varchar(100) NULL COMMENT '目的IP';
ALTER TABLE security1.access_ctrl_appl_telnet ADD target_port INT NULL COMMENT '目标端口';
