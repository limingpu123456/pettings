<!--注文管理-->
<template>
  <div class="petordermanage">
    <div class="selectbar">
      <el-form :inline="true" :model="form">
        <el-form-item label="注文ID">
          <el-input
            clearable
            v-model="form.poid"
            placeholder="ペットID"
          ></el-input>
        </el-form-item>
        <el-form-item label="注文ペットID">
          <el-input
            clearable
            v-model="form.pid"
            placeholder="名前"
          ></el-input>
        </el-form-item>
        <el-form-item label="注文ユーザーID">
          <el-input
            clearable
            v-model="form.uid"
            placeholder="ユーザーID"
          ></el-input>
        </el-form-item>
        <el-form-item label="受取人">
          <el-input
            clearable
            v-model="form.recipientname"
            placeholder="受取人"
          ></el-input>
        </el-form-item>
        <el-form-item label="配送住所">
          <el-input
            clearable
            v-model="form.address"
            placeholder="配送住所"
          ></el-input>
        </el-form-item>
        <el-form-item label="連絡電話番号">
          <el-input
            clearable
            v-model="form.phone"
            placeholder="連絡電話番号"
          ></el-input>
        </el-form-item>
        <el-form-item label="ペット状態">
          <el-select
            style="width: 100%"
            v-model="form.postatu"
            clearable
            filterable
            placeholder="ペット状態"
          >
            <el-option
              v-for="item in postatus"
              :key="item.postatu"
              :label="item.postatuname"
              :value="item.postatu"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="注文日時">
          <el-date-picker
            type="date"
            format="yyyy-MM-dd"
            value-format="yyyy-MM-dd"
            placeholder="日付選択"
            v-model="form.date"
          ></el-date-picker>
        </el-form-item>
        <el-form-item>
          <el-button
            @click="querypetorder"
            icon="el-icon-search"
            type="primary"
            circle
          ></el-button>
        </el-form-item>
      </el-form>
    </div>
    <div>
      <el-table :data="petorder" style="width: 100%">
        <el-table-column prop="poid" label="注文ID" sortable width="90">
        </el-table-column>
        <el-table-column prop="pid" label="ペットID" width="90">
        </el-table-column>
        <el-table-column prop="pet.petname" label="名前" width="130">
        </el-table-column>
        <el-table-column prop="uid" label="注文ユーザーID" width="100">
        </el-table-column>
        <el-table-column prop="user.username" label="下单ユーザー名" width="130">
        </el-table-column>
        <el-table-column prop="recipientname" label="受取人" width="100">
        </el-table-column>
        <el-table-column prop="address" label="配送住所"> </el-table-column>
        <el-table-column prop="phone" label="連絡電話番号" width="150">
        </el-table-column>
        <el-table-column width="100" label="注文状況">
          <template slot-scope="scope">
            <span style="color: rgb(255, 194, 8)" v-if="scope.row.postatu == 0"
              >進行中</span
            >
            <span style="color: rgb(33, 213, 168)" v-if="scope.row.postatu == 1"
              >完了済み</span
            >
            <span
              style="color: rgb(204, 203, 203)"
              v-if="scope.row.postatu == 2"
              >キャンセル</span
            >
            <span
              style="color: rgb(102, 177, 255)"
              v-if="scope.row.postatu == 3"
              >変更中</span
            >
            <span style="color: red" v-if="scope.row.postatu == 4">キャンセル中</span>
          </template>
        </el-table-column>
        <el-table-column prop="date" sortable label="注文日時" width="180">
        </el-table-column>
        <el-table-column label="操作" width="150">
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
        <el-form :model="updateform" label-width="80px">
          <el-form-item label="受取人">
            <el-input
              v-model="updateform.recipientname"
              placeholder="受取人"
              clearable
            ></el-input>
          </el-form-item>
          <el-form-item label="配送住所">
            <el-input
              v-model="updateform.address"
              placeholder="配送住所"
              clearable
            ></el-input>
          </el-form-item>
          <el-form-item label="連絡電話番号">
            <el-input
              v-model="updateform.phone"
              placeholder="連絡電話番号"
              clearable
            ></el-input>
          </el-form-item>
          <el-form-item label="注文状況">
            <el-select
              style="width: 100%"
              v-model="updateform.postatu"
              clearable
              filterable
              placeholder="ペット状態"
            >
              <el-option
                v-for="item in postatus"
                :key="item.postatu"
                :label="item.postatuname"
                :value="item.postatu"
              >
              </el-option>
            </el-select>
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
  requestquerypetorderpage,
  requestdeletepetorder,
  requestupdatepetorder,
} from "network/requestpetorder.js";

