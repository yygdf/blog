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
import Articles from "../views/article/Articles.vue";
import Personal from "../views/personal/Personal.vue";
import QQOauth from "../components/QQOauth.vue";
Vue.use(VueRouter);

const routes = [
  {
    path: "/:userId(\\d+)?",
    component: Home
  },
  {
    path: "/:userId(\\d+)?/article/:articleId(\\d+)",
    component: Article
  },
  {
    path: "/:userId(\\d+)?/archives",
    component: Archive
  },
  {
    path: "/:userId(\\d+)?/tags",
    component: Tag
  },
  {
    path: "/:userId(\\d+)?/categories",
    component: Category
  },
  {
    path: "/:userId(\\d+)?/category/:categoryId(\\d+)",
    component: Articles
  },
  {
    path: "/:userId(\\d+)?/friendLinks",
    component: FriendLink
  },
  {
    path: "/:userId(\\d+)?/about",
    component: About
  },
  {
    path: "/:userId(\\d+)?/messages",
    component: Message
  },
  {
    path: "/:userId(\\d+)?/tag/:tagId(\\d+)",
    component: Articles
  },
  {
    path: "/:userId(\\d+)?/personal",
    component: Personal
  },
  {
    path: "/oauth/qq",
    component: QQOauth
  }
];

const router = new VueRouter({
  mode: "history",
  routes
});

export default router;
