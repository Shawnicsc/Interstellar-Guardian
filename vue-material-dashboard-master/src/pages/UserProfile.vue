<template>
  <div class="content">
    <div class="md-layout" style="display: flex; justify-content: center;">
      <div class="md-layout-item md-medium-size-100 md-size-50">
        <user-card> </user-card>
      </div>

    </div>
  </div>
</template>

<script>
import { UserCard } from "@/pages";

export default {
  components: {
    UserCard,
  },
};
</script>
