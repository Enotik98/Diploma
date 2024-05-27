<template>
  <div id="order-box">
    <h1>Order {{ order.id }}</h1>
    <div>
      <h3>User</h3>
      <p>Email: {{order.user.email}}</p>
      <p>First Name: {{order.user.firstName}}</p>
      <p>Last Name: {{order.user.lastName}}</p>
      <p>Phone: {{order.user.phone}}</p>
    </div>
    <div class="list-order" v-for="elem in order.saleBooks" :key="elem.book.id">
      <div class="item-order">
        <div class="item-order__img"><img src="../assets/car.jpg" alt=""></div>
        <div class="item-order__info">
          <p>{{ elem.book.title }}</p>
          <p>{{ elem.book.author }}</p>
          <p>{{ elem.book.pub_year }}</p>
        </div>
        <div class="item-order__quantity">
          <label class="form-label">Quantity</label>
          <input type="number" class="form-control" v-model="elem.quantity">
        </div>
        <div class="item-order__price">
          <p>Price</p>
          <p>{{ elem.book.price / 100 }}</p>
        </div>
        <div class="item-order__btn">
          <button class="btn btn-outline-danger">Delete</button>
        </div>
      </div>
    </div>
    <div class="order-btn">
      <button class="btn btn-outline-dark" @click="closeOrder">Finish Order</button>
    </div>
  </div>
</template>

<script>
import {sendRequest} from "@/scripts/request.js";

export default {
  name: "OrderModal",
  props: {
    order: Object,
    modalClose: Function
  },
  data() {
    return {
      saleOrder: {}
    }
  },
  methods: {
    async closeOrder() {
      const response = await sendRequest("/sale/sale_order", "POST", {"id": this.order.id});
      if (response.ok) {
        this.modalClose();
      }
    }
  }
}
</script>

<style scoped>
#order-box {
  width: 1000px;
  height: 80vh;
  position: relative;
  align-items: center;

}
.item-order {
  display: grid;
  grid-template-columns: repeat(6, 1fr);
  grid-template-rows: 1fr;
  grid-column-gap: 10px;
  grid-row-gap: 1em;
  margin-bottom: 1em;
}

.item-order__img {
  grid-area: 1/1/2/2;
}

.item-order__img img{
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.item-order__info {
  grid-area: 1/2/2/4;
}
.item-order__info p:nth-last-child(1) {
  margin-bottom: 0;
}

.item-order__quantity {
  grid-area: 1/4/2/5;
}

.item-order__price {
  grid-area: 1/5/2/6;

  display: flex;
  flex-direction: column;
  align-items: center;
}

.item-order__btn {
  grid-area: 1/6/2/7;
  display: flex;
  justify-content: center;
  align-items: center;
}
</style>