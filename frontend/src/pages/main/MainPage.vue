<script setup lang="ts">
import { ref } from "vue";
import LoginModal from "@/components/auth/LoginModal.vue";

const showLogin = ref(false);
</script>

<template>
  <div class="main-page">
    <header class="header">
      <button class="login-btn" @click="showLogin = true">로그인</button>
    </header>
    <main class="content">
      <h1>메인 페이지입니다</h1>
    </main>

    <LoginModal v-if="showLogin" @close="showLogin = false" />
  </div>
</template>
