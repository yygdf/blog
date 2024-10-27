<template>
  <el-card class="main-card">
    <el-tabs
      v-model="activeName"
      tab-position="left"
      style="height:calc(100vh - 180px);"
      @tab-click="handleTabClick"
    >
      <el-tab-pane name="reply">
        <span slot="label" style="font-weight: bold;"
          ><i class="el-icon-s-comment"></i> {{ $t("home.replyToMe") }}
          <sup
            :style="
              replyCommentIdUnReadCount > 0 ? 'color: red;' : 'color: gray;'
            "
            >{{ "NEW(" + replyCommentIdUnReadCount + ")" }}</sup
          ></span
        >
        <div class="el-tab-pane" v-infinite-scroll="loadReplyCommentList">
          <el-card v-for="(item, index) of replyCommentList" :key="item.id">
            <el-image
              :src="item.avatar ? item.avatar : defaultAvatar"
              class="comment-avatar"
              style="width: 40px;height: 40px;border-radius: 20px;"
            />
            <a
              :href="
                homeURL +
                  '/' +
                  userId +
                  '/article/' +
                  item.articleId +
                  loginInfo
              "
              target="_blank"
            >
              <el-tooltip
                :content="item.articleTitle"
                placement="bottom"
                effect="light"
              >
                <el-image
                  :src="
                    item.articleCover ? item.articleCover : defaultArticleCover
                  "
                  style="width: 80px;height: 80px;float: right;"
                />
              </el-tooltip>
            </a>
            <div
              class="comment-meta"
              style="padding-left: 40px;margin-top: -50px"
              @mouseenter="showDelTip(index, item.id)"
              @mouseleave="hideDelTip(index)"
            >
              <div class="comment-user">
                <span
                  v-if="!item.website"
                  style="font-weight: bold;font-size: 20px"
                >
                  {{ item.nickname }}
                </span>
                <a v-else :href="item.website" target="_blank">
                  {{ item.nickname }}
                </a>
                <a
                  :href="
                    homeURL +
                      '/' +
                      userId +
                      '/article/' +
                      item.articleId +
                      loginInfo
                  "
                  style="text-decoration: none;color: inherit;"
                  target="_blank"
                >
                  <span>
                    {{ $t("home.commentMyArticle") }}
                    <sup
                      v-show="replyCommentIdUnReadStatus(item.id)"
                      style="color: red"
                      >·</sup
                    ></span
                  >
                </a>
              </div>
              <a
                :href="
                  homeURL +
                    '/' +
                    userId +
                    '/article/' +
                    item.articleId +
                    loginInfo
                "
                style="text-decoration: none;color: inherit;"
                target="_blank"
              >
                <p
                  class="comment-content"
                  v-html="item.commentContent.replace(/\n/g, '<br/>')"
                ></p>
              </a>
              <div class="comment-info">
                <span style="margin-right:10px">{{
                  item.createTime | dateTime
                }}</span>
                <span class="reply-btn" @click="replyComment(index, item)">
                  <i class="el-icon-chat-square"></i>{{ $t("home.reply") }}
                </span>
                <span
                  v-if="isLike(item.commentId)"
                  class="like-active"
                  @click="like(item)"
                  ><i class="el-icon-thumb"></i>{{ $t("home.liked") }}</span
                >
                <span v-else class="like" @click="like(item)"
                  ><i class="el-icon-thumb"></i>{{ $t("home.like") }}</span
                >
                <span
                  ref="replyDelTip"
                  class="del-btn"
                  style="display: none;"
                  @click="updateNoticesStatus(item.id)"
                  ><i class="el-icon-delete"></i
                  >{{ $t("home.deleteNotice") }}</span
                >
              </div>
              <Reply ref="reply" />
            </div>
          </el-card>
        </div>
      </el-tab-pane>
      <el-tab-pane name="at">
        <span slot="label" style="font-weight: bold;"
          ><i class="el-icon-s-comment"></i> &nbsp;{{ $t("home.mine") }}
          <sup
            :style="atCommentIdUnReadCount > 0 ? 'color: red;' : 'color: gray;'"
            >{{ "NEW(" + atCommentIdUnReadCount + ")" }}</sup
          ></span
        >
        <div class="el-tab-pane" v-infinite-scroll="loadAtCommentList">
          <el-card v-for="(item, index) of atCommentList" :key="item.id">
            <el-image
              :src="item.avatar ? item.avatar : defaultAvatar"
              class="comment-avatar"
              style="width: 40px;height: 40px;border-radius: 20px;"
            />
            <a
              :href="
                homeURL +
                  '/' +
                  userId +
                  '/article/' +
                  item.articleId +
                  loginInfo
              "
              target="_blank"
            >
              <el-tooltip
                :content="item.articleTitle"
                placement="bottom"
                effect="light"
              >
                <el-image
                  :src="
                    item.articleCover ? item.articleCover : defaultArticleCover
                  "
                  style="width: 80px;height: 80px;float: right;"
                />
              </el-tooltip>
            </a>
            <div
              class="comment-meta"
              style="padding-left: 40px;margin-top: -50px"
              @mouseenter="showDelTip(index, item.id)"
              @mouseleave="hideDelTip(index)"
            >
              <div class="comment-user">
                <span
                  v-if="!item.website"
                  style="font-weight: bold;font-size: 20px"
                >
                  {{ item.nickname }}
                </span>
                <a v-else :href="item.website" target="_blank">
                  {{ item.nickname }}
                </a>
                <a
                  :href="
                    homeURL +
                      '/' +
                      userId +
                      '/article/' +
                      item.articleId +
                      loginInfo
                  "
                  style="text-decoration: none;color: inherit;"
                  target="_blank"
                >
                  <span>
                    {{ $t("home.me") }}
                    <sup
                      v-show="atCommentIdUnReadStatus(item.id)"
                      style="color: red"
                      >·</sup
                    ></span
                  >
                </a>
              </div>
              <a
                :href="
                  homeURL +
                    '/' +
                    userId +
                    '/article/' +
                    item.articleId +
                    loginInfo
                "
                style="text-decoration: none;color: inherit;"
                target="_blank"
              >
                <p
                  class="comment-content"
                  v-html="item.commentContent.replace(/\n/g, '<br/>')"
                ></p>
              </a>
              <div class="comment-info">
                <span style="margin-right:10px">{{
                  item.createTime | dateTime
                }}</span>
                <span class="reply-btn" @click="replyComment(index, item)">
                  <i class="el-icon-chat-square"></i>{{ $t("home.reply") }}
                </span>
                <span
                  v-if="isLike(item.commentId)"
                  class="like-active"
                  @click="like(item)"
                  ><i class="el-icon-thumb"></i>{{ $t("home.liked") }}</span
                >
                <span v-else class="like" @click="like(item)"
                  ><i class="el-icon-thumb"></i>{{ $t("home.like") }}</span
                >
                <span
                  ref="atDelTip"
                  class="del-btn"
                  style="display: none;"
                  @click="updateNoticesStatus(item.id)"
                  ><i class="el-icon-delete"></i
                  >{{ $t("home.deleteNotice") }}</span
                >
              </div>
              <Reply ref="at" />
            </div>
          </el-card>
        </div>
      </el-tab-pane>
      <el-tab-pane name="like">
        <span slot="label" style="font-weight: bold;"
          ><i class="el-icon-star-on"></i> {{ $t("home.ownLike") }}
          <sup
            :style="likeIdUnReadCount > 0 ? 'color: red;' : 'color: gray;'"
            >{{ "NEW(" + likeIdUnReadCount + ")" }}</sup
          ></span
        >
        <div class="el-tab-pane" v-infinite-scroll="loadLikeList">
          <el-card v-for="(item, index) of likeList" :key="item.id">
            <el-image
              :src="item.avatar ? item.avatar : defaultAvatar"
              class="comment-avatar"
              style="width: 40px;height: 40px;border-radius: 20px;"
            />
            <a
              :href="
                homeURL +
                  '/' +
                  (item.noticeTypeSub === 2
                    ? 'friendLinks'
                    : item.articleUserId + '/article/' + item.articleId) +
                  loginInfo
              "
              target="_blank"
            >
              <el-tooltip
                v-if="item.noticeTypeSub === 1"
                :content="item.articleTitle"
                placement="bottom"
                effect="light"
              >
                <el-image
                  :src="
                    item.articleCover ? item.articleCover : defaultArticleCover
                  "
                  style="width: 60px;height: 60px;float: right;margin-top: -5px"
                />
              </el-tooltip>
            </a>
            <div
              class="comment-meta"
              style="padding-left: 40px;margin-top: -50px"
              @mouseenter="showDelTip(index, item.id)"
              @mouseleave="hideDelTip(index)"
            >
              <div class="comment-user">
                <span
                  v-if="!item.website"
                  style="font-weight: bold;font-size: 20px"
                >
                  {{ item.nickname }}
                </span>
                <a v-else :href="item.website" target="_blank">
                  {{ item.nickname }}
                </a>
                <a
                  :href="
                    homeURL +
                      '/' +
                      (item.noticeTypeSub === 2
                        ? 'friendLinks'
                        : item.articleUserId + '/article/' + item.articleId) +
                      loginInfo
                  "
                  style="text-decoration: none;color: inherit;"
                  target="_blank"
                >
                  <span>
                    {{
                      item.noticeTypeSub === 1
                        ? $t("home.likedArticle")
                        : $t("home.likedComment")
                    }}
                    <sup v-show="likeIdUnReadStatus(item.id)" style="color: red"
                      >·</sup
                    ></span
                  >
                </a>
              </div>
              <a
                :href="
                  homeURL +
                    '/' +
                    (item.noticeTypeSub === 2
                      ? 'friendLinks'
                      : item.articleUserId + '/article/' + item.articleId) +
                    loginInfo
                "
                style="text-decoration: none;color: inherit;"
                target="_blank"
              >
                <p
                  class="comment-content"
                  v-html="replaceEnter(item.commentContent)"
                ></p>
              </a>
              <div class="comment-info">
                <span style="margin-right:10px">{{
                  item.createTime | dateTime
                }}</span>
                <span class="reply-btn">
                  <i class="el-icon-chat-square"></i
                  >{{ $t("home.privateMessage") }}
                </span>
                <span
                  ref="likeDelTip"
                  class="del-btn"
                  style="display: none;"
                  @click="updateNoticesStatus(item.id)"
                  ><i class="el-icon-delete"></i
                  >{{ $t("home.deleteNotice") }}</span
                >
              </div>
            </div>
          </el-card>
        </div>
      </el-tab-pane>
      <el-tab-pane name="systemNotice">
        <span slot="label" style="font-weight: bold;"
          ><i class="el-icon-message-solid"></i> {{ $t("home.systemNotice") }}
          <sup
            :style="
              systemNoticeIdUnReadCount > 0 ? 'color: red;' : 'color: gray;'
            "
            >{{ "NEW(" + systemNoticeIdUnReadCount + ")" }}</sup
          ></span
        >
        <div class="el-tab-pane" v-infinite-scroll="loadSystemNoticeList">
          <el-card v-for="(item, index) of systemNoticeList" :key="item.id">
            <div
              class="comment-meta"
              @mouseenter="showDelTip(index, item.id)"
              @mouseleave="hideDelTip(index)"
            >
              <div class="comment-user">
                <div slot="header">
                  <span style="font-weight: bold">
                    {{ item.noticeTitle }}
                    <sup
                      v-show="systemNoticeIdUnReadStatus(item.id)"
                      style="color: red"
                      >·</sup
                    ></span
                  >
                </div>
              </div>
              <p
                class="comment-content"
                v-html="item.noticeContent.replace(/\n/g, '<br/>')"
              ></p>
              <div class="comment-info">
                <span style="margin-right:10px">{{
                  item.createTime | dateTime
                }}</span>
                <span class="reply-btn">
                  <i class="el-icon-close-notification"></i
                  >{{ $t("home.noMoreNotice") }}
                </span>
                <span
                  ref="systemNoticeDelTip"
                  class="del-btn"
                  style="display: none;"
                  @click="updateNoticesStatus(item.id)"
                  ><i class="el-icon-delete"></i
                  >{{ $t("home.deleteNotice") }}</span
                >
              </div>
            </div>
          </el-card>
        </div>
      </el-tab-pane>
      <el-tab-pane name="myMessage">
        <span slot="label" style="font-weight: bold;"
          ><i class="el-icon-message-solid"></i> {{ $t("home.myMessage") }}
          <sup
            :style="myMessageIdUnReadCount > 0 ? 'color: red;' : 'color: gray;'"
            >{{ "NEW(" + myMessageIdUnReadCount + ")" }}</sup
          >
        </span>
      </el-tab-pane>
      <el-tab-pane name="messageConfig">
        <span slot="label" style="font-weight: bold;"
          ><i class="el-icon-setting"></i> {{ $t("home.setting") }}</span
        >
        <el-card>
          <div slot="header">
            <span>{{ $t("home.replyToMe") }}</span>
          </div>
          <el-radio-group
            v-model="replyCommentSetting"
            @change="updateMessageConfigStatus(1)"
          >
            <el-radio :label="1">{{ $t("switch.enabled") }}</el-radio>
            <el-radio :label="2">{{ $t("switch.disabled") }}</el-radio>
          </el-radio-group>
        </el-card>
        <el-card>
          <div slot="header">
            <span>{{ $t("home.mine") }}</span>
          </div>
          <el-radio-group
            v-model="atCommentSetting"
            @change="updateMessageConfigStatus(2)"
          >
            <el-radio :label="1">{{ $t("switch.enabled") }}</el-radio>
            <el-radio :label="2">{{ $t("switch.disabled") }}</el-radio>
          </el-radio-group>
        </el-card>
        <el-card>
          <div slot="header">
            <span>{{ $t("home.ownLike") }}</span>
          </div>
          <el-radio-group
            v-model="likeSetting"
            @change="updateMessageConfigStatus(3)"
          >
            <el-radio :label="1">{{ $t("switch.enabled") }}</el-radio>
            <el-radio :label="2">{{ $t("switch.disabled") }}</el-radio>
          </el-radio-group>
        </el-card>
        <el-card>
          <div slot="header">
            <span>{{ $t("home.privateMessage") }}</span>
          </div>
          <el-radio-group
            v-model="myMessageSetting"
            @change="updateMessageConfigStatus(4)"
          >
            <el-radio :label="1">{{ $t("switch.enabled") }}</el-radio>
            <el-radio :label="2">{{ $t("switch.disabled") }}</el-radio>
          </el-radio-group>
        </el-card>
      </el-tab-pane>
      <el-tab-pane name="sendMessage" v-if="checkWeight(200)">
        <span slot="label" style="font-weight: bold;"
          ><i class="el-icon-message"></i> {{ $t("home.sendMessage") }}</span
        >
        <el-tabs v-model="activeNameSendMessage">
          <el-tab-pane
            :label="$t('home.systemNotice')"
            name="system"
            style="height:60vh;"
          >
            <el-form label-width="80px" :model="systemNoticeForm">
              <el-form-item :label="$t('home.recipient')">
                <el-select
                  v-model="systemNoticeForm.userId"
                  ref="input"
                  size="small"
                  class="word-limit-input form-input-width"
                  :placeholder="$t('home.selectRecipient')"
                  remote
                  clearable
                  filterable
                  :remote-method="getUsernames"
                >
                  <el-option
                    v-for="item in usernameList"
                    :key="item.id"
                    :value="item.id"
                    :label="item.label"
                  />
                </el-select>
              </el-form-item>
              <el-form-item :label="$t('home.title')">
                <el-input
                  v-model="systemNoticeForm.noticeTitle"
                  size="small"
                  class="word-limit-input form-input-width"
                  maxlength="50"
                  :placeholder="$t('home.inputTitle')"
                  show-word-limit
                />
              </el-form-item>
              <el-form-item :label="$t('home.content')">
                <el-input
                  v-model="systemNoticeForm.noticeContent"
                  size="small"
                  type="textarea"
                  style="width: 320px;"
                  :placeholder="$t('home.inputContent')"
                  show-word-limit
                />
              </el-form-item>
              <el-button
                @click="sendSystemNotice(1)"
                type="primary"
                size="medium"
                style="margin-left:12rem"
              >
                {{ $t("home.send") }}
              </el-button>
            </el-form>
          </el-tab-pane>
          <el-tab-pane
            :label="$t('home.emailNotice')"
            name="email"
            style="height:60vh;"
          >
            <el-form label-width="80px" :model="emailNoticeForm">
              <el-form-item :label="$t('home.recipient')">
                <el-select
                  v-model="emailNoticeForm.userId"
                  ref="input"
                  size="small"
                  class="word-limit-input form-input-width"
                  :placeholder="$t('home.selectRecipient')"
                  remote
                  clearable
                  filterable
                  :remote-method="getUsernames"
                >
                  <el-option
                    v-for="item in usernameList"
                    :key="item.id"
                    :value="item.id"
                    :label="item.label"
                  />
                </el-select>
              </el-form-item>
              <el-form-item :label="$t('home.theme')">
                <el-input
                  v-model="emailNoticeForm.noticeTitle"
                  size="small"
                  class="word-limit-input form-input-width"
                  maxlength="50"
                  :placeholder="$t('home.inputTheme')"
                  show-word-limit
                />
              </el-form-item>
              <el-form-item :label="$t('home.content')">
                <el-input
                  v-model="emailNoticeForm.noticeContent"
                  size="small"
                  type="textarea"
                  style="width: 320px;"
                  :placeholder="$t('home.inputContent')"
                  show-word-limit
                />
              </el-form-item>
              <el-button
                @click="sendSystemNotice(2)"
                type="primary"
                size="medium"
                style="margin-left:12rem"
              >
                {{ $t("home.send") }}
              </el-button>
            </el-form>
          </el-tab-pane>
        </el-tabs>
      </el-tab-pane>
    </el-tabs>
  </el-card>
