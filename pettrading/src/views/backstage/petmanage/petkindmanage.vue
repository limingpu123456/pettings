<!--種類管理-->
<template>
  <div class="petkindmanage">
    <div class="bkindbar">
      <div class="selectbar">
        <el-form :inline="true" :model="bkindform">
          <el-form-item label="タイプid">
            <el-input
              style="width: 180px"
              clearable
              v-model="bkindform.bkid"
              placeholder="タイプid"
            ></el-input>
          </el-form-item>
          <el-form-item label="タイプ名称">
            <el-input
              style="width: 180px"
              clearable
              v-model="bkindform.bkindname"
              placeholder="タイプ名称"
            ></el-input>
          </el-form-item>
          <el-form-item>
            <el-button
              clearable
              icon="el-icon-search"
              type="primary"
              circle
              @click="querybkind"
            ></el-button>
          </el-form-item>
        </el-form>
        <el-form :inline="true" :model="bkindaddform">
          <el-form-item label="タイプ名称">
            <el-input
              style="width: 180px"
              clearable
              v-model="bkindaddform.bkindname"
              placeholder="タイプ名称"
            ></el-input>
          </el-form-item>
          <el-form-item>
            <el-button
              clearable
              icon="el-icon-plus"
              type="primary"
              circle
              @click="addbkind"
            ></el-button>
          </el-form-item>
        </el-form>
      </div>
      <div class="bkind">
        <el-table :data="bkind" border style="width: 541px">
          <el-table-column prop="bkid" label="タイプid" sortable width="180">
          </el-table-column>
          <el-table-column label="タイプ名称" width="180">
            <template slot-scope="scope">
              <span v-if="showbkindindex != scope.$index">{{
                scope.row.bkindname
              }}</span>
              <el-input
                v-if="showbkindindex == scope.$index"
                clearable
                v-model="upbkind.bkindname"
                type="primary"
              ></el-input>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="180">
            <template slot-scope="scope">
              <span v-if="showbkindindex == scope.$index">
                <el-button
                  size="mini"
                  type="primary"
                  @click="updatebkind(scope.$index, scope.row)"
                  >確認</el-button
                >
                <el-button size="mini" type="info" @click="showbkindindex = -1"
                  >キャンセル</el-button
                >
              </span>
              <span v-if="showbkindindex != scope.$index">
                <el-button
                  size="mini"
                  @click="handleEditbkind(scope.$index, scope.row)"
                  >編集</el-button
                >
                <el-popconfirm
                  confirmButtonText="確定"
                  cancelButtonText="キャンセル"
                  placement="top"
                  icon="el-icon-info"
                  iconColor="red"
                  title="このユーザーを削除してもよろしいですか"
                  @confirm="handleDeletebkind(scope.$index, scope.row)"
                >
                  <el-button
                    v-if="showbkindindex != scope.index"
                    style="margin-left: 10px"
                    size="mini"
                    type="danger"
                    slot="reference"
                    >削除</el-button
                  >
                </el-popconfirm>
              </span>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>
    <div class="skindbar">
      <div class="selectbar">
        <el-form :inline="true" :model="skindform">
          <el-form-item label="品種id">
            <el-input
              style="width: 180px"
              clearable
              v-model="skindform.skid"
              placeholder="品種id"
            ></el-input>
          </el-form-item>
          <el-form-item label="对应タイプid">
            <el-input
              style="width: 180px"
              clearable
              v-model="skindform.bkid"
              placeholder="タイプid"
            ></el-input>
          </el-form-item>
          <el-form-item label="品種名">
            <el-input
              style="width: 180px"
              clearable
              v-model="skindform.skindname"
              placeholder="品種名"
            ></el-input>
          </el-form-item>
          <el-form-item>
            <el-button
              clearable
              icon="el-icon-search"
              type="primary"
              circle
              @click="queryskind"
            ></el-button>
          </el-form-item>
        </el-form>
        <el-form :inline="true" :model="skindaddform">
          <el-form-item label="对应タイプid">
            <el-input
              style="width: 180px"
              clearable
              v-model="skindaddform.bkid"
              placeholder="タイプid"
            ></el-input>
          </el-form-item>
          <el-form-item label="品種名">
            <el-input
              style="width: 180px"
              clearable
              v-model="skindaddform.skindname"
              placeholder="品種名"
            ></el-input>
          </el-form-item>
          <el-form-item>
            <el-button
              clearable
              icon="el-icon-plus"
              type="primary"
              circle
              @click="addskind"
            ></el-button>
          </el-form-item>
        </el-form>
      </div>
      <div class="skind">
        <el-table :data="skind" border style="width: 721px">
          <el-table-column prop="skid" label="品種id" sortable width="180">
          </el-table-column>
          <el-table-column label="対応タイプID" width="180">
            <template slot-scope="scope">
              <span v-if="showskindindex != scope.$index">{{
                scope.row.bkid
              }}</span>
              <el-input
                v-if="showskindindex == scope.$index"
                clearable
                v-model="upskind.bkid"
                type="primary"
              ></el-input>
            </template>
          </el-table-column>
          <el-table-column label="品種名" width="180">
            <template slot-scope="scope">
              <span v-if="showskindindex != scope.$index">{{
                scope.row.skindname
              }}</span>
              <el-input
                v-if="showskindindex == scope.$index"
                clearable
                v-model="upskind.skindname"
                type="primary"
              ></el-input>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="180">
            <template slot-scope="scope">
              <span v-if="showskindindex == scope.$index">
                <el-button
                  size="mini"
                  type="primary"
                  @click="updateskind(scope.$index, scope.row)"
                  >確認</el-button
                >
                <el-button size="mini" type="info" @click="showskindindex = -1"
                  >キャンセル</el-button
                >
              </span>
              <span v-if="showskindindex != scope.$index">
                <el-button
                  size="mini"
                  @click="handleEditskind(scope.$index, scope.row)"
                  >編集</el-button
                >
                <el-popconfirm
                  confirmButtonText="確定"
                  cancelButtonText="キャンセル"
                  placement="top"
                  icon="el-icon-info"
                  iconColor="red"
                  title="このユーザーを削除してもよろしいですか"
                  @confirm="handleDeleteskind(scope.$index, scope.row)"
                >
                  <el-button
                    style="margin-left: 10px"
                    size="mini"
                    type="danger"
                    slot="reference"
                    >削除</el-button
                  >
                </el-popconfirm>
              </span>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>
  </div>
