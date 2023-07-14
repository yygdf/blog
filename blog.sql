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

 Date: 14/07/2023 17:56:35
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
  `publish_user` int(11) NULL DEFAULT NULL COMMENT '发表人',
  `publish_time` datetime(0) NULL DEFAULT NULL COMMENT '发表时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_article
-- ----------------------------
INSERT INTO `tb_article` VALUES (1, 2, 1, '2023-07-14', 'http://192.168.203.130/static/img/article/2/default/defaultCover.jpg', '1231231232', 0, 0, 1, 0, 0, 0, 1, '', '127.0.0.1', 2, '2023-07-14 10:57:47', 2, '2023-07-14 11:40:44', 2, '2023-07-14 11:40:44');
INSERT INTO `tb_article` VALUES (2, 2, 1, '123123', 'http://192.168.203.130/static/img/article/2/default/defaultCover.jpg', '123213\n\n![tiaozhan2.png](http://192.168.203.130/static/img/article/2/2/1679785113212174338.png)', 0, 1, 1, 0, 0, 0, 1, '', '127.0.0.1', 2, '2023-07-14 11:40:04', NULL, NULL, 2, '2023-07-14 11:40:04');
INSERT INTO `tb_article` VALUES (3, 5, 6, '2023-07-14123123', 'http://192.168.203.130/static/img/article/5/default/defaultCover.jpg', '12312323![beijing2.jpg](http://192.168.203.130/static/img/article/5/3/1679744888247226369.jpg)\n\n\n\n![tiaozhan.png](http://192.168.203.130/static/img/article/5/3/1679756298993807361.png)\n\n![tiaozhan.png](http://192.168.203.130/static/img/article/5/3/1679756298993807361.png)', 1, 0, 0, 1, 0, 0, 0, '', '127.0.0.1', 5, '2023-07-14 14:43:46', 2, '2023-07-14 15:39:17', 5, '2023-07-14 14:50:31');
INSERT INTO `tb_article` VALUES (4, 5, 5, '2023-07-14gggg', 'http://192.168.203.130/static/img/article/5/default/defaultCover.jpg', '123123213![beijing3.jpg](1)\n\n\n![beijing6.jpg](1)\n\n\n![beijing6.jpg](1)\n\n\n\n![beijing6.jpg](1)\n\n\n', 1, 0, 1, 1, 0, 0, 1, '', '127.0.0.1', 5, '2023-07-14 14:53:23', 2, '2023-07-14 15:39:30', 2, '2023-07-14 15:14:14');
INSERT INTO `tb_article` VALUES (5, 5, 5, '2023-07-1412312', 'http://192.168.203.130/static/img/article/5/5/1679784094264737794.png', '1231232![beijing4.jpg](http://192.168.203.130/static/img/article/5/5/1679746860593516546.jpg)![beijing4.jpg](http://192.168.203.130/static/img/article/5/5/1679746860593516546.jpg)\n\n\n![tiaozhan.png](http://192.168.203.130/static/img/article/5/5/1679782793879490561.png)![wushuangtiaozhan.png](http://192.168.203.130/static/img/article/5/5/1679783897514131458.png)\n\n\n![beijing2.jpg](http://192.168.203.130/static/img/article/5/5/1679784040380514306.jpg)', 1, 0, 1, 0, 0, 0, 1, '', '127.0.0.1', 5, '2023-07-14 14:54:11', 2, '2023-07-14 17:25:13', 5, '2023-07-14 14:57:33');
INSERT INTO `tb_article` VALUES (6, 2, -1, '2023-07-14', '', '![beijing1.jpeg](http://192.168.203.130/static/img/article/2/6/1679782574081183745.jpeg)', 0, 1, 1, 0, 0, 0, 1, '', '127.0.0.1', 2, '2023-07-14 17:19:09', NULL, NULL, NULL, NULL);
INSERT INTO `tb_article` VALUES (7, 5, -1, '哈哈哈', '', '哈哈哈![beijing2.jpg](1)', 0, 1, 1, 0, 0, 0, 1, '', '127.0.0.1', 5, '2023-07-14 17:26:01', NULL, NULL, NULL, NULL);
INSERT INTO `tb_article` VALUES (8, 2, -1, '2023-07-14', '', '\n\n![beijing2.jpg](1)', 0, 1, 1, 0, 0, 0, 1, '', '127.0.0.1', 2, '2023-07-14 17:28:26', NULL, NULL, NULL, NULL);
INSERT INTO `tb_article` VALUES (9, 2, -1, '2023-07-14', '', '![beijing3.jpg](1)', 0, 1, 1, 0, 0, 0, 1, '', '127.0.0.1', 2, '2023-07-14 17:29:00', NULL, NULL, NULL, NULL);
INSERT INTO `tb_article` VALUES (10, 2, -1, '2023-07-14', '', '![beijing2.jpg](1)', 0, 1, 1, 0, 0, 0, 1, '', '127.0.0.1', 2, '2023-07-14 17:29:28', NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for tb_article_tag
-- ----------------------------
DROP TABLE IF EXISTS `tb_article_tag`;
CREATE TABLE `tb_article_tag`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `tag_id` int(11) NOT NULL COMMENT '标签id',
  `article_id` int(11) NOT NULL COMMENT '文章id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 20 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_article_tag
-- ----------------------------
INSERT INTO `tb_article_tag` VALUES (15, 5, 3);
INSERT INTO `tb_article_tag` VALUES (19, 5, 5);

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
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_category
-- ----------------------------
INSERT INTO `tb_category` VALUES (1, 2, '测试分类1', 1, 1, 2, '2023-07-14 11:06:24', NULL, NULL);
INSERT INTO `tb_category` VALUES (2, 2, '测试分类123', 1, 1, 2, '2023-07-14 11:11:25', 2, '2023-07-14 14:02:08');
INSERT INTO `tb_category` VALUES (3, 2, '测试分类3', 1, 0, 2, '2023-07-14 14:01:28', NULL, NULL);
INSERT INTO `tb_category` VALUES (5, 5, '测试分类1', 1, 0, 5, '2023-07-14 14:37:53', NULL, NULL);
INSERT INTO `tb_category` VALUES (6, 5, '测试分类author', 1, 0, 5, '2023-07-14 14:38:06', NULL, NULL);
INSERT INTO `tb_category` VALUES (11, 2, '123', 1, 0, 2, '2023-07-14 17:10:09', NULL, NULL);

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
) ENGINE = InnoDB AUTO_INCREMENT = 29 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_login_log
-- ----------------------------
INSERT INTO `tb_login_log` VALUES (1, 2, 1, '2023-07-14 10:55:24', 'Computer', 'Windows 10', 'Firefox 11', '', '127.0.0.1');
INSERT INTO `tb_login_log` VALUES (2, 2, 1, '2023-07-14 10:57:02', 'Computer', 'Windows 10', 'Firefox 11', '', '127.0.0.1');
INSERT INTO `tb_login_log` VALUES (3, 2, 1, '2023-07-14 11:03:37', 'Computer', 'Windows 10', 'Firefox 11', '', '127.0.0.1');
INSERT INTO `tb_login_log` VALUES (4, 2, 1, '2023-07-14 11:09:38', 'Computer', 'Windows 10', 'Firefox 11', '', '127.0.0.1');
INSERT INTO `tb_login_log` VALUES (5, 2, 1, '2023-07-14 11:11:09', 'Computer', 'Windows 10', 'Firefox 11', '', '127.0.0.1');
INSERT INTO `tb_login_log` VALUES (6, 2, 1, '2023-07-14 11:13:18', 'Computer', 'Windows 10', 'Firefox 11', '', '127.0.0.1');
INSERT INTO `tb_login_log` VALUES (7, 2, 1, '2023-07-14 11:34:13', 'Computer', 'Windows 10', 'Firefox 11', '', '127.0.0.1');
INSERT INTO `tb_login_log` VALUES (8, 2, 1, '2023-07-14 13:59:19', 'Computer', 'Windows 10', 'Firefox 11', '', '127.0.0.1');
INSERT INTO `tb_login_log` VALUES (9, 6, 1, '2023-07-14 14:17:53', 'Computer', 'Windows 10', 'Firefox 11', '', '127.0.0.1');
INSERT INTO `tb_login_log` VALUES (10, 6, 1, '2023-07-14 14:18:51', 'Computer', 'Windows 10', 'Firefox 11', '', '127.0.0.1');
INSERT INTO `tb_login_log` VALUES (11, 5, 1, '2023-07-14 14:19:54', 'Computer', 'Windows 10', 'Firefox 11', '', '127.0.0.1');
INSERT INTO `tb_login_log` VALUES (12, 5, 1, '2023-07-14 14:20:23', 'Computer', 'Windows 10', 'Firefox 11', '', '127.0.0.1');
INSERT INTO `tb_login_log` VALUES (13, 5, 1, '2023-07-14 14:22:40', 'Computer', 'Windows 10', 'Firefox 11', '', '127.0.0.1');
INSERT INTO `tb_login_log` VALUES (14, 5, 1, '2023-07-14 14:25:02', 'Computer', 'Windows 10', 'Firefox 11', '', '127.0.0.1');
INSERT INTO `tb_login_log` VALUES (15, 5, 1, '2023-07-14 14:31:31', 'Computer', 'Windows 10', 'Firefox 11', '', '127.0.0.1');
INSERT INTO `tb_login_log` VALUES (16, 5, 1, '2023-07-14 14:34:02', 'Computer', 'Windows 10', 'Firefox 11', '', '127.0.0.1');
INSERT INTO `tb_login_log` VALUES (17, 5, 1, '2023-07-14 14:54:03', 'Computer', 'Windows 10', 'Firefox 11', '', '127.0.0.1');
INSERT INTO `tb_login_log` VALUES (18, 5, 1, '2023-07-14 14:56:59', 'Computer', 'Windows 10', 'Firefox 11', '', '127.0.0.1');
INSERT INTO `tb_login_log` VALUES (19, 2, 1, '2023-07-14 14:57:54', 'Computer', 'Windows 10', 'Firefox 11', '', '127.0.0.1');
INSERT INTO `tb_login_log` VALUES (20, 2, 1, '2023-07-14 15:05:29', 'Computer', 'Windows 10', 'Firefox 11', '', '127.0.0.1');
INSERT INTO `tb_login_log` VALUES (21, 2, 1, '2023-07-14 15:13:21', 'Computer', 'Windows 10', 'Firefox 11', '', '127.0.0.1');
INSERT INTO `tb_login_log` VALUES (22, 2, 1, '2023-07-14 15:26:43', 'Computer', 'Windows 10', 'Firefox 11', '', '127.0.0.1');
INSERT INTO `tb_login_log` VALUES (23, 2, 1, '2023-07-14 15:34:19', 'Computer', 'Windows 10', 'Firefox 11', '', '127.0.0.1');
INSERT INTO `tb_login_log` VALUES (24, 2, 1, '2023-07-14 15:49:58', 'Computer', 'Windows 10', 'Firefox 11', '', '127.0.0.1');
INSERT INTO `tb_login_log` VALUES (25, 2, 1, '2023-07-14 17:08:55', 'Computer', 'Windows 10', 'Firefox 11', '', '127.0.0.1');
INSERT INTO `tb_login_log` VALUES (26, 2, 1, '2023-07-14 17:22:50', 'Computer', 'Windows 10', 'Firefox 11', '', '127.0.0.1');
INSERT INTO `tb_login_log` VALUES (27, 5, 1, '2023-07-14 17:25:42', 'Computer', 'Windows 10', 'Firefox 11', '', '127.0.0.1');
INSERT INTO `tb_login_log` VALUES (28, 2, 1, '2023-07-14 17:28:13', 'Computer', 'Windows 10', 'Firefox 11', '', '127.0.0.1');

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
INSERT INTO `tb_menu` VALUES (203, 2, 2, 'el-icon-files', 23, '/category', '分类管理', '/article/Category.vue', 0, 0, 0, 0, 2, '2023-05-12 14:09:01', NULL, NULL);
INSERT INTO `tb_menu` VALUES (204, 2, 2, 'el-icon-collection-tag', 24, '/tag', '标签管理', '/article/Tag.vue', 0, 0, 0, 0, 2, '2023-05-12 14:10:41', NULL, NULL);
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
  `file_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件名称',
  `file_sub_dir` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件子目录',
  `hidden_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未隐藏，1已隐藏',
  `deleted_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未删除，1已删除',
  `ip_source` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'ip来源',
  `ip_address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'ip地址',
  `create_user` int(11) NOT NULL COMMENT '创建人',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_user` int(11) NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_multi_file
-- ----------------------------
INSERT INTO `tb_multi_file` VALUES (1, 5, 3, 'http://192.168.203.130/static/img/article/5/3/1679744888247226369.jpg', '用户[author@qq.com], 文章id[3]中的插图', 'beijing2.jpg', '5/3/', 0, 0, '', '127.0.0.1', 5, '2023-07-14 14:49:25', NULL, NULL);
INSERT INTO `tb_multi_file` VALUES (2, 5, 3, 'http://192.168.203.130/static/img/article/5/5/1679746860593516546.jpg', '用户[author@qq.com], 文章id[5]中的插图', 'beijing4.jpg', '5/5/', 0, 0, '', '127.0.0.1', 5, '2023-07-14 14:57:15', NULL, NULL);
INSERT INTO `tb_multi_file` VALUES (3, 5, 3, 'http://192.168.203.130/static/img/article/5/3/1679756298993807361.png', '用户[root@qq.com], 文章id[3]中的插图', 'tiaozhan.png', '5/3/', 0, 0, '', '127.0.0.1', 2, '2023-07-14 15:34:45', NULL, NULL);
INSERT INTO `tb_multi_file` VALUES (4, 2, 3, 'http://192.168.203.130/static/img/article/2/6/1679782574081183745.jpeg', '用户[root@qq.com], 文章id[6]中的插图', 'beijing1.jpeg', '2/6/', 0, 0, '', '127.0.0.1', 2, '2023-07-14 17:19:10', NULL, NULL);
INSERT INTO `tb_multi_file` VALUES (5, 5, 3, 'http://192.168.203.130/static/img/article/5/5/1679782793879490561.png', '用户[root@qq.com], 文章id[5]中的插图', 'tiaozhan.png', '5/5/', 0, 0, '', '127.0.0.1', 2, '2023-07-14 17:20:02', NULL, NULL);
INSERT INTO `tb_multi_file` VALUES (6, 5, 3, 'http://192.168.203.130/static/img/article/5/5/1679782842894127106.png', '用户[root@qq.com], 文章id[5]中的插图', 'wushuang.png', '5/5/', 0, 1, '', '127.0.0.1', 2, '2023-07-14 17:20:13', NULL, NULL);
INSERT INTO `tb_multi_file` VALUES (7, 5, 3, 'http://192.168.203.130/static/img/article/5/5/1679783897514131458.png', '用户[root@qq.com], 文章id[5]中的插图', 'tiaozhan.png', '5/5/', 0, 1, '', '127.0.0.1', 2, '2023-07-14 17:24:25', NULL, NULL);
INSERT INTO `tb_multi_file` VALUES (8, 5, 3, 'http://192.168.203.130/static/img/article/5/5/1679784040380514306.jpg', '用户[root@qq.com], 文章id[5]中的插图', 'beijing2.jpg', '5/5/', 0, 0, '', '127.0.0.1', 2, '2023-07-14 17:24:59', NULL, NULL);
INSERT INTO `tb_multi_file` VALUES (9, 5, 3, 'http://192.168.203.130/static/img/article/5/5/1679784094264737794.png', '用户[root@qq.com], 文章id[5]中的插图', 'tiaozhan2.png', '5/5/', 0, 0, '', '127.0.0.1', 2, '2023-07-14 17:25:12', NULL, NULL);
INSERT INTO `tb_multi_file` VALUES (10, 2, 3, 'http://192.168.203.130/static/img/article/2/2/1679785113212174338.png', '用户[root@qq.com], 文章id[2]中的插图', 'tiaozhan2.png', '2/2/', 0, 0, '', '127.0.0.1', 2, '2023-07-14 17:29:15', NULL, NULL);

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
) ENGINE = InnoDB AUTO_INCREMENT = 906 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_resource
-- ----------------------------
INSERT INTO `tb_resource` VALUES (1, 2, -1, '', '菜单模块', '', 0, 0, 0, 2, '2023-04-25 23:27:55', NULL, NULL);
INSERT INTO `tb_resource` VALUES (2, 2, -1, '', '首页模块', '', 0, 0, 0, 2, '2023-05-11 10:42:09', NULL, NULL);
INSERT INTO `tb_resource` VALUES (3, 2, -1, '', '文章模块', '', 0, 0, 0, 2, '2023-07-08 09:08:55', NULL, NULL);
INSERT INTO `tb_resource` VALUES (4, 2, -1, '', '分类模块', '', 0, 0, 0, 2, '2023-07-11 10:24:40', NULL, NULL);
INSERT INTO `tb_resource` VALUES (5, 2, -1, '', '标签模块', '', 0, 0, 0, 2, '2023-07-12 10:48:21', NULL, NULL);
INSERT INTO `tb_resource` VALUES (6, 2, -1, '', '用户模块', '', 0, 0, 0, 2, '2023-07-14 13:56:54', NULL, NULL);
INSERT INTO `tb_resource` VALUES (7, 2, -1, '', '文件模块', '', 0, 0, 0, 2, '2023-07-14 16:20:03', NULL, NULL);
INSERT INTO `tb_resource` VALUES (9, 2, -1, '', '其他模块', '', 0, 0, 0, 2, '2023-07-02 11:16:14', NULL, NULL);
INSERT INTO `tb_resource` VALUES (101, 2, 1, '/back/user/menus', '查看用户菜单', 'GET', 0, 0, 0, 2, '2023-04-26 21:33:02', NULL, NULL);
INSERT INTO `tb_resource` VALUES (201, 2, 2, '/back', '查看后台首页信息', 'GET', 0, 0, 0, 2, '2023-04-26 21:34:33', NULL, NULL);
INSERT INTO `tb_resource` VALUES (301, 2, 3, '/back/article/*', '根据文章id查找文章', 'GET', 0, 0, 0, 2, '2023-07-08 09:10:16', NULL, NULL);
INSERT INTO `tb_resource` VALUES (302, 2, 3, '/back/article/options', '查看文章选项', 'GET', 0, 0, 0, 2, '2023-07-08 09:10:53', NULL, NULL);
INSERT INTO `tb_resource` VALUES (303, 2, 3, '/back/article', '添加或修改文章', 'POST', 0, 0, 0, 2, '2023-07-08 09:11:36', NULL, NULL);
INSERT INTO `tb_resource` VALUES (305, 2, 3, '/back/articles', '查看后台文章列表', 'GET', 0, 0, 0, 2, '2023-07-08 14:46:19', NULL, NULL);
INSERT INTO `tb_resource` VALUES (306, 2, 3, '/back/articles', '批量更新垃圾文章', 'PUT', 0, 0, 0, 2, '2023-07-08 18:24:10', NULL, NULL);
INSERT INTO `tb_resource` VALUES (307, 2, 3, '/back/articles', '批量删除文章', 'DELETE', 0, 0, 0, 2, '2023-07-08 18:51:02', NULL, NULL);
INSERT INTO `tb_resource` VALUES (308, 2, 3, '/back/article/status', '修改文章状态', 'PUT', 0, 0, 0, 2, '2023-07-10 15:02:03', NULL, NULL);
INSERT INTO `tb_resource` VALUES (401, 2, 4, '/back/categories', '查看后台分类列表', 'GET', 0, 0, 0, 2, '2023-07-11 10:25:18', NULL, NULL);
INSERT INTO `tb_resource` VALUES (402, 2, 4, '/back/category/status', '修改分类状态', 'PUT', 0, 0, 0, 2, '2023-07-12 07:03:22', NULL, NULL);
INSERT INTO `tb_resource` VALUES (403, 2, 4, '/back/categories', '批量删除分类', 'DELETE', 0, 0, 0, 2, '2023-07-12 07:04:01', NULL, NULL);
INSERT INTO `tb_resource` VALUES (404, 2, 4, '/back/category', '添加或修改分类', 'POST', 0, 0, 0, 2, '2023-07-12 07:04:38', NULL, NULL);
INSERT INTO `tb_resource` VALUES (501, 2, 5, '/back/tags', '查看后台标签列表', 'GET', 0, 0, 0, 2, '2023-07-12 10:48:53', NULL, NULL);
INSERT INTO `tb_resource` VALUES (502, 2, 5, '/back/tags', '批量删除标签', 'DELETE', 0, 0, 0, 2, '2023-07-12 10:49:26', NULL, NULL);
INSERT INTO `tb_resource` VALUES (503, 2, 5, '/back/tag', '添加或修改标签', 'POST', 0, 0, 0, 2, '2023-07-12 10:50:07', NULL, NULL);
INSERT INTO `tb_resource` VALUES (601, 2, 6, '/back/user/username', '查看所有的用户名', 'GET', 0, 0, 0, 2, '2023-07-14 13:57:41', NULL, NULL);
INSERT INTO `tb_resource` VALUES (701, 2, 3, '/back/article/image', '上传文章图片', 'POST', 0, 0, 0, 2, '2023-07-08 09:12:09', NULL, NULL);
INSERT INTO `tb_resource` VALUES (702, 2, 3, '/back/article/image', '删除文章图片', 'DELETE', 0, 0, 0, 2, '2023-07-09 15:01:11', NULL, NULL);
INSERT INTO `tb_resource` VALUES (901, 2, 9, '/swagger-ui.html', 'SwaggerUI', 'GET', 0, 0, 1, 2, '2023-07-02 11:17:04', NULL, NULL);
INSERT INTO `tb_resource` VALUES (902, 2, 9, '/webjars/**', 'SwaggerUI', 'GET', 0, 0, 1, 2, '2023-07-12 17:18:57', NULL, NULL);
INSERT INTO `tb_resource` VALUES (903, 2, 9, '/login', '登录', 'POST', 0, 0, 1, 2, '2023-07-12 17:20:53', NULL, NULL);
INSERT INTO `tb_resource` VALUES (904, 2, 9, '/v2/**', 'SwaggerUI', 'GET', 0, 0, 1, 2, '2023-07-14 10:53:32', NULL, NULL);
INSERT INTO `tb_resource` VALUES (905, 2, 9, '/swagger-resources/**', 'SwaggerUI', 'GET', 0, 0, 1, 2, '2023-07-14 10:53:59', NULL, NULL);

-- ----------------------------
-- Table structure for tb_role
-- ----------------------------
DROP TABLE IF EXISTS `tb_role`;
CREATE TABLE `tb_role`  (
  `id` int(255) NOT NULL AUTO_INCREMENT COMMENT '角色id',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `role_desc` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '角色描述',
  `role_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色名称',
  `role_weight` int(11) NOT NULL DEFAULT 500 COMMENT '角色权重',
  `disabled_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未禁用，1已禁用',
  `deletable_flag` tinyint(1) NOT NULL DEFAULT 1 COMMENT '0不可删除，1可删除',
  `create_user` int(11) NOT NULL COMMENT '创建人',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_user` int(11) NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_role
