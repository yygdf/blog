import Vue from "vue";
import VueRouter from "vue-router";
import Home from "../views/home/Home.vue";
import Article from "../views/article/Article.vue";
import Archive from "../views/archive/Archive.vue";
import Tag from "../views/tag/Tag.vue";
import Category from "../views/category/Category.vue";
import FriendLink from "../views/link/FriendLink.vue";
import About from "../views/about/About.vue";
import Message from "../views/message/Messsage.vue";
import ArticleList from "../components/ArticleList.vue";
import User from "../views/user/User.vue";
import OauthLogin from "../components/OauthLogin.vue";

Vue.use(VueRouter);

const routes = [
  {
    path: "/:userId(\\d+)?",
    component: Home,
    meta: {
      title: "首页"
    }
  },
  {
    path: "/:userId(\\d+)?/article/:articleId(\\d+)",
    component: Article,
    meta: {
      title: "文章"
    }
  },
  {
    path: "/:userId(\\d+)?/archives",
    component: Archive,
    meta: {
      title: "归档"
    }
  },
  {
    path: "/:userId(\\d+)?/tags",
    component: Tag,
    meta: {
      title: "标签"
    }
  },
  {
    path: "/:userId(\\d+)?/categories",
    component: Category,
    meta: {
      title: "分类"
    }
  },
  {
    path: "/:userId(\\d+)?/category/:categoryId(\\d+)",
    component: ArticleList,
    meta: {
      title: "文章"
    }
  },
  {
    path: "/:userId(\\d+)?/friendLinks",
    component: FriendLink,
    meta: {
      title: "友链"
    }
  },
  {
    path: "/:userId(\\d+)?/about",
    component: About,
    meta: {
      title: "关于"
    }
  },
  {
    path: "/:userId(\\d+)?/messages",
    component: Message,
    meta: {
      title: "留言"
    }
  },
  {
    path: "/:userId(\\d+)?/tag/:tagId(\\d+)",
    component: ArticleList,
    meta: {
      title: "文章"
    }
  },
  {
    path: "/:userId(\\d+)?/user",
    component: User,
    meta: {
      title: "个人中心"
    }
  },
  {
    path: "/oauth/login/qq",
    component: OauthLogin,
    meta: {
      title: "QQ登录"
    }
  }
];

const router = new VueRouter({
  mode: "history",
  routes
});

export default router;
