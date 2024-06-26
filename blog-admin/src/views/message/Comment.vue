<template>
  <el-card class="main-card">
    <div class="title">{{ this.$route.name }}</div>
    <div class="operation-container">
      <el-button
        v-if="type !== 7"
        :disabled="commentIdList.length === 0"
        type="danger"
        size="small"
        icon="el-icon-minus"
        @click="editStatus = true"
      >
        批量删除
      </el-button>
      <el-button
        v-else
        :disabled="commentIdList.length === 0"
        type="danger"
        size="small"
        icon="el-icon-minus"
        @click="removeStatus = true"
      >
        批量删除
      </el-button>
      <div style="margin-left:auto">
        <el-select
          v-if="checkWeight(400)"
          v-model="userId"
          size="small"
          style="margin-right:1rem"
          placeholder="请选择用户"
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
        <el-select
          v-if="checkWeight(200)"
          v-model="flag"
          size="small"
          style="margin-right:1rem"
          placeholder="请选择来源"
        >
          <el-option
            v-for="item in source"
            :key="item.value"
            :value="item.value"
            :label="item.label"
          />
        </el-select>
        <el-select
          v-if="checkWeight(300)"
          v-model="type"
          size="small"
          style="margin-right:1rem"
          placeholder="请选择"
        >
          <el-option
            v-for="item in options"
            :key="item.value"
            :value="item.value"
            :label="item.label"
          />
        </el-select>
        <el-input
          v-model="keywords"
          :disabled="flag"
          ref="input"
          size="small"
          style="width: 200px"
          prefix-icon="el-icon-search"
          placeholder="请输入文章标题"
          clearable
          @keyup.enter.native="getComments(false)"
        />
        <el-button
          :disabled="flag"
          type="primary"
          size="small"
          icon="el-icon-search"
          style="margin-left:1rem"
          @click="getComments(false)"
        >
          搜索
        </el-button>
      </div>
    </div>
    <el-table
      v-loading="loading"
      :data="commentList"
      border
      @selection-change="selectionChange"
    >
      <el-table-column type="selection" align="center" width="40" />
      <el-table-column
        v-if="checkWeight(400) && showColumnConfig.username"
        prop="username"
        label="用户"
        align="center"
        min-width="120"
      />
      <el-table-column
        v-if="showColumnConfig.avatar"
        prop="avatar"
        label="头像"
        align="center"
        width="80"
      >
        <template slot-scope="scope">
          <el-image
            :src="scope.row.avatar === '' ? defaultAvatar : scope.row.avatar"
            style="width: 40px;height: 40px;"
            :preview-src-list="[
              scope.row.avatar === '' ? defaultAvatar : scope.row.avatar
            ]"
          />
        </template>
      </el-table-column>
      <el-table-column
        v-if="showColumnConfig.nickname"
        prop="nickname"
        label="评论人"
        align="center"
        min-width="120"
      />
      <el-table-column
        v-if="showColumnConfig.replyNickname"
        prop="replyNickname"
        label="回复人"
        align="center"
        min-width="120"
      >
        <template slot-scope="scope">
          <span v-if="scope.row.replyNickname">
            {{ scope.row.replyNickname }}
          </span>
        </template>
      </el-table-column>
      <el-table-column
        v-if="showColumnConfig.articleTitle"
        prop="articleTitle"
        label="文章标题"
        align="center"
        min-width="120"
      >
        <template slot-scope="scope">
          <span v-if="scope.row.articleTitle">
            {{ scope.row.articleTitle }}
          </span>
        </template>
      </el-table-column>
      <el-table-column
        v-if="showColumnConfig.commentContent"
        prop="commentContent"
        label="评论内容"
        align="center"
        min-width="240"
      >
        <template slot-scope="scope">
          <span v-html="scope.row.commentContent" class="comment-content" />
        </template>
      </el-table-column>
      <el-table-column
        v-if="showColumnConfig.likeCount"
        prop="likeCount"
        label="点赞量"
        align="center"
        width="80"
      >
        <template slot-scope="scope">
          <span v-if="scope.row.likeCount">
            {{ scope.row.likeCount }}
          </span>
          <span v-else>0</span>
        </template>
      </el-table-column>
      <el-table-column
        v-if="showColumnConfig.createTime"
        prop="createTime"
        label="评论时间"
        align="center"
        width="200"
      >
        <template slot-scope="scope">
          <i class="el-icon-time" style="margin-right:5px" />
          {{ scope.row.createTime | dateTime }}
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="160">
        <template slot="header">
          <el-popover placement="bottom" title="选择显示列" width="160">
            <div>
              <el-checkbox v-model="showColumnConfig.username"
                >用户</el-checkbox
              >
              <el-checkbox v-model="showColumnConfig.avatar">头像</el-checkbox>
              <el-checkbox v-model="showColumnConfig.nickname"
                >评论人</el-checkbox
              >
              <el-checkbox v-model="showColumnConfig.replyNickname"
                >回复人</el-checkbox
              >
              <el-checkbox v-model="showColumnConfig.articleTitle"
                >文章标题</el-checkbox
              >
              <el-checkbox v-model="showColumnConfig.commentContent"
                >评论内容</el-checkbox
              >
              <el-checkbox v-model="showColumnConfig.likeCount"
                >点赞量</el-checkbox
              >
              <el-checkbox v-model="showColumnConfig.createTime"
                >评论时间</el-checkbox
              >
              <div>
                <el-button
                  type="primary"
                  size="mini"
                  style="float: right"
                  plain
                  @click="saveColumnConfig"
                >
                  保存
                </el-button>
              </div>
            </div>
            <i slot="reference" class="el-icon-setting table-setting-icon"></i>
          </el-popover>
        </template>
        <template slot-scope="scope">
          <el-popconfirm
            v-if="checkWeight(300)"
            title="确定恢复吗？"
            @confirm="updateCommentsStatus(scope.row.id, type === 6)"
          >
            <el-button
              :disabled="type == null"
              type="success"
              size="mini"
              slot="reference"
              class="smaller-btn"
            >
              <i class="el-icon-refresh-left" /> 恢复
            </el-button>
          </el-popconfirm>
          <el-popconfirm
            v-if="type !== 7"
            title="确定删除吗？"
            style="margin-left:10px"
            @confirm="updateCommentsStatus(scope.row.id)"
          >
            <el-button
              type="danger"
              size="mini"
              slot="reference"
              class="smaller-btn"
            >
              <i class="el-icon-delete" /> 删除
            </el-button>
          </el-popconfirm>
          <el-popconfirm
            v-else
            title="确定彻底删除吗？"
            style="margin-left:10px"
            @confirm="deleteComments(scope.row.id)"
          >
            <el-button
              type="danger"
              size="mini"
              slot="reference"
              class="smaller-btn"
            >
              <i class="el-icon-delete" /> 删除
            </el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      :total="count"
      :page-size="size"
      :page-sizes="[10, 20]"
      :current-page.sync="current"
      class="pagination-container"
      layout="total, sizes, prev, pager, next, jumper"
      background
      @size-change="sizeChange"
      @current-change="currentChange"
    />
    <el-dialog :visible.sync="editStatus" width="30%">
      <div class="dialog-title-container" slot="title">
        <i class="el-icon-warning" style="color:#ff9900" />提示
      </div>
      <div style="font-size:1rem">是否删除选中项？</div>
      <div slot="footer">
        <el-button @click="editStatus = false">取 消</el-button>
        <el-button type="primary" @click="updateCommentsStatus(null)">
          确 定
        </el-button>
      </div>
    </el-dialog>
    <el-dialog :visible.sync="removeStatus" width="30%">
      <div class="dialog-title-container" slot="title">
        <i class="el-icon-warning" style="color:#ff9900" />提示
      </div>
      <div style="font-size:1rem">是否彻底删除选中项？</div>
      <div slot="footer">
        <el-button @click="removeStatus = false">取 消</el-button>
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
    this.loadColumnConfig();
    this.getComments();
    if (this.checkWeight(100)) {
      this.options.push({
        value: 7,
        label: "已删除"
      });
    }
    this.$nextTick(() => {
      this.$refs.input.focus();
    });
  },
  data: function() {
    return {
      source: [
        {
          value: null,
          label: "文章"
        },
        {
          value: true,
          label: "友链"
        }
      ],
      options: [
        {
          value: null,
          label: "已发表"
        },
        {
          value: 6,
          label: "回收站"
        }
      ],
      commentList: [],
      usernameList: [],
      commentIdList: [],
      showColumnConfig: {},
      flag: null,
      type: null,
      userId: null,
      keywords: null,
      oldKeywords: null,
      loading: true,
      editStatus: false,
      removeStatus: false,
      size: 10,
      count: 0,
      current: 1,
      defaultAvatar: process.env.VUE_APP_STATIC_URL + "img/avatar.png"
    };
  },
  methods: {
    sizeChange(size) {
      this.size = size;
      this.getComments(true);
    },
    checkWeight(weight) {
      return this.$store.state.weight <= weight;
    },
    currentChange(current) {
      this.current = current;
      this.getComments();
    },
    selectionChange(selection) {
      this.commentIdList = [];
      selection.forEach(item => {
        this.commentIdList.push(item.id);
      });
    },
    saveColumnConfig() {
      localStorage.setItem(
        "CommentColumnSet",
        JSON.stringify(this.showColumnConfig)
      );
      document.body.click();
    },
    loadColumnConfig() {
      if (localStorage.getItem("CommentColumnSet")) {
        this.showColumnConfig = JSON.parse(
          localStorage.getItem("CommentColumnSet")
        );
      } else {
        this.showColumnConfig = {
          username: true,
          avatar: true,
          nickname: true,
          replyNickname: true,
          articleTitle: true,
          commentContent: true,
          likeCount: true,
          createTime: true
        };
      }
    },
    getComments(resetCurrentPage) {
      if (resetCurrentPage || this.keywords !== this.oldKeywords) {
        this.current = 1;
        this.oldKeywords = this.keywords;
      }
      let params = {
        flag: this.flag,
        size: this.size,
        type: this.type,
        userId: this.userId,
        current: this.current,
        keywords: this.keywords
      };
      params = this.$commonMethod.skipEmptyValue(params);
      this.axios
        .get("/api/back/comments", {
          params
        })
        .then(({ data }) => {
          this.count = data.data.count;
          this.commentList = data.data.pageList;
          this.loading = false;
        });
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
    deleteComments(id) {
      let param = {};
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
          if (param.data.length === this.commentList.length) {
            this.current = --this.current > 1 ? this.current : 1;
          }
          this.getComments();
        } else {
          this.$notify.error({
            title: "失败",
            message: data.message
          });
        }
      });
      this.removeStatus = false;
    },
    updateCommentsStatus(id, isRec = false) {
      let param = {};
      if (id != null) {
        param.idList = [id];
      } else {
        param.idList = this.commentIdList;
      }
      if (this.type != null) {
        param.type = this.type;
      }
      if (isRec) {
        param.status = true;
      }
      this.axios.put("/api/back/comments/status", param).then(({ data }) => {
        if (data.flag) {
          this.$notify.success({
            title: "成功",
            message: data.message
          });
          if (param.idList.length === this.commentList.length) {
            this.current = --this.current > 1 ? this.current : 1;
          }
          this.getComments();
        } else {
          this.$notify.error({
            title: "失败",
            message: data.message
          });
        }
      });
      this.editStatus = false;
    }
  },
  watch: {
    flag() {
      this.getComments(true);
    },
    type() {
      this.getComments(true);
    },
    userId() {
      this.getComments(true);
    }
  }
};
</script>

<style scoped>
.comment-content {
  display: inline-block;
}
</style>