</template>

<script>
import {
  requestqueryBKindandSKind,
  requestaddBKind,
  requestdeleteBKind,
  requestupdateBKind,
} from "network/requestbkind.js";
import {
  requestqueryallskind,
  requestaddskind,
  requestdeleteskind,
  requestupdateskind,
} from "network/requestskind.js";

export default {
  name: "petkindmanage",
  data() {
    return {
      showbkindindex: -1,
      showskindindex: -1,
      upbkind: {},
      upskind: {},
      bkind: [],
      skind: [],
      bkindform: {},
      bkindaddform: {},
      skindform: {},
      skindaddform: {},
    };
  },
  created() {
    requestqueryBKindandSKind()
      .then((res) => {
        this.bkind = res;
      })
      .catch((err) => {
        console.log(err);
      });
    requestqueryallskind()
      .then((res) => {
        this.skind = res;
      })
      .catch((err) => {
        console.log(err);
      });
  },
  methods: {
    querybkind() {
      requestqueryBKindandSKind({
        bkid: this.bkindform.bkid,
        bkindname: this.bkindform.bkindname,
      })
        .then((res) => {
          this.bkind = res;
        })
        .catch((err) => {
          console.log(err);
        });
    },
    addbkind() {
      requestaddBKind({
        bkindname: this.bkindaddform.bkindname,
      })
        .then((res) => {
          this.$notify({
            title: "追加に成功しました",
            message: res.msg,
            type: "success",
            offset: 100,
          });
          this.bkindaddform.bkid = res.bkid;
          let addform = JSON.stringify(this.bkindaddform);
          this.bkind.push(JSON.parse(addform));
        })
        .catch((err) => {
          console.log(err);
        });
    },
    queryskind() {
      requestqueryallskind({
        skid: this.skindform.skid,
        bkid: this.skindform.bkid,
        skindname: this.skindform.skindname,
      })
        .then((res) => {
          this.skind = res;
        })
        .catch((err) => {
          console.log(err);
        });
    },
    addskind() {
      requestaddskind({
        bkid: this.skindaddform.bkid,
        skindname: this.skindaddform.skindname,
      })
        .then((res) => {
          this.$notify({
            title: "追加に成功しました",
            message: res.msg,
            type: "success",
            offset: 100,
          });
          this.skindaddform.skid = res.skid;
          let addform = JSON.stringify(this.skindaddform);
          this.skind.push(JSON.parse(addform));
        })
        .catch((err) => {
          console.log(err);
        });
    },
    handleEditbkind(index, row) {
      this.showbkindindex = index;
      let cbkind = JSON.stringify(row);
      this.upbkind = JSON.parse(cbkind);
    },
    updatebkind() {
      requestupdateBKind({
        bkid: this.upbkind.bkid,
        bkindname: this.upbkind.bkindname,
      })
        .then((res) => {
          this.$notify({
            title: "変更が完了しました",
            message: res,
            type: "success",
            offset: 100,
          });
          this.bkind[this.showbkindindex].bkindname = this.upbkind.bkindname;
          this.showbkindindex = -1;
        })
        .catch((err) => {
          console.log(err);
        });
    },
    handleDeletebkind(index, row) {
      requestdeleteBKind({
        bkid: row.bkid,
      })
        .then((res) => {
          this.$notify({
            title: "削除が成功しました",
            message: res,
            type: "success",
            offset: 100,
          });
          this.bkind.splice(index, 1);
        })
        .catch((err) => {
          console.log(err);
        });
    },
    handleEditskind(index, row) {
      this.showskindindex = index;
      let cskind = JSON.stringify(row);
      this.upskind = JSON.parse(cskind);
    },
    updateskind() {
      requestupdateskind({
        skid: this.upskind.skid,
        bkid: this.upskind.bkid,
        skindname: this.upskind.skindname,
      })
        .then((res) => {
          this.$notify({
            title: "編集が成功しました",
            message: res,
            type: "success",
            offset: 100,
          });
          this.skind[this.showskindindex].bkid = this.upskind.bkid;
          this.skind[this.showskindindex].skindname = this.upskind.skindname;
          this.showskindindex = -1;
        })
        .catch((err) => {
          console.log(err);
        });
    },
    handleDeleteskind(index, row) {
      requestdeleteskind({
        skid: row.skid,
      })
        .then((res) => {
          this.$notify({
            title: "削除が成功しました",
            message: res,
            type: "success",
            offset: 100,
          });
          this.skind.splice(index, 1);
        })
        .catch((err) => {
          console.log(err);
        });
    },
  },
};
</script>
<style scoped>
.petkindmanage {
  display: flex;
  justify-content: space-between;
}

.bkind {
  margin-right: 2em;
}

.selectbar {
  margin-bottom: 1em;
}
</style>
