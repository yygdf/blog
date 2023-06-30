/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50733
 Source Host           : localhost:3306
 Source Schema         : blog

 Target Server Type    : MySQL
 Target Server Version : 50733
 File Encoding         : 65001

 Date: 27/06/2023 06:35:30
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tb_article
-- ----------------------------
DROP TABLE IF EXISTS `tb_article`;
CREATE TABLE `tb_article`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '文章id',
  `user_id` int(11) NOT NULL COMMENT '作者id',
  `article_title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文章标题',
  `article_content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文章内容',
  `article_cover` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '文章封面',
  `category_id` int(11) NOT NULL COMMENT '分类id',
  `is_top` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未置顶，1已置顶',
  `is_draft` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0是草稿，1不是草稿',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未删除，1已删除',
  `creat_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_article
-- ----------------------------

-- ----------------------------
-- Table structure for tb_article_tag
-- ----------------------------
DROP TABLE IF EXISTS `tb_article_tag`;
CREATE TABLE `tb_article_tag`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `article_id` int(11) NOT NULL COMMENT '文章id',
  `tag_id` int(11) NOT NULL COMMENT '标签id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_article_tag
-- ----------------------------

-- ----------------------------
-- Table structure for tb_base_config
-- ----------------------------
DROP TABLE IF EXISTS `tb_base_config`;
CREATE TABLE `tb_base_config`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '配置id',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `config_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '配置名',
  `config_value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '配置值',
  `config_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '配置描述',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未删除，1已删除',
  `is_deletable` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0可以删除，1不可以删除',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_base_config
-- ----------------------------
INSERT INTO `tb_base_config` VALUES (1, 1, 'home_cover', 'https://ksling.cn/static/img/page/home.jpg', '首页封面图', 0, 1, '2023-05-12 18:37:22', NULL);
INSERT INTO `tb_base_config` VALUES (2, 1, 'article_cover', 'https://ksling.cn/static/img/page/archive.jpg', '文章封面图', 0, 1, '2023-05-12 18:38:04', NULL);
INSERT INTO `tb_base_config` VALUES (3, 1, 'category_cover', 'https://ksling.cn/static/img/page/category.jpg', '分类封面图', 0, 1, '2023-05-12 18:39:18', NULL);
INSERT INTO `tb_base_config` VALUES (4, 1, 'tag_cover', 'https://ksling.cn/static/img/page/tag.jpg', '标签封面图', 0, 1, '2023-05-12 18:39:53', NULL);
INSERT INTO `tb_base_config` VALUES (5, 1, 'link_cover', 'https://ksling.cn/static/img/page/link.jpg', '友链封面图', 0, 1, '2023-05-12 18:40:19', NULL);
INSERT INTO `tb_base_config` VALUES (6, 1, 'about_cover', 'https://ksling.cn/static/img/page/about.jpg', '关于封面图', 0, 1, '2023-05-12 18:40:44', NULL);
INSERT INTO `tb_base_config` VALUES (7, 1, 'message_cover', 'https://ksling.cn/static/img/page/message.jpg', '留言封面图', 0, 1, '2023-05-12 18:41:26', NULL);
INSERT INTO `tb_base_config` VALUES (8, 1, 'personal_cover', 'https://ksling.cn/static/img/page/personal.jpg', '个人中心封面图', 0, 1, '2023-05-12 18:42:11', NULL);
INSERT INTO `tb_base_config` VALUES (9, 1, 'default_cover', 'https://ksling.cn/static/img/page/article.jpg', '默认封面图', 0, 1, '2023-05-12 18:43:27', NULL);
INSERT INTO `tb_base_config` VALUES (10, 1, 'home_qq', 'https://wpa.qq.com/msgrd?v=3&uin=294513634&site=qq&menu=yes', '首页QQ链接', 0, 1, '2023-05-12 19:04:41', NULL);
INSERT INTO `tb_base_config` VALUES (11, 1, 'home_github', 'https://github.com/yygdf', '首页Github链接', 0, 1, '2023-05-12 19:05:33', NULL);
INSERT INTO `tb_base_config` VALUES (12, 1, 'home_gitee', 'https://gitee.com/yygdf', '首页Gitee链接', 0, 1, '2023-05-12 19:06:40', NULL);
INSERT INTO `tb_base_config` VALUES (13, 1, 'home_banner', '欢迎来到『有一个地方』', '首页横幅', 0, 1, '2023-05-12 19:07:20', NULL);
INSERT INTO `tb_base_config` VALUES (14, 1, 'default_avatar', 'https://ksling.cn/static/img/avatar/defaultAvatar.jpg', '默认用户头像', 0, 1, '2023-05-12 19:08:03', NULL);
INSERT INTO `tb_base_config` VALUES (15, 1, 'wss_address', 'wss://ksling.cn/websocket', 'websocket监听地址', 0, 1, '2023-05-12 19:08:39', NULL);
INSERT INTO `tb_base_config` VALUES (16, 1, 'home_notice', '暂无内容~~', '首页公告', 0, 1, '2023-05-12 19:12:25', NULL);

-- ----------------------------
-- Table structure for tb_category
-- ----------------------------
DROP TABLE IF EXISTS `tb_category`;
CREATE TABLE `tb_category`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '分类id',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `category_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '分类名',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_category
-- ----------------------------

-- ----------------------------
-- Table structure for tb_chat_record
-- ----------------------------
DROP TABLE IF EXISTS `tb_chat_record`;
CREATE TABLE `tb_chat_record`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '聊天记录id',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `nickname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户昵称',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户头像',
  `chat_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '聊天内容',
  `chat_type` tinyint(1) NOT NULL COMMENT '聊天类型',
  `ip_address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'IP地址',
  `ip_source` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'IP来源',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未撤回，1已撤回',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_chat_record
-- ----------------------------

