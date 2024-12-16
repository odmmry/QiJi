SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for competition
-- ----------------------------
DROP TABLE IF EXISTS `competition`;
CREATE TABLE `competition` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL COMMENT '活动名称',
  `image_url` varchar(255) NOT NULL COMMENT '活动封面图片',
  `address` varchar(500) NOT NULL COMMENT '活动地点',
  `desc` varchar(500) NOT NULL COMMENT '活动描述',
  `start_date` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '开始日期，年月日',
  `end_date` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '结束日期，年月日',
  `user_id` int(11) unsigned NOT NULL COMMENT '发布人id',
  `create_time` int(11) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='比赛活动表';

-- ----------------------------
-- Records of competition
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for competition_participant
-- ----------------------------
DROP TABLE IF EXISTS `competition_participant`;
CREATE TABLE `competition_participant` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `competition_id` int(11) unsigned NOT NULL COMMENT '比赛id',
  `user_id` int(11) unsigned NOT NULL COMMENT '参与人id',
  `create_time` int(11) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='活动参与人表';

-- ----------------------------
-- Records of competition_participant
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for posts
-- ----------------------------
DROP TABLE IF EXISTS `posts`;
CREATE TABLE `posts` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `content` varchar(500) NOT NULL COMMENT '帖子内容',
  `user_id` int(10) unsigned NOT NULL COMMENT '用户人id',
  `create_time` int(11) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='帖子表';

-- ----------------------------
-- Records of posts
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for posts_comment
-- ----------------------------
DROP TABLE IF EXISTS `posts_comment`;
CREATE TABLE `posts_comment` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `post_id` int(11) unsigned NOT NULL COMMENT '帖子id',
  `content` varchar(255) NOT NULL COMMENT '评论内容',
  `user_id` int(11) unsigned NOT NULL COMMENT '用户id',
  `create_time` int(11) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='帖子评论表';

-- ----------------------------
-- Records of posts_comment
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for posts_images
-- ----------------------------
DROP TABLE IF EXISTS `posts_images`;
CREATE TABLE `posts_images` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `post_id` int(11) unsigned NOT NULL COMMENT '帖子id',
  `image_url` varchar(255) NOT NULL COMMENT '图片地址',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='帖子图片表';

-- ----------------------------
-- Records of posts_images
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for posts_thumb
-- ----------------------------
DROP TABLE IF EXISTS `posts_thumb`;
CREATE TABLE `posts_thumb` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `post_id` int(11) unsigned NOT NULL COMMENT '帖子id',
  `user_id` int(11) unsigned NOT NULL COMMENT '用户id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='帖子点赞表';

-- ----------------------------
-- Records of posts_thumb
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for shop_goods
-- ----------------------------
DROP TABLE IF EXISTS `shop_goods`;
CREATE TABLE `shop_goods` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL COMMENT '商品名称',
  `price` decimal(10,2) unsigned NOT NULL DEFAULT '0.00' COMMENT '商品价格',
  `desc` varchar(500) NOT NULL COMMENT '商品描述',
  `user_id` int(11) unsigned NOT NULL COMMENT '发布人id',
  `create_time` int(11) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='二手市场商品表';

-- ----------------------------
-- Records of shop_goods
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for shop_goods_images
-- ----------------------------
DROP TABLE IF EXISTS `shop_goods_images`;
CREATE TABLE `shop_goods_images` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `shop_goods_id` int(11) unsigned NOT NULL COMMENT '商品id',
  `image_url` varchar(255) NOT NULL COMMENT '图片地址',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='二手市场商品图片表';

-- ----------------------------
-- Records of shop_goods_images
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL COMMENT '用户名',
  `password` char(32) NOT NULL COMMENT '登录密码',
  `avatar_url` varchar(255) NOT NULL COMMENT '头像',
  `create_time` int(11) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of users
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for users_cart
-- ----------------------------
DROP TABLE IF EXISTS `users_cart`;
CREATE TABLE `users_cart` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `shop_goods_id` int(11) unsigned NOT NULL COMMENT '商品id',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='购物车表';

-- ----------------------------
-- Records of users_cart
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for users_orders
-- ----------------------------
DROP TABLE IF EXISTS `users_orders`;
CREATE TABLE `users_orders` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `order_no` varbinary(50) NOT NULL COMMENT '订单号，唯一',
  `total_price` decimal(10,2) unsigned NOT NULL DEFAULT '0.00' COMMENT '订单总金额',
  `address_name` varchar(255) NOT NULL COMMENT '收货人姓名',
  `address_phone` varchar(255) NOT NULL COMMENT '收货人联系电话',
  `address_detail` varchar(500) NOT NULL COMMENT '收货人详细地址',
  `user_id` int(11) unsigned NOT NULL COMMENT '用户id',
  `create_time` int(11) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表，二手市场';

-- ----------------------------
-- Records of users_orders
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for users_orders_goods
-- ----------------------------
DROP TABLE IF EXISTS `users_orders_goods`;
CREATE TABLE `users_orders_goods` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `order_id` int(11) unsigned NOT NULL COMMENT '订单id',
  `image_url` varchar(255) NOT NULL COMMENT '商品图片',
  `name` varchar(255) NOT NULL COMMENT '商品名称',
  `price` decimal(10,2) unsigned NOT NULL DEFAULT '0.00' COMMENT '商品价格',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单商品表';

-- ----------------------------
-- Records of users_orders_goods
-- ----------------------------
BEGIN;
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
