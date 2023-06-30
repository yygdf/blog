import store from "../../store";
import axios from "axios";

export function getBaseInfo() {
  axios.get("/api/base").then(({ data }) => {
    if (data.flag) {
      store.commit("saveBaseInfo", data.data);
    }
  });
}