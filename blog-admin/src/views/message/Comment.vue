<template>
  <el-card class="main-card">
    <div class="title">{{ this.$route.name }}</div>
    <div class="operation-container">
      <el-button
        v-if="!garbageFlag"
        type="danger"
        size="small"
        icon="el-icon-minus"
        :disabled="commentIdList.length === 0"
        @click="updateGarbageFlag = true"
      >
        批量删除
      </el-button>
      <el-button
        v-else
        type="danger"
        size="small"
        icon="el-icon-minus"
        :disabled="commentIdList.length === 0"
        @click="remove = true"
      >
        批量删除
      </el-button>
      <div style="margin-left:auto">
        <el-select
          v-model="userId"
          placeholder="请选择用户"
          size="small"
          style="margin-right:1rem"
          clearable
          filterable
          v-if="checkWeight()"
        >
          <el-option
            v-for="item in usernameList"
            :key="item.userId"
            :label="item.username"
            :value="item.userId"
          />
        </el-select>
        <el-select
          v-model="garbageFlag"
          placeholder="请选择"
          size="small"
          style="margin-right:1rem"
        >
          <el-option
            v-for="item in options"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
        <el-input
          v-model="keywords"
          prefix-icon="el-icon-search"
          size="small"
          placeholder="请输入文章标题"
          style="width:200px"
          @keyup.enter.native="listComments"
        />
        <el-button
          type="primary"
          size="small"
          icon="el-icon-search"
          style="margin-left:1rem"
          @click="listComments"
        >
          搜索
        </el-button>
      </div>
    </div>
    <el-table
      border
      :data="commentList"
      @selection-change="selectionChange"
      v-loading="loading"
    >
      <el-table-column type="selection" width="55" />
      <el-table-column prop="avatar" label="头像" align="center" width="120">
        <template slot-scope="scope">
          <img :src="scope.row.avatar" width="40" height="40" />
        </template>
      </el-table-column>
      <el-table-column
        prop="nickname"
        label="评论人"
        align="center"
        width="120"
      />
      <el-table-column
        prop="replyNickname"
        label="回复人"
        align="center"
        width="120"
      >
        <template slot-scope="scope">
          <span v-if="scope.row.replyNickname">
            {{ scope.row.replyNickname }}
          </span>
          <span v-else>无</span>
        </template>
      </el-table-column>
      <el-table-column prop="articleTitle" label="文章标题" align="center">
        <template slot-scope="scope">
          <span v-if="scope.row.articleTitle">
            {{ scope.row.articleTitle }}
          </span>
          <span v-else>无</span>
        </template>
      </el-table-column>
      <el-table-column prop="commentContent" label="评论内容" align="center">
        <template slot-scope="scope">
          <span v-html="scope.row.commentContent" class="comment-content" />
        </template>
      </el-table-column>
      <el-table-column
        prop="likeCount"
        label="点赞量"
        width="80"
        align="center"
      >
        <template slot-scope="scope">
          <span v-if="scope.row.likeCount">
            {{ scope.row.likeCount }}
          </span>
          <span v-else>0</span>
        </template>
      </el-table-column>
      <el-table-column
        prop="createTime"
        label="评论时间"
        width="200"
        align="center"
      >
        <template slot-scope="scope">
          <i class="el-icon-time" style="margin-right:5px" />
          {{ scope.row.createTime | dateTime }}
        </template>
      </el-table-column>
      <el-table-column label="来源" align="center" width="100">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.articleTitle">文章</el-tag>
          <el-tag v-else type="warning">友链</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="160" align="center">
        <template slot-scope="scope">
          <el-popconfirm
            v-if="!scope.row.garbageFlag"
            title="确定删除吗？"
            @confirm="updateCommentGarbageFlag(scope.row.id)"
          >
            <el-button size="mini" type="danger" slot="reference">
              删除
            </el-button>
          </el-popconfirm>
          <el-popconfirm
            v-if="scope.row.garbageFlag"
            title="确定恢复吗？"
            @confirm="updateCommentGarbageFlag(scope.row.id)"
          >
            <el-button size="mini" type="success" slot="reference">
              恢复
            </el-button>
          </el-popconfirm>
          <el-popconfirm
            style="margin-left:10px"
            v-if="scope.row.garbageFlag"
            title="确定彻底删除吗？"
            @confirm="deleteComments(scope.row.id)"
          >
            <el-button size="mini" type="danger" slot="reference">
              删除
            </el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      class="pagination-container"
      background
      @size-change="sizeChange"
      @current-change="currentChange"
      :current-page="current"
      :page-size="size"
      :total="count"
      :page-sizes="[10, 20]"
      layout="total, sizes, prev, pager, next, jumper"
    />
    <el-dialog :visible.sync="updateGarbageFlag" width="30%">
      <div class="dialog-title-container" slot="title">
        <i class="el-icon-warning" style="color:#ff9900" />提示
      </div>
      <div style="font-size:1rem">是否删除选中项？</div>
      <div slot="footer">
        <el-button @click="updateGarbageFlag = false">取 消</el-button>
        <el-button type="primary" @click="updateCommentGarbageFlag(null)">
          确 定
        </el-button>
      </div>
    </el-dialog>
    <el-dialog :visible.sync="remove" width="30%">
      <div class="dialog-title-container" slot="title">
        <i class="el-icon-warning" style="color:#ff9900" />提示
      </div>
      <div style="font-size:1rem">是否彻底删除选中项？</div>
      <div slot="footer">
        <el-button @click="remove = false">取 消</el-button>
        <el-button type="primary" @click="deleteComments(null)">
          确 定
        </el-button>
      </div>
    </el-dialog>
  </el-card>
