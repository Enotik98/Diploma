<template>
  <div class="container d-flex">
    <div class="w-100" id="user-list">
      <div class="table-responsive">
        <p class="title">Users</p>
        <table class="table table-hover ">
          <thead class="thead-dark">
          <tr>
            <th scope="col">#</th>
            <th scope="col">Email</th>
            <th scope="col">First Name</th>
            <th scope="col">Last Name</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="user in users" :key="user.id" @click="openWindowUser(user)">
            <td>{{ user.id }}</td>
            <td>{{ user.email }}</td>
            <td>{{ user.firstName }}</td>
            <td>{{ user.lastName }}</td>
          </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
  <ModalWindow ref="ModalWindow">
    <div id="user-modal">
      <h3>User {{ chooseUser.id }}</h3>
      <div class="form-group">
        <label>User Role:</label>
        <select class="form-select" v-model="role">
          <option value="CLIENT">Client</option>
          <option value="ADMIN">Admin</option>
          <option value="SELLER">Seller</option>
        </select>
      </div>
      <div class="form-group">
        <label>User Blocked: </label>
        <input type="checkbox" v-model="isBlocked" class="form-check-input ms-2">
      </div>
      <div class="form-group">
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
      users: [],
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
        console.error(e);
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
        console.error(e);
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
.container {
  padding: 2rem;
}

#user-list {
  background-color: rgba(255, 255, 255, 0.8);
  padding: 1rem;
  border-radius: 10px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}

/*.table-hover tbody tr:hover {*/
/*  cursor: pointer;*/
/*  background-color: rgba(0, 0, 0, 0.075);*/
/*}*/

#user-modal {
  width: 500px;
  padding: 1rem;
  background-color: #fff;
  border-radius: 10px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}

#user-modal h3 {
  margin-bottom: 1rem;
}

.form-group {
  margin-bottom: 1rem;
}

.form-select {
  width: 100%;
  padding: 0.375rem 0.75rem;
  font-size: 1rem;
  line-height: 1.5;
  border: 1px solid #ced4da;
  border-radius: 0.25rem;
}

.btn-outline-dark {
  width: 100%;
  padding: 0.5rem;
  font-size: 1rem;
  font-weight: bold;
  border-radius: 0.25rem;
}
</style>
