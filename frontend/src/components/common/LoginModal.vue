<template>
  <div class="modal-backdrop" @click.self="emitClose">
    <div class="modal">
      <h2>로그인</h2>
      <form @submit.prevent="handleLogin">
        <input type="text" v-model="id" placeholder="ID" />
        <input type="password" v-model="password" placeholder="비밀번호" />
        <button type="submit">로그인</button>
      </form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, defineEmits } from "vue";

const emit = defineEmits(["close"]);

const id = ref("");
const password = ref("");

const handleLogin = () => {
  console.log("ID:", id.value);
  console.log("Password:", password.value);
  // 실제 로그인 로직은 나중에 연결
  emit("close");
};

const emitClose = () => {
  emit("close");
};
</script>

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

button {
  background-color: #42b983;
  color: white;
  border: none;
  padding: 0.5rem 1rem;
  border-radius: 6px;
  cursor: pointer;
}
</style>
