<template>
  <el-card class="main-card">
    <div class="title">
      {{ isEn ? this.$route.meta.nameEn : this.$route.name }}
    </div>
    <div class="operation-container">
      <el-button
        v-if="type !== 7"
        :disabled="commentIdList.length === 0"
        type="danger"
        size="small"
        icon="el-icon-minus"
        @click="editStatus = true"
      >
        {{ $t("button.batchDelete") }}
      </el-button>
      <el-button
        v-else
        :disabled="commentIdList.length === 0"
        type="danger"
        size="small"
        icon="el-icon-minus"
        @click="removeStatus = true"
      >
        {{ $t("button.batchDelete") }}
      </el-button>
      <div style="margin-left:auto">
        <el-select
          v-if="checkWeight(400)"
          v-model="userId"
          size="small"
          style="margin-right:1rem"
          :placeholder="$t('input.selectUser')"
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
          :placeholder="$t('input.selectSource')"
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
          :placeholder="$t('input.select')"
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
          :placeholder="$t('article.inputTitle')"
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
          {{ $t("button.search") }}
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
        :label="$t('table.user')"
        align="center"
        min-width="120"
      />
      <el-table-column
        v-if="showColumnConfig.avatar"
        prop="avatar"
        :label="$t('table.avatar')"
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
        :label="$t('comment.from')"
        align="center"
        min-width="120"
      />
      <el-table-column
        v-if="showColumnConfig.replyNickname"
        prop="replyNickname"
        :label="$t('comment.to')"
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
        :label="$t('article.articleTitle')"
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
        :label="$t('comment.content')"
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
        :label="$t('article.likeCount')"
        align="center"
        width="120"
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
        :label="$t('comment.createTime')"
        align="center"
        width="200"
      >
        <template slot-scope="scope">
          <i class="el-icon-time" style="margin-right:5px" />
          {{ scope.row.createTime | dateTime }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('table.operate')" align="center" width="160">
        <template slot="header">
          <el-popover placement="bottom" width="160">
            <div>
              <el-checkbox
                :indeterminate="
                  columnCheckedCount > 0 && columnCheckedCount < columnCount
                "
                :value="columnCheckedCount === columnCount"
                @change="handleColumnCheckedAllChange"
                >{{ $t("table.showColumn") }}</el-checkbox
              >
              <el-divider></el-divider>
              <el-checkbox
                v-if="checkWeight(400)"
                v-model="showColumnConfig.username"
                @change="handleColumnCheckedChange"
                >{{ $t("table.user") }}</el-checkbox
              >
              <el-checkbox
                v-model="showColumnConfig.avatar"
                @change="handleColumnCheckedChange"
                >{{ $t("table.avatar") }}</el-checkbox
              >
              <el-checkbox
                v-model="showColumnConfig.nickname"
                @change="handleColumnCheckedChange"
                >{{ $t("comment.from") }}</el-checkbox
              >
              <el-checkbox
                v-model="showColumnConfig.replyNickname"
                @change="handleColumnCheckedChange"
                >{{ $t("comment.to") }}</el-checkbox
              >
              <el-checkbox
                v-model="showColumnConfig.articleTitle"
                @change="handleColumnCheckedChange"
                >{{ $t("article.articleTitle") }}</el-checkbox
              >
              <el-checkbox
                v-model="showColumnConfig.commentContent"
                @change="handleColumnCheckedChange"
                >{{ $t("comment.content") }}</el-checkbox
              >
              <el-checkbox
                v-model="showColumnConfig.likeCount"
                @change="handleColumnCheckedChange"
                >{{ $t("article.likeCount") }}</el-checkbox
              >
              <el-checkbox
                v-model="showColumnConfig.createTime"
                @change="handleColumnCheckedChange"
                >{{ $t("comment.createTime") }}</el-checkbox
              >
              <div>
                <el-button
                  type="primary"
                  size="mini"
                  style="float: right"
                  plain
                  @click="saveColumnConfig"
                >
                  {{ $t("button.save") }}
                </el-button>
              </div>
            </div>
            <i slot="reference" class="el-icon-setting table-setting-icon"></i>
          </el-popover>
          <el-tooltip
            class="item"
            effect="dark"
            :content="$t('table.refresh')"
            placement="top"
          >
            <i
              class="el-icon-refresh table-refresh-icon"
              @click="getComments(false)"
            ></i>
          </el-tooltip>
        </template>
        <template slot-scope="scope">
          <el-popconfirm
            v-if="checkWeight(300)"
            :title="$t('confirm.content2')"
            @confirm="updateCommentsStatus(scope.row.id, type === 6)"
          >
            <el-button
              :disabled="type == null"
              type="success"
              size="mini"
              slot="reference"
              class="smaller-btn"
            >
              <i class="el-icon-refresh-left" /> {{ $t("button.restore") }}
            </el-button>
          </el-popconfirm>
          <el-popconfirm
            v-if="type !== 7"
            :title="$t('confirm.content3')"
            style="margin-left:10px"
            @confirm="updateCommentsStatus(scope.row.id)"
          >
            <el-button
              type="danger"
              size="mini"
              slot="reference"
              class="smaller-btn"
            >
              <i class="el-icon-delete" /> {{ $t("button.delete") }}
            </el-button>
          </el-popconfirm>
          <el-popconfirm
            v-else
            :title="$t('confirm.content4')"
            style="margin-left:10px"
            @confirm="deleteComments(scope.row.id)"
          >
            <el-button
              type="danger"
              size="mini"
              slot="reference"
              class="smaller-btn"
            >
              <i class="el-icon-delete" /> {{ $t("button.delete") }}
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
        <i class="el-icon-warning" style="color:#ff9900" />{{
          $t("confirm.tip")
        }}
      </div>
      <div style="font-size:1rem">{{ $t("confirm.content5") }}</div>
      <div slot="footer">
        <el-button @click="editStatus = false">{{
          $t("confirm.no")
        }}</el-button>
        <el-button type="primary" @click="updateCommentsStatus(null)">
          {{ $t("confirm.yes") }}
        </el-button>
      </div>
    </el-dialog>
    <el-dialog :visible.sync="removeStatus" width="30%">
      <div class="dialog-title-container" slot="title">
        <i class="el-icon-warning" style="color:#ff9900" />{{
          $t("confirm.tip")
        }}
      </div>
      <div style="font-size:1rem">{{ $t("confirm.content6") }}</div>
      <div slot="footer">
        <el-button @click="removeStatus = false">{{
          $t("confirm.no")
        }}</el-button>
        <el-button type="primary" @click="deleteComments(null)">
          {{ $t("confirm.yes") }}
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
    this.$nextTick(() => {
      this.$refs.input.focus();
    });
  },
  data: function() {
    return {
      source: [],
      options: [],
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
      columnCount: 8,
      columnCheckedCount: 0,
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
    handleColumnCheckedChange(value) {
      if (value) {
        this.columnCheckedCount++;
      } else {
        this.columnCheckedCount--;
      }
    },
    handleColumnCheckedAllChange(value) {
      if (value) {
        this.initColumnConfig();
      } else {
        this.showColumnConfig = {
          username: false,
          avatar: false,
          nickname: false,
          replyNickname: false,
          articleTitle: false,
          commentContent: false,
          likeCount: false,
          createTime: false
        };
        this.columnCheckedCount = 0;
      }
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
        this.columnCheckedCount = Object.values(this.showColumnConfig).reduce(
          (count, value) => {
            return value ? ++count : count;
          },
          0
        );
      } else {
        this.initColumnConfig();
      }
    },
    initColumnConfig() {
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
      this.columnCheckedCount = 8;
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
            title: this.$t("success"),
            message: data.message
          });
          if (param.data.length === this.commentList.length) {
            this.current = --this.current > 1 ? this.current : 1;
          }
          this.getComments();
        } else {
          this.$notify.error({
            title: this.$t("failure"),
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
            title: this.$t("success"),
            message: data.message
          });
          if (param.idList.length === this.commentList.length) {
            this.current = --this.current > 1 ? this.current : 1;
          }
          this.getComments();
        } else {
          this.$notify.error({
            title: this.$t("failure"),
            message: data.message
          });
        }
      });
      this.editStatus = false;
    }
  },
  computed: {
    isEn() {
      return this.$i18n.locale === "en_US";
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
    },
    isEn: {
      handler() {
        this.source = [
          {
            value: null,
            label: this.$t("option.article")
          },
          {
            value: true,
            label: this.$t("option.friendLink")
          }
        ];
        this.options = [
          {
            value: null,
            label: this.$t("option.published")
          },
          {
            value: 6,
            label: this.$t("option.bin")
          }
        ];
        if (this.checkWeight(100)) {
          this.options.push({
            value: 7,
            label: this.$t("option.deleted")
          });
        }
      },
      immediate: true
    }
  }
};
</script>

<style scoped>
.comment-content {
  display: inline-block;
}
</style>
