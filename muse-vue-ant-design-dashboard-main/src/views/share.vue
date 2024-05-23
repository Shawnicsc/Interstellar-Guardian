<template>
  <div class="content">
    <a-card
        style="width: 400px; margin: 0 auto; background: rgba(255, 255, 255, 0.8); padding: 20px;"
        bordered="false"
    >
      <a-row justify="center">
        <a-col span={24}>
          <h2 style="text-align: center;">分享码校验</h2>
          <p style="text-align: center; margin-bottom: 24px;"></p>
          <a-form layout="vertical" @submit.prevent="validateShareCode">
            <a-form-item label="分享码">
              <a-input v-model:value="shareCode" placeholder="输入分享码" />
            </a-form-item>
            <a-form-item>
              <a-button type="primary" @click="validateShareCode" block>校验</a-button>
            </a-form-item>
          </a-form>
        </a-col>
      </a-row>
    </a-card>
  </div>
</template>

<script>
export default {
  data() {
    return {
      shareCode: '',
    };
  },
  methods: {
    validateShareCode() {
      let formData = new FormData();
      formData.append('hash', this.shareCode);
      this.request.post('/ipfs/check', formData)
          .then(res => {
            if (res.code === "200") {
              this.$message.success('校验成功，分享的hash值为' + res.data);
            } else {
              this.$message.error('校验失败，请检查分享码');
            }
          })
          .catch(() => {
            this.$message.error('请求失败，请稍后再试');
          });
    },
  },
};
</script>

<style scoped>
.content {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background: url('E:/vue/muse-vue-ant-design-dashboard-main/public/images/share.jpg') no-repeat center center fixed;
  background-size: cover;
}

a-card {
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
  border-radius: 8px;
}

h2 {
  text-align: center;
}

p {
  text-align: center;
  margin-bottom: 24px;
}
</style>
