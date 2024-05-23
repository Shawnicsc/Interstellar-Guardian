<template>
    <div class="wrapper">
      <div style="margin: 100px auto;background-color: #fff;width: 350px;padding: 20px;border-radius: 10px">
        <div style="margin: 20px 0 ;text-align: center;font-size: 24px;">注册</div>
        <el-form :model="user" :rules="rules" ref="userForm">
          <el-form-item prop="userName">
            <el-input placeholder="请输入用户名" size="medium" style="margin: 10px 0;" prefix-icon="el-icon-user" v-model="user.userName" ></el-input>
          </el-form-item>
          <el-form-item prop="userPassword">
            <el-input placeholder="请输入密码" size="medium" style="margin: 10px 0;" prefix-icon="el-icon-lock" show-password v-model="user.userPassword" ></el-input>
          </el-form-item>
          <el-form-item style="margin: 10px 0;text-align: right">
            <el-button type="primary" size="small" autocomplete="off" @click="login">确定</el-button>
            <el-button type="warning" size="small" autocomplete="off" @click="$router.push('/login')">返回登录</el-button>
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
        userPassword: "",
        confirmPassword: ""
      },
      rules: {
        userName: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
          { min: 3, max: 10, message: '长度在 3 到 10 个字符', trigger: 'blur' }
        ],
        userPassword: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { min: 1, max: 12, message: '长度在 1 到 12 个字符', trigger: 'blur' }
        ],

      }
    }
  },
  methods:{
    login(){
      this.$refs['userForm'].validate((valid) => {
        if (valid) {
          this.request.post("/user/register",  this.user ).then( res => {
            if(res.code === '200'){
              this.$router.push("/login")
              this.$message.success("注册成功")
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