<!---->
<template>
  <div class='wantsell'>
    <breadcrumb>
      <span slot="second" @click="topresell">購入エリア</span> > 販売を公開する
    </breadcrumb>
    <div class="title">
      <h1>販売情報を公開する</h1>
    </div>
    <div class="sell">
      <el-form label-width="80px" ref="ruleForm" :model="form" :rules="rules">
        <el-form-item label="名前" prop="name">
          <el-input v-model="form.name" placeholder="名前を入力してください"></el-input>
        </el-form-item>
        <el-form-item label="種類" prop="bkid">
          <el-select style="width: 100%" v-model="form.bkid" clearable filterable placeholder="種類を入力してください"  @change="bkindchange">
            <el-option
              v-for="item in bkind"
              :key="item.bkid"
              :label="item.bkindname"
              :value="item.bkid">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="品種" prop="skid">
          <el-select style="width: 100%" v-model="form.skid" clearable filterable placeholder="品種を入力してください" @change="skindchange">
            <el-option
              v-for="item in fskind"
              :key="item.skid"
              :label="item.skindname"
              :value="item.skid">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="性別" prop="sex">
          <el-radio v-model="form.sex" label="男">男</el-radio>
          <el-radio v-model="form.sex" label="女">女</el-radio>
        </el-form-item>
        <el-form-item label="年齢" prop="age">
          <el-input v-model.number="form.age" placeholder="年齢(月)を入力してください" clearable></el-input>
        </el-form-item>
        <el-form-item label="価格" prop="price">
          <el-input v-model="form.price" placeholder="販売的価格（円）を入力してください" clearable></el-input>
        </el-form-item>
        <!-- <el-form-item label="宠物数量" prop="count">
          <el-input v-model.number="form.count" placeholder="请输入販売的数量（单位个）" clearable></el-input>
        </el-form-item> -->
        <!-- <el-form-item label="連絡電話番号" prop="phone">
          <el-input placeholder="请输入携帯番号" clearable v-model="form.phone"></el-input>
        </el-form-item> -->
        <el-form-item label="販売说明">
          <el-input type="textarea" placeholder="内容を入力してください" clearable rows=4 resize="none" show-word-limit v-model="form.desc"></el-input>
        </el-form-item>
        <el-form-item label="写真" prop="photo">
          <el-upload
            class="upload-demo"
            ref="upload"
            action
            multiple
            accept="image/*"
            list-type="picture-card"
            :on-preview="handlePreview"
            :on-remove="handleRemove"
            :on-change="handleChange"
            :on-exceed="handleExceed"
            :file-list="form.photo"
            :auto-upload="false">
            <i class="el-icon-plus"></i>
            <div slot="tip" class="el-upload__tip">画像は最大5枚までアップロード可能です。1枚あたりのサイズは10MB以下にしてください</div>
          </el-upload>
          <el-dialog :visible.sync="dialogVisible">
            <img width="100%" :src="dialogImageUrl" alt="">
          </el-dialog>
        </el-form-item>
        <el-form-item>
          <el-button v-if="!$route.query.form" type="primary" @click="request('ruleForm')">今すぐ作成</el-button>
          <el-button v-if="$route.query.form" type="primary" @click="updatepet('ruleForm')">変更を確認する</el-button>
        </el-form-item>
      </el-form>
    </div>
    <!-- <sidebar></sidebar> -->
  </div>
</template>

