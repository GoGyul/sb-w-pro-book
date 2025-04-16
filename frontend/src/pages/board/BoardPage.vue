<script setup lang="ts">
import HeaderComponent from "@/components/common/HeaderComponent.vue";
import BoardList from "@/pages/board/components/BoardList.vue";
import { useRouter } from "vue-router";

const router = useRouter();

const goHome = () => {
  router.push("/");
};
const goWritePage = () => {
  router.push("/board/writePage");
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
    <div class="write-button-wrapper">
      <button @click="goWritePage" class="write-btn">✍️ 글쓰기</button>
    </div>
    <BoardList />
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

.write-button-wrapper {
  display: flex;
  justify-content: flex-end;
  max-width: 800px;
  margin: 0 auto 10px;
}

.write-btn {
  background-color: #2196f3;
  color: white;
  font-size: 14px;
  padding: 8px 16px;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.write-btn:hover {
  background-color: #1976d2;
}
</style>
