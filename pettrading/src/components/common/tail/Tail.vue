<!---->
<template>
  <div class='tail'>
    <div class="mainbar container">
      <div>連絡電話番号：090-1283-4756</div>
      <div>連絡メールアドレス： limingpu0327@gmail.com</div>
    </div>
  </div>
</template>

<script>
  export default {
    name: 'Tail',
    data () {
      return {}
    }
  }
</script>
<style scoped>
@import "~assets/css/mediacss.css";

.tail {
  width: 100%;
  min-height: 5em;
  margin-top: 10em;
  background: rgb(39, 39, 39);
  color: rgb(156, 156, 156);
}

.mainbar {
  display: flex;
  justify-content: space-around;
}
</style>
