<template>
  <md-toolbar md-elevation="0" class="md-transparent">
    <div class="md-toolbar-row">
      <div class="md-toolbar-section-start">
        <h3 class="md-title">{{ $route.name }}</h3>
      </div>
      <div class="md-toolbar-section-end">
        <div class="md-collapse">
          <md-list>
            <md-list-item href="#/user">
              <i class="material-icons">person</i>
              <p class="hidden-lg hidden-md">Profile</p>
            </md-list-item>
            <md-list-item @click="logout">
              <i class="material-icons">logout</i>
              <p class="hidden-lg hidden-md">Profile</p>
            </md-list-item>
          </md-list>
        </div>
      </div>
    </div>
  </md-toolbar>
</template>

<script>

export default {
  methods:{

    logout(){
      console.log("登出")
      localStorage.removeItem("user");
      this.$router.push('/login')
      this.$message.success("登出成功")
    }
  }
};
</script>

<style lang="css"></style>
