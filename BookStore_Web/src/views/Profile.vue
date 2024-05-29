<template>
  <div class="">
    <div class="body container">
      <div class="profile-body">
        <div class="personal-data">
          <label class="profile-title">Personal Data</label>
          <form @submit.prevent="updateUserInfo">
            <div class="personal_body">
              <div class="body_column">
                <div class="body_column-item">
                  <label class="">First Name*:</label>
                  <input v-model="user.firstName" type="text" class="form-control" placeholder="First Name"
                         required maxlength="256">
                </div>
                <div class="body_column-item">
                  <label class="">Last Name*:</label>
                  <input v-model="user.lastName" type="text" class="form-control" placeholder="Last Name" required
                         maxlength="256">
                </div>
                <div class="body_column-item">
                  <label class="">Email:</label>
                  <input type="text" class="form-control" :value="user.email" readonly>
                </div>
                <div class="body_column-item">
                  <label class="">Phone Number*:</label>
                  <input v-model="user.phone" type="text" class="form-control" placeholder="0981112233"
                         maxlength="10" required>
                </div>
              </div>
            </div>
            <div class="personal_btn">
              <button class="btn btn-outline-primary" type="submit">Save</button>
            </div>
          </form>
        </div>
        <div class="personal-orders">
          <label class="profile-title">My Orders</label>
          <div>
            <table class="table table-hover">
              <thead>
              <tr>
                <th scope="col">#</th>
                <th scope="col">Order date</th>
                <th scope="col">Sale date</th>
                <th scope="col">Status</th>
              </tr>
              </thead>
              <tbody>
              <tr v-for="order in userOrder" :key="order.id"  @click="openWindow(order)">
                <td>{{ order.id }}</td>
                <td>{{formatDate(order.orderDate)}}</td>
                <td>{{formatDate(order.saleDate)}}</td>
                <td>{{order.status}}</td>
              </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  </div>
  <ModalWindow ref="ModalWindow">
    <OrderModal :order="chooseOrder" :modal-close="()=>{this.$refs.ModalWindow.closeModal()}" />
  </ModalWindow>
</template>

<script>
import {sendRequest} from "@/scripts/request.js";
import {formatDate} from "../scripts/utils.js";
import ModalWindow from "@/components/ModalWindow.vue";
import OrderModal from "@/components/OrderModal.vue";


export default {
  name: "Profile",
  components: {ModalWindow, OrderModal},
  data() {
    return {
      user: {},
      userOrder: {},
      chooseOrder: {}
    }
  },
  mounted() {
    this.getUserInfo();
    this.getUserOrders();
  },
  methods: {
    openWindow(order) {
      this.chooseOrder = order;
      this.$refs.ModalWindow.openModal();
    },
    formatDate,
    async getUserInfo() {
      try {
        const response = await sendRequest("/user", "GET", null);
        if (response.ok) {
          this.user = await response.json();
        }
      } catch (e) {
        console.error(e)
      }
    },
    async updateUserInfo() {
      try {
        const response = await sendRequest("/user", "PUT", this.user);
        if (response.ok) {
          await this.getUserInfo();
        }
      } catch (e) {
        console.error(e)
      }
    },
    async getUserOrders() {
      try {
        const response = await sendRequest("/user/orders", "GET", null);
        if (response.ok) {
          this.userOrder = await response.json();
        }
      } catch (e) {
        console.error(e)
      }
    }
  }
}
</script>

<style scoped>
.body {
  display: flex;
  background-color: rgba(255, 255, 255, 0.8);
  padding: 0 1em;
  height: auto;
  width: 1000px;
}

.profile-body {
  display: flex;
  flex-direction: column;
  gap: 4em;
  width: 100%;
  padding: 2em;
}

.profile-body > div {
  display: flex;
  flex-direction: column;
  gap: 1.5em;
}

.profile-title {
  color: var(--blue-opacity);
  font-size: 16pt;
  font-style: normal;
  font-weight: 500;
  line-height: normal;
}
</style>