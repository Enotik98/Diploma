<template>
  <div class="container d-flex justify-content-center ">
    <!--    <div class="back-link" @click="$router.go(-1)"><i class="fa-solid fa-chevron-left"-->
    <!--                                                      style="color: #000000;"></i><span>Back</span></div>-->
    <div id="book_body">
      <div class="product">
        <img src="../assets/car.jpg" class="card-img-left" alt="Book">
        <!--      <img :src="" class="card-img-left" alt="Book">-->
        <div class="product_body">
          <span class="card-title">{{ book.title }}</span>
          <div class="card-info">
            <span class="card-text">Author: {{ book.author }}</span>
            <span class="card-text">Genre: {{ bookGenre }}</span>
            <span class="card-text">Year: {{ book.pub_year }}</span>
            <span class="card-text">ISBN: {{ book.isbn }}</span>
          </div>
          <div class="order-control">
            <label class="form-label mb-0">Quantity</label>
            <div class="input-group">
              <input type="number" min="1" max="15" class="form-control" v-model="order.quantity">
              <button class="btn btn-outline-dark w-25" @click="addToOrder">Add</button>
            </div>
            <div class="btn-group my-2 w-100" role="group" v-if="store.$state.userRole === 'ADMIN' || store.$state.userRole === 'SELLER'">
              <button class="btn btn-outline-dark w-50" @click="openModal">Edit</button>
              <button class="btn btn-outline-danger w-50" v-if="store.$state.userRole === 'ADMIN'">Delete</button>
            </div>
          </div>
          <span class="card-about">About: <br>{{ book.about }}</span>
        </div>
      </div>
    </div>
    <ModalWindow ref="ModalWindow">
      <AddBookModal :genres="genres" :modal-close="closeModal" :edit-book="book"/>
    </ModalWindow>
  </div>
</template>

<script>
import {sendRequest} from "@/scripts/request";
import {userStore} from "@/stores/userStore.js";
import {getGenreList} from "@/scripts/utils.js";
import ModalWindow from "@/components/ModalWindow.vue";
import AddBookModal from "@/components/AddBookModal.vue";
// import Header from "@/components/Header.vue";
// import {calculateDate, getGenreName} from "@/scripts/utils";
// import {mapState} from "vuex";
// import ModalWindow from "@/components/ModalWindow.vue";
// import CreateBook from "@/components/CreateBook.vue";

export default {
  // eslint-disable-next-line vue/multi-word-component-names
  name: "Product",
  components: {AddBookModal, ModalWindow},
  setup() {
    const store = userStore();

    return {
      store
    };
  },
  data() {
    return {
      book: {},
      bookGenre: "",
      // genres: [],
      // selectDays: 7,
      order: {
        bookId: Number,
        quantity: 1,
      },
      isAvailable: false,
      errorMess: null,
      genres: []
    }
  },
  mounted() {
    this.getBook();
    this.getGenres();
  },
  methods: {
    openModal() {
      this.$refs.ModalWindow.openModal();
    },
    closeModal() {
      this.$refs.ModalWindow.closeModal();
      this.getBook();
    },
    async getGenres() {
      const response = await getGenreList();
      if (response.ok) {
        this.genres = await response.json();
      }
    },
    async getBook() {
      try {
        const response = await sendRequest("/book/" + this.$route.params.id, "GET", null);
        if (response.ok) {
          const data = await response.json();
          this.book = data;
          const gen = data.genres;
          this.bookGenre = gen.map(elem => elem.genre).join(', ');
          // this.isAvailable = data['isAvailable'];
          console.log(data)
        }
      } catch (e) {
        console.error(e)
      }
    },
    async addToOrder() {
      if (this.store.$state.isLoggedIn) {
        this.order.bookId = this.$route.params.id;
        const response = await sendRequest("/sale/basket", "POST", this.order);
        if (response.ok) {
          console.log('ok')
        }
      } else {
        alert("You need to login or register");
        this.$router.push("/login");
      }
    }
    // async createOrder() {
    //   if (!this.isLoggedIn) {
    //     this.$Notiflix.Notify.info("To create an order, you need to log in to your account, go to your accoutn.")
    //     this.$router.push('/login')
    //     return;
    //   }
    //   this.order.book_id = this.$route.params.id
    //   this.order.return_date = calculateDate(new Date, this.selectDays);
    //   if (this.order.book_id === "" || this.order.return_date === "") {
    //     return;
    //   }
    //   console.log(this.order)
    //   const response = await sendRequest("/order", "POST", this.order);
    //   if (!response.ok) {
    //     if (response.status === 403) {
    //       this.errorMess = await response.text();
    //       this.$Notiflix.Notify.failure(this.errorMess)
    //     }
    //     return;
    //   }
    //   this.$Notiflix.Notify.success("The book has been added!")
    // },
    // async deleteBook() {
    //   const response = await sendRequest("/book", "DELETE", {id: this.book.id});
    //   if (!response.ok) {
    //     this.errorMess = await response.text();
    //     this.$Notiflix.Notify.failure(this.errorMess);
    //     return
    //   }
    //   this.$Notiflix.Notify.success("Deleted successful!");
    //   this.$router.go(-1);
    // }
  }
}
</script>

<style scoped>

.container {
  gap: 1em;
}

#book_body {
  background-color: rgba(255, 255, 255, 0.8);
  padding: 0 1em;
  height: auto;
  width: 1000px;
}

.back-link {
  /*position: absolute;*/
  /*left: 100;*/
  display: flex;
  height: fit-content;
  align-items: center;
  padding: 1.5em 0;
}

.product {
  padding: 1.5em 0;
  display: grid;
  grid-template-columns: 300px auto;
  gap: 2em;
}

.product > img {
  width: 100%;
  object-fit: cover;
  object-position: center top;
  border-radius: 8px;
  box-shadow: 0 0 4px 0 rgba(0, 0, 0, 0.25);
}

.product_body {
  display: flex;
  flex-direction: column;
  width: 100%;
}

.card-title {
  font-size: 2em;
  margin-bottom: .5em;
}

.card-info {
  display: flex;
  flex-direction: column;
  margin-bottom: 2em;
}

.card-text {
  color: rgba(0, 0, 0, 0.5)
}

.card-about {
  margin-bottom: 2em;
}

.order-control {
  width: 70%;
}

.order-control label {
  font-size: 1.5em;
  margin-bottom: 1em;
}

.order-control > button {
  margin-top: 1em;
  width: 70%;
}

[name=order-duration] {
  margin-bottom: 2em;
}

.warning {
  color: #ff4013;
}

.disabled {
  opacity: .7;
}

.edit-book {
  padding: 1.5em 0;
  display: flex;
  gap: 1em;
}

.edit-book > button {
  height: fit-content;
  width: 5em;
}
</style>