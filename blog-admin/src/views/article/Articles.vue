<template>
  <el-card class="main-card">
    <div class="title">{{ this.$route.name }}</div>
    <div class="operation-container">
      <el-button
        v-if="!garbageFlag"
        type="danger"
        size="small"
        icon="el-icon-minus"
        :disabled="articleIdList.length === 0"
        @click="updateGarbageFlag = true"
      >
        批量删除
      </el-button>
      <el-button
        v-else
        type="danger"
        size="small"
        icon="el-icon-minus"
        :disabled="articleIdList.length === 0"
        @click="remove = true"
      >
        批量删除
      </el-button>
      <div style="margin-left:auto">
        <el-select
          v-model="condition"
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
          placeholder="请输入文章名"
          style="width:200px"
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
      v-loading="loading"
    >
      <el-table-column type="selection" width="55" />
      <el-table-column prop="articleTitle" label="标题" align="center" />
      <el-table-column
        prop="categoryName"
        label="分类"
        width="120"
        align="center"
      />
      <el-table-column
        prop="tagDTOList"
        label="标签"
        width="180"
        align="center"
      >
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
        prop="createTime"
        label="发表时间"
        width="140"
        align="center"
      >
        <template slot-scope="scope">
          <i class="el-icon-time" style="margin-right:5px" />
          {{ scope.row.createTime | date }}
        </template>
      </el-table-column>
      <el-table-column
        prop="updateTime"
        label="更新时间"
        width="140"
        align="center"
      >
        <template slot-scope="scope" v-if="scope.row.updateTime">
          <i class="el-icon-time" style="margin-right:5px" />
          {{ scope.row.updateTime | date }}
        </template>
      </el-table-column>
      <el-table-column prop="topFlag" label="置顶" width="100" align="center">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.topFlag"
            active-color="#13ce66"
            inactive-color="#F4F4F5"
            :disabled="scope.row.garbageFlag || scope.row.draftFlag"
            :active-value="true"
            :inactive-value="false"
            @change="changeTop(scope.row)"
          />
        </template>
      </el-table-column>
      <el-table-column
        prop="publicFlag"
        label="公开"
        width="100"
        align="center"
      >
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.publicFlag"
            active-color="#13ce66"
            inactive-color="#F4F4F5"
            :disabled="scope.row.garbageFlag || scope.row.draftFlag"
            :active-value="true"
            :inactive-value="false"
            @change="changePublic(scope.row)"
          />
        </template>
      </el-table-column>
      <el-table-column
        prop="hiddenFlag"
        label="隐藏"
        width="100"
        align="center"
      >
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.hiddenFlag"
            active-color="#13ce66"
            inactive-color="#F4F4F5"
            :disabled="scope.row.garbageFlag || scope.row.draftFlag"
            :active-value="true"
            :inactive-value="false"
            @change="changeHidden(scope.row)"
          />
        </template>
      </el-table-column>
      <el-table-column
        prop="commentableFlag"
        label="可评论"
        width="100"
        align="center"
      >
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.commentableFlag"
            active-color="#13ce66"
            inactive-color="#F4F4F5"
            :disabled="scope.row.garbageFlag || scope.row.draftFlag"
            :active-value="true"
            :inactive-value="false"
            @change="changeCommentable(scope.row)"
          />
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="160">
        <template slot-scope="scope">
          <el-button
            type="primary"
            size="mini"
            @click="editArticle(scope.row.id)"
            v-if="!scope.row.garbageFlag"
          >
            编辑
          </el-button>
          <el-popconfirm
            title="确定删除吗？"
            style="margin-left:10px"
            @confirm="updateArticleStatus(scope.row.id)"
            v-if="!scope.row.garbageFlag"
          >
            <el-button size="mini" type="danger" slot="reference">
              删除
            </el-button>
          </el-popconfirm>
          <el-popconfirm
            title="确定恢复吗？"
            v-if="scope.row.garbageFlag"
            @confirm="updateArticleStatus(scope.row.id)"
          >
            <el-button size="mini" type="success" slot="reference">
              恢复
            </el-button>
          </el-popconfirm>
          <el-popconfirm
            style="margin-left:10px"
            v-if="scope.row.garbageFlag"
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
    <el-dialog :visible.sync="updateGarbageFlag" width="30%">
      <div class="dialog-title-container" slot="title">
        <i class="el-icon-warning" style="color:#ff9900" />提示
      </div>
      <div style="font-size:1rem">是否删除选中项？</div>
      <div slot="footer">
        <el-button @click="updateGarbageFlag = false">取 消</el-button>
        <el-button type="primary" @click="updateArticleStatus(null)">
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
    this.listArticles();
  },
  data: function() {
    return {
      loading: true,
      updateGarbageFlag: false,
      remove: false,
      options: [
        {
          value: '{"garbageFlag":false,"draftFlag":false}',
          label: "已发布"
        },
        {
          value: '{"garbageFlag":true,"draftFlag":null}',
          label: "回收站"
        },
        {
          value: '{"garbageFlag":false,"draftFlag":true}',
          label: "草稿箱"
        }
      ],
      condition: '{"garbageFlag":false,"draftFlag":false}',
      articleList: [],
      articleIdList: [],
      keywords: null,
      garbageFlag: false,
      draftFlag: false,
      current: 1,
      size: 10,
      count: 0
    };
  },
  methods: {
    selectionChange(articleList) {
      this.articleIdList = [];
      articleList.forEach(item => {
        this.articleIdList.push(item.id);
      });
    },
    editArticle(id) {
      this.$router.push({ path: "/article/" + id });
    },
    updateArticleStatus(id) {
      let param = new URLSearchParams();
      if (id != null) {
        param.append("idList", [id]);
      } else {
        param.append("idList", this.articleIdList);
      }
      param.append("garbageFlag", !this.garbageFlag);
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
        this.updateGarbageFlag = false;
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
        this.remove = false;
      });
    },
    sizeChange(size) {
      this.size = size;
      this.listArticles();
    },
    currentChange(current) {
      this.current = current;
      this.listArticles();
    },
    changeTop(article) {
      let param = new URLSearchParams();
      param.append("topFlag", article.topFlag);
      this.axios.put("/api/back/article/top/" + article.id, param);
    },
    changePublic(article) {
      let param = new URLSearchParams();
      param.append("publicFlag", article.publicFlag);
      this.axios.put("/api/back/article/public/" + article.id, param);
    },
    changeHidden(article) {
      let param = new URLSearchParams();
      param.append("hiddenFlag", article.hiddenFlag);
      this.axios.put("/api/back/article/hidden/" + article.id, param);
    },
    changeCommentable(article) {
      let param = new URLSearchParams();
      param.append("commentableFlag", article.commentableFlag);
      this.axios.put("/api/back/article/commentable/" + article.id, param);
    },
    listArticles() {
      this.axios
        .get("/api/back/articles", {
          params: {
            size: this.size,
            current: this.current,
            keywords: this.keywords,
            draftFlag: this.draftFlag,
            garbageFlag: this.garbageFlag
          }
        })
        .then(({ data }) => {
          this.count = data.data.count;
          this.articleList = data.data.pageList;
          this.loading = false;
        });
    }
  },
  watch: {
    condition() {
      const condition = JSON.parse(this.condition);
      this.garbageFlag = condition.garbageFlag;
      this.draftFlag = condition.draftFlag;
      this.listArticles();
    }
  }
};
</script>
