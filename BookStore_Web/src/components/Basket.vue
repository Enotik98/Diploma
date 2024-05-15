<template>
  <div id="basket">
    <h1>Shopping Basket</h1>
    <div class="list-basket">
      <div class="item-basket">
        <div class="item-basket__img"><img src="../assets/car.jpg" alt=""></div>
        <div class="item-basket__info">
          <p>Book</p>
          <p>Author</p>
          <p>year</p>
        </div>
        <div class="item-basket__quantity">
          <label class="form-label">Quantity</label>
          <input type="number" class="form-control">
        </div>
        <div class="item-basket__price">
          <p>Price</p>
          <p>300</p>
        </div>
        <div class="item-basket__btn">
          <button class="btn btn-outline-danger">Delete</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import {sendRequest} from "@/scripts/request.js";

export default {
  name: "Basket",
  props: {
    modalClose: Function
  },
  data() {
    return {
      basket: {}
    }
  },
  mounted() {
    this.getBasket();
  },
  methods: {
    async getBasket() {
      const response = await sendRequest("/order/user/basket", "GET", null);
      if (response.ok) {
        this.basket = await response.json();
        console.log(this.basket);
      }
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
  grid-row-gap: 0px;
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

.item-basket__quantity {
  grid-area: 1/4/2/5;
}

.item-basket__price {
  grid-area: 1/5/2/6;

  display: flex;
  flex-direction: column;
  justify-content: space-between;
}
.item-basket__price {

}

.item-basket__btn {
  grid-area: 1/6/2/7;
  display: flex;
  justify-content: center;
  align-items: center;
}
</style>