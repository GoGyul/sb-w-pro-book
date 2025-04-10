<script setup lang="ts">
import { ref } from "vue";
import LoginModal from "@/components/auth/LoginModal.vue";
import { useAuthStore } from "@/stores/useAuthStore"; // ✅ store 불러오기

const showLogin = ref(false);
const authStore = useAuthStore(); // ✅ store 인스턴스 사용
</script>

<template>
  <div class="main-page">
    <header class="header">
      <button class="login-btn" @click="showLogin = true">로그인</button>
    </header>
    <main class="content">
      <h1>메인 페이지입니다</h1>
      <h1>{{ authStore }}</h1>
    </main>

    <LoginModal v-if="showLogin" @close="showLogin = false" />
  </div>
</template>
