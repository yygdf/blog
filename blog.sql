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

 Date: 19/09/2023 07:47:02
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tb_article
-- ----------------------------
DROP TABLE IF EXISTS `tb_article`;
CREATE TABLE `tb_article`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '文章id',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `category_id` int(11) NOT NULL DEFAULT -1 COMMENT '分类id，默认-1',
  `article_title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文章标题',
  `article_cover` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '文章封面',
  `article_content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文章内容',
  `top_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未置顶，1已置顶，默认0',
  `draft_flag` tinyint(1) NOT NULL DEFAULT 1 COMMENT '0不是草稿，1是草稿，默认1',
  `public_flag` tinyint(1) NOT NULL DEFAULT 1 COMMENT '0未公开，1已公开，默认1',
  `hidden_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未隐藏，1已隐藏，默认0',
  `recycle_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未回收，1已回收，默认0',
  `deleted_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未删除，1已删除，默认0',
  `commentable_flag` tinyint(1) NOT NULL DEFAULT 1 COMMENT '0不可评论，1可评论，默认1',
  `ip_source` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'ip来源，默认空串',
  `ip_address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'ip地址，默认空串',
  `create_user` int(11) NOT NULL COMMENT '创建人',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_user` int(11) NULL DEFAULT NULL COMMENT '更新人，默认null',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间，默认null',
  `publish_user` int(11) NULL DEFAULT NULL COMMENT '发表人，默认null',
  `publish_time` datetime(0) NULL DEFAULT NULL COMMENT '发表时间，默认null',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 33 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_article_tag
-- ----------------------------
DROP TABLE IF EXISTS `tb_article_tag`;
CREATE TABLE `tb_article_tag`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `tag_id` int(11) NOT NULL COMMENT '标签id',
  `article_id` int(11) NOT NULL COMMENT '文章id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 61 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_category
-- ----------------------------
DROP TABLE IF EXISTS `tb_category`;
CREATE TABLE `tb_category`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '分类id',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `category_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '分类名',
  `public_flag` tinyint(1) NOT NULL DEFAULT 1 COMMENT '0未公开，1已公开，默认1',
  `hidden_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未隐藏，1已隐藏，默认0',
  `create_user` int(11) NOT NULL COMMENT '创建人',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_user` int(11) NULL DEFAULT NULL COMMENT '更新人，默认null',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间，默认null',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 32 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_chat_record
-- ----------------------------
DROP TABLE IF EXISTS `tb_chat_record`;
CREATE TABLE `tb_chat_record`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '聊天记录id',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户头像',
  `nickname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户昵称',
  `chat_type` int(11) NOT NULL COMMENT '聊天类型',
  `chat_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '聊天内容',
  `recalled_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未撤回，1已撤回，默认0',
  `ip_source` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'ip来源，默认空串',
  `ip_address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'ip地址，默认空串',
  `create_user` int(11) NOT NULL COMMENT '创建人',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_comment
-- ----------------------------
DROP TABLE IF EXISTS `tb_comment`;
CREATE TABLE `tb_comment`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '评论id',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `reply_id` int(11) NOT NULL DEFAULT -1 COMMENT '回复用户id，默认-1',
  `article_id` int(11) NOT NULL DEFAULT -1 COMMENT '文章id，默认-1',
  `parent_id` int(11) NOT NULL DEFAULT -1 COMMENT '父评论id，默认-1',
  `comment_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '评论内容',
  `recycle_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未回收，1已回收，默认0',
  `deleted_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未删除，1已删除，默认0',
  `ip_source` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'ip来源，默认空串',
  `ip_address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'ip地址，默认空串',
  `create_user` int(11) NOT NULL COMMENT '创建人',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

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
-- Table structure for tb_exception_log
-- ----------------------------
DROP TABLE IF EXISTS `tb_exception_log`;
CREATE TABLE `tb_exception_log`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '操作日志id',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `opt_uri` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '操作路径',
  `opt_type` int(11) NOT NULL COMMENT '操作类型，1上传，2删除，3修改，4查询，5新增或修改',
  `opt_desc` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '操作描述，默认空串',
  `opt_module` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '操作模块',
  `opt_method` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '操作方法',
  `request_param` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '请求参数',
  `exception_message` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '异常简略信息',
  `exception_stack_trace` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '异常堆栈信息',
  `illegal_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0不是非法，1是非法，默认0',
  `ip_source` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'ip来源，默认空串',
  `ip_address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'ip地址，默认空串',
  `create_user` int(11) NOT NULL COMMENT '创建人',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_friend_link
-- ----------------------------
DROP TABLE IF EXISTS `tb_friend_link`;
CREATE TABLE `tb_friend_link`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '友链id',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `link_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '友链地址',
  `link_desc` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '友链描述',
  `link_logo` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '友链图标',
  `link_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '友链名称',
  `deleted_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未删除，1已删除，默认0',
  `create_user` int(11) NOT NULL COMMENT '创建人',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_user` int(11) NULL DEFAULT NULL COMMENT '更新人，默认null',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间，默认null',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 20 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_login_log
-- ----------------------------
DROP TABLE IF EXISTS `tb_login_log`;
CREATE TABLE `tb_login_log`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '日志id',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `login_time` datetime(0) NOT NULL COMMENT '登录时间',
  `login_device` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '登录设备，默认空串',
  `login_method` int(11) NOT NULL COMMENT '登录方式，1邮箱，2QQ，3微信，4手机号',
  `login_platform` tinyint(1) NOT NULL COMMENT '登录平台，0前台，1后台',
  `login_system` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '操作系统类型，默认空串',
  `login_browser` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '浏览器类型，默认空串',
  `ip_source` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'ip来源，默认空串',
  `ip_address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'ip地址，默认空串',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 368 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_menu
