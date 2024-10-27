<template>
  <v-dialog
    v-model="resetFlag"
    :fullscreen="this.$store.state.mobileFlag"
    max-width="460"
  >
    <v-card class="login-container" style="border-radius:4px">
      <v-icon class="float-right" @click="resetFlag = false">
        mdi-close
      </v-icon>
      <div class="login-wrapper">
        <v-form ref="validForm">
          <v-text-field
            v-model="passwordForm.oldPassword"
            :rules="[rules.required]"
            :type="show ? 'text' : 'password'"
            :append-icon="show ? 'mdi-eye' : 'mdi-eye-off'"
            class="mt-7"
            :label="$t('reset.oldPassword')"
            maxlength="50"
            autofocus="autofocus"
            :placeholder="$t('reset.inputOldPwd')"
            @keyup.enter="resetPassword"
            @click:append="show = !show"
            ref="validForm1"
          />
          <v-text-field
            v-model="passwordForm.newPassword"
            :rules="[rules.required]"
            :type="show1 ? 'text' : 'password'"
            :append-icon="show1 ? 'mdi-eye' : 'mdi-eye-off'"
            class="mt-7"
            :label="$t('reset.newPassword')"
            maxlength="50"
            :placeholder="$t('reset.inputNewPwd')"
            @keyup.enter="resetPassword"
            @click:append="show1 = !show1"
            ref="validForm2"
          />
          <v-text-field
            v-model="passwordForm.confirmPassword"
            :rules="[rules.required]"
            :type="show2 ? 'text' : 'password'"
            :append-icon="show2 ? 'mdi-eye' : 'mdi-eye-off'"
            class="mt-7"
            :label="$t('reset.newPassword2')"
            maxlength="50"
            :placeholder="$t('reset.inputNewPwd2')"
            @keyup.enter="resetPassword"
            @click:append="show2 = !show2"
            ref="validForm3"
          />
          <v-btn
            class="mt-7"
            block
            color="red"
            style="color:#fff"
            @click="resetPassword"
          >
            {{ $t("button.edit") }}
          </v-btn>
        </v-form>
      </div>
    </v-card>
  </v-dialog>
</template>

<script>
import md5 from "js-md5";
export default {
  data: function() {
    return {
      passwordForm: {
        oldPassword: "",
        newPassword: "",
        confirmPassword: ""
      },
      show: false,
      show1: false,
      show2: false,
      rules: {
        required: value =>
          !value || value.length >= 6 || this.$t("reset.passwordRule1")
      }
    };
  },
  computed: {
    resetFlag: {
      set(value) {
        this.$store.commit("updateResetFlag", value);
      },
      get() {
        return this.$store.state.resetFlag;
      }
    }
  },
  watch: {
    "$i18n.locale"() {
      if (this.$refs.validForm) {
        this.$refs.validForm.reset();
      }
    }
  },
  methods: {
    resetPassword() {
      if (this.passwordForm.oldPassword.trim().length < 6) {
        this.$toast({ type: "error", message: this.$t("reset.oldPwdRule1") });
        return false;
      }
      if (this.passwordForm.newPassword.trim().length < 6) {
        this.$toast({ type: "error", message: this.$t("reset.newPwdRule1") });
        return false;
      }
      if (
        this.passwordForm.newPassword.trim() !==
        this.passwordForm.confirmPassword.trim()
      ) {
        this.$toast({ type: "error", message: this.$t("reset.newPwdRule2") });
        return false;
      }
      let param = {
        oldPassword: md5(this.passwordForm.oldPassword),
        newPassword: md5(this.passwordForm.newPassword)
      };
      this.axios.put("/api/userAuth/password", param).then(({ data }) => {
        if (data.flag) {
          this.passwordForm.oldPassword = "";
          this.passwordForm.newPassword = "";
          this.passwordForm.confirmPassword = "";
          this.$store.commit("updateResetFlag", false);
          if (this.$route.path === this.rootUri + "/personal") {
            this.$router.go(-1);
          }
          this.$toast({ type: "success", message: data.message });
        }
      });
    }
  }
};
</script>
