<script setup lang="ts">
import { ref } from "vue";

interface Post {
  id: number;
  title: string;
  author: string;
  views: number;
}

const posts = ref<Post[]>([]);

// 테스트용 더미 데이터 20개 생성
for (let i = 1; i <= 20; i++) {
  posts.value.push({
    id: i,
    title: `게시글 제목 ${i}`,
    author: `작성자${i}`,
    views: Math.floor(Math.random() * 100), // 조회수 랜덤
  });
}
</script>

<template>
  <div class="board-list">
    <ul>
      <li v-for="post in posts" :key="post.id" class="post-item">
        <div class="post-row">
          <div class="post-title">{{ post.title }}</div>
          <div class="post-author">{{ post.author }}</div>
          <div class="post-views">{{ post.views }}</div>
        </div>
      </li>
    </ul>
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
</style>
