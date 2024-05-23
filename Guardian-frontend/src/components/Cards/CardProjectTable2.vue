<template>
  <!-- Projects Table Column -->
  <a-card :bordered="false" class="header-solid h-full" :bodyStyle="{padding: 0,}">
    <template #title>
      <a-row type="flex" align="middle">
        <a-col :span="24" :md="12">
          <h5 class="font-semibold m-0">已上传文件</h5>
        </a-col>
      </a-row>
    </template>
    <a-table :columns="columns" :data-source="data" :pagination="false">
      <template slot="shareBtn" slot-scope="row">
        <a-button type="link" :data-id="row.key" @click="share(row)">
          <svg width="20" height="20" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path class="fill-gray-7" d="M18 16.08C17.24 16.08 16.56 16.38 16.04 16.84L8.91 12.7C8.96 12.47 9 12.24 9 12C9 11.76 8.96 11.53 8.91 11.3L15.96 7.2C16.5 7.68 17.21 8 18 8C19.66 8 21 6.66 21 5C21 3.34 19.66 2 18 2C16.34 2 15 3.34 15 5C15 5.24 15.04 5.47 15.09 5.7L8.04 9.8C7.5 9.32 6.79 9 6 9C4.34 9 3 10.34 3 12C3 13.66 4.34 15 6 15C6.79 15 7.5 14.68 8.04 14.2L15.16 18.34C15.11 18.55 15.08 18.77 15.08 19C15.08 20.66 16.42 22 18.08 22C19.74 22 21.08 20.66 21.08 19C21.08 17.34 19.74 16 18.08 16H18Z"/>
          </svg>
        </a-button>
      </template>
    </a-table>
    <div class="table-upload-btn">
      <!-- Project Upload Component -->
      <a-upload
          action="http://localhost:8083/guardian/ipfs/uploadFile"
          name="file"
          :data="{ userId: value }"
          :headers="{ token: token }"
          list-type="picture-card"
          class="projects-uploader"
          :show-upload-list="false"
          :before-upload="beforeUpload"
          @change="handleChange"
          :file-list="fileList"
      >
        <img v-if="false" src="" alt="avatar" />
        <div v-else>
          <a-icon v-if="false" type="loading" />
          <svg v-else width="20" height="20" viewBox="0 0 20 20" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path fill-rule="evenodd" clip-rule="evenodd" d="M3 17C3 16.4477 3.44772 16 4 16H16C16.5523 16 17 16.4477 17 17C17 17.5523 16.5523 18 16 18H4C3.44772 18 3 17.5523 3 17ZM6.29289 6.70711C5.90237 6.31658 5.90237 5.68342 6.29289 5.29289L9.29289 2.29289C9.48043 2.10536 9.73478 2 10 2C10.2652 2 10.5196 2.10536 10.7071 2.29289L13.7071 5.29289C14.0976 5.68342 14.0976 6.31658 13.7071 6.70711C13.3166 7.09763 12.6834 7.09763 12.2929 6.70711L11 5.41421L11 13C11 13.5523 10.5523 14 10 14C9.44771 14 9 13.5523 9 13L9 5.41421L7.70711 6.70711C7.31658 7.09763 6.68342 7.09763 6.29289 6.70711Z" fill="#111827"/>
          </svg>
          <div class="ant-upload-text font-semibold text-dark">
            上传文件
          </div>
        </div>
      </a-upload>
      <!-- Download Component -->
      <div class="download-section">
        <a-input v-model="downloadHash" placeholder="输入文件Hash值" style="width: 85%; margin-right: 10px;" />
        <a-button type="primary" @click="downloadFile">下载文件</a-button>
      </div>

    </div>
  </a-card>
  <!-- / Projects Table Column -->

</template>

<script>
export default {
  data() {
    const user = JSON.parse(localStorage.getItem("user"));
    return {
      // Active button for the "Projects" table's card header radio button group.
      projectHeaderBtns: 'all',
      user: user,
      downloadHash: '',
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
        {
          title: '',
          scopedSlots: { customRender: 'shareBtn' },
          key: 'shareButton',
          width: 50,
        },
      ],
      data:[],
      file:'',
      token: user ? user.token : null,
      value: user ? user.id : null,
      userId : '',
      fileList:[],
    }
  },
  created() {
    this.getHashInfo()
  },
  methods:{
    getHashInfo(){
      this.userId = this.user.id
      this.request.get('/ipfs/getList',{
        params:{
          userId: this.userId
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
    handleChange(info){
      let fileList = [...info.fileList];
      this.fileList = fileList
      console.log(this.fileList)

      if (info.file.status !== 'uploading') {
        // // 文件大小校验
        // const isLt100M = info.file.size / 1024 / 1024 < 100;
        // if (!isLt100M) {
        //   this.$message.error('文件大小不能超过 100MB!');
        //   return false;
        // }
        //
        // // 文件名校验
        // const fileExists = this.data.some(existingFile => existingFile.fileName === info.file.name);
        // if (fileExists) {
        //   this.$message.error('已存在相同文件名，请重新命名后上传');
        //   return false;
        // }
        console.log(info.file, info.fileList);
      }
      if (info.file.status === 'done') {
        fileList = fileList.map(file=>{
          if(file.response.code === '200'){
            this.$message.success(`${info.file.name} 上传成功，hash值为:` + file.response.data);

          }
          else{
            this.$message.error(`${info.file.name} 上传失败`)
          }
        })

      } else if (info.file.status === 'error') {
        this.$message.error(`${info.file.name} file upload failed.`);
      }
    },
    beforeUpload(file) {
      const isLt100M = file.size / 1024 / 1024 < 100;
      if (!isLt100M) {
        this.$message.error('文件大小不能超过 100MB!');
      }

      const fileExists = this.data.some(existingFile => existingFile.fileName === file.name);
      if (fileExists) {
        this.$message.error('已存在相同文件名，请重新命名后上传');
        return false;
      }
      return isLt100M;
    },
    handleUploadSuccess(response,file) {
      // 处理上传成功逻辑
      if(response.code === '200'){
        this.$message.success("上传成功，hash值为:" + response.data)
      }
      else{
        this.$message.error("上传失败，请稍后再试")
      }
    },
    handleUploadError(error,file) {
      // 处理上传失败逻辑
      this.message.warning("上传失败，请稍后再试")
    },
    share(item) {
      console.log(item)
      this.userId = this.user.id
      let formData = new FormData();
      formData.append('hash', item.hashcode);
      formData.append('userId',this.userId)
      this.request.post('/ipfs/share', formData).then(res => {
        if(res.code === "200"){
          this.$message.success("分享码生成成功--" + res.data)
        } else {
          this.$message.error("分享失败，请稍后再试")
        }
      });
    },
    downloadFile() {
      if (this.downloadHash) {
      window.open("http://localhost:8082/ipfs/downloadFile?hash="+this.downloadHash+"&userId="+this.userId)
      }
    }
  }
}
</script>
<style>
  .download-section {
    margin-top: 20px;
  }
</style>
