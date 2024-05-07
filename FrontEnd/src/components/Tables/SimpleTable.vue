<template>
  <div>
    <md-table v-model="users" :table-header-color="tableHeaderColor">
      <md-table-row slot="md-table-row" slot-scope="{ item }">
        <md-table-cell md-label="HashCode">{{ item.hashcode }}</md-table-cell>
        <md-table-cell md-label="UserID">{{ item.userid }}</md-table-cell>
        <md-table-cell>
          <md-button class="md-icon-button" @click="share(item)">
            <md-icon>share</md-icon>
          </md-button>
        </md-table-cell>
      </md-table-row>
    </md-table>
  </div>
</template>

<script>
export default {
  name: "simple-table",
  props: {
    tableHeaderColor: {
      type: String,
      default: "",
    },
  },
  data() {
    const user = JSON.parse(localStorage.getItem("user"));
    return {
      selected: [],
      users: [],
      user: user,
      userId : ''
    };
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
        this.users = res
      })
    },
    share(item){
      let formData = new FormData();
      formData.append('hash',item.hashcode)
      this.request.post('/ipfs/share',formData).then(res=>{
        if(res.code === "200"){
          alert("分享码生成成功："+res.data)
        }
      })
    }
  }
};
</script>
