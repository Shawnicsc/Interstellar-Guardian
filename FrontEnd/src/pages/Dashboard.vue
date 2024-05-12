<template>
  <div class="content">
    <div class="left-section">
      <h2>Upload</h2>
      <div class="upload-container">
        <el-form :model="ipfsFile">
          <el-form-item>
            <el-input v-model="ipfsFile.secretKey" placeholder="Upload String"></el-input>
          </el-form-item>
        </el-form>
        <el-button @click="uploadStringData" type="primary" class="upload-button">Upload String</el-button>
      </div>
      <div class="upload-result">{{ this.uploadResult }}</div>

      <el-upload
          ref="uploadRef"
          class="uploadFile"
          action="#"
          multiple
          :http-request="handleFileUpload"
          :auto-upload="false"
          :data={userId:value}
          :on-success="handleUploadSuccess"
          :limit="1"
          :file-list="fileList"
      >

        <template #trigger>
          <el-button type="primary" round>选择文件</el-button>
        </template>

        <el-button class="ml-3" type="submit" @click="submitUpload" round>
          上传
        </el-button>
      </el-upload>
      <div class="upload-result">{{ uploadFileResult }}</div>
    </div>

    <div class="right-section">
      <h2>Download</h2>
      <div class="download-container">
        <el-input v-model="downloadHash" placeholder="Enter Hash Value"></el-input>
        <el-select v-model="downloadType" placeholder="Select Type">
          <el-option label="String" value="string"></el-option>
          <el-option label="File" value="file"></el-option>
        </el-select>
        <el-button @click="downloadData" type="primary" class="download-button">Download</el-button>
      </div>
      <div class="download-result">{{ downloadResult }}</div>
    </div>
  </div>
</template>

<script>


import axios from "axios";

export default {
  data() {
    const user = JSON.parse(localStorage.getItem("user")); // Parse user from localStorage
    return {
      fileList: [],
      ipfsFile:{
        secretKey: '',
        userid: ''
      },
      file: '',
      uploadResult: '',
      uploadFileResult: '',
      downloadHash: '',
      downloadType: '',
      downloadResult: '',
      user: user,
      value: user ? user.id : null, // Assign user.Id if user exists, otherwise null
      userId: user ? user.id : null // Assign user.Id if user exists, otherwise null
    };
  },
  methods: {
    uploadStringData() {
      this.ipfsFile.userid = this.user.id
      this.request.post('/ipfs/uploadStr',this.ipfsFile)
          .then(response => {
            if(response.code === "200")
            this.uploadResult = response.data;
          })
          .catch(error => {
            console.error(error);
          });
    },
    handleUploadSuccess(response, file) {
      this.uploadFileResult = response.data;
    },
    submitUpload() {
      this.$refs.uploadRef.submit();
    },
    handleFileUpload(info){
      const{ file } = info
      this.file = file
      let fileDao = new FormData()
      fileDao.append('file',file)
      fileDao.append('userId',this.userId)
      this.request.post('/ipfs/uploadFile',fileDao).then(res=>{
        if(res.code === "200")
          this.uploadFileResult = res.data
      }).error(error=>{
        console.log(error)
      })
    },
    downloadData() {
      if(this.downloadType === "string"){
        this.request.get("/ipfs/downloadStr/"+this.downloadHash)
            .then(res =>{
              if(res.code === "200")
                this.downloadResult = res.data
            })
            .catch(error =>{
              console.error(error);
            })
      }
      else{
        window.open("http://localhost:8082/ipfs/downloadFile?hash="+this.downloadHash)
        // axios.get("http://localhost:8082/ipfs/downloadFile",{
        //   params:{
        //     'hash':this.downloadHash
        //   }
        // })
        //     .then(res=>{
        //       console(res.data)
        //     })
        //     .catch(error =>{
        //       console.error(error);
        //     })

      }


    }
  }
};
</script>

<style scoped>
.content {
  display: flex;
  justify-content: space-around;
  align-items: flex-start;
}

.left-section, .right-section {
  width: 45%;
}

.upload-container, .download-container {
  margin-bottom: 20px;
}

.upload-result, .download-result {
  margin-top: 10px;
}

.upload-button, .download-button {
  width: 100%;
  margin-top: 10px; /* Add some space between buttons */
}
</style>
