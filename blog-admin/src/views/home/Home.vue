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
          ><i class="el-icon-s-comment"></i> 回复我的</span
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
                  <span> 对我的文章发表了评论</span>
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
              <div
                class="comment-info"
                @mouseenter="showDelTip(index)"
                @mouseleave="hideDelTip(index)"
              >
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
                  ref="delTip"
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
          ><i class="el-icon-s-comment"></i> &nbsp;@ 我的</span
        >
        我的行程
      </el-tab-pane>
      <el-tab-pane name="like">
        <span slot="label" style="font-weight: bold;"
          ><i class="el-icon-star-on"></i> 收到的赞</span
        >
        我的行程
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
  data: function() {
    return {
      activeName: "reply",
      replyCommentList: [],
      defaultAvatar: process.env.VUE_APP_STATIC_URL + "img/avatar.png",
      defaultArticleCover: process.env.VUE_APP_STATIC_URL + "img/article.jpg",
      homeURL: process.env.VUE_APP_HOME_URL,
      current: 1,
      infiniteLoadFlag: true
    };
  },
  methods: {
    handleTabClick(tab) {
      if (tab.name === "reply" && this.replyCommentList == null) {
        this.getReplyCommentList();
      }
    },
    getReplyCommentList() {
      this.axios.get("/api/back/notice/1").then(({ data }) => {
        this.replyCommentList = data.data;
      });
    },
    replyComment(index, item) {
      this.$refs.reply.forEach(item => {
        item.$el.style.display = "none";
      });
      this.$refs.reply[index].commentContent = "";
      this.$refs.reply[index].nickname = item.nickname;
      this.$refs.reply[index].replyId = item.commentUserId;
      this.$refs.reply[index].parentId = item.commentId;
      this.$refs.reply[index].articleId = item.articleId;
      this.$refs.reply[index].layer = item.parentId === -1;
      this.$refs.reply[index].chooseEmoji = false;
      this.$refs.reply[index].index = index;
      this.$refs.reply[index].$el.style.display = "block";
    },
    showDelTip(index) {
      this.$refs.delTip[index].style.display = "";
    },
    hideDelTip(index) {
      this.$refs.delTip[index].style.display = "none";
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
          this.getReplyCommentList();
        } else {
          this.$notify.error({
            title: "失败",
            message: data.message
          });
        }
      });
      this.editStatus = false;
    },
    loadReplyCommentList() {
      if (this.infiniteLoadFlag) {
        this.axios
          .get("/api/back/notices", {
            params: {
              type: 1,
              size: 10,
              current: this.current
            }
          })
          .then(({ data }) => {
            if (data.data.length) {
              this.current++;
              this.replyCommentList.push(...data.data);
            }
            if (data.data.length < 10) {
              this.infiniteLoadFlag = false;
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