-- ----------------------------
INSERT INTO `tb_role` VALUES (1, 2, 'root', 'root', 100, 0, 0, 0, '2023-04-25 08:47:06', NULL, NULL);
INSERT INTO `tb_role` VALUES (2, 2, '管理员', 'admin', 200, 0, 0, 0, '2023-04-25 19:42:22', NULL, NULL);
INSERT INTO `tb_role` VALUES (3, 2, '编辑', 'editor', 300, 0, 0, 0, '2023-04-25 19:44:08', NULL, NULL);
INSERT INTO `tb_role` VALUES (4, 2, '作者', 'author', 400, 0, 0, 0, '2023-04-26 21:35:54', NULL, NULL);
INSERT INTO `tb_role` VALUES (5, 2, '用户', 'user', 500, 0, 0, 0, '2023-04-26 21:36:11', NULL, NULL);
INSERT INTO `tb_role` VALUES (6, 2, 'test', 'test', 1000, 0, 0, 0, '2023-04-26 21:36:30', NULL, NULL);

-- ----------------------------
-- Table structure for tb_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `tb_role_menu`;
CREATE TABLE `tb_role_menu`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `role_id` int(11) NOT NULL COMMENT '角色id',
  `menu_id` int(11) NOT NULL COMMENT '菜单id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 42 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

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
INSERT INTO `tb_role_menu` VALUES (31, 4, 1);
INSERT INTO `tb_role_menu` VALUES (32, 4, 2);
INSERT INTO `tb_role_menu` VALUES (33, 4, 3);
INSERT INTO `tb_role_menu` VALUES (34, 4, 9);
INSERT INTO `tb_role_menu` VALUES (35, 4, 201);
INSERT INTO `tb_role_menu` VALUES (36, 4, 202);
INSERT INTO `tb_role_menu` VALUES (37, 4, 203);
INSERT INTO `tb_role_menu` VALUES (38, 4, 204);
INSERT INTO `tb_role_menu` VALUES (39, 4, 205);
INSERT INTO `tb_role_menu` VALUES (40, 4, 301);
INSERT INTO `tb_role_menu` VALUES (41, 4, 302);

