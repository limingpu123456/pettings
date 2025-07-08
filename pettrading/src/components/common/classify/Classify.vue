<!--搜索规则-->
<template>
  <div class='classify'>
    <classifyitem :kind="sort"></classifyitem>
    <classifyitem :kind="bkind"></classifyitem>
    <classifyitem :kind="skind"></classifyitem>
  </div>
</template>

<script>
  import Classifyitem from "components/common/classify/Classifyitem.vue"

  export default {
    name: 'Classify',
    props: {
      bkind: Array,
      skind: Array
    },
    data () {
      return {
        sort: [
          {
            id: 0,
            name: "デフォルトの並び順"
          },{
            id: 1,
            name: "最少クリック"
          },{
            id: 2,
            name: "最多クリック"
          },{
            id: 2,
            name: "最も古い投稿"
          },{
            id: 2,
            name: "最新の投稿"
          }
        ]
      }
    },
    components: {
      Classifyitem
    }
  }
</script>
<style scoped>
</style>
