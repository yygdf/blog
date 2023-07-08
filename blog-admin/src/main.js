import Vue from "vue";
import App from "./App.vue";
import router from "./router";
import store from "./store";
import ElementUI from "element-ui";
import "element-ui/lib/theme-chalk/index.css";
import "./assets/css/index.css";
import config from "./assets/js/config";
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
import { generaMenu } from "./assets/js/menu";

Vue.prototype.config = config;
Vue.use(mavonEditor);
Vue.component("v-chart", ECharts);
Vue.use(VueAxios, axios);
Vue.use(ElementUI);
Vue.config.productionTip = false;
Vue.prototype.$moment = moment;

Vue.filter("date", function(value, formatStr = "yyyy-MM-DD") {
  return moment(value).format(formatStr);
});

Vue.filter("dateTime", function(value, formatStr = "yyyy-MM-DD HH:mm:ss") {
  return moment(value).format(formatStr);
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
    next({ path: "/login", query: { url: to.path } });
  } else {
    next();
  }
});

router.afterEach(() => {
  NProgress.done();
});

axios.interceptors.response.use(
  function(response) {
    switch (response.data.code) {
      case 40002:
      case 40003:
      case 40004:
      case 40005:
      case 50001:
      case 50002:
      case 50003:
        Vue.prototype.$message({
          type: "error",
          message: response.data.message
        });
        router.push({ path: "/login" });
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
  render: h => h(App),
  created() {
    if (store.state.userId) {
      generaMenu();
    }
  }
}).$mount("#app");