-- ----------------------------
DROP TABLE IF EXISTS `tb_menu`;
CREATE TABLE `tb_menu`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '菜单id',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `parent_id` int(11) NOT NULL DEFAULT -1 COMMENT '父菜单id，默认-1',
  `icon` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '菜单图标',
  `rank` int(11) NOT NULL DEFAULT 127 COMMENT '排序指标，默认127',
  `path` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '菜单路径',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '菜单名称',
  `component` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '菜单组件',
  `hide_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未隐藏，1已隐藏，默认0',
  `hidden_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未隐藏，1已隐藏，默认0',
  `disabled_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未禁用，1已禁用，默认0',
  `deletable_flag` tinyint(1) NOT NULL DEFAULT 1 COMMENT '0不可删除，1可删除，默认1',
  `create_user` int(11) NOT NULL COMMENT '创建人',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_user` int(11) NULL DEFAULT NULL COMMENT '更新人，默认null',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间，默认null',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 906 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_message
-- ----------------------------
DROP TABLE IF EXISTS `tb_message`;
CREATE TABLE `tb_message`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '留言id',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户头像',
  `nickname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户昵称',
  `message_speed` int(11) NOT NULL COMMENT '留言速度',
  `message_content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '留言内容',
  `deleted_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未删除，1已删除，默认0',
  `ip_source` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'ip来源，默认空串',
  `ip_address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'ip地址，默认空串',
  `create_user` int(11) NOT NULL COMMENT '创建人',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

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
  `dir_desc` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '目录描述，默认空串',
  `dir_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '目录名称',
  `dir_cover` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '目录封面，默认空串',
  `public_flag` tinyint(1) NOT NULL DEFAULT 1 COMMENT '0未公开，1已公开，默认1',
  `hidden_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未隐藏，1已隐藏，默认0',
  `deleted_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未删除，1已删除，默认0',
  `deletable_flag` tinyint(1) NOT NULL DEFAULT 1 COMMENT '0不可删除，1可删除，默认1',
  `create_user` int(11) NOT NULL COMMENT '创建人',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_user` int(11) NULL DEFAULT NULL COMMENT '更新人，默认null',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间，默认null',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_multi_file
-- ----------------------------
DROP TABLE IF EXISTS `tb_multi_file`;
CREATE TABLE `tb_multi_file`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '文件id',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `multi_dir_id` int(11) NOT NULL COMMENT '父目录id',
  `file_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件地址',
  `file_desc` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '文件描述，默认空串',
  `file_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件名称',
  `file_sub_dir` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件子目录',
  `hidden_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未隐藏，1已隐藏，默认0',
  `deleted_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未删除，1已删除，默认0',
  `ip_source` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'ip来源，默认空串',
  `ip_address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'ip地址，默认空串',
  `create_user` int(11) NOT NULL COMMENT '创建人',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_user` int(11) NULL DEFAULT NULL COMMENT '更新人，默认null',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间，默认null',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_operation_log
-- ----------------------------
DROP TABLE IF EXISTS `tb_operation_log`;
CREATE TABLE `tb_operation_log`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '操作日志id',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `opt_uri` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '操作路径',
  `opt_type` int(11) NOT NULL COMMENT '操作类型，1上传，2删除，3修改，4查询，5新增或修改',
  `opt_desc` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '操作描述，默认空串',
  `opt_module` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '操作模块',
  `opt_method` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '操作方法',
  `request_param` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '请求参数',
  `response_data` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '响应数据',
  `ip_source` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'ip来源，默认空串',
  `ip_address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'ip地址，默认空串',
  `create_user` int(11) NOT NULL COMMENT '创建人',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_qq_auth
-- ----------------------------
DROP TABLE IF EXISTS `tb_qq_auth`;
CREATE TABLE `tb_qq_auth`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '账号id',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `login_log_id` int(11) NOT NULL COMMENT '登录日志id',
  `openid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'QQ的openid',
  `access_token` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '访问QQ的token',
  `locked_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未锁定，1已锁定，默认0',
  `deleted_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未删除，1已删除，默认0',
  `disabled_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未禁用，1已禁用，默认0',
  `create_user` int(11) NOT NULL COMMENT '创建人',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_user` int(11) NULL DEFAULT NULL COMMENT '更新人，默认null',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间，默认null',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_resource
