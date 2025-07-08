<!--住所管理-->
<template>
  <div class="addressmanage">
    <div class="selectbar">
      <el-form :inline="true" :model="form">
        <el-form-item label="住所ID">
          <el-input v-model="form.aid" placeholder="住所ID"></el-input>
        </el-form-item>
        <el-form-item label="ユーザーID">
          <el-input v-model="form.uid" placeholder="ユーザーID"></el-input>
        </el-form-item>
        <el-form-item label="受取人">
          <el-input v-model="form.username" placeholder="受取人"></el-input>
        </el-form-item>
        <el-form-item label="住所">
          <el-input v-model="form.address" placeholder="住所"></el-input>
        </el-form-item>
        <el-form-item label="連絡電話番号">
          <el-input v-model="form.phone" placeholder="連絡電話番号"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button
            @click="queryaddress"
            icon="el-icon-search"
            type="primary"
            circle
          ></el-button>
        </el-form-item>
      </el-form>
    </div>
    <div>
      <el-table :data="address" style="width: 100%">
        <el-table-column prop="aid" sortable label="住所ID" width="180">
        </el-table-column>
        <el-table-column label="所属ユーザーID" prop="uid" width="180">
        </el-table-column>
        <el-table-column prop="username" label="受取人" width="200">
        </el-table-column>
        <el-table-column prop="address" label="住所"> </el-table-column>
        <el-table-column prop="phone" width="200" label="連絡電話番号">
        </el-table-column>
        <el-table-column label="操作" width="180">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleEdit(scope.$index, scope.row)"
              >編集</el-button
            >
            <el-popconfirm
              confirmButtonText="確定"
              cancelButtonText="キャンセル"
              placement="top"
              icon="el-icon-info"
              iconColor="red"
              title="このユーザーを削除してもよろしいですか"
              @confirm="handleDelete(scope.$index, scope.row)"
            >
              <el-button
                style="margin-left: 10px"
                size="mini"
                type="danger"
                slot="reference"
                >削除</el-button
              >
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="pagination">
      <el-pagination
        :hide-on-single-page="false"
        @current-change="handleCurrentChange"
        :page-size="pagesize"
        :pager-count="pagercount"
        :current-page.sync="currentpage"
        layout="total, prev, pager, next, jumper"
        :total="total"
      >
      </el-pagination>
    </div>
    <el-drawer :visible.sync="drawer" direction="rtl" size="50%">
      <div class="updatebar">
        <el-form :model="updateaddress" label-width="80px">
          <el-form-item label="受取人">
            <el-input
              v-model="updateaddress.username"
              placeholder="受取人"
              clearable
            ></el-input>
          </el-form-item>
          <el-form-item label="住所">
            <el-input
              v-model="updateaddress.address"
              placeholder="住所"
              clearable
            ></el-input>
          </el-form-item>
          <el-form-item label="連絡電話番号">
            <el-input
              v-model="updateaddress.phone"
              placeholder="連絡電話番号"
              clearable
            ></el-input>
          </el-form-item>
          <el-form-item>
            <div class="but">
              <el-button type="primary" @click="update">提出</el-button>
              <el-button type="primary" @click="drawer = false">キャンセル</el-button>
            </div>
          </el-form-item>
        </el-form>
      </div>
    </el-drawer>
  </div>
</template>

<script>
import {
  requestqueryaddress,
  requestdeleteAddress,
  requestupdateAddress,
} from "network/requestaddress.js";

export default {
  name: "addressmanage",
  data() {
    return {
      pagesize: 10,
      pagercount: 5,
      total: 10,
      currentpage: 1,
      drawer: false,
      form: {},
      address: [],
      editaddress: {},
      updateaddress: {
        // aid: null,
        // phone: "",
        // address: '',
        // username: ''
      },
    };
  },
  created() {
    requestqueryaddress({
      page: this.currentpage,
      count: this.pagesize,
    })
      .then((res) => {
        this.address = res.address;
        this.total = res.total;
      })
      .catch((err) => {
        console.log(err);
      });
  },
  methods: {
    handleCurrentChange() {
      requestqueryaddress({
        uid: this.form.uid,
        aid: this.form.aid,
        username: this.form.username,
        address: this.form.address,
        phone: this.form.phone,
        page: this.currentpage,
        count: this.pagesize,
      })
        .then((res) => {
          this.address = res.address;
          this.total = res.total;
        })
        .catch((err) => {
          console.log(err);
        });
    },
    queryaddress() {
      this.currentpage = 1;
      requestqueryaddress({
        uid: this.form.uid,
        aid: this.form.aid,
        username: this.form.username,
        address: this.form.address,
        phone: this.form.phone,
        page: this.currentpage,
        count: this.pagesize,
      })
        .then((res) => {
          this.address = res.address;
          this.total = res.total;
        })
        .catch((err) => {
          console.log(err);
        });
    },
    handleEdit(index, row) {
      this.editaddress = row;
      let editRow = JSON.stringify(row);
      this.updateaddress = JSON.parse(editRow);
      this.drawer = true;
    },
    handleDelete(index, row) {
      requestdeleteAddress({
        aid: row.aid,
      })
        .then((res) => {
          this.$notify({
            title: "削除成功",
            message: res,
            offset: 100,
            type: "success",
          });
          requestqueryaddress({
            uid: this.form.uid,
            aid: this.form.aid,
            username: this.form.username,
            address: this.form.address,
            phone: this.form.phone,
            page: this.currentpage,
            count: this.pagesize,
          })
            .then((res) => {
              this.address = res.address;
              this.total = res.total;
            })
            .catch((err) => {
              console.log(err);
            });
        })
        .catch((err) => {
          console.log(err);
        });
    },
    update() {
      requestupdateAddress({
        aid: this.updateaddress.aid,
        username: this.updateaddress.username,
        address: this.updateaddress.address,
        phone: this.updateaddress.phone,
      })
        .then((res) => {
          this.$notify({
            title: "変更が完了しました",
            message: res,
            type: "success",
            offset: 100,
          });
          this.editaddress.username = this.updateaddress.username;
          this.editaddress.address = this.updateaddress.address;
          this.editaddress.phone = this.updateaddress.phone;
          this.drawer = false;
        })
        .catch((err) => {
          console.log(err);
        });
    },
  },
};
</script>
<style scoped>
.selectbar {
  display: flex;
}

.updatebar {
  max-width: 30em;
  margin: auto;
}

.but {
  display: flex;
  justify-content: space-between;
}

.pagination {
  margin-top: 1em;
  display: flex;
  justify-content: center;
}
</style>
