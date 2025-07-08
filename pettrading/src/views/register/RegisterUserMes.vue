<!---->
<template>
  <div class='registerusermes'>
    <div>
      <!-- 本名 -->
      <div class="inp">
        <img src="~assets/img/user/user.png" alt class="img-bg" />
        <input
          type="text"
          placeholder="本名"
          v-model="form.realname"
          @blur="checkrealname" 
          :class="{ redborder: check.realname.flag }"
        />
        <p v-show="check.realname.flag">{{check.realname.val}}</p>
      </div>
      <!-- 身分証明書号 -->
      <div class="inp">
        <img src="~assets/img/user/idcard.png" alt class="img-bg" />
        <input
          type="text"
          placeholder="身分証明書号"
          v-model="form.idcard"
          @blur="checkidcard" 
          :class="{ redborder: check.idcard.flag }"
        />
        <p v-show="check.idcard.flag">{{check.idcard.val}}</p>
      </div>
      <div class="but">
        <el-button round type="primary" @click="previous">前へ</el-button>
        <el-button round type="primary" @click="next">次へ</el-button>
      </div>
    </div>
  </div>
</template>

<script>
  import {requestcheckuser} from "network/LRF.js";

  export default {
    name: 'RegisterUserMes',
    props: ['form'],
    data () {
      return {
        check: {
          realname: {
            flag: false,
            val: ''
          },
          idcard: {
            flag: false,
            val: ''
          }
        }
      }
    },
    methods: {
      //检验本名
      checkrealname() {
        if (this.form.realname === "") {
          this.check.realname.val = "*本名空にできません";
          this.check.realname.flag = true;
        } else {
          this.check.realname.flag = false;
        }
      },
      //检验身分証明書号
      checkidcard() {
        if (this.form.idcard === "") {
        this.check.idcard.val = "*身分証明書号空にできません";
        this.check.idcard.flag = true;
        } else if (
            /^[1-9]\d{5}(18|19|20)\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$/.test(
            this.form.idcard
            )
        ) {
            this.check.idcard.flag = false;
        } else {
            this.check.idcard.val = "*正しく18桁の身分証明書番号を入力してください";
            this.check.idcard.flag = true;
        }
      },
      previous() {
        this.$emit('previous');
      },
      next() {
        if(this.check.realname.flag != true && this.check.idcard.flag != true){
          requestcheckuser({
            realname: this.form.realname,
            idcard: this.form.idcard
          }).then(res => {
            if(res.flag === 0){
              this.$message({
                showClose: true,
                message: res.msg,
                type: 'error',
                center: true
              })
              this.$emit('next', 0)
            }else {
              this.$emit('next', 1);
              this.$emit('register');
            }
          }).catch(err => {
            console.log(err);
          })
        }
      }
    },
  }
</script>
<style scoped>

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
  justify-content:space-around;
}
</style>