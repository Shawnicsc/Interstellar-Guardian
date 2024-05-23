<template>
  <a-card :bordered="false" class="header-solid h-full" :bodyStyle="{padding: 0,}">
    <template #title>
      <a-row type="flex" align="middle">
        <a-col :span="24" :md="12">
          <h5 class="font-semibold m-0">已下载文件</h5>
        </a-col>
      </a-row>
    </template>
    <a-table :columns="columns" :data-source="data" :pagination="false">
    </a-table>
    <div class="download-section">
      <a-input v-model="shareCode" placeholder="输入分享码" style="width: 85%; margin-right: 10px;" />
      <a-button type="primary" @click="validateShareCode">下载文件</a-button>
    </div>
  </a-card>
</template>

<script>

export default {

  data() {
    const user = JSON.parse(localStorage.getItem("user"));
    return {
      shareCode: '',
      columns: [
        {
          title: '文件名',
          dataIndex: 'fileName',
          key: 'fileName',
        },
        {
          title: 'Hash值',
          dataIndex: 'hashcode',
          key: 'hashcode'
        },
      ],
      data: [],
      user: user,
    };
  },
  created() {
    this.getDownloadList()
  },
  methods: {
    getDownloadList(){
      let userId = this.user.id
      this.request.get('/ipfs/getDownloadList',{
        params:{
          userId: userId
        }
      }).then(res=>{
        this.data = res.map((item, index) => {
          return {
            ...item,
            key: index // 为每个项目添加唯一的 key 标识
          };
        });
      })
    },
    validateShareCode() {
      let formData = new FormData();
      let userId = this.user.id
      formData.append('hash', this.shareCode);
      formData.append('userId',userId)
      this.request.post('/ipfs/check', formData)
          .then(res => {
            if (res.code === "200") {
              this.$message.success("分享码校验成功，等待下载")
              console.log(res.data)

              window.open("http://localhost:8082/ipfs/downloadFile?hash=" + res.data.hashcode + "&userId=" + userId)
              this.$router.go(0);
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

</style>
