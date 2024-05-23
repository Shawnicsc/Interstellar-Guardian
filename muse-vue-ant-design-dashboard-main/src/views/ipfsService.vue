<!--
	This is the tables page, it uses the dashboard layout in:
	"./layouts/Dashboard.vue" .
 -->

<template>
	<div>
		<!-- Projects Table -->
		<a-row :gutter="24" type="flex">

			<!-- Projects Table Column -->
			<a-col :span="24" class="mb-24">

				<!-- Projects Table Column -->
				<CardProjectTable2> </CardProjectTable2>
				<!-- / Projects Table Column -->

			</a-col>
			<!-- / Projects Table Column -->

		</a-row>
		<!-- / Projects Table -->

	</div>
</template>

<script>
	// "Projects" table component.
	import CardProjectTable2 from '../components/Cards/CardProjectTable2' ;




	export default ({
		components: {
			CardProjectTable2,
		},
		data() {
			return {
			}
		},
	})

</script>

<style lang="scss">
</style>
