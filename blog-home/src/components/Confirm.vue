<template>
  <div class="dialog-wrapper" v-if="show">
    <div class="overlay"></div>
    <v-card class="dialog">
      <label class="token-input">
        <input
          v-model="token"
          :placeholder="$t('confirm.input')"
          ref="tokenInput"
          @keyup.enter="ok"
        />
      </label>
      <v-card-actions style="justify-content: center;">
        <v-btn @click="ok" style="margin-right: 1rem;">{{
          $t("button.yes")
        }}</v-btn>
        <v-btn @click="cancel">{{ $t("button.no") }}</v-btn>
      </v-card-actions>
    </v-card>
  </div>
</template>

<script>
export default {
  data() {
    return {
      show: false,
      promiseStatus: null,
      token: ""
    };
  },
  updated() {
    this.$nextTick(() => {
      if (this.$refs.tokenInput) {
        this.$refs.tokenInput.focus();
      }
    });
  },
  methods: {
    confirm() {
      let that = this;
      this.show = true;
      this.token = "";
      return new Promise(function(resolve, reject) {
        that.promiseStatus = { resolve, reject };
      });
    },
    ok() {
      if (this.token.trim() === "") {
        this.cancel();
      } else {
        this.show = false;
        this.promiseStatus && this.promiseStatus.resolve(this.token);
      }
    },
    cancel() {
      this.show = false;
      this.promiseStatus && this.promiseStatus.reject();
    }
  }
};
</script>

<style>
.dialog-wrapper {
  align-items: center;
  display: flex;
  height: 100%;
  justify-content: center;
  left: 0;
  pointer-events: none;
  position: fixed;
  top: 0;
  width: 100%;
  z-index: 6;
  transition: all 0.2s cubic-bezier(0.25, 0.8, 0.25, 1) 0s, z-index 1ms ease 0s;
  outline: none;
}

.dialog {
  overflow-y: auto;
  pointer-events: auto;
  width: 100%;
  z-index: inherit;
  box-shadow: rgba(0, 0, 0, 0.2) 0 11px 15px -7px,
    rgba(0, 0, 0, 0.14) 0 24px 38px 3px, rgba(0, 0, 0, 0.12) 0 9px 46px 8px;
  border-radius: 4px;
  margin: 24px;
  transition: all 0.3s cubic-bezier(0.25, 0.8, 0.25, 1) 0s;
  max-width: 300px;
}

.overlay {
  align-items: center;
  border-radius: inherit;
  display: flex;
  justify-content: center;
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  pointer-events: auto;
  background: #000;
  opacity: 0.46;
}

.token-input {
  display: flex;
  justify-content: center;
  height: 2.5rem;
  margin-top: 1rem;
  margin-left: 50px;
  border: 1px solid #000;
  background-color: #f0f0f0;
  border-radius: 5px;

  width: 203px;
  color: #333;
  font-size: 16px;
}
</style>
