-- 平台数据表(用p_前缀)

drop table if exists p_user;
create table p_user(
  id bigint primary key auto_increment,
  username varchar(20) not null unique comment '只允许大小写字母数字下划线',
  password varchar(32) not null comment '密码（md5加密）'
)charset=utf8, comment '平台管理员用户表';

-- 资源管理系统(用r_前缀)

drop table if exists r_material;
create table r_material(
  id varchar(32) primary key,
  type char(5) not null comment '资源类型',
  name varchar(30) not null comment '资源名称',
  path varchar(100) not null comment '相对路径',
  file_length bigint not null comment '文件大小',
  content_type char(30) not null comment '内容类型',
  properties varchar(255) comment 'json格式的文件属性，不同类型的内容属性不一样',
  create_time datetime not null
)charset=utf8;

-- 微信应用数据库表（用act_前缀）

drop table if exists act_activity;
create table act_activity(
  id bigint primary key auto_increment,
  name varchar(30) not null comment '活动名称',
  start_time datetime not null comment '开始时间',
  end_time datetime not null comment '结束时间',
#   raffle_id bigint comment '抽奖id（完成活动的所有投票后跳转到的抽奖主题）',
  create_time datetime not null comment '创建时间',
  update_time datetime not null comment '更新时间'
)charset=utf8, comment '活动表，记录活动信息';

drop table if exists act_vote;
create table act_vote(
  id bigint primary key auto_increment,
  activity_id bigint not null comment '活动id',
  name varchar(30) not null comment '投票主题名称',
  role varchar(500) not null comment '规则',
  introduction varchar(10000) not null comment '介绍，最长10000个字符',
  start_time datetime not null comment '投票开始时间',
  end_time datetime not null comment '投票结束时间',
  create_time datetime not null comment '创建时间',
  update_time datetime not null comment '更新时间'
)charset=utf8, comment '投票主题表，记录投票主题信息';

drop table if exists act_vote_item;
create table act_vote_item(
  id bigint primary key auto_increment,
  vote_id bigint not null comment '投票主题id',
  title varchar(30) not null comment '主标题',
  sub_title varchar(50) comment '副标题',
  description varchar(1000) comment '描述',
  image_url varchar(100) comment '图片url'
)charset=utf8, comment '投票项表，记录一个投票主题中所有投票项';

drop table if exists act_vote_record;
create table act_vote_record(
  open_id varchar(32) not null comment '投票用户openid',
  vote_id bigint not null comment '投票主题id(冗余)',
  vote_item_id bigint not null comment '投票项id',
  vote_time datetime not null comment '投票时间'
)charset=utf8, comment '投票记录表，存储每个微信用户的投票记录';

drop table if exists act_raffle;
create table act_raffle (
  id bigint primary key auto_increment,
  activity_id bigint comment '活动id（没有完成活动里面的所有投票之前，不能进行抽奖）',
  name varchar(30) not null comment '抽奖主题名称',
  role varchar(500) not null comment '抽奖规则',
  start_time datetime not null comment '开始时间',
  end_time datetime not null comment '结束时间',
  update_time datetime not null comment '更新时间'
)charset=utf8, comment '抽奖主题表，记录抽奖主题';

drop table if exists act_raffle_item;
create table act_raffle_item(
  id bigint primary key auto_increment,
  raffle_id bigint not null comment '抽奖主题id',
  name varchar(30) not null comment '名称',
  description varchar(100) not null comment '描述',
  price bit(1) default b'0' not null comment '是否奖品',
  total_num int not null comment '总数量'
)charset=utf8, comment '抽奖项表，记录抽奖项信息';

drop table if exists act_raffle_record;
create table act_raffle_record(
  open_id varchar(32) not null comment '用户openid',
  raffle_id bigint not null comment '抽奖主题id',
  raffle_item_id bigint not null comment '抽奖项id',
  name varchar(30) not null comment '抽奖项名称',
  description varchar(100) not null comment '抽奖项描述',
  state integer comment '兑奖状态：0-未兑奖，1-已兑奖，-1-不能兑奖',
  raffle_time datetime not null comment '抽奖时间',
  achieve_time datetime comment '兑奖时间',
  unique (open_id, raffle_id)
)charset=utf8, comment '抽奖记录表，记录中奖用户信息';

insert into p_user(username, password) VALUE ('admin', 'admin');