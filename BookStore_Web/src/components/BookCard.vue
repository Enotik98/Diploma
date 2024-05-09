<template>
    <div class="card ">
      <div class="img-wrapper">
      
      </div>
      <div class="card-body">
        <div class="card_header">
          <span class="card_title">{{ book.title }}</span>
        </div>
        <div class="card_text">
          <div>
            <span class="card_year">{{ book.pub_year }}</span>
            <div v-for="genre in book.genrs" :key="genre.id">
              <span class="card_genre"> {{ genre.genre }}</span>
            </div>
          </div>
          <span class="card_author">{{ book.author }}</span>
        </div>
        <div class="card_footer">
          <!-- <router-link :to="{name: 'Product', params: {id: book.id}}" >More information</router-link> -->
        </div>
      </div>
    </div>
  </template>
  
  <script>
  export default {
    name: "BookCard",
    props: {
      book: Object,
      genre: String,
    },
    computed: {
      
    }
  }
  </script>
  
  <style scoped>
  .card {
    width: 300px;
    height: 400px;
    position: relative;
    background-color: #fff;
    color: #000;
  }
  
  .img-wrapper {
    position: relative;
  }
  
  .product-notification {
    position: absolute;
    top:0;
    right: 0;
    background-color: white;
    border-radius: 0 4px 0 4px;
    padding: .5em 1em;
    font-size: .75em;
    color: red;
  }
  .img {
    width: 100%;
    height: 20em;
    object-fit: cover;
    object-position: center top;
  }
  
  .card-body {
    display: flex;
    flex-direction: column;
    padding: .5em;
    height: 20%;
  
    font-size: .75em;
  
    position: relative;
  }
  
  .card_header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    flex-wrap: wrap;
  }
  
  .card_title {
    font-size: 12pt;
  }
  
  .card_author {
    color: black;
  }
  
  .card_text {
    display: flex;
    justify-content: space-between;
    flex-grow: 1;
  }
  
  .card_text > div {
    display: flex;
    gap: 1em;
    color: rgba(0, 0, 0, 0.50);
  }
  
  .card_footer {
    display: flex;
    justify-content: flex-end;
    margin-top: auto;
    font-size: 8pt;
    align-items: flex-end;
  }
  </style>