import Vue from "vue";
import VueRouter from "vue-router";
import Home from "../views/home/Home.vue";
import Article from "../views/article/Article.vue";
import Archive from "../views/archive/Archive.vue";
import Tag from "../views/tag/Tag.vue";
import Category from "../views/category/Category.vue";
import Link from "../views/link/Link.vue";
import About from "../views/about/About.vue";
import Message from "../views/message/Messsage.vue";
import ArticleList from "../components/ArticleList.vue";
import User from "../views/user/User.vue";
import OauthLogin from "../components/OauthLogin.vue";

Vue.use(VueRouter);

const routes = [
  {
    path: "/",
    component: Home,
    meta: {
      title: "首页"
    }
  },
  {
    path: "/articles/:articleId",
    component: Article,
    meta: {
      title: "文章"
    }
  },
  {
    path: "/archives",
    component: Archive,
    meta: {
      title: "归档"
    }
  },
  {
    path: "/tags",
    component: Tag,
    meta: {
      title: "标签"
    }
  },
  {
    path: "/categories",
    component: Category,
    meta: {
      title: "分类"
    }
  },
  {
    path: "/categories/*",
    component: ArticleList,
    meta: {
      title: "文章"
    }
  },
  {
    path: "/links",
    component: Link,
    meta: {
      title: "友链"
    }
  },
  {
    path: "/about",
    component: About,
    meta: {
      title: "关于"
    }
  },
  {
    path: "/messages",
    component: Message,
    meta: {
      title: "留言"
    }
  },
  {
    path: "/tags/*",
    component: ArticleList,
    meta: {
      title: "文章"
    }
  },
  {
    path: "/user",
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
  base: process.env.BASE_URL,
  routes
});

export default router;
