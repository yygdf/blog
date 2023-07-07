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

 Date: 06/07/2023 18:18:44
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
  `category_id` int(11) NOT NULL COMMENT '分类id',
  `article_title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文章标题',
  `article_cover` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '文章封面',
  `article_content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文章内容',
  `is_top` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未置顶，1已置顶',
  `is_draft` tinyint(1) NOT NULL DEFAULT 1 COMMENT '0不是草稿，1是草稿',
  `is_public` tinyint(1) NOT NULL DEFAULT 1 COMMENT '0未公开，1已公开',
  `is_hidden` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未隐藏，1已隐藏',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未删除，1已删除',
  `is_commentable` tinyint(1) NOT NULL DEFAULT 1 COMMENT '0不可评论，1可评论',
  `ip_source` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'ip来源',
  `ip_address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'ip地址',
  `create_user` int(11) NOT NULL COMMENT '创建人',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_user` int(11) NULL DEFAULT NULL COMMENT '更新人',
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
  `tag_id` int(11) NOT NULL COMMENT '标签id',
  `article_id` int(11) NOT NULL COMMENT '文章id',
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
  `config_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '配置描述',
  `config_value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '配置值',
  `config_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '配置名',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未删除，1已删除',
  `is_deletable` tinyint(1) NOT NULL DEFAULT 1 COMMENT '0不可删除，1可删除',
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
  `is_public` tinyint(1) NOT NULL DEFAULT 1 COMMENT '0未公开，1已公开',
  `is_hidden` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未隐藏，1已隐藏',
  `create_user` int(11) NOT NULL COMMENT '创建人',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_user` int(11) NULL DEFAULT NULL COMMENT '更新人',
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
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户头像',
  `nickname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户昵称',
  `chat_type` tinyint(1) NOT NULL COMMENT '聊天类型',
  `chat_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '聊天内容',
  `is_recalled` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未撤回，1已撤回',
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
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未删除，1已删除',
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
  `login_type` tinyint(1) NOT NULL COMMENT '登录类型',
  `login_time` datetime(0) NOT NULL COMMENT '登录时间',
  `login_device` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '登录设备',
  `login_system` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '操作系统类型',
  `login_browser` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '浏览器类型',
  `ip_source` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'ip来源',
  `ip_address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'ip地址',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_login_log
-- ----------------------------

-- ----------------------------
-- Table structure for tb_menu
-- ----------------------------
DROP TABLE IF EXISTS `tb_menu`;
CREATE TABLE `tb_menu`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '菜单id',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `parent_id` int(11) NOT NULL DEFAULT 0 COMMENT '父菜单id',
  `path` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '菜单路径',
  `icon` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '菜单图标',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '菜单名称',
  `order` tinyint(1) NOT NULL DEFAULT 0 COMMENT '排序指标',
  `component` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '菜单组件',
  `is_hidden` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未隐藏，1已隐藏',
  `is_disabled` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未禁用，1已禁用',
  `create_user` int(11) NOT NULL COMMENT '创建人',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_user` int(11) NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 903 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_menu
