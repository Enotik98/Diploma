<template>
  <div class="container" id="create-sale">
    <div class="form-group">
      <label for="search">Search Book by Title or ISBN</label>
      <input
          type="text"
          class="form-control"
          id="search"
          v-model="searchQuery"
          @keyup.enter="searchBooks"
          @blur="searchBooks"
          placeholder="Enter book title or ISBN"
      />
    </div>
    <div v-if="searchResults.length" class="search-results">
      <h4>Search Results</h4>
      <ul class="list-group">
        <li class="list-group-item" v-for="book in searchResults" :key="book.id">
          <div class="d-flex align-items-center">
            <img src="../assets/car.jpg" alt="Book cover" class="img-thumbnail" >

<!--            <img :src="book.image" alt="Book cover" class="img-thumbnail" width="50" height="75">-->
            <div class="ml-3">
              <h5 class="mb-1">{{ book.title }}</h5>
              <small>ISBN: {{ book.isbn }} | {{ book.author }}</small>
              <p class="mb-1">${{ (book.price/100).toFixed(2) }}</p>
              <button @click="addBookToSale(book)" class="btn btn-primary btn-sm">Add to Sale</button>
            </div>
          </div>
        </li>
      </ul>
    </div>
    <div v-if="selectedBooks.length" class="selected-books mt-4">
      <h4>Selected Books</h4>
      <ul class="list-group">
        <li class="list-group-item" v-for="(book, index) in selectedBooks" :key="book.id">
          <div class="d-flex align-items-center">
            <img src="../assets/car.jpg" alt="Book cover" class="img-thumbnail" >
<!--            <img :src="book.image" alt="Book cover" class="img-thumbnail" width="50" height="75">-->
            <div class="ml-3 w-100">
              <div class="d-flex justify-content-between">
                <div>
                  <h5 class="mb-1">{{ book.title }}</h5>
                  <small>ISBN: {{ book.isbn }} | {{ book.author }}</small>
                </div>
                <div>
                  <p class="mb-1">${{ (book.price/100).toFixed(2) }}</p>
                  <input type="number" v-model.number="book.quantity" min="1" class="form-control form-control-sm w-auto" placeholder="Qty">
                </div>
              </div>
            </div>
            <button @click="removeBookFromSale(index)" class="btn btn-danger btn-sm ml-3">Remove</button>
          </div>
        </li>
      </ul>
      <div class="text-right mt-3">
        <button @click="confirmSale" class="btn btn-success">Confirm Sale</button>
      </div>
    </div>
  </div>
</template>

<script>
import {sendRequest} from "@/scripts/request.js";

export default {
  name: "CreateSale",
  data() {
    return {
      searchQuery: "",
      searchResults: [],
      selectedBooks: [],
    };
  },
  methods: {
    async searchBooks() {
      if (!this.searchQuery) return;
      try {
        const response = await sendRequest(`/book/search?query=${this.searchQuery}`, "GET");
        if (response.ok) {
          this.searchResults = await response.json();
        } else {
          console.log("Error searching books");
        }
      } catch (e) {
        console.log(e);
      }
    },
    addBookToSale(book) {
      if (!this.selectedBooks.find(b => b.id === book.id)) {
        book.quantity = 1;
        this.selectedBooks.push(book);
      }
    },
    async confirmSale() {
      // Реалізуйте логіку для підтвердження продажу
      const order = this.selectedBooks.map(book => ({
        bookId: book.id,
        quantity: book.quantity
      }));
      console.log("Sale confirmed", this.selectedBooks);
      const response = await sendRequest("/sale/sale_shop", "POST", order);
      if (response.ok) {
        this.selectedBooks = [];
        this.searchQuery = "";
        this.searchResults = [];
        this.$router.push('/orders');
      }
    },
    removeBookFromSale(index) {
      this.selectedBooks.splice(index, 1);
    }
  }
}
</script>

<style scoped>
#create-sale {
  background-color: rgba(255, 255, 255, 0.8);
  padding: 2em 1em;
}
.search-results, .selected-books {
  margin-top: 1em;
}
.img-thumbnail {
  width: 80px;
  height: 100px;
  object-fit: cover;
  margin-right: 10px;
}
</style>