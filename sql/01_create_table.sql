drop table if exists batch;
create table batch(
    no char(10) primary key comment '批次号',
    start_time datetime not null comment '开始时间',
    end_time datetime not null comment '结束时间'
);

drop table if exists origin_datum;
create table origin_datum(
    id int primary key auto_increment comment 'ID',
    url varchar(255) not null comment '爬取URL',
    success tinyint(1) not null comment '是否爬取成功',
    error_message varchar(255) comment '失败原因',
    start_time datetime not null comment '开始时间',
    end_time datetime not null comment '结束时间',
    content text comment '内容',
    batch_no char(10) not null comment '批次号'
);

drop table if exists price;
create table price(
    id int primary key auto_increment comment 'ID',
    name varchar(20) not null comment '品名',
    max_price int not null comment '最大价格',
    average_price int not null comment '平均价格',
    min_price int not null comment '最小价格',
    specification varchar(20) not null comment '规格',
    unit varchar(10) not null comment '单位',
    issued_date date not null comment '发布日期',
    origin_datum_id int not null comment '原始数据ID'
);