<template>
  <el-card class="main-card">
    <div class="title">{{ this.$route.name }}</div>
    <div class="operation-container">
      <el-button
        v-if="deletedFlag"
        :disabled="messageIdList.length === 0"
        type="danger"
        size="small"
        icon="el-icon-minus"
        @click="removeStatus = true"
      >
        批量删除
      </el-button>
      <el-button
        v-else
        :disabled="messageIdList.length === 0"
        type="danger"
        size="small"
        icon="el-icon-minus"
        @click="editStatus = true"
      >
        批量删除
      </el-button>
      <div style="margin-left:auto">
        <el-select
          v-if="checkWeight(100)"
          v-model="deletedFlag"
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
          style="width:200px"
          prefix-icon="el-icon-search"
          placeholder="请输入用户昵称"
          clearable
          @keyup.enter.native="listMessages"
        />
        <el-button
          type="primary"
          size="small"
          icon="el-icon-search"
          style="margin-left:1rem"
          @click="listMessages"
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
      <el-table-column prop="avatar" label="头像" align="center" width="80">
        <template slot-scope="scope">
          <el-image
            :src="scope.row.avatar"
            style="width: 40px;height: 40px;"
            :preview-src-list="[scope.row.avatar]"
          />
        </template>
      </el-table-column>
      <el-table-column
        prop="nickname"
        label="留言人"
        align="center"
        width="120"
      />
      <el-table-column prop="messageContent" label="留言内容" align="center" />
      <el-table-column
        prop="ipAddress"
        label="ip地址"
        align="center"
        width="120"
      />
      <el-table-column
        prop="ipSource"
        label="ip来源"
        align="center"
        width="120"
      />
      <el-table-column
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
        <template slot-scope="scope">
          <el-popconfirm
            v-if="deletedFlag"
            title="确定恢复吗？"
            @confirm="updateMessagesStatus(scope.row.id)"
          >
            <el-button type="success" size="mini" slot="reference">
              恢复
            </el-button>
          </el-popconfirm>
          <el-popconfirm
            v-if="deletedFlag"
            title="确定彻底删除吗？"
            style="margin-left:10px"
            @confirm="deleteMessages(scope.row.id)"
          >
            <el-button type="danger" size="mini" slot="reference">
              删除
            </el-button>
          </el-popconfirm>
          <el-popconfirm
            v-else
            title="确定删除吗？"
            @confirm="updateMessagesStatus(scope.row.id)"
          >
            <el-button type="danger" size="mini" slot="reference">
              删除
            </el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      :total="count"
      :page-size="size"
      :page-sizes="[10, 20]"
      :current-page="current"
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
    this.listMessages();
    this.$nextTick(() => {
      this.$refs.input.focus();
    });
  },
  data: function() {
    return {
      options: [
        {
          value: false,
          label: "已发送"
        },
        {
          value: true,
          label: "已删除"
        }
      ],
      messageList: [],
      messageIdList: [],
      keywords: null,
      loading: true,
      editStatus: false,
      deletedFlag: false,
      removeStatus: false,
      size: 10,
      count: 0,
      current: 1
    };
  },
  methods: {
    sizeChange(size) {
      this.size = size;
      this.listMessages();
    },
    checkWeight(weight = 200) {
      return this.$store.state.weight <= weight;
    },
    currentChange(current) {
      this.current = current;
      this.listMessages();
    },
    selectionChange(messageList) {
      this.messageIdList = [];
      messageList.forEach(item => {
        this.messageIdList.push(item.id);
      });
    },
    listMessages() {
      this.axios
        .get("/api/back/messages", {
          params: {
            size: this.size,
            current: this.current,
            keywords: this.keywords,
            deletedFlag: this.deletedFlag
          }
        })
        .then(({ data }) => {
          this.count = data.data.count;
          this.messageList = data.data.pageList;
          this.loading = false;
        });
    },
    deleteMessages(id) {
      let param = {};
      if (id != null) {
        param = { data: [id] };
      } else {
        param = { data: this.messageIdList };
      }
      this.axios.delete("/api/back/messages", param).then(({ data }) => {
        if (data.flag) {
          this.$notify.success({
            title: "成功",
            message: data.message
          });
          this.listMessages();
        } else {
          this.$notify.error({
            title: "失败",
            message: data.message
          });
        }
        this.removeStatus = false;
      });
    },
    updateMessagesStatus(id) {
      let param = new URLSearchParams();
      if (id != null) {
        param.append("idList", [id]);
      } else {
        param.append("idList", this.messageIdList);
      }
      param.append("deletedFlag", !this.deletedFlag);
      this.axios.put("/api/back/messages", param).then(({ data }) => {
        if (data.flag) {
          this.$notify.success({
            title: "成功",
            message: data.message
          });
          this.listMessages();
        } else {
          this.$notify.error({
            title: "失败",
            message: data.message
          });
        }
        this.editStatus = false;
      });
    }
  },
  watch: {
    deletedFlag() {
      this.listMessages();
    }
  }
};
</script>