export default {
  name: "petordermanage",
  data() {
    return {
      pagesize: 10,
      pagercount: 5,
      total: 10,
      currentpage: 1,
      //0-进行中，1-完了済み，2-已キャンセル，3-修正中，4-キャンセル中
      postatus: [
        {
          postatu: 0,
          postatuname: "進行中",
        },
        {
          postatu: 1,
          postatuname: "完了済み",
        },
        {
          postatu: 2,
          postatuname: "キャンセル",
        },
        {
          postatu: 3,
          postatuname: "変更中",
        },
        {
          postatu: 4,
          postatuname: "キャンセル中",
        },
      ],
      form: {},
      petorder: [],
      editform: {},
      updateform: {},
      drawer: false,
    };
  },
  created() {
    requestquerypetorderpage({
      page: this.currentpage,
      count: this.pagesize,
    })
      .then((res) => {
        this.petorder = res.petorder;
        this.total = res.total;
      })
      .catch((err) => {
        console.log(err);
      });
  },
  methods: {
    handleCurrentChange() {
      requestquerypetorderpage({
        poid: this.form.poid,
        pid: this.form.pid,
        uid: this.form.uid,
        recipientname: this.form.recipientname,
        address: this.form.address,
        phone: this.form.phone,
        postatu: this.form.postatu,
        date: this.form.date,
        page: this.currentpage,
        count: this.pagesize,
      })
        .then((res) => {
          this.petorder = res.petorder;
          this.total = res.total;
        })
        .catch((err) => {
          console.log(err);
        });
    },
    querypetorder() {
      this.currentpage = 1;
      requestquerypetorderpage({
        poid: this.form.poid,
        pid: this.form.pid,
        uid: this.form.uid,
        recipientname: this.form.recipientname,
        address: this.form.address,
        phone: this.form.phone,
        postatu: this.form.postatu,
        date: this.form.date,
        page: this.currentpage,
        count: this.pagesize,
      })
        .then((res) => {
          this.petorder = res.petorder;
          this.total = res.total;
        })
        .catch((err) => {
          console.log(err);
        });
    },
    handleEdit(index, row) {
      this.editform = row;
      let editRow = JSON.stringify(row);
      this.updateform = JSON.parse(editRow);
      this.drawer = true;
    },
    handleDelete(index, row) {
      requestdeletepetorder({
        poid: row.poid,
      })
        .then((res) => {
          this.$notify({
            title: "削除成功",
            message: res,
            offset: 100,
            type: "success",
          });
          requestquerypetorderpage({
            poid: this.form.poid,
            pid: this.form.pid,
            uid: this.form.uid,
            recipientname: this.form.recipientname,
            address: this.form.address,
            phone: this.form.phone,
            postatu: this.form.postatu,
            date: this.form.date,
            page: this.currentpage,
            count: this.pagesize,
          })
            .then((res) => {
              this.petorder = res.petorder;
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
      requestupdatepetorder({
        poid: this.updateform.poid,
        recipientname: this.updateform.recipientname,
        address: this.updateform.address,
        phone: this.updateform.phone,
        postatu: this.updateform.postatu,
      })
        .then((res) => {
          this.$notify({
            title: "変更が完了しました",
            message: "注文の変更が完了しました",
            type: "success",
            offset: 100,
          });
          this.editform.recipientname = this.updateform.recipientname;
          this.editform.address = this.updateform.address;
          this.editform.phone = this.updateform.phone;
          this.editform.postatu = this.updateform.postatu;
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