-- ----------------------------
INSERT INTO `tb_menu` VALUES (1, 2, 0, '/', 'el-icon-odometer', '首页', 1, '/home/Home.vue', 0, 0, 0, '2023-04-26 23:04:13', NULL, NULL);
INSERT INTO `tb_menu` VALUES (2, 2, 0, '/article-menu', 'el-icon-document', '文章管理', 2, 'Layout', 0, 0, 0, '2023-05-12 13:30:18', NULL, NULL);
INSERT INTO `tb_menu` VALUES (3, 2, 0, '/message-menu', 'el-icon-message', '消息管理', 3, 'Layout', 0, 0, 0, '2023-05-12 13:31:28', NULL, NULL);
INSERT INTO `tb_menu` VALUES (4, 2, 0, '/link-menu', 'el-icon-link', '链接管理', 4, 'Layout', 0, 0, 0, '2023-05-12 13:33:40', NULL, NULL);
INSERT INTO `tb_menu` VALUES (5, 2, 0, '/statistic-menu', 'el-icon-s-data', '数据统计', 5, 'Layout', 0, 0, 0, '2023-05-12 13:37:45', NULL, NULL);
INSERT INTO `tb_menu` VALUES (6, 2, 0, '/system-menu', 'el-icon-setting', '系统管理', 6, 'Layout', 0, 0, 0, '2023-05-12 13:40:50', NULL, NULL);
INSERT INTO `tb_menu` VALUES (7, 2, 0, '/user-menu', 'el-icon-user', '用户管理', 7, 'Layout', 0, 0, 0, '2023-05-12 13:43:01', NULL, NULL);
INSERT INTO `tb_menu` VALUES (8, 2, 0, '/log-menu', 'el-icon-notebook-1', '日志管理', 8, 'Layout', 0, 0, 0, '2023-05-12 13:50:43', NULL, NULL);
INSERT INTO `tb_menu` VALUES (9, 2, 0, '/personal-menu', 'el-icon-s-home', '个人中心', 9, 'Layout', 0, 0, 0, '2023-05-12 13:52:12', NULL, NULL);
INSERT INTO `tb_menu` VALUES (201, 2, 2, '/article', 'el-icon-document-add', '添加文章', 21, '/article/Article.vue', 0, 0, 0, '2023-05-12 13:57:27', NULL, NULL);
INSERT INTO `tb_menu` VALUES (202, 2, 2, '/articles', 'el-icon-document-copy', '文章列表', 22, '/article/Articles.vue', 0, 0, 0, '2023-05-12 14:01:25', NULL, NULL);
INSERT INTO `tb_menu` VALUES (203, 2, 2, '/category', 'el-icon-files', '分类管理', 23, '/article/Category.vue', 0, 0, 0, '2023-05-12 14:09:01', NULL, NULL);
INSERT INTO `tb_menu` VALUES (204, 2, 2, '/tag', 'el-icon-collection-tag', '标签管理', 24, '/article/Tag.vue', 0, 0, 0, '2023-05-12 14:10:41', NULL, NULL);
INSERT INTO `tb_menu` VALUES (301, 2, 3, '/comment', 'el-icon-chat-dot-square', '评论管理', 31, '/message/Comment.vue', 0, 0, 0, '2023-05-12 14:16:31', NULL, NULL);
INSERT INTO `tb_menu` VALUES (302, 2, 3, '/message', 'el-icon-chat-dot-round', '留言管理', 32, '/message/Message.vue', 0, 0, 0, '2023-05-12 14:17:18', NULL, NULL);
INSERT INTO `tb_menu` VALUES (401, 2, 4, '/friend', 'el-icon-connection', '友链管理', 41, '/link/Friend.vue', 0, 0, 0, '2023-05-12 17:37:47', NULL, NULL);
INSERT INTO `tb_menu` VALUES (501, 2, 5, '/article-statistic', 'el-icon-tickets', '文章统计', 51, '/statistic/Article.vue', 0, 0, 0, '2023-05-12 14:22:50', NULL, NULL);
INSERT INTO `tb_menu` VALUES (502, 2, 5, '/message-statistic', 'el-icon-chat-line-square', '留言统计', 52, '/statistic/Message.vue', 0, 0, 0, '2023-05-12 14:25:34', NULL, NULL);
INSERT INTO `tb_menu` VALUES (503, 2, 5, '/user-statistic', 'el-icon-s-check', '用户统计', 53, '/statistic/User.vue', 0, 0, 0, '2023-05-12 14:27:43', NULL, NULL);
INSERT INTO `tb_menu` VALUES (601, 2, 6, '/base', 'el-icon-coin', '基础配置', 61, '/system/Base.vue', 0, 0, 0, '2023-05-12 14:32:36', NULL, NULL);
INSERT INTO `tb_menu` VALUES (602, 2, 6, '/menu', 'el-icon-menu', '菜单管理', 62, '/system/Menu.vue', 0, 0, 0, '2023-05-12 14:44:36', NULL, NULL);
INSERT INTO `tb_menu` VALUES (603, 2, 6, '/resource', 'el-icon-s-grid', '资源管理', 63, '/system/Resource.vue', 0, 0, 0, '2023-05-12 15:33:35', NULL, NULL);
INSERT INTO `tb_menu` VALUES (604, 2, 6, '/role', 'el-icon-s-custom', '角色管理', 64, '/system/Role.vue', 0, 0, 0, '2023-05-12 15:37:11', NULL, NULL);
INSERT INTO `tb_menu` VALUES (701, 2, 7, '/online-user', 'el-icon-coordinate', '在线用户', 71, '/user/Online.vue', 0, 0, 0, '2023-05-12 14:38:37', NULL, NULL);
INSERT INTO `tb_menu` VALUES (702, 2, 7, '/users', 'el-icon-user-solid', '用户列表', 72, '/user/Users.vue', 0, 0, 0, '2023-05-12 14:41:42', NULL, NULL);
INSERT INTO `tb_menu` VALUES (801, 2, 8, '/login', 'el-icon-map-location', '登录日志', 81, '/log/Login.vue', 0, 0, 0, '2023-05-12 15:44:03', NULL, NULL);
INSERT INTO `tb_menu` VALUES (802, 2, 8, '/operation', 'el-icon-receiving', '操作日志', 82, '/log/Operation.vue', 0, 0, 0, '2023-05-12 15:49:53', NULL, NULL);
INSERT INTO `tb_menu` VALUES (901, 2, 9, '/about', 'el-icon-place', '关于我', 91, '/personal/About.vue', 0, 0, 0, '2023-05-12 16:00:46', NULL, NULL);
INSERT INTO `tb_menu` VALUES (902, 2, 9, '/personal', 'el-icon-postcard', '个人配置', 92, '/personal/Personal.vue', 0, 0, 0, '2023-05-12 14:36:02', NULL, NULL);

