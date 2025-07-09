<!---->
<template>
  <div class="login">
    <div class="loginhead">ログイン</div>
    <div>
      <!-- ユーザー名 -->
      <div class="inp">
        <img src="~assets/img/user/user.png" alt class="img-bg" />
        <input
          type="text"
          @blur="checkUsername"
          :class="{ redborder: check.username.flag }"
          autofocus="autofocus"
          placeholder="ユーザー名/携帯番号/メール"
          required="required"
          v-model="form.username"
        />
        <p v-show="check.username.flag">{{ check.username.val }}</p>
      </div>
      <!-- パスワード -->
      <div class="inp">
        <img src="~assets/img/password/password.png" alt class="img-bg" />
        <input
          @blur="checkPassword"
          :class="{ redborder: check.password.flag }"
          :type="types.type"
          placeholder="パスワード"
          required="required"
          v-model="form.password"
          @keyup.enter="request"
        />
        <img
          v-if="form.password != ''"
          @click="changetype"
          :src="types.img"
          alt
          class="img-bg-right"
        />
        <p v-show="check.password.flag">{{ check.password.val }}</p>
      </div>
      <div class="but">
        <el-button type="primary" round @click="request">ログイン</el-button>
        <el-tooltip
          effect="light"
          content="アカウントがありませんか？新規ログインはこちら"
          placement="bottom-start"
        >
          <el-button round type="primary" @click="toregister">サインアップ</el-button>
        </el-tooltip>
      </div>
    </div>
  </div>
</template>

<script>
import { requestLogin } from "network/LRF.js";

export default {
  name: "Loginitem",
  data() {
    return {
      //验证信息
      check: {
        username: {
          flag: false,
          val: "",
        },
        password: {
          flag: false,
          val: "",
        },
      },
      // パスワード输入框タイプ，改变眼睛开闭
      types: {
        type: "password",
        img: require("assets/img/password/close.png"),
      },
      // 表单数据
      form: {
        username: "",
        password: "",
      },
    };
  },
  methods: {
    changetype() {
      if (this.types.type === "text") {
        this.types.img = require("assets/img/password/close.png");
        this.types.type = "password";
      } else {
        this.types.img = require("assets/img/password/open.png");
        this.types.type = "text";
      }
    },
    //验证ユーザー名
    checkUsername() {
      if (this.form.username === "") {
        this.check.username.flag = true;
        this.check.username.val = "*空にできません";
      } else {
        this.check.username.flag = false;
      }
    },
    //验证パスワード
    checkPassword() {
      if (this.form.password === "") {
        this.check.password.flag = true;
        this.check.password.val = "*パスワードは必須入力です";
      } else {
        this.check.password.flag = false;
      }
    },
    //ログイン
    request() {
      this.checkUsername();
      this.checkPassword();
      if (
        this.check.username.flag === false &&
        this.check.password.flag === false
      ) {``
        requestLogin({
          username: this.form.username,
          password: this.form.password,
        })
          .then((res) => {
            console.log(res);
            //权限控制
            if (res.flag === 1) {
              this.$store.commit("setuid", res.uid);
              this.$store.commit("setavatar", "/api/" + res.avatar);
              if (res.type === 1) {
                if (this.$route.path == "/login") {
                  this.$router.push("/home");
                }
                this.$message({
                  message: res.msg,
                  type: "success",
                  center: true,
                  showClose: true,
                });
                this.$store.state.show = false;
                this.$store.state.logshow = false;
              } else if (res.type === 0) {
                this.$router.push("/backstage");
                this.$store.state.show = false;
                this.$store.state.logshow = false;
              }
            } else {
              this.$message({
                message: res.msg,
                type: "error",
                center: true,
                showClose: true,
              });
            }
          })
          .catch((err) => {
            console.log(err);
          });
      }
    },
    toregister() {
      if (this.$store.state.show === true) {
        this.$store.state.logshow = false;
        this.$store.state.regshow = true;
      } else {
        this.$router.push("/register");
      }
    },
  },
};
</script>
<style scoped>
.login {
  width: 25em;
  text-align: center;
  margin: auto;
  padding-bottom: 30em;
}

.loginhead {
  text-shadow: 1px 1px 1px rgba(0, 0, 0, 0.3);
  font-weight: 400;
  font-size: 2.5em;
  padding-bottom: 0.5em;
  color: rgb(23, 155, 254);
  /* letter-spacing: 50px; */
}

.inp {
  position: relative;
  width: 270px;
  height: 36px;
  margin: 20px auto;
  /* border: 1px solid red; */
}

.img-bg {
  position: absolute;
  top: 9px;
  left: 13px;
}

.img-bg-right {
  position: absolute;
  top: 9px;
  right: 15px;
  cursor: pointer;
}

input {
  height: 36px;
  width: 270px;
  border-radius: 18px;
  border: 0.5px solid rgb(221, 221, 226);
}

.inp input {
  padding-left: 35px;
}

.inp input:focus {
  border: 0.5px solid rgb(64, 158, 255);
}

.inp p {
  text-align: left;
  padding-left: 2em;
  font-size: 12px;
  color: rgba(255, 13, 13, 0.733);
}

.redborder {
  border: 0.5px solid rgb(245, 108, 108);
}

.but {
  display: flex;
  justify-content: space-around;
}
</style>
