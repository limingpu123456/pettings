<!--一个订单-->
<template>
  <div class='ordercard' :style="border(statu)">
    <div class="left">
    <div class="pet" @click="todetail">
      <div class="img">
        <img :src="'/api/' + item.pet.photo[0].url" alt="">
      </div>
      <div class="petmes">
        <div class="petname">{{item.pet.petname}}</div>
        <div class="price">{{item.price}}</div>
      </div>
    </div>
    <div class="orderstatu">
      <div v-if="statu === 0" class="doing">進行中</div>
      <div v-if="statu === 1" class="success">完了済み</div>
      <div v-if="statu === 2" class="cancel">キャンセル</div>
      <div v-if="statu === 3" class="changing">変更中</div>
      <div v-if="statu === 4" class="cancelimg">キャンセル中</div>
    </div>
    </div>
    <div class="message">
      <div class="mbar">
        <div class="title">宛先：</div>
        <div class="val">{{item.recipientname}}</div>
      </div>
      <div class="mbar">
        <div class="title">連絡電話番号：</div>
        <div class="val">{{item.phone}}</div>
      </div>
      <div class="mbar">
        <div class="title">住所：</div>
        <div class="address">{{item.address}}</div>
      </div>
      <div class="time">
        <div>{{item.date}}</div>
      </div>
      <div class="but">
        <!-- 修正 -->
        <el-button v-if="statu === 0" type="primary" size="small" @click="changepetorder">修正</el-button>
        <!-- 完成 -->
        <el-popover v-if="statu === 0"
          placement="top"
          width="160"
          v-model="showcomplete">
          <p>商品を受け取りました。注文が完了しました</p>
          <div style="text-align: right; margin: 0">
            <el-button size="mini" type="text" @click="showcomplete = false">キャンセル</el-button>
            <el-button type="primary" size="mini" @click="complete">確定</el-button>
          </div>
          <el-button v-if="statu === 0" type="primary" size="small" slot="reference">完成</el-button>
        </el-popover>
        <!-- キャンセル订单 -->
        <el-popover v-if="statu === 0"
          placement="top"
          width="160"
          v-model="cancelorder">
          <p>本当に注文をキャンセルしますか？</p>
          <div style="text-align: right; margin: 0">
            <el-button size="mini" type="text" @click="cancelorder = false">キャンセル</el-button>
            <el-button type="primary" size="mini" @click="cancelpetorder">確定</el-button>
          </div>
          <el-button v-if="statu === 0" type="danger" size="small" slot="reference">注文をキャンセルする</el-button>
        </el-popover>
        <!-- 削除 -->
        <el-popover v-if="statu === 1 || statu === 2"
          placement="top"
          width="160"
          v-model="visible">
          <p>削除すると復元できません。削除してもよろしいですか</p>
          <div style="text-align: right; margin: 0">
            <el-button size="mini" type="text" @click="visible = false">キャンセル</el-button>
            <el-button type="primary" size="mini" @click="deletepetorder">確定</el-button>
          </div>
          <el-button v-if="statu === 1 || statu === 2" type="danger" size="small" slot="reference">削除</el-button>
        </el-popover>
      </div>
    </div>
    <!-- 注文を修正する -->
    <div v-if="change === true">
      <div class="changepetorder" @click="closechange">
      </div>
      <div class="purchasebar" id="purchasescroll">
        <purchase :orderform="form" @totop="totop" @successupdate="successupdate">注文を修正する</purchase>
      </div>
    </div>
  </div>
</template>

