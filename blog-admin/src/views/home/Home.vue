<template>
  <el-card class="main-card">
    <el-tabs tab-position="left" style="height:calc(100vh - 180px);">
      <el-tab-pane>
        <span slot="label" style="font-weight: bold;"
          ><i class="el-icon-s-comment"></i> 回复我的</span
        >
        <el-card v-for="(item, index) of commentList" :key="item.id">
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
                <span> 对我的文章发表了{{ item.commentCount }}条评论</span>
              </a>
            </div>
            <a
              :href="homeURL + '/' + userId + '/article/' + item.articleId"
              style="text-decoration: none;color: inherit;"
              target="_blank"
            >
              <p v-html="item.commentContent" class="comment-content"></p>
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
                v-if="isLike(item.id)"
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
                @click="like(item)"
                ><i class="el-icon-delete"></i>删除该通知</span
              >
            </div>
            <Reply ref="reply" />
          </div>
        </el-card>
      </el-tab-pane>
      <el-tab-pane>
        <span slot="label" style="font-weight: bold;"
          ><i class="el-icon-s-comment"></i> &nbsp;@ 我的</span
        >
        我的行程
      </el-tab-pane>
      <el-tab-pane>
        <span slot="label" style="font-weight: bold;"
          ><i class="el-icon-star-on"></i> 收到的赞</span
        >
        我的行程
      </el-tab-pane>
      <el-tab-pane>
        <span slot="label" style="font-weight: bold;"
          ><i class="el-icon-message-solid"></i> 系统通知</span
        >
        我的行程
      </el-tab-pane>
      <el-tab-pane>
        <span slot="label" style="font-weight: bold;"
          ><i class="el-icon-message-solid"></i> 我的消息</span
        >
        我的行程
      </el-tab-pane>
      <el-tab-pane>
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
    this.getData();
  },
  data: function() {
    return {
      commentList: [
        {
          id: 9,
          userId: 2,
          articleId: 1,
          commentContent:
            "<img src= 'http://192.168.143.130/static/img/emoji/liuliuliu.png' width='20' height='20' style='padding: 0 1px' alt='' /><img src= 'http://192.168.143.130/static/img/emoji/emm.png' width='20' height='20' style='padding: 0 1px' alt='' /><img src= 'http://192.168.143.130/static/img/emoji/ok.png' width='20' height='20' style='padding: 0 1px' alt='' />",
          createTime: "2024-06-01T13:59:52.000+00:00",
          avatar:
            "http://192.168.143.130/static/2/-1/-11/1792506667906437121.png",
          website: "",
          nickname: "root",
          likeCount: null,
          replyCount: 0,
          commentCount: 2,
          articleCover: "http://192.168.143.130/static/img/emoji/liuliuliu.png",
          articleTitle: "123321"
        },
        {
          id: 3,
          userId: 2,
          articleId: 1,
          commentContent: "gggg",
          createTime: "2024-06-01T08:36:47.000+00:00",
          avatar:
            "http://192.168.143.130/static/2/-1/-11/1792506667906437121.png",
          website: "",
          nickname: "root",
          commentCount: 2,
          articleCover: "",
          articleTitle: "123321"
        },
        {
          id: 2,
          userId: 3,
          articleId: 1,
          commentContent:
            "hhhhhh<img src= '/emoji/zhoumei.png' width='20' height='20' style='padding: 0 1px' alt='' /><img src= '/emoji/liekai.png' width='20' height='20' style='padding: 0 1px' alt='' />",
          createTime: "2024-06-01T08:35:44.000+00:00",
          avatar: "",
          website: "",
          nickname: "admin",
          likeCount: null,
          replyCount: 5,
          commentCount: 2,
          articleCover: "",
          articleTitle: "123321"
        },
        {
          id: 1,
          userId: 3,
          articleId: 1,
          commentContent:
            "hhha<img src= '/emoji/daku.png' width='20' height='20' style='padding: 0 1px' alt='' />",
          createTime: "2024-06-01T08:34:58.000+00:00",
          avatar: "",
          website: "",
          nickname: "admin",
          likeCount: 1,
          replyCount: 0,
          commentCount: 2,
          articleCover: "http://192.168.143.130/static/img/emoji/liuliuliu.png",
          articleTitle: "123321"
        }
      ],
      defaultAvatar: process.env.VUE_APP_STATIC_URL + "img/avatar.png",
      defaultArticleCover: process.env.VUE_APP_STATIC_URL + "img/article.jpg",
      homeURL: process.env.VUE_APP_HOME_URL
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
      this.$refs.reply[index].layer = item.parentId == null;
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
      if (this.$store.state.userId == null) {
        this.$store.state.loginFlag = true;
        return false;
      }
      this.axios.post("/api/comment/like/" + comment.id).then(({ data }) => {
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
    getData() {
      this.axios.get("/api/back/blog/home").then();
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

<style scoped></style>