<script>
  import Breadcrumb from "components/common/breadcrumb/Breadcrumb.vue"
  import Sidebar from "components/content/sidebar/Sidebar.vue"

  import bus from "store/bus.js"

  import {requestqueryBKindandSKind} from "network/requestbkind.js"
  import {requestqueryallskind} from "network/requestskind.js"
  import {requestaddpet,requestupdatePet} from "network/requestpet.js"

  export default {
    name: 'Wantsell',
    components: {
      Breadcrumb,
      Sidebar
    },
    data () {
      //验证年齢
      var checkage = (rule, value, callback) => {
        if(!value) {
          return callback(new Error('年齢空にできません（单位月）'));
        }
        if(!Number.isInteger(value)) {
          callback(new Error('请输入数字值（单位月）'));
        }else {
          callback();
        }
      }
      //验证価格
      var checkprice = (rule, value, callback) => {
        if(!value) {
          return callback(new Error('価格空にできません'));
        }
        if(/^\d+(\.\d+)?$/.test(this.form.price)) {
          callback();
        }else {
          callback(new Error('请输入数字值'));
        }
      }
      //验证数量
      // var checkcount = (rule, value, callback) => {
      //   if(!value) {
      //     return callback(new Error('数量空にできません'));
      //   }
      //   if(!Number.isInteger(value)) {
      //     callback(new Error('请输入数字值'));
      //   }else {
      //     callback();
      //   }
      // }
      //验证图片
      var checkphoto = (rule, value, callback) => {
        if(value.length === 0) {
          return callback(new Error('请选择上传的图片'));
        }else if(value.length > 5) {
          return callback(new Error('最多上传5张'));
        }else {
          for(let i=0;i<value.length;i++) {
            if(value[i].size / 1024 / 1024 > 10) {
              return callback(new Error('每张图片不可超过10m'));
            }
          }
          callback();
        }
      }
      //验证携帯番号
      // var checkphone = (rule, value, callback) => {
      //   if(!value) {
      //     return callback(new Error('連絡電話番号空にできません'));
      //   }
      //   if(/^1\d{10}$/.test(this.form.phone)) {
      //     callback();
      //   }else {
      //     callback(new Error('11桁の携帯番号を入力してください（例：1xx xxxx xxxx'));
      //   }
      // }
      return {
        pk: 0,
        dialogImageUrl: '',
        dialogVisible: false,
        bkind: [],
        skind: [],
        fskind: [],
        //判断表单是否完整
        falg: false,
        form: {
          pid: '',
          name: '',
          bkid: '',
          skid: '',
          sex: '男',
          age: '',
          price: '',
          count: '',
          // phone: '',
          desc: '',
          photo: [],
          ppid: []
        },
        rules: {
          name: [
            { required: true, message: '名前を入力してください', trigger: 'blur'}
          ],
          bkid: [
            { required: true, message: '種類を選んでください', trigger: 'change' }
          ],
          skid: [
            { required: true, message: '品種を選んでください', trigger: 'change' }
          ],
          sex: [
            { required: true, trigger: 'change' }
          ],
          age: [
            { required: true,validator: checkage, trigger: 'blur' }
          ],
          price: [
            { required: true, validator: checkprice, trigger: 'blur' }
          ],
          // count: [
          //   {required: true, validator: checkcount, trigger: 'blur'}
          // ],
          // phone: [
          //   { required: true, validator: checkphone, trigger: 'blur' }
          // ],
          photo: [
            {required: true, validator: checkphoto, trigger: 'change' }
          ]
        }
      }
    },
    methods: {
      //选中タイプ时，筛选出对应品種
      bkindchange() {
        if(this.form.bkid === ""){
          this.fskind = this.skind.filter(n => {
            return n.skindname != "その他"
          })
        }else {
          this.fskind = this.skind.filter(n => {
            return n.bkid === this.form.bkid
          })
          let bsk = this.fskind.filter(n => {
            return n.skid === this.form.skid
          })
          if(bsk.length === 0) {
            this.form.skid = ""
          }
        }
      },
      //选中品種时，选中对应タイプ
      skindchange() {
        if(this.form.skid != ""){
          let sbk = this.fskind.find(n => {
            return n.skid === this.form.skid
          })
          this.form.bkid = sbk.bkid
        }
      },
      handleChange(file, fileList) {
        this.form.photo = fileList;
      },
      handleRemove(file, fileList) {
        if(file.ppid) {
          if(this.form.ppid) {
            this.form.ppid.push(file.ppid)
          } else {
            this.form.ppid = []
            this.form.ppid.push(file.ppid)
          }
        }
        this.form.photo = fileList;
      },
      handlePreview(file) {
        this.dialogImageUrl = file.url;
        this.dialogVisible = true;
      },
      //图片超过限制执行
      handleExceed(files, fileList) {
        // this.$message.warning(`最多选择5个文件`);
      },
      request(formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            if(this.form.desc == "") {
              this.form.desc = "該当なし"
            }
            let formData = new FormData();
            formData.append('petname', this.form.name);
            formData.append('pk', this.pk);
            formData.append('bkid', this.form.bkid);
            formData.append('skid', this.form.skid);
            formData.append('sex', this.form.sex);
            formData.append('uid', this.$store.state.uid);
            formData.append('age', this.form.age);
            formData.append('price', this.form.price);
            // formData.append('count', this.form.count);
            formData.append('description', this.form.desc);
            for(var i=0;i<this.form.photo.length;i++){
              formData.append('files', this.form.photo[i].raw);
            }
            requestaddpet(
              formData
            ).then(res => {
              this.$notify({
                title: '成功',
                message: res,
                type: 'success',
                offset: 100
              });
              // this.$refs.upload.submit();
              this.$router.go(-1)
            }).catch(err => {
              console.log(err);
            })
          } else {
            this.$notify({
              title: '失败',
              message: '正しいペット情報を入力してください',
              type: 'error',
              offset: 100
            });
            return false;
          }
        });
      },
      updatepet(formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            if(this.form.desc == "") {
              this.form.desc = "該当なし"
            }
            let formData = new FormData();
            formData.append('pid', this.form.pid);
            formData.append('petname', this.form.name);
            formData.append('pk', this.pk);
            formData.append('bkid', this.form.bkid);
            formData.append('skid', this.form.skid);
            formData.append('sex', this.form.sex);
            formData.append('uid', this.$store.state.uid);
            formData.append('age', this.form.age);
            formData.append('price', this.form.price);
            // formData.append('count', this.form.count);
            formData.append('description', this.form.desc);
            for(var i=0;i<this.form.photo.length;i++){
              if(this.form.photo[i].raw){
                formData.append('files', this.form.photo[i].raw);
              }
            }
            if(this.form.ppid){
              this.form.ppid.forEach((value) => {
                formData.append('ppid', value);
              })
            }
            requestupdatePet(
              formData
            ).then(res => {
              this.$notify({
                title: '成功',
                message: res,
                type: 'success',
                offset: 100
              });

            }).catch(err => {
              console.log(err);
            })
          } else {
            this.$notify({
              title: '失败',
              message: '正しいペット情報を入力してください',
              type: 'error',
              offset: 100
            });
            return false;
          }
        });
      },
      topresell() {
        this.$router.push('/presell');
      }
    },
    created() {
      //検索所有種類及对应品種
      requestqueryBKindandSKind().then(res => {
          this.bkind = res;
        }).catch(err => {
          console.log(err)
        });
      //検索所有品種
      requestqueryallskind().then(res => {
          this.skind = res;
          this.fskind = this.skind.filter(n => {
            return n.skindname != "その他"
          })
        }).catch(err => {
          console.log(err)
        })
      //修正信息时设初始值
      if(this.$route.query.form){
        let oldform = JSON.parse(this.$route.query.form)
        this.form = oldform
        let photo = this.form.photo
        photo.forEach((value,index,array) => {
          photo[index].url = '/api/' + photo[index].url
        })
      }
    },
  }
</script>
<style scoped>

.wantsell {
  margin-top: 50px;
}

.title {
  text-align: center;
  margin-bottom: 1.5em;
  text-shadow: 1px 1px 1px rgba(0, 0, 0, 0.3);
  font-weight: 400;
}

.sell {
  width: 45.7em;
  margin: auto;
}
</style>
