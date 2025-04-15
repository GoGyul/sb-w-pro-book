<script setup lang="ts">
import { ref } from "vue";
import LoginModal from "@/components/auth/LoginModal.vue";
import SignUpModal from "@/components/auth/SignUpModal.vue";
import { useAuthStore } from "@/stores/useAuthStore";
import { useLogoutMutation } from "@/api/auth/logout/use/useLogoutMutation";

const showLogin = ref(false);
const showSignup = ref(false);
const authStore = useAuthStore(); // ✅ store 인스턴스 사용

const { mutate: logoutMutate, isPending, isError, error } = useLogoutMutation();

const doLogout = async () => {
  if (!authStore.accessToken) return; // 토큰이 없으면 그냥 리턴

  logoutMutate(authStore.accessToken, {
    onSuccess: () => {
      authStore.logout(); // ✅ 유저 상태 초기화
    },
    onError: (error) => {
      console.error("로그아웃 실패:", error);
    },
  });
};
</script>

<template>
  <header class="header">
    <div class="header-inner">
      <span style="margin-right: 10px">👤 </span>
      <span v-if="authStore.isLoggedIn"
        >안녕하세요, {{ authStore.nickname }} 님!</span
      >
      <span v-else>로그인 해주세요.</span>
      <button
        v-if="!authStore.isLoggedIn"
        class="login-btn"
        @click="showLogin = true"
      >
        로그인
      </button>
      <button v-if="authStore.isLoggedIn" class="logout-btn" @click="doLogout">
        로그아웃
      </button>
      <button class="signup-btn" @click="showSignup = true">회원가입</button>
    </div>

    <LoginModal v-if="showLogin" @close="showLogin = false" />
    <SignUpModal v-if="showSignup" @close="showSignup = false" />
  </header>
</template>

<style scoped>
.header {
  position: fixed;
  top: 0;
  width: 100%;
  height: 60px;
  background: white;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  z-index: 1000;
}

.header-inner {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  height: 100%;
  padding: 0 20px;
}

.signup-btn,
.login-btn,
.logout-btn {
  padding: 6px 12px;
  font-size: 14px;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.signup-btn {
  background-color: #2196f3; /* 파란색 */
  margin: 0 10px; /* 로그인 버튼과 로그아웃 버튼 사이 간격 확보 */
}

.login-btn {
  background-color: #4caf50;
  margin-left: 10px;
}

.logout-btn {
  background-color: #f44336;
  margin-left: 10px;
}
</style>
