<template>
  <v-app id="app">
    <TopNavBar></TopNavBar>
    <SideNavBar></SideNavBar>
    <v-main>
      <router-view :key="this.$route.fullPath" />
    </v-main>
    <Footer></Footer>
    <BackTop></BackTop>
    <searchModel></searchModel>
    <LoginModel></LoginModel>
    <RegisterModel></RegisterModel>
    <ResetModel></ResetModel>
    <ForgetModel></ForgetModel>
    <EmailModel></EmailModel>
    <Player></Player>
    <ChatRoom></ChatRoom>
  </v-app>
</template>

<script>
import TopNavBar from "./layout/TopNavBar";
import SideNavBar from "./layout/SideNavBar";
import Footer from "./layout/Footer";
import BackTop from "./components/BackTop";
import searchModel from "./components/model/SearchModel";
import LoginModel from "./components/model/LoginModel";
import ResetModel from "./components/model/ResetModel";
import RegisterModel from "./components/model/RegisterModel";
import ForgetModel from "./components/model/ForgetModel";
import EmailModel from "./components/model/EmailModel";
import Player from "./components/player/Player";
import ChatRoom from "./components/ChatRoom";
export default {
  components: {
    TopNavBar,
    Player,
    SideNavBar,
    Footer,
    BackTop,
    searchModel,
    ResetModel,
    LoginModel,
    RegisterModel,
    ForgetModel,
    EmailModel,
    ChatRoom
  }
};
</script>
