<template>
  <div class="container d-flex mt-4">
    <div class="w-100">
      <div>
        <span class="title">Orders</span>
        <table class="table table-hover">
          <thead>
          <tr>
            <th>#</th>
            <th>Email</th>
            <th>Order Date</th>
            <th>Status</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="order in orders" :key="order.id" @click="openWindow(order)">
            <th>{{ order.id }}</th>
            <th>{{ order.user.email }}</th>
            <th> {{formatDate(new Date(order.orderDate))}}</th>
            <th>{{ order.status }}</th>
          </tr>
          </tbody>
          <!--          <tbody>-->
          <!--          <tr>-->
          <!--            <td class="td-id"><input type="text" v-model="filterOrder.id" placeholder="#" class="form-control"></td>-->
          <!--            <td><input type="text" v-model="filterOrder.email" placeholder="Email" class="form-control"></td>-->
          <!--            <td><input type="text" v-model="filterOrder.title" placeholder="Title" class="form-control"></td>-->
          <!--            <td colspan="2">-->
          <!--              <select v-model="filterOrder.status" class="form-select">-->
          <!--                <option value="">Choose Status</option>-->
          <!--                <option value="AWAITING">AWAITING</option>-->
          <!--                <option value="CHECKOUT">CHECKOUT</option>-->
          <!--                <option value="RETURNED">RETURNED</option>-->
          <!--                <option value="LOST">LOST</option>-->
          <!--              </select></td>-->
          <!--            <td colspan="2">-->
          <!--              <button class="btn btn-outline-dark " @click="filterOrder = clearFilter(filterOrder)">Clear Filter-->
          <!--              </button>-->
          <!--            </td>-->
          <!--          </tr>-->
          <!--          <tr v-for="order in filterOrders" :key="order.id" @click="goToOrderPage(order.id)"-->
          <!--              :class="{'table-warning' : checkLate(order)}">-->
          <!--            <td>{{ order.id }}</td>-->
          <!--            <td>{{ order.user.email }}</td>-->
          <!--            <td>{{ order.book.title }}</td>-->
          <!--            <td>{{ formatDate(order.orderDate) }}</td>-->
          <!--            <td>{{ formatDate(order.return_date) }}</td>-->
          <!--            <td>{{ order.status }}</td>-->
          <!--            <td>-->
          <!--              <div v-if="order.status === 'RETURNED' ">-->
          <!--                <i v-if="order.returnedLate" class="fa-regular fa-calendar-xmark"-->
          <!--                   style="color: #ff4013; font-size: 16pt"></i>-->
          <!--                <i v-else class="fa-regular fa-calendar-check" style="color: #96d35f; font-size: 16pt"></i>-->
          <!--              </div>-->
          <!--            </td>-->
          <!--          </tr>-->
          <!--          </tbody>-->
        </table>
      </div>
    </div>

  </div>
  <ModalWindow ref="ModalWindow">
    <OrderModal :order="chooseOrder" :modal-close="()=>{this.$refs.ModalWindow.closeModal()}" />
  </ModalWindow>
  <!--  <ModalWindow ref="ModalWindow">-->
  <!--    <ExtensionRequestModal :extension-request="chooseRequest" :close-modal-window="()=>{this.$refs.ModalWindow.closeModal()}" :update-list-request="getExtensionRequests"/>-->
  <!--  </ModalWindow>-->
</template>

<script>
import {sendRequest} from "@/scripts/request";
import {formatDate} from "../scripts/utils.js";
import ModalWindow from "@/components/ModalWindow.vue";
import OrderModal from "@/components/OrderModal.vue";


// import ModalWindow from "@/components/ModalWindow.vue";
// import ExtensionRequestModal from "@/components/ExtensionRequestModal.vue";

export default {
  // eslint-disable-next-line vue/multi-word-component-names
  name: "Orders",
  components: {OrderModal, ModalWindow},
  // components: {ExtensionRequestModal, ModalWindow},
  data() {
    return {
      orders: [],
      extensionRequests: [],
      chooseOrder: {},
      filterOrder: {
        id: '',
        email: "",
        title: "",
        status: "",
      },
      showRequests: false,
      showPending: true,
    }
  },
  mounted() {
    this.getOrders();
  },

  methods: {
    formatDate,
    clearFilter(obj) {
      for (let key in obj) {
        obj[key] = "";
      }
      return obj;
    },
    openWindow(order) {
      this.chooseOrder = order;
      this.$refs.ModalWindow.openModal();
    },
    async getOrders() {
      try {
        const response = await sendRequest("/sale/orders", "GET", null);
        if (response.ok) {
          this.orders = await response.json();
          console.log(this.orders)
        }
      } catch (e) {
        console.error(e)
      }
    },

  }
}
</script>

<style scoped>
.panel {
  width: 10em;
}

.title {
  color: var(--blue-opacity);
  font-size: 16pt;
  font-style: normal;
  font-weight: 500;
  line-height: normal;
}

.td-id {
  width: 6em;
}


/*:style="{backgroundColor: checkLate(order) ? '#FFA500' : 'inherit'}*/
</style>