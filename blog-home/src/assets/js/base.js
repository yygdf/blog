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
      store.commit("saveUserConfigs", data.data);
    }
  });
}