-- ----------------------------
-- Table structure for tb_message
-- ----------------------------
DROP TABLE IF EXISTS `tb_message`;
CREATE TABLE `tb_message`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '留言id',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户头像',
  `speed` tinyint(1) NOT NULL COMMENT '留言速度',
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '留言内容',
  `nickname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户昵称',
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
  `dir_desc` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '目录描述',
  `dir_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '目录名称',
  `dir_cover` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '目录封面',
  `is_public` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未公开，1已公开',
  `is_hidden` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未隐藏，1已隐藏',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未删除，1已删除',
  `is_deletable` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0可删除，1不可删除',
  `create_user` int(11) NOT NULL COMMENT '创建人',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_user` int(11) NULL DEFAULT NULL COMMENT '更新人',
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
  `multi_dir_id` int(11) NOT NULL COMMENT '父目录id',
  `sub_dir` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '子目录',
  `file_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件地址',
  `file_desc` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '文件描述',
  `file_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '文件名称',
  `is_hidden` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未隐藏，1已隐藏',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未删除，1已删除',
  `ip_source` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'ip来源',
  `ip_address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'ip地址',
  `create_user` int(11) NOT NULL COMMENT '创建人',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_user` int(11) NULL DEFAULT NULL COMMENT '更新人',
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
  `opt_desc` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '操作描述',
  `opt_module` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '操作模块',
  `opt_method` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '操作方法',
  `request_param` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '请求参数',
  `request_method` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '请求方式',
  `response_data` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '响应数据',
  `ip_source` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'ip来源',
  `ip_address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'ip地址',
  `create_user` int(11) NOT NULL COMMENT '创建人',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_operation_log
