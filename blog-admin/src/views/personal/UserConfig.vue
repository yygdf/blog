<template>
  <el-card class="main-card">
    {{ isEn ? this.$route.meta.nameEn : this.$route.name }}
    <div class="operation-container">
      <el-button
        v-if="checkWeight(200) && type !== 7"
        :disabled="userConfigIdList.length === 0"
        type="danger"
        size="small"
        icon="el-icon-minus"
        @click="editStatusBatch = true"
      >
        {{ $t("button.batchDelete") }}
      </el-button>
      <el-button
        v-if="type === 7"
        :disabled="userConfigIdList.length === 0"
        type="success"
        size="small"
        icon="el-icon-minus"
        @click="editStatusBatch = true"
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
          style="width: 220px"
          prefix-icon="el-icon-search"
          :placeholder="$t('systemConfig.input')"
          clearable
          @keyup.enter.native="getUserConfigs(true)"
        />
        <el-button
          type="primary"
          size="small"
          icon="el-icon-search"
          style="margin-left:1rem"
          @click="getUserConfigs(true)"
        >
          {{ $t("button.search") }}
        </el-button>
      </div>
    </div>
    <el-table
      v-loading="loading"
      :data="userConfigList"
      border
      @selection-change="selectionChange"
    >
      <el-table-column
        v-if="checkWeight(200)"
        type="selection"
        align="center"
        width="40"
        :selectable="checkSelectable"
      />
      <el-table-column
        v-if="checkWeight(200) && showColumnConfig.username"
        prop="username"
        :label="$t('table.user')"
        align="center"
        min-width="120"
      />
      <el-table-column
        v-if="showColumnConfig.configName"
        prop="configName"
        :label="$t('tag.name')"
        align="center"
        width="200"
      />
      <el-table-column
        v-if="showColumnConfig.configValue"
        prop="configValue"
        :label="$t('systemConfig.value')"
        align="center"
        min-width="240"
      />
      <el-table-column
        v-if="showColumnConfig.configDesc"
        prop="configDesc"
        :label="$t('systemConfig.desc')"
        align="center"
        min-width="240"
      />
      <el-table-column
        v-if="showColumnConfig.createTime"
        prop="createTime"
        :label="$t('table.createDate')"
        align="center"
        width="120"
      >
        <template slot-scope="scope">
          <i class="el-icon-time" style="margin-right:5px" />
          {{ scope.row.createTime | date }}
        </template>
      </el-table-column>
      <el-table-column
        v-if="showColumnConfig.updateTime"
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
      <el-table-column :label="$t('table.operate')" align="center" width="160">
        <template slot="header">
          <el-popover
            placement="bottom"
            :title="$t('table.showColumn')"
            width="160"
          >
            <div>
              <el-checkbox
                v-if="checkWeight(200)"
                v-model="showColumnConfig.username"
                >{{ $t("table.user") }}</el-checkbox
              >
              <el-checkbox v-model="showColumnConfig.configName">{{
                $t("systemConfig.name")
              }}</el-checkbox>
              <el-checkbox v-model="showColumnConfig.configValue">{{
                $t("systemConfig.value")
              }}</el-checkbox>
              <el-checkbox v-model="showColumnConfig.configDesc">{{
                $t("systemConfig.desc")
              }}</el-checkbox>
              <el-checkbox v-model="showColumnConfig.createTime">{{
                $t("table.createDate")
              }}</el-checkbox>
              <el-checkbox v-model="showColumnConfig.updateTime">{{
                $t("table.updateDate")
              }}</el-checkbox>
              <div>
                <el-button
                  type="primary"
                  size="mini"
                  style="float: right"
                  plain
                  @click="saveColumnConfig"
                >
                  {{ $t("button.save") }}
                </el-button>
              </div>
            </div>
            <i slot="reference" class="el-icon-setting table-setting-icon"></i>
          </el-popover>
        </template>
        <template slot-scope="scope">
          <el-button
            :disabled="!checkSelectable(scope.row)"
            type="primary"
            size="mini"
            class="smaller-btn"
            @click="openModel(scope.row)"
          >
            <i class="el-icon-edit" /> {{ $t("button.edit") }}
          </el-button>
          <el-popconfirm
            v-if="type === 7"
            :title="$t('confirm.content2')"
            style="margin-left:10px"
            @confirm="updateUserConfigsStatus(scope.row.id)"
          >
            <el-button
              :disabled="!checkSelectable(scope.row)"
              type="success"
              size="mini"
              slot="reference"
              class="smaller-btn"
            >
              <i class="el-icon-refresh-left" /> {{ $t("button.restore") }}
            </el-button>
          </el-popconfirm>
          <el-popconfirm
            v-if="checkWeight(200) && type !== 7"
            :title="$t('confirm.content3')"
            style="margin-left:10px"
            @confirm="updateUserConfigsStatus(scope.row.id)"
          >
            <el-button
              :disabled="!checkSelectable(scope.row)"
              type="danger"
              size="mini"
              slot="reference"
              class="smaller-btn"
            >
              <i class="el-icon-delete" /> {{ $t("button.delete") }}
            </el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      :total="count"
      :page-size="size"
      :page-sizes="[10, 20]"
      :current-page.sync="current"
      class="pagination-container"
      layout="total, sizes, prev, pager, next, jumper"
      background
      @size-change="sizeChange"
      @current-change="currentChange"
    />
    <el-dialog :visible.sync="editStatusBatch" width="30%">
      <div class="dialog-title-container" slot="title">
        <i class="el-icon-warning" style="color:#ff9900" />{{
          $t("confirm.tip")
        }}
      </div>
      <div style="font-size:1rem">
        {{ type ? $t("confirm.content15") : $t("confirm.content5") }}
      </div>
      <div slot="footer">
        <el-button @click="editStatusBatch = false">{{
          $t("confirm.no")
        }}</el-button>
        <el-button type="primary" @click="updateUserConfigsStatus(null)">
          {{ $t("confirm.yes") }}
        </el-button>
      </div>
    </el-dialog>
    <el-dialog :visible.sync="editStatus" width="30%">
      <div class="dialog-title-container" slot="title" ref="userConfigTitle" />
      <el-form :model="userConfig" size="medium" label-width="90px">
        <el-form-item :label="$t('systemConfig.name')">
          <el-input
            :disabled="true"
            v-model="userConfig.configName"
            class="word-limit-input form-input-width"
            maxlength="50"
            :placeholder="$t('systemConfig.inputName')"
            show-word-limit
          />
        </el-form-item>
        <el-form-item :label="$t('systemConfig.value')">
          <el-input
            v-model="userConfig.configValue"
            ref="input"
            class="word-limit-input2 form-input-width"
            maxlength="255"
            :placeholder="$t('systemConfig.inputValue')"
            show-word-limit
          />
        </el-form-item>
        <el-form-item :label="$t('systemConfig.desc')">
          <el-input
            v-model="userConfig.configDesc"
            class="word-limit-input2 form-input-width"
            maxlength="255"
            :placeholder="$t('systemConfig.inputDesc')"
            show-word-limit
          />
        </el-form-item>
        <el-form-item v-if="assimilateStatus" :label="$t('switch.assimilate')">
          <el-switch
            v-model="userConfig.assimilateFlag"
            :active-value="true"
            :inactive-value="false"
            active-color="#13ce66"
            inactive-color="#F4F4F5"
          />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="editStatus = false">{{
          $t("button.cancel")
        }}</el-button>
        <el-button type="primary" @click="editUserConfig">
          {{ $t("button.save") }}
        </el-button>
      </div>
    </el-dialog>
  </el-card>
