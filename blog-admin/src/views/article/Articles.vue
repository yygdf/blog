<template>
  <el-card class="main-card">
    <div class="title">
      {{ isEn ? this.$route.meta.nameEn : this.$route.name }}
    </div>
    <div class="operation-container">
      <el-button
        type="primary"
        size="small"
        icon="el-icon-plus"
        @click="addArticle"
      >
        {{ $t("button.add") }}
      </el-button>
      <el-button
        v-if="type !== 7"
        :disabled="articleIdList.length === 0"
        type="danger"
        size="small"
        icon="el-icon-minus"
        @click="editStatus = true"
      >
        {{ $t("button.batchDelete") }}
      </el-button>
      <el-button
        v-else
        :disabled="articleIdList.length === 0"
        type="danger"
        size="small"
        icon="el-icon-minus"
        @click="removeStatus = true"
      >
        {{ $t("button.batchDelete") }}
      </el-button>
      <div style="margin-left:auto">
        <el-select
          v-if="checkWeight(300)"
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
          v-model="categoryId"
          size="small"
          style="margin-right:1rem"
          :placeholder="$t('input.selectCategory')"
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
          :placeholder="$t('input.selectTag')"
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
          ref="input"
          size="small"
          style="width: 200px"
          prefix-icon="el-icon-search"
          :placeholder="$t('article.inputTitle')"
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
          {{ $t("button.search") }}
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
        :label="$t('table.user')"
        align="center"
        min-width="120"
      />
      <el-table-column
        v-if="showColumnConfig.articleTitle"
        prop="articleTitle"
        :label="$t('article.title')"
        align="center"
        min-width="120"
      />
      <el-table-column
        v-if="showColumnConfig.categoryName"
        prop="categoryName"
        :label="$t('article.category')"
        align="center"
        min-width="120"
      />
      <el-table-column
        v-if="showColumnConfig.tagNameList"
        prop="tagNameList"
        :label="$t('article.tag')"
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
        :label="$t('article.viewCount')"
        align="center"
        width="120"
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
        v-if="showColumnConfig.publishTime"
        prop="publishTime"
        :label="$t('table.publishDate')"
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
        :label="$t('table.updateDate')"
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
        :label="$t('switch.top')"
        align="center"
        width="80"
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
        :label="$t('switch.public')"
        align="center"
        width="80"
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
        :label="$t('switch.hidden')"
        align="center"
        width="80"
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
        :label="$t('switch.commentable')"
        align="center"
        width="120"
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
      <el-table-column
        fixed="right"
        :label="$t('table.operate')"
        align="center"
        width="220"
      >
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
                v-if="checkWeight(300)"
                v-model="showColumnConfig.username"
                @change="handleColumnCheckedChange"
                >{{ $t("table.user") }}</el-checkbox
              >
              <el-checkbox
                v-model="showColumnConfig.articleTitle"
                @change="handleColumnCheckedChange"
                >{{ $t("article.title") }}</el-checkbox
              >
              <el-checkbox
                v-model="showColumnConfig.categoryName"
                @change="handleColumnCheckedChange"
                >{{ $t("article.category") }}</el-checkbox
              >
              <el-checkbox
                v-model="showColumnConfig.tagNameList"
                @change="handleColumnCheckedChange"
                >{{ $t("article.tag") }}</el-checkbox
              >
              <el-checkbox
                v-model="showColumnConfig.viewCount"
                @change="handleColumnCheckedChange"
                >{{ $t("article.viewCount") }}</el-checkbox
              >
              <el-checkbox
                v-model="showColumnConfig.likeCount"
                @change="handleColumnCheckedChange"
                >{{ $t("article.likeCount") }}</el-checkbox
              >
              <el-checkbox
                v-model="showColumnConfig.publishTime"
                @change="handleColumnCheckedChange"
                >{{ $t("table.publishDate") }}</el-checkbox
              >
              <el-checkbox
                v-model="showColumnConfig.updateTime"
                @change="handleColumnCheckedChange"
                >{{ $t("table.updateDate") }}</el-checkbox
              >
              <el-checkbox
                v-model="showColumnConfig.topFlag"
                @change="handleColumnCheckedChange"
                >{{ $t("switch.top") }}</el-checkbox
              >
              <el-checkbox
                v-model="showColumnConfig.publicFlag"
                @change="handleColumnCheckedChange"
                >{{ $t("switch.public") }}</el-checkbox
              >
              <el-checkbox
                v-model="showColumnConfig.hiddenFlag"
                @change="handleColumnCheckedChange"
                >{{ $t("switch.hidden") }}</el-checkbox
              >
              <div />
              <el-checkbox
                v-model="showColumnConfig.commentableFlag"
                @change="handleColumnCheckedChange"
                >{{ $t("switch.commentable") }}</el-checkbox
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
              @click="getArticles(false)"
            ></i>
          </el-tooltip>
        </template>
        <template slot-scope="scope">
          <el-button
            v-if="type == null || type === 5"
            type="primary"
            size="mini"
            class="smaller-btn"
            @click="editArticle(scope.row.id, scope.row.userId)"
          >
            <i class="el-icon-edit" /> {{ $t("button.edit") }}
          </el-button>
          <el-popconfirm
            v-else
            :title="$t('confirm.content2')"
            @confirm="updateArticlesStatus(scope.row.id, type === 6)"
          >
            <el-button
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
            @confirm="updateArticlesStatus(scope.row.id)"
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
            @confirm="deleteArticles(scope.row.id)"
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
          <el-button
            :disabled="type != null || scope.row.publicFlag"
            type="primary"
            size="mini"
            class="smaller-btn"
            style="margin-left:10px"
            @click="openOperateModel(scope.row)"
          >
            <i class="el-icon-lock" /> {{ $t("button.key") }}
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
        <i class="el-icon-warning" style="color:#ff9900" />{{
          $t("confirm.tip")
        }}
      </div>
      <div style="font-size:1rem">{{ $t("confirm.content5") }}</div>
      <div slot="footer">
        <el-button @click="editStatus = false">{{
          $t("confirm.no")
        }}</el-button>
        <el-button type="primary" @click="updateArticlesStatus(null)">
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
        <el-button type="primary" @click="deleteArticles(null)">
          {{ $t("confirm.yes") }}
        </el-button>
      </div>
    </el-dialog>
    <el-dialog
      :visible.sync="articleTokenFlag"
      width="30%"
      @close="cancelAddOrEditArticleToken"
    >
      <div class="dialog-title-container" slot="title">
        {{ $t("article.dialogTitle2") }}
      </div>
      <el-form :model="articleToken" size="medium" label-width="120px">
        <el-form-item :label="$t('article.articleTitle')">
          <el-input
            v-model="articleToken.articleTitle"
            style="width: 200px"
            disabled
          />
        </el-form-item>
        <el-form-item :label="$t('article.accessKey')">
          <el-input
            v-model="articleToken.accessToken"
            ref="input"
            style="width: 200px"
            maxlength="100"
            :placeholder="$t('article.inputAccessKey')"
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
            {{ $t("article.illegalKey") }}</span
          >
        </el-form-item>
        <el-form-item :label="$t('article.effectiveCount')">
          <el-input-number
            v-model="articleToken.effectiveCount"
            :min="-1"
            :max="2147483647"
            value="-1"
            controls-position="right"
          />
        </el-form-item>
        <el-form-item :label="$t('article.expireTime')">
          <el-date-picker
            v-model="articleToken.expireTime"
            type="datetime"
            style="width: 200px"
            :placeholder="$t('article.selectExpireTime')"
          >
          </el-date-picker>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="articleTokenFlag = false">{{
          $t("button.cancel")
        }}</el-button>
        <el-button
          :disabled="tokenValidStatus !== 2"
          type="primary"
          @click="addOrEditArticleToken"
        >
          {{ $t("button.save") }}
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
    this.$nextTick(() => {
      this.$refs.input.focus();
    });
  },
  data: function() {
    return {
      options: [],
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
      columnCount: 12,
      tokenValidStatus: 0,
      columnCheckedCount: 0,
      tokenMap: new Map()
    };
  },
  methods: {
    openOperateModel(article) {
      if (this.tokenMap.get(article.id)) {
        this.articleToken = this.tokenMap.get(article.id);
        this.articleTokenOrigin = JSON.parse(JSON.stringify(this.articleToken));
        if (this.articleToken.accessToken !== "") {
          this.tokenValidStatus = 2;
        } else {
          this.tokenValidStatus = 0;
        }
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
          articleTitle: false,
          categoryName: false,
          tagNameList: false,
          viewCount: false,
          likeCount: false,
          publishTime: false,
          updateTime: false,
          topFlag: false,
          publicFlag: false,
          hiddenFlag: false,
          commentableFlag: false
        };
        this.columnCheckedCount = 0;
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
      this.columnCheckedCount = 12;
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
            title: this.$t("success"),
            message: data.message
          });
          if (param.data.length === this.articleList.length) {
            this.current = --this.current > 1 ? this.current : 1;
          }
          this.getArticles();
        } else {
          this.$notify.error({
            title: this.$t("failure"),
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
            title: this.$t("failure"),
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
            title: this.$t("success"),
            message: data.message
          });
          if (param.idList.length === this.articleList.length) {
            this.current = --this.current > 1 ? this.current : 1;
          }
          this.getArticles();
        } else {
          this.$notify.error({
            title: this.$t("failure"),
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
            title: this.$t("success"),
            message: data.message
          });
        } else {
          this.$notify.error({
            title: this.$t("failure"),
            message: data.message
          });
        }
      });
      this.articleTokenFlag = false;
    }
  },
  computed: {
    isEn() {
      return this.$i18n.locale === "en_US";
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
    },
    isEn: {
      handler() {
        this.options = [
          {
            value: null,
            label: this.$t("option.published")
          },
          {
            value: 5,
            label: this.$t("option.drafts")
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
