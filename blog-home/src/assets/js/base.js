import store from "../../store";
import axios from "axios";

export function getBlogInfo() {
  let params = {};
  let currentPathArr = window.location.pathname.split("/");
  if (currentPathArr[1] === "" || isNaN(Number(currentPathArr[1]))) {
    params.bloggerId = -1;
  } else {
    params.bloggerId = currentPathArr[1];
  }
  axios.get("/api/blog/info", { params }).then(({ data }) => {
    if (data.flag) {
      if (!data.data.blogConfig.home_banner_cover) {
        data.data.blogConfig.home_banner_cover =
          "../../assets/img/banner/home.jpg";
      }
      if (!data.data.blogConfig.home_banner_title) {
        data.data.blogConfig.home_banner_title = "";
      }
      if (!data.data.blogConfig.home_contact_qq) {
        data.data.blogConfig.home_contact_qq = "#";
      } else {
        data.data.blogConfig.home_contact_qq =
          "https://wpa.qq.com/msgrd?v=3&uin=" +
          data.data.blogConfig.home_contact_qq +
          "&site=qq&menu=yes";
      }
      if (!data.data.blogConfig.home_github) {
        data.data.blogConfig.home_github = "#";
      }
      if (!data.data.blogConfig.home_gitee) {
        data.data.blogConfig.home_gitee = "#";
      }
      if (!data.data.blogConfig.home_notice) {
        data.data.blogConfig.home_notice = "暂无内容~~~";
      }
      store.commit("saveBlogConfig", data.data.blogConfig);
      store.commit("saveBloggerInfo", data.data.bloggerInfo);
      store.commit("saveBloggerId", data.data.bloggerId);
    }
  });
}
