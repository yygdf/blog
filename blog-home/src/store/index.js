import Vue from "vue";
import Vuex from "vuex";
import createPersistedState from "vuex-persistedstate";

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    searchFlag: false,
    loginFlag: false,
    registerFlag: false,
    forgetFlag: false,
    emailFlag: false,
    resetFlag: false,
    drawer: false,
    loginUrl: "",
    userId: null,
    nickname: null,
    avatar: null,
    intro: null,
    email: null,
    webSite: null,
    articleLikeSet: [],
    commentLikeSet: [],
    userConfigs: {}
  },
  mutations: {
    login(state, user) {
      state.userId = user.userId;
      state.nickname = user.nickname;
      state.avatar = user.avatar;
      state.intro = user.intro;
      state.email = user.email;
      state.webSite = user.webSite;
      state.articleLikeSet = user.articleLikeSet ? user.articleLikeSet : [];
      state.commentLikeSet = user.commentLikeSet ? user.commentLikeSet : [];
    },
    logout(state) {
      state.userId = null;
      state.nickname = null;
      state.avatar = null;
      state.intro = null;
      state.email = null;
      state.webSite = null;
      state.articleLikeSet = [];
      state.commentLikeSet = [];
    },
    saveLoginUrl(state, url) {
      state.loginUrl = url;
    },
    saveEmail(state, email) {
      state.email = email;
    },
    updateUserInfo(state, user) {
      state.nickname = user.nickname;
      state.intro = user.intro;
      state.webSite = user.webSite;
    },
    updateAvatar(state, avatar) {
      state.avatar = avatar;
    },
    saveUserConfigs(state, userConfigs) {
      state.userConfigs = userConfigs;
    },
    closeModel(state) {
      state.registerFlag = false;
      state.loginFlag = false;
      state.searchFlag = false;
      state.emailFlag = false;
    },
    articleLike(state, articleId) {
      let articleLikeSet = state.articleLikeSet;
      if (articleLikeSet.indexOf(articleId) !== -1) {
        articleLikeSet.splice(articleLikeSet.indexOf(articleId), 1);
      } else {
        articleLikeSet.push(articleId);
      }
    },
    commentLike(state, commentId) {
      let commentLikeSet = state.commentLikeSet;
      if (commentLikeSet.indexOf(commentId) !== -1) {
        commentLikeSet.splice(commentLikeSet.indexOf(commentId), 1);
      } else {
        commentLikeSet.push(commentId);
      }
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
