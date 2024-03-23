import Vue from "vue";
import Vuex from "vuex";
import createPersistedState from "vuex-persistedstate";

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    loginUrl: "",
    rootUri: "",
    drawerFlag: false,
    loginFlag: false,
    emailFlag: false,
    resetFlag: false,
    searchFlag: false,
    forgetFlag: false,
    registerFlag: false,
    mobileFlag: document.documentElement.clientWidth <= 960,
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
    blogConfig: {
      home_banner_title: "",
      home_contact_qq: "#",
      home_github: "#",
      home_gitee: "#",
      home_notice: "暂无内容~~~",
      wx_pay_code: require("../assets/img/wxPay.png"),
      ali_pay_code: require("../assets/img/aliPay.png"),
      article_default_cover: require("../assets/img/default/article.jpg"),
      tag_banner_cover: require("../assets/img/banner/tag.jpg"),
      home_banner_cover: require("../assets/img/banner/home.jpg"),
      link_banner_cover: require("../assets/img/banner/link.jpg"),
      about_banner_cover: require("../assets/img/banner/about.jpg"),
      archive_banner_cover: require("../assets/img/banner/archive.jpg"),
      message_banner_cover: require("../assets/img/banner/message.jpg"),
      category_banner_cover: require("../assets/img/banner/category.jpg"),
      personal_banner_cover: require("../assets/img/banner/personal.jpg")
    },
    bloggerInfo: {},
    rootUrl: location.protocol + "//" + location.host
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
    updateUserInfo(state, userInfo) {
      state.intro = userInfo.intro;
      state.website = userInfo.website;
      state.nickname = userInfo.nickname;
    },
    updateAvatar(state, avatar) {
      state.avatar = avatar;
    },
    saveBlogConfig(state, blogConfig) {
      if (blogConfig.home_banner_title) {
        state.blogConfig.home_banner_title = blogConfig.home_banner_title;
      }
      if (blogConfig.home_contact_qq) {
        state.blogConfig.home_contact_qq =
          "https://wpa.qq.com/msgrd?v=3&uin=" +
          blogConfig.home_contact_qq +
          "&site=qq&menu=yes";
      }
      if (blogConfig.home_github) {
        state.blogConfig.home_github = blogConfig.home_github;
      }
      if (blogConfig.home_gitee) {
        state.blogConfig.home_gitee = blogConfig.home_gitee;
      }
      if (blogConfig.home_notice) {
        state.blogConfig.home_notice = blogConfig.home_notice;
      }
      if (blogConfig.wx_pay_code) {
        state.blogConfig.wx_pay_code = blogConfig.wx_pay_code;
      }
      if (blogConfig.ali_pay_code) {
        state.blogConfig.ali_pay_code = blogConfig.ali_pay_code;
      }
      if (blogConfig.article_default_cover) {
        state.blogConfig.article_default_cover =
          blogConfig.article_default_cover;
      }
      if (blogConfig.tag_banner_cover) {
        state.blogConfig.tag_banner_cover = blogConfig.tag_banner_cover;
      }
      if (blogConfig.home_banner_cover) {
        state.blogConfig.home_banner_cover = blogConfig.home_banner_cover;
      }
      if (blogConfig.link_banner_cover) {
        state.blogConfig.link_banner_cover = blogConfig.link_banner_cover;
      }
      if (blogConfig.about_banner_cover) {
        state.blogConfig.about_banner_cover = blogConfig.about_banner_cover;
      }
      if (blogConfig.archive_banner_cover) {
        state.blogConfig.archive_banner_cover = blogConfig.archive_banner_cover;
      }
      if (blogConfig.message_banner_cover) {
        state.blogConfig.message_banner_cover = blogConfig.message_banner_cover;
      }
      if (blogConfig.category_banner_cover) {
        state.blogConfig.category_banner_cover =
          blogConfig.category_banner_cover;
      }
      if (blogConfig.personal_banner_cover) {
        state.blogConfig.personal_banner_cover =
          blogConfig.personal_banner_cover;
      }
    },
    saveBloggerInfo(state, bloggerInfo) {
      state.bloggerInfo = bloggerInfo;
    },
    saveBloggerId(state, bloggerId) {
      state.bloggerId = bloggerId;
    },
    updateDrawerFlag(state, drawerFlag) {
      state.drawerFlag = drawerFlag;
    },
    updateLoginFlag(state, loginFlag) {
      state.loginFlag = loginFlag;
    },
    updateEmailFlag(state, emailFlag) {
      state.emailFlag = emailFlag;
    },
    updateResetFlag(state, resetFlag) {
      state.resetFlag = resetFlag;
    },
    updateForgetFlag(state, forgetFlag) {
      state.forgetFlag = forgetFlag;
    },
    updateSearchFlag(state, searchFlag) {
      state.searchFlag = searchFlag;
    },
    updateRegisterFlag(state, registerFlag) {
      state.registerFlag = registerFlag;
    },
    closeModel(state) {
      state.loginFlag = false;
      state.emailFlag = false;
      state.resetFlag = false;
      state.forgetFlag = false;
      state.searchFlag = false;
      state.registerFlag = false;
    },
    articleLike(state, articleId) {
      state.articleLikeSet.push(articleId);
    },
    articleUnLike(state, articleId) {
      let articleLikeSet = state.articleLikeSet;
      articleLikeSet.splice(articleLikeSet.indexOf(articleId), 1);
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
