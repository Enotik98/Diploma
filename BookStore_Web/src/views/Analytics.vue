<template>
  <div class="body container">
    <div>
      <h3>Analytics</h3>
      <div>
        <label>Choose type</label>
        <select class="form-select" v-model="report">
          <option selected disabled value="">Open this select menu</option>
          <option value="trends" aria-label="Default">Trends</option>
        </select>
      </div>
      <button class="btn btn-outline-dark" @click="getReport">Download</button>
    </div>
  </div>
</template>

<script>
import {sendRequest} from "@/scripts/request.js";

export default {
  name: "Analytics",
  data() {
    return{
      report: ""
    }
  },
  methods: {
    async getReport() {
      try {
        const response  = await sendRequest("/analytics/arima-excel", "GET", null)
        if (response.ok) {
          const blob = await response.blob();
          const url = window.URL.createObjectURL(blob);
          const link = document.createElement('a');
          link.href = url;
          link.setAttribute("download", `report.xlsx`);
          document.body.appendChild(link);
          link.click();
          link.parentNode.removeChild(link);
        } else {
          console.log('Failed to download report');
        }
      }catch (e) {
        console.log(e)
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
</style>