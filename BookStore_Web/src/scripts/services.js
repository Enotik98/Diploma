import {sendRequest} from "@/scripts/request.js";

export async function fetchUser(store) {
    console.log(store);
    if (store.$state.isLoggedIn) {
        const response = await sendRequest('/user', 'GET', null);
        if (response.ok) {
            const data = await response.json();
            store.setUser(data);
        } else {
            store.logout();
        }
    }
}