</template>

<script>
export default {
  created() {
    this.userId = this.$store.state.userId;
    this.listComments();
    this.listAllUsername();
  },
  data: function() {
    return {
      options: [
        {
          value: false,
          label: "已发表"
        },
        {
          value: true,
          label: "回收站"
        }
      ],
      commentList: [],
      usernameList: [],
      commentIdList: [],
      userId: null,
      keywords: null,
      remove: false,
      loading: true,
      garbageFlag: false,
      updateGarbageFlag: false,
      size: 10,
      count: 0,
      current: 1
    };
  },
  methods: {
    listAllUsername() {
      if (this.checkWeight()) {
        this.axios.get("/api/back/user/username").then(({ data }) => {
          this.usernameList = data.data;
        });
      }
    },
    selectionChange(commentList) {
      this.commentIdList = [];
      commentList.forEach(item => {
        this.commentIdList.push(item.id);
      });
    },
    sizeChange(size) {
      this.size = size;
      this.listComments();
    },
    currentChange(current) {
      this.current = current;
      this.listComments();
    },
    updateCommentGarbageFlag(id) {
      let param = new URLSearchParams();
      if (id != null) {
        param.append("idList", [id]);
      } else {
        param.append("idList", this.commentIdList);
      }
      param.append("garbageFlag", !this.garbageFlag);
      this.axios.put("/api/back/comments", param).then(({ data }) => {
        if (data.flag) {
          this.$notify.success({
            title: "成功",
            message: data.message
          });
          this.listComments();
        } else {
          this.$notify.error({
            title: "失败",
            message: data.message
          });
        }
        this.updateGarbageFlag = false;
      });
    },
    deleteComments(id) {
      var param = {};
      if (id == null) {
        param = { data: this.commentIdList };
      } else {
        param = { data: [id] };
      }
      this.axios.delete("/api/back/comments", param).then(({ data }) => {
        if (data.flag) {
          this.$notify.success({
            title: "成功",
            message: data.message
          });
          this.listComments();
        } else {
          this.$notify.error({
            title: "失败",
            message: data.message
          });
        }
        this.remove = false;
      });
    },
    listComments() {
      this.axios
        .get("/api/back/comments", {
          params: {
            size: this.size,
            userId: this.userId,
            current: this.current,
            keywords: this.keywords,
            garbageFlag: this.garbageFlag
          }
        })
        .then(({ data }) => {
          this.count = data.data.count;
          this.commentList = data.data.pageList;
          this.loading = false;
        });
    },
    checkWeight() {
      return this.$store.state.weight <= 300;
    }
  },
  watch: {
    garbageFlag() {
      this.listComments();
    },
    userId(newVal, oldVal) {
      if (oldVal != null) {
        this.listComments();
      }
    }
  }
};
</script>

<style scoped>
.comment-content {
  display: inline-block;
}
</style>
