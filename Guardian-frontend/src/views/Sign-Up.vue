<!--
	This is the sign up page, it uses the dashboard layout in:
	"./layouts/Default.vue" .
 -->

<template>
	<div>

		<!-- Sign Up Image And Headings -->
		<div class="sign-up-header" style="background-image: url('images/bg-signup.jpg')">
			<div class="content">
				<h1 class="mb-5">注册</h1>
			</div>
		</div>
		<!-- / Sign Up Image And Headings -->

		<!-- Sign Up Form -->
		<a-card :bordered="false" class="card-signup header-solid h-full" :bodyStyle="{paddingTop: 0}">
			<template #title>
				<h5 class="font-semibold text-center">注册您的账号</h5>
			</template>

			<a-form
				id="user"
				:form="form"
				class="login-form"
				@submit="handleSubmit"
			>
				<a-form-item class="mb-10">
					<a-input
						v-decorator="[
						'userName',
						{ rules: [{ required: true, message: '请输入用户名!' }] },
						]"
						placeholder="用户名"
					>
					</a-input>
				</a-form-item>

				<a-form-item class="mb-5">
					<a-input
						v-decorator="[
						'userPassword',
						{ rules: [{ required: true, message: '请输入密码!' }] },
						]"
						type="password"
						placeholder="密码"
					>
					</a-input>
				</a-form-item>
        <a-form-item class="mb-10">
          <a-input
              v-decorator="[
						'confirmPassword',
						{ rules: [{ required: true, message: '请再次输入密码!' }] },
						]"
              type="password"
              placeholder="再次输入密码"
          >
          </a-input>
        </a-form-item>
				<a-form-item class="mb-10">
					<a-checkbox
						v-decorator="[
						'remember',
						{
							valuePropName: 'checked',
							initialValue: true,
						},
						]"
					>
						我已阅读并同意 <a href="#" class="font-bold text-dark">隐私条款</a>
					</a-checkbox>
				</a-form-item>
				<a-form-item>
					<a-button type="primary" block html-type="submit" class="login-form-button">
						注册
					</a-button>
				</a-form-item>
			</a-form>
			<p class="font-semibold text-muted text-center">已经有账号? <router-link to="/sign-in" class="font-bold text-dark">登录</router-link></p>
		</a-card>
		<!-- / Sign Up Form -->

	</div>
</template>

<script>

	import {message} from "ant-design-vue";

  export default ({
		data() {
			return {
			}
		},
		beforeCreate() {
			// Creates the form and adds to it component's "form" property.
			this.form = this.$form.createForm(this, { name: 'user' });
		},
		methods: {
			// Handles input validation after submission.
			handleSubmit(e) {
				e.preventDefault();
				this.form.validateFields((err, values) => {
					if ( !err ) {
            if(values.userPassword !== values.confirmPassword){
              this.$message.warning('两次密码输入不一致,请重试');
            }
            else{
              this.request.post("/user/register",  values ).then( res => {
                if(res.code === '200'){
                  this.$router.push("/sign-in")
                  this.$message.success("注册成功")
                }
                else{
                  this.$message.error(res.msg)
                }
              })
            }

					}
				});
			},
		},
	})

</script>

<style lang="scss">
</style>
