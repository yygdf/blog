<template>
  <el-card class="main-card">
    <div class="title">{{ this.$route.name }}</div>
    <div class="article-title-container">
      <el-input
        v-model="article.articleTitle"
        size="medium"
        :maxlength="50"
        placeholder="输入文章标题"
      />
      <el-button
        type="danger"
        size="medium"
        class="save-btn"
        @click="saveArticleDraft"
        v-if="article.draftFlag"
      >
        保存草稿
      </el-button>
      <el-button
        type="danger"
        size="medium"
        @click="addOrEdit = true"
        style="margin-left:10px"
      >
        发布文章
      </el-button>
    </div>
    <mavon-editor
      ref="md"
      v-model="article.articleContent"
      @imgAdd="uploadArticleImg"
      @imgDel="deleteArticleImg"
      style="height:calc(100vh - 260px)"
    />
    <el-dialog :visible.sync="addOrEdit" width="40%" top="10vh">
      <div class="dialog-title-container" slot="title">
        上传文章
      </div>
      <el-form label-width="80px" size="medium" :model="article">
        <el-form-item label="文章分类">
          <el-select v-model="article.categoryId" placeholder="请选择分类">
            <el-option
              v-for="item in categoryList"
              :key="item.id"
              :label="item.categoryName"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="文章标签">
          <el-select
            v-model="article.tagIdList"
            multiple
            placeholder="请选择标签"
          >
            <el-option
              v-for="item in tagList"
              :key="item.id"
              :label="item.tagName"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="上传封面">
          <el-upload
            class="upload-cover"
            drag
            action=""
            :http-request="uploadCover"
            :on-remove="deleteCover"
            :limit="1"
          >
            <i class="el-icon-upload" v-if="article.articleCover === ''" />
            <div class="el-upload__text" v-if="article.articleCover === ''">
              将文件拖到此处，或<em>点击上传</em>
            </div>
            <img
              v-else
              :src="article.articleCover"
              width="360px"
              height="180px"
            />
          </el-upload>
        </el-form-item>
      </el-form>
      <el-form label-width="80px" size="medium" :model="article" :inline="true">
        <el-form-item label="置顶">
          <el-switch
            v-model="article.topFlag"
            active-color="#13ce66"
            inactive-color="#F4F4F5"
            :active-value="true"
            :inactive-value="false"
          />
        </el-form-item>
        <el-form-item label="隐藏">
          <el-switch
            v-model="article.hiddenFlag"
            active-color="#13ce66"
            inactive-color="#F4F4F5"
            :active-value="true"
            :inactive-value="false"
          />
        </el-form-item>
        <el-form-item label="公开">
          <el-switch
            v-model="article.publicFlag"
            active-color="#13ce66"
            inactive-color="#F4F4F5"
            :active-value="true"
            :inactive-value="false"
          />
        </el-form-item>
        <el-form-item label="可评论">
          <el-switch
            v-model="article.commentableFlag"
            active-color="#13ce66"
            inactive-color="#F4F4F5"
            :active-value="true"
            :inactive-value="false"
          />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="addOrEdit = false">取 消</el-button>
        <el-button type="danger" @click="saveOrUpdateArticle">
          发 表
        </el-button>
      </div>
    </el-dialog>
  </el-card>
</template>

