<template>
  <div class="body">
    <div class="list-books">
      <div class="card-grid">
        <div v-for="book in books" :key="book.id" class="card-item">
          <BookCard :book="book"/>
        </div>
      </div>
    </div>
    <div v-if="store.$state.userRole === 'ADMIN' " class="book-manager m-3" >
      <button class="btn btn-light" @click="openModal">Add Book</button>
      <button class="btn btn-light mt-2" @click="()=>{this.$refs.ModalWindowGenre.openModal()}">Genre</button>
    </div>
  </div>
  <ModalWindow ref="ModalWindow">
    <AddBookModal :genres="genres" :modal-close="closeModal" />
  </ModalWindow>
  <ModalWindow ref="ModalWindowGenre">
    <GenreModal :genres="genres" :modal-close="closeModalGenre" />
  </ModalWindow>
</template>

<script>
import {sendRequest} from "../scripts/request.js";
import BookCard from "../components/BookCard.vue";
import {getGenreList} from "@/scripts/utils.js";
import AddBookModal from "@/components/AddBookModal.vue";
import ModalWindow from "@/components/ModalWindow.vue";
import GenreModal from "@/components/genreModal.vue";
import {userStore} from "@/stores/userStore.js";
export default {
  name: 'HomeV',
  components: {
    GenreModal,
    AddBookModal,
    BookCard,
    ModalWindow
  },
  data() {
    return {
      req: [],
      page: 1,
      countPages: Number,
      books: [],
      genres: [],
    }
  },
  setup() {
    const store = userStore();

    return {
      store
    };
  },
  mounted() {
    this.getBooks();
    this.getGenres();

  },
  methods: {
    openModal() {
      this.$refs.ModalWindow.openModal();
    },
    closeModal() {
      this.$refs.ModalWindow.closeModal();
      this.getBooks();
    },
    closeModalGenre() {
      this.$refs.ModalWindowGenre.closeModal();
      this.getGenres();
    },

    async getBooks() {
      const response = await sendRequest(`/book`, 'GET');
      if (response.status === 200) {
        this.books = await response.json();
      }
    },
    async getGenres() {
      const response = await getGenreList();
      if (response.ok) {
        this.genres = await response.json();
        console.log(this.genres);
      }
    },
  }
}
</script>

<style scoped>
.body {
  display: flex;
}

.search-criteria {
  padding: 1em;
  flex-basis: 24em;
  flex-shrink: 0;
}

.admin-panel {
  padding: 1em;
  flex-basis: 24em;
  flex-shrink: 0;
}

.list-books {
  max-width: calc(100% - 2em);
  flex-grow: 1;
}

.card-grid {
  justify-content: center;
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, max-content));
  gap: 2em;
}

.card-item {
  max-width: 300px;
  /*opacity: 0.5;*/
  margin: 1rem;
  box-shadow: 0px 0px 4px 2px rgba(0, 0, 0, 0.25);
  border-radius: 0.5rem;
}
.book-manager {
  display: flex;
  flex-direction: column;
}


</style>