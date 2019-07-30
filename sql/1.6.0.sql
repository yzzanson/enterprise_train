drop table if exists grain_brand;
CREATE TABLE `grain_brand` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `grain_sponsor` varchar(64) NOT NULL COMMENT '赞助商名称',
  `grain_brand` varchar(32) NOT NULL COMMENT '爱心物资品牌',
  `status` int(4) COMMENT '状态',
  `create_time` timestamp COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='爱心粮品牌';


drop table if exists grain_activity;
CREATE TABLE `grain_activity` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `grain_brand_id` int(8) NOT NULL COMMENT '爱心物资品牌id',
  `grain_logo` varchar(128) NOT NULL COMMENT '爱心物资缩略图',
  `grain_detail_logo` varchar(128) NOT NULL COMMENT '爱心物资缩略图',
  `grain_type` varchar(16) NOT NULL COMMENT '分类',
  `grain_cost` int(8) NOT NULL COMMENT '所需爱心',
  `grain_count` int(8) NOT NULL COMMENT '申请上限',
  `donated_base` varchar(64) NOT NULL COMMENT '被捐助方',
  `grain_explain` varchar(128) NOT NULL COMMENT '爱心物资描述',
  `certificate` varchar(32) NOT NULL COMMENT '证书编号英文缩写',
  `certificate_content` varchar(64) NOT NULL COMMENT '证书正文',
  `status` int(4) COMMENT '状态',
  `create_time` timestamp COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='爱心粮';

insert into grain_brand(grain_sponsor,grain_brand,status,create_time)
values('Toptrees领先','天然原肉粮',1,now());
insert into grain_brand(grain_sponsor,grain_brand,status,create_time)
values('Toptrees领先','鲜鲜猫条',1,now());

insert into grain_activity(grain_brand_id,grain_logo,grain_detail_logo,grain_type,grain_cost,grain_count,donated_base,grain_explain,certificate,certificate_content,status,create_time)
values(1,'https://neixun.forwe.store/static/neixun/1550562005493cat1-1.png','https://neixun.forwe.store/static/neixun/1550562027096cat1.png','主食',15000,1000,'杭州流浪动物基地','澳大利亚进口牛肋为原料，粒粒有肉的颗粒形态，55%高含肉量，满足狗狗食肉天性，提供科学均衡的天然营养。','YR','TA于%s申请捐助一袋天然原肉爱心粮1.5Kg，已被杭州流浪动物基地认领',1,now());

insert into grain_activity(grain_brand_id,grain_logo,grain_detail_logo,grain_type,grain_cost,grain_count,donated_base,grain_explain,certificate,certificate_content,status,create_time)
values(2,'https://neixun.forwe.store/static/neixun/1550562044272cat2-1.png','https://neixun.forwe.store/static/neixun/1550562063657cat2.png','主食',48000,2000,'杭州流浪动物基地','甄选纯净海域三文鱼，猫咪尽享营养与美味；添加海藻颗粒，富含不饱和脂肪酸，亮泽毛发。','MT','TA于%s申请捐助一盒鲜鲜猫条48g，已被杭州流浪动物基地认领',1,now());

drop table if exists user_grain_activity;
CREATE TABLE `user_grain_activity` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `company_id` int(8) NOT NULL  COMMENT '公司id',
  `activity_id` int(8) NOT NULL COMMENT '活动id',
  `user_id` int(8) NOT NULL COMMENT '用户id',
  `grain_cost` int(8) NOT NULL COMMENT '捐助爱心数',
  `certificate_no` varchar(64) NOT NULL COMMENT '证书编号',
  `create_time` timestamp COMMENT '创建时间',
   PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='爱心粮捐助记录';

ALTER TABLE user ADD union_id VARCHAR(64) COMMENT '钉钉用户唯一标识' AFTER ding_id;

