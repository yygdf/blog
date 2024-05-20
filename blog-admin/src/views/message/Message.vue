<template>
  <el-card class="main-card">
    <div class="title">{{ this.$route.name }}</div>
    <div class="operation-container">
      <el-button
        v-if="type !== 7"
        :disabled="messageIdList.length === 0"
        type="danger"
        size="small"
        icon="el-icon-minus"
        @click="editStatus = true"
      >
        批量删除
      </el-button>
      <el-button
        v-else
        :disabled="messageIdList.length === 0"
        type="danger"
        size="small"
        icon="el-icon-minus"
        @click="removeStatus = true"
      >
        批量删除
      </el-button>
      <div style="margin-left:auto">
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
          placeholder="请输入用户昵称"
          clearable
          @keyup.enter.native="getMessages(false)"
        />
        <el-button
          type="primary"
          size="small"
          icon="el-icon-search"
          style="margin-left:1rem"
          @click="getMessages(false)"
        >
          搜索
        </el-button>
      </div>
    </div>
    <el-table
      v-loading="loading"
      :data="messageList"
      border
      @selection-change="selectionChange"
    >
      <el-table-column type="selection" align="center" width="40" />
      <el-table-column
        v-if="showColumnConfig.nickname"
        prop="nickname"
        label="留言人"
        align="center"
        min-width="120"
      />
      <el-table-column
        v-if="showColumnConfig.avatar"
        prop="avatar"
        label="头像"
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
        v-if="showColumnConfig.messageContent"
        prop="messageContent"
        label="留言内容"
        align="center"
        min-width="240"
      />
      <el-table-column
        v-if="showColumnConfig.ipAddress"
        prop="ipAddress"
        label="ip地址"
        align="center"
        width="120"
      />
      <el-table-column
        v-if="showColumnConfig.ipSource"
        prop="ipSource"
        label="ip来源"
        align="center"
        width="120"
      />
      <el-table-column
        v-if="showColumnConfig.createTime"
        prop="createTime"
        label="留言时间"
        align="center"
        width="200"
      >
        <template slot-scope="scope">
          <i class="el-icon-time" style="margin-right:5px" />
          {{ scope.row.createTime | dateTime }}
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="160">
        <template slot="header">
          <el-popover placement="bottom" title="选择显示列" width="160">
            <div>
              <el-checkbox v-model="showColumnConfig.nickname"
                >留言人</el-checkbox
              >
              <el-checkbox v-model="showColumnConfig.avatar">头像</el-checkbox>
              <el-checkbox v-model="showColumnConfig.messageContent"
                >留言内容</el-checkbox
              >
              <el-checkbox v-model="showColumnConfig.ipAddress"
                >ip地址</el-checkbox
              >
              <el-checkbox v-model="showColumnConfig.ipSource"
                >ip来源</el-checkbox
              >
              <el-checkbox v-model="showColumnConfig.createTime"
                >留言时间</el-checkbox
              >
              <div>
                <el-button
                  type="primary"
                  size="mini"
                  style="float: right"
                  plain
                  @click="saveColumnConfig"
                >
                  保存
                </el-button>
              </div>
            </div>
            <i slot="reference" class="el-icon-setting table-setting-icon"></i>
          </el-popover>
        </template>
        <template slot-scope="scope">
          <el-popconfirm
            v-if="checkWeight(100)"
            title="确定恢复吗？"
            @confirm="updateMessagesStatus(scope.row.id)"
          >
            <el-button
              :disabled="type == null"
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
            @confirm="updateMessagesStatus(scope.row.id)"
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
          <el-popconfirm
            v-else
            title="确定彻底删除吗？"
            style="margin-left:10px"
            @confirm="deleteMessages(scope.row.id)"
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
    <el-dialog :visible.sync="editStatus" width="30%">
      <div class="dialog-title-container" slot="title">
        <i class="el-icon-warning" style="color:#ff9900" />提示
      </div>
      <div style="font-size:1rem">是否删除选中项？</div>
      <div slot="footer">
        <el-button @click="editStatus = false">取 消</el-button>
        <el-button type="primary" @click="updateMessagesStatus(null)">
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
        <el-button type="primary" @click="deleteMessages(null)">
          确 定
        </el-button>
      </div>
    </el-dialog>
  </el-card>
</template>

<script>
export default {
  created() {
    this.loadColumnConfig();
    this.getMessages();
    this.$nextTick(() => {
      this.$refs.input.focus();
    });
  },
  data: function() {
    return {
      options: [
        {
          value: null,
          label: "已发送"
        },
        {
          value: 7,
          label: "已删除"
        }
      ],
      messageList: [],
      messageIdList: [],
      showColumnConfig: {},
      type: null,
      keywords: null,
      oldKeywords: null,
      loading: true,
      editStatus: false,
      removeStatus: false,
      size: 10,
      count: 0,
      current: 1,
      defaultAvatar: require("../../assets/img/default/avatar.png")
    };
  },
  methods: {
    sizeChange(size) {
      this.size = size;
      this.getMessages(true);
    },
    checkWeight(weight) {
      return this.$store.state.weight <= weight;
    },
    currentChange(current) {
      this.current = current;
      this.getMessages();
    },
    selectionChange(selection) {
      this.messageIdList = [];
      selection.forEach(item => {
        this.messageIdList.push(item.id);
      });
    },
    saveColumnConfig() {
      localStorage.setItem(
        "MessageColumnSet",
        JSON.stringify(this.showColumnConfig)
      );
      document.body.click();
    },
    loadColumnConfig() {
      if (localStorage.getItem("MessageColumnSet")) {
        this.showColumnConfig = JSON.parse(
          localStorage.getItem("MessageColumnSet")
        );
      } else {
        this.showColumnConfig = {
          nickname: true,
          avatar: true,
          messageContent: true,
          ipAddress: true,
          ipSource: true,
          createTime: true
        };
      }
    },
    getMessages(resetCurrentPage) {
      if (resetCurrentPage || this.keywords !== this.oldKeywords) {
        this.current = 1;
        this.oldKeywords = this.keywords;
      }
      let params = {
        size: this.size,
        type: this.type,
        current: this.current,
        keywords: this.keywords
      };
      params = this.$commonMethod.skipEmptyValue(params);
      this.axios
        .get("/api/back/messages", {
          params
        })
        .then(({ data }) => {
          this.count = data.data.count;
          this.messageList = data.data.pageList;
          this.loading = false;
        });
    },
    deleteMessages(id) {
      let param = {};
      if (id == null) {
        param = { data: this.messageIdList };
      } else {
        param = { data: [id] };
      }
      this.axios.delete("/api/back/messages", param).then(({ data }) => {
        if (data.flag) {
          this.$notify.success({
            title: "成功",
            message: data.message
          });
          if (param.data.length === this.messageList.length) {
            this.current = --this.current > 1 ? this.current : 1;
          }
          this.getMessages();
        } else {
          this.$notify.error({
            title: "失败",
            message: data.message
          });
        }
      });
      this.removeStatus = false;
    },
    updateMessagesStatus(id) {
      let param = {};
      if (id != null) {
        param.idList = [id];
      } else {
        param.idList = this.messageIdList;
      }
      if (this.type != null) {
        param.type = this.type;
      }
      this.axios.put("/api/back/messages/status", param).then(({ data }) => {
        if (data.flag) {
          this.$notify.success({
            title: "成功",
            message: data.message
          });
          if (param.idList.length === this.messageList.length) {
            this.current = --this.current > 1 ? this.current : 1;
          }
          this.getMessages();
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
      this.getMessages(true);
    }
  }
};
</script>
