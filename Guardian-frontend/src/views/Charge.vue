<template>
  <a-card title="收款">
    <a-row :gutter="16">
      <a-col :span="12">
        <a-card hoverable cover>
          <img alt="收款码1" src="/images/wecaht.jpg" />
          <a-card-meta title="微信支付" description="扫描二维码进行支付" />
        </a-card>
      </a-col>
      <a-col :span="12">
        <a-card hoverable cover>
          <img alt="收款码2" src="/images/alipay.jpg" />
          <a-card-meta title="支付宝支付" description="扫描二维码进行支付" />
        </a-card>
      </a-col>
    </a-row>
  </a-card>
</template>

<script>
export default {
  name: 'ChargePage',
};
</script>

<style scoped>
img {
  width: 100%;
  height: auto;
}
</style>
