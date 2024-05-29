<template>
  <div class="container d-flex">
    <div class="w-100" id="user-list">
      <div class="">
        <p class="title">Orders</p>
        <table class="table table-hover">
          <thead>
          <tr>
            <th scope="col">#</th>
            <th scope="col">Email</th>
            <th scope="col">First Name</th>
            <th scope="col">Last Name</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="user in users" :key="user.id" @click="openWindowUser(user)">
            <th>{{ user.id }}</th>
            <th>{{ user.email }}</th>
            <th> {{ user.firstName }}</th>
            <th>{{ user.lastName }}</th>
          </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
  <ModalWindow ref="ModalWindow">
    <div id="user-modal">
      <h3>User {{ chooseUser.id }}</h3>
      <div>
        <p>User Role: {{ chooseUser.role }}</p>
        <select class="form-select" v-model="role">
          <option value="CLIENT">Client</option>
          <option value="ADMIN">Admin</option>
          <option value="SELLER">Seller</option>
        </select>
      </div>
      <div>
        <p>User Blocked: <input type="checkbox" v-bind="chooseUser.blocked" disabled></p>
        <input type="checkbox" v-model="isBlocked">
      </div>
      <div>
        <button class="btn btn-outline-dark" @click="saveUser">Save</button>
      </div>
    </div>
  </ModalWindow>
</template>

<script>
import {sendRequest} from "@/scripts/request.js";
import ModalWindow from "@/components/ModalWindow.vue";

export default {
  name: "Users",
  components: {ModalWindow},
  data() {
    return {
      users: {},
      chooseUser: {},
      role: "",
      isBlocked: false
    }
  },
  mounted() {
    this.getUsers();
  },
  methods: {
    async getUsers() {
      try {
        const response = await sendRequest("/user/all", "GET", null);
        if (response.ok) {
          this.users = await response.json();
        }
      } catch (e) {
        console.error(e)
      }
    },
    async saveUser() {
      try {
        const response = await sendRequest("/user/permission", "PUT", {
          id: this.chooseUser.id,
          role: this.role,
          blocked: this.isBlocked
        });
        if (response.ok) {
          this.$refs.ModalWindow.closeModal();
        }
      } catch (e) {
        console.error(e)
      }
    },
    openWindowUser(user) {
      this.chooseUser = user;
      this.isBlocked = this.chooseUser.blocked;
      this.role = this.chooseUser.role;
      this.$refs.ModalWindow.openModal();
    }
  }
}
</script>

<style scoped>
#user-modal {
  width: 1000px;
  height: 80vh;
  position: relative;
  align-items: center;
}

#user-list {
  background-color: rgba(255, 255, 255, 0.8);
  padding: 2em 1em;
}
</style>