<template>
  <div id="basket">
    <h1>Shopping Basket</h1>
    <div class="list-basket" v-for="elem in basket.saleBooks" :key="elem.book.id">
      <div class="item-basket">
        <div class="item-basket__img"><img src="../assets/car.jpg" alt=""></div>
        <div class="item-basket__info">
          <p>{{elem.book.title}}</p>
          <p>{{elem.book.author}}</p>
          <p>{{elem.book.pub_year}}</p>
        </div>
        <div class="item-basket__quantity">
          <label class="form-label">Quantity</label>
          <input type="number" class="form-control" v-model="elem.quantity" >
        </div>
        <div class="item-basket__price">
          <p>Price</p>
          <p>{{elem.book.price / 100}}</p>
        </div>
        <div class="item-basket__btn">
          <button class="btn btn-outline-danger">Delete</button>
        </div>
      </div>
    </div>
    <div class="order-btn">
      <button class="btn btn-outline-dark" @click="creatOrder">Order</button>
    </div>
  </div>
</template>

<script>
import {sendRequest} from "@/scripts/request.js";
import { saveAs } from 'file-saver';

export default {
  name: "Basket",
  props: {
    modalClose: Function
  },
  data() {
    return {
      basket: {},
      amount: 0
    }
  },
  mounted() {
    this.getBasket();
  },
  methods: {
    async getBasket() {
      const response = await sendRequest("/user/basket", "GET", null);
      if (response.ok) {
        this.basket = await response.json();
        this.amount = this.basket.amount / 100;
        console.log(this.basket);
      }
    },
    async creatOrder() {
      const response = await sendRequest("/user/order", "POST", {"id": this.basket.id});
      if (response.ok) {
        this.modalClose();
      }
    },
    downloadFile() {
      let headers = "";
      let token = localStorage.getItem("Token");
      headers = token? "Bearer " + token : "";
      fetch('http://localhost:8083/api/analitycs/arima-excel', {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': headers
        },
      })
          .then(response => response.blob())
          .then(blob => {
            // Збереження файлу з Blob об'єкту
            saveAs(blob, 'forecast.xlsx');
          })
          .catch(error => {
            console.error('Помилка завантаження файлу:', error);
          });
    }
  }
}
</script>

<style scoped>
#basket {
  width: 1000px;
  height: 80vh;
  position: relative;
  align-items: center;
}
.item-basket {
  display: grid;
  grid-template-columns: repeat(6, 1fr);
  grid-template-rows: 1fr;
  grid-column-gap: 10px;
  grid-row-gap: 1em;
  margin-bottom: 1em;
}

.item-basket__img {
  grid-area: 1/1/2/2;
}

.item-basket__img img{
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.item-basket__info {
  grid-area: 1/2/2/4;
}
.item-basket__info p:nth-last-child(1) {
  margin-bottom: 0;
}

.item-basket__quantity {
  grid-area: 1/4/2/5;
}

.item-basket__price {
  grid-area: 1/5/2/6;

  display: flex;
  flex-direction: column;
  align-items: center;
}

.item-basket__btn {
  grid-area: 1/6/2/7;
  display: flex;
  justify-content: center;
  align-items: center;
}

.order-btn {
  float: right;
  margin-top: 2em;
  margin-right: 4em;
}
.order-btn button {
  width: 100px;
}
</style>