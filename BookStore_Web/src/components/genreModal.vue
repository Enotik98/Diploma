<template>
  <div class="genre">
    <div>
      <label>Genre*</label>
      <input class="form-control" type="text" v-model="genre" placeholder="Title" required maxlength="256">
    </div>
    <div>
      <button class="btn btn-outline-dark" @click="createGenre">Add</button>
    </div>
    <select class="form-select" v-model="genreId" required>
      <option value="" selected disabled>Choose genre</option>
      <option v-for="genre in genres" :key="genre.id" :value="genre.id">{{ genre.name }}</option>
    </select>
    <div>
      <label>Update Genre</label>
      <input class="form-control" type="text" v-model="genre" placeholder="Title" required maxlength="256">
    </div>
    <button class="btn btn-outline-dark" @click="updateGenre">Update</button>
  </div>
</template>

<script>
import {sendRequest} from "@/scripts/request.js";

export default {
  name: "genreModal",
  props: {
    modalClose: Function,
    genres: Array,
  },
  data() {
    return {
      genre: "",
      update: "",
      genreId: 0
    }
  },
  methods: {
    async createGenre() {
      const response = await sendRequest("/book/genre/create", "POST", this.genre);
      if (response.ok) {
        this.genre = "";
        this.modalClose();
      }
    },
    async updateGenre() {
      const response = await sendRequest("/book/genre", "PUT", {
        id: this.genreId,
        genre: this.update
      })
      if (response.ok) {
        this.update = "";
        this.modalClose();
      }
    }
  }
}
</script>

<style scoped>

</style>