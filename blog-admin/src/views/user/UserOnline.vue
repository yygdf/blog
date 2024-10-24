<template>
  <el-card class="main-card">
    <div class="title">
      {{ isEn ? this.$route.meta.nameEn : this.$route.name }}
    </div>
    <div class="operation-container">
      <el-button
        :disabled="userOnlineIdList.length === 0"
        type="danger"
        size="small"
        icon="el-icon-minus"
        @click="removeStatus = true"
      >
        {{ $t("button.batchOffline") }}
      </el-button>
      <div style="margin-left:auto">
        <el-select
          v-model="flag"
          size="small"
          style="margin-right:1rem"
          :placeholder="$t('input.select')"
          clearable
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
          prefix-icon="el-icon-search"
          :placeholder="$t('auth.inputUsername')"
          style="width: 200px"
          @keyup.enter.native="getUserOnlines(true)"
        />
        <el-button
          type="primary"
          size="small"
          icon="el-icon-search"
          style="margin-left:1rem"
          @click="getUserOnlines(true)"
        >
          {{ $t("button.search") }}
        </el-button>
      </div>
    </div>
    <el-table
      v-loading="loading"
      :data="userOnlineList"
      border
      @selection-change="selectionChange"
    >
      <el-table-column
        type="selection"
        width="40"
        align="center"
        :selectable="checkSelectable"
      />
      <el-table-column
        v-if="showColumnConfig.username"
        prop="username"
        :label="$t('table.user')"
        align="center"
        min-width="120"
      />
      <el-table-column
        v-if="showColumnConfig.avatar"
        prop="avatar"
        :label="$t('table.avatar')"
        align="center"
        width="80"
      >
        <template slot-scope="scope">
          <el-image
            :src="scope.row.avatar === '' ? defaultAvatar : scope.row.avatar"
            style="width: 40px;height: 40px;"
            :preview-src-list="[
              scope.row.avatar === '' ? defaultAvatar : scope.row.avatar
            ]"
          />
        </template>
      </el-table-column>
      <el-table-column
        v-if="showColumnConfig.nickname"
        prop="nickname"
        :label="$t('table.nickname')"
        align="center"
        min-width="120"
      />
      <el-table-column
        v-if="showColumnConfig.loginDevice"
        prop="loginDevice"
        :label="$t('table.loginDevice')"
        align="center"
        width="120"
      >
        <template slot-scope="scope">
          <el-tag style="margin-right:4px;margin-top:4px">
            {{ scope.row.loginDevice }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column
        v-if="showColumnConfig.loginMethod"
        prop="loginMethod"
        :label="$t('table.loginMethod')"
        align="center"
        width="120"
      >
        <template slot-scope="scope">
          <el-tag style="margin-right:4px;margin-top:4px">
            {{ loginMethod(scope.row.loginMethod) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column
        v-if="showColumnConfig.loginPlatform"
        prop="loginPlatform"
        :label="$t('table.loginPlatform')"
        align="center"
        width="160"
      >
        <template slot-scope="scope">
          <el-tag style="margin-right:4px;margin-top:4px">
            {{
              scope.row.loginPlatform
                ? $t("option.backend")
                : $t("option.frontend")
            }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column
        v-if="showColumnConfig.ipAddress"
        prop="ipAddress"
        :label="$t('table.ipAddress')"
        align="center"
        width="120"
      />
      <el-table-column
        v-if="showColumnConfig.ipSource"
        prop="ipSource"
        :label="$t('table.ipSource')"
        align="center"
        width="120"
      />
      <el-table-column
        v-if="showColumnConfig.loginTime"
        prop="loginTime"
        :label="$t('table.loginTime')"
        align="center"
        width="200"
      >
        <template slot-scope="scope">
          <i class="el-icon-time" style="margin-right:5px" />
          {{ scope.row.loginTime | dateTime }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('table.operate')" align="center" width="80">
        <template slot="header">
          <el-popover
            placement="bottom"
            :title="$t('table.showColumn')"
            width="160"
          >
            <div>
              <el-checkbox v-model="showColumnConfig.username">{{
                $t("table.user")
              }}</el-checkbox>
              <el-checkbox v-model="showColumnConfig.avatar">{{
                $t("table.avatar")
              }}</el-checkbox>
              <el-checkbox v-model="showColumnConfig.nickname">{{
                $t("table.nickname")
              }}</el-checkbox>
              <el-checkbox v-model="showColumnConfig.loginDevice">{{
                $t("table.loginDevice")
              }}</el-checkbox>
              <el-checkbox v-model="showColumnConfig.loginMethod">{{
                $t("table.loginMethod")
              }}</el-checkbox>
              <el-checkbox v-model="showColumnConfig.loginPlatform">{{
                $t("table.loginPlatform")
              }}</el-checkbox>
              <el-checkbox v-model="showColumnConfig.ipAddress">{{
                $t("table.ipAddress")
              }}</el-checkbox>
              <el-checkbox v-model="showColumnConfig.ipSource">{{
                $t("table.ipSource")
              }}</el-checkbox>
              <el-checkbox v-model="showColumnConfig.loginTime">{{
                $t("table.loginTime")
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
          <el-popconfirm
            :title="$t('confirm.content9')"
            @confirm="deleteUserOnlines(scope.row.id)"
          >
            <el-button
              :disabled="rootUserIdList.some(e => e === scope.row.id)"
              size="mini"
              type="danger"
              slot="reference"
              class="smaller-btn"
            >
              <i class="el-icon-warning-outline" /> {{ $t("button.offline") }}
            </el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      :total="count"
      :page-size="size"
      :current-page.sync="current"
      :page-sizes="[10, 20]"
      class="pagination-container"
      layout="total, sizes, prev, pager, next, jumper"
      background
      @size-change="sizeChange"
      @current-change="currentChange"
    />
    <el-dialog :visible.sync="removeStatus" width="30%">
      <div class="dialog-title-container" slot="title">
        <i class="el-icon-warning" style="color:#ff9900" />{{
          $t("confirm.tip")
        }}
      </div>
      <div style="font-size:1rem">{{ $t("confirm.content10") }}</div>
      <div slot="footer">
        <el-button @click="removeStatus = false">{{
          $t("confirm.no")
        }}</el-button>
        <el-button type="primary" @click="deleteUserOnlines(null)">
          {{ $t("confirm.yes") }}
        </el-button>
      </div>
    </el-dialog>
  </el-card>
</template>

<script>
export default {
  created() {
    this.loadColumnConfig();
    this.getUserOnlines();
    this.$nextTick(() => {
      this.$refs.input.focus();
    });
  },
  data() {
    return {
      options: [],
      rootUserIdList: [],
      userOnlineList: [],
      userOnlineIdList: [],
      showColumnConfig: {},
      flag: null,
      keywords: null,
      oldKeywords: null,
      loading: true,
      removeStatus: false,
      size: 10,
      count: 0,
      current: 1,
      defaultAvatar: process.env.VUE_APP_STATIC_URL + "img/avatar.png"
    };
  },
  methods: {
    sizeChange(size) {
      this.size = size;
      this.getUserOnlines(true);
    },
    currentChange(current) {
      this.current = current;
      this.getUserOnlines();
    },
    checkSelectable(row) {
      return !this.rootUserIdList.some(e => e === row.id);
    },
    selectionChange(selection) {
      this.userOnlineIdList = [];
      selection.forEach(item => {
        this.userOnlineIdList.push(item.id);
      });
    },
    saveColumnConfig() {
      localStorage.setItem(
        "UserOnlineColumnSet",
        JSON.stringify(this.showColumnConfig)
      );
      document.body.click();
    },
    loadColumnConfig() {
      if (localStorage.getItem("UserOnlineColumnSet")) {
        this.showColumnConfig = JSON.parse(
          localStorage.getItem("UserOnlineColumnSet")
        );
      } else {
        this.showColumnConfig = {
          username: true,
          avatar: true,
          nickname: true,
          loginDevice: true,
          loginMethod: true,
          loginPlatform: true,
          ipAddress: true,
          ipSource: true,
          loginTime: true
        };
      }
    },
    getUserOnlines(resetCurrentPage) {
      if (resetCurrentPage || this.keywords !== this.oldKeywords) {
        this.current = 1;
        this.oldKeywords = this.keywords;
      }
      let params = {
        size: this.size,
        flag: this.flag,
        current: this.current,
        keywords: this.keywords
      };
      params = this.$commonMethod.skipEmptyValue(params);
      this.axios
        .get("/api/back/user/onlines", {
          params
        })
        .then(({ data }) => {
          this.rootUserIdList = data.data.rootUserIdList;
          this.count = data.data.pagePojo.count;
          this.userOnlineList = data.data.pagePojo.pageList;
          this.loading = false;
        });
    },
    deleteUserOnlines(id) {
      let param = {};
      if (id == null) {
        param = { data: this.userOnlineIdList };
      } else {
        param = { data: [id] };
      }
      this.axios.delete("/api/back/user/onlines", param).then(({ data }) => {
        if (data.flag) {
          this.$notify.success({
            title: this.$t("success"),
            message: data.message
          });
          if (param.data.length === this.userOnlineList.length) {
            this.current = --this.current > 1 ? this.current : 1;
          }
          this.getUserOnlines();
        } else {
          this.$notify.error({
            title: this.$t("failure"),
            message: data.message
          });
        }
      });
      this.removeStatus = false;
    }
  },
  computed: {
    loginMethod() {
      return function(method) {
        switch (method) {
          case 1:
            return this.$t("option.email");
          case 2:
            return this.$t("option.qq");
          case 3:
            return this.$t("option.wx");
          case 4:
            return this.$t("option.phone");
        }
      };
    },
    isEn() {
      return this.$i18n.locale === "en_US";
    }
  },
  watch: {
    flag() {
      this.getUserOnlines(true);
    },
    isEn: {
      handler() {
        this.options = [
          {
            value: false,
            label: this.$t("option.frontend")
          },
          {
            value: true,
            label: this.$t("option.backend")
          }
        ];
      },
      immediate: true
    }
  }
};
</script>

<style scoped>
label {
  font-weight: bold !important;
}
</style>