-- ----------------------------
-- Table structure for tb_role_resource
-- ----------------------------
DROP TABLE IF EXISTS `tb_role_resource`;
CREATE TABLE `tb_role_resource`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `role_id` int(11) NOT NULL COMMENT '角色id',
  `resource_id` int(11) NOT NULL COMMENT '资源id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 59 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_role_resource
-- ----------------------------
INSERT INTO `tb_role_resource` VALUES (1, 1, 1);
INSERT INTO `tb_role_resource` VALUES (2, 1, 2);
INSERT INTO `tb_role_resource` VALUES (3, 1, 3);
INSERT INTO `tb_role_resource` VALUES (4, 1, 9);
INSERT INTO `tb_role_resource` VALUES (5, 1, 101);
INSERT INTO `tb_role_resource` VALUES (6, 1, 201);
INSERT INTO `tb_role_resource` VALUES (7, 1, 301);
INSERT INTO `tb_role_resource` VALUES (8, 1, 302);
INSERT INTO `tb_role_resource` VALUES (9, 1, 303);
INSERT INTO `tb_role_resource` VALUES (10, 1, 701);
INSERT INTO `tb_role_resource` VALUES (11, 1, 305);
INSERT INTO `tb_role_resource` VALUES (12, 1, 306);
INSERT INTO `tb_role_resource` VALUES (13, 1, 307);
INSERT INTO `tb_role_resource` VALUES (14, 1, 308);
INSERT INTO `tb_role_resource` VALUES (15, 1, 702);
INSERT INTO `tb_role_resource` VALUES (16, 1, 310);
INSERT INTO `tb_role_resource` VALUES (17, 1, 311);
INSERT INTO `tb_role_resource` VALUES (18, 1, 312);
INSERT INTO `tb_role_resource` VALUES (19, 1, 901);
INSERT INTO `tb_role_resource` VALUES (20, 1, 4);
INSERT INTO `tb_role_resource` VALUES (21, 1, 401);
INSERT INTO `tb_role_resource` VALUES (22, 1, 402);
INSERT INTO `tb_role_resource` VALUES (23, 1, 403);
INSERT INTO `tb_role_resource` VALUES (24, 1, 404);
INSERT INTO `tb_role_resource` VALUES (25, 1, 5);
INSERT INTO `tb_role_resource` VALUES (26, 1, 501);
INSERT INTO `tb_role_resource` VALUES (27, 1, 502);
INSERT INTO `tb_role_resource` VALUES (28, 1, 503);
INSERT INTO `tb_role_resource` VALUES (29, 1, 902);
INSERT INTO `tb_role_resource` VALUES (30, 1, 903);
INSERT INTO `tb_role_resource` VALUES (31, 1, 904);
INSERT INTO `tb_role_resource` VALUES (32, 1, 905);
INSERT INTO `tb_role_resource` VALUES (33, 1, 6);
INSERT INTO `tb_role_resource` VALUES (34, 1, 601);
INSERT INTO `tb_role_resource` VALUES (35, 4, 1);
INSERT INTO `tb_role_resource` VALUES (36, 4, 2);
INSERT INTO `tb_role_resource` VALUES (37, 4, 3);
INSERT INTO `tb_role_resource` VALUES (38, 4, 4);
INSERT INTO `tb_role_resource` VALUES (39, 4, 5);
INSERT INTO `tb_role_resource` VALUES (40, 4, 9);
INSERT INTO `tb_role_resource` VALUES (41, 4, 101);
INSERT INTO `tb_role_resource` VALUES (42, 4, 201);
INSERT INTO `tb_role_resource` VALUES (43, 4, 301);
INSERT INTO `tb_role_resource` VALUES (44, 4, 302);
INSERT INTO `tb_role_resource` VALUES (45, 4, 303);
INSERT INTO `tb_role_resource` VALUES (46, 4, 701);
INSERT INTO `tb_role_resource` VALUES (47, 4, 305);
INSERT INTO `tb_role_resource` VALUES (48, 4, 306);
INSERT INTO `tb_role_resource` VALUES (49, 4, 307);
INSERT INTO `tb_role_resource` VALUES (50, 4, 308);
INSERT INTO `tb_role_resource` VALUES (51, 4, 702);
INSERT INTO `tb_role_resource` VALUES (52, 4, 401);
INSERT INTO `tb_role_resource` VALUES (53, 4, 402);
INSERT INTO `tb_role_resource` VALUES (54, 4, 403);
INSERT INTO `tb_role_resource` VALUES (55, 4, 404);
INSERT INTO `tb_role_resource` VALUES (56, 4, 501);
INSERT INTO `tb_role_resource` VALUES (57, 4, 502);
INSERT INTO `tb_role_resource` VALUES (58, 4, 503);

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
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_tag
-- ----------------------------
INSERT INTO `tb_tag` VALUES (1, 2, '测试标签1', 2, '2023-07-14 14:02:27', NULL, NULL);
INSERT INTO `tb_tag` VALUES (2, 2, '测试标签123', 2, '2023-07-14 14:02:36', 2, '2023-07-14 14:03:06');
INSERT INTO `tb_tag` VALUES (3, 2, '测试标签3', 2, '2023-07-14 14:03:00', NULL, NULL);
INSERT INTO `tb_tag` VALUES (5, 5, '测试标签author', 5, '2023-07-14 14:39:00', NULL, NULL);
INSERT INTO `tb_tag` VALUES (10, 2, '123', 2, '2023-07-14 17:09:15', NULL, NULL);

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
INSERT INTO `tb_user` VALUES (0, '', '', 'https://ksling.cn/static/img/avatar/default/defaultAvatar.jpg', '', 'ling', 2, '2023-04-25 08:46:25', NULL, NULL);
INSERT INTO `tb_user` VALUES (1, '', '', 'https://ksling.cn/static/img/avatar/default/defaultAvatar.jpg', '', 'ks', 2, '2023-04-25 08:46:25', NULL, NULL);
INSERT INTO `tb_user` VALUES (2, '', '', 'https://ksling.cn/static/img/avatar/default/defaultAvatar.jpg', '', 'root', 2, '2023-04-25 08:46:25', NULL, NULL);
INSERT INTO `tb_user` VALUES (3, '', '', 'https://ksling.cn/static/img/avatar/default/defaultAvatar.jpg', '', 'admin', 2, '2023-04-25 08:46:25', NULL, NULL);
INSERT INTO `tb_user` VALUES (4, '', '', 'https://ksling.cn/static/img/avatar/default/defaultAvatar.jpg', '', 'editor', 2, '2023-04-25 08:46:25', NULL, NULL);
INSERT INTO `tb_user` VALUES (5, '', '', 'https://ksling.cn/static/img/avatar/default/defaultAvatar.jpg', '', 'author', 2, '2023-04-25 08:46:25', NULL, NULL);
INSERT INTO `tb_user` VALUES (6, '', '', 'https://ksling.cn/static/img/avatar/default/defaultAvatar.jpg', '', 'user', 2, '2023-04-25 08:46:25', NULL, NULL);
INSERT INTO `tb_user` VALUES (7, '', '', 'https://ksling.cn/static/img/avatar/default/defaultAvatar.jpg', '', 'test', 2, '2023-04-25 08:46:25', NULL, NULL);
INSERT INTO `tb_user` VALUES (100000001, '', '', 'https://ksling.cn/static/img/avatar/default/defaultAvatar.jpg', '', 'zhangsan', 2, '2023-04-25 08:46:25', NULL, NULL);

