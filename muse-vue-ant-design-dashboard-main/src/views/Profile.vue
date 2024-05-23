<!--
	This is the user profile page, it uses the dashboard layout in:
	"./layouts/Dashboard.vue" .
 -->

<template>
	<div>

		<!-- Header Background Image -->
		<div class="profile-nav-bg" style="background-image: url('images/bg-profile.jpg')"></div>
		<!-- / Header Background Image -->

		<!-- User Profile Card -->
		<a-card :bordered="false" class="card-profile-head" :bodyStyle="{padding: 0,}">
			<template #title>
				<a-row type="flex" align="middle">
					<a-col :span="24" :md="12" class="col-info">
						<a-avatar :size="74" shape="square" src="images/face-2.jpg" />
						<div class="avatar-info">
							<h4 class="font-semibold m-0">{{ generateUserName() }}</h4>
						</div>
					</a-col>
				</a-row>
			</template>
		</a-card>
		<!-- User Profile Card -->

		<a-row type="flex" :gutter="24">

			<!-- Profile Information Column -->
			<a-col :span="24" :md="24" class="mb-24">

				<!-- Profile Information Card -->
				<CardProfileInformation></CardProfileInformation>
				<!-- / Profile Information Card -->

			</a-col>
			<!-- / Profile Information Column -->

		</a-row>
	</div>
</template>

<script>

	import CardPlatformSettings from "../components/Cards/CardPlatformSettings"
	import CardProfileInformation from "../components/Cards/CardProfileInformation"
	import CardConversations from "../components/Cards/CardConversations"
	import CardProject from "../components/Cards/CardProject"
  import {generate} from "@vue/compiler-core";

	// Project cards data
	const files = [] ;

	export default ({
		components: {
			CardPlatformSettings,
			CardProfileInformation,
			CardConversations,
			CardProject,
		},
		data() {
      const user = JSON.parse(localStorage.getItem("user"));
			return {
        user: user,
				// Active button for the "User Profile" card's radio button group.
				profileHeaderBtns: 'overview',

			}
		},
    methods: {
      generateUserName() {
        return this.user.userName
      }
    }
	})

</script>

<style lang="scss">
</style>
