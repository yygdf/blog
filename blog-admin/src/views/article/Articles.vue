<template>
  <el-card class="main-card">
    <div class="title">{{ this.$route.name }}</div>
    <div class="operation-container">
      <el-button
        type="primary"
        size="small"
        icon="el-icon-plus"
        @click="addArticle()"
      >
        新增
      </el-button>
      <el-button
        v-if="deletedFlag"
        type="danger"
        size="small"
        icon="el-icon-minus"
        :disabled="articleIdList.length === 0"
        @click="removeStatus = true"
      >
        批量删除
      </el-button>
      <el-button
        v-else
        type="danger"
        size="small"
        icon="el-icon-minus"
        :disabled="articleIdList.length === 0"
        @click="editStatus = true"
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
          v-if="checkWeight(300)"
        >
          <el-option
            v-for="item in usernameList"
            :key="item.userId"
            :label="item.username"
            :value="item.userId"
          />
        </el-select>
        <el-select
          v-model="categoryId"
          placeholder="请选择分类"
          size="small"
          style="margin-right:1rem"
          clearable
          filterable
        >
          <el-option
            v-for="item in categoryList"
            :key="item.id"
            :label="item.categoryName"
            :value="item.id"
          />
        </el-select>
        <el-select
          v-model="tagIdList"
          placeholder="请选择标签"
          multiple
          size="small"
          style="margin-right:1rem;"
          clearable
          filterable
        >
          <el-option
            v-for="item in tagList"
            :key="item.id"
            :label="item.tagName"
            :value="item.id"
          />
        </el-select>
        <el-select
          v-model="condition"
          placeholder="请选择"
          size="small"
          style="margin-right:1rem;"
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
          placeholder="请输入文章名"
          style="width:200px"
          clearable
          @keyup.enter.native="listArticles"
        />
        <el-button
          type="primary"
          size="small"
          icon="el-icon-search"
          style="margin-left:1rem"
          @click="listArticles"
        >
          搜索
        </el-button>
      </div>
    </div>
    <el-table
      border
      :data="articleList"
      @selection-change="selectionChange"
      v-loading="loadStatus"
    >
      <el-table-column type="selection" width="40" align="center" />
      <el-table-column
        prop="username"
        label="用户"
        width="120"
        align="center"
        v-if="checkWeight(300)"
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
        width="120"
        align="center"
      />
      <el-table-column prop="tagDTOList" label="标签" align="center">
        <template slot-scope="scope">
          <el-tag
            v-for="item of scope.row.tagDTOList"
            :key="item.tagId"
            style="margin-right:0.2rem;margin-top:0.2rem"
          >
            {{ item.tagName }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column
        prop="viewCount"
        label="浏览量"
        width="80"
        align="center"
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
        prop="publishTime"
        label="发表时间"
        width="120"
        align="center"
      >
        <template slot-scope="scope" v-if="scope.row.publishTime">
          <i class="el-icon-time" style="margin-right:5px" />
          {{ scope.row.publishTime | date }}
        </template>
      </el-table-column>
      <el-table-column
        prop="updateTime"
        label="更新时间"
        width="120"
        align="center"
      >
        <template slot-scope="scope" v-if="scope.row.updateTime">
          <i class="el-icon-time" style="margin-right:5px" />
          {{ scope.row.updateTime | date }}
        </template>
      </el-table-column>
      <el-table-column prop="topFlag" label="置顶" width="80" align="center">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.topFlag"
            active-color="#13ce66"
            inactive-color="#F4F4F5"
            :disabled="optionIndex !== 0"
            :active-value="true"
            :inactive-value="false"
            @change="updateArticleStatus(scope.row)"
          />
        </template>
      </el-table-column>
      <el-table-column prop="hiddenFlag" label="隐藏" width="80" align="center">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.hiddenFlag"
            active-color="#13ce66"
            inactive-color="#F4F4F5"
            :disabled="optionIndex !== 0"
            :active-value="true"
            :inactive-value="false"
            @change="updateArticleStatus(scope.row)"
          />
        </template>
      </el-table-column>
      <el-table-column prop="publicFlag" label="公开" width="80" align="center">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.publicFlag"
            active-color="#13ce66"
            inactive-color="#F4F4F5"
            :disabled="optionIndex !== 0"
            :active-value="true"
            :inactive-value="false"
            @change="updateArticleStatus(scope.row)"
          />
        </template>
      </el-table-column>
      <el-table-column
        prop="commentableFlag"
        label="可评论"
        width="80"
        align="center"
      >
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.commentableFlag"
            active-color="#13ce66"
            inactive-color="#F4F4F5"
            :disabled="optionIndex !== 0"
            :active-value="true"
            :inactive-value="false"
            @change="updateArticleStatus(scope.row)"
          />
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="160">
        <template slot-scope="scope">
          <el-button
            type="primary"
            size="mini"
            @click="editArticle(scope.row.id, scope.row.userId)"
            v-if="optionIndex === 0 || optionIndex === 1"
          >
            编辑
          </el-button>
          <el-popconfirm
            title="确定恢复吗？"
            v-if="optionIndex === 2"
            @confirm="updateArticlesStatus(scope.row.id, true)"
          >
            <el-button size="mini" type="success" slot="reference">
              恢复
            </el-button>
          </el-popconfirm>
          <el-popconfirm
            title="确定恢复吗？"
            v-if="optionIndex === 3"
            @confirm="updateArticlesStatus(scope.row.id)"
          >
            <el-button size="mini" type="success" slot="reference">
              恢复
            </el-button>
          </el-popconfirm>
          <el-popconfirm
            title="确定删除吗？"
            style="margin-left:10px"
            v-if="optionIndex !== 3"
            @confirm="updateArticlesStatus(scope.row.id)"
          >
            <el-button size="mini" type="danger" slot="reference">
              删除
            </el-button>
          </el-popconfirm>
          <el-popconfirm
            style="margin-left:10px"
            v-if="optionIndex === 3"
            title="确定彻底删除吗？"
            @confirm="deleteArticles(scope.row.id)"
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
import qs from "qs";
export default {
  created() {
    this.userId = this.$store.state.userId;
    this.listArticles();
    this.listAllUsername();
    this.listArticleOptions();
    if (this.checkWeight(100)) {
      this.options[3] = {
        value: '{"draftFlag":null,"recycleFlag":null,"deletedFlag":true}',
        label: "已删除"
      };
    }
  },
  data: function() {
    return {
      options: [
        {
          value: '{"draftFlag":false,"recycleFlag":false,"deletedFlag":null}',
          label: "已发表"
        },
        {
          value: '{"draftFlag":true,"recycleFlag":false,"deletedFlag":null}',
          label: "草稿箱"
        },
        {
          value: '{"draftFlag":null,"recycleFlag":true,"deletedFlag":null}',
          label: "回收站"
        }
      ],
      condition: '{"draftFlag":false,"recycleFlag":false,"deletedFlag":null}',
      tagList: [],
      tagIdList: [],
      articleList: [],
      usernameList: [],
      categoryList: [],
      articleIdList: [],
      userId: null,
      keywords: null,
      categoryId: null,
      loadStatus: true,
      editStatus: false,
      removeStatus: false,
      draftFlag: false,
      recycleFlag: false,
      deletedFlag: false,
      size: 10,
      count: 0,
      current: 1,
      optionIndex: 0
    };
  },
  methods: {
    addArticle() {
      this.$router.push({ path: "/article" });
    },
    sizeChange(size) {
      this.size = size;
      this.listArticles();
    },
    checkWeight(weight = 200) {
      return this.$store.state.weight <= weight;
    },
    editArticle(id, userId) {
      this.$router.push({ path: "/article/" + id });
      this.$store.commit("updateArticleUserId", userId);
    },
    currentChange(current) {
      this.current = current;
      this.listArticles();
    },
    selectionChange(articleList) {
      this.articleIdList = [];
      articleList.forEach(item => {
        this.articleIdList.push(item.id);
      });
    },
    listArticles() {
      let params = {
        size: this.size,
        userId: this.userId,
        current: this.current,
        keywords: this.keywords,
        draftFlag: this.draftFlag,
        recycleFlag: this.recycleFlag,
        deletedFlag: this.deletedFlag
      };
      if (!this.recycleFlag && !this.draftFlag) {
        params.tagIdList = this.tagIdList;
        params.categoryId = this.categoryId;
      }
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
          this.loadStatus = false;
        });
    },
    listAllUsername() {
      if (this.checkWeight(300)) {
        this.axios.get("/api/back/user/username").then(({ data }) => {
          this.usernameList = data.data;
        });
      }
    },
    listArticleOptions() {
      this.axios
        .get("/api/back/article/options", {
          params: { userId: this.userId }
        })
        .then(({ data }) => {
          this.tagList = data.data.tagDTOList;
          this.categoryList = data.data.categoryDTOList;
        });
    },
    deleteArticles(id) {
      var param = {};
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
          this.listArticles();
        } else {
          this.$notify.error({
            title: "失败",
            message: data.message
          });
        }
        this.removeStatus = false;
      });
    },
    updateArticleStatus(article) {
      let param = {
        id: article.id,
        topFlag: article.topFlag,
        publicFlag: article.publicFlag,
        hiddenFlag: article.hiddenFlag,
        commentableFlag: article.commentableFlag
      };
      this.axios.put("/api/back/article/status", param);
    },
    updateArticlesStatus(id, isRec = false) {
      let param = new URLSearchParams();
      if (id != null) {
        param.append("idList", [id]);
      } else {
        param.append("idList", this.articleIdList);
      }
      let recycleFlag = !this.recycleFlag;
      let deletedFlag = this.deletedFlag;
      if (this.optionIndex === 2) {
        recycleFlag = null;
        deletedFlag = !this.deletedFlag;
        if (isRec) {
          recycleFlag = !this.recycleFlag;
          deletedFlag = this.deletedFlag;
        }
      }
      if (this.optionIndex === 3) {
        deletedFlag = !this.deletedFlag;
      }
      param.append("recycleFlag", recycleFlag);
      param.append("deletedFlag", deletedFlag);
      this.axios.put("/api/back/articles", param).then(({ data }) => {
        if (data.flag) {
          this.$notify.success({
            title: "成功",
            message: data.message
          });
          this.listArticles();
        } else {
          this.$notify.error({
            title: "失败",
            message: data.message
          });
        }
        this.editStatus = false;
      });
    }
  },
  watch: {
    condition() {
      const condition = JSON.parse(this.condition);
      this.draftFlag = condition.draftFlag;
      this.recycleFlag = condition.recycleFlag;
      this.deletedFlag = condition.deletedFlag;
      if (this.deletedFlag) {
        this.optionIndex = 3;
      } else if (this.recycleFlag) {
        this.optionIndex = 2;
      } else if (this.draftFlag) {
        this.optionIndex = 1;
      } else {
        this.optionIndex = 0;
      }
      this.listArticles();
    },
    userId(newVal, oldVal) {
      if (oldVal != null) {
        this.listArticles();
        this.listArticleOptions();
      }
    },
    tagIdList() {
      this.listArticles();
    },
    categoryId() {
      this.listArticles();
    }
  }
};
</script>
