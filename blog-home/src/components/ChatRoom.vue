<template>
  <div>
    <div
      class="chat-container animated bounceInUp"
      v-show="isShow"
      @click="closeAll"
      @contextmenu.prevent.stop="closeAll"
    >
      <div class="header">
        <img
          width="32"
          height="32"
          src="../assets/img/chatRoom.png"
          @click="isShow = false"
          alt=""
        />
        <div style="margin-left:12px">
          <div>{{ $t("chat.room") }}</div>
          <div style="font-size:12px">
            {{ $t("chat.currently") + " " + count + " " + $t("chat.people") }}
          </div>
        </div>
        <v-icon class="close" @click="isShow = false">
          mdi-close
        </v-icon>
      </div>
      <div class="message" id="message">
        <div
          v-show="voiceActive"
          class="voice"
          @mousemove.prevent.stop="translationMove($event)"
          @mouseup.prevent.stop="translationEnd($event)"
        >
          <v-icon ref="voiceClose" class="close-voice">mdi-close</v-icon>
        </div>
        <div
          :class="isMyMessage(item)"
          v-for="(item, index) of chatRecordList"
          :key="index"
        >
          <img
            :src="item.avatar ? item.avatar : defaultAvatar"
            :class="isLeft(item)"
            alt=""
          />
          <div>
            <div class="nickname" v-if="!isSelf(item)">
              {{ item.nickname ? item.nickname : $t("message.guest") }}
              <span v-if="item.ipSource" style="margin-left: 12px">{{
                ipCity(item.ipSource)
              }}</span>
              <span style="margin-left:10px">{{ item.createTime | time }}</span>
            </div>
            <div
              ref="content"
              @contextmenu.prevent.stop="showBack(item, index, $event)"
              :class="isMyContent(item)"
            >
              <div v-if="item.chatType === 3" v-html="item.chatContent" />
              <div
                v-if="item.chatType === 4"
                @click.prevent.stop="playVoice(item)"
              >
                <audio
                  @ended="endVoice(item)"
                  @canplay="getVoiceTime(item)"
                  ref="voices"
                  :src="item.chatContent"
                  style="display:none"
                />
                <v-icon
                  :color="isSelf(item) ? '#fff' : '#000'"
                  ref="plays"
                  style="display:inline-flex;cursor: pointer;"
                >
                  mdi-arrow-right-drop-circle
                </v-icon>
                <v-icon
                  :color="isSelf(item) ? '#fff' : '#000'"
                  ref="pauses"
                  style="display:none;cursor: pointer;"
                >
                  mdi-pause-circle
                </v-icon>
                <span ref="voiceTimes" />
              </div>
              <div class="back-menu" ref="backBtn" @click="back(item)">
                {{ $t("chat.withdraw") }}
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="footer">
        <div class="emoji-box" v-show="isEmoji">
          <Emoji :chooseEmoji="true" @addEmoji="addEmoji" />
        </div>
        <div class="emoji-border" v-show="isEmoji" />
        <v-tooltip top :disabled="userId != null">
          <template v-slot:activator="{ on, attrs }">
            <v-icon
              v-show="!isVoice"
              v-bind="attrs"
              v-on="on"
              @click="openVoice"
              style="margin-right: 8px"
            >
              mdi-microphone
            </v-icon>
          </template>
          <span>{{ $t("chat.tip1") }}</span>
        </v-tooltip>

        <v-icon
          v-show="isVoice"
          @click="isVoice = !isVoice"
          style="margin-right: 8px"
        >
          mdi-keyboard
        </v-icon>
        <textarea
          v-show="!isVoice"
          style="border-radius: 10px"
          ref="chatInput"
          v-model="chatContent"
          @keydown.enter="saveChatRecord($event)"
          :placeholder="$t('chat.input')"
        />
        <button
          class="voice-btn"
          v-show="isVoice"
          @mousedown.prevent.stop="translationStart"
          @mouseup.prevent.stop="translationEnd($event)"
          @touchstart.prevent.stop="translationStart"
          @touchend.prevent.stop="translationEnd($event)"
          @touchmove.prevent.stop="translationMove($event)"
        >
          {{ $t("chat.hold") }}
        </button>
        <i
          class="iconfont my-icon-expression emoji"
          :style="isEmoji ? 'color:#FFC83D' : ''"
          @click.prevent.stop="openEmoji"
        />
        <i :class="isInput" @click="saveChatRecord" style="font-size: 1.5rem" />
      </div>
    </div>
    <div class="chat-btn" @click="open">
      <span class="unread" v-if="unreadCount > 0">{{ unreadCount }}</span>
      <img width="100%" height="100%" src="../assets/img/chatRoom.png" alt="" />
    </div>
  </div>