-- ----------------------------
-- Table structure for tb_comment
-- ----------------------------
DROP TABLE IF EXISTS `tb_comment`;
CREATE TABLE `tb_comment`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '评论id',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `article_id` int(11) NOT NULL COMMENT '文章id',
  `comment_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '评论内容',
  `reply_id` int(11) NOT NULL DEFAULT 0 COMMENT '回复用户id',
  `parent_id` int(11) NOT NULL DEFAULT 0 COMMENT '父评论id',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未删除，1已删除',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_comment
-- ----------------------------

-- ----------------------------
-- Table structure for tb_day_traffic
-- ----------------------------
DROP TABLE IF EXISTS `tb_day_traffic`;
CREATE TABLE `tb_day_traffic`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '访问记录id',
  `view_date` date NOT NULL COMMENT '日期',
  `view_count` int(11) NOT NULL COMMENT '访问量',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_day_traffic
-- ----------------------------

-- ----------------------------
-- Table structure for tb_friend_link
-- ----------------------------
DROP TABLE IF EXISTS `tb_friend_link`;
CREATE TABLE `tb_friend_link`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '友链id',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `link_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '友链名称',
  `link_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '友链地址',
  `link_desc` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '友链描述',
  `link_logo` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '友链logo',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_friend_link
-- ----------------------------

-- ----------------------------
-- Table structure for tb_login_log
-- ----------------------------
DROP TABLE IF EXISTS `tb_login_log`;
CREATE TABLE `tb_login_log`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '日志id',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户账号',
  `login_type` tinyint(1) NOT NULL COMMENT '登录类型',
  `login_time` datetime(0) NOT NULL COMMENT '登录时间',
  `login_device` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '登录设备',
  `browser` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '浏览器类型',
  `operation_system` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '操作系统类型',
  `ip_address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'IP地址',
  `ip_source` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'IP来源',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 76 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_login_log
