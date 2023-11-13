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
          @keyup.enter.native="getMultiFiles(false)"
        />
        <el-button
          type="primary"
          size="small"
          icon="el-icon-search"
          style="margin-left:1rem"
          @click="getMultiFiles(false)"
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
      row-key="id"
      height="720"
      lazy
    >
      <el-table-column prop="fileNameOrigin" label="名称" />
      <el-table-column prop="publicFlag" label="公开" align="center" width="80">
        <template v-if="scope.row.fileMark === 0" slot-scope="scope">
          <el-switch
            v-model="scope.row.publicFlag"
            :active-value="true"
            :inactive-value="false"
            active-color="#13ce66"
            inactive-color="#F4F4F5"
            @change="changeMultiFileStatus(scope.row)"
          />
        </template>
      </el-table-column>
      <el-table-column prop="hiddenFlag" label="隐藏" align="center" width="80">
        <template v-if="scope.row.fileMark === 0" slot-scope="scope">
          <el-switch
            v-model="scope.row.hiddenFlag"
            :active-value="true"
            :inactive-value="false"
            active-color="#13ce66"
            inactive-color="#F4F4F5"
            @change="changeMultiFileStatus(scope.row, 3)"
          />
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
      <el-table-column label="操作" align="center" width="200">
        <template slot-scope="scope">
          <el-button
            :disabled="!scope.row.deletableFlag || scope.row.fileMark !== 0"
            type="primary"
            size="mini"
            class="smaller-btn"
            @click="openModel(scope.row, true)"
          >
            <i class="el-icon-plus" /> 新增
          </el-button>
          <el-button
            :disabled="!scope.row.deletableFlag || scope.row.fileMark !== 0"
            type="primary"
            size="mini"
            class="smaller-btn"
            @click="openModel(scope.row)"
          >
            <i class="el-icon-plus" /> 上传
          </el-button>
          <el-button
            v-if="type !== 7"
            :disabled="!scope.row.deletableFlag"
            type="primary"
            size="mini"
            @click="openModel(scope.row)"
          >
            编辑
          </el-button>
          <el-popconfirm
            v-else
            title="确定恢复吗？"
            @confirm="updateMultiFilesStatus(scope.row.id)"
          >
            <el-button type="success" size="mini" slot="reference">
              恢复
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
            >
              删除
            </el-button>
          </el-popconfirm>
          <el-popconfirm
            v-else
            title="确定彻底删除吗？"
            style="margin-left:10px"
            @confirm="deleteMultiFiles(scope.row.id)"
          >
            <el-button type="danger" size="mini" slot="reference">
              删除
            </el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <el-dialog :visible.sync="addOrEditStatus" width="30%">
      <div class="dialog-title-container" slot="title" ref="resourceTitle" />
      <el-form :model="multiFile" size="medium" label-width="80">
        <el-form-item label="文件名称">
          <el-input
            v-model="multiFile.fileName"
            ref="input"
            class="word-limit-input"
            style="width: 200px"
            maxlength="50"
            placeholder="请输入文件名称"
            show-word-limit
          />
        </el-form-item>
        <el-form-item label="文件描述">
          <el-input
            v-model="multiFile.fileDesc"
            class="word-limit-input"
            style="width: 200px"
            maxlength="50"
            placeholder="请输入文件描述"
            show-word-limit
          />
        </el-form-item>
        <el-form-item label="文件封面">
          <el-input
            v-model="multiFile.fileCover"
            class="word-limit-input2"
            style="width: 200px"
            maxlength="255"
            placeholder="请输入文件封面"
            show-word-limit
          />
        </el-form-item>
      </el-form>
      <el-form :model="multiFile" :inline="true" size="medium" label-width="80">
        <el-form-item label="公开">
          <el-switch
            v-model="multiFile.publicFlag"
            :active-value="true"
            :inactive-value="false"
            active-color="#13ce66"
            inactive-color="#F4F4F5"
          />
        </el-form-item>
        <el-form-item label="隐藏">
          <el-switch
            v-model="multiFile.hiddenFlag"
            :active-value="true"
            :inactive-value="false"
            active-color="#13ce66"
            inactive-color="#F4F4F5"
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
      type: null,
      userId: null,
      keywords: null,
      oldKeywords: null,
      loading: true,
      addOrEditStatus: false,
      size: 10,
      count: 0,
      current: 1
    };
  },
  methods: {
    openModel(multiFile, flag = false) {
      if (multiFile != null) {
        if (flag) {
          this.multiFile = {
            multiDirId: multiFile.id,
            fileName: "",
            fileDesc: "",
            fileCover: "",
            publicFlag: multiFile.publicFlag,
            hiddenFlag: multiFile.hiddenFlag
          };
          this.$refs.friendLinkTitle.innerHTML = "添加子目录";
        } else {
          this.multiFile = {
            id: multiFile.id,
            fileName: multiFile.fileName,
            fileDesc: multiFile.fileDesc,
            fileCover: multiFile.fileCover,
            publicFlag: multiFile.publicFlag,
            hiddenFlag: multiFile.hiddenFlag
          };
          this.$refs.friendLinkTitle.innerHTML = "修改目录";
        }
      } else {
        this.multiFile = {
          fileName: "",
          fileDesc: "",
          fileCover: "",
          publicFlag: true,
          hiddenFlag: false
        };
        this.$refs.friendLinkTitle.innerHTML = "添加目录";
      }
      this.multiFileOrigin = JSON.parse(JSON.stringify(this.multiFile));
      this.$nextTick(() => {
        this.$refs.input.focus();
      });
      this.addOrEditStatus = true;
    },
    sizeChange(size) {
      this.size = size;
      this.getMultiFiles(true);
    },
    checkWeight(weight) {
      return this.$store.state.weight <= weight;
    },
    currentChange(current) {
      this.current = current;
      this.getMultiFiles();
    },
    selectionChange(multiFileList) {
      this.multiFileIdList = [];
      multiFileList.forEach(item => {
        this.multiFileIdList.push(item.id);
      });
    },
    getMultiFiles(resetCurrentPage = false) {
      if (resetCurrentPage || this.keywords !== this.oldKeywords) {
        this.current = 1;
        this.oldKeywords = this.keywords;
      }
      let params = {
        size: this.size,
        type: this.type,
        userId: this.userId,
        current: this.current,
        keywords: this.keywords
      };
      params = this.$commonMethod.skipEmptyValue(params);
      this.axios
        .get("/api/back/multiFiles", {
          params
        })
        .then(({ data }) => {
          this.multiFileList = data.data;
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
      let param = { data: [id] };
      this.axios.delete("/api/back/resources", param).then(({ data }) => {
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
      if (this.multiFile.fileName.trim() === "") {
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
        if (this.multiFile.multiDirId != null) {
          param.multiDirId = this.multiFile.multiDirId;
        }
      }
      this.axios.post("/api/back/multiFile", param).then(({ data }) => {
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
      this.addOrEditStatus = false;
    },
    changeMultiFileStatus(resource, type) {
      let param = {
        idList: [resource.id]
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
          if (type === 10) {
            resource.anonymousFlag = !resource.anonymousFlag;
          } else {
            resource.disabledFlag = !resource.disabledFlag;
          }
        }
      });
    }
  }
};
</script>