</template>

<script>
import Recorderx, { ENCODE_TYPE } from "recorderx";
import Emoji from "./Emoji";
import EmojiList from "../assets/js/emoji";
export default {
  components: {
    Emoji
  },
  updated() {
    let ele = document.getElementById("message");
    ele.scrollTop = ele.scrollHeight;
  },
  beforeDestroy() {
    clearInterval(this.heartBeat);
  },
  data: function() {
    return {
      isEmoji: false,
      isShow: false,
      websocket: null,
      chatContent: "",
      chatRecordList: [],
      voiceList: [],
      rc: null,
      ipSource: "",
      ipAddress: "",
      permitFlag: false,
      count: 0,
      unreadCount: 0,
      isVoice: false,
      voiceActive: false,
      startVoiceTime: null,
      WebsocketMessage: {
        type: null,
        data: null
      },
      heartBeat: null,
      defaultAvatar: process.env.VUE_APP_STATIC_URL + "img/avatar.png",
      wsURL: process.env.VUE_APP_WS_URL
    };
  },
  methods: {
    open() {
      if (this.websocket == null) {
        this.connect();
      }
      this.unreadCount = 0;
      this.isShow = !this.isShow;
    },
    openEmoji() {
      this.isEmoji = !this.isEmoji;
      this.isVoice = false;
    },
    openVoice() {
      if (this.userId == null) {
        return false;
      }
      this.isVoice = !this.isVoice;
    },
    connect() {
      this.websocket = new WebSocket(this.wsURL);
      this.websocket.onerror = function() {};
      const that = this;
      this.websocket.onopen = function() {
        that.getChatRecords();
        that.heartBeat = setInterval(function() {
          let beatMessage = {
            type: 9,
            data: "Ping"
          };
          that.websocket.send(JSON.stringify(beatMessage));
        }, 30 * 1000);
      };
      this.websocket.onmessage = function(event) {
        const data = JSON.parse(event.data);
        switch (data.type) {
          case 1:
            that.count = data.data;
            break;
          case 3:
            that.chatRecordList.push(data.data);
            if (!that.isShow) {
              that.unreadCount++;
            }
            break;
          case 4:
            that.voiceList.push(data.data.id);
            that.chatRecordList.push(data.data);
            if (!that.isShow) {
              that.unreadCount++;
            }
            break;
          case 5:
            if (data.data.chatType === 4) {
              that.voiceList.splice(that.voiceList.indexOf(data.data.id), 1);
            }
            for (let i = 0; i < that.chatRecordList.length; i++) {
              if (that.chatRecordList[i].id === data.data.id) {
                that.chatRecordList.splice(i, 1);
                i--;
              }
            }
            break;
          case 9:
            console.log(data.data);
            break;
        }
      };
      this.websocket.onclose = function() {};
    },
    getChatRecords() {
      this.axios.get("/api/chatRecords").then(({ data }) => {
        if (data.flag) {
          this.ipSource = data.data.ipSource;
          this.ipAddress = data.data.ipAddress;
          this.permitFlag = data.data.permitFlag;
          this.chatRecordList = data.data.chatRecordsDTOList;
          this.chatRecordList.forEach(item => {
            if (item.chatType === 4) {
              this.voiceList.push(item.id);
            }
          });
        }
      });
    },
    saveChatRecord(e) {
      e.preventDefault();
      if (this.chatContent.trim() === "") {
        this.$toast({ type: "error", message: this.$t("chat.tip2") });
        return false;
      }
      const reg = /#\[.+?]/g;
      let chatRecord = this.chatContent;
      chatRecord = chatRecord.replace(reg, function(str) {
        return (
          "<img style='vertical-align: middle' src= '" +
          EmojiList[str] +
          "' width='20' height='20' style='padding: 0 1px' alt=''/>"
        );
      });
      this.axios
        .post("/api/chatRecord", { chatContent: chatRecord })
        .then(({ data }) => {
          if (data.flag) {
            this.chatContent = "";
          }
        });
    },
    addEmoji(key) {
      this.isEmoji = false;
      this.$refs.chatInput.focus();
      this.chatContent += key;
    },
    showBack(item, index, e) {
      this.$refs.backBtn.forEach(item => {
        item.style.display = "none";
      });
      if (this.isSelf(item) || this.permitFlag) {
        this.$refs.backBtn[index].style.left = e.offsetX + "px";
        this.$refs.backBtn[index].style.bottom = e.offsetY + "px";
        this.$refs.backBtn[index].style.display = "block";
      }
    },
    back(item) {
      this.axios.put("/api/chatRecord/" + item.id).then();
    },
    closeAll() {
      this.isEmoji = false;
      if (this.chatRecordList.length > 0) {
        this.$refs.backBtn.forEach(item => {
          item.style.display = "none";
        });
      }
    },
    translationStart() {
      this.voiceActive = true;
      this.rc = new Recorderx();
      let that = this;
      this.$nextTick(() => {
        that.rc
          .start()
          .then(() => {
            that.startVoiceTime = new Date();
          })
          .catch();
      });
    },
    translationEnd() {
      this.voiceActive = false;
      this.rc.pause();
      if (new Date() - this.startVoiceTime < 1000) {
        this.$toast({ type: "error", message: this.$t("chat.tip3") });
        return false;
      }
      let wav = this.rc.getRecord({
        encodeTo: ENCODE_TYPE.WAV
      });
      let file = new File([wav], "voice.wav", {
        type: wav.type
      });
      let formData = new FormData();
      formData.append("file", file);
      this.axios.post("/api/chatRecord/voice", formData);
    },
    translationMove() {},
    playVoice(item) {
      let player = this.$refs.voices[this.voiceList.indexOf(item.id)];
      if (player.paused) {
        player.play();
        this.$refs.plays[this.voiceList.indexOf(item.id)].$el.style.display =
          "none";
        this.$refs.pauses[this.voiceList.indexOf(item.id)].$el.style.display =
          "inline-flex";
      } else {
        this.$refs.plays[this.voiceList.indexOf(item.id)].$el.style.display =
          "inline-flex";
        this.$refs.pauses[this.voiceList.indexOf(item.id)].$el.style.display =
          "none";
        player.pause();
      }
    },
    endVoice(item) {
      this.$refs.plays[this.voiceList.indexOf(item.id)].$el.style.display =
        "inline-flex";
      this.$refs.pauses[this.voiceList.indexOf(item.id)].$el.style.display =
        "none";
    },
    getVoiceTime(item) {
      let time = this.$refs.voices[this.voiceList.indexOf(item.id)].duration;
      time = Math.ceil(time);
      let str = "⬝⬝⬝";
      for (let i = 0; i < time; i++) {
        if (i % 2 === 0) {
          str += "⬝";
        }
      }
      this.$refs.voiceTimes[this.voiceList.indexOf(item.id)].innerHTML =
        " " + str + " " + time + " ''";
    },
    ipCity(addr) {
      addr = addr.toString();
      let len = addr.length;
      let half = len >> 1;
      if (addr.substring(0, half) === addr.substring(half, len)) {
        return addr.substring(0, half);
      }
      return addr;
    }
  },
  computed: {
    isSelf() {
      return function(item) {
        return item.userId !== -1 && item.userId === this.userId;
      };
    },
    isLeft() {
      return function(item) {
        return this.isSelf(item)
          ? "user-avatar right-avatar"
          : "user-avatar left-avatar";
      };
    },
    isMyContent() {
      return function(item) {
        return this.isSelf(item) ? "my-content" : "user-content";
      };
    },
    isMyMessage() {
      return function(item) {
        return this.isSelf(item) ? "my-message" : "user-message";
      };
    },
    nickname() {
      return this.$store.state.nickname;
    },
    avatar() {
      return this.$store.state.avatar;
    },
    userId() {
      return this.$store.state.userId;
    },
    isInput() {
      return this.chatContent.trim() !== ""
        ? "iconfont my-icon-paper-plane submit-btn"
        : "iconfont my-icon-paper-plane";
    }
  }
};
</script>