-- ----------------------------
INSERT INTO `tb_login_log` VALUES (1, 1, 'root@qq.com', 1, '2023-04-26 22:03:59', 'Computer', 'Firefox 7', 'Windows 10', '127.0.0.1', '');
INSERT INTO `tb_login_log` VALUES (2, 1, 'root@qq.com', 1, '2023-04-26 23:24:59', 'Computer', 'Firefox 7', 'Windows 10', '127.0.0.1', '');
INSERT INTO `tb_login_log` VALUES (3, 1, 'root@qq.com', 1, '2023-04-26 23:31:30', 'Computer', 'Firefox 7', 'Windows 10', '127.0.0.1', '');
INSERT INTO `tb_login_log` VALUES (4, 1, 'root@qq.com', 1, '2023-04-26 23:33:13', 'Computer', 'Firefox 7', 'Windows 10', '127.0.0.1', '');
INSERT INTO `tb_login_log` VALUES (5, 1, 'root@qq.com', 1, '2023-04-26 23:38:15', 'Computer', 'Firefox 7', 'Windows 10', '127.0.0.1', '');
INSERT INTO `tb_login_log` VALUES (6, 1, 'root@qq.com', 1, '2023-04-26 23:59:20', 'Computer', 'Firefox 7', 'Windows 10', '127.0.0.1', '');
INSERT INTO `tb_login_log` VALUES (7, 1, 'root@qq.com', 1, '2023-04-27 00:00:58', 'Computer', 'Firefox 7', 'Windows 10', '127.0.0.1', '');
INSERT INTO `tb_login_log` VALUES (8, 1, 'root@qq.com', 1, '2023-04-27 00:03:16', 'Computer', 'Firefox 7', 'Windows 10', '127.0.0.1', '');
INSERT INTO `tb_login_log` VALUES (9, 1, 'root@qq.com', 1, '2023-04-27 00:04:33', 'Computer', 'Firefox 7', 'Windows 10', '127.0.0.1', '');
INSERT INTO `tb_login_log` VALUES (10, 1, 'root@qq.com', 1, '2023-04-27 00:09:10', 'Computer', 'Firefox 7', 'Windows 10', '127.0.0.1', '');
INSERT INTO `tb_login_log` VALUES (11, 1, 'root@qq.com', 1, '2023-04-27 00:11:51', 'Computer', 'Firefox 7', 'Windows 10', '127.0.0.1', '');
INSERT INTO `tb_login_log` VALUES (12, 1, 'root@qq.com', 1, '2023-04-27 00:12:34', 'Computer', 'Firefox 7', 'Windows 10', '127.0.0.1', '');
INSERT INTO `tb_login_log` VALUES (13, 1, 'root@qq.com', 1, '2023-04-27 00:15:50', 'Computer', 'Firefox 7', 'Windows 10', '127.0.0.1', '');
INSERT INTO `tb_login_log` VALUES (14, 1, 'root@qq.com', 1, '2023-04-27 00:22:52', 'Computer', 'Firefox 7', 'Windows 10', '127.0.0.1', '');
INSERT INTO `tb_login_log` VALUES (15, 1, 'root@qq.com', 1, '2023-04-27 00:23:44', 'Computer', 'Firefox 7', 'Windows 10', '127.0.0.1', '');
INSERT INTO `tb_login_log` VALUES (16, 2, 'admin@qq.com', 1, '2023-04-27 00:24:59', 'Computer', 'Firefox 7', 'Windows 10', '127.0.0.1', '');
INSERT INTO `tb_login_log` VALUES (17, 2, 'admin@qq.com', 1, '2023-04-27 00:32:16', 'Computer', 'Firefox 7', 'Windows 10', '127.0.0.1', '');
INSERT INTO `tb_login_log` VALUES (18, 1, 'root@qq.com', 1, '2023-04-27 00:33:36', 'Computer', 'Firefox 7', 'Windows 10', '127.0.0.1', '');
INSERT INTO `tb_login_log` VALUES (19, 1, 'root@qq.com', 1, '2023-04-27 00:35:53', 'Computer', 'Firefox 7', 'Windows 10', '127.0.0.1', '');
INSERT INTO `tb_login_log` VALUES (20, 1, 'root@qq.com', 1, '2023-04-27 00:46:55', 'Computer', 'Firefox 7', 'Windows 10', '127.0.0.1', '');
INSERT INTO `tb_login_log` VALUES (21, 2, 'admin@qq.com', 1, '2023-05-09 10:22:48', 'Computer', 'Firefox 7', 'Windows 10', '127.0.0.1', '');
INSERT INTO `tb_login_log` VALUES (22, 2, 'admin@qq.com', 1, '2023-05-09 10:23:55', 'Computer', 'Firefox 7', 'Windows 10', '127.0.0.1', '');
INSERT INTO `tb_login_log` VALUES (23, 2, 'admin@qq.com', 1, '2023-05-09 10:28:07', 'Computer', 'Firefox 7', 'Windows 10', '127.0.0.1', '');
INSERT INTO `tb_login_log` VALUES (24, 2, 'admin@qq.com', 1, '2023-05-09 10:38:03', 'Computer', 'Firefox 7', 'Windows 10', '127.0.0.1', '');
INSERT INTO `tb_login_log` VALUES (25, 2, 'admin@qq.com', 1, '2023-05-09 10:40:26', 'Computer', 'Firefox 7', 'Windows 10', '127.0.0.1', '');
INSERT INTO `tb_login_log` VALUES (26, 2, 'admin@qq.com', 1, '2023-05-09 10:41:04', 'Computer', 'Firefox 7', 'Windows 10', '127.0.0.1', '');
INSERT INTO `tb_login_log` VALUES (27, 2, 'admin@qq.com', 1, '2023-05-09 10:43:57', 'Computer', 'Firefox 7', 'Windows 10', '127.0.0.1', '');
INSERT INTO `tb_login_log` VALUES (28, 2, 'admin@qq.com', 1, '2023-05-09 10:44:28', 'Computer', 'Firefox 7', 'Windows 10', '127.0.0.1', '');
INSERT INTO `tb_login_log` VALUES (29, 2, 'admin@qq.com', 1, '2023-05-09 10:45:56', 'Computer', 'Firefox 7', 'Windows 10', '127.0.0.1', '');
INSERT INTO `tb_login_log` VALUES (30, 2, 'admin@qq.com', 1, '2023-05-09 10:49:12', 'Computer', 'Firefox 7', 'Windows 10', '127.0.0.1', '');
INSERT INTO `tb_login_log` VALUES (31, 2, 'admin@qq.com', 1, '2023-05-09 11:06:05', 'Computer', 'Firefox 7', 'Windows 10', '127.0.0.1', '');
INSERT INTO `tb_login_log` VALUES (32, 2, 'admin@qq.com', 1, '2023-05-09 11:07:27', 'Computer', 'Firefox 7', 'Windows 10', '127.0.0.1', '');
INSERT INTO `tb_login_log` VALUES (33, 2, 'admin@qq.com', 1, '2023-05-09 11:09:46', 'Computer', 'Firefox 7', 'Windows 10', '127.0.0.1', '');
INSERT INTO `tb_login_log` VALUES (34, 2, 'admin@qq.com', 1, '2023-05-09 11:10:45', 'Computer', 'Firefox 7', 'Windows 10', '127.0.0.1', '');
INSERT INTO `tb_login_log` VALUES (35, 2, 'admin@qq.com', 1, '2023-05-09 11:13:18', 'Computer', 'Firefox 7', 'Windows 10', '127.0.0.1', '');
INSERT INTO `tb_login_log` VALUES (36, 2, 'admin@qq.com', 1, '2023-05-09 11:13:39', 'Computer', 'Firefox 7', 'Windows 10', '127.0.0.1', '');
INSERT INTO `tb_login_log` VALUES (37, 1, 'root@qq.com', 1, '2023-05-11 11:10:17', 'Computer', 'Firefox 7', 'Windows 10', '127.0.0.1', '');
INSERT INTO `tb_login_log` VALUES (38, 1, 'root@qq.com', 1, '2023-05-11 11:11:30', 'Computer', 'Firefox 7', 'Windows 10', '127.0.0.1', '');
INSERT INTO `tb_login_log` VALUES (39, 1, 'root@qq.com', 1, '2023-05-11 11:14:40', 'Computer', 'Firefox 7', 'Windows 10', '127.0.0.1', '');
INSERT INTO `tb_login_log` VALUES (40, 1, 'root@qq.com', 1, '2023-05-11 11:17:29', 'Computer', 'Firefox 7', 'Windows 10', '127.0.0.1', '');
INSERT INTO `tb_login_log` VALUES (41, 1, 'root@qq.com', 1, '2023-05-11 11:19:19', 'Computer', 'Firefox 7', 'Windows 10', '127.0.0.1', '');
INSERT INTO `tb_login_log` VALUES (42, 1, 'root@qq.com', 1, '2023-05-12 10:48:09', 'Computer', 'Firefox 7', 'Windows 10', '127.0.0.1', '');
INSERT INTO `tb_login_log` VALUES (43, 1, 'root@qq.com', 1, '2023-05-12 10:49:27', 'Computer', 'Firefox 7', 'Windows 10', '127.0.0.1', '');
INSERT INTO `tb_login_log` VALUES (44, 1, 'root@qq.com', 1, '2023-05-12 10:49:40', 'Computer', 'Firefox 7', 'Windows 10', '127.0.0.1', '');
INSERT INTO `tb_login_log` VALUES (45, 1, 'root@qq.com', 1, '2023-05-12 10:54:37', 'Computer', 'Firefox 7', 'Windows 10', '127.0.0.1', '');
INSERT INTO `tb_login_log` VALUES (46, 1, 'root@qq.com', 1, '2023-05-12 10:55:17', 'Computer', 'Firefox 7', 'Windows 10', '127.0.0.1', '');
INSERT INTO `tb_login_log` VALUES (47, 1, 'root@qq.com', 1, '2023-05-12 11:00:06', 'Computer', 'Firefox 7', 'Windows 10', '127.0.0.1', '');
INSERT INTO `tb_login_log` VALUES (48, 1, 'root@qq.com', 1, '2023-05-12 11:02:53', 'Computer', 'Firefox 7', 'Windows 10', '127.0.0.1', '');
INSERT INTO `tb_login_log` VALUES (49, 1, 'root@qq.com', 1, '2023-05-12 11:06:10', 'Computer', 'Firefox 7', 'Windows 10', '127.0.0.1', '');
INSERT INTO `tb_login_log` VALUES (50, 1, 'root@qq.com', 1, '2023-05-12 11:15:35', 'Computer', 'Firefox 7', 'Windows 10', '127.0.0.1', '');
INSERT INTO `tb_login_log` VALUES (51, 1, 'root@qq.com', 1, '2023-05-12 11:21:57', 'Computer', 'Firefox 7', 'Windows 10', '127.0.0.1', '');
INSERT INTO `tb_login_log` VALUES (52, 1, 'root@qq.com', 1, '2023-05-12 11:24:22', 'Computer', 'Firefox 7', 'Windows 10', '127.0.0.1', '');
INSERT INTO `tb_login_log` VALUES (53, 1, 'root@qq.com', 1, '2023-05-12 12:05:19', 'Computer', 'Firefox 7', 'Windows 10', '127.0.0.1', '');
INSERT INTO `tb_login_log` VALUES (54, 1, 'root@qq.com', 1, '2023-05-12 12:18:11', 'Computer', 'Firefox 7', 'Windows 10', '127.0.0.1', '');
INSERT INTO `tb_login_log` VALUES (55, 1, 'root@qq.com', 1, '2023-05-12 12:20:36', 'Computer', 'Firefox 7', 'Windows 10', '127.0.0.1', '');
INSERT INTO `tb_login_log` VALUES (56, 1, 'root@qq.com', 1, '2023-05-12 12:21:05', 'Computer', 'Firefox 7', 'Windows 10', '127.0.0.1', '');
INSERT INTO `tb_login_log` VALUES (57, 1, 'root@qq.com', 1, '2023-05-12 12:21:19', 'Computer', 'Firefox 7', 'Windows 10', '127.0.0.1', '');
INSERT INTO `tb_login_log` VALUES (58, 1, 'root@qq.com', 1, '2023-05-12 12:35:34', 'Computer', 'Firefox 7', 'Windows 10', '127.0.0.1', '');
INSERT INTO `tb_login_log` VALUES (59, 1, 'root@qq.com', 1, '2023-05-12 12:35:42', 'Computer', 'Firefox 7', 'Windows 10', '127.0.0.1', '');
INSERT INTO `tb_login_log` VALUES (60, 1, 'root@qq.com', 1, '2023-05-12 12:36:05', 'Computer', 'Firefox 7', 'Windows 10', '127.0.0.1', '');
INSERT INTO `tb_login_log` VALUES (61, 1, 'root@qq.com', 1, '2023-05-12 12:36:40', 'Computer', 'Firefox 7', 'Windows 10', '127.0.0.1', '');
INSERT INTO `tb_login_log` VALUES (62, 1, 'root@qq.com', 1, '2023-05-12 12:36:50', 'Computer', 'Firefox 7', 'Windows 10', '127.0.0.1', '');
INSERT INTO `tb_login_log` VALUES (63, 1, 'root@qq.com', 1, '2023-05-12 12:39:52', 'Computer', 'Firefox 7', 'Windows 10', '127.0.0.1', '');
INSERT INTO `tb_login_log` VALUES (64, 1, 'root@qq.com', 1, '2023-05-12 12:41:08', 'Computer', 'Firefox 7', 'Windows 10', '127.0.0.1', '');
INSERT INTO `tb_login_log` VALUES (65, 1, 'root@qq.com', 1, '2023-05-12 12:41:16', 'Computer', 'Firefox 7', 'Windows 10', '127.0.0.1', '');
INSERT INTO `tb_login_log` VALUES (66, 1, 'root@qq.com', 1, '2023-05-12 12:42:09', 'Computer', 'Firefox 7', 'Windows 10', '127.0.0.1', '');
INSERT INTO `tb_login_log` VALUES (67, 1, 'root@qq.com', 1, '2023-05-12 12:43:06', 'Computer', 'Firefox 7', 'Windows 10', '127.0.0.1', '');
INSERT INTO `tb_login_log` VALUES (68, 1, 'root@qq.com', 1, '2023-05-12 12:45:11', 'Computer', 'Firefox 7', 'Windows 10', '127.0.0.1', '');
INSERT INTO `tb_login_log` VALUES (69, 1, 'root@qq.com', 1, '2023-05-12 12:47:36', 'Computer', 'Firefox 7', 'Windows 10', '127.0.0.1', '');
INSERT INTO `tb_login_log` VALUES (70, 1, 'root@qq.com', 1, '2023-05-12 12:48:33', 'Computer', 'Firefox 7', 'Windows 10', '127.0.0.1', '');
INSERT INTO `tb_login_log` VALUES (71, 1, 'root@qq.com', 1, '2023-05-12 12:49:35', 'Computer', 'Firefox 7', 'Windows 10', '127.0.0.1', '');
INSERT INTO `tb_login_log` VALUES (72, 1, 'root@qq.com', 1, '2023-05-12 12:56:28', 'Computer', 'Firefox 7', 'Windows 10', '127.0.0.1', '');
INSERT INTO `tb_login_log` VALUES (73, 1, 'root@qq.com', 1, '2023-05-12 12:57:08', 'Computer', 'Firefox 7', 'Windows 10', '127.0.0.1', '');
INSERT INTO `tb_login_log` VALUES (74, 1, 'root@qq.com', 1, '2023-05-31 20:38:26', 'Computer', 'Firefox 7', 'Windows 10', '127.0.0.1', '');
INSERT INTO `tb_login_log` VALUES (75, 1, 'root@qq.com', 1, '2023-05-31 20:52:01', 'Computer', 'Firefox 7', 'Windows 10', '127.0.0.1', '');