-- ----------------------------
-- Table structure for tb_user_auth
-- ----------------------------
DROP TABLE IF EXISTS `tb_user_auth`;
CREATE TABLE `tb_user_auth`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '账号id',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名',
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
INSERT INTO `tb_user_auth` VALUES (2, 2, 'root@qq.com', '$2a$10$EEJEoO4JH09hUTWezz9pq.pYk/HU2HDigdSWIos9GfFODrnXVcrHe', '2023-07-14 17:28:13', 'Computer', 0, 0, '', '127.0.0.1', 0, '2023-04-26 21:44:26', NULL, NULL);
INSERT INTO `tb_user_auth` VALUES (3, 3, 'admin@qq.com', '$2a$10$EEJEoO4JH09hUTWezz9pq.pYk/HU2HDigdSWIos9GfFODrnXVcrHe', '2023-04-26 21:44:26', '', 0, 0, '', '', 0, '2023-04-26 21:44:26', NULL, NULL);
INSERT INTO `tb_user_auth` VALUES (4, 4, 'editor@qq.com', '$2a$10$EEJEoO4JH09hUTWezz9pq.pYk/HU2HDigdSWIos9GfFODrnXVcrHe', '2023-04-26 21:44:26', '', 0, 0, '', '', 0, '2023-04-26 21:44:26', NULL, NULL);
INSERT INTO `tb_user_auth` VALUES (5, 5, 'author@qq.com', '$2a$10$EEJEoO4JH09hUTWezz9pq.pYk/HU2HDigdSWIos9GfFODrnXVcrHe', '2023-07-14 17:25:42', 'Computer', 0, 0, '', '127.0.0.1', 0, '2023-04-26 21:44:26', NULL, NULL);
INSERT INTO `tb_user_auth` VALUES (6, 6, 'user@qq.com', '$2a$10$EEJEoO4JH09hUTWezz9pq.pYk/HU2HDigdSWIos9GfFODrnXVcrHe', '2023-07-14 14:18:51', 'Computer', 0, 0, '', '127.0.0.1', 0, '2023-04-26 21:44:26', NULL, NULL);
INSERT INTO `tb_user_auth` VALUES (7, 7, 'test@qq.com', '$2a$10$EEJEoO4JH09hUTWezz9pq.pYk/HU2HDigdSWIos9GfFODrnXVcrHe', '2023-04-26 21:44:26', '', 0, 0, '', '', 0, '2023-04-26 21:44:26', NULL, NULL);
INSERT INTO `tb_user_auth` VALUES (100000001, 100000001, 'zhangsan@qq.com', '$2a$10$EEJEoO4JH09hUTWezz9pq.pYk/HU2HDigdSWIos9GfFODrnXVcrHe', '2023-04-26 21:44:26', '', 0, 0, '', '', 0, '2023-04-26 21:44:26', NULL, NULL);

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
