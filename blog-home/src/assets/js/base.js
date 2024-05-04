import store from "../../store";
import axios from "axios";

export function getBloggerId() {
  let param = "";
  let commonUserFlag = true;
  let currentPathArr = window.location.pathname.split("/");
  if (store.state.loginUrl) {
    currentPathArr = store.state.loginUrl;
    store.commit("saveLoginUrl", "");
  }
  if (currentPathArr[1] === "" || isNaN(Number(currentPathArr[1]))) {
    commonUserFlag = false;
  } else {
    param = "?bloggerId=" + currentPathArr[1];
  }
  let xhr = new XMLHttpRequest();
  xhr.open("GET", "/api/blog/id" + param, false);
  xhr.send(null);
  if (xhr.status === 200) {
    let data = JSON.parse(xhr.responseText);
    store.commit("saveBloggerId", data.data);
    if (commonUserFlag) {
      store.commit("saveRootUri", "/" + data.data);
    }
  }
}

export async function getBlogInfo() {
  await axios.get("/api/blog/info").then(({ data }) => {
    if (data.flag) {
      store.commit("saveBlogConfig", data.data.blogConfig);
      if (!data.data.bloggerInfo.avatar) {
        data.data.bloggerInfo.avatar = require("../../assets/img/default/avatar.png");
      }
      store.commit("saveBloggerInfo", data.data.bloggerInfo);
    }
  });
}
