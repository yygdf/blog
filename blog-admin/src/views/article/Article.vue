<template>
  <el-card class="main-card">
    <div class="title">{{ this.$route.name }}</div>
    <div class="article-title-container">
      <el-input
        v-model="article.articleTitle"
        maxlength="50"
        size="medium"
        placeholder="输入文章标题"
        show-word-limit
      />
      <el-button
        type="danger"
        size="medium"
        class="save-btn"
        @click="addOrEditArticleDraft(true)"
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
      <el-button
        type="warning"
        size="medium"
        style="margin-left:10px"
        @click="exitWithNoSave"
      >
        不保存退出
      </el-button>
    </div>
    <mavon-editor
      v-model="article.articleContent"
      ref="md"
      style="height:calc(100vh - 260px)"
      @imgAdd="uploadArticleImage"
      @imgDel="updateArticleImage"
      @save="addOrEditArticleDraft(true)"
    />
    <el-dialog
      :visible.sync="addOrEditStatus"
      width="40%"
      top="10vh"
      @close="cancelAddOrEditArticle"
    >
      <div class="dialog-title-container" slot="title">
        发表文章
      </div>
      <el-form :model="article" size="medium" label-width="80">
        <el-form-item label="文章分类">
          <el-select
            v-model="article.categoryId"
            placeholder="请选择分类"
            class="form-input-width2"
          >
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
            class="form-input-width2"
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
            :on-change="changeCover"
            :on-remove="updateCover"
            :http-request="uploadCover"
            :before-upload="beforeUpload"
            drag
          >
            <i class="el-icon-upload" v-if="!article.articleCover" />
            <div class="el-upload__text" v-if="!article.articleCover">
              将文件拖到此处, 或<em>点击上传</em><br />
              支持jpg/png/gif文件, 且不超过5MB
            </div>
            <img v-else :src="article.articleCover" width="384" height="216" />
          </el-upload>
        </el-form-item>
        <el-form-item label="封面链接">
          <el-input
            v-model="article.articleCover"
            :placeholder="staticResourceUrl"
            class="word-limit-input2 form-input-width2"
            maxlength="255"
            @focus="articleCoverUpload = article.articleCover"
            @change="changeInputCover"
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
        <el-form-item label="公开">
          <el-switch
            v-model="article.publicFlag"
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
        <el-button type="danger" @click="addOrEditArticle">
          发 表
        </el-button>
      </div>
    </el-dialog>
  </el-card>
</template>

