<template>

	<!-- Profile Information Card -->
	<a-card :bordered="false" class="header-solid h-full card-profile-information" :bodyStyle="{paddingTop: 0, paddingBottom: '16px' }" :headStyle="{paddingRight: 0,}">
		<template #title>
			<h6 class="font-semibold m-0">个人信息</h6>
		</template>
		<a-button type="link" slot="extra">
			<svg width="20" height="20" viewBox="0 0 20 20" fill="none" xmlns="http://www.w3.org/2000/svg">
				<path class="fill-muted" d="M13.5858 3.58579C14.3668 2.80474 15.6332 2.80474 16.4142 3.58579C17.1953 4.36683 17.1953 5.63316 16.4142 6.41421L15.6213 7.20711L12.7929 4.37868L13.5858 3.58579Z" fill="#111827"/>
				<path class="fill-muted" d="M11.3787 5.79289L3 14.1716V17H5.82842L14.2071 8.62132L11.3787 5.79289Z" fill="#111827"/>
			</svg>
		</a-button>
		<p class="text-dark">
			您好，这是 Shawn i 的个人主页
		</p>
		<hr class="my-25">
    <a-descriptions :column="1">
      <a-descriptions-item label="昵称">
        <a-input v-model="user.nickname" />
      </a-descriptions-item>
      <a-descriptions-item label="手机号">
        <a-input v-model="user.phone" />
      </a-descriptions-item>
      <a-descriptions-item label="邮箱">
        <a-input v-model="user.email" />
      </a-descriptions-item>
      <a-descriptions-item label="国家">
        <a-input v-model="user.country" />
      </a-descriptions-item>
      <a-descriptions-item label="联系方式">
        <a href="https://github.com/Shawnicsc" class="mx-5 px-5">
          <a-icon type="github" style="color: #3EA1EC;" />
        </a>
        <a href="#" class="mx-5 px-5">
          <a-icon type="wechat" style="color: #344E86" />
        </a>
        <a href="#" class="mx-5 px-5">
          <a-icon type="qq" style="color: #E1306C" />
        </a>
      </a-descriptions-item>
    </a-descriptions>
	</a-card>
	<!-- / Profile Information Card -->

</template>

<script>

	export default ({
		data() {
			return {
        user: {
          nickname: 'BUPT',
          phone: '12138',
          email: 'shawn973@bupt.edu.cn',
          country: 'China',
        }
			}
		},
	})

</script>
