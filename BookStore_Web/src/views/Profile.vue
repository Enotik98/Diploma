<template>
  <div class="profile-container">
    <div class="profile-body container">
      <div class="personal-data">
        <h2 class="profile-title">Personal Data</h2>
        <form @submit.prevent="updateUserInfo">
          <div class="personal_body">
            <div class="body_column">
              <div class="body_column-item">
                <label>First Name*:</label>
                <input v-model="user.firstName" type="text" class="form-control" placeholder="First Name"
                       required maxlength="256">
              </div>
              <div class="body_column-item">
                <label>Last Name*:</label>
                <input v-model="user.lastName" type="text" class="form-control" placeholder="Last Name" required
                       maxlength="256">
              </div>
              <div class="body_column-item">
                <label>Email:</label>
                <input type="text" class="form-control" :value="user.email" readonly>
              </div>
              <div class="body_column-item">
                <label>Phone Number*:</label>
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
        <h2 class="profile-title">My Orders</h2>
        <div>
          <table class="table table-hover">
            <thead>
            <tr>
              <th scope="col">#</th>
              <th scope="col">Order Date</th>
              <th scope="col">Sale Date</th>
              <th scope="col">Status</th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="order in userOrder" :key="order.id" @click="openWindow(order)">
              <td>{{ order.id }}</td>
              <td>{{ formatDate(order.orderDate) }}</td>
              <td>{{ formatDate(order.saleDate) }}</td>
              <td>{{ order.status }}</td>
            </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
    <ModalWindow ref="ModalWindow">
      <OrderModal :order="chooseOrder" :modal-close="closeModal" :is-user="true"/>
    </ModalWindow>
  </div>
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
.profile-container {
  display: flex;
  justify-content: center;
  padding: 2em;
}

.profile-body {
  display: flex;
  flex-direction: column;
  gap: 4em;
  width: 100%;
  max-width: 1000px;
  background-color: rgba(255, 255, 255, 0.8);
  padding: 2em;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  border-radius: 10px;
}

.profile-title {
  color: var(--blue-opacity);
  font-size: 18pt;
  font-weight: 500;
  margin-bottom: 1em;
}

.personal_body {
  display: flex;
  flex-direction: column;
  gap: 1.5em;
}

.body_column {
  display: flex;
  flex-direction: column;
  gap: 1em;
}

.body_column-item {
  display: flex;
  flex-direction: column;
}

.body_column-item label {
  margin-bottom: 0.5em;
  font-weight: 600;
}

.form-control {
  border-radius: 5px;
  padding: 0.5em;
  border: 1px solid #ced4da;
}

.personal_btn {
  display: flex;
  justify-content: flex-end;
  margin-top: 1em;
}

.table {
  width: 100%;
  margin-bottom: 1em;
  background-color: #fff;
  border-collapse: collapse;
}

.table th,
.table td {
  padding: 0.75em;
  text-align: left;
  border-top: 1px solid #dee2e6;
}

.table-hover tbody tr:hover {
  background-color: rgba(0, 0, 0, 0.075);
}

.table-hover tbody tr {
  cursor: pointer;
}
</style>