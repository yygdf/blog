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
        :disabled="modCount === 0"
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
        @click="publicArticle"
      >
        发表文章
      </el-button>
    </div>
    <mavon-editor
      v-model="article.articleContent"
      ref="md"
      style="height:calc(100vh - 260px)"
      @imgAdd="uploadArticleImg"
      @imgDel="updateArticleImg"
    />
    <el-dialog
      :visible.sync="addOrEditStatus"
      width="40%"
      top="10vh"
      @close="cancelSaveOrUpdateArticle"
    >
      <div class="dialog-title-container" slot="title">
        发表文章
      </div>
      <el-form :model="article" size="medium" label-width="80">
        <el-form-item label="文章分类">
          <el-select v-model="article.categoryId" placeholder="请选择分类">
            <el-option
              v-for="item in categoryList"
              :key="item.id"
              :value="item.id"
              :label="item.label"
            />
          </el-select>
          <span style="color: red;"> *</span>
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
              :label="item.label"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="文章封面">
          <el-upload
            ref="upload"
            action=""
            class="upload-cover"
            :on-change="changeCover"
            :on-remove="updateCover"
            :http-request="uploadCover"
            drag
          >
            <i class="el-icon-upload" v-if="!article.articleCover" />
            <div class="el-upload__text" v-if="!article.articleCover">
              将文件拖到此处，或<em>点击上传</em>
            </div>
            <img v-else :src="article.articleCover" width="360" height="180" />
          </el-upload>
        </el-form-item>
        <el-form-item label="封面链接">
          <el-input
            v-model="article.articleCover"
            :placeholder="staticResourceUrl"
            class="word-limit-input"
            style="width: 520px"
            maxlength="255"
            @focus="articleCoverUpload = article.articleCover"
            @change="updateCover(true)"
            show-word-limit
          />
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
        <el-button
          :disabled="modCount === 0"
          type="danger"
          @click="saveOrUpdateArticle"
        >
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
        this.articleBackVO.id = articleId;
        this.articleOrigin = JSON.parse(JSON.stringify(data.data));
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
        publicFlag: true,
        commentableFlag: true
      },
      articleOrigin: {
        articleTitle: "",
        publicFlag: true,
        commentableFlag: true
      },
      tagList: [],
      categoryList: [],
      articleBackVO: {},
      staticResourceUrl: "",
      articleCoverUpload: "",
      modCount: 0,
      addOrEditStatus: false,
      articleCoverUploadFlag: false
    };
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
          this.staticResourceUrl = data.data.staticResourceUrl;
        });
    },
    updateImg(url) {
      let pathArr = url.split("/");
      let fileName = pathArr[pathArr.length - 1].split(".")[0];
      this.axios.put("/api/back/article/image", null, { params: { fileName } });
    },
    uploadImg(pos, file) {
      if (this.article.id == null) {
        if (this.article.articleTitle.trim() === "") {
          this.$message.error("文章标题不能为空");
          this.$refs.md.$img2Url(pos, "");
          return false;
        }
        this.article.draftFlag = true;
        this.axios.post("/api/back/article", this.article).then(({ data }) => {
          if (data.flag) {
            this.article.id = data.data;
            this.articleBackVO.id = data.data;
            let formData = new FormData();
            formData.append("file", file);
            formData.append("userId", this.checkArticleUserIdIsNull());
            formData.append("articleId", this.article.id);
            this.axios
              .post("/api/back/article/image", formData)
              .then(({ data }) => {
                if (pos == null) {
                  this.article.articleCover = data.data;
                  this.articleCoverUploadFlag = true;
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
        formData.append("articleId", this.article.id);
        this.axios
          .post("/api/back/article/image", formData)
          .then(({ data }) => {
            if (pos == null) {
              this.article.articleCover = data.data;
              this.articleCoverUploadFlag = true;
            } else {
              this.$refs.md.$img2Url(pos, data.data);
            }
          });
      }
    },
    changeCover(file, fileList) {
      if (fileList.length > 1) {
        fileList.splice(0, 1);
      }
    },
    updateCover(flag) {
      if (typeof flag == "boolean") {
        if (this.articleCoverUploadFlag) {
          this.$refs.upload.clearFiles();
          this.updateImg(this.articleCoverUpload);
          this.articleCoverUploadFlag = false;
        }
      } else {
        this.updateImg(this.article.articleCover);
        this.article.articleCover = "";
        this.articleCoverUploadFlag = false;
      }
    },
    uploadCover(form) {
      if (this.articleCoverUploadFlag) {
        this.updateImg(this.article.articleCover);
      }
      this.uploadImg(null, form.file);
    },
    autoSaveArticle() {
      if (
        this.modCount !== 0 &&
        this.article.articleTitle.trim() !== "" &&
        this.article.articleContent.trim() !== ""
      ) {
        this.article.draftFlag = true;
        this.axios
          .post("/api/back/article", this.articleBackVO)
          .then(({ data }) => {
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
    updateArticleImg(pos) {
      this.updateImg(pos[0]);
    },
    publicArticle() {
      if (this.article.articleTitle.trim() === "") {
        this.$message.error("文章标题不能为空");
        return false;
      }
      if (this.article.articleContent.trim() === "") {
        this.$message.error("文章内容不能为空");
        return false;
      }
      this.addOrEditStatus = true;
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
      this.axios
        .post("/api/back/article", this.articleBackVO)
        .then(({ data }) => {
          if (data.flag) {
            this.article.id = data.data;
            this.articleBackVO.id = data.data;
            this.articleOrigin = JSON.parse(JSON.stringify(this.article));
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
      this.modCount = 0;
    },
    saveOrUpdateArticle() {
      if (!this.article.categoryId) {
        this.$message.error("文章分类不能为空");
        this.$refs.upload.clearFiles();
        return false;
      }
      this.article.draftFlag = false;
      this.axios
        .post("/api/back/article", this.articleBackVO)
        .then(({ data }) => {
          if (data.flag) {
            this.article.id = data.data;
            this.articleBackVO.id = data.data;
            this.articleOrigin = JSON.parse(JSON.stringify(this.article));
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
      this.modCount = 0;
      this.addOrEditStatus = false;
    },
    cancelSaveOrUpdateArticle() {
      if (this.articleCoverUploadFlag) {
        this.updateImg(this.article.articleCover);
        this.$refs.upload.clearFiles();
        this.article.articleCover = this.articleOrigin.articleCover;
      }
    }
  },
  watch: {
    "article.categoryId"(newVal) {
      if (newVal !== this.articleOrigin.categoryId) {
        this.modCount++;
        this.articleBackVO.categoryId = newVal;
      } else {
        this.modCount = --this.modCount < 0 ? 0 : this.modCount;
        delete this.articleBackVO.categoryId;
      }
    },
    "article.articleTitle"(newVal) {
      if (newVal !== this.articleOrigin.articleTitle) {
        this.modCount++;
        this.articleBackVO.articleTitle = newVal;
      } else {
        this.modCount = --this.modCount < 0 ? 0 : this.modCount;
        delete this.articleBackVO.articleTitle;
      }
    },
    "article.articleCover"(newVal) {
      if (newVal !== this.articleOrigin.articleCover) {
        this.modCount++;
        this.articleBackVO.articleCover = newVal;
      } else {
        this.modCount = --this.modCount < 0 ? 0 : this.modCount;
        delete this.articleBackVO.articleCover;
      }
    },
    "article.articleContent"(newVal) {
      if (newVal !== this.articleOrigin.articleContent) {
        this.modCount++;
        this.articleBackVO.articleContent = newVal;
      } else {
        this.modCount = --this.modCount < 0 ? 0 : this.modCount;
        delete this.articleBackVO.articleContent;
      }
    },
    "article.topFlag"(newVal) {
      if (newVal !== this.articleOrigin.topFlag) {
        this.modCount++;
        this.articleBackVO.topFlag = newVal;
      } else {
        this.modCount--;
        delete this.articleBackVO.topFlag;
      }
    },
    "article.publicFlag"(newVal) {
      if (newVal !== this.articleOrigin.publicFlag) {
        this.modCount++;
        this.articleBackVO.publicFlag = newVal;
      } else {
        this.modCount--;
        delete this.articleBackVO.publicFlag;
      }
    },
    "article.hiddenFlag"(newVal) {
      if (newVal !== this.articleOrigin.hiddenFlag) {
        this.modCount++;
        this.articleBackVO.hiddenFlag = newVal;
      } else {
        this.modCount--;
        delete this.articleBackVO.hiddenFlag;
      }
    },
    "article.commentableFlag"(newVal) {
      if (newVal !== this.articleOrigin.commentableFlag) {
        this.modCount++;
        this.articleBackVO.commentableFlag = newVal;
      } else {
        this.modCount--;
        delete this.articleBackVO.commentableFlag;
      }
    },
    "article.tagIdList": {
      handler(newVal) {
        if (
          JSON.stringify(newVal) !==
          JSON.stringify(this.articleOrigin.tagIdList)
        ) {
          this.modCount++;
          this.articleBackVO.tagIdList = newVal;
        } else {
          this.modCount = --this.modCount < 0 ? 0 : this.modCount;
          delete this.articleBackVO.tagIdList;
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
.word-limit-input {
  padding-right: 50px;
}
</style>
