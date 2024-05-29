<template>
  <div id="order-box">
    <h1>Order #{{ order.id }}</h1>
    <div class="user-info">
      <h3>User Information</h3>
      <p><strong>Email:</strong> {{ order.user.email }}</p>
      <p><strong>First Name:</strong> {{ order.user.firstName }}</p>
      <p><strong>Last Name:</strong> {{ order.user.lastName }}</p>
      <p><strong>Phone:</strong> {{ order.user.phone }}</p>
    </div>
    <div class="list-order">
      <div class="item-order" v-for="elem in order.saleBooks" :key="elem.book.id">
        <div class="item-order__img">
          <img src="../assets/car.jpg" alt="">
        </div>
        <div class="item-order__info">
          <p><strong>Title:</strong> {{ elem.book.title }}</p>
          <p><strong>Author:</strong> {{ elem.book.author }}</p>
          <p><strong>Published Year:</strong> {{ elem.book.pub_year }}</p>
        </div>
        <div class="item-order__quantity">
          <label class="form-label">Quantity</label>
          <input type="number" class="form-control" v-model="elem.quantity" :disabled="isUser">
        </div>
        <div class="item-order__price">
          <p><strong>Price:</strong></p>
          <p>{{ (elem.book.price / 100).toFixed(2) }}</p>
        </div>
        <div class="item-order__btn" v-if="!isUser">
          <button class="btn btn-outline-danger">Delete</button>
        </div>
      </div>
    </div>
    <div class="order-summary">
      <p><strong>Total amount:</strong> {{ (order.amount / 100).toFixed(2) }}</p>
    </div>
    <div class="order-btn" v-if="!isUser">
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
    modalClose: Function,
    isUser: Boolean,
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
  /*width: 100%;*/
  width: 1000px;
  /*margin: auto;*/
  padding: 20px;
}

.user-info {
  margin-bottom: 20px;
}

.user-info h3 {
  margin-bottom: 10px;
}

.user-info p {
  margin: 5px 0;
}

.list-order {
  display: flex;
  flex-direction: column;
  gap: 15px;
  margin-bottom: 20px;
}

.item-order {
  display: grid;
  grid-template-columns: 100px 1fr 150px 100px 80px;
  gap: 10px;
  align-items: center;
  padding: 10px;
  background-color: #fff;
  border-radius: 5px;
  box-shadow: 0 0 5px rgba(0, 0, 0, 0.1);
}

.item-order__img img {
  width: 100%;
  height: 100px;
  object-fit: cover;
  border-radius: 5px;
}

.item-order__info p,
.item-order__price p {
  margin: 0;
}

.item-order__quantity input {
  width: 80px;
}

.item-order__btn button {
  width: 100%;
}

.order-summary {
  text-align: right;
  margin-bottom: 20px;
}
.order-btn {
  display: flex;
  justify-content: flex-end;
}

.order-btn button {
  width: 150px;
}
</style>