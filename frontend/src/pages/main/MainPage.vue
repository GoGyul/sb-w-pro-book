<script setup lang="ts">
// 필요시 나중에 로그인 로직이나 라우팅 추가 가능
</script>

<template>
  <div class="main-page">
    <header class="header">
      <button class="login-btn">로그인</button>
    </header>
    <main class="content">
      <h1>메인 페이지입니다</h1>
    </main>
  </div>
</template>

<style scoped>
.main-page {
  display: flex;
  flex-direction: column;
  height: 100vh;
}

.header {
  display: flex;
  justify-content: flex-end;
  padding: 1rem;
  background-color: #f5f5f5;
  border-bottom: 1px solid #ddd;
}

.login-btn {
  background-color: #42b983;
  color: white;
  border: none;
  padding: 0.5rem 1rem;
  border-radius: 6px;
  cursor: pointer;
  font-size: 14px;
}

.login-btn:hover {
  background-color: #369870;
}

.content {
  flex: 1;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 24px;
}
</style>
