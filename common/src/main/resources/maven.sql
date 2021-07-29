-- 建库
drop database if exists maven;
create database maven character set utf8mb4 collate utf8mb4_general_ci;
use maven;

-- user 表
drop table if exists user;
create table user(
    id bigint primary key comment '主键',
    email varchar(128) not null comment '电子邮箱',
	user_name varchar(32) not null comment '用户名',
	user_pass char(64) not null comment '密码',
    salt char(64) not null comment '盐值',
	age int not null default 0 comment '年龄',
	height double not null default 0 comment '身高',
    avatar varchar(128) not null default '' comment '头像', 
	register_time timestamp not null default current_timestamp comment '注册时间',
    last_update_time timestamp not null default current_timestamp on update current_timestamp comment '上次更新时间',
	login_time datetime not null default now() comment '上次登录时间',
    status tinyint not null default 0 comment '状态',
    role_id bigint not null default 0 comment '角色 id',
	deleted bit not null default 0 comment '删除标记',
	constraint unq_email unique(email),
    constraint unq_user_name unique(user_name)
);
insert into user(id, email, user_name, user_pass, salt, age, height, role_id) values 
(1, 'cat@hao.com', 'cat', sha2(concat('cat', sha2('cat', 256)), 256), sha2('cat', 256), 11, 181.5, 1),
(2, 'dog@hao.com', 'dog', sha2(concat('dog', sha2('dog', 256)), 256), sha2('dog', 256), 12, 182.3, 2),
(3, 'fox@hao.com', 'fox', sha2(concat('fox', sha2('fox', 256)), 256), sha2('fox', 256), 13, 183.5, 3),
(4, 'pig@hao.com', 'pig', sha2(concat('pig', sha2('pig', 256)), 256), sha2('pig', 256), 14, 184.5, 1),
(5, 'cow@hao.com', 'cow', sha2(concat('cow', sha2('cow', 256)), 256), sha2('cow', 256), 15, 185.5, 2),
(6, 'rat@hao.com', 'rat', sha2(concat('rat', sha2('rat', 256)), 256), sha2('rat', 256), 16, 186.5, 3),
(7, 'ant@hao.com', 'ant', sha2(concat('ant', sha2('ant', 256)), 256), sha2('ant', 256), 17, 187.5, 0);


-- 角色表
drop table if exists role;
create table role (
    id bigint primary key comment '主键',
    name varchar(30) not null comment '角色名称',
    constraint unq_name unique(name)
);
insert into role(id, name) values 
(1, 'admin'),
(2, 'manager'),
(3, 'member');

-- 权限表
drop table if exists perm;
create table perm (
    id bigint primary key,
    name varchar(30) not null comment '权限或权限类别名称',
    tag varchar(30) not null comment '英文标记，程序中使用，类别是路径',
    pid bigint not null default 0 comment '所属权限类别',
    nav bit not null default 0 comment '是否导航栏用到',
    key idx_name (name),
    key idx_pid (pid)
);
insert into perm(id, name, tag, pid, nav) values 
(1, '所有权限', '*', 0, 1),
(2, '角色管理', '/role', 0, 1),
(201, '新增角色', 'item:add', 2, 0),
(202, '删除角色', 'item:del', 2, 0),
(203, '更新角色', 'item:update', 2, 0),
(204, '查询单个角色', 'item:query', 2, 0),
(205, '批量查询角色', 'item:list', 2, 0),
(3, '用户管理', '/user', 0, 1),
(301, '新增用户', 'user:add', 3, 0),
(302, '删除用户', 'user:del', 3, 0),
(303, '更新用户', 'user:update', 3, 0),
(304, '查询单个用户', 'user:query', 3, 0),
(305, '批量查询用户', 'user:list', 3, 0),
(4, '类别管理', '/category', 0, 1),
(401, '新增类别', 'category:add', 4, 0),
(402, '删除类别', 'category:del', 4, 0),
(403, '更新类别', 'category:update', 4, 0),
(404, '查询单个类别', 'category:query', 4, 0),
(405, '批量查询类别', 'category:list', 4, 0),
(5, '商品管理', '/item', 0, 1),
(501, '新增商品', 'item:add', 5, 0),
(502, '删除商品', 'item:del', 5, 0),
(503, '更新商品', 'item:update', 5, 0),
(504, '查询单个商品', 'item:query', 5, 0),
(505, '批量查询商品', 'item:list', 5, 0),
(6, '图形图表', '/chart', 0, 1),
(601, '柱形图', '/chart/bar', 6, 1),
(602, '折线图', '/chart/line', 6, 1),
(603, '饼图', '/chart/pie', 6, 1),
(7, '订单管理', '/order', 0, 1),
(10, '首页', '/home', 0, 1),
(11, '商品', '/item-and-category', 0, 1);

