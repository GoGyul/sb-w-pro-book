<script setup lang="ts">
import HeaderComponent from "@/components/common/HeaderComponent.vue";
import { useRouter } from "vue-router";

const router = useRouter();

const goHome = () => {
  router.push("/");
};
</script>

<template>
  <HeaderComponent />

  <!-- ✅ 헤더 바로 아래에 위치한 HOME 버튼 -->
  <div class="home-button-wrapper">
    <button class="home-btn" @click="goHome">🏠 HOME</button>
  </div>

  <div class="board-page">
    <h1>📋</h1>
    <p></p>
  </div>
</template>

<style scoped>
.home-button-wrapper {
  display: flex;
  justify-content: center;
  margin-top: 1px;
}

.home-btn {
  background-color: #4caf50;
  color: white;
  font-size: 16px;
  padding: 10px 20px;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.home-btn:hover {
  background-color: #45a049;
}

.board-page {
  padding: 40px;
  text-align: center;
}

.board-page h1 {
  font-size: 32px;
  margin-bottom: 20px;
}

.board-page p {
  font-size: 18px;
  color: #555;
}
</style>
