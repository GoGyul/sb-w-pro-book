<script setup lang="ts">
import { ref } from "vue";
import { useRouter } from "vue-router";
import HeaderComponent from "@/components/common/HeaderComponent.vue";
import { useAuthStore } from "@/stores/useAuthStore"; // ✅ store 불러오기

const router = useRouter();
const showLogin = ref(false);
const authStore = useAuthStore(); // ✅ store 인스턴스 사용

const goToSecretPage = () => {
  router.push("/secret"); // 이동할 라우터 경로
};
</script>

<template>
  <div class="main-page">
    <HeaderComponent />

    <main class="content">
      <h1></h1>
      <h1>
        {{ authStore }}
        {{ authStore.nickname }}
      </h1>
      <div class="center-box" v-if="authStore.isLoggedIn">
        <button class="secret-btn" @click="goToSecretPage">
          비밀 페이지로 이동
        </button>
      </div>
      <!-- 로그인 상태에 따른 출력 -->
    </main>
  </div>
</template>

<div class="center-box" v-if="authStore.isLoggedIn">
<button class="secret-btn" @click="goToSecretPage">
  비밀 페이지로 이동
</button>
</div>
