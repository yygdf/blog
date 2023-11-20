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
          @keyup.enter.native="getMultiFiles"
        />
        <el-button
          type="primary"
          size="small"
          icon="el-icon-search"
          style="margin-left:1rem"
          @click="getMultiFiles"
        >
          搜索
        </el-button>
      </div>
    </div>
    <el-table
      v-loading="loading"
      :data="multiFileList"
      :load="load"
      :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
      ref="table"
      row-key="id"
      height="720"
      lazy
    >
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
            @confirm="updateMultiFilesStatus(scope.row.id)"
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
            @confirm="updateMultiFilesStatus(scope.row.id)"
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
            @confirm="deleteMultiFiles(scope.row.id)"
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
                :disabled="!scope.row.deletableFlag"
                icon="el-icon-plus"
                @click.native="openModel(scope.row, true)"
              >
                新增
              </el-dropdown-item>
              <el-dropdown-item
                v-if="!scope.row.fileExtension"
                :disabled="!scope.row.deletableFlag"
                icon="el-icon-upload2"
              >
                上传
              </el-dropdown-item>
              <el-dropdown-item
                v-if="scope.row.fileExtension"
                icon="el-icon-link"
                @click.native="copyUrl(scope.row.fileFullPath)"
              >
                复制
              </el-dropdown-item>
              <el-dropdown-item
                icon="el-icon-lock"
                @click.native="openModel(scope.row)"
              >
                口令
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </template>
      </el-table-column>
    </el-table>
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
        <el-form-item label="封面">
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
  </el-card>
</template>

<script>
export default {
  created() {
    this.getMultiFiles();
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
      multiFileIdList: [],
      multiFile: {},
      multiFileOrigin: {},
      staticResourceUrl: "",
      type: null,
      userId: null,
      keywords: null,
      loading: true,
      editStatus: false,
      addOrEditStatus: false,
      treeNodeMap: new Map()
    };
  },
  methods: {
    load(tree, treeNode, resolve) {
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
          resolve(data.data.dataList);
        });
    },
    refreshLoad(lazyTreeNodeMap, treeNodeMap, id) {
      if (treeNodeMap.get(id)) {
        const { tree, treeNode, resolve } = treeNodeMap.get(id);
        this.$set(lazyTreeNodeMap, id, []);
        if (tree) {
          this.load(tree, treeNode, resolve);
        }
      }
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
            fileCover: multiFile.fileCover,
            fileNameOrigin: multiFile.fileNameOrigin
          };
          this.$refs.multiFileTitle.innerHTML = "修改目录";
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
      this.$notify.success({
        title: "成功",
        message: "复制成功"
      });
    },
    selectionChange(multiFileList) {
      this.multiFileIdList = [];
      multiFileList.forEach(item => {
        this.multiFileIdList.push(item.id);
      });
    },
    getMultiFiles() {
      let params = {
        type: this.type,
        userId: this.userId,
        keywords: this.keywords
      };
      params = this.$commonMethod.skipEmptyValue(params);
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
    deleteMultiFiles(id) {
      let param = {};
      if (id == null) {
        param = { data: this.multiFileIdList };
      } else {
        param = { data: [id] };
      }
      this.axios.delete("/api/back/multiFiles", param).then(({ data }) => {
        if (data.flag) {
          this.$notify.success({
            title: "成功",
            message: data.message
          });
          this.getMultiFiles();
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
      } else {
        if (this.multiFile.parentId != null) {
          param.parentId = this.multiFile.parentId;
        }
      }
      this.axios.post("/api/back/multiFile", param).then(({ data }) => {
        if (data.flag) {
          if (this.multiFile.parentId == null) {
            this.getMultiFiles();
          } else {
            this.refreshLoad(
              this.$refs.table.store.states.lazyTreeNodeMap,
              this.treeNodeMap,
              this.multiFile.parentId
            );
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
        if (!data.flag) {
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
    updateMultiFilesStatus(id) {
      let param = {};
      if (id != null) {
        param.idList = [id];
      } else {
        param.idList = this.multiFileIdList;
      }
      if (this.type != null) {
        param.type = this.type;
      }
      this.axios.put("/api/back/multiFiles/status", param).then(({ data }) => {
        if (data.flag) {
          this.$notify.success({
            title: "成功",
            message: data.message
          });
          if (param.idList.length === this.multiFileList.length) {
            this.current = --this.current > 1 ? this.current : 1;
          }
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
      this.getMultiFiles();
    },
    userId() {
      this.getMultiFiles();
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
