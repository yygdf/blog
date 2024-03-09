<template>
  <div class="reply-input-wrapper" style="display: none" ref="reply">
    <textarea
      class="comment-textarea"
      :placeholder="'回复 @' + nickname + '：'"
      auto-grow
      dense
      v-model="commentContent"
    />
    <div class="emoji-container">
      <span
        :class="chooseEmoji ? 'emoji-btn-active' : 'emoji-btn'"
        @click="chooseEmoji = !chooseEmoji"
      >
        <i class="iconfont my-icon-expression" />
      </span>
      <div style="margin-left:auto">
        <button @click="cancelReply" class="cancel-btn v-comment-btn">
          取消
        </button>
        <button @click="addReply" class="upload-btn v-comment-btn">
          提交
        </button>
      </div>
    </div>
    <emoji @addEmoji="addEmoji" :chooseEmoji="chooseEmoji"></emoji>
  </div>
</template>

<script>
import Emoji from "./Emoji";
import EmojiList from "../assets/js/emoji";
export default {
  components: {
    Emoji
  },
  data: function() {
    return {
      index: 0,
      chooseEmoji: false,
      layer: false,
      nickname: "",
      replyId: null,
      parentId: null,
      commentContent: ""
    };
  },
  methods: {
    cancelReply() {
      this.$refs.reply.style.display = "none";
    },
    addReply() {
      if (this.$store.state.userId == null) {
        this.$store.state.loginFlag = true;
        return false;
      }
      if (this.commentContent.trim() === "") {
        this.$toast({ type: "error", message: "回复不能为空" });
        return false;
      }
      const reg = /\[.+?]/g;
      let content = this.commentContent;
      content = content.replace(reg, function(str) {
        return (
          "<img src= '" +
          EmojiList[str] +
          "' width='20' height='20' style='padding: 0 1px' alt=''/>"
        );
      });
      const path = this.$route.path;
      const arr = path.split("/");
      let comment = {
        parentId: this.parentId,
        articleId: arr[2],
        commentContent: content
      };
      if (!this.layer) {
        comment.replyId = this.replyId;
      }
      this.commentContent = "";
      this.axios.post("/api/comment", comment).then(({ data }) => {
        if (data.flag) {
          this.$emit("reloadCommentsReply", this.index);
          this.$toast({ type: "success", message: "回复成功" });
        } else {
          this.$toast({ type: "error", message: data.message });
        }
      });
    },
    addEmoji(text) {
      this.commentContent += text;
    }
  }
};
</script>

<style scoped>
.reply-input-wrapper {
  border: 1px solid #90939950;
  border-radius: 4px;
  padding: 10px;
  margin: 0 0 10px;
}
</style>
