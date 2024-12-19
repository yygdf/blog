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
        @click="openModel(null)"
      >
        {{ $t("button.add") }}
      </el-button>
      <el-button
        v-if="type !== 7"
        :disabled="multiFileIdList.length === 0"
        type="danger"
        size="small"
        icon="el-icon-minus"
        @click="editStatus = true"
      >
        {{ $t("button.batchDelete") }}
      </el-button>
      <el-button
        v-else
        :disabled="multiFileIdList.length === 0"
        type="danger"
        size="small"
        icon="el-icon-minus"
        @click="removeStatus = true"
      >
        {{ $t("button.batchDelete") }}
      </el-button>
      <el-button
        v-if="type === 7"
        :disabled="multiFileIdList.length === 0"
        type="success"
        size="small"
        icon="el-icon-refresh-left"
        @click="editStatus = true"
      >
        {{ $t("button.batchRestore") }}
      </el-button>
      <div style="margin-left:auto">
        <el-select
          v-if="checkWeight(200)"
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
          v-if="checkWeight(100)"
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
          :placeholder="$t('multiFile.inputName')"
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
          {{ $t("button.search") }}
        </el-button>
        <el-button
          type="primary"
          size="small"
          style="margin-left: 5px"
          @click="getMultiFiles(2)"
        >
          {{ $t("button.deepSearch") }}
        </el-button>
      </div>
    </div>
    <el-table
      v-loading="loading"
      :data="multiFileList"
      :load="load"
      :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
      @row-click="rowClick"
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
      <el-table-column prop="fileNameOrigin" :label="$t('multiFile.name')" />
      <el-table-column
        prop="fileExtension"
        :label="$t('multiFile.type')"
        width="80"
      >
        <template slot-scope="scope">
          {{
            scope.row.fileExtension
              ? scope.row.fileExtension
              : $t("multiFile.directory")
          }}
        </template>
      </el-table-column>
      <el-table-column prop="fileSize" :label="$t('multiFile.size')" width="80">
        <template slot-scope="scope">
          {{ switchFileSize(scope.row.fileSize) }}
        </template>
      </el-table-column>
      <el-table-column
        prop="createTime"
        :label="$t('table.createDate')"
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
        prop="publicFlag"
        :label="$t('switch.public')"
        align="center"
        width="80"
      >
        <template slot-scope="scope">
          <div @click.stop="">
            <el-switch
              v-model="scope.row.publicFlag"
              :active-value="true"
              :inactive-value="false"
              active-color="#13ce66"
              inactive-color="#F4F4F5"
              @change="updateMultiFileStatus(scope.row)"
            />
          </div>
        </template>
      </el-table-column>
      <el-table-column
        prop="hiddenFlag"
        :label="$t('switch.hidden')"
        align="center"
        width="80"
      >
        <template slot-scope="scope">
          <div @click.stop="">
            <el-switch
              v-model="scope.row.hiddenFlag"
              :active-value="true"
              :inactive-value="false"
              active-color="#13ce66"
              inactive-color="#F4F4F5"
              @change="updateMultiFileStatus(scope.row, 3)"
            />
          </div>
        </template>
      </el-table-column>
      <el-table-column :label="$t('table.operate')" align="center" width="240">
        <template slot-scope="scope">
          <el-button
            v-if="type !== 7"
            :disabled="!scope.row.deletableFlag"
            type="primary"
            size="mini"
            class="smaller-btn"
            @click.stop="openModel(scope.row)"
          >
            <i class="el-icon-edit" /> {{ $t("button.edit") }}
          </el-button>
          <el-popconfirm
            v-else
            :title="$t('confirm.content2')"
            @confirm="updateMultiFilesStatus(scope.row)"
          >
            <el-button
              :disabled="scope.row.deletedCount !== 1"
              type="success"
              size="mini"
              slot="reference"
              class="smaller-btn"
              @click.stop=""
            >
              <i class="el-icon-refresh-left" /> {{ $t("button.restore") }}
            </el-button>
          </el-popconfirm>
          <el-popconfirm
            v-if="type !== 7"
            :title="$t('confirm.content3')"
            style="margin-left:10px"
            @confirm="updateMultiFilesStatus(scope.row)"
          >
            <el-button
              :disabled="!scope.row.deletableFlag"
              type="danger"
              size="mini"
              slot="reference"
              class="smaller-btn"
              @click.stop=""
            >
              <i class="el-icon-delete" /> {{ $t("button.delete") }}
            </el-button>
          </el-popconfirm>
          <el-popconfirm
            v-else
            :title="$t('confirm.content4')"
            style="margin-left:10px"
            @confirm="deleteMultiFiles(scope.row)"
          >
            <el-button
              type="danger"
              size="mini"
              slot="reference"
              class="smaller-btn"
              @click.stop=""
            >
              <i class="el-icon-delete" /> {{ $t("button.delete") }}
            </el-button>
          </el-popconfirm>
          <el-dropdown style="margin-left:10px">
            <el-button
              type="primary"
              size="mini"
              class="smaller-btn"
              @click.stop=""
            >
              {{ $t("button.more")
              }}<i class="el-icon-arrow-down el-icon--right"></i>
            </el-button>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item
                v-if="!scope.row.fileExtension"
                :disabled="!scope.row.deletableFlag || type === 7"
                icon="el-icon-plus"
                @click.native="openModel(scope.row, true)"
              >
                {{ $t("button.add") }}
              </el-dropdown-item>
              <el-dropdown-item
                v-if="!scope.row.fileExtension"
                :disabled="!scope.row.deletableFlag || type === 7"
                icon="el-icon-upload2"
                @click.native="openOperateModel(scope.row, true)"
              >
                {{ $t("button.upload") }}
              </el-dropdown-item>
              <el-dropdown-item
                v-else
                icon="el-icon-link"
                @click.native="copyUrl(scope.row.fileFullPath)"
              >
                {{ $t("button.copy") }}
              </el-dropdown-item>
              <el-dropdown-item
                v-if="!scope.row.fileExtension"
                :disabled="
                  !scope.row.deletableFlag || type === 7 || scope.row.publicFlag
                "
                icon="el-icon-lock"
                @click.native="openOperateModel(scope.row, false)"
              >
                {{ $t("button.key") }}
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </template>
      </el-table-column>
    </el-table>
    <el-dialog :visible.sync="editStatus" width="30%">
      <div class="dialog-title-container" slot="title">
        <i class="el-icon-warning" style="color:#ff9900" />{{
          $t("confirm.tip")
        }}
      </div>
      <div style="font-size:1rem">
        {{ type === 7 ? $t("confirm.content15") : $t("confirm.content5") }}
      </div>
      <div slot="footer">
        <el-button @click="editStatus = false">{{
          $t("confirm.no")
        }}</el-button>
        <el-button type="primary" @click="updateMultiFilesStatus(null)">
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
        <el-button type="primary" @click="deleteMultiFiles(null)">
          {{ $t("confirm.yes") }}
        </el-button>
      </div>
    </el-dialog>
    <el-dialog :visible.sync="addOrEditStatus" width="30%">
      <div class="dialog-title-container" slot="title" ref="multiFileTitle" />
      <el-form :model="multiFile" size="medium" label-width="60px">
        <el-form-item :label="$t('multiFile.name')">
          <el-input
            v-model="multiFile.fileNameOrigin"
            ref="input"
            class="word-limit-input form-input-width2"
            maxlength="50"
            :placeholder="$t('multiFile.inputName')"
            show-word-limit
          />
        </el-form-item>
        <el-form-item :label="$t('multiFile.desc')">
          <el-input
            v-model="multiFile.fileDesc"
            class="word-limit-input form-input-width2"
            maxlength="50"
            :placeholder="$t('multiFile.inputDesc')"
            show-word-limit
          />
        </el-form-item>
        <el-form-item
          v-if="multiFile.fileCover != null"
          :label="$t('article.cover')"
        >
          <el-input
            v-model="multiFile.fileCover"
            class="word-limit-input2 form-input-width2"
            maxlength="255"
            :placeholder="staticResourceUrl"
            show-word-limit
          />
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="addOrEditStatus = false">{{
          $t("button.cancel")
        }}</el-button>
        <el-button type="primary" @click="addOrEditMultiFile">
          {{ $t("button.save") }}
        </el-button>
      </span>
    </el-dialog>
    <el-dialog :visible.sync="multiFileUploadFlag" width="30%">
      <div class="dialog-title-container" slot="title">
        {{ $t("button.upload") }}
      </div>
      <div style="margin-top: -1.5rem;margin-bottom: 0.5rem;">
        {{ $t("multiFile.dirName") }}: {{ multiFile.fileNameOrigin }}
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
        <el-button slot="trigger" size="small" type="primary">{{
          $t("button.pick")
        }}</el-button>
        <div slot="tip" class="el-upload__tip">
          {{ $t("multiFile.tip1") }}<br />{{ $t("multiFile.tip2") }}<br />{{
            $t("multiFile.tip3")
          }}
        </div>
      </el-upload>
      <div slot="footer">
        <el-button @click="multiFileUploadFlag = false">{{
          $t("button.cancel")
        }}</el-button>
        <el-button
          :disabled="uploadFileList.length === 0"
          type="danger"
          @click="submitUpload"
        >
          {{ $t("button.upload") }}
        </el-button>
      </div>
    </el-dialog>
    <el-dialog
      :visible.sync="multiFileTokenFlag"
      width="30%"
      @close="cancelAddOrEditMultiFileToken"
    >
      <div class="dialog-title-container" slot="title">
        {{ $t("article.dialogTitle2") }}
      </div>
      <el-form :model="multiFileToken" size="medium" label-width="120px">
        <el-form-item :label="$t('multiFile.dirName')">
          <el-input
            v-model="multiFileToken.fileNameOrigin"
            style="width: 200px"
            disabled
          />
        </el-form-item>
        <el-form-item :label="$t('article.accessKey')">
          <el-input
            v-model="multiFileToken.accessToken"
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
            v-model="multiFileToken.effectiveCount"
            style="width: 200px"
            :min="-1"
            :max="2147483647"
            value="-1"
            controls-position="right"
          />
        </el-form-item>
        <el-form-item :label="$t('article.expireTime')">
          <el-date-picker
            v-model="multiFileToken.expireTime"
            type="datetime"
            style="width: 200px"
            :placeholder="$t('article.selectExpireTime')"
          >
          </el-date-picker>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="multiFileTokenFlag = false">{{
          $t("button.cancel")
        }}</el-button>
        <el-button
          :disabled="tokenValidStatus !== 2"
          type="primary"
          @click="addOrEditMultiFileToken"
        >
          {{ $t("button.save") }}
        </el-button>
      </span>
    </el-dialog>
  </el-card>