<script>
export default {
  created() {
    const path = this.$route.path;
    const arr = path.split("/");
    const articleId = arr[2];
    if (articleId) {
      this.axios.get("/api/back/article/" + articleId).then(({ data }) => {
        if (data.data.categoryId === -1) {
          data.data.categoryId = null;
        }
        this.article = data.data;
      });
    }
    this.listArticleOptions();
  },
  destroyed() {
    this.autoSaveArticle();
  },
  data: function() {
    return {
      addOrEdit: false,
      autoSave: false,
      categoryList: [],
      tagList: [],
      article: {
        id: null,
        categoryId: null,
        articleTitle: this.$moment(new Date()).format("YYYY-MM-DD"),
        articleCover: "",
        articleContent: "",
        topFlag: false,
        draftFlag: true,
        publicFlag: true,
        hiddenFlag: false,
        commentableFlag: true,
        tagIdList: []
      }
    };
  },
  computed: {
    watchList() {
      const {
        id,
        categoryId,
        articleTitle,
        articleCover,
        articleContent,
        tagIdList
      } = this.article;
      return {
        id,
        categoryId,
        articleTitle,
        articleCover,
        articleContent,
        tagIdList
      };
    }
  },
  methods: {
    listArticleOptions() {
      this.axios.get("/api/back/article/options").then(({ data }) => {
        this.categoryList = data.data.categoryDTOList;
        this.tagList = data.data.tagDTOList;
      });
    },
    uploadCover(form) {
      if (this.article.articleCover !== "") {
        this.deleteImg(this.article.articleCover);
      }
      this.uploadImg(null, form.file);
    },
    deleteCover() {
      this.deleteImg(this.article.articleCover);
      this.article.articleCover = "";
    },
    uploadArticleImg(pos, file) {
      this.uploadImg(pos, file);
    },
    deleteArticleImg(pos) {
      this.deleteImg(pos[0]);
    },
    uploadImg(pos, file) {
      if (this.article.id == null) {
        if (this.article.articleTitle.trim() === "") {
          this.$message.error("文章标题不能为空");
          return false;
        }
        if (this.article.articleContent.trim() === "") {
          this.$message.error("文章内容不能为空");
          return false;
        }
        this.article.draftFlag = true;
        this.axios.post("/api/back/article", this.article).then(({ data }) => {
          if (data.flag) {
            this.article.id = data.data;
            var formData = new FormData();
            formData.append("file", file);
            formData.append("fileSubDir", this.article.id);
            this.axios
              .post("/api/back/article/image", formData)
              .then(({ data }) => {
                if (pos == null) {
                  this.article.articleCover = data.data;
                } else {
                  this.$refs.md.$img2Url(pos, data.data);
                }
              });
          } else {
            this.$notify.error({
              title: "失败",
              message: "图片上传失败"
            });
          }
        });
      } else {
        var formData = new FormData();
        formData.append("file", file);
        formData.append("fileSubDir", this.article.id);
        this.axios
          .post("/api/back/article/image", formData)
          .then(({ data }) => {
            if (pos == null) {
              this.article.articleCover = data.data;
            } else {
              this.$refs.md.$img2Url(pos, data.data);
            }
          });
      }
    },
    deleteImg(url) {
      var param = { data: url };
      this.axios.delete("/api/back/article/image", param);
    },
    saveArticleDraft() {
      if (this.article.articleTitle.trim() === "") {
        this.$message.error("文章标题不能为空");
        return false;
      }
      if (this.article.articleContent.trim() === "") {
        this.$message.error("文章内容不能为空");
        return false;
      }
      this.article.draftFlag = true;
      this.axios.post("/api/back/article", this.article).then(({ data }) => {
        if (data.flag) {
          this.article.id = data.data;
          this.$notify.success({
            title: "成功",
            message: "保存草稿成功"
          });
        } else {
          this.$notify.error({
            title: "失败",
            message: "保存草稿失败"
          });
        }
      });
      this.autoSave = false;
    },
    saveOrUpdateArticle() {
      if (this.article.articleTitle.trim() === "") {
        this.$message.error("文章标题不能为空");
        return false;
      }
      if (this.article.articleContent.trim() === "") {
        this.$message.error("文章内容不能为空");
        return false;
      }
      if (!this.article.categoryId) {
        this.$message.error("文章分类不能为空");
        return false;
      }
      this.article.draftFlag = false;
      this.axios.post("/api/back/article", this.article).then(({ data }) => {
        if (data.flag) {
          this.article.id = data.data;
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
        this.addOrEdit = false;
      });
      this.autoSave = false;
    },
    autoSaveArticle() {
      if (
        this.autoSave &&
        this.article.articleTitle.trim() !== "" &&
        this.article.articleContent.trim() !== ""
      ) {
        this.article.draftFlag = true;
        this.axios.post("/api/back/article", this.article).then(({ data }) => {
          this.article.id = data.data;
          if (data.flag) {
            this.$notify.success({
              title: "成功",
              message: "自动保存成功"
            });
          } else {
            this.$notify.error({
              title: "失败",
              message: "自动保存失败"
            });
          }
        });
      }
    }
  },
  watch: {
    watchList: {
      handler(newVal, oldVal) {
        if (oldVal.id != null || (newVal.id == null && oldVal.id == null)) {
          this.autoSave = true;
        }
      },
      deep: true
    }
  }
};
</script>

<style scoped>
.article-title-container {
  display: flex;
  align-items: center;
  margin-bottom: 1.25rem;
  margin-top: 2.25rem;
}
.save-btn {
  margin-left: 0.75rem;
  background: #fff;
  color: #f56c6c;
}
</style>
