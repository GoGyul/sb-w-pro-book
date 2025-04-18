<script setup lang="ts">
import { ref, onMounted } from "vue";
import { useRoute, useRouter } from "vue-router";
import { mvBoardApi } from "@/api/board/movie/mvBoardApi";

import { useMvBoardDetailQuery } from "@/api/board/movie/use/useMvBoardListQuery";

const route = useRoute();
const router = useRouter(); // useRouter 훅 사용
const boardId = Number(route.params.id);

const {
  data: boardDetail,
  isLoading,
  isError,
} = useMvBoardDetailQuery(boardId);

// 뒤로 가기 버튼 클릭 시
function goBack() {
  router.back(); // 이전 페이지로 돌아가기
}
</script>

<template>
  <button @click="goBack" class="back-button">뒤로 가기</button>
  <div class="board-detail" v-if="boardDetail">
    <div class="title-row">
      <h1 class="title">{{ boardDetail.title }}</h1>
      <span class="views">조회수 {{ boardDetail.views }}</span>
    </div>
    <div class="author">작성자: {{ boardDetail.userId }}</div>
    <div class="content">
      {{ boardDetail.content }}
    </div>
  </div>
  <div v-else class="loading">불러오는 중...</div>
</template>

<style scoped>
.board-detail {
  max-width: 800px;
  margin: 0 auto;
  padding: 24px 16px;
  font-family: "Segoe UI", sans-serif;
}

.title-row {
  display: flex;
  justify-content: space-between;
  align-items: baseline;
  border-bottom: 1px solid #ccc;
  padding-bottom: 8px;
  margin-bottom: 12px;
}

.title {
  font-size: 24px;
  font-weight: bold;
  color: #222;
  margin: 0;
}

.views {
  font-size: 14px;
  color: #777;
}

.author {
  font-size: 16px;
  color: #444;
  margin-bottom: 16px;
  text-align: left; /* 작성자 텍스트 왼쪽 정렬 */
}

.content {
  font-size: 16px;
  line-height: 1.6;
  white-space: pre-wrap;
  color: #333;
}

.loading {
  text-align: center;
  padding: 40px;
  font-size: 18px;
  color: #666;
}

.back-button {
  background-color: #4caf50;
  color: white;
  border: none;
  padding: 6px 12px; /* 작은 크기로 변경 */
  font-size: 14px; /* 작은 폰트 크기 */
  cursor: pointer;
  margin-bottom: 8px; /* 조회수 바로 위에 위치하도록 간격 조정 */
  display: block;
}
</style>