<script>
export default {
  created() {
    const arr = this.$route.path.split("/");
    if (arr[2]) {
      this.articleUserId = arr[2];
      this.axios.get("/api/back/article/" + arr[3]).then(({ data }) => {
        if (data.data.tagIdList == null) {
          data.data.tagIdList = [];
        } else {
          data.data.tagIdList = data.data.tagIdList
            .split(",")
            .map(e => Number(e));
        }
        this.article = data.data;
        this.articleOrigin = JSON.parse(JSON.stringify(data.data));
      });
    } else {
      this.article.articleTitle = this.$moment(new Date()).format("YYYY-MM-DD");
    }
    this.getArticleOption();
  },
  destroyed() {
    if (this.autoSaveFlag) {
      this.addOrEditArticleDraft(false);
    }
  },
  data: function() {
    return {
      article: {
        categoryId: null,
        articleTitle: "",
        articleCover: "",
        articleContent: "",
        topFlag: false,
        publicFlag: true,
        hiddenFlag: false,
        commentableFlag: true,
        tagIdList: []
      },
      articleOrigin: {
        categoryId: null,
        articleTitle: "",
        articleCover: "",
        articleContent: "",
        topFlag: false,
        publicFlag: true,
        hiddenFlag: false,
        commentableFlag: true,
        tagIdList: []
      },
      tagList: [],
      fileNameList: [],
      categoryList: [],
      staticResourceUrl: process.env.VUE_APP_STATIC_URL,
      articleCoverUpload: "",
      articleUserId: null,
      autoSaveFlag: true,
      addOrEditStatus: false,
      articleCoverUploadFlag: false
    };
  },
  methods: {
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
    exitWithNoSave() {
      this.$confirm("确定关闭吗?编辑的内容将不会保存!", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          if (this.fileNameList.length !== 0) {
            this.updateImage(null);
          }
          this.autoSaveFlag = false;
          let tab = this.$store.state.currentTab;
          this.$store.commit("removeTab", tab);
          let tabList = this.$store.state.tabList;
          this.$router.push({ path: tabList[tabList.length - 1].path });
        })
        .catch(() => {});
    },
    splitFileNameByUrl(url) {
      let pathArr = url.split("/");
      return pathArr[pathArr.length - 1].split(".")[0];
    },
    cancelAddOrEditArticle() {
      if (this.articleCoverUploadFlag) {
        this.updateImage(this.article.articleCover);
        this.$refs.upload.clearFiles();
        this.articleCoverUploadFlag = false;
        this.article.articleCover = this.articleOrigin.articleCover;
      }
    },
    updateImage(url) {
      let param;
      if (url == null) {
        param = this.fileNameList;
      } else {
        let fileName = this.splitFileNameByUrl(url);
        let index = this.fileNameList.findIndex(e => e === fileName);
        this.fileNameList.splice(index, 1);
        param = [fileName];
      }
      this.axios.put("/api/back/article/images", param);
    },
    uploadImage(pos, file) {
      if (this.article.id == null) {
        if (this.article.articleTitle.trim() === "") {
          this.$message.error("文章标题不能为空");
          this.$refs.md.$img2Url(pos, "");
          return false;
        }
        if (this.article.articleContent.trim() === "") {
          this.$message.error("文章内容不能为空");
          return false;
        }
        this.axios
          .post(
            "/api/back/article",
            this.$commonMethod.skipIdenticalValue(
              this.article,
              this.articleOrigin
            )
          )
          .then(({ data }) => {
            if (data.flag) {
              this.article.id = data.data;
              this.articleOrigin = JSON.parse(JSON.stringify(this.article));
              let formData = new FormData();
              formData.append("file", file);
              formData.append("articleId", this.article.id);
              if (this.articleUserId != null) {
                formData.append("userId", this.articleUserId);
              }
              this.axios
                .post("/api/back/article/image", formData)
                .then(({ data }) => {
                  if (data.flag) {
                    if (pos == null) {
                      this.article.articleCover = data.data;
                      this.articleCoverUploadFlag = true;
                    } else {
                      this.$refs.md.$img2Url(pos, data.data);
                    }
                    this.fileNameList.push(this.splitFileNameByUrl(data.data));
                  } else {
                    this.$notify.error({
                      title: "失败",
                      message: data.message
                    });
                    return false;
                  }
                });
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
      } else {
        let formData = new FormData();
        formData.append("file", file);
        formData.append("articleId", this.article.id);
        if (this.articleUserId != null) {
          formData.append("userId", this.articleUserId);
        }
        this.axios
          .post("/api/back/article/image", formData)
          .then(({ data }) => {
            if (data.flag) {
              if (pos == null) {
                this.article.articleCover = data.data;
                this.articleCoverUploadFlag = true;
              } else {
                this.$refs.md.$img2Url(pos, data.data);
              }
              this.fileNameList.push(this.splitFileNameByUrl(data.data));
            } else {
              this.$notify.error({
                title: "失败",
                message: data.message
              });
            }
          });
      }
    },
    updateCover(file) {
      if (file && file.status === "success") {
        this.updateImage(this.article.articleCover);
        this.article.articleCover = "";
        this.articleCoverUploadFlag = false;
      }
    },
    changeInputCover() {
      if (this.articleCoverUploadFlag) {
        this.$refs.upload.clearFiles();
        this.updateImage(this.articleCoverUpload);
        this.articleCoverUploadFlag = false;
      }
    },
    uploadCover(form) {
      if (this.articleCoverUploadFlag) {
        this.updateImage(this.article.articleCover);
      }
      this.uploadImage(null, form.file);
    },
    beforeUpload(file) {
      let contentType = file.type;
      if (
        contentType !== "image/jpeg" &&
        contentType !== "image/png" &&
        contentType !== "image/gif"
      ) {
        this.$message.error("上传的图片只能是jpg, png, gif格式");
        return false;
      }
      if (file.size >>> 20 > 5) {
        this.$message.error("上传图片的大小不能超过5MB");
        return false;
      }
      return true;
    },
    changeCover(file, fileList) {
      if (fileList.length > 1) {
        fileList.splice(0, 1);
      }
    },
    updateArticleImage(pos) {
      this.updateImage(pos[0]);
    },
    uploadArticleImage(pos, file) {
      this.uploadImage(pos, file);
    },
    addOrEditArticleDraft(flag) {
      if (this.article.articleTitle.trim() === "") {
        if (flag) {
          this.$message.error("文章标题不能为空");
        }
        return false;
      }
      if (this.article.articleContent.trim() === "") {
        if (flag) {
          this.$message.error("文章内容不能为空");
        }
        return false;
      }
      let param = this.$commonMethod.skipIdenticalValue(
        this.article,
        this.articleOrigin
      );
      if (this.article.draftFlag === false) {
        if (Object.keys(param).length === 0) {
          return false;
        }
        param.draftFlag = true;
      } else {
        if (Object.keys(param).length === 0) {
          return false;
        }
      }
      if (this.article.id != null) {
        param.id = this.article.id;
      }
      this.axios.post("/api/back/article", param).then(({ data }) => {
        if (data.flag) {
          this.article.id = data.data;
          this.article.draftFlag = true;
          this.articleOrigin = JSON.parse(JSON.stringify(this.article));
          this.$notify.success({
            title: "成功",
            message: "保存草稿成功"
          });
          this.fileNameList = [];
          this.articleCoverUploadFlag = false;
        } else {
          this.$notify.error({
            title: "失败",
            message: "保存草稿失败"
          });
        }
      });
    },
    getArticleOption() {
      this.axios
        .get("/api/back/article/option", {
          params: { userId: this.articleUserId }
        })
        .then(({ data }) => {
          this.tagList = data.data.tagDTOList;
          this.categoryList = data.data.categoryDTOList;
        });
    },
    addOrEditArticle() {
      if (!this.article.categoryId) {
        this.$message.error("文章分类不能为空");
        this.$refs.upload.clearFiles();
        return false;
      }
      let param = this.$commonMethod.skipIdenticalValue(
        this.article,
        this.articleOrigin
      );
      if (this.article.draftFlag !== false) {
        param.draftFlag = false;
      }
      if (Object.keys(param).length === 0) {
        return false;
      }
      if (this.article.id != null) {
        param.id = this.article.id;
      }
      this.axios.post("/api/back/article", param).then(({ data }) => {
        if (data.flag) {
          this.article.id = data.data;
          this.article.draftFlag = false;
          this.articleOrigin = JSON.parse(JSON.stringify(this.article));
          this.$notify.success({
            title: "成功",
            message: data.message
          });
          this.fileNameList = [];
          this.articleCoverUploadFlag = false;
        } else {
          this.$notify.error({
            title: "失败",
            message: data.message
          });
        }
        this.addOrEditStatus = false;
      });
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
>>> .add-image-link .title {
  font-size: 16px !important;
  margin-top: -20px !important;
}
</style>
