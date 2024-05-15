<template>
  <div class="modal" v-if="showModal">
    <div class="modal-content">
      <div class="modal-btn">
        <button @click="closeModal" class="btn-close"></button>
      </div>
      <slot></slot>
    </div>
  </div>
</template>

<script>
export default {
  name: "ModalWindow",
  data() {
    return {
      showModal: false,
    }
  },
  methods: {
    openModal() {
      this.showModal = true;
    },
    closeModal() {
      this.showModal = false;
    }
  }
}
</script>

<style scoped>
.modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal-content {
  background: #ffffff;
  width: fit-content;
  padding: 1em;
}

.modal-btn{
  display: flex;
  justify-content: end;
}

</style>