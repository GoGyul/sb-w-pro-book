<script setup lang="ts">
import { ref, defineEmits, reactive } from "vue";
import type { CreateUserDto } from "@/types/auth/userDto";
import { useSignUpMutation } from "@/api/auth/signup/use/useSignUpMutation";

const emit = defineEmits(["close"]);

const insertForm = reactive({
  userId: "",
  userPassword: "",
  gender: "",
  birthDate: "",
});

const { mutate: signUpMutate, isPending, isError, error } = useSignUpMutation();

// 회원가입 버튼 클릭 시 처리
const doSignup = () => {
  // if (!insertForm.userId || !insertForm.userPassword) {
  //   alert("아이디와 비밀번호는 필수입니다.");
  //   return;
  // }
  debugger;
  const signUpData: CreateUserDto = {
    userId: insertForm.userId,
    userPassword: insertForm.userPassword,
    gender: insertForm.gender === "" ? null : insertForm.gender,
    birthDate: insertForm.birthDate === "" ? null : insertForm.birthDate,
  };

  signUpMutate(signUpData, {
    onSuccess: (res) => {
      alert("회원가입 성공");
      console.error("❌ 회원가입 성공", res);
      emit("close");
    },
    onError: (error) => {
      console.error("❌ 회원가입 실패", error);
      alert("회원가입에 실패했습니다.");
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
      <h2>회원가입</h2>
      <form @submit.prevent="doSignup">
        <input
          type="text"
          v-model="insertForm.userId"
          placeholder="ID (필수)"
          required
          maxlength="10"
        />
        <input
          type="password"
          v-model="insertForm.userPassword"
          placeholder="비밀번호 (필수)"
          required
          maxlength="10"
        />
        <select v-model="insertForm.gender">
          <option disabled value="">성별 선택 (선택)</option>
          <option value="M">남자</option>
          <option value="F">여자</option>
          <option value="">없음</option>
        </select>
        <input
          type="date"
          v-model="insertForm.birthDate"
          placeholder="생년월일 (선택)"
        />

        <div class="button-group">
          <button type="submit">가입하기</button>
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

input,
select {
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
