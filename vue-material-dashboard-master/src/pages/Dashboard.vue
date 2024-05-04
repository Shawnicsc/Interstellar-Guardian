<template>
  <div class="content">
    <div class="md-layout">
      <div>
      <el-upload
          :ref="upload"
          action="https://jsonplaceholder.typicode.com/posts/"
          multiple
          list-type="picture-card"
          :limit="8"
          :file-list="imageUrls"
          :on-remove="handleRemove"
          :on-exceed="handleExceed"
          :on-success="uploadSuccess"
          :on-error="error"
          :before-upload="beforeUpload"
          :before-remove="beforeRemove"
          :auto-upload="false"
      >
      <el-button slot="trigger" size="small" type="primary">选取文件</el-button>
      <el-button style="margin-left: 10px;" size="small" type="success" @click="submitUpload">上传到服务器</el-button>
      </el-upload>

      </div>
    </div>
  </div>
</template>

<script>
import {
  StatsCard,
  ChartCard,
  NavTabsCard,
  NavTabsTable,
  OrderedTable,
} from "@/components";

export default {
  components: {

  },
  data() {
    return {}
  },

};
</script>
<style>
/* 添加样式调整以确保美观排版 */
.content {
  display: flex;
  justify-content: center;
}

/* 可以根据需要调整卡片的样式 */
.v-card {
  margin: 10px;
  padding: 20px;
  text-align: center;
}
</style>