<style scoped>
@media (min-width: 760px) {
  .chat-container {
    position: fixed;
    bottom: 104px;
    right: 20px;
    height: calc(85% - 64px - 20px);
    max-height: 590px !important;
    min-height: 250px !important;
    width: 400px !important;
    border-radius: 16px !important;
  }
  .close {
    margin-left: auto;
  }
}
@media (max-width: 760px) {
  .chat-container {
    position: fixed;
    top: 0;
    bottom: 0;
    right: 0;
    left: 0;
  }
  .close {
    display: block;
    margin-left: auto;
  }
}
.chat-container {
  box-shadow: 0 5px 40px rgba(0, 0, 0, 0.16) !important;
  font-size: 14px;
  background: #f4f6fb;
  z-index: 1200;
}
.theme--dark .chat-container {
  background: #1b1f23 !important;
}
.chat-btn {
  background: #1f93ff;
  border-radius: 100px !important;
  position: fixed;
  bottom: 15px;
  right: 5px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.16) !important;
  cursor: pointer;
  height: 60px !important;
  width: 60px !important;
  z-index: 400 !important;
  user-select: none;
}
.header {
  display: flex;
  align-items: center;
  padding: 20px 24px;
  background: #ffffff;
  border-radius: 1rem 1rem 0 0;
  box-shadow: 0 10px 15px -16px rgba(50, 50, 93, 0.08),
    0 4px 6px -8px rgba(50, 50, 93, 0.04);
}
.theme--dark .header {
  background: #1b1f23;
}
.footer {
  padding: 8px 16px;
  position: absolute;
  width: 100%;
  bottom: 0;
  background: #f7f7f7;
  border-radius: 0 0 1rem 1rem;
  display: flex;
  align-items: center;
}
.theme--dark .footer {
  background: #1b1f23;
}
.footer textarea {
  background: #fff;
  padding-left: 10px;
  padding-top: 8px;
  width: 100%;
  height: 32px;
  outline: none;
  resize: none;
  overflow: hidden;
  font-size: 13px;
}
.voice-btn {
  font-size: 13px;
  outline: none;
  height: 32px;
  width: 100%;
  background: #fff;
  border-radius: 2px;
}
.message {
  position: absolute;
  width: 100%;
  padding: 20px 16px 0 16px;
  top: 80px;
  bottom: 50px;
  overflow-y: auto;
  overflow-x: hidden;
}
.text {
  color: #999;
  text-align: center;
  font-size: 12px;
  margin-bottom: 12px;
}
.user-message {
  display: flex;
  margin-bottom: 10px;
}
.my-message {
  display: flex;
  margin-bottom: 10px;
  justify-content: flex-end;
}
.left-avatar {
  margin-right: 10px;
}
.right-avatar {
  order: 1;
  margin-left: 10px;
}
.user-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
}
.nickname {
  display: flex;
  align-items: center;
  font-size: 12px;
  margin-top: 3px;
  margin-bottom: 5px;
}
.user-content {
  position: relative;
  background-color: #fff;
  padding: 10px;
  border-radius: 5px 20px 20px 20px;
  width: fit-content;
}
.theme--dark .user-content {
  background-color: #111;
}
.my-content {
  position: relative;
  border-radius: 20px 5px 20px 20px;
  padding: 12px;
  background: #12b7f5;
  color: #fff;
}
.submit-btn {
  color: rgb(31, 147, 255);
}
.submit-btn:hover {
  cursor: pointer;
}
.emoji {
  cursor: pointer;
  font-size: 1.3rem;
  margin: 0 8px;
}
.emoji-box {
  position: absolute;
  box-shadow: 0 8px 16px rgba(50, 50, 93, 0.08), 0 4px 12px rgba(0, 0, 0, 0.07);
  background: #fff;
  border-radius: 8px;
  right: 20px;
  bottom: 52px;
  height: 180px;
  width: 300px;
  overflow-y: auto;
  padding: 6px 16px;
}
.emoji-border:before {
  display: block;
  height: 0;
  width: 0;
  content: "";
  border-left: 14px solid transparent;
  border-right: 14px solid transparent;
  border-top: 12px solid #fff;
  bottom: 40px;
  position: absolute;
  right: 43px;
}
.unread {
  text-align: center;
  border-radius: 50%;
  font-size: 14px;
  height: 20px;
  width: 20px;
  position: absolute;
  background: #f24f2d;
  color: #fff;
}
.back-menu {
  font-size: 13px;
  border-radius: 2px;
  position: absolute;
  background: rgba(255, 255, 255, 0.9);
  color: #000;
  width: 80px;
  height: 35px;
  text-align: center;
  line-height: 35px;
  display: none;
}
.voice {
  position: fixed;
  z-index: 1500;
  bottom: 52px;
  left: 0;
  right: 0;
  top: 80px;
  background: rgba(0, 0, 0, 0.8);
}
.close-voice {
  position: absolute;
  bottom: 60px;
  left: 30px;
  display: inline-block;
  height: 50px;
  width: 50px;
  line-height: 50px;
  border-radius: 50%;
  text-align: center;
  background: #fff;
}
</style>
