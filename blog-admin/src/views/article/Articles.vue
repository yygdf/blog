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
        v-if="checkWeight(300) && showColumnConfig.username"
        prop="username"
        label="用户"
        align="center"
        min-width="120"
      />
      <el-table-column
        v-if="showColumnConfig.articleTitle"
        prop="articleTitle"
        label="标题"
        align="center"
        min-width="120"
      />
      <el-table-column
        v-if="showColumnConfig.categoryName"
        prop="categoryName"
        label="分类"
        align="center"
        min-width="120"
      />
      <el-table-column
        v-if="showColumnConfig.tagNameList"
        prop="tagNameList"
        label="标签"
        align="center"
        min-width="240"
      >
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
        v-if="showColumnConfig.viewCount"
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
        v-if="showColumnConfig.publishTime"
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
        v-if="showColumnConfig.updateTime"
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
      <el-table-column
        v-if="showColumnConfig.topFlag"
        prop="topFlag"
        label="置顶"
        align="center"
        min-width="70"
      >
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
      <el-table-column
        v-if="showColumnConfig.publicFlag"
        prop="publicFlag"
        label="公开"
        align="center"
        min-width="70"
      >
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
      <el-table-column
        v-if="showColumnConfig.hiddenFlag"
        prop="hiddenFlag"
        label="隐藏"
        align="center"
        min-width="70"
      >
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
        v-if="showColumnConfig.commentableFlag"
        prop="commentableFlag"
        label="可评论"
        align="center"
        min-width="70"
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
      <el-table-column fixed="right" label="操作" align="center" width="200">
        <template slot="header">
          <el-popover placement="bottom" title="选择显示列" width="160">
            <div>
              <el-checkbox v-model="showColumnConfig.username"
                >用户</el-checkbox
              >
              <el-checkbox v-model="showColumnConfig.articleTitle"
                >标题</el-checkbox
              >
              <el-checkbox v-model="showColumnConfig.categoryName"
                >分类</el-checkbox
              >
              <el-checkbox v-model="showColumnConfig.tagNameList"
                >标签</el-checkbox
              >
              <el-checkbox v-model="showColumnConfig.viewCount"
                >浏览量</el-checkbox
              >
              <el-checkbox v-model="showColumnConfig.likeCount"
                >点赞量</el-checkbox
              >
              <el-checkbox v-model="showColumnConfig.publishTime"
                >发表日期</el-checkbox
              >
              <el-checkbox v-model="showColumnConfig.updateTime"
                >更新日期</el-checkbox
              >
              <el-checkbox v-model="showColumnConfig.topFlag">置顶</el-checkbox>
              <el-checkbox v-model="showColumnConfig.publicFlag"
                >公开</el-checkbox
              >
              <el-checkbox v-model="showColumnConfig.hiddenFlag"
                >隐藏</el-checkbox
              >
              <div />
              <el-checkbox v-model="showColumnConfig.commentableFlag"
                >可评论</el-checkbox
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
          <el-button
            v-if="type == null || type === 5"
            type="primary"
            size="mini"
            class="smaller-btn"
            @click="editArticle(scope.row.id, scope.row.userId)"
          >
            <i class="el-icon-edit" /> 编辑
          </el-button>
          <el-popconfirm
            v-else
            title="确定恢复吗？"
            @confirm="updateArticlesStatus(scope.row.id, type === 6)"
          >
            <el-button
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
            @confirm="updateArticlesStatus(scope.row.id)"
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
            @confirm="deleteArticles(scope.row.id)"
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
          <el-button
            :disabled="type != null || scope.row.publicFlag"
            type="primary"
            size="mini"
            class="smaller-btn"
            style="margin-left:10px"
            @click="openOperateModel(scope.row)"
          >
            <i class="el-icon-lock" /> 密令
          </el-button>
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
    <el-dialog
      :visible.sync="articleTokenFlag"
      width="30%"
      @close="cancelAddOrEditArticleToken"
    >
      <div class="dialog-title-container" slot="title">
        密令设置
      </div>
      <el-form :model="articleToken" size="medium" label-width="80">
        <el-form-item label="文章标题">
          <el-input
            v-model="articleToken.articleTitle"
            style="width: 200px"
            disabled
          />
        </el-form-item>
        <el-form-item label="访问密令">
          <el-input
            v-model="articleToken.accessToken"
            ref="input"
            style="width: 200px"
            maxlength="100"
            placeholder="请输入访问密令"
            @keyup.native="tokenInputChange"
          />&nbsp;
          <span
            v-if="tokenValidStatus === 2"
            class="el-icon-success"
            style="color: green;"
          ></span>
          <span
            v-if="tokenValidStatus === -1"
            class="el-icon-error"
            style="color: red;"
          >
            该密令不合法!</span
          >
        </el-form-item>
        <el-form-item label="有效次数">
          <el-input-number
            v-model="articleToken.effectiveCount"
            :min="-1"
            :max="2147483647"
            value="-1"
            controls-position="right"
          />
        </el-form-item>
        <el-form-item label="过期时间">
          <el-date-picker
            v-model="articleToken.expireTime"
            type="datetime"
            style="width: 200px"
            placeholder="选择过期时间"
          >
          </el-date-picker>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="articleTokenFlag = false">取 消</el-button>
        <el-button
          :disabled="tokenValidStatus !== 2"
          type="primary"
          @click="addOrEditArticleToken"
        >
          确 定
        </el-button>
      </span>
    </el-dialog>
  </el-card>
