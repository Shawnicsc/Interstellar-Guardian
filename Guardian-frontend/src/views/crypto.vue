<template>
  <a-row justify="center" align="middle" style="min-height: 100vh; background-color: #f0f2f5;">
    <a-col :span="24" :md="14">
      <a-card title="字符串加解密" bordered={false}>
        <a-form layout="vertical">
          <a-form-item label="选择算法">
            <a-select v-model="algorithm" placeholder="请选择加密算法">
              <a-select-option value="AES">AES</a-select-option>
              <a-select-option value="ECDSA">ECDSA</a-select-option>
              <a-select-option value="SHA256">SHA256</a-select-option>
              <a-select-option value="SM2">SM2</a-select-option>
              <a-select-option value="SM3">SM3</a-select-option>
              <a-select-option value="SM4">SM4</a-select-option>
              <a-select-option value="Paillier">Paillier</a-select-option>
            </a-select>
          </a-form-item>
          <a-form-item label="输入字符串">
            <a-textarea v-model="inputString" placeholder="请输入要加密或解密的字符串" rows="4" />
          </a-form-item>
          <a-form-item>
            <a-space>
              <a-button type="primary" @click="encryptString">加密</a-button>
              <a-button type="danger" @click="decryptString">解密</a-button>
            </a-space>
          </a-form-item>
          <a-form-item label="输出字符串">
            <a-textarea v-model="outputString" placeholder="输出结果将在此显示" rows="4" readonly />
          </a-form-item>
        </a-form>
      </a-card>
    </a-col>
    <a-col :span="24" :md="10" >
      <div >
        <img src="/images/bug.jpg"  style="max-width: 350px; height: 640px; display: block; margin: 0 auto;" />
      </div>

    </a-col>
  </a-row>
</template>

<script>

export default {
  data() {
    return {
      algorithm: null,
      inputString: '',
      outputString: '',
    };
  },
  methods: {
    encryptString() {
      if (!this.algorithm || !this.inputString) {
        this.$message.error('请选择算法并输入字符串');
        return;
      }
      let form = new FormData()
      form.append('alg', this.algorithm)
      form.append('text',this.inputString)
      this.request.post('/cipher/encrypt',form)
          .then(res => {
            this.outputString = res.encryptedText
            console.log(res)
          })

    },
    decryptString() {
      if (!this.algorithm || !this.inputString) {
        this.$message.error('请选择算法并输入字符串');
        return;
      }
      let form = new FormData()
      form.append('alg', this.algorithm)
      form.append('text',this.inputString)
      this.request.post('/cipher/decrypt',form)
          .then(res => {
            this.outputString = res.decryptedText
            console.log(res)
          })
    },
  },
};
</script>

<style scoped>
a-card {
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}
</style>
