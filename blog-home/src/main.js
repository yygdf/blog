import Vue from "vue";
import App from "./App.vue";
import router from "./router";
import store from "./store";
import vuetify from "./plugins/vuetify";
import "@mdi/font/css/materialdesignicons.css";
import animated from "animate.css";
import "./assets/css/index.css";
import "./assets/icon/iconfont.css";
import "./assets/css/markdown.css";
import Share from "vue-social-share";
import "./assets/vue-social-share/client.css";
import { vueBaberrage } from "vue-baberrage";
import axios from "axios";
import VueAxios from "vue-axios";
import moment from "moment";
import InfiniteLoading from "vue-infinite-loading";
import "highlight.js/styles/atom-one-dark.css";
import VueImageSwipe from "vue-image-swipe";
import "vue-image-swipe/dist/vue-image-swipe.css";
import Toast from "./components/toast/index";
import NProgress from "nprogress";
import "nprogress/nprogress.css";
import { getBloggerId, getBlogInfo } from "./assets/js/base";
import "./assets/js/ribbon";
import confirm from "./plugins/confirm";
import { i18n } from "@/i18n/i18n";

Vue.prototype.$confirm = confirm;
Vue.config.productionTip = false;
Vue.use(animated);
Vue.use(Share);
Vue.use(vueBaberrage);
Vue.use(InfiniteLoading);
Vue.use(VueAxios, axios);
Vue.use(VueImageSwipe);
Vue.use(Toast);

Vue.filter("date", function(value) {
  return moment(value).format("yyyy-MM-DD");
});

Vue.filter("time", function(value) {
  return moment(value).format("HH:mm:ss");
});

Vue.filter("num", function(value) {
  if (value >= 1000) {
    return (value / 1000).toFixed(1) + "k";
  }
  return value;
});

router.beforeEach((to, from, next) => {
  NProgress.start();
  if (to.query.token != null) {
    store.commit("saveToken", to.query.token);
    store.commit("login", JSON.parse(to.query.loginUserDTO.toString()));
  }
  next();
});

router.afterEach(() => {
  window.scrollTo({
    top: 0,
    behavior: "instant"
  });
  NProgress.done();
});

axios.interceptors.request.use(
  function(request) {
    if (store.state.bloggerId != null) {
      request.headers["Blogger-Id"] = store.state.bloggerId;
    }
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
      case 20001:
      case 40001:
      case 40002:
      case 60001:
      case 60002:
        Vue.prototype.$toast({ type: "error", message: response.data.message });
        break;
      case 40003:
      case 40004:
      case 40005:
      case 50001:
      case 50002:
      case 50003:
        Vue.prototype.$toast({ type: "error", message: response.data.message });
        store.commit("logout");
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
  vuetify,
  i18n,
  render: h => h(App),
  created() {
    store.commit("closeModel");
    getBloggerId();
    getBlogInfo().then();
  }
}).$mount("#app");
