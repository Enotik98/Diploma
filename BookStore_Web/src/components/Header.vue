<template>
  <header class="">
    <nav class="navbar">
      <span><router-link to="/" class="logo">LibraryPlusPlus</router-link></span>
      <ul>
        <li><router-link to="/" class="link" exact><div class="nav-item" >Catalog</div></router-link></li>
        <li><router-link to="/orders" class="link" exact><div class="nav-item">Orders</div></router-link></li>
        <li><router-link to="/users" class="link" exact><div class="nav-item">Users</div></router-link></li>
        <li><router-link to="/analytics" class="link" exact><div class="nav-item">Analytics</div></router-link></li>
        <li><router-link to="/profile" class="link" exact><div class="nav-item" >Profile</div></router-link></li>
        <li v-if="!store.$state.isLoggedIn"><router-link to="/login" class="link" exact><div class="nav-item" >Sign in</div></router-link></li>
        <li v-if="store.$state.isLoggedIn"><div @click="logoutSys" class="link"><div class="nav-item" >Logout</div></div></li>
        <li v-if="store.$state.isLoggedIn"><div @click="openModal" class="nav-item">Basket</div></li>
      </ul>
    </nav>
    <ModalWindow ref="ModalWindow">
      <Basket :modal-close="() => {this.$refs.ModalWindow.closeModal()}" ></Basket>
    </ModalWindow>
  </header>
</template>

<script>
import {userStore} from "@/stores/userStore.js";
import ModalWindow from "@/components/ModalWindow.vue";
import Basket from "@/components/Basket.vue";

export default {
  name: "Header",
  components: {Basket, ModalWindow},
  setup() {
    const store = userStore();

    const logoutSys = () => {
      console.log("1")
      store.logout();
    }

    return {
      store,
      logoutSys
    };
  },
  methods: {
    openModal() {
      this.$refs.ModalWindow.openModal();
    }
  }
}
</script>

<style scoped>
header {
  width: 100%;
}
.navbar {
  display: flex;
  justify-content: space-between;
  padding: 1em 2em;
  box-shadow: 0px 4px 4px 0px #00000040;
  background-color: #fff;

}

.navbar ul {
  list-style: none;
  display: flex;
  gap: 1em;
  margin: 0;
}
.navbar ul li {
  position: relative;
  width: 6em;
}
.nav-item{
  padding: .3rem;
  width: 6em;
  text-align: center;
  border-radius: 5px;


  background-color: #ffffff;
  color: black;
}
.link.router-link-exact-active .nav-item,
.nav-item:hover{
  background-color: black;
  color: white;
}

.logo {
  text-decoration: none;
  color: var(--blue);

  font-family: Megrim;
  font-size: 24pt;
  font-weight: 500;
  line-height: 37px;
  letter-spacing: 0em;
  text-align: left;
}

.link {
  text-decoration: none;
}
</style>