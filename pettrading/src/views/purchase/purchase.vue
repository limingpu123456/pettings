<!--购买页面-->
<template>
  <div class='purchase'>
    <div class="title">
      <h1>
        <slot>注文を提出する</slot>
      </h1>
    </div>
    <div class="mainbar">
      <el-form :rules="rules" ref="ruleForm" label-width="80px" :model="form">
        <el-form-item label="配送住所" prop="address">
          <el-input v-model="form.address" clearable placeholder="住所を入力してください"></el-input>
        </el-form-item>
        <el-form-item label="電話番号" prop="phone">
          <el-input v-model="form.phone" clearable placeholder="携帯電話番号を入力してください"></el-input>
        </el-form-item>
        <el-form-item label="受取人" prop="recipientname">
          <el-input v-model="form.recipientname" clearable placeholder="受取人を入力してください"></el-input>
        </el-form-item>
        <el-form-item>
          <div class="but">
            <el-button type="primary" @click="request('ruleForm')">提出</el-button>
            <el-tooltip effect="dark" content="この住所は新しい住所です。住所リストに追加します" placement="bottom">
              <el-button type="primary" @click="addaddress('ruleForm')">新しい住所に保存する</el-button>
            </el-tooltip>
          </div>
        </el-form-item>
      </el-form>
      <div class="chooseaddress">既存の住所を選択</div>
      <address-item @choose="choose"></address-item>
    </div>
    <!-- <sidebar></sidebar> -->
  </div>
</template>

<script>
  import {requestaddAddress} from "network/requestaddress.js"
  import {requestaddpetorder,requestupdatepetorder} from "network/requestpetorder.js"

  import bus from 'store/bus.js'

  import AddressItem from "views/purchase/AddressItem.vue"
  // import Sidebar from "components/content/sidebar/Sidebar.vue"


  export default {
    name: 'purchase',
    inject: ['reload'],
    props: ['orderform'],
    components: {
      AddressItem,
      // Sidebar
    },
    data () {
      var checkphone = (rule, value, callback) => {
        if(!value) {
          return callback(new Error('携帯番号空にできません'))
        }else if(/^1\d{10}$/.test(value)) {
          callback();
        }else {
          callback(new Error('11桁の携帯番号を入力してください（例：1xx xxxx xxxx）'))
        }
      }
      return {
        form: {
          address: '',
          phone: '',
          recipientname: ''
        },
        rules: {
          address: [
            { required: true, message: '住所空にできません' , trigger: 'blur'}
          ],
          phone: [
            { required: true, validator: checkphone, trigger: 'blur'}
          ],
          recipientname: [
            { required: true, message: '受取人空にできません' , trigger: 'blur'}
          ]
        }
      }
    },
    methods: {
      //提出订单
      request(formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            if(this.orderform) {
              //修正订单
              requestupdatepetorder({
                // pid: this.$route.query.pid,
                // uid: this.$store.state.uid,
                // puid: ,
                poid: this.orderform.poid,
                newaddress: this.form.address,
                newphone: this.form.phone,
                newrecipientname: this.form.recipientname,
                postatu: 3
              }).then(res => {
                this.form.postatu = 3
                this.$emit("successupdate",this.form)
                let purchasemsg = {}
                purchasemsg.type = 2
                purchasemsg.msg = '注文を1件修正したいです。クリックして確認してください'
                purchasemsg.touid = this.orderform.touid
                purchasemsg.pid = this.orderform.pid
                bus.$emit("purchase",purchasemsg)
                this.$router.go(-1)
                this.$notify({
                  title: '変更中',
                  message: res,
                  type: 'warning',
                  offset: 100
                })
              }).catch(err => {
                console.log(err)
              })
            }else {
              //提出订单
              requestaddpetorder({
                pid: this.$route.query.pid,
                uid: this.$store.state.uid,
                puid: this.$route.query.uid,
                address: this.form.address,
                phone: this.form.phone,
                recipientname: this.form.recipientname,
                postatu: 0,
                price: this.$route.query.price
              }).then(res => {
                if(res.flag == 1) {
                  let purchasemsg = {}
                  purchasemsg.type = 1
                  purchasemsg.msg = 'あなたのペットを購入しました。クリックして確認してください'
                  purchasemsg.touid = this.$route.query.uid
                  purchasemsg.pid = this.$route.query.pid
                  bus.$emit("purchase",purchasemsg)
                  this.$router.go(-1)
                  this.$notify({
                    title: '成功',
                    message: res.msg,
                    type: 'success',
                    offset: 100
                  });
                }else {
                  this.$notify({
                    title: '失败',
                    message: res.msg,
                    type: 'error'
                  });
                }
              }).catch(err => {
                console.log(err)
              })
            }
          } else {
            return false;
          }
        });
      },
      //增加新住所
      addaddress(formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            requestaddAddress({
              uid: this.$store.state.uid,
              phone: this.form.phone,
              username: this.form.recipientname,
              address: this.form.address
            }).then(res => {
              this.$message({
                message: res.msg,
                type: 'success',
                center: true,
                showClose: true,
              })
              this.reload()
            }).catch(err => {
              console.log(err)
            })
          } else {
            console.log('error submit!!');
            return false;
          }
        });
      },
      //选择住所
      choose(item) {
        this.$refs['ruleForm'].clearValidate()
        this.form.recipientname = item.username
        this.form.phone = item.phone
        this.form.address = item.address
        if(this.orderform){
          this.$emit("totop")
        }else {
          window.scrollTo({
                    top: 0,
                    behavior: "smooth"
                })
        }
      }
    },
    created() {
      if(this.orderform) {
        this.form.address = this.orderform.address
        this.form.phone = this.orderform.phone
        this.form.recipientname = this.orderform.recipientname
      }
    },
  }
</script>
<style scoped>

.title {
  text-align: center;
  margin-bottom: 1.5em;
  text-shadow: 1px 1px 1px rgba(0, 0, 0, 0.3);
  font-weight: 400;
}

.mainbar {
  width: 40em;
  margin: auto;
}

.but {
  display: flex;
  justify-content: space-between;
}

.chooseaddress {
  margin-top: 2em;
  color: rgb(140, 196, 253);
  font-size: 1.5em;
}
</style>
