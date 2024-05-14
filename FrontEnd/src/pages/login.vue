<template>
    <div class="wrapper">
      <div style="margin: 200px auto;background-color: #fff;width: 350px;padding: 20px;border-radius: 10px">
        <div style="margin: 20px 0 ;text-align: center;font-size: 24px;">登录</div>
        <el-form :model="user" :rules="rules" ref="userForm">
          <el-form-item prop="userName">
            <el-input size="medium" style="margin: 10px 0;" prefix-icon="el-icon-user" v-model="user.userName" ></el-input>
          </el-form-item>
          <el-form-item prop="userPassword">
            <el-input size="medium" style="margin: 10px 0;" prefix-icon="el-icon-lock" show-password v-model="user.userPassword" ></el-input>
          </el-form-item>
          <el-form-item style="margin: 10px 0;text-align: right">
            <el-button type="primary" size="small" autocomplete="off" @click="login">登录</el-button>
            <el-button type="warning" size="small" autocomplete="off" @click="$router.push('/register')">注册</el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>
</template>

<script>
export default {
  name: "Login",
  data(){
    return{
      user: {
        userName: "",
        userPassword: ""
      },
      rules: {
        userName: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
          { min: 3, max: 10, message: '长度在 3 到 10 个字符', trigger: 'blur' }
        ],
        userPassword: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { min: 1, max: 12, message: '长度在 8 到 12 个字符', trigger: 'blur' }
        ],
      }
    }
  },
  methods:{
    login(){
      this.$refs['userForm'].validate((valid) => {
        if (valid) {
          this.request.post("/user/login",  this.user).then( res => {
            if(res.code === '200'){
              if(localStorage.getItem("user") !== null)
                localStorage.removeItem("user")

              let data = Object.assign(res.data, { startTime: new Date().getTime() })
              localStorage.setItem("user",JSON.stringify(data)) // 存储用户信息
              console.log(res.data)
              this.$router.push("/dashboard")
              this.$message.success("登录成功")
            }
            else{
              this.$message.error(res.msg)
            }
          })
        }
      });
    }
  }
}
</script>

<style scoped>
  .wrapper{
    height: 100vh;
    background-image: linear-gradient(to bottom right,#FC466B,#3F5EF8);
    overflow: hidden;
  }
</style>