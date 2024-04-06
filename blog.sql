/*
 Navicat Premium Data Transfer

 Source Server         : 192.168.247.129-root
 Source Server Type    : MySQL
 Source Server Version : 50731
 Source Host           : 192.168.247.129:3306
 Source Schema         : blog

 Target Server Type    : MySQL
 Target Server Version : 50731
 File Encoding         : 65001

 Date: 06/04/2024 22:52:05
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tb_article
-- ----------------------------
DROP TABLE IF EXISTS `tb_article`;
CREATE TABLE `tb_article`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `category_id` int(11) NOT NULL DEFAULT -1 COMMENT '分类id, 默认-1',
  `article_title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文章标题',
  `article_cover` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '文章封面, 默认空串',
  `article_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文章内容',
  `top_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未置顶, 1已置顶, 默认0',
  `draft_flag` tinyint(1) NOT NULL DEFAULT 1 COMMENT '0不是草稿, 1是草稿, 默认1',
  `public_flag` tinyint(1) NOT NULL DEFAULT 1 COMMENT '0未公开, 1已公开, 默认1',
  `hidden_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未隐藏, 1已隐藏, 默认0',
  `recycle_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未回收, 1已回收, 默认0',
  `deleted_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未删除, 1已删除, 默认0',
  `commentable_flag` tinyint(1) NOT NULL DEFAULT 1 COMMENT '0不可评论, 1可评论, 默认1',
  `ip_source` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'ip来源, 默认空串',
  `ip_address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'ip地址, 默认空串',
  `create_user` int(11) NOT NULL COMMENT '创建人',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_user` int(11) NULL DEFAULT NULL COMMENT '更新人, 默认null',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间, 默认null',
  `publish_user` int(11) NULL DEFAULT NULL COMMENT '发表人, 默认null',
  `publish_time` datetime(0) NULL DEFAULT NULL COMMENT '发表时间, 默认null',
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
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `tag_id` int(11) NOT NULL COMMENT '标签id',
  `article_id` int(11) NOT NULL COMMENT '文章id',
  `deleted_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未删除, 1已删除, 默认0',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_article_tag
-- ----------------------------

-- ----------------------------
-- Table structure for tb_category
-- ----------------------------
DROP TABLE IF EXISTS `tb_category`;
CREATE TABLE `tb_category`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `category_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '分类名',
  `public_flag` tinyint(1) NOT NULL DEFAULT 1 COMMENT '0未公开, 1已公开, 默认1',
  `hidden_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未隐藏, 1已隐藏, 默认0',
  `deleted_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未删除, 1已删除, 默认0',
  `create_user` int(11) NOT NULL COMMENT '创建人',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_user` int(11) NULL DEFAULT NULL COMMENT '更新人, 默认null',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间, 默认null',
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
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` int(11) NOT NULL DEFAULT -1 COMMENT '用户id, 默认-1',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '用户头像, 默认空串',
  `nickname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '用户昵称, 默认空串',
  `chat_type` int(11) NOT NULL COMMENT '聊天类型',
  `chat_content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '聊天内容',
  `recalled_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未撤回, 1已撤回, 默认0',
  `ip_source` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'ip来源, 默认空串',
  `ip_address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'ip地址, 默认空串',
  `create_user` int(11) NOT NULL DEFAULT -1 COMMENT '创建人, 默认-1',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_user` int(11) NULL DEFAULT NULL COMMENT '更新人, 默认null',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间, 默认null',
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
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `reply_id` int(11) NOT NULL DEFAULT -1 COMMENT '回复用户id, 默认-1',
  `article_id` int(11) NOT NULL DEFAULT -1 COMMENT '文章id, 默认-1',
  `parent_id` int(11) NOT NULL DEFAULT -1 COMMENT '父id, 默认-1',
  `comment_content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '评论内容',
  `recycle_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未回收, 1已回收, 默认0',
  `deleted_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未删除, 1已删除, 默认0',
  `ip_source` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'ip来源, 默认空串',
  `ip_address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'ip地址, 默认空串',
  `create_user` int(11) NOT NULL COMMENT '创建人',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_user` int(11) NULL DEFAULT NULL COMMENT '更新人, 默认null',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间, 默认null',
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
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `view_date` date NOT NULL COMMENT '日期',
  `view_count` int(11) NOT NULL COMMENT '访问量',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_day_view
-- ----------------------------

-- ----------------------------
-- Table structure for tb_exception_log
-- ----------------------------
DROP TABLE IF EXISTS `tb_exception_log`;
CREATE TABLE `tb_exception_log`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `opt_uri` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '操作路径',
  `opt_type` int(11) NOT NULL COMMENT '操作类型, 1上传, 2删除, 3修改, 4查询, 5新增或修改',
  `opt_desc` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '操作描述, 默认空串',
  `opt_module` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '操作模块',
  `opt_method` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '操作方法',
  `request_param` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '请求参数',
  `exception_message` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '异常简略信息',
  `exception_stack_trace` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '异常堆栈信息',
  `illegal_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0不是非法, 1是非法, 默认0',
  `ip_source` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'ip来源, 默认空串',
  `ip_address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'ip地址, 默认空串',
  `create_user` int(11) NOT NULL COMMENT '创建人',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_exception_log
-- ----------------------------

-- ----------------------------
-- Table structure for tb_friend_link
-- ----------------------------
DROP TABLE IF EXISTS `tb_friend_link`;
CREATE TABLE `tb_friend_link`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `link_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '友链地址',
  `link_desc` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '友链描述',
  `link_logo` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '友链图标',
  `link_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '友链名称',
  `deleted_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未删除, 1已删除, 默认0',
  `create_user` int(11) NOT NULL COMMENT '创建人',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_user` int(11) NULL DEFAULT NULL COMMENT '更新人, 默认null',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间, 默认null',
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
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `login_time` datetime(0) NOT NULL COMMENT '登录时间',
  `login_method` int(11) NOT NULL DEFAULT 1 COMMENT '登录方式, 1邮箱, 2QQ, 3微信, 4手机号, 默认1',
  `login_platform` tinyint(1) NOT NULL DEFAULT 0 COMMENT '登录平台, 0前台, 1后台, 默认0',
  `login_device` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '登录设备, 默认空串',
  `login_system` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '操作系统类型, 默认空串',
  `login_browser` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '浏览器类型, 默认空串',
  `ip_source` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'ip来源, 默认空串',
  `ip_address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'ip地址, 默认空串',
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
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `parent_id` int(11) NOT NULL DEFAULT -1 COMMENT '父id, 默认-1',
  `icon` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '菜单图标',
  `rank` int(11) NOT NULL DEFAULT 100 COMMENT '排序指标, 默认100',
  `path` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '菜单路径',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '菜单名称',
  `component` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'Layout' COMMENT '菜单组件, 默认Layout',
  `hide_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未隐藏, 1已隐藏, 默认0',
  `hidden_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未隐藏, 1已隐藏, 默认0',
  `disabled_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未禁用, 1已禁用, 默认0',
  `deletable_flag` tinyint(1) NOT NULL DEFAULT 1 COMMENT '0不可删除, 1可删除, 默认1',
  `create_user` int(11) NOT NULL COMMENT '创建人',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_user` int(11) NULL DEFAULT NULL COMMENT '更新人, 默认null',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间, 默认null',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 125 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_menu
-- ----------------------------
INSERT INTO `tb_menu` VALUES (1, 2, -1, 'el-icon-odometer', 1, '/', '首页', '/home/Home.vue', 0, 0, 0, 0, 2, '2023-04-26 23:04:13', NULL, NULL);
INSERT INTO `tb_menu` VALUES (2, 2, -1, 'el-icon-document', 2, '/article-menu', '文章管理', 'Layout', 0, 0, 0, 0, 2, '2023-05-12 13:30:18', NULL, NULL);
INSERT INTO `tb_menu` VALUES (3, 2, -1, 'el-icon-message', 3, '/message-menu', '消息管理', 'Layout', 0, 0, 0, 0, 2, '2023-05-12 13:31:28', NULL, NULL);
INSERT INTO `tb_menu` VALUES (4, 2, -1, 'el-icon-link', 4, '/link-menu', '链接管理', 'Layout', 0, 0, 0, 0, 2, '2023-05-12 13:33:40', NULL, NULL);
INSERT INTO `tb_menu` VALUES (5, 2, -1, 'el-icon-data-board', 5, '/statistic-menu', '数据统计', 'Layout', 0, 0, 0, 0, 2, '2023-05-12 13:37:45', NULL, NULL);
INSERT INTO `tb_menu` VALUES (6, 2, -1, 'el-icon-setting', 6, '/system-menu', '系统管理', 'Layout', 0, 0, 0, 0, 2, '2023-05-12 13:40:50', NULL, NULL);
INSERT INTO `tb_menu` VALUES (7, 2, -1, 'el-icon-user', 7, '/user-menu', '用户管理', 'Layout', 0, 0, 0, 0, 2, '2023-05-12 13:43:01', NULL, NULL);
INSERT INTO `tb_menu` VALUES (8, 2, -1, 'el-icon-notebook-1', 8, '/log-menu', '日志管理', 'Layout', 0, 0, 0, 0, 2, '2023-05-12 13:50:43', NULL, NULL);
INSERT INTO `tb_menu` VALUES (9, 2, -1, 'el-icon-folder', 9, '/file-menu', '文件管理', 'Layout', 0, 0, 0, 0, 2, '2023-11-15 06:22:09', NULL, NULL);
INSERT INTO `tb_menu` VALUES (10, 2, -1, 'el-icon-s-home', 10, '/personal-menu', '个人中心', 'Layout', 0, 0, 0, 0, 2, '2023-05-12 13:52:12', NULL, NULL);
INSERT INTO `tb_menu` VALUES (101, 2, 2, 'el-icon-document-add', 21, '/article', '添加文章', '/article/Article.vue', 0, 0, 0, 0, 2, '2023-05-12 13:57:27', NULL, NULL);
INSERT INTO `tb_menu` VALUES (102, 2, 2, 'el-icon-document-copy', 22, '/articles', '文章列表', '/article/Articles.vue', 0, 0, 0, 0, 2, '2023-05-12 14:01:25', NULL, NULL);
INSERT INTO `tb_menu` VALUES (103, 2, 2, 'el-icon-files', 23, '/category', '分类管理', '/article/Category.vue', 0, 0, 0, 0, 2, '2023-05-12 14:09:01', NULL, NULL);
INSERT INTO `tb_menu` VALUES (104, 2, 2, 'el-icon-collection-tag', 24, '/tag', '标签管理', '/article/Tag.vue', 0, 0, 0, 0, 2, '2023-05-12 14:10:41', NULL, NULL);
INSERT INTO `tb_menu` VALUES (105, 2, 2, 'el-icon-document-add', 25, '/article/*', '修改文章', '/article/Article.vue', 1, 0, 0, 0, 2, '2023-07-09 09:58:07', NULL, NULL);
INSERT INTO `tb_menu` VALUES (106, 2, 3, 'el-icon-chat-dot-square', 31, '/comment', '评论管理', '/message/Comment.vue', 0, 0, 0, 0, 2, '2023-05-12 14:16:31', NULL, NULL);
INSERT INTO `tb_menu` VALUES (107, 2, 3, 'el-icon-chat-dot-round', 32, '/message', '留言管理', '/message/Message.vue', 0, 0, 0, 0, 2, '2023-05-12 14:17:18', NULL, NULL);
INSERT INTO `tb_menu` VALUES (108, 2, 4, 'el-icon-connection', 41, '/friend', '友链管理', '/link/FriendLink.vue', 0, 0, 0, 0, 2, '2023-05-12 17:37:47', NULL, NULL);
INSERT INTO `tb_menu` VALUES (109, 2, 5, 'el-icon-tickets', 51, '/articleStatistic', '文章统计', '/statistic/ArticleStatistic.vue', 0, 0, 1, 0, 2, '2023-05-12 14:22:50', NULL, NULL);
INSERT INTO `tb_menu` VALUES (110, 2, 5, 'el-icon-chat-line-square', 52, '/commentStatistic', '评论统计', '/statistic/CommentStatistic.vue', 0, 0, 1, 0, 2, '2023-05-12 14:25:34', NULL, NULL);
INSERT INTO `tb_menu` VALUES (111, 2, 5, 'el-icon-coordinate', 53, '/userStatistic', '用户统计', '/statistic/UserStatictic.vue', 0, 0, 1, 0, 2, '2023-05-12 14:27:43', NULL, NULL);
INSERT INTO `tb_menu` VALUES (112, 2, 6, 'el-icon-s-tools', 61, '/systemConfig', '系统配置', '/system/SystemConfig.vue', 0, 0, 0, 0, 2, '2023-05-12 14:32:36', NULL, NULL);
INSERT INTO `tb_menu` VALUES (113, 2, 6, 'el-icon-menu', 62, '/menu', '菜单管理', '/system/Menu.vue', 0, 0, 0, 0, 2, '2023-05-12 14:44:36', NULL, NULL);
INSERT INTO `tb_menu` VALUES (114, 2, 6, 'el-icon-s-grid', 63, '/resource', '资源管理', '/system/Resource.vue', 0, 0, 0, 0, 2, '2023-05-12 15:33:35', NULL, NULL);
INSERT INTO `tb_menu` VALUES (115, 2, 6, 'el-icon-s-custom', 64, '/role', '角色管理', '/system/Role.vue', 0, 0, 0, 0, 2, '2023-05-12 15:37:11', NULL, NULL);
INSERT INTO `tb_menu` VALUES (116, 2, 7, 'el-icon-s-check', 71, '/userOnline', '在线用户', '/user/UserOnline.vue', 0, 0, 0, 0, 2, '2023-05-12 14:38:37', NULL, NULL);
INSERT INTO `tb_menu` VALUES (117, 2, 7, 'el-icon-user-solid', 72, '/user', '用户列表', '/user/User.vue', 0, 0, 0, 0, 2, '2023-05-12 14:41:42', NULL, NULL);
INSERT INTO `tb_menu` VALUES (118, 2, 7, 'el-icon-s-order', 73, '/userAuth', '账号列表', '/user/UserAuth.vue', 0, 0, 0, 0, 2, '2023-08-26 08:58:23', NULL, NULL);
INSERT INTO `tb_menu` VALUES (119, 2, 8, 'el-icon-receiving', 82, '/operation', '操作日志', '/log/Operation.vue', 0, 0, 0, 0, 2, '2023-05-12 15:49:53', NULL, NULL);
INSERT INTO `tb_menu` VALUES (120, 2, 8, 'el-icon-map-location', 81, '/loginLog', '登录日志', '/log/Login.vue', 0, 0, 0, 0, 2, '2023-05-12 15:44:03', NULL, NULL);
INSERT INTO `tb_menu` VALUES (121, 2, 8, 'el-icon-warning-outline', 83, '/exception', '异常日志', '/log/Exception.vue', 0, 0, 0, 0, 2, '2023-09-18 07:57:41', NULL, NULL);
INSERT INTO `tb_menu` VALUES (122, 2, 9, 'el-icon-folder-opened', 91, '/multiFile', '文件管理', '/file/MultiFile.vue', 0, 0, 0, 0, 2, '2023-11-15 06:24:09', NULL, NULL);
INSERT INTO `tb_menu` VALUES (123, 2, 10, 'el-icon-postcard', 92, '/personal', '个人信息', '/personal/Personal.vue', 0, 0, 0, 0, 2, '2023-05-12 14:36:02', NULL, NULL);
INSERT INTO `tb_menu` VALUES (124, 2, 10, 'el-icon-coin', 94, '/userConfig', '用户配置', '/personal/UserConfig.vue', 0, 0, 0, 0, 2, '2023-09-07 18:09:10', NULL, NULL);

-- ----------------------------
-- Table structure for tb_message
-- ----------------------------
DROP TABLE IF EXISTS `tb_message`;
CREATE TABLE `tb_message`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` int(11) NOT NULL DEFAULT -1 COMMENT '用户id, 默认-1',
  `message_speed` int(11) NOT NULL COMMENT '留言速度',
  `message_content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '留言内容',
  `deleted_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未删除, 1已删除, 默认0',
  `ip_source` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'ip来源, 默认空串',
  `ip_address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'ip地址, 默认空串',
  `create_user` int(11) NOT NULL DEFAULT -1 COMMENT '创建人, 默认-1',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_user` int(11) NULL DEFAULT NULL COMMENT '更新人, 默认null',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间, 默认null',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_message
-- ----------------------------

-- ----------------------------
-- Table structure for tb_multi_file
-- ----------------------------
DROP TABLE IF EXISTS `tb_multi_file`;
CREATE TABLE `tb_multi_file`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `parent_id` int(11) NOT NULL DEFAULT -1 COMMENT '父id, 默认-1',
  `file_desc` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '文件描述, 默认空串',
  `file_mark` int(11) NOT NULL DEFAULT 0 COMMENT '文件标识, 默认0',
  `file_name` bigint(20) NOT NULL COMMENT '文件名称',
  `file_size` bigint(20) NOT NULL DEFAULT -1 COMMENT '文件大小, 默认-1',
  `file_cover` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '文件封面, 默认空串',
  `file_full_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件完整路径',
  `file_extension` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '文件拓展名, 默认空串',
  `file_name_new` bigint(20) NOT NULL DEFAULT -1 COMMENT '文件新名称, 默认-1',
  `file_name_origin` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件原始名称',
  `public_flag` tinyint(1) NOT NULL DEFAULT 1 COMMENT '0未公开, 1已公开, 默认1',
  `hidden_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未隐藏, 1已隐藏, 默认0',
  `deletable_flag` tinyint(1) NOT NULL DEFAULT 1 COMMENT '0不可删除, 1可删除, 默认1',
  `deleted_count` int(11) NOT NULL DEFAULT 0 COMMENT '删除次数, 默认0',
  `ip_source` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'ip来源, 默认空串',
  `ip_address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'ip地址, 默认空串',
  `create_user` int(11) NOT NULL COMMENT '创建人',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_user` int(11) NULL DEFAULT NULL COMMENT '更新人, 默认null',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间, 默认null',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 51 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_multi_file
-- ----------------------------
INSERT INTO `tb_multi_file` VALUES (1, 0, -1, '', 0, -1, 0, '', '0/-1', '', -1, 'img', 0, 0, 0, 0, '', '', 0, '2023-11-13 06:18:17', NULL, NULL);
INSERT INTO `tb_multi_file` VALUES (2, 0, -1, '', 0, -2, 0, '', '0/-2', '', -1, 'audio', 0, 0, 0, 0, '', '', 0, '2023-11-13 06:19:53', NULL, NULL);
INSERT INTO `tb_multi_file` VALUES (3, 0, 1, '', 0, -11, 0, '', '0/-1/-11', '', -1, 'avatar', 0, 0, 0, 0, '', '', 0, '2023-11-13 06:22:09', NULL, NULL);
INSERT INTO `tb_multi_file` VALUES (4, 0, 1, '', 0, -12, 0, '', '0/-1/-12', '', -1, 'article', 0, 0, 0, 0, '', '', 0, '2023-11-13 06:24:14', NULL, NULL);
INSERT INTO `tb_multi_file` VALUES (5, 0, 1, '', 0, -13, 0, '', '0/-1/-13', '', -1, 'album', 0, 0, 0, 0, '', '', 0, '2023-11-13 06:25:12', NULL, NULL);
INSERT INTO `tb_multi_file` VALUES (6, 0, 2, '', 0, -21, 0, '', '0/-2/-21', '', -1, 'chat', 0, 0, 0, 0, '', '', 0, '2023-11-15 07:37:13', NULL, NULL);
INSERT INTO `tb_multi_file` VALUES (7, 0, 2, '', 0, -22, 0, '', '0/-2/-22', '', -1, 'music', 0, 0, 0, 0, '', '', 0, '2023-11-15 07:37:13', NULL, NULL);
INSERT INTO `tb_multi_file` VALUES (8, 1, -1, '', 0, -1, 0, '', '1/-1', '', -1, 'img', 0, 0, 0, 0, '', '', 1, '2023-11-13 06:18:17', NULL, NULL);
INSERT INTO `tb_multi_file` VALUES (9, 1, -1, '', 0, -2, 0, '', '1/-2', '', -1, 'audio', 0, 0, 0, 0, '', '', 1, '2023-11-13 06:19:53', NULL, NULL);
INSERT INTO `tb_multi_file` VALUES (10, 1, 8, '', 0, -11, 0, '', '1/-1/-11', '', -1, 'avatar', 0, 0, 0, 0, '', '', 1, '2023-11-13 06:22:09', NULL, NULL);
INSERT INTO `tb_multi_file` VALUES (11, 1, 8, '', 0, -12, 0, '', '1/-1/-12', '', -1, 'article', 0, 0, 0, 0, '', '', 1, '2023-11-13 06:24:14', NULL, NULL);
INSERT INTO `tb_multi_file` VALUES (12, 1, 8, '', 0, -13, 0, '', '1/-1/-13', '', -1, 'album', 0, 0, 0, 0, '', '', 1, '2023-11-13 06:25:12', NULL, NULL);
INSERT INTO `tb_multi_file` VALUES (13, 1, 9, '', 0, -21, 0, '', '1/-2/-21', '', -1, 'chat', 0, 0, 0, 0, '', '', 1, '2023-11-15 07:37:13', NULL, NULL);
INSERT INTO `tb_multi_file` VALUES (14, 1, 9, '', 0, -22, 0, '', '1/-2/-22', '', -1, 'music', 0, 0, 0, 0, '', '', 1, '2023-11-15 07:37:13', NULL, NULL);
INSERT INTO `tb_multi_file` VALUES (15, 2, -1, '', 0, -1, 0, '', '2/-1', '', -1, 'img', 0, 0, 0, 0, '', '', 2, '2023-11-13 06:18:17', NULL, NULL);
INSERT INTO `tb_multi_file` VALUES (16, 2, -1, '', 0, -2, 0, '', '2/-2', '', -1, 'audio', 0, 0, 0, 0, '', '', 2, '2023-11-13 06:19:53', NULL, NULL);
INSERT INTO `tb_multi_file` VALUES (17, 2, 15, '', 0, -11, 0, '', '2/-1/-11', '', -1, 'avatar', 0, 0, 0, 0, '', '', 2, '2023-11-13 06:22:09', NULL, NULL);
INSERT INTO `tb_multi_file` VALUES (18, 2, 15, '', 0, -12, 0, '', '2/-1/-12', '', -1, 'article', 0, 0, 0, 0, '', '', 2, '2023-11-13 06:24:14', NULL, NULL);
INSERT INTO `tb_multi_file` VALUES (19, 2, 15, '', 0, -13, 0, '', '2/-1/-13', '', -1, 'album', 0, 0, 0, 0, '', '', 2, '2023-11-13 06:25:12', NULL, NULL);
INSERT INTO `tb_multi_file` VALUES (20, 2, 16, '', 0, -21, 0, '', '2/-2/-21', '', -1, 'chat', 0, 0, 0, 0, '', '', 2, '2023-11-15 07:37:13', NULL, NULL);
INSERT INTO `tb_multi_file` VALUES (21, 2, 16, '', 0, -22, 0, '', '2/-2/-22', '', -1, 'music', 0, 0, 0, 0, '', '', 2, '2023-11-15 07:37:13', NULL, NULL);
INSERT INTO `tb_multi_file` VALUES (22, 3, -1, '', 0, -1, 0, '', '3/-1', '', -1, 'img', 0, 0, 0, 0, '', '', 3, '2023-11-13 06:18:17', NULL, NULL);
INSERT INTO `tb_multi_file` VALUES (23, 3, -1, '', 0, -2, 0, '', '3/-2', '', -1, 'audio', 0, 0, 0, 0, '', '', 3, '2023-11-13 06:19:53', NULL, NULL);
INSERT INTO `tb_multi_file` VALUES (24, 3, 22, '', 0, -11, 0, '', '3/-1/-11', '', -1, 'avatar', 0, 0, 0, 0, '', '', 3, '2023-11-13 06:22:09', NULL, NULL);
INSERT INTO `tb_multi_file` VALUES (25, 3, 22, '', 0, -12, 0, '', '3/-1/-12', '', -1, 'article', 0, 0, 0, 0, '', '', 3, '2023-11-13 06:24:14', NULL, NULL);
INSERT INTO `tb_multi_file` VALUES (26, 3, 22, '', 0, -13, 0, '', '3/-1/-13', '', -1, 'album', 0, 0, 0, 0, '', '', 3, '2023-11-13 06:25:12', NULL, NULL);
INSERT INTO `tb_multi_file` VALUES (27, 3, 23, '', 0, -21, 0, '', '3/-2/-21', '', -1, 'chat', 0, 0, 0, 0, '', '', 3, '2023-11-15 07:37:13', NULL, NULL);
INSERT INTO `tb_multi_file` VALUES (28, 3, 23, '', 0, -22, 0, '', '3/-2/-22', '', -1, 'music', 0, 0, 0, 0, '', '', 3, '2023-11-15 07:37:13', NULL, NULL);
INSERT INTO `tb_multi_file` VALUES (29, 4, -1, '', 0, -1, 0, '', '4/-1', '', -1, 'img', 0, 0, 0, 0, '', '', 4, '2023-11-13 06:18:17', NULL, NULL);
INSERT INTO `tb_multi_file` VALUES (30, 4, -1, '', 0, -2, 0, '', '4/-2', '', -1, 'audio', 0, 0, 0, 0, '', '', 4, '2023-11-13 06:19:53', NULL, NULL);
INSERT INTO `tb_multi_file` VALUES (31, 4, 29, '', 0, -11, 0, '', '4/-1/-11', '', -1, 'avatar', 0, 0, 0, 0, '', '', 4, '2023-11-13 06:22:09', NULL, NULL);
INSERT INTO `tb_multi_file` VALUES (32, 4, 29, '', 0, -12, 0, '', '4/-1/-12', '', -1, 'article', 0, 0, 0, 0, '', '', 4, '2023-11-13 06:24:14', NULL, NULL);
INSERT INTO `tb_multi_file` VALUES (33, 4, 29, '', 0, -13, 0, '', '4/-1/-13', '', -1, 'album', 0, 0, 0, 0, '', '', 4, '2023-11-13 06:25:12', NULL, NULL);
INSERT INTO `tb_multi_file` VALUES (34, 4, 30, '', 0, -21, 0, '', '4/-2/-21', '', -1, 'chat', 0, 0, 0, 0, '', '', 4, '2023-11-15 07:37:13', NULL, NULL);
INSERT INTO `tb_multi_file` VALUES (35, 4, 30, '', 0, -22, 0, '', '4/-2/-22', '', -1, 'music', 0, 0, 0, 0, '', '', 4, '2023-11-15 07:37:13', NULL, NULL);
INSERT INTO `tb_multi_file` VALUES (36, 5, -1, '', 0, -1, 0, '', '5/-1', '', -1, 'img', 0, 0, 0, 0, '', '', 5, '2023-11-13 06:18:17', NULL, NULL);
INSERT INTO `tb_multi_file` VALUES (37, 5, -1, '', 0, -2, 0, '', '5/-2', '', -1, 'audio', 0, 0, 0, 0, '', '', 5, '2023-11-13 06:19:53', NULL, NULL);
INSERT INTO `tb_multi_file` VALUES (38, 5, 36, '', 0, -11, 0, '', '5/-1/-11', '', -1, 'avatar', 0, 0, 0, 0, '', '', 5, '2023-11-13 06:22:09', NULL, NULL);
INSERT INTO `tb_multi_file` VALUES (39, 5, 36, '', 0, -12, 0, '', '5/-1/-12', '', -1, 'article', 0, 0, 0, 0, '', '', 5, '2023-11-13 06:24:14', NULL, NULL);
INSERT INTO `tb_multi_file` VALUES (40, 5, 36, '', 0, -13, 0, '', '5/-1/-13', '', -1, 'album', 0, 0, 0, 0, '', '', 5, '2023-11-13 06:25:12', NULL, NULL);
INSERT INTO `tb_multi_file` VALUES (41, 5, 37, '', 0, -21, 0, '', '5/-2/-21', '', -1, 'chat', 0, 0, 0, 0, '', '', 5, '2023-11-15 07:37:13', NULL, NULL);
INSERT INTO `tb_multi_file` VALUES (42, 5, 37, '', 0, -22, 0, '', '5/-2/-22', '', -1, 'music', 0, 0, 0, 0, '', '', 5, '2023-11-15 07:37:13', NULL, NULL);
INSERT INTO `tb_multi_file` VALUES (43, 6, -1, '', 0, -1, 0, '', '6/-1', '', -1, 'img', 0, 0, 0, 0, '', '', 6, '2023-11-13 06:18:17', NULL, NULL);
INSERT INTO `tb_multi_file` VALUES (44, 6, -1, '', 0, -2, 0, '', '6/-2', '', -1, 'audio', 0, 0, 0, 0, '', '', 6, '2023-11-13 06:19:53', NULL, NULL);
INSERT INTO `tb_multi_file` VALUES (45, 6, 43, '', 0, -11, 0, '', '6/-1/-11', '', -1, 'avatar', 0, 0, 0, 0, '', '', 6, '2023-11-13 06:22:09', NULL, NULL);
INSERT INTO `tb_multi_file` VALUES (46, 6, 44, '', 0, -21, 0, '', '6/-2/-21', '', -1, 'chat', 0, 0, 0, 0, '', '', 6, '2023-11-15 07:37:13', NULL, NULL);
INSERT INTO `tb_multi_file` VALUES (47, 7, -1, '', 0, -1, 0, '', '7/-1', '', -1, 'img', 0, 0, 0, 0, '', '', 7, '2023-11-13 06:18:17', NULL, NULL);
INSERT INTO `tb_multi_file` VALUES (48, 7, -1, '', 0, -2, 0, '', '7/-2', '', -1, 'audio', 0, 0, 0, 0, '', '', 7, '2023-11-13 06:19:53', NULL, NULL);
INSERT INTO `tb_multi_file` VALUES (49, 7, 47, '', 0, -11, 0, '', '7/-1/-11', '', -1, 'avatar', 0, 0, 0, 0, '', '', 7, '2023-11-13 06:22:09', NULL, NULL);
INSERT INTO `tb_multi_file` VALUES (50, 7, 48, '', 0, -21, 0, '', '7/-2/-21', '', -1, 'chat', 0, 0, 0, 0, '', '', 7, '2023-11-15 07:37:13', NULL, NULL);

-- ----------------------------
-- Table structure for tb_operation_log
-- ----------------------------
DROP TABLE IF EXISTS `tb_operation_log`;
CREATE TABLE `tb_operation_log`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `opt_uri` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '操作路径',
  `opt_type` int(11) NOT NULL COMMENT '操作类型, 1上传, 2删除, 3修改, 5新增或修改',
  `opt_desc` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '操作描述, 默认空串',
  `opt_module` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '操作模块',
  `opt_method` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '操作方法',
  `request_param` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '请求参数',
  `response_data` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '响应数据',
  `ip_source` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'ip来源, 默认空串',
  `ip_address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'ip地址, 默认空串',
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
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `openid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'QQ的openid',
  `access_token` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '访问QQ的token',
  `locked_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未锁定, 1已锁定, 默认0',
  `deleted_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未删除, 1已删除, 默认0',
  `disabled_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未禁用, 1已禁用, 默认0',
  `create_user` int(11) NOT NULL COMMENT '创建人',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_user` int(11) NULL DEFAULT NULL COMMENT '更新人, 默认null',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间, 默认null',
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
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `parent_id` int(11) NOT NULL DEFAULT -1 COMMENT '父id, 默认-1',
  `resource_uri` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '资源路径, 默认空串',
  `resource_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '资源名称',
  `resource_request_method` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '请求方式, 默认空串',
  `disabled_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未禁用, 1已禁用, 默认0',
  `deletable_flag` tinyint(1) NOT NULL DEFAULT 1 COMMENT '0不可删除, 1可删除, 默认1',
  `anonymous_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未匿名, 1已匿名, 默认0',
  `create_user` int(11) NOT NULL COMMENT '创建人',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_user` int(11) NULL DEFAULT NULL COMMENT '更新人, 默认null',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间, 默认null',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 222 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_resource
-- ----------------------------
INSERT INTO `tb_resource` VALUES (1, 2, -1, '', '菜单模块', '', 0, 0, 0, 2, '2023-04-25 23:27:55', NULL, NULL);
INSERT INTO `tb_resource` VALUES (2, 2, -1, '', '文章模块', '', 0, 0, 0, 2, '2023-07-08 09:08:55', NULL, NULL);
INSERT INTO `tb_resource` VALUES (3, 2, -1, '', '分类模块', '', 0, 0, 0, 2, '2023-07-11 10:24:40', NULL, NULL);
INSERT INTO `tb_resource` VALUES (4, 2, -1, '', '标签模块', '', 0, 0, 0, 2, '2023-07-12 10:48:21', NULL, NULL);
INSERT INTO `tb_resource` VALUES (5, 2, -1, '', '用户模块', '', 0, 0, 0, 2, '2023-07-14 13:56:54', NULL, NULL);
INSERT INTO `tb_resource` VALUES (6, 2, -1, '', '文件模块', '', 0, 0, 0, 2, '2023-07-14 16:20:03', NULL, NULL);
INSERT INTO `tb_resource` VALUES (7, 2, -1, '', '评论模块', '', 0, 0, 0, 2, '2023-07-26 07:34:30', NULL, NULL);
INSERT INTO `tb_resource` VALUES (8, 2, -1, '', '其他模块', '', 0, 0, 0, 2, '2023-07-02 11:16:14', NULL, NULL);
INSERT INTO `tb_resource` VALUES (9, 2, -1, '', '留言模块', '', 0, 0, 0, 2, '2023-07-29 19:16:27', NULL, NULL);
INSERT INTO `tb_resource` VALUES (10, 2, -1, '', '友链模块', '', 0, 0, 0, 2, '2023-08-03 18:58:40', NULL, NULL);
INSERT INTO `tb_resource` VALUES (11, 2, -1, '', '资源模块', '', 0, 0, 0, 2, '2023-08-08 19:05:14', NULL, NULL);
INSERT INTO `tb_resource` VALUES (12, 2, -1, '', '角色模块', '', 0, 0, 0, 2, '2023-08-13 09:08:16', NULL, NULL);
INSERT INTO `tb_resource` VALUES (13, 2, -1, '', '账号模块', '', 0, 0, 0, 2, '2023-08-17 19:25:50', NULL, NULL);
INSERT INTO `tb_resource` VALUES (14, 2, -1, '', '系统配置模块', '', 0, 0, 0, 2, '2023-09-06 18:48:22', NULL, NULL);
INSERT INTO `tb_resource` VALUES (15, 2, -1, '', '用户配置模块', '', 0, 0, 0, 2, '2023-09-08 07:28:52', NULL, NULL);
INSERT INTO `tb_resource` VALUES (16, 2, -1, '', '异常日志模块', '', 0, 0, 0, 2, '2023-09-18 07:55:36', NULL, NULL);
INSERT INTO `tb_resource` VALUES (17, 2, -1, '', '登录日志模块', '', 0, 0, 0, 2, '2023-09-20 07:39:40', NULL, NULL);
INSERT INTO `tb_resource` VALUES (18, 2, -1, '', '操作日志模块', '', 0, 0, 0, 2, '2023-09-20 07:39:48', NULL, NULL);
INSERT INTO `tb_resource` VALUES (19, 2, -1, '', '博客模块', '', 0, 0, 0, 2, '2024-02-27 14:42:00', NULL, NULL);
INSERT INTO `tb_resource` VALUES (20, 2, -1, '', '聊天室模块', '', 0, 0, 0, 2, '2024-03-31 17:32:18', NULL, NULL);
INSERT INTO `tb_resource` VALUES (101, 2, 1, '/back/menus/user', '查看用户菜单', 'GET', 0, 0, 0, 2, '2023-04-26 21:33:02', NULL, NULL);
INSERT INTO `tb_resource` VALUES (102, 2, 1, '/back/menus', '查看后台菜单列表', 'GET', 0, 0, 0, 2, '2023-08-06 14:41:08', NULL, NULL);
INSERT INTO `tb_resource` VALUES (103, 2, 1, '/back/menu/status', '修改菜单状态', 'PUT', 0, 0, 0, 2, '2023-08-06 14:41:46', NULL, NULL);
INSERT INTO `tb_resource` VALUES (104, 2, 1, '/back/menus', '物理批量删除菜单', 'DELETE', 0, 0, 0, 2, '2023-08-06 14:42:51', NULL, NULL);
INSERT INTO `tb_resource` VALUES (105, 2, 1, '/back/menu', '添加或修改菜单', 'POST', 0, 0, 0, 2, '2023-08-06 14:43:26', NULL, NULL);
INSERT INTO `tb_resource` VALUES (106, 2, 2, '/back/article/*', '根据文章id查找文章', 'GET', 0, 0, 0, 2, '2023-07-08 09:10:16', NULL, NULL);
INSERT INTO `tb_resource` VALUES (107, 2, 2, '/back/article/option', '查看文章选项', 'GET', 0, 0, 0, 2, '2023-07-08 09:10:53', NULL, NULL);
INSERT INTO `tb_resource` VALUES (108, 2, 2, '/back/article', '添加或修改文章', 'POST', 0, 0, 0, 2, '2023-07-08 09:11:36', NULL, NULL);
INSERT INTO `tb_resource` VALUES (109, 2, 2, '/back/articles', '查看后台文章列表', 'GET', 0, 0, 0, 2, '2023-07-08 14:46:19', NULL, NULL);
INSERT INTO `tb_resource` VALUES (110, 2, 2, '/back/articles/status', '批量更新文章状态', 'PUT', 0, 0, 0, 2, '2023-07-08 18:24:10', NULL, NULL);
INSERT INTO `tb_resource` VALUES (111, 2, 2, '/back/articles', '物理批量删除文章', 'DELETE', 0, 0, 0, 2, '2023-07-08 18:51:02', NULL, NULL);
INSERT INTO `tb_resource` VALUES (112, 2, 2, '/back/article/status', '修改文章状态', 'PUT', 0, 0, 0, 2, '2023-07-10 15:02:03', NULL, NULL);
INSERT INTO `tb_resource` VALUES (113, 2, 2, '/back/article/image', '上传文章图片', 'POST', 0, 0, 0, 2, '2023-07-08 09:12:09', NULL, NULL);
INSERT INTO `tb_resource` VALUES (114, 2, 2, '/back/article/images', '批量更新文章图片状态', 'PUT', 0, 0, 0, 2, '2023-07-09 15:01:11', NULL, NULL);
INSERT INTO `tb_resource` VALUES (115, 2, 2, '/articles', '查看文章列表', 'GET', 0, 0, 1, 2, '2024-02-27 14:38:23', NULL, NULL);
INSERT INTO `tb_resource` VALUES (116, 2, 2, '/article/*', '根据文章id查找文章', 'GET', 0, 0, 1, 2, '2024-03-02 10:20:39', NULL, NULL);
INSERT INTO `tb_resource` VALUES (117, 2, 2, '/articles/newest', '查看最新文章列表', 'GET', 0, 0, 1, 2, '2024-03-05 07:10:09', NULL, NULL);
INSERT INTO `tb_resource` VALUES (118, 2, 2, '/article/like/*', '点赞文章', 'POST', 0, 0, 0, 2, '2024-03-09 17:44:56', NULL, NULL);
INSERT INTO `tb_resource` VALUES (119, 2, 2, '/articles/preview', '预览文章列表', 'GET', 0, 0, 1, 2, '2024-03-13 07:47:25', NULL, NULL);
INSERT INTO `tb_resource` VALUES (120, 2, 2, '/articles/archive', '查看文章归档', 'GET', 0, 0, 1, 2, '2024-03-15 06:21:31', NULL, NULL);
INSERT INTO `tb_resource` VALUES (121, 2, 2, '/articles/search', '搜索文章列表', 'GET', 0, 0, 1, 2, '2024-03-19 06:22:01', NULL, NULL);
INSERT INTO `tb_resource` VALUES (122, 2, 3, '/back/categories', '查看后台分类列表', 'GET', 0, 0, 0, 2, '2023-07-11 10:25:18', NULL, NULL);
INSERT INTO `tb_resource` VALUES (123, 2, 3, '/back/category/status', '修改分类状态', 'PUT', 0, 0, 0, 2, '2023-07-12 07:03:22', NULL, NULL);
INSERT INTO `tb_resource` VALUES (124, 2, 3, '/back/categories', '物理批量删除分类', 'DELETE', 0, 0, 0, 2, '2023-07-12 07:04:01', NULL, NULL);
INSERT INTO `tb_resource` VALUES (125, 2, 3, '/back/category', '添加或修改分类', 'POST', 0, 0, 0, 2, '2023-07-12 07:04:38', NULL, NULL);
INSERT INTO `tb_resource` VALUES (126, 2, 3, '/back/categories/status', '批量更新分类状态', 'PUT', 0, 0, 0, 2, '2023-10-19 08:02:49', NULL, NULL);
INSERT INTO `tb_resource` VALUES (127, 2, 3, '/categories', '查看分类列表', 'GET', 0, 0, 1, 2, '2024-03-15 10:39:42', NULL, NULL);
INSERT INTO `tb_resource` VALUES (128, 2, 4, '/back/tags', '查看后台标签列表', 'GET', 0, 0, 0, 2, '2023-07-12 10:48:53', NULL, NULL);
INSERT INTO `tb_resource` VALUES (129, 2, 4, '/back/tags', '物理批量删除标签', 'DELETE', 0, 0, 0, 2, '2023-07-12 10:49:26', NULL, NULL);
INSERT INTO `tb_resource` VALUES (130, 2, 4, '/back/tag', '添加或修改标签', 'POST', 0, 0, 0, 2, '2023-07-12 10:50:07', NULL, NULL);
INSERT INTO `tb_resource` VALUES (131, 2, 4, '/back/tags/status', '批量更新标签状态', 'PUT', 0, 0, 0, 2, '2023-10-20 07:47:59', NULL, NULL);
INSERT INTO `tb_resource` VALUES (132, 2, 4, '/tags', '查看标签列表', 'GET', 0, 0, 1, 2, '2024-03-15 10:40:31', NULL, NULL);
INSERT INTO `tb_resource` VALUES (133, 2, 5, '/back/users', '查看后台用户列表', 'GET', 0, 0, 0, 2, '2023-08-17 19:26:43', NULL, NULL);
INSERT INTO `tb_resource` VALUES (134, 2, 5, '/back/users', '物理批量删除用户', 'DELETE', 0, 0, 0, 2, '2023-08-26 08:53:19', NULL, NULL);
INSERT INTO `tb_resource` VALUES (135, 2, 5, '/back/user', '添加或修改用户', 'POST', 0, 0, 0, 2, '2023-08-26 08:53:43', NULL, NULL);
INSERT INTO `tb_resource` VALUES (136, 2, 5, '/back/users/status', '批量更新用户状态', 'PUT', 0, 0, 0, 2, '2023-08-26 08:55:14', NULL, NULL);
INSERT INTO `tb_resource` VALUES (137, 2, 5, '/back/user/email', '查看邮箱是否存在', 'GET', 0, 0, 0, 2, '2023-08-27 09:19:15', NULL, NULL);
INSERT INTO `tb_resource` VALUES (138, 2, 5, '/back/user/avatar', '上传用户头像', 'POST', 0, 0, 0, 2, '2023-08-27 13:59:28', NULL, NULL);
INSERT INTO `tb_resource` VALUES (139, 2, 5, '/back/user/avatars', '批量更新用户头像状态', 'PUT', 0, 0, 0, 2, '2023-08-27 13:59:55', NULL, NULL);
INSERT INTO `tb_resource` VALUES (140, 2, 5, '/back/user/onlines', '查看后台在线用户列表', 'GET', 0, 0, 0, 2, '2023-09-02 07:18:11', NULL, NULL);
INSERT INTO `tb_resource` VALUES (141, 2, 5, '/back/user/onlines', '批量下线用户', 'DELETE', 0, 0, 0, 2, '2023-09-02 10:54:36', NULL, NULL);
INSERT INTO `tb_resource` VALUES (142, 2, 5, '/user', '修改用户信息', 'PUT', 0, 0, 0, 2, '2023-09-23 10:51:30', NULL, NULL);
INSERT INTO `tb_resource` VALUES (143, 2, 5, '/back/user/username', '查看用户是否存在', 'GET', 0, 0, 0, 2, '2023-11-04 14:47:05', NULL, NULL);
INSERT INTO `tb_resource` VALUES (144, 2, 5, '/user/avatar', '修改用户头像', 'POST', 0, 0, 0, 2, '2023-11-05 11:19:52', NULL, NULL);
INSERT INTO `tb_resource` VALUES (145, 2, 5, '/user/email/code', '发送邮箱验证码', 'POST', 0, 0, 1, 2, '2024-03-16 15:34:09', NULL, NULL);
INSERT INTO `tb_resource` VALUES (146, 2, 5, '/user/register', '用户注册', 'POST', 0, 0, 1, 2, '2024-03-16 15:35:11', NULL, NULL);
INSERT INTO `tb_resource` VALUES (147, 2, 5, '/user/email', '邮箱换绑', 'PUT', 0, 0, 0, 2, '2024-03-18 06:11:34', NULL, NULL);
INSERT INTO `tb_resource` VALUES (148, 2, 6, '/back/multiFiles', '查看后台文件列表', 'GET', 0, 0, 0, 2, '2023-11-15 06:27:58', NULL, NULL);
INSERT INTO `tb_resource` VALUES (149, 2, 6, '/back/multiFile', '添加或修改文件', 'POST', 0, 0, 0, 2, '2023-11-18 14:37:09', NULL, NULL);
INSERT INTO `tb_resource` VALUES (150, 2, 6, '/back/multiFile/status', '修改文件状态', 'PUT', 0, 0, 0, 2, '2023-11-18 14:37:34', NULL, NULL);
INSERT INTO `tb_resource` VALUES (151, 2, 6, '/back/multiFiles', '批量上传文件', 'POST', 0, 0, 0, 2, '2023-11-25 17:32:45', NULL, NULL);
INSERT INTO `tb_resource` VALUES (152, 2, 6, '/back/multiFiles/status', '批量更新文件状态', 'PUT', 0, 0, 0, 2, '2023-11-27 07:51:17', NULL, NULL);
INSERT INTO `tb_resource` VALUES (153, 2, 6, '/back/multiFile/token', '添加或修改文件令牌', 'POST', 0, 0, 0, 2, '2023-12-08 07:26:54', NULL, NULL);
INSERT INTO `tb_resource` VALUES (154, 2, 6, '/back/multiFile/*', '根据文件id查找文件令牌', 'GET', 0, 0, 0, 2, '2023-12-08 07:27:33', NULL, NULL);
INSERT INTO `tb_resource` VALUES (155, 2, 6, '/back/multiFiles', '物理批量删除文件', 'DELETE', 0, 0, 0, 2, '2023-12-08 07:28:13', NULL, NULL);
INSERT INTO `tb_resource` VALUES (156, 2, 7, '/back/comments', '查看后台评论列表', 'GET', 0, 0, 0, 2, '2023-07-26 07:35:19', NULL, NULL);
INSERT INTO `tb_resource` VALUES (157, 2, 7, '/back/comments/status', '批量更新评论状态', 'PUT', 0, 0, 0, 2, '2023-07-26 07:36:57', NULL, NULL);
INSERT INTO `tb_resource` VALUES (158, 2, 7, '/back/comments', '物理批量删除评论', 'DELETE', 0, 0, 0, 2, '2023-07-26 07:37:28', NULL, NULL);
INSERT INTO `tb_resource` VALUES (159, 2, 7, '/comments', '查看评论列表', 'GET', 0, 0, 1, 2, '2024-03-05 07:10:59', NULL, NULL);
INSERT INTO `tb_resource` VALUES (160, 2, 7, '/comment', '添加评论', 'POST', 0, 0, 0, 2, '2024-03-08 10:08:36', NULL, NULL);
INSERT INTO `tb_resource` VALUES (161, 2, 7, '/comment/like/*', '点赞评论', 'POST', 0, 0, 0, 2, '2024-03-08 10:09:05', NULL, NULL);
INSERT INTO `tb_resource` VALUES (162, 2, 7, '/comments/reply', '查看评论回复列表', 'GET', 0, 0, 1, 2, '2024-03-09 10:07:38', NULL, NULL);
INSERT INTO `tb_resource` VALUES (163, 2, 8, '/swagger-ui.html', 'SwaggerUI', 'GET', 0, 0, 0, 2, '2023-07-02 11:17:04', NULL, NULL);
INSERT INTO `tb_resource` VALUES (164, 2, 8, '/webjars/**', 'SwaggerUI', 'GET', 0, 0, 0, 2, '2023-07-12 17:18:57', NULL, NULL);
INSERT INTO `tb_resource` VALUES (165, 2, 8, '/login', '登录', 'POST', 0, 0, 0, 2, '2023-07-12 17:20:53', NULL, NULL);
INSERT INTO `tb_resource` VALUES (166, 2, 8, '/v2/**', 'SwaggerUI', 'GET', 0, 0, 0, 2, '2023-07-14 10:53:32', NULL, NULL);
INSERT INTO `tb_resource` VALUES (167, 2, 8, '/swagger-resources/**', 'SwaggerUI', 'GET', 0, 0, 0, 2, '2023-07-14 10:53:59', NULL, NULL);
INSERT INTO `tb_resource` VALUES (168, 2, 9, '/back/messages', '查看后台留言列表', 'GET', 0, 0, 0, 2, '2023-07-29 19:17:11', NULL, NULL);
INSERT INTO `tb_resource` VALUES (169, 2, 9, '/back/messages/status', '批量更新留言状态', 'PUT', 0, 0, 0, 2, '2023-08-05 10:26:02', NULL, NULL);
INSERT INTO `tb_resource` VALUES (170, 2, 9, '/back/messages', '物理批量删除留言', 'DELETE', 0, 0, 0, 2, '2023-07-29 19:17:51', NULL, NULL);
INSERT INTO `tb_resource` VALUES (171, 2, 9, '/message', '添加留言', 'POST', 0, 0, 1, 2, '2024-03-15 06:22:35', NULL, NULL);
INSERT INTO `tb_resource` VALUES (172, 2, 9, '/messages', '查看留言列表', 'GET', 0, 0, 1, 2, '2024-03-15 06:22:52', NULL, NULL);
INSERT INTO `tb_resource` VALUES (173, 2, 10, '/back/friendLinks', '查看后台友链列表', 'GET', 0, 0, 0, 2, '2023-08-03 18:59:20', NULL, NULL);
INSERT INTO `tb_resource` VALUES (174, 2, 10, '/back/friendLinks/status', '批量更新友链状态', 'PUT', 0, 0, 0, 2, '2023-08-03 19:00:05', NULL, NULL);
INSERT INTO `tb_resource` VALUES (175, 2, 10, '/back/friendLinks', '物理批量删除友链', 'DELETE', 0, 0, 0, 2, '2023-08-03 19:00:36', NULL, NULL);
INSERT INTO `tb_resource` VALUES (176, 2, 10, '/back/friendLink', '添加或修改友链', 'POST', 0, 0, 0, 2, '2023-08-03 19:01:04', NULL, NULL);
INSERT INTO `tb_resource` VALUES (177, 2, 10, '/friendLinks', '查看友链列表', 'GET', 0, 0, 1, 2, '2024-03-15 06:23:39', NULL, NULL);
INSERT INTO `tb_resource` VALUES (178, 2, 11, '/back/resources', '查看后台资源列表', 'GET', 0, 0, 0, 2, '2023-08-08 19:06:04', NULL, NULL);
INSERT INTO `tb_resource` VALUES (179, 2, 11, '/back/resource/status', '修改资源状态', 'PUT', 0, 0, 0, 2, '2023-08-08 19:07:17', NULL, NULL);
INSERT INTO `tb_resource` VALUES (180, 2, 11, '/back/resources', '物理批量删除资源', 'DELETE', 0, 0, 0, 2, '2023-08-08 19:07:49', NULL, NULL);
INSERT INTO `tb_resource` VALUES (181, 2, 11, '/back/resource', '添加或修改资源', 'POST', 0, 0, 0, 2, '2023-08-08 19:08:24', NULL, NULL);
INSERT INTO `tb_resource` VALUES (182, 2, 11, '/back/resource/moduleNames', '查看所有的模块名', 'GET', 0, 0, 0, 2, '2023-09-18 08:23:55', NULL, NULL);
INSERT INTO `tb_resource` VALUES (183, 2, 12, '/back/roles', '查看后台角色列表', 'GET', 0, 0, 0, 2, '2023-08-13 09:05:00', NULL, NULL);
INSERT INTO `tb_resource` VALUES (184, 2, 12, '/back/role/permission', '查看角色权限', 'GET', 0, 0, 0, 2, '2023-08-13 09:05:38', NULL, NULL);
INSERT INTO `tb_resource` VALUES (185, 2, 12, '/back/role/status', '修改角色状态', 'PUT', 0, 0, 0, 2, '2023-08-13 09:06:11', NULL, NULL);
INSERT INTO `tb_resource` VALUES (186, 2, 12, '/back/roles', '物理批量删除角色', 'DELETE', 0, 0, 0, 2, '2023-08-13 09:06:40', NULL, NULL);
INSERT INTO `tb_resource` VALUES (187, 2, 12, '/back/role', '添加或修改角色', 'POST', 0, 0, 0, 2, '2023-08-13 09:07:13', NULL, NULL);
INSERT INTO `tb_resource` VALUES (188, 2, 12, '/back/role/permission', '修改角色权限', 'PUT', 0, 0, 0, 2, '2023-08-13 14:59:02', NULL, NULL);
INSERT INTO `tb_resource` VALUES (189, 2, 12, '/back/role/roleNames', '查看所有的角色名', 'GET', 0, 0, 0, 2, '2023-08-24 08:03:00', NULL, NULL);
INSERT INTO `tb_resource` VALUES (190, 2, 13, '/back/userAuth/usernames', '查看所有的用户名', 'GET', 0, 0, 0, 2, '2023-07-14 13:57:41', NULL, NULL);
INSERT INTO `tb_resource` VALUES (191, 2, 13, '/back/userAuths', '查看后台账号列表', 'GET', 0, 0, 0, 2, '2023-08-26 08:54:25', NULL, NULL);
INSERT INTO `tb_resource` VALUES (192, 2, 13, '/back/userAuth/status', '修改帐号状态', 'PUT', 0, 0, 0, 2, '2023-08-26 08:54:49', NULL, NULL);
INSERT INTO `tb_resource` VALUES (193, 2, 13, '/back/userAuth', '修改账号', 'PUT', 0, 0, 0, 2, '2023-08-30 06:23:55', NULL, NULL);
INSERT INTO `tb_resource` VALUES (194, 2, 13, '/userAuth/password', '修改用户密码', 'PUT', 0, 0, 0, 2, '2023-11-05 11:20:30', NULL, NULL);
INSERT INTO `tb_resource` VALUES (195, 2, 13, '/userAuth/forget', '忘记密码', 'PUT', 0, 0, 1, 2, '2024-03-16 18:05:56', NULL, NULL);
INSERT INTO `tb_resource` VALUES (196, 2, 14, '/back/systemConfigs', '查看后台系统配置列表', 'GET', 0, 0, 0, 2, '2023-09-06 18:49:01', NULL, NULL);
INSERT INTO `tb_resource` VALUES (197, 2, 14, '/back/systemConfig', '添加或修改系统配置', 'POST', 0, 0, 0, 2, '2023-09-06 18:49:24', NULL, NULL);
INSERT INTO `tb_resource` VALUES (198, 2, 14, '/back/systemConfigs', '物理批量删除系统配置', 'DELETE', 0, 0, 0, 2, '2023-09-10 15:32:25', NULL, NULL);
INSERT INTO `tb_resource` VALUES (199, 2, 15, '/back/userConfigs', '查看后台用户配置列表', 'GET', 0, 0, 0, 2, '2023-09-08 07:29:13', NULL, NULL);
INSERT INTO `tb_resource` VALUES (200, 2, 15, '/back/userConfig', '修改用户配置', 'PUT', 0, 0, 0, 2, '2023-09-08 07:29:37', NULL, NULL);
INSERT INTO `tb_resource` VALUES (201, 2, 15, '/back/userConfigs/status', '批量更新用户配置状态', 'PUT', 0, 0, 0, 2, '2023-09-10 15:33:03', NULL, NULL);
INSERT INTO `tb_resource` VALUES (203, 2, 16, '/back/exceptionLogs', '查看后台异常日志列表', 'GET', 0, 0, 0, 2, '2023-09-18 07:56:00', NULL, NULL);
INSERT INTO `tb_resource` VALUES (204, 2, 17, '/back/loginLogs', '查看后台登录日志列表', 'GET', 0, 0, 0, 2, '2023-09-20 07:40:20', NULL, NULL);
INSERT INTO `tb_resource` VALUES (205, 2, 18, '/back/operationLogs', '查看后台操作日志列表', 'GET', 0, 0, 0, 2, '2023-09-20 07:40:58', NULL, NULL);
INSERT INTO `tb_resource` VALUES (206, 2, 19, '/back/blog/home', '查看后台首页信息', 'GET', 0, 0, 0, 2, '2023-04-26 21:34:33', NULL, NULL);
INSERT INTO `tb_resource` VALUES (207, 2, 19, '/blog/info', '查看博客信息', 'GET', 0, 0, 1, 2, '2024-02-27 14:42:22', NULL, NULL);
INSERT INTO `tb_resource` VALUES (208, 2, 19, '/blog/id', '查看博主id', 'GET', 0, 0, 1, 2, '2024-03-06 16:29:43', NULL, NULL);
INSERT INTO `tb_resource` VALUES (209, 2, 19, '/back/about', '修改关于我', 'PUT', 0, 0, 0, 2, '2024-03-14 07:19:09', NULL, NULL);
INSERT INTO `tb_resource` VALUES (210, 2, 19, '/about', '查看关于我', 'GET', 0, 0, 1, 2, '2024-03-14 07:19:30', NULL, NULL);
INSERT INTO `tb_resource` VALUES (211, 2, 8, '/websocket', 'websocket后台接口', 'GET', 0, 0, 1, 2, '2024-03-31 17:29:59', NULL, NULL);
INSERT INTO `tb_resource` VALUES (213, 2, 20, '/chatRecord', '发送文本消息', 'POST', 0, 0, 1, 2, '2024-03-31 17:32:39', NULL, NULL);
INSERT INTO `tb_resource` VALUES (214, 2, 20, '/chatRecord/voice', '发送语音消息', 'POST', 0, 0, 0, 2, '2024-03-31 17:33:04', NULL, NULL);
INSERT INTO `tb_resource` VALUES (215, 2, 20, '/chatRecord/*', '撤回聊天记录', 'PUT', 0, 0, 0, 2, '2024-03-31 17:33:33', NULL, NULL);
INSERT INTO `tb_resource` VALUES (216, 2, 20, '/chatRecords', '查看聊天记录', 'GET', 0, 0, 1, 2, '2024-03-31 17:33:53', NULL, NULL);
INSERT INTO `tb_resource` VALUES (217, 2, 19, '/blog/token', '校验访问令牌', 'POST', 0, 0, 0, 2, '2024-04-01 06:37:55', NULL, NULL);
INSERT INTO `tb_resource` VALUES (218, 2, 5, '/user/oauth/qq', 'qq登录', 'POST', 0, 0, 1, 2, '2024-04-01 06:40:28', NULL, NULL);
INSERT INTO `tb_resource` VALUES (219, 2, 2, '/back/article/token', '添加或修改文件令牌', 'POST', 0, 0, 0, 2, '2024-04-01 06:41:41', NULL, NULL);
INSERT INTO `tb_resource` VALUES (220, 2, 2, '/back/article/token/*', '根据文章id查找文章令牌', 'GET', 0, 0, 0, 2, '2024-04-01 06:42:06', NULL, NULL);
INSERT INTO `tb_resource` VALUES (221, 2, 13, '/back/userAuth/username', '修改用户名', 'PUT', 0, 0, 0, 2, '2024-04-06 22:48:10', NULL, NULL);

-- ----------------------------
-- Table structure for tb_role
-- ----------------------------
DROP TABLE IF EXISTS `tb_role`;
CREATE TABLE `tb_role`  (
  `id` int(255) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `role_desc` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色描述',
  `role_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色名称',
  `role_weight` int(11) NOT NULL DEFAULT 1000 COMMENT '角色权重, 默认1000',
  `disabled_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未禁用, 1已禁用, 默认0',
  `deletable_flag` tinyint(1) NOT NULL DEFAULT 1 COMMENT '0不可删除, 1可删除, 默认1',
  `create_user` int(11) NOT NULL COMMENT '创建人',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_user` int(11) NULL DEFAULT NULL COMMENT '更新人, 默认null',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间, 默认null',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_role
-- ----------------------------
INSERT INTO `tb_role` VALUES (1, 2, 'root', 'root', 100, 0, 0, 2, '2023-04-25 08:47:06', NULL, NULL);
INSERT INTO `tb_role` VALUES (2, 2, '管理', 'admin', 200, 0, 0, 2, '2023-04-25 19:42:22', NULL, NULL);
INSERT INTO `tb_role` VALUES (3, 2, '编辑', 'editor', 300, 0, 0, 2, '2023-04-25 19:44:08', NULL, NULL);
INSERT INTO `tb_role` VALUES (4, 2, '作者', 'author', 400, 0, 0, 2, '2023-04-26 21:35:54', NULL, NULL);
INSERT INTO `tb_role` VALUES (5, 2, '用户', 'user', 500, 0, 0, 2, '2023-04-26 21:36:11', NULL, NULL);
INSERT INTO `tb_role` VALUES (6, 2, '测试', 'test', 1000, 0, 0, 2, '2023-04-26 21:36:30', NULL, NULL);

-- ----------------------------
-- Table structure for tb_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `tb_role_menu`;
CREATE TABLE `tb_role_menu`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `role_id` int(11) NOT NULL COMMENT '角色id',
  `menu_id` int(11) NOT NULL COMMENT '菜单id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 131 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

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
INSERT INTO `tb_role_menu` VALUES (10, 1, 10);
INSERT INTO `tb_role_menu` VALUES (11, 1, 101);
INSERT INTO `tb_role_menu` VALUES (12, 1, 102);
INSERT INTO `tb_role_menu` VALUES (13, 1, 103);
INSERT INTO `tb_role_menu` VALUES (14, 1, 104);
INSERT INTO `tb_role_menu` VALUES (15, 1, 105);
INSERT INTO `tb_role_menu` VALUES (16, 1, 106);
INSERT INTO `tb_role_menu` VALUES (17, 1, 107);
INSERT INTO `tb_role_menu` VALUES (18, 1, 108);
INSERT INTO `tb_role_menu` VALUES (19, 1, 109);
INSERT INTO `tb_role_menu` VALUES (20, 1, 110);
INSERT INTO `tb_role_menu` VALUES (21, 1, 111);
INSERT INTO `tb_role_menu` VALUES (22, 1, 112);
INSERT INTO `tb_role_menu` VALUES (23, 1, 113);
INSERT INTO `tb_role_menu` VALUES (24, 1, 114);
INSERT INTO `tb_role_menu` VALUES (25, 1, 115);
INSERT INTO `tb_role_menu` VALUES (26, 1, 116);
INSERT INTO `tb_role_menu` VALUES (27, 1, 117);
INSERT INTO `tb_role_menu` VALUES (28, 1, 118);
INSERT INTO `tb_role_menu` VALUES (29, 1, 119);
INSERT INTO `tb_role_menu` VALUES (30, 1, 120);
INSERT INTO `tb_role_menu` VALUES (31, 1, 121);
INSERT INTO `tb_role_menu` VALUES (32, 1, 122);
INSERT INTO `tb_role_menu` VALUES (33, 1, 123);
INSERT INTO `tb_role_menu` VALUES (34, 1, 124);
INSERT INTO `tb_role_menu` VALUES (35, 2, 1);
INSERT INTO `tb_role_menu` VALUES (36, 2, 2);
INSERT INTO `tb_role_menu` VALUES (37, 2, 101);
INSERT INTO `tb_role_menu` VALUES (38, 2, 102);
INSERT INTO `tb_role_menu` VALUES (39, 2, 103);
INSERT INTO `tb_role_menu` VALUES (40, 2, 104);
INSERT INTO `tb_role_menu` VALUES (41, 2, 105);
INSERT INTO `tb_role_menu` VALUES (42, 2, 3);
INSERT INTO `tb_role_menu` VALUES (43, 2, 106);
INSERT INTO `tb_role_menu` VALUES (44, 2, 107);
INSERT INTO `tb_role_menu` VALUES (45, 2, 4);
INSERT INTO `tb_role_menu` VALUES (46, 2, 108);
INSERT INTO `tb_role_menu` VALUES (47, 2, 5);
INSERT INTO `tb_role_menu` VALUES (48, 2, 109);
INSERT INTO `tb_role_menu` VALUES (49, 2, 110);
INSERT INTO `tb_role_menu` VALUES (50, 2, 111);
INSERT INTO `tb_role_menu` VALUES (51, 2, 7);
INSERT INTO `tb_role_menu` VALUES (52, 2, 116);
INSERT INTO `tb_role_menu` VALUES (53, 2, 117);
INSERT INTO `tb_role_menu` VALUES (54, 2, 118);
INSERT INTO `tb_role_menu` VALUES (55, 2, 9);
INSERT INTO `tb_role_menu` VALUES (56, 2, 122);
INSERT INTO `tb_role_menu` VALUES (57, 2, 10);
INSERT INTO `tb_role_menu` VALUES (58, 2, 123);
INSERT INTO `tb_role_menu` VALUES (59, 2, 124);
INSERT INTO `tb_role_menu` VALUES (90, 3, 1);
INSERT INTO `tb_role_menu` VALUES (91, 3, 2);
INSERT INTO `tb_role_menu` VALUES (92, 3, 101);
INSERT INTO `tb_role_menu` VALUES (93, 3, 102);
INSERT INTO `tb_role_menu` VALUES (94, 3, 103);
INSERT INTO `tb_role_menu` VALUES (95, 3, 104);
INSERT INTO `tb_role_menu` VALUES (96, 3, 105);
INSERT INTO `tb_role_menu` VALUES (97, 3, 106);
INSERT INTO `tb_role_menu` VALUES (98, 3, 109);
INSERT INTO `tb_role_menu` VALUES (99, 3, 110);
INSERT INTO `tb_role_menu` VALUES (100, 3, 9);
INSERT INTO `tb_role_menu` VALUES (101, 3, 122);
INSERT INTO `tb_role_menu` VALUES (102, 3, 10);
INSERT INTO `tb_role_menu` VALUES (103, 3, 123);
INSERT INTO `tb_role_menu` VALUES (104, 3, 124);
INSERT INTO `tb_role_menu` VALUES (105, 3, 3);
INSERT INTO `tb_role_menu` VALUES (106, 3, 5);
INSERT INTO `tb_role_menu` VALUES (107, 5, 1);
INSERT INTO `tb_role_menu` VALUES (108, 5, 106);
INSERT INTO `tb_role_menu` VALUES (109, 5, 9);
INSERT INTO `tb_role_menu` VALUES (110, 5, 122);
INSERT INTO `tb_role_menu` VALUES (111, 5, 123);
INSERT INTO `tb_role_menu` VALUES (112, 5, 3);
INSERT INTO `tb_role_menu` VALUES (113, 5, 10);
INSERT INTO `tb_role_menu` VALUES (114, 4, 1);
INSERT INTO `tb_role_menu` VALUES (115, 4, 2);
INSERT INTO `tb_role_menu` VALUES (116, 4, 101);
INSERT INTO `tb_role_menu` VALUES (117, 4, 102);
INSERT INTO `tb_role_menu` VALUES (118, 4, 103);
INSERT INTO `tb_role_menu` VALUES (119, 4, 104);
INSERT INTO `tb_role_menu` VALUES (120, 4, 105);
INSERT INTO `tb_role_menu` VALUES (121, 4, 106);
INSERT INTO `tb_role_menu` VALUES (122, 4, 109);
INSERT INTO `tb_role_menu` VALUES (123, 4, 110);
INSERT INTO `tb_role_menu` VALUES (124, 4, 9);
INSERT INTO `tb_role_menu` VALUES (125, 4, 122);
INSERT INTO `tb_role_menu` VALUES (126, 4, 10);
INSERT INTO `tb_role_menu` VALUES (127, 4, 123);
INSERT INTO `tb_role_menu` VALUES (128, 4, 124);
INSERT INTO `tb_role_menu` VALUES (129, 4, 3);
INSERT INTO `tb_role_menu` VALUES (130, 4, 5);

-- ----------------------------
-- Table structure for tb_role_resource
-- ----------------------------
DROP TABLE IF EXISTS `tb_role_resource`;
CREATE TABLE `tb_role_resource`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `role_id` int(11) NOT NULL COMMENT '角色id',
  `resource_id` int(11) NOT NULL COMMENT '资源id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1105 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_role_resource
-- ----------------------------
INSERT INTO `tb_role_resource` VALUES (523, 1, 1);
INSERT INTO `tb_role_resource` VALUES (524, 1, 101);
INSERT INTO `tb_role_resource` VALUES (525, 1, 102);
INSERT INTO `tb_role_resource` VALUES (526, 1, 103);
INSERT INTO `tb_role_resource` VALUES (527, 1, 104);
INSERT INTO `tb_role_resource` VALUES (528, 1, 105);
INSERT INTO `tb_role_resource` VALUES (529, 1, 106);
INSERT INTO `tb_role_resource` VALUES (530, 1, 107);
INSERT INTO `tb_role_resource` VALUES (531, 1, 108);
INSERT INTO `tb_role_resource` VALUES (532, 1, 109);
INSERT INTO `tb_role_resource` VALUES (533, 1, 110);
INSERT INTO `tb_role_resource` VALUES (534, 1, 111);
INSERT INTO `tb_role_resource` VALUES (535, 1, 112);
INSERT INTO `tb_role_resource` VALUES (536, 1, 113);
INSERT INTO `tb_role_resource` VALUES (537, 1, 114);
INSERT INTO `tb_role_resource` VALUES (538, 1, 118);
INSERT INTO `tb_role_resource` VALUES (539, 1, 219);
INSERT INTO `tb_role_resource` VALUES (540, 1, 220);
INSERT INTO `tb_role_resource` VALUES (541, 1, 122);
INSERT INTO `tb_role_resource` VALUES (542, 1, 123);
INSERT INTO `tb_role_resource` VALUES (543, 1, 124);
INSERT INTO `tb_role_resource` VALUES (544, 1, 125);
INSERT INTO `tb_role_resource` VALUES (545, 1, 126);
INSERT INTO `tb_role_resource` VALUES (546, 1, 128);
INSERT INTO `tb_role_resource` VALUES (547, 1, 129);
INSERT INTO `tb_role_resource` VALUES (548, 1, 130);
INSERT INTO `tb_role_resource` VALUES (549, 1, 131);
INSERT INTO `tb_role_resource` VALUES (550, 1, 133);
INSERT INTO `tb_role_resource` VALUES (551, 1, 134);
INSERT INTO `tb_role_resource` VALUES (552, 1, 135);
INSERT INTO `tb_role_resource` VALUES (553, 1, 136);
INSERT INTO `tb_role_resource` VALUES (554, 1, 137);
INSERT INTO `tb_role_resource` VALUES (555, 1, 138);
INSERT INTO `tb_role_resource` VALUES (556, 1, 139);
INSERT INTO `tb_role_resource` VALUES (557, 1, 140);
INSERT INTO `tb_role_resource` VALUES (558, 1, 141);
INSERT INTO `tb_role_resource` VALUES (559, 1, 142);
INSERT INTO `tb_role_resource` VALUES (560, 1, 143);
INSERT INTO `tb_role_resource` VALUES (561, 1, 144);
INSERT INTO `tb_role_resource` VALUES (562, 1, 147);
INSERT INTO `tb_role_resource` VALUES (563, 1, 6);
INSERT INTO `tb_role_resource` VALUES (564, 1, 148);
INSERT INTO `tb_role_resource` VALUES (565, 1, 149);
INSERT INTO `tb_role_resource` VALUES (566, 1, 150);
INSERT INTO `tb_role_resource` VALUES (567, 1, 151);
INSERT INTO `tb_role_resource` VALUES (568, 1, 152);
INSERT INTO `tb_role_resource` VALUES (569, 1, 153);
INSERT INTO `tb_role_resource` VALUES (570, 1, 154);
INSERT INTO `tb_role_resource` VALUES (571, 1, 155);
INSERT INTO `tb_role_resource` VALUES (572, 1, 156);
INSERT INTO `tb_role_resource` VALUES (573, 1, 157);
INSERT INTO `tb_role_resource` VALUES (574, 1, 158);
INSERT INTO `tb_role_resource` VALUES (575, 1, 160);
INSERT INTO `tb_role_resource` VALUES (576, 1, 161);
INSERT INTO `tb_role_resource` VALUES (577, 1, 163);
INSERT INTO `tb_role_resource` VALUES (578, 1, 164);
INSERT INTO `tb_role_resource` VALUES (579, 1, 166);
INSERT INTO `tb_role_resource` VALUES (580, 1, 167);
INSERT INTO `tb_role_resource` VALUES (581, 1, 168);
INSERT INTO `tb_role_resource` VALUES (582, 1, 169);
INSERT INTO `tb_role_resource` VALUES (583, 1, 170);
INSERT INTO `tb_role_resource` VALUES (584, 1, 173);
INSERT INTO `tb_role_resource` VALUES (585, 1, 174);
INSERT INTO `tb_role_resource` VALUES (586, 1, 175);
INSERT INTO `tb_role_resource` VALUES (587, 1, 176);
INSERT INTO `tb_role_resource` VALUES (588, 1, 11);
INSERT INTO `tb_role_resource` VALUES (589, 1, 178);
INSERT INTO `tb_role_resource` VALUES (590, 1, 179);
INSERT INTO `tb_role_resource` VALUES (591, 1, 180);
INSERT INTO `tb_role_resource` VALUES (592, 1, 181);
INSERT INTO `tb_role_resource` VALUES (593, 1, 182);
INSERT INTO `tb_role_resource` VALUES (594, 1, 12);
INSERT INTO `tb_role_resource` VALUES (595, 1, 183);
INSERT INTO `tb_role_resource` VALUES (596, 1, 184);
INSERT INTO `tb_role_resource` VALUES (597, 1, 185);
INSERT INTO `tb_role_resource` VALUES (598, 1, 186);
INSERT INTO `tb_role_resource` VALUES (599, 1, 187);
INSERT INTO `tb_role_resource` VALUES (600, 1, 188);
INSERT INTO `tb_role_resource` VALUES (601, 1, 189);
INSERT INTO `tb_role_resource` VALUES (602, 1, 190);
INSERT INTO `tb_role_resource` VALUES (603, 1, 191);
INSERT INTO `tb_role_resource` VALUES (604, 1, 192);
INSERT INTO `tb_role_resource` VALUES (605, 1, 193);
INSERT INTO `tb_role_resource` VALUES (606, 1, 194);
INSERT INTO `tb_role_resource` VALUES (607, 1, 14);
INSERT INTO `tb_role_resource` VALUES (608, 1, 196);
INSERT INTO `tb_role_resource` VALUES (609, 1, 197);
INSERT INTO `tb_role_resource` VALUES (610, 1, 198);
INSERT INTO `tb_role_resource` VALUES (611, 1, 199);
INSERT INTO `tb_role_resource` VALUES (612, 1, 200);
INSERT INTO `tb_role_resource` VALUES (613, 1, 201);
INSERT INTO `tb_role_resource` VALUES (614, 1, 16);
INSERT INTO `tb_role_resource` VALUES (615, 1, 203);
INSERT INTO `tb_role_resource` VALUES (616, 1, 17);
INSERT INTO `tb_role_resource` VALUES (617, 1, 204);
INSERT INTO `tb_role_resource` VALUES (618, 1, 18);
INSERT INTO `tb_role_resource` VALUES (619, 1, 205);
INSERT INTO `tb_role_resource` VALUES (620, 1, 206);
INSERT INTO `tb_role_resource` VALUES (621, 1, 209);
INSERT INTO `tb_role_resource` VALUES (622, 1, 217);
INSERT INTO `tb_role_resource` VALUES (623, 1, 214);
INSERT INTO `tb_role_resource` VALUES (624, 1, 215);
INSERT INTO `tb_role_resource` VALUES (625, 1, 2);
INSERT INTO `tb_role_resource` VALUES (626, 1, 3);
INSERT INTO `tb_role_resource` VALUES (627, 1, 4);
INSERT INTO `tb_role_resource` VALUES (628, 1, 5);
INSERT INTO `tb_role_resource` VALUES (629, 1, 7);
INSERT INTO `tb_role_resource` VALUES (630, 1, 8);
INSERT INTO `tb_role_resource` VALUES (631, 1, 9);
INSERT INTO `tb_role_resource` VALUES (632, 1, 10);
INSERT INTO `tb_role_resource` VALUES (633, 1, 13);
INSERT INTO `tb_role_resource` VALUES (634, 1, 15);
INSERT INTO `tb_role_resource` VALUES (635, 1, 19);
INSERT INTO `tb_role_resource` VALUES (636, 1, 20);
INSERT INTO `tb_role_resource` VALUES (835, 5, 101);
INSERT INTO `tb_role_resource` VALUES (836, 5, 138);
INSERT INTO `tb_role_resource` VALUES (837, 5, 142);
INSERT INTO `tb_role_resource` VALUES (838, 5, 144);
INSERT INTO `tb_role_resource` VALUES (839, 5, 147);
INSERT INTO `tb_role_resource` VALUES (840, 5, 148);
INSERT INTO `tb_role_resource` VALUES (841, 5, 149);
INSERT INTO `tb_role_resource` VALUES (842, 5, 150);
INSERT INTO `tb_role_resource` VALUES (843, 5, 151);
INSERT INTO `tb_role_resource` VALUES (844, 5, 152);
INSERT INTO `tb_role_resource` VALUES (845, 5, 153);
INSERT INTO `tb_role_resource` VALUES (846, 5, 154);
INSERT INTO `tb_role_resource` VALUES (847, 5, 156);
INSERT INTO `tb_role_resource` VALUES (848, 5, 157);
INSERT INTO `tb_role_resource` VALUES (849, 5, 160);
INSERT INTO `tb_role_resource` VALUES (850, 5, 161);
INSERT INTO `tb_role_resource` VALUES (851, 5, 190);
INSERT INTO `tb_role_resource` VALUES (852, 5, 194);
INSERT INTO `tb_role_resource` VALUES (853, 5, 206);
INSERT INTO `tb_role_resource` VALUES (854, 5, 217);
INSERT INTO `tb_role_resource` VALUES (855, 5, 214);
INSERT INTO `tb_role_resource` VALUES (856, 5, 215);
INSERT INTO `tb_role_resource` VALUES (857, 5, 1);
INSERT INTO `tb_role_resource` VALUES (858, 5, 5);
INSERT INTO `tb_role_resource` VALUES (859, 5, 6);
INSERT INTO `tb_role_resource` VALUES (860, 5, 7);
INSERT INTO `tb_role_resource` VALUES (861, 5, 13);
INSERT INTO `tb_role_resource` VALUES (862, 5, 19);
INSERT INTO `tb_role_resource` VALUES (863, 5, 20);
INSERT INTO `tb_role_resource` VALUES (919, 3, 101);
INSERT INTO `tb_role_resource` VALUES (920, 3, 106);
INSERT INTO `tb_role_resource` VALUES (921, 3, 107);
INSERT INTO `tb_role_resource` VALUES (922, 3, 108);
INSERT INTO `tb_role_resource` VALUES (923, 3, 109);
INSERT INTO `tb_role_resource` VALUES (924, 3, 110);
INSERT INTO `tb_role_resource` VALUES (925, 3, 112);
INSERT INTO `tb_role_resource` VALUES (926, 3, 113);
INSERT INTO `tb_role_resource` VALUES (927, 3, 114);
INSERT INTO `tb_role_resource` VALUES (928, 3, 118);
INSERT INTO `tb_role_resource` VALUES (929, 3, 219);
INSERT INTO `tb_role_resource` VALUES (930, 3, 220);
INSERT INTO `tb_role_resource` VALUES (931, 3, 122);
INSERT INTO `tb_role_resource` VALUES (932, 3, 123);
INSERT INTO `tb_role_resource` VALUES (933, 3, 125);
INSERT INTO `tb_role_resource` VALUES (934, 3, 126);
INSERT INTO `tb_role_resource` VALUES (935, 3, 128);
INSERT INTO `tb_role_resource` VALUES (936, 3, 130);
INSERT INTO `tb_role_resource` VALUES (937, 3, 131);
INSERT INTO `tb_role_resource` VALUES (938, 3, 138);
INSERT INTO `tb_role_resource` VALUES (939, 3, 142);
INSERT INTO `tb_role_resource` VALUES (940, 3, 144);
INSERT INTO `tb_role_resource` VALUES (941, 3, 147);
INSERT INTO `tb_role_resource` VALUES (942, 3, 148);
INSERT INTO `tb_role_resource` VALUES (943, 3, 149);
INSERT INTO `tb_role_resource` VALUES (944, 3, 150);
INSERT INTO `tb_role_resource` VALUES (945, 3, 151);
INSERT INTO `tb_role_resource` VALUES (946, 3, 152);
INSERT INTO `tb_role_resource` VALUES (947, 3, 153);
INSERT INTO `tb_role_resource` VALUES (948, 3, 154);
INSERT INTO `tb_role_resource` VALUES (949, 3, 156);
INSERT INTO `tb_role_resource` VALUES (950, 3, 157);
INSERT INTO `tb_role_resource` VALUES (951, 3, 160);
INSERT INTO `tb_role_resource` VALUES (952, 3, 161);
INSERT INTO `tb_role_resource` VALUES (953, 3, 190);
INSERT INTO `tb_role_resource` VALUES (954, 3, 194);
INSERT INTO `tb_role_resource` VALUES (955, 3, 199);
INSERT INTO `tb_role_resource` VALUES (956, 3, 200);
INSERT INTO `tb_role_resource` VALUES (957, 3, 206);
INSERT INTO `tb_role_resource` VALUES (958, 3, 209);
INSERT INTO `tb_role_resource` VALUES (959, 3, 217);
INSERT INTO `tb_role_resource` VALUES (960, 3, 214);
INSERT INTO `tb_role_resource` VALUES (961, 3, 215);
INSERT INTO `tb_role_resource` VALUES (962, 3, 1);
INSERT INTO `tb_role_resource` VALUES (963, 3, 2);
INSERT INTO `tb_role_resource` VALUES (964, 3, 3);
INSERT INTO `tb_role_resource` VALUES (965, 3, 4);
INSERT INTO `tb_role_resource` VALUES (966, 3, 5);
INSERT INTO `tb_role_resource` VALUES (967, 3, 6);
INSERT INTO `tb_role_resource` VALUES (968, 3, 7);
INSERT INTO `tb_role_resource` VALUES (969, 3, 13);
INSERT INTO `tb_role_resource` VALUES (970, 3, 15);
INSERT INTO `tb_role_resource` VALUES (971, 3, 19);
INSERT INTO `tb_role_resource` VALUES (972, 3, 20);
INSERT INTO `tb_role_resource` VALUES (973, 4, 101);
INSERT INTO `tb_role_resource` VALUES (974, 4, 106);
INSERT INTO `tb_role_resource` VALUES (975, 4, 107);
INSERT INTO `tb_role_resource` VALUES (976, 4, 108);
INSERT INTO `tb_role_resource` VALUES (977, 4, 109);
INSERT INTO `tb_role_resource` VALUES (978, 4, 110);
INSERT INTO `tb_role_resource` VALUES (979, 4, 112);
INSERT INTO `tb_role_resource` VALUES (980, 4, 113);
INSERT INTO `tb_role_resource` VALUES (981, 4, 114);
INSERT INTO `tb_role_resource` VALUES (982, 4, 118);
INSERT INTO `tb_role_resource` VALUES (983, 4, 219);
INSERT INTO `tb_role_resource` VALUES (984, 4, 220);
INSERT INTO `tb_role_resource` VALUES (985, 4, 122);
INSERT INTO `tb_role_resource` VALUES (986, 4, 123);
INSERT INTO `tb_role_resource` VALUES (987, 4, 125);
INSERT INTO `tb_role_resource` VALUES (988, 4, 126);
INSERT INTO `tb_role_resource` VALUES (989, 4, 128);
INSERT INTO `tb_role_resource` VALUES (990, 4, 130);
INSERT INTO `tb_role_resource` VALUES (991, 4, 131);
INSERT INTO `tb_role_resource` VALUES (992, 4, 138);
INSERT INTO `tb_role_resource` VALUES (993, 4, 142);
INSERT INTO `tb_role_resource` VALUES (994, 4, 144);
INSERT INTO `tb_role_resource` VALUES (995, 4, 147);
INSERT INTO `tb_role_resource` VALUES (996, 4, 148);
INSERT INTO `tb_role_resource` VALUES (997, 4, 149);
INSERT INTO `tb_role_resource` VALUES (998, 4, 150);
INSERT INTO `tb_role_resource` VALUES (999, 4, 151);
INSERT INTO `tb_role_resource` VALUES (1000, 4, 152);
INSERT INTO `tb_role_resource` VALUES (1001, 4, 153);
INSERT INTO `tb_role_resource` VALUES (1002, 4, 154);
INSERT INTO `tb_role_resource` VALUES (1003, 4, 156);
INSERT INTO `tb_role_resource` VALUES (1004, 4, 157);
INSERT INTO `tb_role_resource` VALUES (1005, 4, 160);
INSERT INTO `tb_role_resource` VALUES (1006, 4, 161);
INSERT INTO `tb_role_resource` VALUES (1007, 4, 190);
INSERT INTO `tb_role_resource` VALUES (1008, 4, 194);
INSERT INTO `tb_role_resource` VALUES (1009, 4, 199);
INSERT INTO `tb_role_resource` VALUES (1010, 4, 200);
INSERT INTO `tb_role_resource` VALUES (1011, 4, 206);
INSERT INTO `tb_role_resource` VALUES (1012, 4, 209);
INSERT INTO `tb_role_resource` VALUES (1013, 4, 217);
INSERT INTO `tb_role_resource` VALUES (1014, 4, 214);
INSERT INTO `tb_role_resource` VALUES (1015, 4, 215);
INSERT INTO `tb_role_resource` VALUES (1016, 4, 1);
INSERT INTO `tb_role_resource` VALUES (1017, 4, 2);
INSERT INTO `tb_role_resource` VALUES (1018, 4, 3);
INSERT INTO `tb_role_resource` VALUES (1019, 4, 4);
INSERT INTO `tb_role_resource` VALUES (1020, 4, 5);
INSERT INTO `tb_role_resource` VALUES (1021, 4, 6);
INSERT INTO `tb_role_resource` VALUES (1022, 4, 7);
INSERT INTO `tb_role_resource` VALUES (1023, 4, 13);
INSERT INTO `tb_role_resource` VALUES (1024, 4, 15);
INSERT INTO `tb_role_resource` VALUES (1025, 4, 19);
INSERT INTO `tb_role_resource` VALUES (1026, 4, 20);
INSERT INTO `tb_role_resource` VALUES (1027, 2, 101);
INSERT INTO `tb_role_resource` VALUES (1028, 2, 106);
INSERT INTO `tb_role_resource` VALUES (1029, 2, 107);
INSERT INTO `tb_role_resource` VALUES (1030, 2, 108);
INSERT INTO `tb_role_resource` VALUES (1031, 2, 109);
INSERT INTO `tb_role_resource` VALUES (1032, 2, 110);
INSERT INTO `tb_role_resource` VALUES (1033, 2, 112);
INSERT INTO `tb_role_resource` VALUES (1034, 2, 113);
INSERT INTO `tb_role_resource` VALUES (1035, 2, 114);
INSERT INTO `tb_role_resource` VALUES (1036, 2, 118);
INSERT INTO `tb_role_resource` VALUES (1037, 2, 219);
INSERT INTO `tb_role_resource` VALUES (1038, 2, 220);
INSERT INTO `tb_role_resource` VALUES (1039, 2, 122);
INSERT INTO `tb_role_resource` VALUES (1040, 2, 123);
INSERT INTO `tb_role_resource` VALUES (1041, 2, 125);
INSERT INTO `tb_role_resource` VALUES (1042, 2, 126);
INSERT INTO `tb_role_resource` VALUES (1043, 2, 128);
INSERT INTO `tb_role_resource` VALUES (1044, 2, 130);
INSERT INTO `tb_role_resource` VALUES (1045, 2, 131);
INSERT INTO `tb_role_resource` VALUES (1046, 2, 133);
INSERT INTO `tb_role_resource` VALUES (1047, 2, 135);
INSERT INTO `tb_role_resource` VALUES (1048, 2, 136);
INSERT INTO `tb_role_resource` VALUES (1049, 2, 137);
INSERT INTO `tb_role_resource` VALUES (1050, 2, 138);
INSERT INTO `tb_role_resource` VALUES (1051, 2, 139);
INSERT INTO `tb_role_resource` VALUES (1052, 2, 140);
INSERT INTO `tb_role_resource` VALUES (1053, 2, 141);
INSERT INTO `tb_role_resource` VALUES (1054, 2, 142);
INSERT INTO `tb_role_resource` VALUES (1055, 2, 143);
INSERT INTO `tb_role_resource` VALUES (1056, 2, 144);
INSERT INTO `tb_role_resource` VALUES (1057, 2, 147);
INSERT INTO `tb_role_resource` VALUES (1058, 2, 148);
INSERT INTO `tb_role_resource` VALUES (1059, 2, 149);
INSERT INTO `tb_role_resource` VALUES (1060, 2, 150);
INSERT INTO `tb_role_resource` VALUES (1061, 2, 151);
INSERT INTO `tb_role_resource` VALUES (1062, 2, 152);
INSERT INTO `tb_role_resource` VALUES (1063, 2, 153);
INSERT INTO `tb_role_resource` VALUES (1064, 2, 154);
INSERT INTO `tb_role_resource` VALUES (1065, 2, 156);
INSERT INTO `tb_role_resource` VALUES (1066, 2, 157);
INSERT INTO `tb_role_resource` VALUES (1067, 2, 160);
INSERT INTO `tb_role_resource` VALUES (1068, 2, 161);
INSERT INTO `tb_role_resource` VALUES (1069, 2, 168);
INSERT INTO `tb_role_resource` VALUES (1070, 2, 169);
INSERT INTO `tb_role_resource` VALUES (1071, 2, 173);
INSERT INTO `tb_role_resource` VALUES (1072, 2, 174);
INSERT INTO `tb_role_resource` VALUES (1073, 2, 176);
INSERT INTO `tb_role_resource` VALUES (1074, 2, 190);
INSERT INTO `tb_role_resource` VALUES (1075, 2, 191);
INSERT INTO `tb_role_resource` VALUES (1076, 2, 192);
INSERT INTO `tb_role_resource` VALUES (1077, 2, 193);
INSERT INTO `tb_role_resource` VALUES (1078, 2, 194);
INSERT INTO `tb_role_resource` VALUES (1079, 2, 15);
INSERT INTO `tb_role_resource` VALUES (1080, 2, 199);
INSERT INTO `tb_role_resource` VALUES (1081, 2, 200);
INSERT INTO `tb_role_resource` VALUES (1082, 2, 201);
INSERT INTO `tb_role_resource` VALUES (1083, 2, 206);
INSERT INTO `tb_role_resource` VALUES (1084, 2, 209);
INSERT INTO `tb_role_resource` VALUES (1085, 2, 217);
INSERT INTO `tb_role_resource` VALUES (1086, 2, 214);
INSERT INTO `tb_role_resource` VALUES (1087, 2, 215);
INSERT INTO `tb_role_resource` VALUES (1088, 2, 1);
INSERT INTO `tb_role_resource` VALUES (1089, 2, 2);
INSERT INTO `tb_role_resource` VALUES (1090, 2, 3);
INSERT INTO `tb_role_resource` VALUES (1091, 2, 4);
INSERT INTO `tb_role_resource` VALUES (1092, 2, 5);
INSERT INTO `tb_role_resource` VALUES (1093, 2, 6);
INSERT INTO `tb_role_resource` VALUES (1094, 2, 7);
INSERT INTO `tb_role_resource` VALUES (1095, 2, 9);
INSERT INTO `tb_role_resource` VALUES (1096, 2, 10);
INSERT INTO `tb_role_resource` VALUES (1097, 2, 13);
INSERT INTO `tb_role_resource` VALUES (1098, 2, 19);
INSERT INTO `tb_role_resource` VALUES (1099, 2, 20);
INSERT INTO `tb_role_resource` VALUES (1100, 1, 221);
INSERT INTO `tb_role_resource` VALUES (1101, 2, 221);
INSERT INTO `tb_role_resource` VALUES (1102, 3, 221);
INSERT INTO `tb_role_resource` VALUES (1103, 4, 221);
INSERT INTO `tb_role_resource` VALUES (1104, 5, 221);

-- ----------------------------
-- Table structure for tb_system_config
-- ----------------------------
DROP TABLE IF EXISTS `tb_system_config`;
CREATE TABLE `tb_system_config`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `config_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '配置描述, 默认空串',
  `config_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '配置名',
  `config_value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '配置值',
  `deletable_flag` tinyint(1) NOT NULL DEFAULT 1 COMMENT '0不可删除, 1可删除, 默认1',
  `create_user` int(11) NOT NULL COMMENT '创建人',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_user` int(11) NULL DEFAULT NULL COMMENT '更新人, 默认null',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间, 默认null',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_system_config
-- ----------------------------
INSERT INTO `tb_system_config` VALUES (1, 2, '是否启用用户配置', 'enable_user_config', 'true', 0, 2, '2024-01-08 10:00:38', NULL, NULL);

-- ----------------------------
-- Table structure for tb_tag
-- ----------------------------
DROP TABLE IF EXISTS `tb_tag`;
CREATE TABLE `tb_tag`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `tag_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标签名称',
  `deleted_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未删除, 1已删除, 默认0',
  `create_user` int(11) NOT NULL COMMENT '创建人',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_user` int(11) NULL DEFAULT NULL COMMENT '更新人, 默认null',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间, 默认null',
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
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `intro` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '用户介绍, 默认空串',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户邮箱',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '用户头像, 默认空串',
  `gender` int(11) NOT NULL DEFAULT -1 COMMENT '用户性别, 默认-1',
  `website` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '用户网站, 默认空串',
  `nickname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户昵称',
  `modified_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未修改，1已修改，默认0',
  `create_user` int(11) NULL DEFAULT NULL COMMENT '创建人, 默认null',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_user` int(11) NULL DEFAULT NULL COMMENT '更新人, 默认null',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间, 默认null',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_user
-- ----------------------------
INSERT INTO `tb_user` VALUES (1, '', 'ks@qq.com', '', 1, '', 'ks', 0, 2, '2023-04-25 08:46:25', NULL, NULL);
INSERT INTO `tb_user` VALUES (2, '', 'root@qq.com', '', 1, '', 'root', 0, NULL, '2023-04-25 08:46:25', NULL, NULL);
INSERT INTO `tb_user` VALUES (3, '', 'admin@qq.com', '', 1, '', 'admin', 0, 2, '2023-04-25 08:46:25', NULL, NULL);
INSERT INTO `tb_user` VALUES (4, '', 'editor@qq.com', '', 1, '', 'editor', 0, 2, '2023-04-25 08:46:25', NULL, NULL);
INSERT INTO `tb_user` VALUES (5, '', 'author@qq.com', '', 1, '', 'author', 0, 2, '2023-04-25 08:46:25', NULL, NULL);
INSERT INTO `tb_user` VALUES (6, '', 'user@qq.com', '', 1, '', 'user', 0, 2, '2023-04-25 08:46:25', NULL, NULL);
INSERT INTO `tb_user` VALUES (7, '', 'test@qq.com', '', -1, '', 'test', 0, 2, '2023-04-25 08:46:25', NULL, NULL);
INSERT INTO `tb_user` VALUES (9, '', 'ling@qq.com', '', 2, '', 'ling', 0, 2, '2023-04-25 08:46:25', NULL, NULL);

-- ----------------------------
-- Table structure for tb_user_auth
-- ----------------------------
DROP TABLE IF EXISTS `tb_user_auth`;
CREATE TABLE `tb_user_auth`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `login_log_id` int(11) NOT NULL DEFAULT -1 COMMENT '登录日志id, 默认-1',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户密码',
  `login_method` int(11) NOT NULL DEFAULT 1 COMMENT '1邮箱, 2QQ, 3微信, 4手机号, 默认1',
  `locked_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未锁定, 1已锁定, 默认0',
  `deleted_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未删除, 1已删除, 默认0',
  `disabled_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未禁用, 1已禁用, 默认0',
  `assimilate_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未同步, 1已同步, 默认0(历史角色)',
  `assimilate_now_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未同步，1已同步，默认0(当前角色)',
  `create_user` int(11) NOT NULL COMMENT '创建人',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_user` int(11) NULL DEFAULT NULL COMMENT '更新人, 默认null',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间, 默认null',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_user_auth
-- ----------------------------
INSERT INTO `tb_user_auth` VALUES (1, 1, -1, 'ks@qq.com', '$2a$10$jCk/zLpw/bs6x22.J2VSEuOAGAw4YiXK48RvRyTh7E0V70OFmAxAC', 1, 0, 0, 0, 1, 1, 2, '2023-04-26 21:44:26', NULL, NULL);
INSERT INTO `tb_user_auth` VALUES (2, 2, 7, 'root@qq.com', '$2a$10$jCk/zLpw/bs6x22.J2VSEuOAGAw4YiXK48RvRyTh7E0V70OFmAxAC', 1, 0, 0, 0, 1, 1, 2, '2023-04-26 21:44:26', NULL, NULL);
INSERT INTO `tb_user_auth` VALUES (3, 3, -1, 'admin@qq.com', '$2a$10$jCk/zLpw/bs6x22.J2VSEuOAGAw4YiXK48RvRyTh7E0V70OFmAxAC', 1, 0, 0, 0, 1, 1, 2, '2023-04-26 21:44:26', NULL, NULL);
INSERT INTO `tb_user_auth` VALUES (4, 4, -1, 'editor@qq.com', '$2a$10$jCk/zLpw/bs6x22.J2VSEuOAGAw4YiXK48RvRyTh7E0V70OFmAxAC', 1, 0, 0, 0, 1, 1, 2, '2023-04-26 21:44:26', NULL, NULL);
INSERT INTO `tb_user_auth` VALUES (5, 5, -1, 'author@qq.com', '$2a$10$jCk/zLpw/bs6x22.J2VSEuOAGAw4YiXK48RvRyTh7E0V70OFmAxAC', 1, 0, 0, 0, 1, 1, 2, '2023-04-26 21:44:26', NULL, NULL);
INSERT INTO `tb_user_auth` VALUES (6, 6, -1, 'user@qq.com', '$2a$10$jCk/zLpw/bs6x22.J2VSEuOAGAw4YiXK48RvRyTh7E0V70OFmAxAC', 1, 0, 0, 0, 0, 0, 2, '2023-04-26 21:44:26', NULL, NULL);
INSERT INTO `tb_user_auth` VALUES (7, 7, -1, 'test@qq.com', '$2a$10$jCk/zLpw/bs6x22.J2VSEuOAGAw4YiXK48RvRyTh7E0V70OFmAxAC', 1, 0, 0, 0, 0, 0, 2, '2023-04-26 21:44:26', NULL, NULL);
INSERT INTO `tb_user_auth` VALUES (9, 0, -1, 'ling@qq.com', '$2a$10$jCk/zLpw/bs6x22.J2VSEuOAGAw4YiXK48RvRyTh7E0V70OFmAxAC', 1, 0, 0, 0, 1, 1, 2, '2024-03-31 14:46:03', NULL, NULL);

-- ----------------------------
-- Table structure for tb_user_config
-- ----------------------------
DROP TABLE IF EXISTS `tb_user_config`;
CREATE TABLE `tb_user_config`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `config_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '配置描述, 默认空串',
  `config_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '配置名',
  `config_value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '配置值, 默认空串',
  `deleted_flag` tinyint(1) NOT NULL DEFAULT 1 COMMENT '0未删除, 1已删除, 默认1',
  `create_user` int(11) NOT NULL COMMENT '创建人',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_user` int(11) NULL DEFAULT NULL COMMENT '更新人, 默认null',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间, 默认null',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_user_config
-- ----------------------------
INSERT INTO `tb_user_config` VALUES (1, 2, '首页横幅封面图', 'home_banner_cover', 'https://iksling.com/static/img/banner/home.jpg', 0, 2, '2023-05-12 18:37:22', NULL, NULL);
INSERT INTO `tb_user_config` VALUES (2, 2, '首页横幅标题', 'home_banner_title', '欢迎来到「有一个地方」', 0, 2, '2023-05-12 18:38:04', NULL, NULL);
INSERT INTO `tb_user_config` VALUES (3, 2, '首页联系QQ', 'home_contact_qq', '294513634', 0, 2, '2023-05-12 18:39:18', NULL, NULL);
INSERT INTO `tb_user_config` VALUES (4, 2, '首页Github链接', 'home_github', 'https://github.com/yygdf', 0, 2, '2023-05-12 18:39:53', NULL, NULL);
INSERT INTO `tb_user_config` VALUES (5, 2, '首页Gitee链接', 'home_gitee', 'https://gitee.com/yygdf', 0, 2, '2023-05-12 18:40:19', NULL, NULL);
INSERT INTO `tb_user_config` VALUES (6, 2, '首页公告', 'home_notice', '暂无公告~~~', 0, 2, '2023-05-12 18:40:44', NULL, NULL);
INSERT INTO `tb_user_config` VALUES (7, 2, '文章默认封面图', 'article_default_cover', 'https://iksling.com/static/img/banner/archive.jpg', 0, 2, '2023-05-12 18:41:26', NULL, NULL);
INSERT INTO `tb_user_config` VALUES (8, 2, '微信支付二维码', 'wx_pay_code', 'https://iksling.com/static/img/aliPay.png', 0, 2, '2023-05-12 18:42:11', NULL, NULL);
INSERT INTO `tb_user_config` VALUES (9, 2, '支付宝支付二维码', 'ali_pay_code', 'https://iksling.com/static/img/wxPay.png', 0, 2, '2023-05-12 18:43:27', NULL, NULL);
INSERT INTO `tb_user_config` VALUES (10, 2, '分类横幅封面图', 'category_banner_cover', 'https://iksling.com/static/img/banner/category.jpg', 0, 2, '2023-05-12 19:04:41', NULL, NULL);
INSERT INTO `tb_user_config` VALUES (11, 2, '标签横幅封面图', 'tag_banner_cover', 'https://iksling.com/static/img/banner/tag.jpg', 0, 2, '2023-05-12 19:05:33', NULL, NULL);
INSERT INTO `tb_user_config` VALUES (12, 2, '关于横幅封面图', 'about_banner_cover', 'https://iksling.com/static/img/banner/about.jpg', 0, 2, '2023-05-12 19:06:40', NULL, NULL);
INSERT INTO `tb_user_config` VALUES (13, 2, '归档横幅封面图', 'archive_banner_cover', 'https://iksling.com/static/img/banner/archive.jpg', 0, 2, '2023-05-12 19:07:20', NULL, NULL);
INSERT INTO `tb_user_config` VALUES (14, 2, '友链横幅封面图', 'link_banner_cover', 'https://iksling.com/static/img/banner/link.jpg', 0, 2, '2023-05-12 19:08:03', NULL, NULL);
INSERT INTO `tb_user_config` VALUES (16, 2, '留言横幅封面图', 'message_banner_cover', 'https://iksling.com/static/img/banner/message.jpg', 0, 2, '2023-05-12 19:12:25', NULL, NULL);
INSERT INTO `tb_user_config` VALUES (17, 2, '个人中心封面图', 'personal_banner_cover', 'https://iksling.com/static/img/banner/personal.jpg', 0, 2, '2023-05-12 18:37:22', NULL, NULL);

-- ----------------------------
-- Table structure for tb_user_role
-- ----------------------------
DROP TABLE IF EXISTS `tb_user_role`;
CREATE TABLE `tb_user_role`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `role_id` int(11) NOT NULL COMMENT '角色id',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_user_role
-- ----------------------------
INSERT INTO `tb_user_role` VALUES (1, 1, 0);
INSERT INTO `tb_user_role` VALUES (2, 1, 1);
INSERT INTO `tb_user_role` VALUES (3, 1, 2);
INSERT INTO `tb_user_role` VALUES (4, 2, 3);
INSERT INTO `tb_user_role` VALUES (5, 3, 4);
INSERT INTO `tb_user_role` VALUES (6, 4, 5);
INSERT INTO `tb_user_role` VALUES (7, 5, 6);
INSERT INTO `tb_user_role` VALUES (8, 6, 7);

SET FOREIGN_KEY_CHECKS = 1;
