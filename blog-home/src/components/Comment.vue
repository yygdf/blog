<template>
  <div>
    <div class="comment-title">
      <i class="iconfont my-icon-comment-group" />评论
    </div>
    <div class="comment-input-wrapper">
      <div style="display:flex">
        <v-avatar size="40">
          <img
            :src="
              this.$store.state.avatar
                ? this.$store.state.avatar
                : defaultAvatar
            "
            alt=""
          />
        </v-avatar>
        <div style="width:100%" class="ml-3">
          <div class="comment-input">
            <textarea
              class="comment-textarea"
              v-model="commentContent"
              placeholder="留下点什么吧..."
              auto-grow
              dense
            />
          </div>
          <div class="emoji-container">
            <span
              :class="chooseEmoji ? 'emoji-btn-active' : 'emoji-btn'"
              @click="chooseEmoji = !chooseEmoji"
            >
              <i class="iconfont my-icon-expression" />
            </span>
            <button
              @click="addComment"
              class="upload-btn v-comment-btn"
              style="margin-left:auto"
            >
              提交
            </button>
          </div>
          <emoji @addEmoji="addEmoji" :chooseEmoji="chooseEmoji" />
        </div>
      </div>
    </div>
    <div v-if="count > 0 && reFresh">
      <div class="count">{{ count }} 评论</div>
      <div
        style="display:flex"
        class="pt-5"
        v-for="(item, index) of commentList"
        :key="item.id"
      >
        <v-avatar size="40" class="comment-avatar">
          <img :src="item.avatar ? item.avatar : defaultAvatar" alt="" />
        </v-avatar>
        <div class="comment-meta">
          <div class="comment-user">
            <span v-if="!item.website">{{ item.nickname }}</span>
            <a v-else :href="item.website" target="_blank">
              {{ item.nickname }}
            </a>
            <span class="blogger-tag" v-if="item.userId === bloggerId"
              >博主</span
            >
          </div>
          <div class="comment-info">
            <span style="margin-right:10px">{{ count - index }}楼</span>
            <span style="margin-right:10px">{{ item.createTime | date }}</span>
            <span
              :class="isLike(item.id) + ' iconfont my-icon-like'"
              @click="like(item)"
            />
            <span v-show="item.likeCount > 0"> {{ item.likeCount }}</span>
            <span class="reply-btn" @click="replyComment(index, item)">
              回复
            </span>
          </div>
          <p v-html="item.commentContent" class="comment-content"></p>
          <div
            style="display:flex"
            v-for="reply of item.replyDTOList"
            :key="reply.id"
          >
            <v-avatar size="36" class="comment-avatar">
              <img :src="reply.avatar ? reply.avatar : defaultAvatar" alt="" />
            </v-avatar>
            <div class="reply-meta">
              <div class="comment-user">
                <span v-if="!reply.website">{{ reply.nickname }}</span>
                <a v-else :href="reply.website" target="_blank">
                  {{ reply.nickname }}
                </a>
                <span class="blogger-tag" v-if="reply.userId === bloggerId"
                  >博主</span
                >
              </div>
              <div class="comment-info">
                <span style="margin-right:10px">
                  {{ reply.createTime | date }}
                </span>
                <span
                  :class="isLike(reply.id) + ' iconfont my-icon-like'"
                  @click="like(reply)"
                />
                <span v-show="reply.likeCount > 0"> {{ reply.likeCount }}</span>
                <span class="reply-btn" @click="replyComment(index, reply)">
                  回复
                </span>
              </div>
              <p class="comment-content">
                <template v-if="reply.replyId !== item.userId">
                  <span v-if="!reply.replyWebsite" class="ml-1">
                    @{{ reply.replyNickname }}
                  </span>
                  <a
                    v-else
                    :href="reply.replyWebsite"
                    target="_blank"
                    class="comment-nickname ml-1"
                  >
                    @{{ reply.replyNickname }}
                  </a>
                  ，
                </template>
                <span v-html="reply.commentContent" />
              </p>
            </div>
          </div>
          <div
            class="mb-3"
            style="font-size:0.75rem;color:#6d757a"
            v-show="item.replyCount > 3"
            ref="check"
          >
            共
            <b>{{ item.replyCount }}</b>
            条回复，
            <span
              style="color:#00a1d6;cursor:pointer"
              @click="checkReplies(index, item)"
            >
              点击查看
            </span>
          </div>
          <div
            class="mb-3"
            style="font-size:0.75rem;color:#222;display:none"
            ref="paging"
          >
            <span style="padding-right:10px">
              共{{ Math.ceil(item.replyCount / 5) }}页
            </span>
            <paging
              ref="page"
              :totalPage="Math.ceil(item.replyCount / 5)"
              :index="index"
              :commentId="item.id"
              @changeReplyCurrent="changeReplyCurrent"
            />
          </div>
          <Reply ref="reply" @reloadReply="reloadReply" />
        </div>
      </div>
      <div class="load-wrapper">
        <v-btn outlined v-if="count > commentList.length" @click="getComments">
          加载更多...
        </v-btn>
      </div>
    </div>
    <div v-else style="padding:1.25rem;text-align:center">
      来发评论吧~
    </div>
  </div>
