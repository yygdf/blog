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
    avatar: null,
    gender: null,
    weight: 1000,
    website: "",
    nickname: "",
    modifiedFlag: null,
    userMenuList: [],
    commentLikeSet: [],
    currentRoutePath: "/",
    currentTab: { name: "首页", path: "/" },
    token: null
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
      state.gender = user.gender;
      state.weight = user.weight;
      state.website = user.website;
      state.nickname = user.nickname;
      state.modifiedFlag = user.modifiedFlag;
      state.commentLikeSet = user.commentLikeSet ? user.commentLikeSet : [];
    },
    saveToken(state, token) {
      state.token = token;
    },
    removeToken(state) {
      state.token = null;
    },
    saveUserMenuList(state, userMenuList) {
      state.userMenuList = userMenuList;
    },
    logout(state) {
      state.userId = null;
      state.intro = "";
      state.email = "";
      state.avatar = "";
      state.gender = null;
      state.weight = 1000;
      state.website = "";
      state.nickname = "";
      state.modifiedFlag = null;
      state.userMenuList = [];
    },
    updateAvatar(state, avatar) {
      state.avatar = avatar;
    },
    updateUserInfo(state, user) {
      state.intro = user.intro;
      state.gender = user.gender;
      state.website = user.website;
      state.nickname = user.nickname;
    },
    commentLike(state, commentId) {
      state.commentLikeSet.push(commentId);
    },
    commentUnLike(state, commentId) {
      let commentLikeSet = state.commentLikeSet;
      commentLikeSet.splice(commentLikeSet.indexOf(commentId), 1);
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