</template>

<script>
import { getRowIdentity } from "element-ui/packages/table/src/util.js";
export default {
  created() {
    this.getMultiFiles();
    this.$nextTick(() => {
      this.$refs.input.focus();
    });
  },
  data() {
    return {
      options: [],
      usernameList: [],
      multiFileList: [],
      uploadFileList: [],
      multiFileIdList: [],
      multiFileIdListSelected: [],
      multiFile: {},
      multiFileToken: {},
      multiFileOrigin: {},
      multiFileTokenOrigin: {},
      type: null,
      userId: null,
      multiFileParentId: null,
      keywords: "",
      staticResourceUrl: process.env.VUE_APP_STATIC_URL,
      loading: true,
      editStatus: false,
      removeStatus: false,
      deepSearchFlag: false,
      addOrEditStatus: false,
      multiFileTokenFlag: false,
      multiFileUploadFlag: false,
      tokenValidStatus: 0,
      tokenMap: new Map(),
      treeNodeMap: new Map(),
      lazyLoadIdSet: new Set(),
      batchParentIdSet: new Set(),
      multiFileIdSetExpended: new Set()
    };
  },
  methods: {
    load(tree, treeNode, resolve) {
      if (this.deepSearchFlag) {
        this.keywords = "";
        this.multiFileParentId = tree.id;
        this.getMultiFiles();
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
            tree.children = data.data;
            if (this.multiFileIdListSelected.includes(tree.id)) {
              this.setChildren(data.data, true);
            }
            resolve(data.data);
          });
      }
    },
    refreshLoad(id, allFlag) {
      if (this.treeNodeMap.get(id)) {
        const { tree, treeNode, resolve } = this.treeNodeMap.get(id);
        if (allFlag) {
          this.$set(this.$refs.table.store.states.lazyTreeNodeMap, id, []);
        }
        this.load(tree, treeNode, resolve);
      } else {
        this.getMultiFiles();
      }
    },
    rowClick(row) {
      if (row.fileExtension) {
        this.copyUrl(row.fileFullPath);
      } else if (!this.treeNodeMap.get(row.id)) {
        this.toggleRowExpansion(row);
      } else {
        this.$refs.table.toggleRowExpansion(row);
      }
    },
    expandChange(row, expanded) {
      if (expanded) {
        this.multiFileIdSetExpended.add(row.id);
        this.multiFileParentId = row.id;
        if (this.lazyLoadIdSet.has(row.id)) {
          this.refreshLoad(row.id);
          this.lazyLoadIdSet.delete(row.id);
        }
      } else {
        this.multiFileIdSetExpended.delete(row.id);
        this.multiFileParentId = row.parentId;
      }
    },
    toggleRowExpansion(row) {
      const {
        table: { store }
      } = this.$refs;
      const {
        states: { treeData, rowKey },
        assertRowKey,
        loadData
      } = store;
      assertRowKey();
      const id = getRowIdentity(row, rowKey);
      const data = treeData[id];
      return loadData(row, id, data);
    },
    setChildren(children, type) {
      children
        .filter(
          e =>
            e.deletedCount === 0 ||
            e.deletedCount === 1 ||
            e.deletedCount === -1
        )
        .map(e => {
          this.$nextTick(() => this.$refs.table.toggleRowSelection(e, type));
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
      this.multiFileIdListSelected = [];
      selection
        .sort((e1, e2) => {
          return e1.id - e2.id;
        })
        .forEach(item => {
          this.multiFileIdListSelected.push(item.id);
          if (this.multiFileIdList.every(e => e !== item.parentId)) {
            this.multiFileIdList.push(item.id);
            this.batchParentIdSet.add(item.parentId);
          }
        });
    },
    checkSelectable(row) {
      return (
        row.deletableFlag &&
        !this.multiFileIdListSelected.includes(row.parentId) &&
        (row.deletedCount === 0 || row.deletedCount === 1)
      );
    },
    openModel(multiFile, flag) {
      if (multiFile != null) {
        if (flag) {
          this.multiFile = {
            parentId: multiFile.id,
            fileDesc: "",
            fileCover: "",
            fileNameOrigin: ""
          };
          this.$refs.multiFileTitle.innerHTML = this.$t("multiFile.add2");
        } else {
          this.multiFile = {
            id: multiFile.id,
            parentId: multiFile.parentId,
            fileDesc: multiFile.fileDesc,
            fileNameOrigin: multiFile.fileNameOrigin
          };
          if (multiFile.fileExtension) {
            this.$refs.multiFileTitle.innerHTML = this.$t("multiFile.edit2");
          } else {
            this.multiFile.fileCover = multiFile.fileCover;
            this.$refs.multiFileTitle.innerHTML = this.$t("multiFile.edit1");
          }
        }
      } else {
        this.multiFile = {
          fileDesc: "",
          fileCover: "",
          fileNameOrigin: ""
        };
        this.$refs.multiFileTitle.innerHTML = this.$t("multiFile.add1");
      }
      this.multiFileOrigin = JSON.parse(JSON.stringify(this.multiFile));
      this.$nextTick(() => {
        this.$refs.input.focus();
      });
      this.addOrEditStatus = true;
    },
    openOperateModel(multiFile, flag) {
      if (flag) {
        this.multiFile = {
          id: multiFile.id,
          fileNameOrigin: multiFile.fileNameOrigin
        };
        this.multiFileUploadFlag = true;
      } else {
        if (this.tokenMap.get(multiFile.id)) {
          this.multiFileToken = this.tokenMap.get(multiFile.id);
          this.multiFileTokenOrigin = JSON.parse(
            JSON.stringify(this.multiFileToken)
          );
          if (this.multiFileToken.accessToken !== "") {
            this.tokenValidStatus = 2;
          } else {
            this.tokenValidStatus = 0;
          }
          this.$nextTick(() => {
            this.$refs.input.focus();
          });
          this.multiFileTokenFlag = true;
        } else {
          this.multiFileToken = {
            id: multiFile.id,
            expireTime: null,
            accessToken: "",
            effectiveCount: -1,
            fileNameOrigin: multiFile.fileNameOrigin
          };
          this.tokenValidStatus = 0;
          this.axios
            .get("/api/back/multiFile/token/" + multiFile.id)
            .then(({ data }) => {
              if (data.data.accessToken != null) {
                this.multiFileToken = { ...this.multiFileToken, ...data.data };
                this.tokenMap.set(multiFile.id, this.multiFileToken);
                this.tokenValidStatus = 2;
              }
              this.multiFileTokenOrigin = JSON.parse(
                JSON.stringify(this.multiFileToken)
              );
              this.multiFileTokenFlag = true;
              this.$nextTick(() => {
                this.$refs.input.focus();
              });
            });
        }
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
      this.$message.success(this.$t("multiFile.copy"));
    },
    handleRemove(file) {
      this.uploadFileList = this.uploadFileList.filter(
        item => item.uid !== file.uid
      );
    },
    beforeRemove(file) {
      return this.$confirm(this.$t("confirm.content16") + ` ${file.name} ?`);
    },
    tokenInputChange() {
      if (this.multiFileToken.accessToken.trim() !== "") {
        this.tokenValidStatus = 2;
      } else {
        this.tokenValidStatus = -1;
      }
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
            title: this.$t("success"),
            message: data.message
          });
          if (this.treeNodeMap.get(this.multiFile.id)) {
            if (this.multiFileIdSetExpended.has(this.multiFile.id)) {
              this.refreshLoad(this.multiFile.id);
            } else {
              this.lazyLoadIdSet.add(this.multiFile.id);
            }
          }
        } else {
          this.$notify.error({
            title: this.$t("failure"),
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
    cancelAddOrEditMultiFileToken() {
      this.tokenMap.set(
        this.multiFileTokenOrigin.id,
        this.multiFileTokenOrigin
      );
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
      if (this.deepSearchFlag) {
        params.flag = true;
      }
      if (searchType > 0) {
        this.multiFileList = [];
        this.$set(this.$refs.table.store.states, "lazyTreeNodeMap", {});
        this.treeNodeMap = new Map();
        if (searchType > 1) {
          if (this.keywords.trim() === "") {
            delete params.flag;
            delete params.categoryId;
            this.multiFileParentId = null;
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
    deleteMultiFiles(multiFile) {
      let param = {};
      if (multiFile == null) {
        param = { data: this.multiFileIdList };
      } else {
        param = { data: [multiFile.id] };
        this.batchParentIdSet.add(multiFile.parentId);
      }
      this.axios.delete("/api/back/multiFiles", param).then(({ data }) => {
        if (data.flag) {
          if (
            this.batchParentIdSet.size === 1 &&
            !this.batchParentIdSet.has(-1) &&
            !this.deepSearchFlag
          ) {
            let parentId = this.batchParentIdSet.values().next().value;
            const { tree } = this.treeNodeMap.get(parentId);
            this.refreshLoad(
              parentId,
              tree.children.length === param.idList.length
            );
          } else {
            this.multiFileParentId = null;
            this.getMultiFiles(1);
          }
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
      this.removeStatus = false;
    },
    addOrEditMultiFile() {
      if (this.multiFile.fileNameOrigin.trim() === "") {
        this.$message.error(this.$t("multiFile.nameRule1"));
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
          if (
            this.multiFile.id != null ||
            this.multiFile.parentId == null ||
            this.multiFileIdSetExpended.has(this.multiFile.parentId)
          ) {
            this.refreshLoad(this.multiFile.parentId);
          }
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
      this.addOrEditStatus = false;
    },
    addOrEditMultiFileToken() {
      let param = {
        id: this.multiFileToken.id
      };
      if (this.multiFileToken.expireTime != null) {
        param.expireTime = this.multiFileToken.expireTime;
      }
      if (
        this.multiFileToken.accessToken !==
        this.multiFileTokenOrigin.accessToken
      ) {
        param.accessToken = this.multiFileToken.accessToken;
      }
      if (
        this.multiFileToken.effectiveCount !==
        this.multiFileTokenOrigin.effectiveCount
      ) {
        param.effectiveCount = this.multiFileToken.effectiveCount;
      }
      this.axios.post("/api/back/multiFile/token", param).then(({ data }) => {
        if (data.flag) {
          this.tokenMap.set(this.multiFileToken.id, this.multiFileToken);
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
      this.multiFileTokenFlag = false;
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
            if (this.multiFileIdSetExpended.has(multiFile.id)) {
              this.refreshLoad(multiFile.id);
            } else {
              this.lazyLoadIdSet.add(multiFile.id);
            }
          }
        } else {
          this.$notify.error({
            title: this.$t("failure"),
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
        this.batchParentIdSet.add(multiFile.parentId);
      } else {
        param.idList = this.multiFileIdList;
      }
      if (this.type != null) {
        param.type = this.type;
      }
      this.axios.put("/api/back/multiFiles/status", param).then(({ data }) => {
        if (data.flag) {
          if (
            this.batchParentIdSet.size === 1 &&
            !this.batchParentIdSet.has(-1) &&
            !this.deepSearchFlag
          ) {
            let parentId = this.batchParentIdSet.values().next().value;
            const { tree } = this.treeNodeMap.get(parentId);
            this.refreshLoad(
              parentId,
              tree.children.length === param.idList.length
            );
          } else {
            this.multiFileParentId = null;
            this.getMultiFiles(1);
          }
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
      this.editStatus = false;
    }
  },
  watch: {
    type() {
      this.lazyLoadIdSet.clear();
      this.multiFileIdSetExpended.clear();
      this.deepSearchFlag = false;
      this.multiFileParentId = null;
      this.getMultiFiles();
    },
    userId() {
      this.lazyLoadIdSet.clear();
      this.multiFileIdSetExpended.clear();
      this.deepSearchFlag = false;
      this.multiFileParentId = null;
      this.getMultiFiles();
    },
    isEn: {
      handler() {
        this.options = [
          {
            value: null,
            label: this.$t("option.available")
          },
          {
            value: 7,
            label: this.$t("option.deleted")
          }
        ];
      },
      immediate: true
    }
  },
  computed: {
    switchFileSize() {
      return function(size) {
        if (size >= 1073741824) {
          return Math.ceil((size / 1073741824) * 100) / 100 + "GB";
        }
        if (size >= 1048576) {
          return Math.ceil((size / 1048576) * 100) / 100 + "MB";
        }
        if (size >= 1024) {
          return Math.ceil((size / 1024) * 100) / 100 + "KB";
        }
        if (size > 0) {
          return size + "B";
        }
      };
    },
    isEn() {
      return this.$i18n.locale === "en_US";
    }
  }
};
</script>