</template>

<script>
import Reply from "./Reply";
import Paging from "./Paging";
import Emoji from "./Emoji";
import EmojiList from "../assets/js/emoji";
export default {
  components: {
    Reply,
    Emoji,
    Paging
  },
  props: {
    commentList: {
      type: Array
    },
    count: {
      type: Number
    }
  },
  data: function() {
    return {
      reFresh: true,
      commentContent: "",
      chooseEmoji: false,
      current: 1,
      defaultAvatar: require("../assets/img/default/avatar.png")
    };
  },
  methods: {
    replyComment(index, item) {
      this.$refs.reply.forEach(item => {
        item.$el.style.display = "none";
      });
      this.$refs.reply[index].commentContent = "";
      this.$refs.reply[index].nickname = item.nickname;
      this.$refs.reply[index].replyId = item.userId;
      this.$refs.reply[index].parentId = this.commentList[index].id;
      this.$refs.reply[index].chooseEmoji = false;
      this.$refs.reply[index].index = index;
      this.$refs.reply[index].$el.style.display = "block";
    },
    addEmoji(key) {
      this.commentContent += key;
    },
    checkReplies(index, item) {
      this.axios
        .get("/api/comments/replies/" + item.id, {
          params: { current: 1 }
        })
        .then(({ data }) => {
          this.$refs.check[index].style.display = "none";
          item.replyDTOList = data.data;
          if (Math.ceil(item.replyCount / 5) > 1) {
            this.$refs.paging[index].style.display = "flex";
          }
        });
    },
    changeReplyCurrent(current, index, commentId) {
      this.axios
        .get("/api/comments/replies/" + commentId, {
          params: { current: current }
        })
        .then(({ data }) => {
          this.commentList[index].replyDTOList = data.data;
        });
    },
    getComments() {
      this.current++;
      const path = this.$route.path;
      const arr = path.split("/");
      this.axios
        .get("/api/comments", {
          params: { current: this.current, articleId: arr[2] }
        })
        .then(({ data }) => {
          this.commentList.push(...data.data.recordList);
        });
    },
    addComment() {
      if (!this.$store.state.userId) {
        this.$store.state.loginFlag = true;
        return false;
      }
      if (this.commentContent.trim() === "") {
        this.$toast({ type: "error", message: "评论不能为空" });
        return false;
      }
      const reg = /\[.+?]/g;
      let content = this.commentContent;
      content = content.replace(reg, function(str) {
        return (
          "<img src= '" +
          EmojiList[str] +
          "' width='20' height='20' style='padding: 0 1px' alt='' />"
        );
      });
      let pathArr = this.$route.path.split("/");
      let param = {
        articleId: pathArr[pathArr.length - 1],
        commentContent: content
      };
      this.axios.post("/api/comment", param).then(({ data }) => {
        if (data.flag) {
          this.$emit("reloadComment");
          this.commentContent = "";
          this.$toast({ type: "success", message: "评论成功" });
        } else {
          this.$toast({ type: "error", message: data.message });
        }
      });
    },
    like(comment) {
      if (!this.$store.state.userId) {
        this.$store.state.loginFlag = true;
        return false;
      }
      this.axios.post("/api/comment/" + comment.id).then(({ data }) => {
        if (data.flag) {
          if (this.$store.state.commentLikeSet.indexOf(comment.id) !== -1) {
            this.$set(comment, "likeCount", comment.likeCount - 1);
            this.$store.commit("commentUnLike", comment.id);
          } else {
            this.$set(comment, "likeCount", comment.likeCount + 1);
            this.$store.commit("commentLike", comment.id);
          }
        }
      });
    },
    reloadReply(index) {
      this.axios
        .get("/api/comments/replies/" + this.commentList[index].id, {
          params: {
            current: this.$refs.page[index].current
          }
        })
        .then(({ data }) => {
          this.commentList[index].replyCount++;
          if (this.commentList[index].replyCount > 5) {
            this.$refs.paging[index].style.display = "flex";
          }
          this.$refs.check[index].style.display = "none";
          this.$refs.reply[index].$el.style.display = "none";
          this.commentList[index].replyDTOList = data.data;
        });
    }
  },
  computed: {
    isLike() {
      return function(commentId) {
        let commentLikeSet = this.$store.state.commentLikeSet;
        return commentLikeSet.indexOf(commentId) !== -1
          ? "like-active"
          : "like";
      };
    },
    bloggerId() {
      return this.$store.state.bloggerId;
    }
  },
  watch: {
    commentList() {
      this.reFresh = false;
      this.$nextTick(() => {
        this.reFresh = true;
      });
    }
  }
};
</script>

