<template>
  <el-card class="main-card">
    <div class="title">{{ this.$route.name }}</div>
    <div class="operation-container">
      <el-button
        type="primary"
        size="small"
        icon="el-icon-plus"
        @click="openModel(null)"
      >
        新增目录
      </el-button>
      <el-button
        v-if="type !== 7"
        :disabled="multiFileIdList.length === 0"
        type="danger"
        size="small"
        icon="el-icon-minus"
        @click="editStatus = true"
      >
        批量删除
      </el-button>
      <el-button
        v-else
        :disabled="multiFileIdList.length === 0"
        type="danger"
        size="small"
        icon="el-icon-minus"
        @click="removeStatus = true"
      >
        批量删除
      </el-button>
      <el-button
        v-if="type === 7"
        :disabled="multiFileIdList.length === 0"
        type="success"
        size="small"
        icon="el-icon-refresh-left"
        @click="editStatus = true"
      >
        批量恢复
      </el-button>
      <div style="margin-left:auto">
        <el-select
          v-if="checkWeight(200)"
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
          v-if="checkWeight(100)"
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
          placeholder="请输入文件名"
          clearable
          @keyup.enter.native="refreshLoad(multiFileParentId)"
        />
        <el-button
          type="primary"
          size="small"
          icon="el-icon-search"
          style="margin-left: 1rem"
          @click="refreshLoad(multiFileParentId)"
        >
          搜索
        </el-button>
        <el-button
          type="primary"
          size="small"
          style="margin-left: 5px"
          @click="getMultiFiles(2)"
        >
          深度搜索
        </el-button>
      </div>
    </div>
    <el-table
      v-loading="loading"
      :data="multiFileList"
      :load="load"
      :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
      @expand-change="expandChange"
      @select="select"
      @select-all="selectAll"
      @selection-change="selectionChange"
      ref="table"
      row-key="id"
      height="720"
      lazy
    >
      <el-table-column
        type="selection"
        align="center"
        width="40"
        :selectable="checkSelectable"
      />
      <el-table-column prop="fileNameOrigin" label="名称" />
      <el-table-column prop="fileExtension" label="类型" width="80">
        <template slot-scope="scope">
          {{ scope.row.fileExtension ? scope.row.fileExtension : "文件夹" }}
        </template>
      </el-table-column>
      <el-table-column prop="fileSize" label="大小" width="80">
        <template slot-scope="scope">
          {{ switchFileSize(scope.row.fileSize) }}
        </template>
      </el-table-column>
      <el-table-column
        prop="createTime"
        label="创建时间"
        align="center"
        width="120"
      >
        <template slot-scope="scope" v-if="scope.row.createTime">
          <i class="el-icon-time" style="margin-right:5px" />
          {{ scope.row.createTime | date }}
        </template>
      </el-table-column>
      <el-table-column
        prop="updateTime"
        label="更新时间"
        align="center"
        width="120"
      >
        <template slot-scope="scope" v-if="scope.row.updateTime">
          <i class="el-icon-time" style="margin-right:5px" />
          {{ scope.row.updateTime | date }}
        </template>
      </el-table-column>
      <el-table-column prop="publicFlag" label="公开" align="center" width="80">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.publicFlag"
            :active-value="true"
            :inactive-value="false"
            active-color="#13ce66"
            inactive-color="#F4F4F5"
            @change="updateMultiFileStatus(scope.row)"
          />
        </template>
      </el-table-column>
      <el-table-column prop="hiddenFlag" label="隐藏" align="center" width="80">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.hiddenFlag"
            :active-value="true"
            :inactive-value="false"
            active-color="#13ce66"
            inactive-color="#F4F4F5"
            @change="updateMultiFileStatus(scope.row, 3)"
          />
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="240">
        <template slot-scope="scope">
          <el-button
            v-if="type !== 7"
            :disabled="!scope.row.deletableFlag"
            type="primary"
            size="mini"
            class="smaller-btn"
            @click="openModel(scope.row)"
          >
            <i class="el-icon-edit" /> 编辑
          </el-button>
          <el-popconfirm
            v-else
            title="确定恢复吗？"
            @confirm="updateMultiFilesStatus(scope.row)"
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
            @confirm="updateMultiFilesStatus(scope.row)"
          >
            <el-button
              :disabled="!scope.row.deletableFlag"
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
            @confirm="deleteMultiFiles(scope.row)"
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
          <el-dropdown style="margin-left:10px">
            <el-button type="primary" size="mini" class="smaller-btn">
              更多操作<i class="el-icon-arrow-down el-icon--right"></i>
            </el-button>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item
                v-if="!scope.row.fileExtension"
                :disabled="!scope.row.deletableFlag || type === 7"
                icon="el-icon-plus"
                @click.native="openModel(scope.row, true)"
              >
                新增
              </el-dropdown-item>
              <el-dropdown-item
                v-if="!scope.row.fileExtension"
                :disabled="!scope.row.deletableFlag || type === 7"
                icon="el-icon-upload2"
                @click.native="openOperateModel(scope.row, true)"
              >
                上传
              </el-dropdown-item>
              <el-dropdown-item
                v-else
                icon="el-icon-link"
                @click.native="copyUrl(scope.row.fileFullPath)"
              >
                复制
              </el-dropdown-item>
              <el-dropdown-item
                v-if="!scope.row.fileExtension"
                :disabled="
                  !scope.row.deletableFlag || type === 7 || scope.row.publicFlag
                "
                icon="el-icon-lock"
                @click.native="openOperateModel(scope.row, false)"
              >
                密令
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </template>
      </el-table-column>
    </el-table>
    <el-dialog :visible.sync="editStatus" width="30%">
      <div class="dialog-title-container" slot="title">
        <i class="el-icon-warning" style="color:#ff9900" />提示
      </div>
      <div style="font-size:1rem">
        是否{{ type === 7 ? "恢复" : "删除" }}选中项？
      </div>
      <div slot="footer">
        <el-button @click="editStatus = false">取 消</el-button>
        <el-button type="primary" @click="updateMultiFilesStatus(null)">
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
        <el-button type="primary" @click="deleteMultiFiles(null)">
          确 定
        </el-button>
      </div>
    </el-dialog>
    <el-dialog :visible.sync="addOrEditStatus" width="30%">
      <div class="dialog-title-container" slot="title" ref="multiFileTitle" />
      <el-form :model="multiFile" size="medium" label-width="80">
        <el-form-item label="名称">
          <el-input
            v-model="multiFile.fileNameOrigin"
            ref="input"
            class="word-limit-input"
            style="width: 200px"
            maxlength="50"
            placeholder="请输入名称"
            show-word-limit
          />
        </el-form-item>
        <el-form-item label="描述">
          <el-input
            v-model="multiFile.fileDesc"
            class="word-limit-input"
            style="width: 200px"
            maxlength="50"
            placeholder="请输入描述"
            show-word-limit
          />
        </el-form-item>
        <el-form-item v-if="multiFile.fileCover != null" label="封面">
          <el-input
            v-model="multiFile.fileCover"
            class="word-limit-input2"
            style="width: 400px"
            maxlength="255"
            :placeholder="staticResourceUrl"
            show-word-limit
          />
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="addOrEditStatus = false">取 消</el-button>
        <el-button type="primary" @click="addOrEditMultiFile">
          确 定
        </el-button>
      </span>
    </el-dialog>
    <el-dialog :visible.sync="multiFileUploadFlag" width="30%">
      <div class="dialog-title-container" slot="title">
        上传文件
      </div>
      <div style="margin-top: -1.5rem;margin-bottom: 0.5rem;">
        目录名称: {{ multiFile.fileNameOrigin }}
      </div>
      <el-upload
        ref="upload"
        :on-remove="handleRemove"
        :before-remove="beforeRemove"
        :auto-upload="false"
        action=""
        :limit="10"
        :on-change="changeMultiFiles"
        multiple
      >
        <el-button slot="trigger" size="small" type="primary"
          >选取文件</el-button
        >
        <div slot="tip" class="el-upload__tip">
          目前支持的文件类型有{jpg,png,gif,pdf,xlsx,docx,pptx,wav,mp3,mp4,avi}<br />其他文件类型请压缩为zip/rar进行上传<br />单个文件不超过100MB
        </div>
      </el-upload>
      <div slot="footer">
        <el-button @click="multiFileUploadFlag = false">取 消</el-button>
        <el-button
          :disabled="uploadFileList.length === 0"
          type="danger"
          @click="submitUpload"
        >
          上 传
        </el-button>
      </div>
    </el-dialog>
    <el-dialog :visible.sync="multiFileTokenFlag" width="30%">
      <div class="dialog-title-container" slot="title">
        密令设置
      </div>
      <div style="margin-top: -1.5rem;margin-bottom: 0.5rem;">
        目录名称: {{ multiFile.fileNameOrigin }}
      </div>
    </el-dialog>
  </el-card>