-- ----------------------------
DROP TABLE IF EXISTS `tb_resource`;
CREATE TABLE `tb_resource`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '资源id',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `parent_id` int(11) NOT NULL DEFAULT -1 COMMENT '父资源id，默认-1',
  `resource_uri` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '资源路径，默认空串',
  `resource_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '资源名称',
  `resource_request_method` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '请求方式，默认空串',
  `disabled_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未禁用，1已禁用，默认0',
  `deletable_flag` tinyint(1) NOT NULL DEFAULT 1 COMMENT '0不可删除，1可删除，默认1',
  `anonymous_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未匿名，1已匿名，默认0',
  `create_user` int(11) NOT NULL COMMENT '创建人',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_user` int(11) NULL DEFAULT NULL COMMENT '更新人，默认null',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间，默认null',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1335 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_role
-- ----------------------------
DROP TABLE IF EXISTS `tb_role`;
CREATE TABLE `tb_role`  (
  `id` int(255) NOT NULL AUTO_INCREMENT COMMENT '角色id',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `role_desc` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色描述',
  `role_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色名称',
  `role_weight` int(11) NOT NULL DEFAULT 1000 COMMENT '角色权重，默认1000',
  `disabled_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未禁用，1已禁用，默认0',
  `deletable_flag` tinyint(1) NOT NULL DEFAULT 1 COMMENT '0不可删除，1可删除，默认1',
  `create_user` int(11) NOT NULL COMMENT '创建人',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_user` int(11) NULL DEFAULT NULL COMMENT '更新人，默认null',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间，默认null',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `tb_role_menu`;
CREATE TABLE `tb_role_menu`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `role_id` int(11) NOT NULL COMMENT '角色id',
  `menu_id` int(11) NOT NULL COMMENT '菜单id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 518 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_role_resource
-- ----------------------------
DROP TABLE IF EXISTS `tb_role_resource`;
CREATE TABLE `tb_role_resource`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `role_id` int(11) NOT NULL COMMENT '角色id',
  `resource_id` int(11) NOT NULL COMMENT '资源id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2604 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_system_config
-- ----------------------------
DROP TABLE IF EXISTS `tb_system_config`;
CREATE TABLE `tb_system_config`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '配置id',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `config_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '配置描述，默认空串',
  `config_value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '配置值',
  `config_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '配置名',
  `deletable_flag` tinyint(1) NOT NULL DEFAULT 1 COMMENT '0不可删除，1可删除，默认1',
  `create_user` int(11) NOT NULL COMMENT '创建人',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_user` int(11) NULL DEFAULT NULL COMMENT '更新人，默认null',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间，默认null',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

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
  `update_user` int(11) NULL DEFAULT NULL COMMENT '更新人，默认null',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间，默认null',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_user
-- ----------------------------
DROP TABLE IF EXISTS `tb_user`;
CREATE TABLE `tb_user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `intro` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '用户介绍，默认空串',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户邮箱',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户头像',
  `website` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '用户网站，默认空串',
  `nickname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户昵称',
  `create_user` int(11) NOT NULL COMMENT '创建人',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_user` int(11) NULL DEFAULT NULL COMMENT '更新人，默认null',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间，默认null',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 100000003 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_user_auth
-- ----------------------------
DROP TABLE IF EXISTS `tb_user_auth`;
CREATE TABLE `tb_user_auth`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '账号id',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `login_log_id` int(11) NOT NULL COMMENT '登录日志id',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户密码',
  `login_method` int(11) NOT NULL COMMENT '1邮箱，2QQ，3微信，4手机号',
  `locked_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未锁定，1已锁定，默认0',
  `deleted_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未删除，1已删除，默认0',
  `disabled_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未禁用，1已禁用，默认0',
  `user_config_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未启用过，1已启用过，默认0',
  `create_user` int(11) NOT NULL COMMENT '创建人',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_user` int(11) NULL DEFAULT NULL COMMENT '更新人，默认null',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间，默认null',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 100000003 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_user_config
-- ----------------------------
DROP TABLE IF EXISTS `tb_user_config`;
CREATE TABLE `tb_user_config`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '配置id',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `config_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '配置描述，默认空串',
  `config_value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '配置值',
  `config_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '配置名',
  `deleted_flag` tinyint(1) NOT NULL DEFAULT 1 COMMENT '0未删除，1已删除，默认1',
  `deletable_flag` tinyint(1) NOT NULL DEFAULT 1 COMMENT '0不可删除，1可删除，默认1',
  `create_user` int(11) NOT NULL COMMENT '创建人',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_user` int(11) NULL DEFAULT NULL COMMENT '更新人，默认null',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间，默认null',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 47 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_user_role
-- ----------------------------
DROP TABLE IF EXISTS `tb_user_role`;
CREATE TABLE `tb_user_role`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `role_id` int(11) NOT NULL COMMENT '角色id',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 100000028 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
