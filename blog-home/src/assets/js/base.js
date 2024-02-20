import store from "../../store";
import axios from "axios";

export function getUserConfigs() {
  let params = {};
  let currentPathArr = window.location.pathname.split("/");
  if (currentPathArr.length > 1 && typeof currentPathArr[0] === "number") {
    params.userId = currentPathArr[0];
  }
  axios.get("/api/userConfigs", { params }).then(({ data }) => {
    if (data.flag) {
      if (!data.data.home_banner_cover) {
        data.data.home_banner_cover = "../../assets/img/banner/home.jpg";
      }
      if (!data.data.home_banner_title) {
        data.data.home_banner_title = "";
      }
      if (!data.data.home_contact_qq) {
        data.data.home_contact_qq = "#";
      } else {
        data.data.home_contact_qq =
          "https://wpa.qq.com/msgrd?v=3&uin=" +
          data.data.home_contact_qq +
          "&site=qq&menu=yes";
      }
      if (!data.data.home_github) {
        data.data.home_github = "#";
      }
      if (!data.data.home_gitee) {
        data.data.home_gitee = "#";
      }
      store.commit("saveUserConfigs", data.data);
    }
  });
}

export function getUserInfo() {
  let params = {};
  let currentPathArr = window.location.pathname.split("/");
  if (currentPathArr.length > 1 && typeof currentPathArr[0] === "number") {
    params.userId = currentPathArr[0];
  }
  axios.get("/api/user/userInfo", { params }).then(({ data }) => {
    if (data.flag) {
      store.commit("saveUserInfo", data.data);
    }
  });
}