-- ----------------------------
-- Table structure for tb_menu
-- ----------------------------
DROP TABLE IF EXISTS `tb_menu`;
CREATE TABLE `tb_menu`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '菜单id',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '菜单名称',
  `path` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '菜单路径',
  `component` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '菜单组件',
  `icon` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '菜单图标',
  `parent_id` int(11) NOT NULL DEFAULT 0 COMMENT '父菜单id',
  `order_num` tinyint(1) NOT NULL DEFAULT 0 COMMENT '排序指标',
  `is_hidden` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未隐藏，1已隐藏',
  `is_disabled` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未禁用，1已禁用',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 903 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_menu
-- ----------------------------
INSERT INTO `tb_menu` VALUES (1, 1, '首页', '/', '/home/Home.vue', 'el-icon-odometer', 0, 1, 0, 0, '2023-04-26 23:04:13', NULL);
INSERT INTO `tb_menu` VALUES (2, 1, '文章管理', '/article-menu', 'Layout', 'el-icon-document', 0, 2, 0, 0, '2023-05-12 13:30:18', NULL);
INSERT INTO `tb_menu` VALUES (3, 1, '消息管理', '/message-menu', 'Layout', 'el-icon-message', 0, 3, 0, 0, '2023-05-12 13:31:28', NULL);
INSERT INTO `tb_menu` VALUES (4, 1, '链接管理', '/link-menu', 'Layout', 'el-icon-link', 0, 4, 0, 0, '2023-05-12 13:33:40', NULL);
INSERT INTO `tb_menu` VALUES (5, 1, '数据统计', '/statistic-menu', 'Layout', 'el-icon-s-data', 0, 5, 0, 0, '2023-05-12 13:37:45', NULL);
INSERT INTO `tb_menu` VALUES (6, 1, '系统管理', '/system-menu', 'Layout', 'el-icon-setting', 0, 6, 0, 0, '2023-05-12 13:40:50', NULL);
INSERT INTO `tb_menu` VALUES (7, 1, '用户管理', '/user-menu', 'Layout', 'el-icon-user', 0, 7, 0, 0, '2023-05-12 13:43:01', NULL);
INSERT INTO `tb_menu` VALUES (8, 1, '日志管理', '/log-menu', 'Layout', 'el-icon-notebook-1', 0, 8, 0, 0, '2023-05-12 13:50:43', NULL);
INSERT INTO `tb_menu` VALUES (9, 1, '个人中心', '/personal-menu', 'Layout', 'el-icon-s-home', 0, 9, 0, 0, '2023-05-12 13:52:12', NULL);
INSERT INTO `tb_menu` VALUES (201, 1, '添加文章', '/article', '/article/Article.vue', 'el-icon-document-add', 2, 21, 0, 0, '2023-05-12 13:57:27', NULL);
INSERT INTO `tb_menu` VALUES (202, 1, '文章列表', '/articles', '/article/Articles.vue', 'el-icon-document-copy', 2, 22, 0, 0, '2023-05-12 14:01:25', NULL);
INSERT INTO `tb_menu` VALUES (203, 1, '分类管理', '/category', '/article/Category.vue', 'el-icon-files', 2, 23, 0, 0, '2023-05-12 14:09:01', NULL);
INSERT INTO `tb_menu` VALUES (204, 1, '标签管理', '/tag', '/article/Tag.vue', 'el-icon-collection-tag', 2, 24, 0, 0, '2023-05-12 14:10:41', NULL);
INSERT INTO `tb_menu` VALUES (301, 1, '评论管理', '/comment', '/message/Comment.vue', 'el-icon-chat-dot-square', 3, 31, 0, 0, '2023-05-12 14:16:31', NULL);
INSERT INTO `tb_menu` VALUES (302, 1, '留言管理', '/message', '/message/Message.vue', 'el-icon-chat-dot-round', 3, 32, 0, 0, '2023-05-12 14:17:18', NULL);
INSERT INTO `tb_menu` VALUES (401, 1, '友链管理', '/friend', '/link/Friend.vue', 'el-icon-connection', 4, 41, 0, 0, '2023-05-12 17:37:47', NULL);
INSERT INTO `tb_menu` VALUES (501, 1, '文章统计', '/article-statistic', '/statistic/Article.vue', 'el-icon-tickets', 5, 51, 0, 0, '2023-05-12 14:22:50', NULL);
INSERT INTO `tb_menu` VALUES (502, 1, '留言统计', '/message-statistic', '/statistic/Message.vue', 'el-icon-chat-line-square', 5, 52, 0, 0, '2023-05-12 14:25:34', NULL);
INSERT INTO `tb_menu` VALUES (503, 1, '用户统计', '/user-statistic', '/statistic/User.vue', 'el-icon-s-check', 5, 53, 0, 0, '2023-05-12 14:27:43', NULL);
INSERT INTO `tb_menu` VALUES (601, 1, '基础配置', '/base', '/system/Base.vue', 'el-icon-coin', 6, 61, 0, 0, '2023-05-12 14:32:36', NULL);
INSERT INTO `tb_menu` VALUES (602, 1, '菜单管理', '/menu', '/system/Menu.vue', 'el-icon-menu', 6, 62, 0, 0, '2023-05-12 14:44:36', NULL);
INSERT INTO `tb_menu` VALUES (603, 1, '资源管理', '/resource', '/system/Resource.vue', 'el-icon-s-grid', 6, 63, 0, 0, '2023-05-12 15:33:35', NULL);
INSERT INTO `tb_menu` VALUES (604, 1, '角色管理', '/role', '/system/Role.vue', 'el-icon-s-custom', 6, 64, 0, 0, '2023-05-12 15:37:11', NULL);
INSERT INTO `tb_menu` VALUES (701, 1, '在线用户', '/online-user', '/user/Online.vue', 'el-icon-coordinate', 7, 71, 0, 0, '2023-05-12 14:38:37', NULL);
INSERT INTO `tb_menu` VALUES (702, 1, '用户列表', '/users', '/user/Users.vue', 'el-icon-user-solid', 7, 72, 0, 0, '2023-05-12 14:41:42', NULL);
INSERT INTO `tb_menu` VALUES (801, 1, '登录日志', '/login', '/log/Login.vue', 'el-icon-map-location', 8, 81, 0, 0, '2023-05-12 15:44:03', NULL);
INSERT INTO `tb_menu` VALUES (802, 1, '操作日志', '/operation', '/log/Operation.vue', 'el-icon-receiving', 8, 82, 0, 0, '2023-05-12 15:49:53', NULL);
INSERT INTO `tb_menu` VALUES (901, 1, '关于我', '/about', '/personal/About.vue', 'el-icon-place', 9, 91, 0, 0, '2023-05-12 16:00:46', NULL);
INSERT INTO `tb_menu` VALUES (902, 1, '个人配置', '/personal', '/personal/Personal.vue', 'el-icon-postcard', 9, 92, 0, 0, '2023-05-12 14:36:02', NULL);

