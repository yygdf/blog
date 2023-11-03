<template>
  <el-card class="main-card">
    <div class="title">{{ this.$route.name }}</div>
    <div class="operation-container">
      <el-button
        type="primary"
        size="small"
        icon="el-icon-plus"
        @click="addArticle"
      >
        新增
      </el-button>
      <el-button
        v-if="type !== 7"
        :disabled="articleIdList.length === 0"
        type="danger"
        size="small"
        icon="el-icon-minus"
        @click="editStatus = true"
      >
        批量删除
      </el-button>
      <el-button
        v-else
        :disabled="articleIdList.length === 0"
        type="danger"
        size="small"
        icon="el-icon-minus"
        @click="removeStatus = true"
      >
        批量删除
      </el-button>
      <div style="margin-left:auto">
        <el-select
          v-if="checkWeight(300)"
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
          v-model="categoryId"
          size="small"
          style="margin-right:1rem"
          placeholder="请选择分类"
          clearable
          filterable
        >
          <el-option
            v-for="item in categoryList"
            :key="item.id"
            :value="item.id"
            :label="item.label"
          />
        </el-select>
        <el-select
          v-model="tagIdList"
          size="small"
          style="margin-right:1rem"
          placeholder="请选择标签"
          multiple
          clearable
          filterable
        >
          <el-option
            v-for="item in tagList"
            :key="item.id"
            :value="item.id"
            :label="item.label"
          />
        </el-select>
        <el-select
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
          ref="input"
          size="small"
          style="width: 200px"
          prefix-icon="el-icon-search"
          placeholder="请输入文章名"
          clearable
          @keyup.enter.native="getArticles(false)"
        />
        <el-button
          type="primary"
          size="small"
          icon="el-icon-search"
          style="margin-left:1rem"
          @click="getArticles(false)"
        >
          搜索
        </el-button>
      </div>
    </div>
    <el-table
      v-loading="loading"
      :data="articleList"
      border
      @selection-change="selectionChange"
    >
      <el-table-column type="selection" align="center" width="40" />
      <el-table-column
        v-if="checkWeight(300)"
        prop="username"
        label="用户"
        align="center"
        width="120"
      />
      <el-table-column
        prop="articleTitle"
        label="标题"
        align="center"
        width="120"
      />
      <el-table-column
        prop="categoryName"
        label="分类"
        align="center"
        width="120"
      />
      <el-table-column prop="tagNameList" label="标签" align="center">
        <template slot-scope="scope">
          <el-tag
            v-for="item of scope.row.tagNameList == null
              ? []
              : scope.row.tagNameList.split(',')"
            :key="item"
            style="margin-right:0.2rem;margin-top:0.2rem"
          >
            {{ item }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column
        prop="viewCount"
        label="浏览量"
        align="center"
        width="80"
      >
        <template slot-scope="scope">
          <span v-if="scope.row.viewCount">
            {{ scope.row.viewCount }}
          </span>
          <span v-else>0</span>
        </template>
      </el-table-column>
      <el-table-column
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
        prop="publishTime"
        label="发表日期"
        align="center"
        width="120"
      >
        <template slot-scope="scope" v-if="scope.row.publishTime">
          <i class="el-icon-time" style="margin-right:5px" />
          {{ scope.row.publishTime | date }}
        </template>
      </el-table-column>
      <el-table-column
        prop="updateTime"
        label="更新日期"
        align="center"
        width="120"
      >
        <template slot-scope="scope" v-if="scope.row.updateTime">
          <i class="el-icon-time" style="margin-right:5px" />
          {{ scope.row.updateTime | date }}
        </template>
      </el-table-column>
      <el-table-column prop="topFlag" label="置顶" align="center" width="80">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.topFlag"
            :disabled="type != null"
            :active-value="true"
            :inactive-value="false"
            active-color="#13ce66"
            inactive-color="#F4F4F5"
            @change="updateArticleStatus(scope.row)"
          />
        </template>
      </el-table-column>
      <el-table-column prop="publicFlag" label="公开" align="center" width="80">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.publicFlag"
            :disabled="type != null"
            :active-value="true"
            :inactive-value="false"
            active-color="#13ce66"
            inactive-color="#F4F4F5"
            @change="updateArticleStatus(scope.row, 2)"
          />
        </template>
      </el-table-column>
      <el-table-column prop="hiddenFlag" label="隐藏" align="center" width="80">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.hiddenFlag"
            :disabled="type != null"
            :active-value="true"
            :inactive-value="false"
            active-color="#13ce66"
            inactive-color="#F4F4F5"
            @change="updateArticleStatus(scope.row, 3)"
          />
        </template>
      </el-table-column>
      <el-table-column
        prop="commentableFlag"
        label="可评论"
        align="center"
        width="80"
      >
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.commentableFlag"
            :disabled="type != null"
            :active-value="true"
            :inactive-value="false"
            active-color="#13ce66"
            inactive-color="#F4F4F5"
            @change="updateArticleStatus(scope.row, 4)"
          />
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="160">
        <template slot-scope="scope">
          <el-button
            v-if="type == null || type === 5"
            type="primary"
            size="mini"
            @click="editArticle(scope.row.id, scope.row.userId)"
          >
            编辑
          </el-button>
          <el-popconfirm
            v-else
            title="确定恢复吗？"
            @confirm="updateArticlesStatus(scope.row.id, type === 6)"
          >
            <el-button type="success" size="mini" slot="reference">
              恢复
            </el-button>
          </el-popconfirm>
          <el-popconfirm
            v-if="type !== 7"
            title="确定删除吗？"
            style="margin-left:10px"
            @confirm="updateArticlesStatus(scope.row.id)"
          >
            <el-button type="danger" size="mini" slot="reference">
              删除
            </el-button>
          </el-popconfirm>
          <el-popconfirm
            v-else
            title="确定彻底删除吗？"
            style="margin-left:10px"
            @confirm="deleteArticles(scope.row.id)"
          >
            <el-button type="danger" size="mini" slot="reference">
              删除
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
        <el-button type="primary" @click="updateArticlesStatus(null)">
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
        <el-button type="primary" @click="deleteArticles(null)">
          确 定
        </el-button>
      </div>
    </el-dialog>
  </el-card>
</template>

<script>
export default {
  created() {
    this.getArticles();
    this.getArticleOption();
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
      options: [
        {
          value: null,
          label: "已发表"
        },
        {
          value: 5,
          label: "草稿箱"
        },
        {
          value: 6,
          label: "回收站"
        }
      ],
      tagList: [],
      tagIdList: [],
      articleList: [],
      usernameList: [],
      categoryList: [],
      articleIdList: [],
      type: null,
      userId: null,
      keywords: null,
      categoryId: null,
      oldKeywords: null,
      loading: true,
      editStatus: false,
      removeStatus: false,
      size: 10,
      count: 0,
      current: 1
    };
  },
  methods: {
    addArticle() {
      this.$router.push({ path: "/article" });
    },
    sizeChange(size) {
      this.size = size;
      this.getArticles(true);
    },
    editArticle(id, userId) {
      this.$router.push({ path: "/article/" + userId + "/" + id });
    },
    checkWeight(weight) {
      return this.$store.state.weight <= weight;
    },
    currentChange(current) {
      this.current = current;
      this.getArticles();
    },
    selectionChange(articleList) {
      this.articleIdList = [];
      articleList.forEach(item => {
        this.articleIdList.push(item.id);
      });
    },
    getArticles(resetCurrentPage = false) {
      if (resetCurrentPage || this.keywords !== this.oldKeywords) {
        this.current = 1;
        this.oldKeywords = this.keywords;
      }
      let params = {
        size: this.size,
        type: this.type,
        userId: this.userId,
        current: this.current,
        keywords: this.keywords,
        tagIdList: this.tagIdList,
        categoryId: this.categoryId
      };
      params = this.$commonMethod.skipEmptyValue(params);
      this.axios.get("/api/back/articles", { params }).then(({ data }) => {
        this.count = data.data.count;
        this.articleList = data.data.pageList;
        this.loading = false;
      });
    },
    deleteArticles(id) {
      let param = {};
      if (id == null) {
        param = { data: this.articleIdList };
      } else {
        param = { data: [id] };
      }
      this.axios.delete("/api/back/articles", param).then(({ data }) => {
        if (data.flag) {
          this.$notify.success({
            title: "成功",
            message: data.message
          });
          if (param.data.length === this.articleList.length) {
            this.current = --this.current > 1 ? this.current : 1;
          }
          this.getArticles();
        } else {
          this.$notify.error({
            title: "失败",
            message: data.message
          });
        }
      });
      this.removeStatus = false;
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
    getArticleOption() {
      let param = {};
      if (this.userId != null && this.userId !== "") {
        param.userId = this.userId;
      }
      this.axios
        .get("/api/back/article/option", { params: param })
        .then(({ data }) => {
          this.tagIdList = [];
          this.categoryId = null;
          this.tagList = data.data.tagDTOList;
          this.categoryList = data.data.categoryDTOList;
        });
    },
    updateArticleStatus(article, type) {
      let param = {
        idList: [article.id]
      };
      if (type != null) {
        param.type = type;
      }
      this.axios.put("/api/back/article/status", param).then(({ data }) => {
        if (!data.flag) {
          this.$notify.error({
            title: "失败",
            message: data.message
          });
          if (type === 2) {
            article.publicFlag = !article.publicFlag;
          } else if (type === 3) {
            article.hiddenFlag = !article.hiddenFlag;
          } else if (type === 4) {
            article.commentableFlag = !article.commentableFlag;
          } else {
            article.topFlag = !article.topFlag;
          }
        }
      });
    },
    updateArticlesStatus(id, isRec = false) {
      let param = {};
      if (id != null) {
        param.idList = [id];
      } else {
        param.idList = this.articleIdList;
      }
      if (this.type != null && this.type !== 5) {
        param.type = this.type;
      }
      if (isRec) {
        param.status = true;
      }
      this.axios.put("/api/back/articles/status", param).then(({ data }) => {
        if (data.flag) {
          this.$notify.success({
            title: "成功",
            message: data.message
          });
          if (param.idList.length === this.articleList.length) {
            this.current = --this.current > 1 ? this.current : 1;
          }
          this.getArticles();
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
    type() {
      this.getArticles(true);
    },
    userId() {
      this.getArticles(true);
      this.getArticleOption();
    },
    categoryId() {
      this.getArticles(true);
    },
    tagIdList: {
      handler(newVal, oldVal) {
        if (newVal.length !== oldVal.length) {
          this.getArticles(true);
        }
      },
      deep: true
    }
  }
};
</script>
