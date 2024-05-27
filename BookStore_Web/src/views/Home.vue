<template>
  <div class="body">
    <div class="list-books">
      <div class="card-grid">
        <div v-for="book in books" :key="book.id" class="card-item">
          <BookCard :book="book" :genre="'gen'"/>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import {sendRequest} from "../scripts/request.js";
import BookCard from "../components/BookCard.vue";

export default {
  name: 'HomeV',
  components: {
    BookCard
  },
  data() {
    return {
      req: [],
      page: 1,
      countPages: Number,
      books: []
    }
  },
  mounted() {
    this.getBooks()
  },
  methods: {
    async getBooks() {
      const response = await sendRequest(`/book`, 'GET');
      if (response.status === 200) {
        this.books = await response.json();
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
  max-width: calc(100% - 48em);
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


</style>