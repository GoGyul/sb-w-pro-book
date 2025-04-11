<script setup lang="ts">
import { ref } from "vue";
import HeaderComponent from "@/components/common/HeaderComponent.vue";
import { useAuthStore } from "@/stores/useAuthStore"; // ✅ store 불러오기

const showLogin = ref(false);
const authStore = useAuthStore(); // ✅ store 인스턴스 사용
</script>

<template>
  <div class="main-page">
    <HeaderComponent />

    <main class="content">
      <h1></h1>
      <h1>
        {{ authStore }}
      </h1>
      <!-- 로그인 상태에 따른 출력 -->
    </main>
  </div>
</template>

<style scoped>
.main-page {
  padding-top: 60px; /* 헤더 높이만큼 여백 */
}

.content {
  padding: 20px;
}
</style>