-- ----------------------------
-- Table structure for tb_message
-- ----------------------------
DROP TABLE IF EXISTS `tb_message`;
CREATE TABLE `tb_message`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '留言id',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `nickname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户昵称',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户头像',
  `message_content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '留言内容',
  `message_speed` tinyint(1) NOT NULL COMMENT '留言速度',
  `ip_address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'IP地址',
  `ip_source` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'IP来源',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_message
-- ----------------------------

-- ----------------------------
-- Table structure for tb_multi_dir
-- ----------------------------
DROP TABLE IF EXISTS `tb_multi_dir`;
CREATE TABLE `tb_multi_dir`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '目录id',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `dir_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '目录名称',
  `dir_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '目录地址',
  `dir_desc` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '目录描述',
  `dir_cover` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '目录封面',
  `is_public` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未公开，1已公开',
  `is_hidden` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未隐藏，1已隐藏',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未删除，1已删除',
  `is_deletable` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0可以删除，1不可以删除',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_multi_dir
-- ----------------------------

-- ----------------------------
-- Table structure for tb_multi_file
-- ----------------------------
DROP TABLE IF EXISTS `tb_multi_file`;
CREATE TABLE `tb_multi_file`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '文件id',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `file_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '文件名称',
  `file_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件地址',
  `file_desc` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '文件描述',
  `multi_dir_id` int(11) NOT NULL COMMENT '父目录id',
  `sub_dir` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '子目录',
  `is_hidden` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未隐藏，1已隐藏',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未删除，1已删除',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_multi_file
-- ----------------------------

-- ----------------------------
-- Table structure for tb_operation_log
-- ----------------------------
DROP TABLE IF EXISTS `tb_operation_log`;
CREATE TABLE `tb_operation_log`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '操作日志id',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `opt_uri` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '操作路径',
  `opt_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '操作类型',
  `opt_module` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '操作模块',
  `opt_method` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '操作方法',
  `opt_desc` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '操作描述',
  `request_param` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '请求参数',
  `request_method` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '请求方式',
  `response_data` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '响应数据',
  `ip_address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'IP地址',
  `ip_source` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'IP来源',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_operation_log
-- ----------------------------
INSERT INTO `tb_operation_log` VALUES (1, 1, '/back/user/menus', '登录', '菜单模块', 'com.iksling.blog.controller.MenuControllerlistUserMenus', '查看用户菜单', '[]', 'GET', '{\"code\":20000,\"data\":[{\"component\":\"/home/Home.vue\",\"icon\":\"el-icon-odometer\",\"id\":1,\"isDisabled\":0,\"name\":\"首页\",\"path\":\"/\",\"userId\":1},{\"children\":[{\"component\":\"/article/Article.vue\",\"icon\":\"el-icon-document-add\",\"id\":201,\"isDisabled\":0,\"name\":\"添加文章\",\"path\":\"/article\",\"userId\":1},{\"component\":\"/article/Articles.vue\",\"icon\":\"el-icon-document-copy\",\"id\":202,\"isDisabled\":0,\"name\":\"文章列表\",\"path\":\"/articles\",\"userId\":1},{\"component\":\"/article/Category.vue\",\"icon\":\"el-icon-files\",\"id\":203,\"isDisabled\":0,\"name\":\"分类管理\",\"path\":\"/category\",\"userId\":1},{\"component\":\"/article/Tag.vue\",\"icon\":\"el-icon-collection-tag\",\"id\":204,\"isDisabled\":0,\"name\":\"标签管理\",\"path\":\"/tag\",\"userId\":1}],\"component\":\"Layout\",\"icon\":\"el-icon-document\",\"id\":2,\"isDisabled\":0,\"name\":\"文章管理\",\"path\":\"/article-menu\",\"userId\":1},{\"children\":[{\"component\":\"/message/Comment.vue\",\"icon\":\"el-icon-chat-dot-square\",\"id\":301,\"isDisabled\":0,\"name\":\"评论管理\",\"path\":\"/comment\",\"userId\":1},{\"component\":\"/message/Message.vue\",\"icon\":\"el-icon-chat-dot-round\",\"id\":302,\"isDisabled\":0,\"name\":\"留言管理\",\"path\":\"/message\",\"userId\":1}],\"component\":\"Layout\",\"icon\":\"el-icon-message\",\"id\":3,\"isDisabled\":0,\"name\":\"消息管理\",\"path\":\"/message-menu\",\"userId\":1},{\"children\":[{\"component\":\"/link/Friend.vue\",\"icon\":\"el-icon-connection\",\"id\":401,\"isDisabled\":0,\"name\":\"友链管理\",\"path\":\"/friend\",\"userId\":1}],\"component\":\"Layout\",\"icon\":\"el-icon-link\",\"id\":4,\"isDisabled\":0,\"name\":\"链接管理\",\"path\":\"/link-menu\",\"userId\":1},{\"children\":[{\"component\":\"/statistic/Article.vue\",\"icon\":\"el-icon-tickets\",\"id\":501,\"isDisabled\":0,\"name\":\"文章统计\",\"path\":\"/article-statistic\",\"userId\":1},{\"component\":\"/statistic/Message.vue\",\"icon\":\"el-icon-chat-line-square\",\"id\":502,\"isDisabled\":0,\"name\":\"留言统计\",\"path\":\"/message-statistic\",\"userId\":1},{\"component\":\"/statistic/User.vue\",\"icon\":\"el-icon-s-check\",\"id\":503,\"isDisabled\":0,\"name\":\"用户统计\",\"path\":\"/user-statistic\",\"userId\":1}],\"component\":\"Layout\",\"icon\":\"el-icon-s-data\",\"id\":5,\"isDisabled\":0,\"name\":\"数据统计\",\"path\":\"/statistic-menu\",\"userId\":1},{\"children\":[{\"component\":\"/system/Base.vue\",\"icon\":\"el-icon-coin\",\"id\":601,\"isDisabled\":0,\"name\":\"基础配置\",\"path\":\"/base\",\"userId\":1},{\"component\":\"/system/Menu.vue\",\"icon\":\"el-icon-menu\",\"id\":602,\"isDisabled\":0,\"name\":\"菜单管理\",\"path\":\"/menu\",\"userId\":1},{\"component\":\"/system/Resource.vue\",\"icon\":\"el-icon-s-grid\",\"id\":603,\"isDisabled\":0,\"name\":\"资源管理\",\"path\":\"/resource\",\"userId\":1},{\"component\":\"/system/Role.vue\",\"icon\":\"el-icon-s-custom\",\"id\":604,\"isDisabled\":0,\"name\":\"角色管理\",\"path\":\"/role\",\"userId\":1}],\"component\":\"Layout\",\"icon\":\"el-icon-setting\",\"id\":6,\"isDisabled\":0,\"name\":\"系统管理\",\"path\":\"/system-menu\",\"userId\":1},{\"children\":[{\"component\":\"/user/Online.vue\",\"icon\":\"el-icon-coordinate\",\"id\":701,\"isDisabled\":0,\"name\":\"在线用户\",\"path\":\"/online-user\",\"userId\":1},{\"component\":\"/user/Users.vue\",\"icon\":\"el-icon-user-solid\",\"id\":702,\"isDisabled\":0,\"name\":\"用户列表\",\"path\":\"/users\",\"userId\":1}],\"component\":\"Layout\",\"icon\":\"el-icon-user\",\"id\":7,\"isDisabled\":0,\"name\":\"用户管理\",\"path\":\"/user-menu\",\"userId\":1},{\"children\":[{\"component\":\"/log/Login.vue\",\"icon\":\"el-icon-map-location\",\"id\":801,\"isDisabled\":0,\"name\":\"登录日志\",\"path\":\"/login\",\"userId\":1},{\"component\":\"/log/Operation.vue\",\"icon\":\"el-icon-receiving\",\"id\":802,\"isDisabled\":0,\"name\":\"操作日志\",\"path\":\"/operation\",\"userId\":1}],\"component\":\"Layout\",\"icon\":\"el-icon-notebook-1\",\"id\":8,\"isDisabled\":0,\"name\":\"日志管理\",\"path\":\"/log-menu\",\"userId\":1},{\"children\":[{\"component\":\"/personal/About.vue\",\"icon\":\"el-icon-place\",\"id\":901,\"isDisabled\":0,\"name\":\"关于我\",\"path\":\"/about\",\"userId\":1},{\"component\":\"/personal/Personal.vue\",\"icon\":\"el-icon-postcard\",\"id\":902,\"isDisabled\":0,\"name\":\"个人配置\",\"path\":\"/personal\",\"userId\":1}],\"component\":\"Layout\",\"icon\":\"el-icon-s-home\",\"id\":9,\"isDisabled\":0,\"name\":\"个人中心\",\"path\":\"/personal-menu\",\"userId\":1}],\"flag\":true,\"message\":\"查询成功\"}', '127.0.0.1', '', '2023-05-31 20:52:02');

-- ----------------------------
-- Table structure for tb_qq_auth
-- ----------------------------
DROP TABLE IF EXISTS `tb_qq_auth`;
CREATE TABLE `tb_qq_auth`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '账号id',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `openid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'QQ的openid',
  `access_token` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '访问QQ的token',
  `login_time` datetime(0) NOT NULL COMMENT '登录时间',
  `login_device` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '登录设备',
  `ip_address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'IP地址',
  `ip_source` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'IP来源',
  `is_locked` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未锁定，1已锁定',
  `is_disabled` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未禁用，1已禁用',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_qq_auth
-- ----------------------------
INSERT INTO `tb_qq_auth` VALUES (1, 1, 'admin@qq.com', '$2a$10$EEJEoO4JH09hUTWezz9pq.pYk/HU2HDigdSWIos9GfFODrnXVcrHe', '2023-04-25 08:44:26', '', '', '', 0, 1, '2023-04-25 08:44:35', NULL);

-- ----------------------------
-- Table structure for tb_resource
-- ----------------------------
DROP TABLE IF EXISTS `tb_resource`;
CREATE TABLE `tb_resource`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '资源id',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `resource_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '资源名称',
  `resource_uri` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '资源路径',
  `request_method` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '请求方式',
  `parent_id` int(11) NOT NULL DEFAULT 0 COMMENT '父资源id',
  `is_disabled` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未禁用，1已禁用',
  `is_anonymous` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0不能匿名，1可以匿名',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 202 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_resource
