<!--后台界面-->
<template>
  <div class='backstage'>
    <div class="menu">
      <el-menu
        style="height: 100%"
        :default-active="this.$route.path"
        class="el-menu-vertical-demo"
        @open="handleOpen"
        @close="handleClose"
        background-color="#545c64"
        text-color="#fff"
        :collapse="isCollapse"
        router
        active-text-color="#ffd04b">
        <el-submenu index="4">
          <template slot="title">
            <i class="el-icon-s-custom"></i>
            <span slot="title">個人管理</span>
          </template>
          <el-menu-item index="/backstage/mymanage">自分の情報を更新する</el-menu-item>
        </el-submenu>
        <el-submenu index="1">
          <template slot="title">
            <i class="el-icon-s-custom"></i>
            <span slot="title">ユーザー管理モジュール</span>
          </template>
          <el-menu-item-group>
            <template slot="title">ユーザー情報</template>
            <el-menu-item index="/backstage/usermanage">ユーザー管理</el-menu-item>
          </el-menu-item-group>
          <el-menu-item-group>
            <template slot="title">ユーザー住所</template>
            <el-menu-item index="/backstage/addressmanage">住所管理</el-menu-item>
          </el-menu-item-group>
        </el-submenu>
        <el-submenu index="2">
          <template slot="title">
            <i class="el-icon-shopping-bag-1"></i>
            <span>ペット管理モジュール</span>
          </template>
          <el-menu-item-group>
            <template slot="title">ユーザーペット</template>
            <el-menu-item index="/backstage/petmanage">ペット管理</el-menu-item>
          </el-menu-item-group>
          <el-menu-item-group title="ペットの種類">
            <el-menu-item index="/backstage/petkindmanage">ペット種類管理</el-menu-item>
          </el-menu-item-group>
        </el-submenu>
        <el-submenu index="3">
          <template slot="title">
            <i class="el-icon-s-order"></i>
            <span slot="title">注文管理モジュール</span>
          </template>
          <el-menu-item index="/backstage/petordermanage">注文管理</el-menu-item>
        </el-submenu>
      </el-menu>
    </div>
    <div class="menuviews" :style="changewidth()">
      <div class="topbar">
        <div class="titlebar">
          <div class="meunCollapse" @click="changeCollapse">
            <img :src="menuimg" alt="">
          </div>
          <div class="title">
            {{$route.meta.title}}
          </div>
        </div>
        <div class="logoutbut" @click="tologout">
          <i class="el-icon-switch-button" style="color: red;font-size: 3.5em;width: 100%;margin-top: 0.4em"></i>
          <div class="clicklogout">ログイン解除</div>
        </div>
      </div>
      <div class="mainbar">
        <router-view></router-view>
      </div>
    </div>
  </div>
</template>

<script>
  export default {
    name: 'backstage',
    data () {
      return {
        isCollapse: false,
        menuimg: require('assets/img/menu/close.png'),
        activepath: ''
      }
    },
    methods: {
      handleOpen(key, keyPath) {
        console.log(key, keyPath);
      },
      handleClose(key, keyPath) {
        console.log(key, keyPath);
      },
      changewidth() {
        if(this.isCollapse == false) {
          return "width: calc(100% - 25em)"
        }else {
          return "width: calc(100% - 5.5em)"
        }
      },
      changeCollapse() {
        if(this.isCollapse == false) {
          this.isCollapse = true
          this.menuimg = require('assets/img/menu/open.png')
        }else {
          this.isCollapse = false
          this.menuimg = require('assets/img/menu/close.png')
        }
      },
      tologout() {
        this.$confirm('本当にログインを解除してもよろしいですか？', '退会', {
          confirmButtonText: '確定',
          cancelButtonText: 'キャンセル',
          type: 'warning',
          center: true
        }).then(() => {
          if(this.$route.path.indexOf("/home") == -1 || this.$route.path.indexOf("/presell") == -1 || this.$route.path.indexOf("/buying") == -1){
            this.$router.push('/home');
          }
          this.$store.commit('setuid',null);
          window.sessionStorage.removeItem("uid");
          this.$store.commit('setavatar',null);
          window.sessionStorage.removeItem("avatar");
        }).catch(() => {
        });
      },
    },
  }
</script>
<style scoped>

.backstage {
  display: flex;
}

.el-menu-vertical-demo:not(.el-menu--collapse) {
  width: 25em;
}


.el-menu{
  border-right-width: 0;
}

.menuviews {
  height: 100vh;
}

.topbar {
  display: flex;
  justify-content: space-between;
  background: rgb(255, 255, 255);
  border: 1px solid rgb(232, 232, 232);
  padding-left: 1.5em;
}

.titlebar {
  width: 30em;
  height: 8em;
  display: flex;
  flex-direction: column;
}

.meunCollapse {
  margin-top:1em;
  width: 4em;
  height: 2em;
  cursor: pointer;
}

.meunCollapse img {
  height: 100%;
}

.title {
  font-size: 2em;
  margin-top: 0.5em;
}

.logoutbut {
  width: 5em;
  height: 8em;
  cursor: pointer;
  text-align: center;
  margin-right: 5em;
  display: flex;
  flex-direction: column;
}

.clicklogout:hover {
  color: red;
}

.mainbar {
  margin: 1.5em;
  background: rgb(255, 255, 255);
  padding: 2.5em;
  height: 85vh;
  overflow-y: auto;
}

</style>
