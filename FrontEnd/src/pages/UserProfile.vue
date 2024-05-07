<template>
  <div class="content">
    <div class="md-layout">
      <div class="md-layout-item md-medium-size-100 md-size-66">
        <md-card>
          <md-card-header data-background-color="green">
            <h4 class="title">HashCode Table</h4>
            <p class="category">Show and Share What you have</p>
          </md-card-header>
          <md-card-content>
            <simple-table table-header-color="green"></simple-table>
          </md-card-content>
        </md-card>
      </div>
      <div class="md-layout-item md-medium-size-100 md-size-33">
          <user-card> </user-card>
      </div>
    </div>
  </div>
</template>

<script>
import { UserCard } from "@/pages";
import { SimpleTable } from "@/components";
export default {
  components: {
    UserCard,
    SimpleTable,
  },
};
</script>
<style>
.centered-card {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%; /* Ensure the card is centered vertically */
}
</style>