</template>

<script>
import qs from "qs";
export default {
  created() {
    this.loadColumnConfig();
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
      articleToken: {},
      articleTokenOrigin: {},
      showColumnConfig: {},
      type: null,
      userId: null,
      keywords: null,
      categoryId: null,
      oldKeywords: null,
      loading: true,
      editStatus: false,
      removeStatus: false,
      articleTokenFlag: false,
      size: 10,
      count: 0,
      current: 1,
      tokenValidStatus: 0,
      tokenMap: new Map()
    };
  },
  methods: {
    openOperateModel(article) {
      if (this.tokenMap.get(article.id)) {
        this.tokenValidStatus = 2;
        this.articleToken = this.tokenMap.get(article.id);
        this.articleTokenOrigin = JSON.parse(JSON.stringify(this.articleToken));
        this.$nextTick(() => {
          this.$refs.input.focus();
        });
        this.articleTokenFlag = true;
      } else {
        this.articleToken = {
          id: article.id,
          articleTitle: article.articleTitle,
          expireTime: null,
          accessToken: "",
          effectiveCount: -1
        };
        this.tokenValidStatus = 0;
        this.axios
          .get("/api/back/article/token/" + article.id)
          .then(({ data }) => {
            if (data.data.accessToken != null) {
              this.articleToken = { ...this.articleToken, ...data.data };
              this.tokenMap.set(article.id, this.articleToken);
              this.tokenValidStatus = 2;
            }
            this.articleTokenOrigin = JSON.parse(
              JSON.stringify(this.articleToken)
            );
            this.articleTokenFlag = true;
            this.$nextTick(() => {
              this.$refs.input.focus();
            });
          });
      }
    },
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
    selectionChange(selection) {
      this.articleIdList = [];
      selection.forEach(item => {
        this.articleIdList.push(item.id);
      });
    },
    tokenInputChange() {
      if (this.articleToken.accessToken.trim() !== "") {
        this.tokenValidStatus = 2;
      } else {
        this.tokenValidStatus = -1;
      }
    },
    cancelAddOrEditArticleToken() {
      this.tokenMap.set(this.articleTokenOrigin.id, this.articleTokenOrigin);
    },
    saveColumnConfig() {
      localStorage.setItem(
        "ArticlesColumnSet",
        JSON.stringify(this.showColumnConfig)
      );
      document.body.click();
    },
    loadColumnConfig() {
      if (localStorage.getItem("ArticlesColumnSet")) {
        this.showColumnConfig = JSON.parse(
          localStorage.getItem("ArticlesColumnSet")
        );
      } else {
        this.showColumnConfig = {
          username: true,
          articleTitle: true,
          categoryName: true,
          tagNameList: true,
          viewCount: true,
          likeCount: true,
          publishTime: true,
          updateTime: true,
          topFlag: true,
          publicFlag: true,
          hiddenFlag: true,
          commentableFlag: true
        };
      }
    },
    getArticles(resetCurrentPage) {
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
      this.axios
        .get("/api/back/articles", {
          params,
          paramsSerializer: params => {
            return qs.stringify(params, { indices: false, skipNulls: true });
          }
        })
        .then(({ data }) => {
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
          this.tagList = data.data.option.tagDTOList;
          this.categoryList = data.data.option.categoryDTOList;
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
    updateArticlesStatus(id, isRec) {
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
    },
    addOrEditArticleToken() {
      let param = {
        id: this.articleToken.id
      };
      if (this.articleToken.expireTime != null) {
        param.expireTime = this.articleToken.expireTime;
      }
      if (
        this.articleToken.accessToken !== this.articleTokenOrigin.accessToken
      ) {
        param.accessToken = this.articleToken.accessToken;
      }
      if (
        this.articleToken.effectiveCount !==
        this.articleTokenOrigin.effectiveCount
      ) {
        param.effectiveCount = this.articleToken.effectiveCount;
      }
      this.axios.post("/api/back/article/token", param).then(({ data }) => {
        if (data.flag) {
          this.tokenMap.set(this.articleToken.id, this.articleToken);
          this.$notify.success({
            title: "成功",
            message: data.message
          });
        } else {
          this.$notify.error({
            title: "失败",
            message: data.message
          });
        }
      });
      this.articleTokenFlag = false;
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
