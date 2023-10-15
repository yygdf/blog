import Vue from "vue";
import Vuex from "vuex";
import createPersistedState from "vuex-persistedstate";

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    collapse: false,
    tabList: [{ name: "首页", path: "/" }],
    userId: null,
    email: "",
    intro: "",
    avatar: "",
    weight: 1000,
    website: "",
    nickname: "",
    userMenuList: [],
    currentRoutePath: "/",
    currentTab: { name: "首页", path: "/" }
  },
  mutations: {
    saveTab(state, tab) {
      if (state.tabList.findIndex(item => item.path === tab.path) === -1) {
        state.tabList.push({ name: tab.name, path: tab.path });
      }
    },
    saveCurrentTab(state, tab) {
      state.currentTab = tab;
    },
    removeTab(state, tab) {
      let index = state.tabList.findIndex(item => item.name === tab.name);
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
      state.intro = user.intro;
      state.email = user.email;
      state.avatar = user.avatar;
      state.weight = user.weight;
      state.website = user.website;
      state.nickname = user.nickname;
    },
    saveUserMenuList(state, userMenuList) {
      state.userMenuList = userMenuList;
    },
    logout(state) {
      state.userId = null;
      state.intro = "";
      state.email = "";
      state.avatar = "";
      state.weight = 1000;
      state.website = "";
      state.nickname = "";
      state.userMenuList = [];
    },
    updateAvatar(state, avatar) {
      state.avatar = avatar;
    },
    updateUserInfo(state, user) {
      state.intro = user.intro;
      state.website = user.website;
      state.nickname = user.nickname;
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
