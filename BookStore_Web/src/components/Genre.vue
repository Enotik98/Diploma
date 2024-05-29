<template>
  <div class="container d-flex justify-content-center">
    <div class="genre">
      <label class="title">Add Genre</label>
      <div>
        <label>Name</label>
        <input class="form-control" type="text" v-model="name" placeholder="Genre" maxlength="255">
      </div>
      <div class="genre_btn">
        <button class="btn btn-outline-dark" @click="createGenre">Add Genre</button>
      </div>
    </div>
  </div>
</template>

<script>
import {sendRequest} from "@/scripts/request.js";

export default {
  name: "Genre",
  data() {
    return {
      name: ""
    }
  },
  methods: {
    async createGenre() {
      const response = await sendRequest("/book/genre/create", "POST", {name : this.name});
      if (!response) {
        let mess = await response.text();
        // this.$Notiflix.Notify.failure(mess);
        return;
      }
      // this.$Notiflix.Notify.success("Add successful!");
      this.name = '';
    }
  }
}
</script>

<style scoped>

</style>