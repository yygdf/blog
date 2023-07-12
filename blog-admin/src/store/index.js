import Vue from "vue";
import Vuex from "vuex";
import createPersistedState from "vuex-persistedstate";

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    collapse: false,
    userMenuList: [],
    tabList: [{ name: "首页", path: "/" }],
    userId: null,
    email: "",
    avatar: "",
    nickname: "",
    intro: "",
    webSite: "",
    currentRoutePath: "/"
  },
  mutations: {
    saveTab(state, tab) {
      if (state.tabList.findIndex(item => item.path === tab.path) === -1) {
        state.tabList.push({ name: tab.name, path: tab.path });
      }
    },
    removeTab(state, tab) {
      var index = state.tabList.findIndex(item => item.name === tab.name);
      state.tabList.splice(index, 1);
    },
    resetTab(state) {
      state.tabList = [{ name: "首页", path: "/" }];
    },
    trigger(state) {
      state.collapse = !state.collapse;
    },
    login(state, user) {
      state.userId = user.userId;
      state.nickname = user.nickname;
      state.avatar = user.avatar;
      state.intro = user.intro;
      state.email = user.email;
      state.webSite = user.webSite;
    },
    saveUserMenuList(state, userMenuList) {
      state.userMenuList = userMenuList;
    },
    logout(state) {
      state.userId = null;
      state.avatar = "";
      state.nickname = "";
      state.intro = "";
      state.email = "";
      state.webSite = "";
      state.userMenuList = [];
    },
    updateAvatar(state, avatar) {
      state.avatar = avatar;
    },
    updateUserInfo(state, user) {
      state.nickname = user.nickname;
      state.intro = user.intro;
      state.webSite = user.webSite;
    }
  },
  actions: {},
  modules: {},
  plugins: [
    createPersistedState({
      storage: window.sessionStorage
    })
  ]
});
