import Vue from "vue";
import App from "./App.vue";
import router from "./router";
import store from "./store";
import ElementUI from "element-ui";
import "element-ui/lib/theme-chalk/index.css";
import "./assets/css/index.css";
import axios from "axios";
import VueAxios from "vue-axios";
import ECharts from "vue-echarts";
import "echarts/lib/chart/line";
import "echarts/lib/chart/pie";
import "echarts/lib/chart/bar";
import "echarts/lib/component/tooltip";
import "echarts/lib/component/legend";
import "echarts/lib/component/title";
import mavonEditor from "mavon-editor";
import "mavon-editor/dist/css/index.css";
import moment from "moment";
import NProgress from "nprogress";
import "nprogress/nprogress.css";
import { generateMenu } from "./assets/js/menu";
import commonMethod from "./assets/js/common";
import VueI18n from "vue-i18n";
import en_US from "./i18n/en_US";
import zh_CN from "./i18n/zh_CN";

Vue.use(mavonEditor);
Vue.component("v-chart", ECharts);
Vue.use(VueAxios, axios);
Vue.use(ElementUI);
Vue.config.productionTip = false;
Vue.prototype.$moment = moment;
Vue.prototype.$commonMethod = commonMethod;
Vue.use(VueI18n);

Vue.filter("date", function(value, formatStr = "yyyy-MM-DD") {
  return moment(value).format(formatStr);
});

Vue.filter("dateTime", function(value, formatStr = "yyyy-MM-DD HH:mm:ss") {
  return moment(value).format(formatStr);
});

Vue.filter("subStr", function(value, maxLen = 120) {
  return value.length > maxLen ? value.substr(0, maxLen) + "..." : value;
});

const messages = { en_US, zh_CN };
const i18n = new VueI18n({
  messages,
  locale: localStorage.getItem("lang") || "zh_CN"
});

NProgress.configure({
  easing: "ease",
  speed: 500,
  showSpinner: false,
  trickleSpeed: 200,
  minimum: 0.3
});

router.beforeEach((to, from, next) => {
  NProgress.start();
  if (to.path === "/login") {
    next();
  } else if (!store.state.userId) {
    if (to.query.token != null) {
      store.commit("saveToken", to.query.token);
      store.commit("login", JSON.parse(to.query.loginUserDTO.toString()));
      next();
    } else {
      next({ path: "/login", query: { url: to.path } });
    }
  } else {
    next();
    store.state.currentRoutePath = to.path;
  }
});

router.afterEach(() => {
  NProgress.done();
});

axios.interceptors.request.use(
  function(request) {
    if (store.state.token != null) {
      request.headers["Token"] = store.state.token;
    }
    request.headers["Lang"] = i18n.locale;
    return request;
  },
  function(error) {
    return Promise.reject(error);
  }
);

axios.interceptors.response.use(
  function(response) {
    switch (response.data.code) {
      // case 40001:
      case 40003:
      case 40005:
      case 50001:
      case 50002:
      case 50003:
        Vue.prototype.$message({
          type: "error",
          message: response.data.message
        });
        router.push({ path: "/login" }).then();
        store.commit("saveToken", null);
        break;
      case 40002:
        Vue.prototype.$message({
          type: "error",
          message: response.data.message
        });
        break;
      case 40004:
        Vue.prototype.$message({
          type: "error",
          message: response.data.message
        });
        router
          .push({
            path: "/login",
            query: { url: store.state.currentRoutePath }
          })
          .then();
        store.commit("saveToken", null);
        break;
    }
    return response;
  },
  function(error) {
    return Promise.reject(error);
  }
);

new Vue({
  router,
  store,
  i18n,
  render: h => h(App),
  created() {
    if (store.state.userId) {
      generateMenu().then();
    }
  }
}).$mount("#app");
