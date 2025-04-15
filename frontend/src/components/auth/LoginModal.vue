<script setup lang="ts">
import { ref, defineEmits, reactive } from "vue";
import { useLoginMutation } from "@/api/auth/login/use/useLoginMutation";
import { useAuthStore } from "@/stores/useAuthStore";
import type { LoginUserDto, LoginResponseDto } from "@/types/auth/loginDto";

const emit = defineEmits(["close"]);
const authStore = useAuthStore();

const insertForm = reactive({
  userId: "",
  userPassword: "",
});

const { mutate: loginMutate, isPending, isError, error } = useLoginMutation();

const doLogin = () => {
  console.log("ID:", insertForm.userId);
  console.log("Password:", insertForm.userPassword);

  const loginData: LoginUserDto = {
    userId: insertForm.userId,
    userPassword: insertForm.userPassword,
  };

  // useMutation의 mutate 함수 호출
  loginMutate(loginData, {
    onSuccess: (res) => {
      authStore.loginSuccess(
        res.accessToken,
        res.user.userId,
        res.user.nickname
      ); // ✅ 유저 상태 저장..
      emit("close");
    },
    onError: (error) => {
      console.error("❌ 로그인 실패", error);
      alert("로그인에 실패했습니다. 아이디/비밀번호를 확인해주세요.");
    },
  });
};

const emitClose = () => {
  emit("close");
};
</script>

<template>
  <div class="modal-backdrop" @click.self="emitClose">
    <div class="modal">
      <h2>로그인</h2>
      <form @submit.prevent="doLogin">
        <input type="text" v-model="insertForm.userId" placeholder="ID" />
        <input
          type="password"
          v-model="insertForm.userPassword"
          placeholder="비밀번호"
        />
        <div class="button-group">
          <button type="submit">로그인</button>
          <button type="button" class="cancel-btn" @click="emitClose">
            닫기
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<style scoped>
.modal-backdrop {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.4);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal {
  background: white;
  padding: 2rem;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.2);
  min-width: 300px;
}

input {
  display: block;
  margin-bottom: 1rem;
  padding: 0.5rem;
  width: 100%;
  box-sizing: border-box;
}

.button-group {
  display: flex;
  justify-content: space-between;
  gap: 1rem;
}

button {
  flex: 1;
  padding: 0.5rem;
  border-radius: 6px;
  font-size: 14px;
  cursor: pointer;
  border: none;
}

button[type="submit"] {
  background-color: #42b983;
  color: white;
}

button[type="submit"]:hover {
  background-color: #369870;
}

.cancel-btn {
  background-color: #e0e0e0;
  color: #333;
}

.cancel-btn:hover {
  background-color: #c0c0c0;
}
</style>