-- ----------------------------
INSERT INTO `tb_resource` VALUES (1, 1, '菜单模块', '', '', 0, 0, 0, '2023-04-25 23:27:55', NULL);
INSERT INTO `tb_resource` VALUES (2, 1, '首页模块', '', '', 0, 0, 0, '2023-05-11 10:42:09', NULL);
INSERT INTO `tb_resource` VALUES (101, 1, '查看用户菜单', '/back/user/menus', 'GET', 1, 0, 0, '2023-04-26 21:33:02', NULL);
INSERT INTO `tb_resource` VALUES (201, 1, '查看后台首页信息', '/back', 'GET', 2, 0, 0, '2023-04-26 21:34:33', NULL);

-- ----------------------------
-- Table structure for tb_role
-- ----------------------------
DROP TABLE IF EXISTS `tb_role`;
CREATE TABLE `tb_role`  (
  `id` int(255) NOT NULL AUTO_INCREMENT COMMENT '角色id',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `role_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色名称',
  `role_desc` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '角色描述',
  `is_disabled` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未禁用，1已禁用',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_role
-- ----------------------------
INSERT INTO `tb_role` VALUES (1, 1, 'root', 'root', 0, '2023-04-25 08:47:06', NULL);
INSERT INTO `tb_role` VALUES (2, 1, 'admin', '管理员', 0, '2023-04-25 19:42:22', NULL);
INSERT INTO `tb_role` VALUES (3, 1, 'editor', '编辑', 0, '2023-04-25 19:44:08', NULL);
INSERT INTO `tb_role` VALUES (4, 1, 'author', '作者', 0, '2023-04-26 21:35:54', NULL);
INSERT INTO `tb_role` VALUES (5, 1, 'user', '用户', 0, '2023-04-26 21:36:11', NULL);
INSERT INTO `tb_role` VALUES (6, 1, 'test', 'test', 0, '2023-04-26 21:36:30', NULL);

-- ----------------------------
-- Table structure for tb_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `tb_role_menu`;
CREATE TABLE `tb_role_menu`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `role_id` int(11) NOT NULL COMMENT '角色id',
  `menu_id` int(11) NOT NULL COMMENT '菜单id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 30 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_role_menu
-- ----------------------------
INSERT INTO `tb_role_menu` VALUES (1, 1, 1);
INSERT INTO `tb_role_menu` VALUES (2, 1, 2);
INSERT INTO `tb_role_menu` VALUES (3, 1, 3);
INSERT INTO `tb_role_menu` VALUES (4, 1, 4);
INSERT INTO `tb_role_menu` VALUES (5, 1, 5);
INSERT INTO `tb_role_menu` VALUES (6, 1, 6);
INSERT INTO `tb_role_menu` VALUES (7, 1, 7);
INSERT INTO `tb_role_menu` VALUES (8, 1, 8);
INSERT INTO `tb_role_menu` VALUES (9, 1, 9);
INSERT INTO `tb_role_menu` VALUES (10, 1, 201);
INSERT INTO `tb_role_menu` VALUES (11, 1, 202);
INSERT INTO `tb_role_menu` VALUES (12, 1, 203);
INSERT INTO `tb_role_menu` VALUES (13, 1, 204);
INSERT INTO `tb_role_menu` VALUES (14, 1, 301);
INSERT INTO `tb_role_menu` VALUES (15, 1, 302);
INSERT INTO `tb_role_menu` VALUES (16, 1, 401);
INSERT INTO `tb_role_menu` VALUES (17, 1, 501);
INSERT INTO `tb_role_menu` VALUES (18, 1, 502);
INSERT INTO `tb_role_menu` VALUES (19, 1, 503);
INSERT INTO `tb_role_menu` VALUES (20, 1, 601);
INSERT INTO `tb_role_menu` VALUES (21, 1, 602);
INSERT INTO `tb_role_menu` VALUES (22, 1, 603);
INSERT INTO `tb_role_menu` VALUES (23, 1, 604);
INSERT INTO `tb_role_menu` VALUES (24, 1, 701);
INSERT INTO `tb_role_menu` VALUES (25, 1, 702);
INSERT INTO `tb_role_menu` VALUES (26, 1, 801);
INSERT INTO `tb_role_menu` VALUES (27, 1, 802);
INSERT INTO `tb_role_menu` VALUES (28, 1, 901);
INSERT INTO `tb_role_menu` VALUES (29, 1, 902);

-- ----------------------------
-- Table structure for tb_role_resource
-- ----------------------------
DROP TABLE IF EXISTS `tb_role_resource`;
CREATE TABLE `tb_role_resource`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `role_id` int(11) NOT NULL COMMENT '角色id',
  `resource_id` int(11) NOT NULL COMMENT '资源id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 305 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_role_resource
-- ----------------------------
INSERT INTO `tb_role_resource` VALUES (1, 1, 1);
INSERT INTO `tb_role_resource` VALUES (2, 1, 2);
INSERT INTO `tb_role_resource` VALUES (3, 1, 101);
INSERT INTO `tb_role_resource` VALUES (4, 1, 201);
INSERT INTO `tb_role_resource` VALUES (101, 2, 1);
INSERT INTO `tb_role_resource` VALUES (102, 2, 2);
INSERT INTO `tb_role_resource` VALUES (103, 2, 101);
INSERT INTO `tb_role_resource` VALUES (104, 2, 201);
INSERT INTO `tb_role_resource` VALUES (201, 3, 1);
INSERT INTO `tb_role_resource` VALUES (202, 3, 2);
INSERT INTO `tb_role_resource` VALUES (203, 3, 101);
INSERT INTO `tb_role_resource` VALUES (204, 3, 201);
INSERT INTO `tb_role_resource` VALUES (301, 4, 1);
INSERT INTO `tb_role_resource` VALUES (302, 4, 2);
INSERT INTO `tb_role_resource` VALUES (303, 4, 101);
INSERT INTO `tb_role_resource` VALUES (304, 4, 201);

-- ----------------------------
-- Table structure for tb_tag
-- ----------------------------
DROP TABLE IF EXISTS `tb_tag`;
CREATE TABLE `tb_tag`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '标签id',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `tag_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标签名称',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_tag
-- ----------------------------

-- ----------------------------
-- Table structure for tb_user
-- ----------------------------
DROP TABLE IF EXISTS `tb_user`;
CREATE TABLE `tb_user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `nickname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户昵称',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户头像',
  `intro` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '用户介绍',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '用户邮箱',
  `website` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '用户网站',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_user
-- ----------------------------
INSERT INTO `tb_user` VALUES (1, 'root', 'https://ksling.cn/static/img/avatar/defaultAvatar.jpg', '', '', '', '2023-04-25 08:46:25', NULL);
INSERT INTO `tb_user` VALUES (2, 'admin', 'https://ksling.cn/static/img/avatar/defaultAvatar.jpg', '', '', '', '2023-04-25 08:46:25', NULL);
INSERT INTO `tb_user` VALUES (3, 'editor', 'https://ksling.cn/static/img/avatar/defaultAvatar.jpg', '', '', '', '2023-04-25 08:46:25', NULL);
INSERT INTO `tb_user` VALUES (4, 'author', 'https://ksling.cn/static/img/avatar/defaultAvatar.jpg', '', '', '', '2023-04-25 08:46:25', NULL);
INSERT INTO `tb_user` VALUES (5, 'user', 'https://ksling.cn/static/img/avatar/defaultAvatar.jpg', '', '', '', '2023-04-25 08:46:25', NULL);
INSERT INTO `tb_user` VALUES (6, 'test', 'https://ksling.cn/static/img/avatar/defaultAvatar.jpg', '', '', '', '2023-04-25 08:46:25', NULL);

-- ----------------------------
-- Table structure for tb_user_auth
-- ----------------------------
DROP TABLE IF EXISTS `tb_user_auth`;
CREATE TABLE `tb_user_auth`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '账号id',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名称',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户密码',
  `login_time` datetime(0) NOT NULL COMMENT '登录时间',
  `login_device` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '登录设备',
  `ip_address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'IP地址',
  `ip_source` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'IP来源',
  `is_locked` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未锁定，1已锁定',
  `is_disabled` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未禁用，1已禁用',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_user_auth
-- ----------------------------
INSERT INTO `tb_user_auth` VALUES (1, 1, 'root@qq.com', '$2a$10$EEJEoO4JH09hUTWezz9pq.pYk/HU2HDigdSWIos9GfFODrnXVcrHe', '2023-05-31 20:52:01', 'Computer', '127.0.0.1', '', 0, 0, '2023-04-25 08:44:35', NULL);
INSERT INTO `tb_user_auth` VALUES (2, 2, 'admin@qq.com', '$2a$10$EEJEoO4JH09hUTWezz9pq.pYk/HU2HDigdSWIos9GfFODrnXVcrHe', '2023-05-09 11:13:39', 'Computer', '127.0.0.1', '', 0, 0, '2023-04-26 21:42:41', NULL);
INSERT INTO `tb_user_auth` VALUES (3, 3, 'editor@qq.com', '$2a$10$EEJEoO4JH09hUTWezz9pq.pYk/HU2HDigdSWIos9GfFODrnXVcrHe', '2023-04-26 21:43:25', '', '', '', 0, 0, '2023-04-26 21:43:33', NULL);
INSERT INTO `tb_user_auth` VALUES (4, 4, 'author@qq.com', '$2a$10$EEJEoO4JH09hUTWezz9pq.pYk/HU2HDigdSWIos9GfFODrnXVcrHe', '2023-04-26 21:43:47', '', '', '', 0, 0, '2023-04-26 21:43:50', NULL);
INSERT INTO `tb_user_auth` VALUES (5, 5, 'user@qq.com', '$2a$10$EEJEoO4JH09hUTWezz9pq.pYk/HU2HDigdSWIos9GfFODrnXVcrHe', '2023-04-26 21:44:06', '', '', '', 0, 0, '2023-04-26 21:44:09', NULL);
INSERT INTO `tb_user_auth` VALUES (6, 6, 'test@qq.com', '$2a$10$EEJEoO4JH09hUTWezz9pq.pYk/HU2HDigdSWIos9GfFODrnXVcrHe', '2023-04-26 21:44:26', '', '', '', 0, 0, '2023-04-26 21:44:30', NULL);

-- ----------------------------
-- Table structure for tb_user_config
-- ----------------------------
DROP TABLE IF EXISTS `tb_user_config`;
CREATE TABLE `tb_user_config`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '配置id',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `config_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '配置名',
  `config_value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '配置值',
  `config_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '配置描述',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未删除，1已删除',
  `is_deletable` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0可以删除，1不可以删除',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_user_config
-- ----------------------------

-- ----------------------------
-- Table structure for tb_user_role
-- ----------------------------
DROP TABLE IF EXISTS `tb_user_role`;
CREATE TABLE `tb_user_role`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `role_id` int(11) NOT NULL COMMENT '角色id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_user_role
-- ----------------------------
INSERT INTO `tb_user_role` VALUES (1, 1, 1);
INSERT INTO `tb_user_role` VALUES (2, 2, 2);
INSERT INTO `tb_user_role` VALUES (3, 3, 3);
INSERT INTO `tb_user_role` VALUES (4, 4, 4);
INSERT INTO `tb_user_role` VALUES (5, 5, 5);
INSERT INTO `tb_user_role` VALUES (6, 6, 6);

SET FOREIGN_KEY_CHECKS = 1;