<style scoped>
p {
  margin-bottom: 1.25rem !important;
}
.blogger-tag {
  background: #ffa51e;
  font-size: 12px;
  display: inline-block;
  border-radius: 2px;
  color: #fff;
  padding: 0 5px;
  margin-left: 6px;
}
.comment-title {
  display: flex;
  align-items: center;
  font-size: 1.25rem;
  font-weight: bold;
  line-height: 40px;
  margin-bottom: 10px;
}
.comment-title i {
  font-size: 1.5rem;
  margin-right: 5px;
}
.comment-input-wrapper {
  border: 1px solid #90939950;
  border-radius: 4px;
  padding: 10px;
  margin: 0 0 10px;
}
.count {
  padding: 5px;
  line-height: 1.75;
  font-size: 1.25rem;
  font-weight: bold;
}
.comment-meta {
  margin-left: 0.8rem;
  width: 100%;
  border-bottom: 1px dashed #f5f5f5;
}
.reply-meta {
  margin-left: 0.8rem;
  width: 100%;
}
.comment-user {
  font-size: 14px;
  line-height: 1.75;
}
.comment-user a {
  color: #1abc9c !important;
  font-weight: 500;
  transition: 0.3s all;
}
.comment-nickname {
  text-decoration: none;
  color: #1abc9c !important;
  font-weight: 500;
}
.comment-info {
  line-height: 1.75;
  font-size: 0.75rem;
  color: #b3b3b3;
}
.reply-btn {
  cursor: pointer;
  float: right;
  color: #ef2f11;
}
.comment-content {
  font-size: 0.875rem;
  line-height: 1.75;
  padding-top: 0.625rem;
}
.comment-avatar {
  transition: all 0.5s;
}
.comment-avatar:hover {
  transform: rotate(360deg);
}
.like {
  cursor: pointer;
  font-size: 0.875rem;
}
.like:hover {
  color: #eb5055;
}
.like-active {
  cursor: pointer;
  font-size: 0.875rem;
  color: #eb5055;
}
.load-wrapper {
  margin-top: 10px;
  display: flex;
  justify-content: center;
  align-items: center;
}
.load-wrapper button {
  background-color: #49b1f5;
  color: #fff;
}
</style>
