<template>
    <div>
        Hello
        <button @click="submitForm">Submit</button>
    </div>
</template>

<script >
import {sendRequest} from "../scipts/request.js";

export default {
    name: 'HomeV',
    data() {
        return {
            req: []
        }
    },
    methods: {
        async submitForm(e) {
            e.preventDefault();
            console.log("1");
            const response = await sendRequest('/auth/login', 'POST', {"email": "alex","password": "bla"} )
            console.log(response);
            if (response.status == 200) {
                const res = await response.json();
                console.log(res);
            }
        }
    }
}
</script>

<style lang="scss" scoped>

</style>