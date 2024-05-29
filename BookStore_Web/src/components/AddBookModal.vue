<template>
  <div class="book">
    <form class="form-book" @submit.prevent="submitFormBook">
      <label v-if="!editBook" class="title-card">Add Book</label>
      <label v-else class="title-card">Edit Book</label>
      <div class="form-group">
        <label>Image*</label>
        <textarea class="form-control" type="text" v-model="book.path_img" rows="1" placeholder="Image Url" required
                  maxlength="256"/>
      </div>
      <div>
        <label>Title*</label>
        <input class="form-control" type="text" v-model="book.title" placeholder="Title" required maxlength="256">
      </div>
      <div class="form-group">
        <label>Author*</label>
        <input class="form-control" type="text" v-model="book.author" placeholder="Author" maxlength="256">
      </div>
      <div class="form-group">
        <label>Genre*</label>
        <div>

          <VueMultiselect v-model="book.genre"
                          :multiple="true"
                          :options="listGenre"
                          :close-on-select="false"
                          placeholder="Choose genre"
                          label="genre"
                          track-by="id"
          ></VueMultiselect>
        </div>

        <!--        <select class="form-select" v-model="book.genre" multiple required>-->
        <!--          <option value="" selected disabled>Choose genre</option>-->
        <!--          <option v-for="genre in genres" :key="genre.id" :value="genre.id">{{genre.name}}</option>-->
        <!--        </select>-->
      </div>
      <div class="book-input-group">
        <div class="form-group pe-1">
          <label>ISBN*</label>
          <input class="form-control" type="text" v-model="book.isbn" placeholder="ISNB" maxlength="13" required>
        </div>
        <div class="form-group px-1">
          <label>Publication Year*</label>
          <input class="form-control" type="text" v-model="book.pubYear" placeholder="Publication Year" maxlength="4"
                 required>
        </div>
        <div class="form-group ps-1">
          <label>Quantity*</label>
          <input class="form-control" type="text" v-model="book.quantity" placeholder="Quantity" maxlength="3" required>
        </div>
      </div>
      <div class="form-group">
        <label>Price*</label>
        <input class="form-control" type="number" v-model="book.price" placeholder="Price" maxlength="256">
      </div>
      <div class="form-group">
        <label>About*</label>
        <textarea class="form-control" type="text" v-model="book.about" rows="7" placeholder="About" required/>
      </div>
      <div class="button">
        <button v-if="!editBook" type="submit" class="btn btn-outline-dark">Add Book</button>
        <button v-else type="submit" class="btn btn-outline-dark">Save Book</button>
      </div>
    </form>
  </div>
</template>

<script>
import VueMultiselect from 'vue-multiselect';
import {sendRequest} from "@/scripts/request.js";

export default {
  name: "AddBookModal",
  components: {VueMultiselect},
  props: {
    editBook: Object,
    modalClose: Function,
    genres: Array,
  },
  data() {
    return {
      book: {
        path_img: null,
        title: "",
        author: "",
        genre: [],
        isbn: "",
        pubYear: "",
        quantity: "",
        about: "",
        price: 0.00
      },

      listGenre: this.genres
      // genres: optionsGenre.sort((a, b) => a.localeCompare(b)),
    }
  },
  mounted() {
    if (this.editBook != null) {
      this.book = this.editBook
    }
    this.listGenre = this.listGenre.sort((a, b) => a.genre.localeCompare(b.genre))
  },
  methods: {
    async submitFormBook() {
      let errorMessage = null;
      this.book.genre = this.book.genre.map(genre => genre.id);
      if (!this.editBook) {
        const response = await sendRequest("/book/create", "POST", this.book)
        if (!response.ok) {
          errorMessage = await response.text()
        }
      } else {
        const response = await sendRequest("/book", "PUT", this.book)
        if (!response.ok) {
          errorMessage = await response.text()
        }
      }
      !errorMessage ? this.modalClose() : null;
    }
  }
}
</script>
<style src="vue-multiselect/dist/vue-multiselect.css"></style>
<style scoped>

.book {
  width: 40em;
  padding: 0 1em;
  color: #000;
  /*font-family: Inter,serif;*/
  font-style: normal;
  font-weight: 400;
  line-height: normal;

}

.title-card {
  font-size: 16pt;
}

.book-input-group {
  display: flex;
}

.form-group {
  margin: .5em 0;
}

.form-group > label {
  padding-bottom: .4em;
}

.button {
  display: flex;
  justify-content: end;
}

.button button {
  width: 10em;
}
</style>