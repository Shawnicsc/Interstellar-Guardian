<template>
  <md-card class="md-card-profile">
    <div class="md-card-avatar">
      <img class="img" :src="cardUserImage" />
    </div>

    <md-card-content>
      <h6 class="category text-gray">CEO / Co-Founder</h6>
      <h4 class="card-title">Shawn i</h4>
      <p>
        UserId: {{ generateUserId() }}
      </p>
      <a href="https://github.com/Shawnicsc">
      <md-button class="md-round md-success ">
          Follow
      </md-button>
      </a>
    </md-card-content>
  </md-card>
</template>
<script>
export default {
  name: "user-card",
  props: {
    cardUserImage: {
      type: String,
      default: require("@/assets/img/faces/marc.jpg"),
    },
  },
  data() {
    const user = JSON.parse(localStorage.getItem("user"));
    return {
      user: user,
      userId : ''
    };
  },
  methods: {
    generateUserId(){
      return this.user.id
    }
  }
};
</script>
<style></style>
