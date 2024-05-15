<template>
  <div id="authorization">
    <div class="d-flex justify-content-center align-items-center forms">
      <div class="moving-image">
        <img class="" src="../assets/Alternative.jpg">
      </div>
      <form @submit.prevent="signUpForm" class="sign-up ">
        <label class="title-card">Sign up</label>
        <div class="form-group">
          <label class="form-label">Email</label>
          <input type="email" class="form-control" v-model="formData.email">
        </div>
        <div class="form-group">
          <label class="form-label">Password</label>
          <div class="password">
            <input class="form-control" :type="showPassword ? 'text' : 'password'" v-model="formData.password" >
            <span @click="showPassword = togglePasswordVisible(showPassword)">
              <i v-if="showPassword" class="fas fa-eye-slash"></i>
              <i v-else class="fas fa-eye"></i>
            </span>
          </div>
        </div>
        <div class="form-group">
          <label class="form-label">Confirm password</label>
          <div class="password">
            <input class="form-control" @input="validateConfirmPassword"
                   :type="showConfirmPassword ? 'text' : 'password'" v-model="confirmPass">
            <span @click="showConfirmPassword = togglePasswordVisible(showConfirmPassword)">
              <i v-if="showConfirmPassword" class="fas fa-eye-slash"></i>
              <i v-else class="fas fa-eye"></i>
            </span>
            <div class="form-text">{{ validationErrors.confirmPass }}</div>
          </div>
        </div>
        <div class="button">
          <button class="btn btn-outline-dark" type="submit">Sign up</button>
        </div>
      </form>
      <form @submit.prevent="signInForm" class="sign-in">
        <label class="title-card">Sign In</label>
        <div class="form-group">
          <label class="form-label">Email</label>
          <input class="form-control" type="email" name="login-email" v-model="formData.email" maxlength="25">
        </div>
        <div class="form-group">
          <label class="form-label">Password</label>
          <div class="password">
            <input class="form-control " name="login-password" :type="showPassword ? 'text' : 'password'" v-model="formData.password"
                   maxlength="20">
            <span @click="showPassword = togglePasswordVisible(showPassword)">
              <i v-if="showPassword" class="fas fa-eye-slash"></i>
              <i v-else class="fas fa-eye"></i>
            </span>
          </div>
        </div>
        <div class="button">
          <button class="btn btn-outline-primary" type="submit" name="login">Sign In</button>
        </div>
      </form>
    </div>
  </div>
</template>

<script>
import {sendRequest} from "@/scripts/request";
import {userStore} from "@/stores/userStore.js";
import {fetchUser} from "@/scripts/services.js";

export default {
  // eslint-disable-next-line vue/multi-word-component-names
  name: "Authorization",
  data() {
    return {
      isLoginFormVisible: true,
      showPassword: false,
      showConfirmPassword: false,
      confirmPass: '',
      formData: {
        email: '',
        password: ''
      },
      validationErrors: {
        confirmPass: ''
      },
      resp: []
    };
  },
  methods: {
    toggleForm() {
      this.isLoginFormVisible = !this.isLoginFormVisible;
      const movingImage = document.querySelector('.moving-image');
      this.showPassword = false;
      this.showConfirmPassword = false;
      if (this.isLoginFormVisible) {
        movingImage.style.left = '0';
      } else {
        movingImage.style.left = `50%`;
      }
    },
    validationField() {
      if (this.formData.email === "") {
        this.Notiflix.Notify.failure("Please fill in the email field")
      }
      if (this.formData.password === "") {
        this.Notiflix.Notify.failure("Please fill in the password field")
      }
    },
    validateConfirmPassword() {
      if (this.formData.password !== this.confirmPass) {
        this.validationErrors.confirmPass = "Password do not match";
        return false;
      } else {
        this.validationErrors.confirmPass = "";
        return true;
      }
    },
    async signInForm() {
      if (this.isLoginFormVisible) {
        if (this.formData.email !== '' && this.formData.password !== '') {
          const response = await sendRequest('/auth/login', 'POST', this.formData)
          console.log(await response)
          if (response.ok) {
            const store = userStore();
            const data = await response.json();
            localStorage.setItem("Token", data['token']);
            store.login();
            await fetchUser(store);
            // this.$store.dispatch('fetchUser');

            this.$router.push('/');
          } else {
            const errorMessage = await response.text();
            this.Notiflix.Notify.failure(errorMessage);
          }
        } else {
          this.validationField();
        }
      } else {
        this.toggleForm();
      }

    },
    async signUpForm() {
      if (!this.isLoginFormVisible) {
        if (this.formData.email !== '' && this.formData.password !== '' && this.validateConfirmPassword()) {
          const response = await sendRequest('/auth/registration', 'POST', this.formData)
          if (response.ok) {
            const store = userStore();
            const data = await response.json();
            localStorage.setItem("Token", data['token']);

            store.login();
            await fetchUser(store);

            // this.$store.dispatch('fetchUser');

            this.$router.push('/');
          } else {
            const errorMessage = await response.text();
            this.Notiflix.Notify.failure(errorMessage);
          }
        } else {
          this.validationField();
          this.validateConfirmPassword() ? this.Notiflix.Notify.failure("Please fill in the password confirm field") : null;
        }
      } else {
        this.toggleForm();
      }
    },
  }

}
</script>

<style scoped>
#authorization {
  background-image: url("../assets/background.jpg");
  background-size: cover;
  background-repeat: no-repeat;
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
}

.forms {
  box-shadow: 0px 0px 10px 5px #00000040;
  position: relative;
}

.moving-image {
  width: 25em;
  height: 30em;
  position: absolute;
  z-index: 1;
  left: 0;
  border-right: 1px solid black;
  border-left: 1px solid black;

  transform: translateX(0);
  transition: left 0.3s ease;
}

.moving-image img {
  width: 100%;
}

.sign-in {
  color: var(--blue);
}

.sign-in,
.sign-up {
  padding: 2em 2em;
  width: 25em;
  height: 30em;
  background: #fff;

  position: relative;
}

.title-card {
  text-align: center;
  width: 100%;
  font-family: 'Jim Nightshade', cursive;
  font-size: 32pt;
}

.form-group {
  margin: 1em 0;
}

.password {
  position: relative;
}

.password span {
  position: absolute;
  right: .5em;
  top: .5em;
}

.button {
  position: absolute;
  margin: 1.5em 0;
  bottom: 0;
  left: 0;
  width: 100%;
  text-align: center;
  z-index: 2;
}

.button button {
  width: 10em;
}

</style>