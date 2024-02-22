import store from "../../store";
import axios from "axios";

export function getBlogInfo() {
  let params = {};
  let currentPathArr = window.location.pathname.split("/");
  if (currentPathArr.length > 1 && typeof currentPathArr[0] === "number") {
    params.bloggerId = currentPathArr[0];
  }
  axios.get("/api/blog/info", { params }).then(({ data }) => {
    if (data.flag) {
      if (!data.data.userConfig.home_banner_cover) {
        data.data.userConfig.home_banner_cover =
          "../../assets/img/banner/home.jpg";
      }
      if (!data.data.userConfig.home_banner_title) {
        data.data.userConfig.home_banner_title = "";
      }
      if (!data.data.userConfig.home_contact_qq) {
        data.data.userConfig.home_contact_qq = "#";
      } else {
        data.data.userConfig.home_contact_qq =
          "https://wpa.qq.com/msgrd?v=3&uin=" +
          data.data.userConfig.home_contact_qq +
          "&site=qq&menu=yes";
      }
      if (!data.data.userConfig.home_github) {
        data.data.userConfig.home_github = "#";
      }
      if (!data.data.userConfig.home_gitee) {
        data.data.userConfig.home_gitee = "#";
      }
      store.commit("saveUserConfig", data.data.userConfig);
      store.commit("saveUserInfo", data.data.userInfo);
      store.commit("saveBloggerId", data.data.bloggerId);
    }
  });
}