<script>
  import purchase from "views/purchase/purchase.vue"

  import bus from "store/bus.js"

  import {requestupdatePet} from "network/requestpet.js"
  import {requestupdatepetorder,requestdeletepetorder} from "network/requestpetorder.js"


  export default {
    name: 'Ordercard',
    components: {
      purchase
    },
    props: {
      item: Object,
      statu: Number
    },
    data () {
      return {
        showcomplete: false,//完成按钮的二次確認
        cancelorder: false,//キャンセル订单的二次確認
        visible: false,
        change: false,
        form: {
          username: ''
        }
      }
    },
    methods: {
      border(statu) {
        if(statu === 0) {
          return "border: 0.5px solid rgb(255, 194, 8);"
        }else if(statu === 1) {
          return "border: 0.5px solid rgb(33, 213, 168);"
        }else if(statu === 2) {
          return "border: 0.5px solid rgb(204, 203, 203);"
        }
        else if(statu === 3) {
          return "border: 0.5px solid rgb(102, 177, 255);"
        }else if(statu === 4) {
          return "border: 0.5px solid red;"
        }
      },
      todetail() {
        this.$router.push({
          path: '/detail',
          query: {
            pid: this.item.pet.pid
          }
        })
        // let detail = this.$router.resolve({
        //   path: '/detail',
        //   query: {
        //     pid: this.item.pet.pid
        //   }
        // })
        // window.open(detail.href, '_blank')
      },
      totop() {
        document.getElementById('purchasescroll').scrollTop = 0;
      },
      changepetorder(form) {
        this.change = true
      },
      closechange() {
        this.change = false
      },
      successupdate(form) {
        // this.item.postatu = form.postatu
        this.$emit("update:statu",form.postatu)
        this.item.recipientname = form.recipientname
        this.item.phone = form.phone
        this.item.address = form.address
        this.form.recipientname = form.recipientname
        this.form.phone = form.phone
        this.form.address = form.address
        this.change = false
      },
      //完成按钮
      complete() {
        this.showcomplete = false
        requestupdatepetorder({
          poid: this.item.poid,
          postatu: 1,
          pid: this.item.pid,
          pk: 2
        }).then(res => {
          this.$notify({
            title: '注文完了',
            message: res,
            type: 'success'
          })
          this.item.postatu = 1
          let message = {}
          message.type = 4
          message.msg = '注文が成功しました。クリックして確認してください'
          message.touid = this.form.touid
          message.pid = this.form.pid
          bus.$emit("purchase",message)
        }).catch(err => {
          console.log(err)
        })
      },
      //削除按钮
      deletepetorder() {
        this.visible = false
        requestdeletepetorder({
          poid: this.item.poid
        }).then(res => {
          this.$message({
            message: res,
            type: "success",
            center: true,
            showClose: true
          })
          this.$emit("deleteorder")
        }).catch(err => {
          console.log(err)
        })
      },
      //キャンセル订单
      cancelpetorder() {
        this.cancelorder = false
        requestupdatepetorder({
          poid: this.item.poid,
          pid: this.item.pid,
          postatu: 4
        }).then(res => {
          this.item.postatu = 4
          this.$notify({
            title: "キャンセル中",
            message: res,
            type: 'warning',
            offset: 100
          })
          let message = {}
          message.type = 3
          message.msg = 'この注文をキャンセルしたいです。クリックして確認してください'
          message.touid = this.form.touid
          message.pid = this.form.pid
          bus.$emit("purchase",message)
        }).catch(err => {
          console.log(err)
        })
      }
    },
    created() {
      this.statu = this.item.postatu
      this.form.touid = this.item.puid
      this.form.poid = this.item.poid
      this.form.pid = this.item.pid
      this.form.address = this.item.address
      this.form.phone = this.item.phone
      this.form.recipientname = this.item.recipientname
    },
  }
</script>
<style scoped>

.ordercard {
  display: flex;
  /* align-items: center; */
  width: 25em;
  background: rgb(255, 255, 255);
  margin-top: 2em;
  margin-right: 1.5em;
  padding: 1em;
  font-size: 1.5em;
  border-radius: 0.5em;
  transition: transform 0.5s, box-shadow 0.5s;
  -moz-transition: transform 0.5s, box-shadow 0.5s;
  -webkit-transition: transform 0.5s, box-shadow 0.5s;
  -o-transition: transform 0.5s, box-shadow 0.5s;
}

.left {
  width: 40%;
}

.ordercard .pet {
  display: flex;
  flex-direction: column;
  height: 12.5em;
  border-radius: 0.5em;
  overflow: hidden;
  border: 0.6px solid rgb(204, 203, 203);
  cursor: pointer;
}

.ordercard .pet:hover {
  /* transform: translateY(-0.1em); */
  box-shadow: 0.5px 4px 4px rgb(206, 205, 205), -5px -0.5px 15px rgb(206, 205, 205);
}

.img {
  width: 100%;
  height: 8em;
  overflow: hidden;
}

.img img{
  width: 100%;
}

.petname {
  height: 1.5em;
  line-height: 1.5em;
  font-size: 1.1em;
  font-weight: 550;
  text-align: center;
  overflow: hidden;
}

.price {
  height: 1.5em;
  line-height: 1.5em;
  font-size: 1.1em;
  text-align: center;
  color: rgb(255, 144, 3);
}

.orderstatu {
  margin-top: 1em;
  text-align: center;
}

.success {
  color: rgb(33, 213, 168);
}

.doing {
  color: rgb(255, 194, 8);
}

.changing {
  color: rgb(102, 177, 255);
}

.cancelimg {
  color: red;
}

.cancel {
  color: rgb(204, 203, 203);
}

.message {
  width: 60%;
}

.mbar {
  width: 100%;
  display: flex;
}

.message .title {
  width: 50%;
  height: 2em;
  text-align: center;
  overflow: hidden;
}

.message .val {
  width: 70%;
  height: 2em;
  color: rgb(120, 120, 120);
  overflow: hidden;
}
.message .address {
  width: 70%;
  height: 7em;
  color: rgb(120, 120, 120);
  overflow: hidden;
}

.time {
  display: flex;
  justify-content: flex-end;
  color: rgb(183, 183, 183);
  font-size: 0.5em;
}

.but {
  display: flex;
  justify-content: space-around;
  /* flex-wrap: wrap; */
  margin-top: 1em;
}

.changepetorder {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background: rgba(0, 0, 0,0.3);
}

.purchasebar {
  /* margin: 10em auto; */
  position: fixed;
  top: 0;
  left: 0;
  margin-top: 7em;
  margin-left: 50%;
  transform: translate(-22em,0);
  padding-top: 1em;
  width: 44em;
  /* min-width: 60em; */
  height: 52em;
  overflow-y: auto;
  background: #f2f2f2;
  border-radius: 5px;
}
</style>