-- 角色-权限对应表
drop table if exists role_perm;
create table role_perm (
    role_id bigint comment '角色 id',
    perm_id bigint comment '权限 id',
    primary key (role_id, perm_id)
);
insert into role_perm(role_id, perm_id) values 
(1, 1), -- admin, *
(2, 2), -- manager, 角色管理
(2, 201),
(2, 202),
(2, 203),
(2, 204),
(2, 205),
(2, 3), -- manager, 用户管理
(2, 301),
(2, 302),
(2, 303),
(2, 304),
(2, 305),
(2, 4), -- manager, 类别管理
(2, 401),
(2, 402),
(2, 403),
(2, 404),
(2, 405),
(2, 5), -- manager, 商品管理
(2, 501),
(2, 502),
(2, 503),
(2, 504),
(2, 505),
(3, 4), -- member, 类别管理
(3, 401),
(3, 402),
(3, 403),
(3, 404),
(3, 405),
(3, 5), -- member, 商品管理
(3, 501),
(3, 502),
(3, 503),
(3, 504),
(3, 505);


-- 商品类别表
drop table if exists category;
create table category (
    id bigint primary key comment '主键',
    name varchar(31) not null comment '名称', 
    pid bigint not null default 0 comment '父类别'
);
insert into category(id, name, pid) values
(1, '经典系', 0),
(101, '草系', 1),
(102, '火系', 1),
(103, '水系', 1),
(104, '电系', 1),
(2, '物理相关系', 0),
(201, '一般系', 2),
(202, '飞行系', 2),
(203, '岩石系', 2),
(204, '格斗系', 2),
(205, '地面系', 2),
(206, '钢系', 2),
(3, '精神相关系', 0),
(301, '超能力系', 3),
(302, '虫系', 3),
(303, '幽灵系', 3),
(304, '冰系', 3),
(305, '毒系', 3),
(4, '其他系', 0),
(401, '龙系', 4),
(402, '恶系', 4),
(403, '妖精系', 4);


drop table if exists item;
create table item(
    id bigint primary key,
    name varchar(31) not null comment '商品名称',
    price float not null comment '价格',
    description varchar(127) not null default '' comment '商品描述',
    detail varchar(1023) not null default '' comment '商品详情',
    images varchar(255) not null default '' comment '图片列表',
    note varchar(127) not null default '' comment '备注',
    status tinyint not null default 0 comment '状态',
    cid1 bigint not null comment '一级类别',
    cid2 bigint not null comment '二级类别',
    constraint unq_name unique(name),
    fulltext key ftx_name_desc_detail(name, description, detail) with parser ngram
);
insert into item(id, name, price, note, status, cid1, cid2) values
(1, '妙蛙种子', 318.0, '', 0, 1, 101),
(2, '妙蛙草', 405.0, '', 0, 1, 101),
(3, '妙蛙花', 525.0, '', 0, 1, 101),
(4, '小火龙', 309.0, '', 0, 1, 102),
(5, '火恐龙', 405.0, '', 0, 1, 102),
(6, '喷火龙', 534.0, '', 0, 1, 102),
(7, '杰尼龟', 314.0, '', 0, 1, 103),
(8, '卡咪龟', 405.0, '', 0, 1, 103),
(9, '水箭龟', 530.0, '', 0, 1, 103),
(10, '绿毛虫', 195.0, '', 0, 3, 302),
(11, '铁甲蛹', 205.0, '', 0, 3, 302),
(12, '巴大蝴', 395.0, '', 0, 3, 302),
(13, '独角虫', 195.0, '', 0, 3, 302),
(14, '铁壳昆', 205.0, '', 0, 3, 302),
(15, '大针蜂', 395.0, '', 0, 3, 302);