-- ----------------------------

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
  `is_locked` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未锁定，1已锁定',
  `is_disabled` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未禁用，1已禁用',
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
  `request_method` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '请求方式',
  `is_disabled` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未禁用，1已禁用',
  `is_anonymous` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未匿名，1已匿名',
  `create_user` int(11) NOT NULL COMMENT '创建人',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_user` int(11) NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 902 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_resource
-- ----------------------------
INSERT INTO `tb_resource` VALUES (1, 2, 0, '', '菜单模块', '', 0, 0, 0, '2023-04-25 23:27:55', NULL, NULL);
INSERT INTO `tb_resource` VALUES (2, 2, 0, '', '首页模块', '', 0, 0, 0, '2023-05-11 10:42:09', NULL, NULL);
INSERT INTO `tb_resource` VALUES (9, 2, 0, '', '其他模块', '', 0, 0, 0, '2023-07-02 11:16:14', NULL, NULL);
INSERT INTO `tb_resource` VALUES (101, 2, 1, '/back/user/menus', '查看用户菜单', 'GET', 0, 0, 0, '2023-04-26 21:33:02', NULL, NULL);
INSERT INTO `tb_resource` VALUES (201, 2, 2, '/back', '查看后台首页信息', 'GET', 0, 0, 0, '2023-04-26 21:34:33', NULL, NULL);
INSERT INTO `tb_resource` VALUES (901, 2, 9, '/swagger-ui.html', 'SwaggerUI', 'GET', 0, 1, 0, '2023-07-02 11:17:04', NULL, NULL);

-- ----------------------------
-- Table structure for tb_role
-- ----------------------------
DROP TABLE IF EXISTS `tb_role`;
CREATE TABLE `tb_role`  (
  `id` int(255) NOT NULL AUTO_INCREMENT COMMENT '角色id',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `role_desc` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '角色描述',
  `role_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色名称',
  `is_disabled` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未禁用，1已禁用',
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
) ENGINE = InnoDB AUTO_INCREMENT = 30 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_role_menu
-- ----------------------------
INSERT INTO `tb_role_menu` VALUES (1, 2, 1);
INSERT INTO `tb_role_menu` VALUES (2, 2, 2);
INSERT INTO `tb_role_menu` VALUES (3, 2, 3);
INSERT INTO `tb_role_menu` VALUES (4, 2, 4);
INSERT INTO `tb_role_menu` VALUES (5, 2, 5);
INSERT INTO `tb_role_menu` VALUES (6, 2, 6);
INSERT INTO `tb_role_menu` VALUES (7, 2, 7);
INSERT INTO `tb_role_menu` VALUES (8, 2, 8);
INSERT INTO `tb_role_menu` VALUES (9, 2, 9);
INSERT INTO `tb_role_menu` VALUES (10, 2, 201);
INSERT INTO `tb_role_menu` VALUES (11, 2, 202);
INSERT INTO `tb_role_menu` VALUES (12, 2, 203);
INSERT INTO `tb_role_menu` VALUES (13, 2, 204);
INSERT INTO `tb_role_menu` VALUES (14, 2, 301);
INSERT INTO `tb_role_menu` VALUES (15, 2, 302);
INSERT INTO `tb_role_menu` VALUES (16, 2, 401);
INSERT INTO `tb_role_menu` VALUES (17, 2, 501);
INSERT INTO `tb_role_menu` VALUES (18, 2, 502);
INSERT INTO `tb_role_menu` VALUES (19, 2, 503);
INSERT INTO `tb_role_menu` VALUES (20, 2, 601);
INSERT INTO `tb_role_menu` VALUES (21, 2, 602);
INSERT INTO `tb_role_menu` VALUES (22, 2, 603);
INSERT INTO `tb_role_menu` VALUES (23, 2, 604);
INSERT INTO `tb_role_menu` VALUES (24, 2, 701);
INSERT INTO `tb_role_menu` VALUES (25, 2, 702);
INSERT INTO `tb_role_menu` VALUES (26, 2, 801);
INSERT INTO `tb_role_menu` VALUES (27, 2, 802);
INSERT INTO `tb_role_menu` VALUES (28, 2, 901);
INSERT INTO `tb_role_menu` VALUES (29, 2, 902);

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
  `is_locked` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未锁定，1已锁定',
  `is_disabled` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未禁用，1已禁用',
  `ip_source` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'ip来源',
  `ip_address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'ip地址',
  `create_user` int(11) NOT NULL COMMENT '创建人',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_user` int(11) NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_user_auth
-- ----------------------------
INSERT INTO `tb_user_auth` VALUES (0, 0, 'ling@qq.com', '$2a$10$EEJEoO4JH09hUTWezz9pq.pYk/HU2HDigdSWIos9GfFODrnXVcrHe', '2023-04-26 21:44:26', '', 0, 0, '', '', 0, '2023-04-26 21:44:26', NULL, NULL);
INSERT INTO `tb_user_auth` VALUES (1, 1, 'ks@qq.com', '$2a$10$EEJEoO4JH09hUTWezz9pq.pYk/HU2HDigdSWIos9GfFODrnXVcrHe', '2023-04-26 21:44:26', '', 0, 0, '', '', 0, '2023-04-26 21:44:26', NULL, NULL);
INSERT INTO `tb_user_auth` VALUES (2, 2, 'root@qq.com', '$2a$10$EEJEoO4JH09hUTWezz9pq.pYk/HU2HDigdSWIos9GfFODrnXVcrHe', '2023-04-26 21:44:26', '', 0, 0, '', '', 0, '2023-04-26 21:44:26', NULL, NULL);
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
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未删除，1已删除',
  `is_deletable` tinyint(1) NOT NULL DEFAULT 1 COMMENT '0不可删除，1可删除',
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
