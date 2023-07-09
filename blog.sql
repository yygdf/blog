/*
 Navicat Premium Data Transfer

 Source Server         : 192.168.203.130
 Source Server Type    : MySQL
 Source Server Version : 50731
 Source Host           : 192.168.203.130:3306
 Source Schema         : blog

 Target Server Type    : MySQL
 Target Server Version : 50731
 File Encoding         : 65001

 Date: 09/07/2023 11:51:28
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
  `category_id` int(11) NOT NULL DEFAULT -1 COMMENT '分类id',
  `article_title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文章标题',
  `article_cover` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '文章封面',
  `article_content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文章内容',
  `top_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未置顶，1已置顶',
  `draft_flag` tinyint(1) NOT NULL DEFAULT 1 COMMENT '0不是草稿，1是草稿',
  `public_flag` tinyint(1) NOT NULL DEFAULT 1 COMMENT '0未公开，1已公开',
  `hidden_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未隐藏，1已隐藏',
  `deleted_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未删除，1已删除',
  `garbage_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0不是垃圾，1是垃圾',
  `commentable_flag` tinyint(1) NOT NULL DEFAULT 1 COMMENT '0不可评论，1可评论',
  `ip_source` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'ip来源',
  `ip_address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'ip地址',
  `create_user` int(11) NOT NULL COMMENT '创建人',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_user` int(11) NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_article
-- ----------------------------
INSERT INTO `tb_article` VALUES (1, 2, 1, '测试文章1', 'http://192.168.203.130/static/img/article/2/1/1677835052211286018.jpg', '# 测试文章1\n![beijing1.jpeg](http://192.168.203.130/static/img/article/2/1/1677834997010051074.jpeg)\n\n![beijing2.jpg](http://192.168.203.130/static/img/article/2/1/1677838235826434049.jpg)', 1, 0, 1, 0, 0, 0, 1, '', '127.0.0.1', 2, '2023-07-09 08:20:10', 2, '2023-07-09 08:33:39');
INSERT INTO `tb_article` VALUES (2, 2, 2, '测试文章2', '', '1. 哈哈哈哈哈哈\n2. 呵呵呵呵', 1, 0, 1, 1, 0, 0, 1, '', '127.0.0.1', 2, '2023-07-09 09:51:12', NULL, NULL);

-- ----------------------------
-- Table structure for tb_article_tag
-- ----------------------------
DROP TABLE IF EXISTS `tb_article_tag`;
CREATE TABLE `tb_article_tag`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `tag_id` int(11) NOT NULL COMMENT '标签id',
  `article_id` int(11) NOT NULL COMMENT '文章id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_article_tag
-- ----------------------------
INSERT INTO `tb_article_tag` VALUES (1, 1, 1);
INSERT INTO `tb_article_tag` VALUES (2, 2, 2);

-- ----------------------------
-- Table structure for tb_base_config
-- ----------------------------
DROP TABLE IF EXISTS `tb_base_config`;
CREATE TABLE `tb_base_config`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '配置id',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `config_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '配置描述',
  `config_value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '配置值',
  `config_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '配置名',
  `deleted_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未删除，1已删除',
  `deletable_flag` tinyint(1) NOT NULL DEFAULT 1 COMMENT '0不可删除，1可删除',
  `create_user` int(11) NOT NULL COMMENT '创建人',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_user` int(11) NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_base_config
-- ----------------------------
INSERT INTO `tb_base_config` VALUES (1, 2, '首页封面图', 'https://ksling.cn/static/img/page/home.jpg', 'home_cover', 0, 0, 1, '2023-05-12 18:37:22', NULL, NULL);
INSERT INTO `tb_base_config` VALUES (2, 2, '文章封面图', 'https://ksling.cn/static/img/page/archive.jpg', 'article_cover', 0, 0, 1, '2023-05-12 18:38:04', NULL, NULL);
INSERT INTO `tb_base_config` VALUES (3, 2, '分类封面图', 'https://ksling.cn/static/img/page/category.jpg', 'category_cover', 0, 0, 1, '2023-05-12 18:39:18', NULL, NULL);
INSERT INTO `tb_base_config` VALUES (4, 2, '标签封面图', 'https://ksling.cn/static/img/page/tag.jpg', 'tag_cover', 0, 0, 1, '2023-05-12 18:39:53', NULL, NULL);
INSERT INTO `tb_base_config` VALUES (5, 2, '友链封面图', 'https://ksling.cn/static/img/page/link.jpg', 'link_cover', 0, 0, 1, '2023-05-12 18:40:19', NULL, NULL);
INSERT INTO `tb_base_config` VALUES (6, 2, '关于封面图', 'https://ksling.cn/static/img/page/about.jpg', 'about_cover', 0, 0, 1, '2023-05-12 18:40:44', NULL, NULL);
INSERT INTO `tb_base_config` VALUES (7, 2, '留言封面图', 'https://ksling.cn/static/img/page/message.jpg', 'message_cover', 0, 0, 1, '2023-05-12 18:41:26', NULL, NULL);
INSERT INTO `tb_base_config` VALUES (8, 2, '个人中心封面图', 'https://ksling.cn/static/img/page/personal.jpg', 'personal_cover', 0, 0, 1, '2023-05-12 18:42:11', NULL, NULL);
INSERT INTO `tb_base_config` VALUES (9, 2, '默认封面图', 'https://ksling.cn/static/img/page/article.jpg', 'default_cover', 0, 0, 1, '2023-05-12 18:43:27', NULL, NULL);
INSERT INTO `tb_base_config` VALUES (10, 2, '首页QQ链接', 'https://wpa.qq.com/msgrd?v=3&uin=294513634&site=qq&menu=yes', 'home_qq', 0, 0, 1, '2023-05-12 19:04:41', NULL, NULL);
INSERT INTO `tb_base_config` VALUES (11, 2, '首页Github链接', 'https://github.com/yygdf', 'home_github', 0, 0, 1, '2023-05-12 19:05:33', NULL, NULL);
INSERT INTO `tb_base_config` VALUES (12, 2, '首页Gitee链接', 'https://gitee.com/yygdf', 'home_gitee', 0, 0, 1, '2023-05-12 19:06:40', NULL, NULL);
INSERT INTO `tb_base_config` VALUES (13, 2, '首页横幅', '欢迎来到『有一个地方』', 'home_banner', 0, 0, 1, '2023-05-12 19:07:20', NULL, NULL);
INSERT INTO `tb_base_config` VALUES (14, 2, '默认用户头像', 'https://ksling.cn/static/img/avatar/defaultAvatar.jpg', 'default_avatar', 0, 0, 1, '2023-05-12 19:08:03', NULL, NULL);
INSERT INTO `tb_base_config` VALUES (15, 2, 'websocket监听地址', 'wss://ksling.cn/websocket', 'wss_address', 0, 0, 1, '2023-05-12 19:08:39', NULL, NULL);
INSERT INTO `tb_base_config` VALUES (16, 2, '首页公告', '暂无内容~~', 'home_notice', 0, 0, 1, '2023-05-12 19:12:25', NULL, NULL);

-- ----------------------------
-- Table structure for tb_category
-- ----------------------------
DROP TABLE IF EXISTS `tb_category`;
CREATE TABLE `tb_category`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '分类id',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `category_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '分类名',
  `public_flag` tinyint(1) NOT NULL DEFAULT 1 COMMENT '0未公开，1已公开',
  `hidden_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未隐藏，1已隐藏',
  `create_user` int(11) NOT NULL COMMENT '创建人',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_user` int(11) NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_category
-- ----------------------------
INSERT INTO `tb_category` VALUES (1, 2, '测试分类1', 1, 0, 2, '2023-07-08 09:39:02', NULL, NULL);
INSERT INTO `tb_category` VALUES (2, 2, '测试分类2', 1, 0, 2, '2023-07-08 09:39:18', NULL, NULL);
INSERT INTO `tb_category` VALUES (3, 2, '测试分类3', 1, 0, 2, '2023-07-08 09:39:30', NULL, NULL);

-- ----------------------------
-- Table structure for tb_chat_record
-- ----------------------------
DROP TABLE IF EXISTS `tb_chat_record`;
CREATE TABLE `tb_chat_record`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '聊天记录id',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户头像',
  `nickname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户昵称',
  `chat_type` tinyint(1) NOT NULL COMMENT '聊天类型',
  `chat_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '聊天内容',
  `recalled_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未撤回，1已撤回',
  `ip_source` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'ip来源',
  `ip_address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'ip地址',
  `create_user` int(11) NOT NULL COMMENT '创建人',
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
  `reply_id` int(11) NOT NULL DEFAULT -1 COMMENT '回复用户id',
  `article_id` int(11) NOT NULL COMMENT '文章id',
  `parent_id` int(11) NOT NULL DEFAULT -1 COMMENT '父评论id',
  `comment_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '评论内容',
  `deleted_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未删除，1已删除',
  `ip_source` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'ip来源',
  `ip_address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'ip地址',
  `create_user` int(11) NOT NULL COMMENT '创建人',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_comment
-- ----------------------------

-- ----------------------------
-- Table structure for tb_day_view
-- ----------------------------
DROP TABLE IF EXISTS `tb_day_view`;
CREATE TABLE `tb_day_view`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '访问记录id',
  `view_date` date NOT NULL COMMENT '日期',
  `view_count` int(11) NOT NULL COMMENT '访问量',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_day_view
-- ----------------------------

-- ----------------------------
-- Table structure for tb_friend_link
-- ----------------------------
DROP TABLE IF EXISTS `tb_friend_link`;
CREATE TABLE `tb_friend_link`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '友链id',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `link_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '友链地址',
  `link_desc` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '友链描述',
  `link_logo` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '友链图标',
  `link_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '友链名称',
  `create_user` int(11) NOT NULL COMMENT '创建人',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_user` int(11) NULL DEFAULT NULL COMMENT '更新人',
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
  `login_type` int(11) NOT NULL COMMENT '登录类型',
  `login_time` datetime(0) NOT NULL COMMENT '登录时间',
  `login_device` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '登录设备',
  `login_system` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '操作系统类型',
  `login_browser` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '浏览器类型',
  `ip_source` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'ip来源',
  `ip_address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'ip地址',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_login_log
-- ----------------------------
INSERT INTO `tb_login_log` VALUES (1, 2, 1, '2023-07-09 08:12:04', 'Computer', 'Windows 10', 'Firefox 11', '', '127.0.0.1');
INSERT INTO `tb_login_log` VALUES (2, 2, 1, '2023-07-09 08:19:45', 'Computer', 'Windows 10', 'Firefox 11', '', '127.0.0.1');
INSERT INTO `tb_login_log` VALUES (3, 2, 1, '2023-07-09 08:35:59', 'Computer', 'Windows 10', 'Firefox 11', '', '127.0.0.1');
INSERT INTO `tb_login_log` VALUES (4, 2, 1, '2023-07-09 08:42:02', 'Computer', 'Windows 10', 'Firefox 11', '', '127.0.0.1');
INSERT INTO `tb_login_log` VALUES (5, 2, 1, '2023-07-09 08:46:41', 'Computer', 'Windows 10', 'Firefox 11', '', '127.0.0.1');
INSERT INTO `tb_login_log` VALUES (6, 2, 1, '2023-07-09 08:48:44', 'Computer', 'Windows 10', 'Firefox 11', '', '127.0.0.1');
INSERT INTO `tb_login_log` VALUES (7, 2, 1, '2023-07-09 08:51:34', 'Computer', 'Windows 10', 'Firefox 11', '', '127.0.0.1');
INSERT INTO `tb_login_log` VALUES (8, 2, 1, '2023-07-09 08:52:58', 'Computer', 'Windows 10', 'Firefox 11', '', '127.0.0.1');
INSERT INTO `tb_login_log` VALUES (9, 2, 1, '2023-07-09 08:55:55', 'Computer', 'Windows 10', 'Firefox 11', '', '127.0.0.1');
INSERT INTO `tb_login_log` VALUES (10, 2, 1, '2023-07-09 09:02:34', 'Computer', 'Windows 10', 'Firefox 11', '', '127.0.0.1');
INSERT INTO `tb_login_log` VALUES (11, 2, 1, '2023-07-09 09:08:01', 'Computer', 'Windows 10', 'Firefox 11', '', '127.0.0.1');
INSERT INTO `tb_login_log` VALUES (12, 2, 1, '2023-07-09 09:50:14', 'Computer', 'Windows 10', 'Firefox 11', '', '127.0.0.1');
INSERT INTO `tb_login_log` VALUES (13, 2, 1, '2023-07-09 09:52:59', 'Computer', 'Windows 10', 'Firefox 11', '', '127.0.0.1');
INSERT INTO `tb_login_log` VALUES (14, 2, 1, '2023-07-09 09:55:05', 'Computer', 'Windows 10', 'Firefox 11', '', '127.0.0.1');
INSERT INTO `tb_login_log` VALUES (15, 2, 1, '2023-07-09 10:02:55', 'Computer', 'Windows 10', 'Firefox 11', '', '127.0.0.1');
INSERT INTO `tb_login_log` VALUES (16, 2, 1, '2023-07-09 10:11:15', 'Computer', 'Windows 10', 'Firefox 11', '', '127.0.0.1');
INSERT INTO `tb_login_log` VALUES (17, 2, 1, '2023-07-09 11:34:49', 'Computer', 'Windows 10', 'Firefox 11', '', '127.0.0.1');
INSERT INTO `tb_login_log` VALUES (18, 2, 1, '2023-07-09 11:45:24', 'Computer', 'Windows 10', 'Firefox 11', '', '127.0.0.1');
INSERT INTO `tb_login_log` VALUES (19, 2, 1, '2023-07-09 11:48:15', 'Computer', 'Windows 10', 'Firefox 11', '', '127.0.0.1');
INSERT INTO `tb_login_log` VALUES (20, 2, 1, '2023-07-09 11:49:38', 'Computer', 'Windows 10', 'Firefox 11', '', '127.0.0.1');

-- ----------------------------
-- Table structure for tb_menu
-- ----------------------------
DROP TABLE IF EXISTS `tb_menu`;
CREATE TABLE `tb_menu`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '菜单id',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `parent_id` int(11) NOT NULL DEFAULT -1 COMMENT '父菜单id',
  `icon` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '菜单图标',
  `rank` tinyint(1) NOT NULL DEFAULT 127 COMMENT '排序指标',
  `path` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '菜单路径',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '菜单名称',
  `component` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '菜单组件',
  `hide_flag` tinyint(1) NOT NULL COMMENT '0未隐藏，1已隐藏',
  `hidden_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未隐藏，1已隐藏',
  `disabled_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未禁用，1已禁用',
  `deletable_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0不可删除，1可删除',
  `create_user` int(11) NOT NULL COMMENT '创建人',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_user` int(11) NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 903 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_menu
-- ----------------------------
INSERT INTO `tb_menu` VALUES (1, 2, -1, 'el-icon-odometer', 1, '/', '首页', '/home/Home.vue', 0, 0, 0, 0, 2, '2023-04-26 23:04:13', NULL, NULL);
INSERT INTO `tb_menu` VALUES (2, 2, -1, 'el-icon-document', 2, '/article-menu', '文章管理', 'Layout', 0, 0, 0, 0, 2, '2023-05-12 13:30:18', NULL, NULL);
INSERT INTO `tb_menu` VALUES (3, 2, -1, 'el-icon-message', 3, '/message-menu', '消息管理', 'Layout', 0, 0, 0, 0, 2, '2023-05-12 13:31:28', NULL, NULL);
INSERT INTO `tb_menu` VALUES (4, 2, -1, 'el-icon-link', 4, '/link-menu', '链接管理', 'Layout', 0, 0, 0, 0, 2, '2023-05-12 13:33:40', NULL, NULL);
INSERT INTO `tb_menu` VALUES (5, 2, -1, 'el-icon-s-data', 5, '/statistic-menu', '数据统计', 'Layout', 0, 0, 0, 0, 2, '2023-05-12 13:37:45', NULL, NULL);
INSERT INTO `tb_menu` VALUES (6, 2, -1, 'el-icon-setting', 6, '/system-menu', '系统管理', 'Layout', 0, 0, 0, 0, 2, '2023-05-12 13:40:50', NULL, NULL);
INSERT INTO `tb_menu` VALUES (7, 2, -1, 'el-icon-user', 7, '/user-menu', '用户管理', 'Layout', 0, 0, 0, 0, 2, '2023-05-12 13:43:01', NULL, NULL);
INSERT INTO `tb_menu` VALUES (8, 2, -1, 'el-icon-notebook-1', 8, '/log-menu', '日志管理', 'Layout', 0, 0, 0, 0, 2, '2023-05-12 13:50:43', NULL, NULL);
INSERT INTO `tb_menu` VALUES (9, 2, -1, 'el-icon-s-home', 9, '/personal-menu', '个人中心', 'Layout', 0, 0, 0, 0, 2, '2023-05-12 13:52:12', NULL, NULL);
INSERT INTO `tb_menu` VALUES (201, 2, 2, 'el-icon-document-add', 21, '/article', '添加文章', '/article/Article.vue', 0, 0, 0, 0, 2, '2023-05-12 13:57:27', NULL, NULL);
INSERT INTO `tb_menu` VALUES (202, 2, 2, 'el-icon-document-copy', 22, '/articles', '文章列表', '/article/Articles.vue', 0, 0, 0, 0, 2, '2023-05-12 14:01:25', NULL, NULL);
INSERT INTO `tb_menu` VALUES (203, 2, 2, 'el-icon-files', 23, '/category', '分类管理', '/article/Category.vue', 0, 0, 1, 0, 2, '2023-05-12 14:09:01', NULL, NULL);
INSERT INTO `tb_menu` VALUES (204, 2, 2, 'el-icon-collection-tag', 24, '/tag', '标签管理', '/article/Tag.vue', 0, 1, 0, 0, 2, '2023-05-12 14:10:41', NULL, NULL);
INSERT INTO `tb_menu` VALUES (205, 2, 2, 'el-icon-document-add', 25, '/article/*', '修改文章', '/article/Article.vue', 1, 0, 0, 0, 2, '2023-07-09 09:58:07', NULL, NULL);
INSERT INTO `tb_menu` VALUES (301, 2, 3, 'el-icon-chat-dot-square', 31, '/comment', '评论管理', '/message/Comment.vue', 0, 0, 0, 0, 2, '2023-05-12 14:16:31', NULL, NULL);
INSERT INTO `tb_menu` VALUES (302, 2, 3, 'el-icon-chat-dot-round', 32, '/message', '留言管理', '/message/Message.vue', 0, 0, 0, 0, 2, '2023-05-12 14:17:18', NULL, NULL);
INSERT INTO `tb_menu` VALUES (401, 2, 4, 'el-icon-connection', 41, '/friend', '友链管理', '/link/Friend.vue', 0, 0, 0, 0, 2, '2023-05-12 17:37:47', NULL, NULL);
INSERT INTO `tb_menu` VALUES (501, 2, 5, 'el-icon-tickets', 51, '/article-statistic', '文章统计', '/statistic/Article.vue', 0, 0, 0, 0, 2, '2023-05-12 14:22:50', NULL, NULL);
INSERT INTO `tb_menu` VALUES (502, 2, 5, 'el-icon-chat-line-square', 52, '/message-statistic', '留言统计', '/statistic/Message.vue', 0, 0, 0, 0, 2, '2023-05-12 14:25:34', NULL, NULL);
INSERT INTO `tb_menu` VALUES (503, 2, 5, 'el-icon-s-check', 53, '/user-statistic', '用户统计', '/statistic/User.vue', 0, 0, 0, 0, 2, '2023-05-12 14:27:43', NULL, NULL);
INSERT INTO `tb_menu` VALUES (601, 2, 6, 'el-icon-coin', 61, '/base', '基础配置', '/system/Base.vue', 0, 0, 0, 0, 2, '2023-05-12 14:32:36', NULL, NULL);
INSERT INTO `tb_menu` VALUES (602, 2, 6, 'el-icon-menu', 62, '/menu', '菜单管理', '/system/Menu.vue', 0, 0, 0, 0, 2, '2023-05-12 14:44:36', NULL, NULL);
INSERT INTO `tb_menu` VALUES (603, 2, 6, 'el-icon-s-grid', 63, '/resource', '资源管理', '/system/Resource.vue', 0, 0, 0, 0, 2, '2023-05-12 15:33:35', NULL, NULL);
INSERT INTO `tb_menu` VALUES (604, 2, 6, 'el-icon-s-custom', 64, '/role', '角色管理', '/system/Role.vue', 0, 0, 0, 0, 2, '2023-05-12 15:37:11', NULL, NULL);
INSERT INTO `tb_menu` VALUES (701, 2, 7, 'el-icon-coordinate', 71, '/online-user', '在线用户', '/user/Online.vue', 0, 0, 0, 0, 2, '2023-05-12 14:38:37', NULL, NULL);
INSERT INTO `tb_menu` VALUES (702, 2, 7, 'el-icon-user-solid', 72, '/users', '用户列表', '/user/Users.vue', 0, 0, 0, 0, 2, '2023-05-12 14:41:42', NULL, NULL);
INSERT INTO `tb_menu` VALUES (801, 2, 8, 'el-icon-map-location', 81, '/login', '登录日志', '/log/Login.vue', 0, 0, 0, 0, 2, '2023-05-12 15:44:03', NULL, NULL);
INSERT INTO `tb_menu` VALUES (802, 2, 8, 'el-icon-receiving', 82, '/operation', '操作日志', '/log/Operation.vue', 0, 0, 0, 0, 2, '2023-05-12 15:49:53', NULL, NULL);
INSERT INTO `tb_menu` VALUES (901, 2, 9, 'el-icon-place', 91, '/about', '关于我', '/personal/About.vue', 0, 0, 0, 0, 2, '2023-05-12 16:00:46', NULL, NULL);
INSERT INTO `tb_menu` VALUES (902, 2, 9, 'el-icon-postcard', 92, '/personal', '个人配置', '/personal/Personal.vue', 0, 0, 0, 0, 2, '2023-05-12 14:36:02', NULL, NULL);

-- ----------------------------
-- Table structure for tb_message
-- ----------------------------
DROP TABLE IF EXISTS `tb_message`;
CREATE TABLE `tb_message`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '留言id',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户头像',
  `nickname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户昵称',
  `message_speed` tinyint(1) NOT NULL COMMENT '留言速度',
  `message_content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '留言内容',
  `ip_source` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'ip来源',
  `ip_address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'ip地址',
  `create_user` int(11) NOT NULL COMMENT '创建人',
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
  `dir_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '目录链接',
  `dir_size` int(11) NOT NULL COMMENT '目录大小',
  `dir_unit` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '大小单位',
  `dir_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '目录路径',
  `dir_desc` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '目录描述',
  `dir_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '目录名称',
  `dir_cover` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '目录封面',
  `public_flag` tinyint(1) NOT NULL DEFAULT 1 COMMENT '0未公开，1已公开',
  `hidden_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未隐藏，1已隐藏',
  `deleted_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未删除，1已删除',
  `deletable_flag` tinyint(1) NOT NULL DEFAULT 1 COMMENT '0不可删除，1可删除',
  `create_user` int(11) NOT NULL COMMENT '创建人',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_user` int(11) NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_multi_dir
-- ----------------------------
INSERT INTO `tb_multi_dir` VALUES (1, 2, '123', 1, 'MB', 'voice/chat/', '聊天室音频目录', 'chat', '', 1, 0, 0, 0, 2, '2023-07-08 10:49:43', NULL, NULL);
INSERT INTO `tb_multi_dir` VALUES (2, 2, '123', 1, 'MB', 'img/avatar/', '用户头像目录', 'avatar', '', 1, 0, 0, 0, 2, '2023-07-08 10:54:54', NULL, NULL);
INSERT INTO `tb_multi_dir` VALUES (3, 2, '123', 5, 'MB', 'img/article/', '文章图片目录', 'article', '', 1, 0, 0, 0, 2, '2023-07-08 10:55:24', NULL, NULL);

-- ----------------------------
-- Table structure for tb_multi_file
-- ----------------------------
DROP TABLE IF EXISTS `tb_multi_file`;
CREATE TABLE `tb_multi_file`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '文件id',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `multi_dir_id` int(11) NOT NULL COMMENT '父目录id',
  `file_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件地址',
  `file_desc` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '文件描述',
  `file_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '文件名称',
  `file_sub_dir` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '文件子目录',
  `hidden_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未隐藏，1已隐藏',
  `deleted_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未删除，1已删除',
  `ip_source` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'ip来源',
  `ip_address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'ip地址',
  `create_user` int(11) NOT NULL COMMENT '创建人',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_user` int(11) NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_multi_file
-- ----------------------------
INSERT INTO `tb_multi_file` VALUES (1, 2, 3, 'http://192.168.203.130/static/img/article/2/1/1677834997010051074.jpeg', '用户: root@qq.com, 文章id: 1 中的图片', 'beijing1.jpeg', '2/1/', 0, 0, '', '127.0.0.1', 2, '2023-07-09 08:20:12', NULL, NULL);
INSERT INTO `tb_multi_file` VALUES (2, 2, 3, 'http://192.168.203.130/static/img/article/2/1/1677835052211286018.jpg', '用户: root@qq.com, 文章id: 1 中的图片', 'beijing3.jpg', '2/1/', 0, 0, '', '127.0.0.1', 2, '2023-07-09 08:20:24', NULL, NULL);
INSERT INTO `tb_multi_file` VALUES (3, 2, 3, 'http://192.168.203.130/static/img/article/2/1/1677838235826434049.jpg', '用户: root@qq.com, 文章id: 1 中的图片', 'beijing2.jpg', '2/1/', 0, 0, '', '127.0.0.1', 2, '2023-07-09 08:33:03', NULL, NULL);

-- ----------------------------
-- Table structure for tb_operation_log
-- ----------------------------
DROP TABLE IF EXISTS `tb_operation_log`;
CREATE TABLE `tb_operation_log`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '操作日志id',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `opt_uri` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '操作路径',
  `opt_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '操作类型',
  `opt_desc` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '操作描述',
  `opt_module` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '操作模块',
  `opt_method` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '操作方法',
  `opt_request_param` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '请求参数',
  `opt_request_method` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '请求方式',
  `opt_response_data` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '响应数据',
  `ip_source` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'ip来源',
  `ip_address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'ip地址',
  `create_user` int(11) NOT NULL COMMENT '创建人',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_operation_log
-- ----------------------------
INSERT INTO `tb_operation_log` VALUES (1, 2, '/back/article', '新增或修改', '添加或修改文章', '文章模块', 'com.iksling.blog.controller.ArticleController.saveBackArticle', '[{\"articleContent\":\"# 测试文章1\\n![beijing1.jpeg](1)\",\"articleCover\":\"\",\"articleTitle\":\"测试文章1\",\"commentableFlag\":true,\"draftFlag\":true,\"hiddenFlag\":false,\"publicFlag\":true,\"tagIdList\":[],\"topFlag\":false}]', 'POST', '{\"code\":20000,\"data\":1,\"flag\":true,\"message\":\"操作成功\"}', '', '127.0.0.1', 2, '2023-07-09 08:20:11');
INSERT INTO `tb_operation_log` VALUES (2, 2, '/back/article/image', '上传', '上传文章图片', '文章模块', 'com.iksling.blog.controller.ArticleController.saveBackArticleImage', '[{\"fileSubDir\":\"1\"}]', 'POST', '{\"code\":20000,\"data\":\"http://192.168.203.130/static/img/article/2/1/1677834997010051074.jpeg\",\"flag\":true,\"message\":\"上传成功\"}', '', '127.0.0.1', 2, '2023-07-09 08:20:12');
INSERT INTO `tb_operation_log` VALUES (3, 2, '/back/article/image', '上传', '上传文章图片', '文章模块', 'com.iksling.blog.controller.ArticleController.saveBackArticleImage', '[{\"fileSubDir\":\"1\"}]', 'POST', '{\"code\":20000,\"data\":\"http://192.168.203.130/static/img/article/2/1/1677835052211286018.jpg\",\"flag\":true,\"message\":\"上传成功\"}', '', '127.0.0.1', 2, '2023-07-09 08:20:24');
INSERT INTO `tb_operation_log` VALUES (4, 2, '/back/article/image', '上传', '上传文章图片', '文章模块', 'com.iksling.blog.controller.ArticleController.saveBackArticleImage', '[{\"fileSubDir\":\"1\"}]', 'POST', '{\"code\":20000,\"data\":\"http://192.168.203.130/static/img/article/2/1/1677838235826434049.jpg\",\"flag\":true,\"message\":\"上传成功\"}', '', '127.0.0.1', 2, '2023-07-09 08:33:03');
INSERT INTO `tb_operation_log` VALUES (5, 2, '/back/article', '新增或修改', '添加或修改文章', '文章模块', 'com.iksling.blog.controller.ArticleController.saveBackArticle', '[{\"articleContent\":\"# 测试文章1\\n![beijing1.jpeg](http://192.168.203.130/static/img/article/2/1/1677834997010051074.jpeg)\\n\\n![beijing2.jpg](http://192.168.203.130/static/img/article/2/1/1677838235826434049.jpg)\",\"articleCover\":\"http://192.168.203.130/static/img/article/2/1/1677835052211286018.jpg\",\"articleTitle\":\"测试文章1\",\"categoryId\":1,\"commentableFlag\":true,\"draftFlag\":false,\"hiddenFlag\":false,\"id\":1,\"publicFlag\":true,\"tagIdList\":[1],\"topFlag\":true}]', 'POST', '{\"code\":20000,\"data\":1,\"flag\":true,\"message\":\"操作成功\"}', '', '127.0.0.1', 2, '2023-07-09 08:33:39');
INSERT INTO `tb_operation_log` VALUES (6, 2, '/back/article', '新增或修改', '添加或修改文章', '文章模块', 'com.iksling.blog.controller.ArticleController.saveBackArticle', '[{\"articleContent\":\"1. 哈哈哈哈哈哈\\n2. 呵呵呵呵\",\"articleCover\":\"\",\"articleTitle\":\"测试文章2\",\"categoryId\":2,\"commentableFlag\":false,\"draftFlag\":false,\"hiddenFlag\":false,\"publicFlag\":true,\"tagIdList\":[2],\"topFlag\":true}]', 'POST', '{\"code\":20000,\"data\":2,\"flag\":true,\"message\":\"操作成功\"}', '', '127.0.0.1', 2, '2023-07-09 09:51:12');
INSERT INTO `tb_operation_log` VALUES (7, 2, '/back/article/top/2', '修改', '修改文章置顶', '文章模块', 'com.iksling.blog.controller.ArticleController.updateArticleTop', '[2,false]', 'PUT', '{\"code\":20000,\"flag\":true,\"message\":\"操作成功\"}', '', '127.0.0.1', 2, '2023-07-09 09:51:46');
INSERT INTO `tb_operation_log` VALUES (8, 2, '/back/article/public/2', '修改', '修改文章是否公开', '文章模块', 'com.iksling.blog.controller.ArticleController.updateArticlePublic', '[2,false]', 'PUT', '{\"code\":20000,\"flag\":true,\"message\":\"操作成功\"}', '', '127.0.0.1', 2, '2023-07-09 09:51:49');
INSERT INTO `tb_operation_log` VALUES (9, 2, '/back/article/hidden/2', '修改', '修改文章是否隐藏', '文章模块', 'com.iksling.blog.controller.ArticleController.updateArticleHidden', '[2,true]', 'PUT', '{\"code\":20000,\"flag\":true,\"message\":\"操作成功\"}', '', '127.0.0.1', 2, '2023-07-09 09:51:59');
INSERT INTO `tb_operation_log` VALUES (10, 2, '/back/article/commentable/2', '修改', '修改文章是否可评论', '文章模块', 'com.iksling.blog.controller.ArticleController.updateArticleCommentable', '[2,true]', 'PUT', '{\"code\":20000,\"flag\":true,\"message\":\"操作成功\"}', '', '127.0.0.1', 2, '2023-07-09 09:55:24');
INSERT INTO `tb_operation_log` VALUES (11, 2, '/back/article/top/2', '修改', '修改文章置顶', '文章模块', 'com.iksling.blog.controller.ArticleController.updateArticleTop', '[2,true]', 'PUT', '{\"code\":20000,\"flag\":true,\"message\":\"操作成功\"}', '', '127.0.0.1', 2, '2023-07-09 09:55:37');
INSERT INTO `tb_operation_log` VALUES (12, 2, '/back/article/public/2', '修改', '修改文章是否公开', '文章模块', 'com.iksling.blog.controller.ArticleController.updateArticlePublic', '[2,true]', 'PUT', '{\"code\":20000,\"flag\":true,\"message\":\"操作成功\"}', '', '127.0.0.1', 2, '2023-07-09 09:55:38');

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
  `locked_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未锁定，1已锁定',
  `disabled_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未禁用，1已禁用',
  `ip_source` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'ip来源',
  `ip_address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'ip地址',
  `create_user` int(11) NOT NULL COMMENT '创建人',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_user` int(11) NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_qq_auth
-- ----------------------------

-- ----------------------------
-- Table structure for tb_resource
-- ----------------------------
DROP TABLE IF EXISTS `tb_resource`;
CREATE TABLE `tb_resource`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '资源id',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `parent_id` int(11) NOT NULL DEFAULT 0 COMMENT '父资源id',
  `resource_uri` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '资源路径',
  `resource_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '资源名称',
  `resource_request_method` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '请求方式',
  `disabled_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未禁用，1已禁用',
  `deletable_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0不可删除，1可删除',
  `anonymous_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未匿名，1已匿名',
  `create_user` int(11) NOT NULL COMMENT '创建人',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_user` int(11) NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 902 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_resource
-- ----------------------------
INSERT INTO `tb_resource` VALUES (1, 2, -1, '', '菜单模块', '', 0, 0, 0, 2, '2023-04-25 23:27:55', NULL, NULL);
INSERT INTO `tb_resource` VALUES (2, 2, -1, '', '首页模块', '', 0, 0, 0, 2, '2023-05-11 10:42:09', NULL, NULL);
INSERT INTO `tb_resource` VALUES (3, 2, -1, '', '文章模块', '', 0, 0, 0, 2, '2023-07-08 09:08:55', NULL, NULL);
INSERT INTO `tb_resource` VALUES (9, 2, -1, '', '其他模块', '', 0, 0, 0, 2, '2023-07-02 11:16:14', NULL, NULL);
INSERT INTO `tb_resource` VALUES (101, 2, 1, '/back/user/menus', '查看用户菜单', 'GET', 0, 0, 0, 2, '2023-04-26 21:33:02', NULL, NULL);
INSERT INTO `tb_resource` VALUES (201, 2, 2, '/back', '查看后台首页信息', 'GET', 0, 0, 0, 2, '2023-04-26 21:34:33', NULL, NULL);
INSERT INTO `tb_resource` VALUES (301, 2, 3, '/back/article/*', '根据文章id查找文章', 'GET', 0, 0, 0, 2, '2023-07-08 09:10:16', NULL, NULL);
INSERT INTO `tb_resource` VALUES (302, 2, 3, '/back/article/options', '查看文章选项', 'GET', 0, 0, 0, 2, '2023-07-08 09:10:53', NULL, NULL);
INSERT INTO `tb_resource` VALUES (303, 2, 3, '/back/article', '添加或修改文章', 'POST', 0, 0, 0, 2, '2023-07-08 09:11:36', NULL, NULL);
INSERT INTO `tb_resource` VALUES (304, 2, 3, '/back/article/image', '上传文章图片', 'POST', 0, 0, 0, 2, '2023-07-08 09:12:09', NULL, NULL);
INSERT INTO `tb_resource` VALUES (305, 2, 3, '/back/articles', '查看后台文章列表', 'GET', 0, 0, 0, 2, '2023-07-08 14:46:19', NULL, NULL);
INSERT INTO `tb_resource` VALUES (306, 2, 3, '/back/articles', '更新文章是否为垃圾文章', 'PUT', 0, 0, 0, 2, '2023-07-08 18:24:10', NULL, NULL);
INSERT INTO `tb_resource` VALUES (307, 2, 3, '/back/articles', '逻辑删除文章', 'DELETE', 0, 0, 0, 2, '2023-07-08 18:51:02', NULL, NULL);
INSERT INTO `tb_resource` VALUES (308, 2, 3, '/back/article/top/*', '修改文章置顶', 'PUT', 0, 0, 0, 2, '2023-07-08 18:51:51', NULL, NULL);
INSERT INTO `tb_resource` VALUES (309, 2, 3, '/back/article/public/*', '修改文章是否公开', 'PUT', 0, 0, 0, 2, '2023-07-08 18:52:21', NULL, NULL);
INSERT INTO `tb_resource` VALUES (310, 2, 3, '/back/article/hidden/*', '修改文章是否隐藏', 'PUT', 0, 0, 0, 2, '2023-07-08 18:52:54', NULL, NULL);
INSERT INTO `tb_resource` VALUES (311, 2, 3, '/back/article/commentable/*', '修改文章是否可评论', 'PUT', 0, 0, 0, 2, '2023-07-08 18:53:19', NULL, NULL);
INSERT INTO `tb_resource` VALUES (901, 2, 9, '/swagger-ui.html', 'SwaggerUI', 'GET', 0, 0, 1, 2, '2023-07-02 11:17:04', NULL, NULL);

-- ----------------------------
-- Table structure for tb_role
-- ----------------------------
DROP TABLE IF EXISTS `tb_role`;
CREATE TABLE `tb_role`  (
  `id` int(255) NOT NULL AUTO_INCREMENT COMMENT '角色id',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `role_desc` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '角色描述',
  `role_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色名称',
  `disabled_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未禁用，1已禁用',
  `create_user` int(11) NOT NULL COMMENT '创建人',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_user` int(11) NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_role
-- ----------------------------
INSERT INTO `tb_role` VALUES (1, 2, 'root', 'root', 0, 0, '2023-04-25 08:47:06', NULL, NULL);
INSERT INTO `tb_role` VALUES (2, 2, '管理员', 'admin', 0, 0, '2023-04-25 19:42:22', NULL, NULL);
INSERT INTO `tb_role` VALUES (3, 2, '编辑', 'editor', 0, 0, '2023-04-25 19:44:08', NULL, NULL);
INSERT INTO `tb_role` VALUES (4, 2, '作者', 'author', 0, 0, '2023-04-26 21:35:54', NULL, NULL);
INSERT INTO `tb_role` VALUES (5, 2, '用户', 'user', 0, 0, '2023-04-26 21:36:11', NULL, NULL);
INSERT INTO `tb_role` VALUES (6, 2, 'test', 'test', 0, 0, '2023-04-26 21:36:30', NULL, NULL);

-- ----------------------------
-- Table structure for tb_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `tb_role_menu`;
CREATE TABLE `tb_role_menu`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `role_id` int(11) NOT NULL COMMENT '角色id',
  `menu_id` int(11) NOT NULL COMMENT '菜单id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 31 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

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
INSERT INTO `tb_role_menu` VALUES (30, 1, 205);

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
  `create_user` int(11) NOT NULL COMMENT '创建人',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_user` int(11) NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_tag
-- ----------------------------
INSERT INTO `tb_tag` VALUES (1, 2, '测试标签1', 2, '2023-07-08 09:39:56', NULL, NULL);
INSERT INTO `tb_tag` VALUES (2, 2, '测试标签2', 2, '2023-07-08 09:40:11', NULL, NULL);
INSERT INTO `tb_tag` VALUES (3, 2, '测试标签3', 2, '2023-07-08 09:40:21', NULL, NULL);

-- ----------------------------
-- Table structure for tb_user
-- ----------------------------
DROP TABLE IF EXISTS `tb_user`;
CREATE TABLE `tb_user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `intro` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '用户介绍',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '用户邮箱',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户头像',
  `website` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '用户网站',
  `nickname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户昵称',
  `create_user` int(11) NOT NULL COMMENT '创建人',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_user` int(11) NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 100000002 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_user
-- ----------------------------
INSERT INTO `tb_user` VALUES (0, '', '', 'https://ksling.cn/static/img/avatar/defaultAvatar.jpg', '', 'ling', 2, '2023-04-25 08:46:25', NULL, NULL);
INSERT INTO `tb_user` VALUES (1, '', '', 'https://ksling.cn/static/img/avatar/defaultAvatar.jpg', '', 'ks', 2, '2023-04-25 08:46:25', NULL, NULL);
INSERT INTO `tb_user` VALUES (2, '', '', 'https://ksling.cn/static/img/avatar/defaultAvatar.jpg', '', 'root', 2, '2023-04-25 08:46:25', NULL, NULL);
INSERT INTO `tb_user` VALUES (3, '', '', 'https://ksling.cn/static/img/avatar/defaultAvatar.jpg', '', 'admin', 2, '2023-04-25 08:46:25', NULL, NULL);
INSERT INTO `tb_user` VALUES (4, '', '', 'https://ksling.cn/static/img/avatar/defaultAvatar.jpg', '', 'editor', 2, '2023-04-25 08:46:25', NULL, NULL);
INSERT INTO `tb_user` VALUES (5, '', '', 'https://ksling.cn/static/img/avatar/defaultAvatar.jpg', '', 'author', 2, '2023-04-25 08:46:25', NULL, NULL);
INSERT INTO `tb_user` VALUES (6, '', '', 'https://ksling.cn/static/img/avatar/defaultAvatar.jpg', '', 'user', 2, '2023-04-25 08:46:25', NULL, NULL);
INSERT INTO `tb_user` VALUES (7, '', '', 'https://ksling.cn/static/img/avatar/defaultAvatar.jpg', '', 'test', 2, '2023-04-25 08:46:25', NULL, NULL);
INSERT INTO `tb_user` VALUES (100000001, '', '', 'https://ksling.cn/static/img/avatar/defaultAvatar.jpg', '', 'zhangsan', 2, '2023-04-25 08:46:25', NULL, NULL);

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
  `locked_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未锁定，1已锁定',
  `disabled_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未禁用，1已禁用',
  `ip_source` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'ip来源',
  `ip_address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'ip地址',
  `create_user` int(11) NOT NULL COMMENT '创建人',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_user` int(11) NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 100000002 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_user_auth
-- ----------------------------
INSERT INTO `tb_user_auth` VALUES (0, 0, 'ling@qq.com', '$2a$10$EEJEoO4JH09hUTWezz9pq.pYk/HU2HDigdSWIos9GfFODrnXVcrHe', '2023-04-26 21:44:26', '', 0, 0, '', '', 0, '2023-04-26 21:44:26', NULL, NULL);
INSERT INTO `tb_user_auth` VALUES (1, 1, 'ks@qq.com', '$2a$10$EEJEoO4JH09hUTWezz9pq.pYk/HU2HDigdSWIos9GfFODrnXVcrHe', '2023-04-26 21:44:26', '', 0, 0, '', '', 0, '2023-04-26 21:44:26', NULL, NULL);
INSERT INTO `tb_user_auth` VALUES (2, 2, 'root@qq.com', '$2a$10$EEJEoO4JH09hUTWezz9pq.pYk/HU2HDigdSWIos9GfFODrnXVcrHe', '2023-07-09 11:49:38', 'Computer', 0, 0, '', '127.0.0.1', 0, '2023-04-26 21:44:26', NULL, NULL);
INSERT INTO `tb_user_auth` VALUES (3, 3, 'admin@qq.com', '$2a$10$EEJEoO4JH09hUTWezz9pq.pYk/HU2HDigdSWIos9GfFODrnXVcrHe', '2023-04-26 21:44:26', '', 0, 0, '', '', 0, '2023-04-26 21:44:26', NULL, NULL);
INSERT INTO `tb_user_auth` VALUES (4, 4, 'editor@qq.com', '$2a$10$EEJEoO4JH09hUTWezz9pq.pYk/HU2HDigdSWIos9GfFODrnXVcrHe', '2023-04-26 21:44:26', '', 0, 0, '', '', 0, '2023-04-26 21:44:26', NULL, NULL);
INSERT INTO `tb_user_auth` VALUES (5, 5, 'author@qq.com', '$2a$10$EEJEoO4JH09hUTWezz9pq.pYk/HU2HDigdSWIos9GfFODrnXVcrHe', '2023-04-26 21:44:26', '', 0, 0, '', '', 0, '2023-04-26 21:44:26', NULL, NULL);
INSERT INTO `tb_user_auth` VALUES (6, 6, 'user@qq.com', '$2a$10$EEJEoO4JH09hUTWezz9pq.pYk/HU2HDigdSWIos9GfFODrnXVcrHe', '2023-04-26 21:44:26', '', 0, 0, '', '', 0, '2023-04-26 21:44:26', NULL, NULL);
INSERT INTO `tb_user_auth` VALUES (7, 7, 'test@qq.com', '$2a$10$EEJEoO4JH09hUTWezz9pq.pYk/HU2HDigdSWIos9GfFODrnXVcrHe', '2023-04-26 21:44:26', '', 0, 0, '', '', 0, '2023-04-26 21:44:26', NULL, NULL);
INSERT INTO `tb_user_auth` VALUES (100000001, 100000001, 'zhangsan@qq.com', '$2a$10$EEJEoO4JH09hUTWezz9pq.pYk/HU2HDigdSWIos9GfFODrnXVcrHe', '2023-04-26 21:44:26', '', 0, 0, '', '', 0, '2023-04-26 21:44:26', NULL, NULL);

-- ----------------------------
-- Table structure for tb_user_config
-- ----------------------------
DROP TABLE IF EXISTS `tb_user_config`;
CREATE TABLE `tb_user_config`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '配置id',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `config_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '配置描述',
  `config_value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '配置值',
  `config_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '配置名',
  `deleted_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未删除，1已删除',
  `deletable_flag` tinyint(1) NOT NULL DEFAULT 1 COMMENT '0不可删除，1可删除',
  `create_user` int(11) NOT NULL COMMENT '创建人',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_user` int(11) NULL DEFAULT NULL COMMENT '更新人',
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
  `role_id` int(11) NOT NULL COMMENT '角色id',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 100000002 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_user_role
-- ----------------------------
INSERT INTO `tb_user_role` VALUES (0, 1, 0);
INSERT INTO `tb_user_role` VALUES (1, 1, 1);
INSERT INTO `tb_user_role` VALUES (2, 1, 2);
INSERT INTO `tb_user_role` VALUES (3, 2, 3);
INSERT INTO `tb_user_role` VALUES (4, 3, 4);
INSERT INTO `tb_user_role` VALUES (5, 4, 5);
INSERT INTO `tb_user_role` VALUES (6, 5, 6);
INSERT INTO `tb_user_role` VALUES (7, 6, 7);
INSERT INTO `tb_user_role` VALUES (100000001, 5, 100000001);

SET FOREIGN_KEY_CHECKS = 1;
