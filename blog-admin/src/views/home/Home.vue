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
          ><i class="el-icon-s-comment"></i> 回复我的
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
              :href="homeURL + '/' + userId + '/article/' + item.articleId"
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
                  :href="homeURL + '/' + userId + '/article/' + item.articleId"
                  style="text-decoration: none;color: inherit;"
                  target="_blank"
                >
                  <span> 对我的文章发表了评论 <sup v-show="replyCommentIdUnReadStatus(item.id)" style="color: red">·</sup></span>
                </a>
              </div>
              <a
                :href="homeURL + '/' + userId + '/article/' + item.articleId"
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
                  <i class="el-icon-chat-square"></i>回复
                </span>
                <span
                  v-if="isLike(item.commentId)"
                  class="like-active"
                  @click="like(item)"
                  ><i class="el-icon-thumb"></i>已赞</span
                >
                <span v-else class="like" @click="like(item)"
                  ><i class="el-icon-thumb"></i>点赞</span
                >
                <span
                  ref="replyDelTip"
                  class="del-btn"
                  style="display: none;"
                  @click="updateNoticesStatus(item.id)"
                  ><i class="el-icon-delete"></i>删除该通知</span
                >
              </div>
              <Reply ref="reply" />
            </div>
          </el-card>
        </div>
      </el-tab-pane>
      <el-tab-pane name="at">
        <span slot="label" style="font-weight: bold;"
          ><i class="el-icon-s-comment"></i> &nbsp;@ 我的
          <sup
            :style="
              atCommentIdUnReadCount > 0 ? 'color: red;' : 'color: gray;'
            "
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
              :href="homeURL + '/' + userId + '/article/' + item.articleId"
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
                  :href="homeURL + '/' + userId + '/article/' + item.articleId"
                  style="text-decoration: none;color: inherit;"
                  target="_blank"
                >
                  <span> @了我 <sup v-show="atCommentIdUnReadStatus(item.id)" style="color: red">·</sup></span>
                </a>
              </div>
              <a
                :href="homeURL + '/' + userId + '/article/' + item.articleId"
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
                  <i class="el-icon-chat-square"></i>回复
                </span>
                <span
                  v-if="isLike(item.commentId)"
                  class="like-active"
                  @click="like(item)"
                  ><i class="el-icon-thumb"></i>已赞</span
                >
                <span v-else class="like" @click="like(item)"
                  ><i class="el-icon-thumb"></i>点赞</span
                >
                <span
                  ref="atDelTip"
                  class="del-btn"
                  style="display: none;"
                  @click="updateNoticesStatus(item.id)"
                  ><i class="el-icon-delete"></i>删除该通知</span
                >
              </div>
              <Reply ref="at" />
            </div>
          </el-card>
        </div>
      </el-tab-pane>
      <el-tab-pane name="like">
        <span slot="label" style="font-weight: bold;"
          ><i class="el-icon-star-on"></i> 收到的赞
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
                    : item.articleUserId + '/article/' + item.articleId)
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
                        : item.articleUserId + '/article/' + item.articleId)
                  "
                  style="text-decoration: none;color: inherit;"
                  target="_blank"
                >
                  <span>
                    {{
                      item.noticeTypeSub === 1 ? "赞了我的文章" : "赞了我的评论"
                    }} <sup v-show="likeIdUnReadStatus(item.id)" style="color: red">·</sup></span
                  >
                </a>
              </div>
              <a
                :href="
                  homeURL +
                    '/' +
                    (item.noticeTypeSub === 2
                      ? 'friendLinks'
                      : item.articleUserId + '/article/' + item.articleId)
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
                  <i class="el-icon-chat-square"></i>回复
                </span>
                <span
                  ref="likeDelTip"
                  class="del-btn"
                  style="display: none;"
                  @click="updateNoticesStatus(item.id)"
                  ><i class="el-icon-delete"></i>删除该通知</span
                >
              </div>
            </div>
          </el-card>
        </div>
      </el-tab-pane>
      <el-tab-pane name="systemNotice">
        <span slot="label" style="font-weight: bold;"
          ><i class="el-icon-message-solid"></i> 系统通知</span
        >
        我的行程
      </el-tab-pane>
      <el-tab-pane name="myMessage">
        <span slot="label" style="font-weight: bold;"
          ><i class="el-icon-message-solid"></i> 我的消息</span
        >
        我的行程
      </el-tab-pane>
      <el-tab-pane name="messageConfig">
        <span slot="label" style="font-weight: bold;"
          ><i class="el-icon-setting"></i> 消息设置</span
        >
        我的行程
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
      replyCommentList: [],
      atCommentList: [],
      likeList: [],
      systemNotice: [],
      myMessage: [],
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
      idReadList: []
    };
  },
  methods: {
    handleTabClick() {
      if (this.activeName === "at" && this.atCommentList.length === 0) {
        this.loadAtCommentList();
      } else if (this.activeName === "like" && this.likeList.length === 0) {
        this.loadLikeList();
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
      }
    },
    hideDelTip(index) {
      if (this.activeName === "reply") {
        this.$refs.replyDelTip[index].style.display = "none";
      } else if (this.activeName === "at") {
        this.$refs.atDelTip[index].style.display = "none";
      } else if (this.activeName === "like") {
        this.$refs.likeDelTip[index].style.display = "none";
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
            title: "成功",
            message: data.message
          });
          let commentList;
          if (this.activeName === "reply") {
            commentList = this.replyCommentList;
          } else if (this.activeName === "at") {
            commentList = this.atCommentList;
          } else if (this.activeName === "like") {
            commentList = this.likeList;
          }
          let index = commentList.findIndex(item => item.id === id);
          commentList.splice(index, 1);
        } else {
          this.$notify.error({
            title: "失败",
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
