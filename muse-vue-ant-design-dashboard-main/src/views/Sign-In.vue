<!--
	This is the sign in page, it uses the dashboard layout in:
	"./layouts/Default.vue" .
 -->

<template>
	<div class="sign-in" >

		<a-row type="flex" :gutter="[24,24]" justify="space-around" align="middle">

			<!-- Sign In Form Column -->
			<a-col :span="24" :md="16" :lg="{span: 12, offset: 0}" :xl="{span: 6, offset: 2}" class="col-form">
				<h1 class="mb-15">用户登录</h1>
				<!-- Sign In Form -->
				<a-form
					id="user"
					:form="form"
					class="login-form"
					@submit="handleSubmit"
					:hideRequiredMark="true"
				>
					<a-form-item class="mb-10" label="用户名" :colon="false">
						<a-input
						v-decorator="[
						'userName',
						{ rules: [{ required: true, message: '请输入你的用户名!' }] },
						]" placeholder="用户名" />
					</a-form-item>
					<a-form-item class="mb-5" label="密码" :colon="false">
						<a-input
						v-decorator="[
						'userPassword',
						{ rules: [{ required: true, message: '请输入你的密码!' }] },
						]" type="password" placeholder="密码" />
					</a-form-item>
					<a-form-item class="mb-10">
    					<a-switch v-model="rememberMe" /> Remember Me
					</a-form-item>
					<a-form-item>
						<a-button type="primary" block html-type="submit" class="login-form-button">
							登录
						</a-button>
					</a-form-item>
				</a-form>
				<!-- / Sign In Form -->

				<p class="font-semibold text-muted">还没有账号? <router-link to="/sign-up" class="font-bold text-dark">注册</router-link></p>
			</a-col>
			<!-- / Sign In Form Column -->

			<!-- Sign In Image Column -->
			<a-col :span="24" :md="12" :lg="12" :xl="12" class="col-img">
        <div class="logo">
          <img src="images/bupt.jpg" alt="">
        </div>

			</a-col>
			<!-- Sign In Image Column -->

		</a-row>

	</div>
</template>

<script>
  export default ({
		data() {
			return {
				// Binded model property for "Sign In Form" switch button for "Remember Me" .
				rememberMe: true,
			}
		},
		beforeCreate() {
			// Creates the form and adds to it component's "form" property.
			this.form = this.$form.createForm(this, { name: 'user' });
		},
		methods: {
			handleSubmit(e) {
				e.preventDefault();
				this.form.validateFields((err, values) => {
					if ( !err ) {
            this.request.post("/user/login",  values).then( res => {
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
			},
		},
	})

</script>

<style lang="scss">
	body {
		background-color: #ffffff;
	}

  .logo {
    height: 400px;
    width: 400px;
    padding: 12px;
    margin-left: 150px;
  }

  .col-form {
    margin-left: 250px;
  }

</style>
