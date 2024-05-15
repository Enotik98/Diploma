import {defineStore} from "pinia";

export const userStore = defineStore('user', {
    state: () => ({
        isLoggedIn: !!localStorage.getItem('Token'),
        isUser: true,
        userRole: "USER",
        countBasket: 0
    }),
    actions: {
        login() {
            this.isLoggedIn = !!localStorage.getItem('Token');
        },
        setUser(values) {
            if (values) {
                this.userRole = values['role'];
                this.isUser = values["role"] === "USER";
            }
        },
        logout() {
            this.User = true;
            this.isLoggedIn = false;
            this.userRole = 'USER';
            localStorage.removeItem('Token');
        }
    }
})