</template>

<script>
import Reply from "../../components/Reply";
export default {
  components: {
    Reply
  },
  created() {
    this.loadUnReadSet();
  },
  destroyed() {
    this.updateNoticesReadStatus();
  },
  data: function() {
    return {
      activeName: "reply",
      activeNameSendMessage: "system",
      replyCommentList: [],
      atCommentList: [],
      likeList: [],
      systemNoticeList: [],
      myMessageList: [],
      defaultAvatar: process.env.VUE_APP_STATIC_URL + "img/avatar.png",
      defaultArticleCover: process.env.VUE_APP_STATIC_URL + "img/article.jpg",
      homeURL: process.env.VUE_APP_HOME_URL,
      replyCurrent: 1,
      replyInfiniteLoadFlag: true,
      atCurrent: 1,
      atInfiniteLoadFlag: true,
      likeCurrent: 1,
      likeInfiniteLoadFlag: true,
      systemNoticeCurrent: 1,
      systemNoticeInfiniteLoadFlag: true,
      myMessageCurrent: 1,
      myMessageInfiniteLoadFlag: true,
      replyCommentIdUnReadSet: new Set(),
      replyCommentIdUnReadCount: 0,
      atCommentIdUnReadSet: new Set(),
      atCommentIdUnReadCount: 0,
      likeIdUnReadSet: new Set(),
      likeIdUnReadCount: 0,
      systemNoticeIdUnReadSet: new Set(),
      systemNoticeIdUnReadCount: 0,
      myMessageIdUnReadSet: new Set(),
      myMessageIdUnReadCount: 0,
      idReadList: [],
      replyCommentSetting: 1,
      atCommentSetting: 1,
      likeSetting: 1,
      myMessageSetting: 1,
      loadMessageConfigFlag: true,
      usernameList: [],
      systemNoticeForm: {
        type: 1,
        userId: null,
        noticeTitle: "",
        noticeContent: ""
      },
      emailNoticeForm: {
        type: 2,
        userId: null,
        noticeTitle: "",
        noticeContent: ""
      }
    };
  },
  methods: {
    checkWeight(weight) {
      return this.$store.state.weight <= weight;
    },
    getUsernames(keywords) {
      if (keywords.trim() === "") {
        return;
      }
      this.axios
        .get("/api/back/userAuth/usernames", { params: { keywords } })
        .then(({ data }) => {
          this.usernameList = data.data;
        });
    },
    handleTabClick() {
      if (this.activeName === "at" && this.atCommentList.length === 0) {
        this.loadAtCommentList();
      } else if (this.activeName === "like" && this.likeList.length === 0) {
        this.loadLikeList();
      } else if (
        this.activeName === "systemNotice" &&
        this.likeList.length === 0
      ) {
        this.loadSystemNoticeList();
      } else if (this.activeName === "messageConfig") {
        this.loadMessageConfig();
      }
    },
    replyComment(index, item) {
      if (this.activeName === "reply") {
        this.$refs.reply.forEach(item => {
          item.$el.style.display = "none";
        });
        this.$refs.reply[index].commentContent = "";
        this.$refs.reply[index].nickname = item.nickname;
        this.$refs.reply[index].replyId = item.commentUserId;
        this.$refs.reply[index].parentId =
          item.parentId === -1 ? item.commentId : item.parentId;
        this.$refs.reply[index].articleId = item.articleId;
        this.$refs.reply[index].layer = item.parentId === -1;
        this.$refs.reply[index].chooseEmoji = false;
        this.$refs.reply[index].index = index;
        this.$refs.reply[index].$el.style.display = "block";
      } else if (this.activeName === "at") {
        this.$refs.at.forEach(item => {
          item.$el.style.display = "none";
        });
        this.$refs.at[index].commentContent = "";
        this.$refs.at[index].nickname = item.nickname;
        this.$refs.at[index].replyId = item.commentUserId;
        this.$refs.at[index].parentId = item.parentId;
        this.$refs.at[index].articleId = item.articleId;
        this.$refs.at[index].layer = false;
        this.$refs.at[index].chooseEmoji = false;
        this.$refs.at[index].index = index;
        this.$refs.at[index].$el.style.display = "block";
      }
    },
    showDelTip(index, id) {
      if (this.activeName === "reply") {
        this.$refs.replyDelTip[index].style.display = "";
        if (this.replyCommentIdUnReadSet.has(id)) {
          this.replyCommentIdUnReadSet.delete(id);
          this.replyCommentIdUnReadCount--;
          this.idReadList.push(id);
        }
      } else if (this.activeName === "at") {
        this.$refs.atDelTip[index].style.display = "";
        if (this.atCommentIdUnReadSet.has(id)) {
          this.atCommentIdUnReadSet.delete(id);
          this.atCommentIdUnReadCount--;
          this.idReadList.push(id);
        }
      } else if (this.activeName === "like") {
        this.$refs.likeDelTip[index].style.display = "";
        if (this.likeIdUnReadSet.has(id)) {
          this.likeIdUnReadSet.delete(id);
          this.likeIdUnReadCount--;
          this.idReadList.push(id);
        }
      } else if (this.activeName === "systemNotice") {
        this.$refs.systemNoticeDelTip[index].style.display = "";
        if (this.systemNoticeIdUnReadSet.has(id)) {
          this.systemNoticeIdUnReadSet.delete(id);
          this.systemNoticeIdUnReadCount--;
          this.idReadList.push(id);
        }
      }
    },
    hideDelTip(index) {
      if (this.activeName === "reply") {
        this.$refs.replyDelTip[index].style.display = "none";
      } else if (this.activeName === "at") {
        this.$refs.atDelTip[index].style.display = "none";
      } else if (this.activeName === "like") {
        this.$refs.likeDelTip[index].style.display = "none";
      } else if (this.activeName === "systemNotice") {
        this.$refs.systemNoticeDelTip[index].style.display = "none";
      }
    },
    like(comment) {
      this.axios
        .post("/api/comment/like/" + comment.commentId)
        .then(({ data }) => {
          if (data.flag) {
            if (
              this.$store.state.commentLikeSet.indexOf(comment.commentId) !== -1
            ) {
              this.$store.commit("commentUnLike", comment.commentId);
            } else {
              this.$store.commit("commentLike", comment.commentId);
            }
          }
        });
    },
    updateNoticesStatus(id) {
      let param = {
        idList: [id]
      };
      this.axios.put("/api/back/notices/status", param).then(({ data }) => {
        if (data.flag) {
          this.$notify.success({
            title: this.$t("success"),
            message: data.message
          });
          let commentList;
          if (this.activeName === "reply") {
            commentList = this.replyCommentList;
          } else if (this.activeName === "at") {
            commentList = this.atCommentList;
          } else if (this.activeName === "like") {
            commentList = this.likeList;
          } else if (this.activeName === "systemNotice") {
            commentList = this.systemNoticeList;
          }
          let index = commentList.findIndex(item => item.id === id);
          commentList.splice(index, 1);
        } else {
          this.$notify.error({
            title: this.$t("failure"),
            message: data.message
          });
        }
      });
    },
    loadUnReadSet() {
      this.axios.get("/api/back/notices/unread").then(({ data }) => {
        if (data.flag) {
          this.replyCommentIdUnReadSet = new Set(data.data["1"]);
          this.replyCommentIdUnReadCount = this.replyCommentIdUnReadSet.size;
          this.atCommentIdUnReadSet = new Set(data.data["2"]);
          this.atCommentIdUnReadCount = this.atCommentIdUnReadSet.size;
          this.likeIdUnReadSet = new Set(data.data["3"]);
          this.likeIdUnReadCount = this.likeIdUnReadSet.size;
          this.systemNoticeIdUnReadSet = new Set(data.data["4"]);
          this.systemNoticeIdUnReadCount = this.systemNoticeIdUnReadSet.size;
          this.myMessageIdUnReadSet = new Set(data.data["5"]);
          this.myMessageIdUnReadCount = this.myMessageIdUnReadSet.size;
        }
      });
    },
    updateNoticesReadStatus() {
      if (this.idReadList.length > 0) {
        this.axios.put("/api/back/notices/status/read", {
          idList: this.idReadList
        });
      }
    },
    updateMessageConfigStatus(id) {
      let value;
      if (id === 1) {
        value = this.replyCommentSetting;
      } else if (id === 2) {
        value = this.atCommentSetting;
      } else if (id === 3) {
        value = this.likeSetting;
      } else if (id === 4) {
        value = this.myMessageSetting;
      }
      this.axios.put("/api/back/blog/messageConfig", {
        idList: [id],
        type: value
      });
    },
    loadReplyCommentList() {
      if (this.replyInfiniteLoadFlag) {
        this.axios
          .get("/api/back/notices", {
            params: {
              type: 1,
              size: 10,
              current: this.replyCurrent
            }
          })
          .then(({ data }) => {
            if (data.data.length) {
              this.replyCurrent++;
              this.replyCommentList.push(...data.data);
            }
            if (data.data.length < 10) {
              this.replyInfiniteLoadFlag = false;
            }
          });
      }
    },
    loadAtCommentList() {
      if (this.atInfiniteLoadFlag) {
        this.axios
          .get("/api/back/notices", {
            params: {
              type: 2,
              size: 10,
              current: this.atCurrent
            }
          })
          .then(({ data }) => {
            if (data.data.length) {
              this.atCurrent++;
              this.atCommentList.push(...data.data);
            }
            if (data.data.length < 10) {
              this.atInfiniteLoadFlag = false;
            }
          });
      }
    },
    loadLikeList() {
      if (this.likeInfiniteLoadFlag) {
        this.axios
          .get("/api/back/notices", {
            params: {
              type: 3,
              size: 10,
              current: this.likeCurrent
            }
          })
          .then(({ data }) => {
            if (data.data.length) {
              this.likeCurrent++;
              this.likeList.push(...data.data);
            }
            if (data.data.length < 10) {
              this.likeInfiniteLoadFlag = false;
            }
          });
      }
    },
    loadSystemNoticeList() {
      if (this.systemNoticeInfiniteLoadFlag) {
        this.axios
          .get("/api/back/notices", {
            params: {
              type: 4,
              size: 10,
              current: this.systemNoticeCurrent
            }
          })
          .then(({ data }) => {
            if (data.data.length) {
              this.systemNoticeCurrent++;
              this.systemNoticeList.push(...data.data);
            }
            if (data.data.length < 10) {
              this.systemNoticeInfiniteLoadFlag = false;
            }
          });
      }
    },
    loadMessageConfig() {
      if (this.loadMessageConfigFlag) {
        this.axios.get("/api/back/blog/messageConfig").then(({ data }) => {
          this.replyCommentSetting = data.data["1"];
          this.atCommentSetting = data.data["2"];
          this.likeSetting = data.data["3"];
          this.myMessageSetting = data.data["4"];
          this.loadMessageConfigFlag = false;
        });
      }
    },
    sendSystemNotice(type) {
      let param;
      if (type === 1) {
        if (!this.systemNoticeForm.userId) {
          this.$message.error(this.$t("home.recipientRule1"));
          return false;
        }
        if (this.systemNoticeForm.noticeTitle.trim() === "") {
          this.$message.error(this.$t("home.titleRule1"));
          return false;
        }
        if (this.systemNoticeForm.noticeContent.trim() === "") {
          this.$message.error(this.$t("home.contentRule1"));
          return false;
        }
        param = this.systemNoticeForm;
      } else if (type === 2) {
        if (!this.emailNoticeForm.userId) {
          this.$message.error(this.$t("home.recipientRule1"));
          return false;
        }
        if (this.emailNoticeForm.noticeTitle.trim() === "") {
          this.$message.error(this.$t("home.themeRule1"));
          return false;
        }
        if (this.emailNoticeForm.noticeContent.trim() === "") {
          this.$message.error(this.$t("home.contentRule1"));
          return false;
        }
        param = this.emailNoticeForm;
      }
      this.axios.post("/api/back/notice", param).then(({ data }) => {
        if (data.flag) {
          this.$notify.success({
            title: this.$t("success"),
            message: data.message
          });
        } else {
          this.$notify.error({
            title: this.$t("failure"),
            message: data.message
          });
        }
      });
    }
  },
  computed: {
    isLike() {
      return function(commentId) {
        let commentLikeSet = this.$store.state.commentLikeSet;
        return commentLikeSet.indexOf(commentId) !== -1;
      };
    },
    userId() {
      return this.$store.state.userId;
    },
    replaceEnter: function() {
      return commentContent => {
        if (commentContent != null) {
          return commentContent.toString().replace(/\n/g, "<br/><br/>");
        }
        return "";
      };
    },
    replyCommentIdUnReadStatus: function() {
      return id => {
        return this.replyCommentIdUnReadSet.has(id);
      };
    },
    atCommentIdUnReadStatus: function() {
      return id => {
        return this.atCommentIdUnReadSet.has(id);
      };
    },
    likeIdUnReadStatus: function() {
      return id => {
        return this.likeIdUnReadSet.has(id);
      };
    },
    systemNoticeIdUnReadStatus: function() {
      return id => {
        return this.systemNoticeIdUnReadSet.has(id);
      };
    },
    myMessageIdUnReadStatus: function() {
      return id => {
        return this.myMessageIdUnReadSet.has(id);
      };
    },
    loginInfo() {
      return (
        "?token=" +
        this.$store.state.token +
        "&loginUserDTO=" +
        JSON.stringify({
          userId: this.$store.state.userId,
          intro: this.$store.state.intro,
          email: this.$store.state.email,
          avatar: this.$store.state.avatar,
          gender: this.$store.state.gender,
          weight: this.$store.state.weight,
          website: this.$store.state.website,
          nickname: this.$store.state.nickname,
          modifiedFlag: this.$store.state.modifiedFlag,
          articleLikeSet: this.$store.state.articleLikeSet,
          commentLikeSet: this.$store.state.commentLikeSet
        })
      );
    }
  }
};
</script>

<style scoped>
.el-tab-pane {
  height: 80vh;
  overflow-y: auto;
}
.el-tab-pane >>> .el-card:hover {
  background-color: rgba(240, 240, 240, 0.3);
}
</style>