</template>

<script>
export default {
  created() {
    this.loadColumnConfig();
    this.getUserConfigs();
    this.$nextTick(() => {
      this.$refs.input.focus();
    });
  },
  data: function() {
    return {
      options: [],
      usernameList: [],
      rootUserIdList: [],
      userConfigList: [],
      userConfigIdList: [],
      userConfig: {},
      userConfigOrigin: {},
      showColumnConfig: {},
      type: null,
      userId: null,
      keywords: null,
      rootUserId: null,
      oldKeywords: null,
      loading: true,
      editStatus: false,
      editStatusBatch: false,
      assimilateStatus: false,
      size: 10,
      count: 0,
      current: 1
    };
  },
  methods: {
    openModel(userConfig) {
      this.userConfig = {
        id: userConfig.id,
        configName: userConfig.configName,
        configValue: userConfig.configValue,
        configDesc: userConfig.configDesc
      };
      this.$refs.userConfigTitle.innerHTML = this.$t("systemConfig.edit");
      this.assimilateStatus =
        userConfig.userId === this.rootUserId &&
        this.$store.state.userId === this.rootUserId;
      this.userConfigOrigin = JSON.parse(JSON.stringify(this.userConfig));
      this.$nextTick(() => {
        this.$refs.input.focus();
      });
      this.editStatus = true;
    },
    sizeChange(size) {
      this.size = size;
      this.getUserConfigs(true);
    },
    checkWeight(weight) {
      return this.$store.state.weight <= weight;
    },
    currentChange(current) {
      this.current = current;
      this.getUserConfigs();
    },
    checkSelectable(row) {
      return (
        this.checkWeight(100) ||
        !this.rootUserIdList.some(e => e === row.userId)
      );
    },
    selectionChange(selection) {
      this.userConfigIdList = [];
      selection.forEach(item => {
        this.userConfigIdList.push(item.id);
      });
    },
    saveColumnConfig() {
      localStorage.setItem(
        "UserConfigColumnSet",
        JSON.stringify(this.showColumnConfig)
      );
      document.body.click();
    },
    loadColumnConfig() {
      if (localStorage.getItem("UserConfigColumnSet")) {
        this.showColumnConfig = JSON.parse(
          localStorage.getItem("UserConfigColumnSet")
        );
      } else {
        this.showColumnConfig = {
          username: true,
          configName: true,
          configValue: true,
          configDesc: true,
          createTime: true,
          updateTime: true
        };
      }
    },
    getUserConfigs(resetCurrentPage) {
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
        .get("/api/back/userConfigs", {
          params
        })
        .then(({ data }) => {
          this.rootUserId = data.data.rootUserId;
          this.rootUserIdList = data.data.rootUserIdList;
          this.count = data.data.pagePojo.count;
          this.userConfigList = data.data.pagePojo.pageList;
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
    editUserConfig() {
      let param = this.$commonMethod.skipIdenticalValue(
        this.userConfig,
        this.userConfigOrigin
      );
      if (Object.keys(param).length === 0) {
        return false;
      }
      param.id = this.userConfig.id;
      this.axios.put("/api/back/userConfig", param).then(({ data }) => {
        if (data.flag) {
          this.$notify.success({
            title: this.$t("success"),
            message: data.message
          });
          this.getUserConfigs();
        } else {
          this.$notify.error({
            title: this.$t("failure"),
            message: data.message
          });
        }
      });
      this.editStatus = false;
    },
    updateUserConfigsStatus(id) {
      let param = {};
      if (id != null) {
        param.idList = [id];
      } else {
        param.idList = this.userConfigIdList;
      }
      if (this.type != null) {
        param.type = this.type;
      }
      this.axios.put("/api/back/userConfigs/status", param).then(({ data }) => {
        if (data.flag) {
          this.$notify.success({
            title: this.$t("success"),
            message: data.message
          });
          if (param.idList.length === this.userConfigList.length) {
            this.current = --this.current > 1 ? this.current : 1;
          }
          this.getUserConfigs();
        } else {
          this.$notify.error({
            title: this.$t("failure"),
            message: data.message
          });
        }
      });
      this.editStatusBatch = false;
    }
  },
  watch: {
    type() {
      this.getUserConfigs(true);
    },
    userId() {
      this.getUserConfigs(true);
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
    isEn() {
      return this.$i18n.locale === "en_US";
    }
  }
};
</script>
