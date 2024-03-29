import Layout from "@/layout/index.vue";
import router from "../../router";
import store from "../../store";
import axios from "axios";

export async function generateMenu() {
  await axios.get("/api/back/menus/user").then(({ data }) => {
    if (data.flag) {
      let userMenuList = data.data;
      userMenuList.forEach(item => {
        if (item.children.length === 0) {
          item.children = [
            {
              name: item.name,
              path: "",
              component: item.component
            }
          ];
          item.name = "";
        }
        item.component = Layout;
        item.children.forEach(route => {
          route.component = loadView(route.component);
        });
      });
      store.commit("saveUserMenuList", userMenuList);
      router.addRoutes(userMenuList);
    }
  });
}

export const loadView = view => {
  return resolve => require([`@/views${view}`], resolve);
};
