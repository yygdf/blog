import Vue from "vue";
import Vuex from "vuex";
import createPersistedState from "vuex-persistedstate";

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    loginUrl: "",
    rootUri: "",
    drawer: false,
    loginFlag: false,
    emailFlag: false,
    resetFlag: false,
    searchFlag: false,
    forgetFlag: false,
    registerFlag: false,
    userId: null,
    intro: null,
    email: null,
    avatar: null,
    gender: null,
    website: null,
    nickname: null,
    bloggerId: null,
    articleLikeSet: [],
    commentLikeSet: [],
    blogConfig: {},
    bloggerInfo: {}
  },
  mutations: {
    login(state, user) {
      state.userId = user.userId;
      state.intro = user.intro;
      state.email = user.email;
      state.avatar = user.avatar;
      state.gender = user.gender;
      state.website = user.website;
      state.nickname = user.nickname;
      state.articleLikeSet = user.articleLikeSet ? user.articleLikeSet : [];
      state.commentLikeSet = user.commentLikeSet ? user.commentLikeSet : [];
    },
    logout(state) {
      state.userId = null;
      state.intro = null;
      state.email = null;
      state.avatar = null;
      state.gender = null;
      state.website = null;
      state.nickname = null;
      state.articleLikeSet = [];
      state.commentLikeSet = [];
    },
    saveLoginUrl(state, url) {
      state.loginUrl = url;
    },
    saveRootUri(state, uri) {
      state.rootUri = uri;
    },
    saveEmail(state, email) {
      state.email = email;
    },
    updateLoginInfo(state, userInfo) {
      state.intro = userInfo.intro;
      state.website = userInfo.website;
      state.nickname = userInfo.nickname;
    },
    updateAvatar(state, avatar) {
      state.avatar = avatar;
    },
    saveBlogConfig(state, blogConfig) {
      state.blogConfig = blogConfig;
    },
    saveBloggerInfo(state, bloggerInfo) {
      state.bloggerInfo = bloggerInfo;
    },
    saveBloggerId(state, bloggerId) {
      state.bloggerId = bloggerId;
    },
    closeModel(state) {
      state.loginFlag = false;
      state.emailFlag = false;
      state.searchFlag = false;
      state.registerFlag = false;
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
