<script setup lang="ts">
import { ref, watch, computed } from "vue";
import { useMvBoardListQuery } from "@/api/board/movie/use/useMvBoardListQuery";

const currentPage = ref(1);
const pageSize = 10;

// API 호출 (자동으로 실행됨!)
const { data, isLoading, isError } = useMvBoardListQuery(currentPage, pageSize);

const mvBoardList = computed(() => data?.value?.items || []);
const totalPages = computed(() =>
  data?.value?.totalCount ? Math.ceil(data.value.totalCount / pageSize) : 1
);

// 페이지 이동 핸들러
function nextPage() {
  if (currentPage.value < totalPages.value) {
    currentPage.value += 1;
  }
}
function prevPage() {
  if (currentPage.value > 1) {
    currentPage.value -= 1;
  }
}
</script>

<template>
  <div class="board-list">
    <div v-if="isLoading">로딩 중...</div>
    <div v-else-if="isError">에러가 발생했습니다.</div>
    <ul v-else>
      <li v-for="board in mvBoardList" :key="board.id" class="post-item">
        <div class="post-row">
          <div class="post-title">{{ board.title }}</div>
          <div class="post-author">{{ board.userId }}</div>
          <div class="post-views">{{ board.views }}</div>
        </div>
      </li>
    </ul>
    <div class="pagination">
      <button @click="prevPage" :disabled="currentPage === 1">이전</button>
      <span>Page {{ currentPage }} / {{ totalPages }}</span>
      <button @click="nextPage" :disabled="currentPage === totalPages">
        다음
      </button>
    </div>
  </div>
</template>

<style scoped>
.board-list {
  max-width: 800px;
  margin: 0 auto;
  padding: 0 16px;
}

.post-item {
  border-bottom: 1px solid #ddd;
  padding: 12px 0;
}

.post-row {
  display: flex;
  align-items: center;
  font-size: 16px;
}

.post-title {
  width: 60%;
  font-weight: 600;
  color: #333;
  text-align: left;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.post-author {
  width: 32%; /* 👈 더 좁게 */
  text-align: right;
  padding-right: 4px;
  color: #555;
}

.post-views {
  width: 8%; /* 👈 나머지 맞추기 위해 조정 */
  text-align: right;
  color: #777;
}
.pagination {
  margin-top: 16px;
  display: flex;
  justify-content: center;
  gap: 8px;
}
</style>
