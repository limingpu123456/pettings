<!---->
<template>
  <div class='usersafety'>
    <el-collapse accordion>
      <el-collapse-item>
        <template slot="title">
          <div class="collapsetitle">修正パスワード</div>
        </template>
        <changepwd></changepwd>
      </el-collapse-item>
      <el-collapse-item>
        <template slot="title">
          <div class="collapsetitle">
            実名認証
            <span class="done"> (認証済み)</span>
          </div>
        </template>
        <verifyrealname></verifyrealname>
      </el-collapse-item>
      <el-collapse-item>
        <template slot="title">
          <div class="collapsetitle">
            携帯番号を連携する
            <span class="done">(連携済み)</span>
          </div>
        </template>
        <change-phone></change-phone>
      </el-collapse-item>
      <!-- <el-collapse-item>
        <template slot="title">
          <div class="collapsetitle">绑定邮箱 (未绑定)</div>
        </template>
      </el-collapse-item> -->
    </el-collapse>
  </div>
</template>

<script>
  import Changepwd from "views/user/childcomps/usersafty/Changepwd.vue"
  import Verifyrealname from "views/user/childcomps/usersafty/Verifyrealname.vue"
  import ChangePhone from "views/user/childcomps/usersafty/ChangePhone.vue"

  export default {
    name: 'UserSafety',
    data () {
      return {}
    },
    components: {
      Changepwd,
      Verifyrealname,
      ChangePhone
    }
  }
</script>
<style scoped>
.usersafety {
  padding-left: 1em;
}

.collapsetitle {
  font-size: 1.1em;
  padding-left: 1em;
}

.done {
  color: rgb(33, 209, 9);
}
</style>
