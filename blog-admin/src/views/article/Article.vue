<template>
  <el-card class="main-card">
    <div class="title">{{ this.$route.name }}</div>
    <div class="article-title-container">
      <el-input
        v-model="article.articleTitle"
        :maxlength="50"
        size="medium"
        placeholder="输入文章标题"
      />
      <el-button
        v-if="article.draftFlag"
        type="danger"
        size="medium"
        class="save-btn"
        @click="saveArticleDraft"
      >
        保存草稿
      </el-button>
      <el-button
        type="danger"
        size="medium"
        style="margin-left:10px"
        @click="addOrEditStatus = true"
      >
        发表文章
      </el-button>
    </div>
    <mavon-editor
      v-model="article.articleContent"
      ref="md"
      style="height:calc(100vh - 260px)"
      @imgAdd="uploadArticleImg"
      @imgDel="deleteArticleImg"
    />
    <el-dialog :visible.sync="addOrEditStatus" width="40%" top="10vh">
      <div class="dialog-title-container" slot="title">
        上传文章
      </div>
      <el-form :model="article" size="medium" label-width="80">
        <el-form-item label="文章分类">
          <el-select v-model="article.categoryId" placeholder="请选择分类">
            <el-option
              v-for="item in categoryList"
              :key="item.id"
              :value="item.id"
              :label="item.categoryName"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="文章标签">
          <el-select
            v-model="article.tagIdList"
            placeholder="请选择标签"
            multiple
            clearable
          >
            <el-option
              v-for="item in tagList"
              :key="item.id"
              :value="item.id"
              :label="item.tagName"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="上传封面">
          <el-upload
            :limit="1"
            action=""
            class="upload-cover"
            drag
            :on-remove="deleteCover"
            :http-request="uploadCover"
          >
            <i class="el-icon-upload" v-if="article.articleCover === ''" />
            <div class="el-upload__text" v-if="article.articleCover === ''">
              将文件拖到此处，或<em>点击上传</em>
            </div>
            <img
              v-else
              :src="article.articleCover"
              width="360"
              height="180"
            />
          </el-upload>
        </el-form-item>
      </el-form>
      <el-form :model="article" :inline="true" size="medium" label-width="80">
        <el-form-item label="置顶">
          <el-switch
            v-model="article.topFlag"
            :active-value="true"
            :inactive-value="false"
            active-color="#13ce66"
            inactive-color="#F4F4F5"
          />
        </el-form-item>
        <el-form-item label="隐藏">
          <el-switch
            v-model="article.hiddenFlag"
            :active-value="true"
            :inactive-value="false"
            active-color="#13ce66"
            inactive-color="#F4F4F5"
          />
        </el-form-item>
        <el-form-item label="公开">
          <el-switch
            v-model="article.publicFlag"
            :active-value="true"
            :inactive-value="false"
            active-color="#13ce66"
            inactive-color="#F4F4F5"
          />
        </el-form-item>
        <el-form-item label="可评论">
          <el-switch
            v-model="article.commentableFlag"
            :active-value="true"
            :inactive-value="false"
            active-color="#13ce66"
            inactive-color="#F4F4F5"
          />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="addOrEditStatus = false">取 消</el-button>
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
    this.$store.commit("updateArticleUserId", null);
  },
  data: function() {
    return {
      article: {
        articleTitle: this.$moment(new Date()).format("YYYY-MM-DD"),
        articleContent: "",
        draftFlag: true,
        publicFlag: true,
        commentableFlag: true
      },
      tagList: [],
      categoryList: [],
      autoSave: false,
      addOrEditStatus: false
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
    checkArticleUserIdIsNull() {
      return this.$store.state.articleUserId == null
        ? ""
        : this.$store.state.articleUserId;
    },
    listArticleOptions() {
      this.axios
        .get("/api/back/article/option", {
          params: { userId: this.$store.state.articleUserId }
        })
        .then(({ data }) => {
          this.tagList = data.data.tagDTOList;
          this.categoryList = data.data.categoryDTOList;
        });
    },
    deleteImg(url) {
      let param = { data: url };
      this.axios.delete("/api/back/article/image", param);
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
            formData.append("userId", this.checkArticleUserIdIsNull());
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
        let formData = new FormData();
        formData.append("file", file);
        formData.append("userId", this.checkArticleUserIdIsNull());
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
    deleteCover() {
      this.deleteImg(this.article.articleCover);
      this.article.articleCover = "";
    },
    uploadCover(form) {
      if (this.article.articleCover !== "") {
        this.deleteImg(this.article.articleCover);
      }
      this.uploadImg(null, form.file);
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
    },
    uploadArticleImg(pos, file) {
      this.uploadImg(pos, file);
    },
    deleteArticleImg(pos) {
      this.deleteImg(pos[0]);
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
      });
      this.addOrEditStatus = false;
      this.autoSave = false;
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
