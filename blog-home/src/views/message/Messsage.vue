<template>
  <div>
    <div class="message-banner" :style="cover">
      <div class="message-container">
        <h1 class="message-title">留言板</h1>
        <div class="message-input-wrapper">
          <input
            v-model="messageContent"
            @click="show = true"
            @keyup.enter="addToList"
            maxlength="100"
            placeholder="说点什么吧"
          />
          <button @click="addToList" v-show="show">
            发送
          </button>
        </div>
      </div>
      <div class="barrage-container">
        <vue-baberrage :barrageList="barrageList">
          <template v-slot:default="slotProps">
            <span class="barrage-items">
              <img
                :src="
                  slotProps.item.avatar ? slotProps.item.avatar : defaultAvatar
                "
                width="30"
                height="30"
                style="border-radius:50%"
                alt=""
              />
              <span class="ml-2"
                >{{
                  slotProps.item.nickname ? slotProps.item.nickname : "游客"
                }}
                :</span
              >
              <span class="ml-2">{{ slotProps.item.messageContent }}</span>
            </span>
          </template>
        </vue-baberrage>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  mounted() {
    this.getMessages();
  },
  data() {
    return {
      show: false,
      messageContent: "",
      barrageList: [],
      defaultAvatar: process.env.VUE_APP_STATIC_URL + "img/avatar.png"
    };
  },
  methods: {
    addToList() {
      if (this.messageContent.trim() === "") {
        this.$toast({ type: "error", message: "留言不能为空" });
        return false;
      }
      let message = {
        messageSpeed:
          this.messageContent.trim().length > 50
            ? Math.floor(Math.random() * 5) + 6
            : Math.floor(Math.random() * 8) + 3,
        messageContent: this.messageContent
      };
      this.axios.post("/api/message", message).then(({ data }) => {
        if (data.flag) {
          this.messageContent = "";
          this.barrageList.push({
            avatar: this.$store.state.avatar,
            nickname: this.$store.state.nickname,
            time: message.messageSpeed,
            messageContent: message.messageContent
          });
        }
      });
    },
    getMessages() {
      this.axios.get("/api/messages").then(({ data }) => {
        if (data.flag) {
          this.barrageList = data.data;
        }
      });
    }
  },
  computed: {
    cover() {
      return (
        "background: url(" +
        this.$store.state.blogConfig.message_banner_cover +
        ") center center / cover no-repeat"
      );
    }
  }
};
</script>

<style scoped>
.message-banner {
  position: absolute;
  top: -60px;
  left: 0;
  right: 0;
  height: 100vh;
  background: #49b1f5;
  animation: header-effect 1s;
}
.message-title {
  color: #eee;
  animation: title-scale 1s;
}
.message-container {
  position: absolute;
  width: 360px;
  top: 35%;
  left: 0;
  right: 0;
  text-align: center;
  z-index: 5;
  margin: 0 auto;
  color: #fff;
}
.message-input-wrapper {
  display: flex;
  justify-content: center;
  height: 2.5rem;
  margin-top: 2rem;
}
.message-input-wrapper input {
  outline: none;
  width: 70%;
  border-radius: 20px;
  height: 100%;
  padding: 0 1.25rem;
  color: #eee;
  border: #fff 1px solid;
}
.message-input-wrapper input::-webkit-input-placeholder {
  color: #eeee;
}
.message-input-wrapper button {
  outline: none;
  border-radius: 20px;
  height: 100%;
  padding: 0 1.25rem;
  border: #fff 1px solid;
}
.barrage-container {
  position: absolute;
  top: 50px;
  left: 0;
  right: 0;
  bottom: 0;
  height: calc(100% - 50px);
  width: 100%;
}
.barrage-items {
  background: rgba(0, 0, 0, 0.7);
  border-radius: 100px;
  color: #fff;
  padding: 5px 10px 5px 5px;
  align-items: center;
  display: flex;
}
</style>