</template>

<script>
export default {
  created() {
    this.getMultiFiles(0);
    this.$nextTick(() => {
      this.$refs.input.focus();
    });
  },
  data() {
    return {
      options: [
        {
          value: null,
          label: "未删除"
        },
        {
          value: 7,
          label: "已删除"
        }
      ],
      usernameList: [],
      multiFileList: [],
      uploadFileList: [],
      multiFileIdList: [],
      batchParentIdList: [],
      multiFile: {},
      multiFileOrigin: {},
      staticResourceUrl: "",
      type: null,
      userId: null,
      keywords: "",
      multiFileParentId: null,
      loading: true,
      editStatus: false,
      removeStatus: false,
      deepSearchFlag: false,
      addOrEditStatus: false,
      multiFileTokenFlag: false,
      multiFileUploadFlag: false,
      treeNodeMap: new Map()
    };
  },
  methods: {
    load(tree, treeNode, resolve) {
      if (this.deepSearchFlag) {
        this.keywords = "";
        this.multiFileParentId = tree.id;
        this.getMultiFiles(0);
        this.deepSearchFlag = false;
      } else {
        this.treeNodeMap.set(tree.id, { tree, treeNode, resolve });
        let params = {
          type: this.type,
          keywords: this.keywords
        };
        params = this.$commonMethod.skipEmptyValue(params);
        params.categoryId = tree.id;
        this.axios
          .get("/api/back/multiFiles", {
            params
          })
          .then(({ data }) => {
            tree.children = data.data.dataList;
            if (this.multiFileIdList.includes(tree.id)) {
              this.setChildren(data.data.dataList, true);
            }
            resolve(data.data.dataList);
          });
      }
    },
    refreshLoad(id) {
      if (this.treeNodeMap.get(id)) {
        const { tree, treeNode, resolve } = this.treeNodeMap.get(id);
        this.$set(this.$refs.table.store.states.lazyTreeNodeMap, id, []);
        if (tree) {
          this.load(tree, treeNode, resolve);
        }
      } else {
        this.getMultiFiles(0);
      }
    },
    expandChange(row, expanded) {
      if (expanded) {
        this.multiFileParentId = row.id;
      } else {
        this.multiFileParentId = row.parentId;
      }
    },
    toggleSelection(row, select) {
      if (row) {
        this.$nextTick(() => this.$refs.table.toggleRowSelection(row, select));
      }
    },
    setChildren(children, type) {
      children.map(e => {
        this.toggleSelection(e, type);
        if (e.children) {
          this.setChildren(e.children, type);
        }
      });
    },
    select(selection, row) {
      if (selection.some(e => e.id === row.id)) {
        if (row.children) {
          this.setChildren(row.children, true);
        }
      } else {
        if (row.children) {
          this.setChildren(row.children, false);
        }
      }
    },
    selectAll() {
      if (this.$refs.table.store.states.isAllSelected) {
        this.multiFileList
          .filter(e => e.children != null)
          .map(e => this.setChildren(e.children, true));
      } else {
        this.multiFileList
          .filter(e => e.children != null)
          .map(e => this.setChildren(e.children, false));
      }
    },
    selectionChange(selection) {
      this.multiFileIdList = [];
      selection
        .sort((e1, e2) => {
          return e1.id - e2.id;
        })
        .forEach(item => {
          if (this.multiFileIdList.every(e => e !== item.parentId)) {
            this.multiFileIdList.push(item.id);
            this.batchParentIdList.push(item.parentId);
          }
        });
    },
    checkSelectable(row) {
      return row.deletableFlag && !this.multiFileIdList.includes(row.parentId);
    },
    openModel(multiFile, flag = false) {
      if (multiFile != null) {
        if (flag) {
          this.multiFile = {
            parentId: multiFile.id,
            fileDesc: "",
            fileCover: "",
            fileNameOrigin: ""
          };
          this.$refs.multiFileTitle.innerHTML = "添加子目录";
        } else {
          this.multiFile = {
            id: multiFile.id,
            parentId: multiFile.parentId,
            fileDesc: multiFile.fileDesc,
            fileNameOrigin: multiFile.fileNameOrigin
          };
          if (multiFile.fileExtension) {
            this.$refs.multiFileTitle.innerHTML = "修改文件";
          } else {
            this.multiFile.fileCover = multiFile.fileCover;
            this.$refs.multiFileTitle.innerHTML = "修改目录";
          }
        }
      } else {
        this.multiFile = {
          fileDesc: "",
          fileCover: "",
          fileNameOrigin: ""
        };
        this.$refs.multiFileTitle.innerHTML = "添加目录";
      }
      this.multiFileOrigin = JSON.parse(JSON.stringify(this.multiFile));
      this.$nextTick(() => {
        this.$refs.input.focus();
      });
      this.addOrEditStatus = true;
    },
    openOperateModel(multiFile, flag) {
      this.multiFile = {
        id: multiFile.id,
        fileNameOrigin: multiFile.fileNameOrigin
      };
      if (flag) {
        this.multiFileUploadFlag = true;
      } else {
        this.multiFileTokenFlag = true;
      }
    },
    checkWeight(weight) {
      return this.$store.state.weight <= weight;
    },
    copyUrl(url) {
      const input = document.createElement("input");
      input.value = this.staticResourceUrl + url;
      document.body.appendChild(input);
      input.select();
      document.execCommand("copy");
      document.body.removeChild(input);
      this.$message.success("复制成功");
    },
    handleRemove(file) {
      this.uploadFileList = this.uploadFileList.filter(
        item => item.uid !== file.uid
      );
    },
    beforeRemove(file) {
      return this.$confirm(`确定移除 ${file.name} ?`);
    },
    changeMultiFiles(file, fileList) {
      let contentType = file.raw.type;
      if (
        contentType !== "image/jpeg" &&
        contentType !== "image/png" &&
        contentType !== "image/gif" &&
        contentType !== "application/pdf" &&
        contentType !== "audio/wav" &&
        contentType !== "audio/mpeg" &&
        contentType !== "video/mp4" &&
        contentType !== "video/avi" &&
        contentType !== "application/x-zip-compressed" &&
        contentType !== "application/octet-stream" &&
        contentType !==
          "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" &&
        contentType !==
          "application/vnd.openxmlformats-officedocument.wordprocessingml.document" &&
        contentType !==
          "application/vnd.openxmlformats-officedocument.presentationml.presentation"
      ) {
        fileList.pop();
        return false;
      }
      if (file.size >>> 20 > 100) {
        fileList.pop();
        return false;
      }
      this.uploadFileList.push(file.raw);
    },
    uploadMultiFiles() {
      let formData = new FormData();
      formData.append("id", this.multiFile.id);
      this.uploadFileList.forEach(e => formData.append("fileList", e));
      if (this.userId != null) {
        formData.append("userId", this.userId);
      }
      this.axios.post("/api/back/multiFiles", formData).then(({ data }) => {
        if (data.flag) {
          this.$notify.success({
            title: "成功",
            message: data.message
          });
          if (this.treeNodeMap.get(this.multiFile.id)) {
            this.refreshLoad(this.multiFile.id);
          }
        } else {
          this.$notify.error({
            title: "失败",
            message: data.message
          });
        }
        this.$refs.upload.clearFiles();
        this.uploadFileList = [];
        this.multiFileUploadFlag = false;
      });
    },
    submitUpload() {
      this.uploadMultiFiles();
    },
    getMultiFiles(searchType) {
      let params = {
        type: this.type,
        userId: this.userId,
        keywords: this.keywords
      };
      params = this.$commonMethod.skipEmptyValue(params);
      if (this.multiFileParentId != null && this.multiFileParentId !== -1) {
        params.categoryId = this.multiFileParentId;
      }
      if (searchType > 0) {
        this.$set(this.$refs.table.store.states, "lazyTreeNodeMap", {});
        this.treeNodeMap = new Map();
        if (searchType > 1) {
          if (this.keywords.trim() === "") {
            delete params.categoryId;
            this.deepSearchFlag = false;
          } else {
            params.flag = true;
            this.deepSearchFlag = true;
          }
        }
      }
      this.axios
        .get("/api/back/multiFiles", {
          params
        })
        .then(({ data }) => {
          this.multiFileList = data.data.dataList;
          this.staticResourceUrl = data.data.staticResourceUrl;
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
    deleteMultiFiles(multiFile) {
      let param = {};
      if (multiFile == null) {
        param = { data: this.multiFileIdList };
      } else {
        param = { data: [multiFile.id] };
        this.batchParentIdList.push(multiFile.parentId);
      }
      this.axios.delete("/api/back/multiFiles", param).then(({ data }) => {
        if (data.flag) {
          if (
            (this.batchParentIdList.length === 1 ||
              Array.from(new Set(this.batchParentIdList)).length === 1) &&
            this.batchParentIdList[0] !== -1
          ) {
            this.refreshLoad(this.batchParentIdList[0]);
          } else {
            this.multiFileParentId = null;
            this.getMultiFiles(1);
          }
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
    },
    addOrEditMultiFile() {
      if (this.multiFile.fileNameOrigin.trim() === "") {
        this.$message.error("名称不能为空");
        return false;
      }
      let param = this.$commonMethod.skipIdenticalValue(
        this.multiFile,
        this.multiFileOrigin
      );
      if (Object.keys(param).length === 0) {
        return false;
      }
      if (this.multiFile.id != null) {
        param.id = this.multiFile.id;
      } else if (this.multiFile.parentId != null) {
        param.parentId = this.multiFile.parentId;
      }
      this.axios.post("/api/back/multiFile", param).then(({ data }) => {
        if (data.flag) {
          if (this.multiFile.parentId == null) {
            this.getMultiFiles(0);
          } else {
            this.refreshLoad(this.multiFile.parentId);
          }
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
    },
    updateMultiFileStatus(multiFile, type) {
      let param = {
        idList: [multiFile.id]
      };
      if (type != null) {
        param.type = type;
      }
      this.axios.put("/api/back/multiFile/status", param).then(({ data }) => {
        if (data.flag) {
          if (multiFile.fileExtension) {
            this.refreshLoad(multiFile.parentId);
          } else if (this.treeNodeMap.get(multiFile.id)) {
            if (this.multiFileParentId === multiFile.id) {
              this.refreshLoad(multiFile.id);
            } else {
              this.$set(
                this.$refs.table.store.states.lazyTreeNodeMap,
                multiFile.id,
                []
              );
            }
          }
        } else {
          this.$notify.error({
            title: "失败",
            message: data.message
          });
          if (type === 3) {
            multiFile.hiddenFlag = !multiFile.hiddenFlag;
          } else {
            multiFile.publicFlag = !multiFile.publicFlag;
          }
        }
      });
    },
    updateMultiFilesStatus(multiFile) {
      let param = {};
      if (multiFile != null) {
        param.idList = [multiFile.id];
        this.batchParentIdList.push(multiFile.parentId);
      } else {
        param.idList = this.multiFileIdList;
      }
      if (this.type != null) {
        param.type = this.type;
      }
      this.axios.put("/api/back/multiFiles/status", param).then(({ data }) => {
        if (data.flag) {
          if (
            (this.batchParentIdList.length === 1 ||
              Array.from(new Set(this.batchParentIdList)).length === 1) &&
            this.batchParentIdList[0] !== -1
          ) {
            this.refreshLoad(this.batchParentIdList[0]);
          } else {
            this.multiFileParentId = null;
            this.getMultiFiles(1);
          }
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
      this.editStatus = false;
    }
  },
  watch: {
    type() {
      this.multiFileParentId = null;
      this.getMultiFiles(0);
    },
    userId() {
      this.multiFileParentId = null;
      this.getMultiFiles(0);
    }
  },
  computed: {
    switchFileSize() {
      return function(size) {
        if (size >= 1073741824) {
          return Math.ceil((size >>> 30) * 100) / 100 + "GB";
        }
        if (size >= 1048576) {
          return Math.ceil((size >>> 20) * 100) / 100 + "MB";
        }
        if (size >= 1024) {
          return Math.ceil((size >>> 10) * 100) / 100 + "KB";
        }
        if (size > 0) {
          return size + "B";
        }
      };
    }
  }
};
</script>