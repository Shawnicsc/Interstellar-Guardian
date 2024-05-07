<template>
  <div class="content">
    <div class="md-layout">
      <md-field >
        <label>分享码</label>
        <md-input v-model="shareCode" placeholder="输入分享码"></md-input>
      </md-field>
      <md-button @click="validateShareCode" class="md-primary">校验</md-button>
    </div>
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
      formData.append('hash',this.shareCode)
      this.request.post('/ipfs/check',formData)
      .then(res=>{
        if(res.code === "200"){
          alert("校验成功，分享的hash值为"+res.data)
        }
      })
    },

  }
};
</script>

