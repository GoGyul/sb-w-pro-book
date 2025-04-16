<script setup lang="ts">
import HeaderComponent from "@/components/common/HeaderComponent.vue";
import { ref, computed, reactive } from "vue";
import { useRouter } from "vue-router";

const router = useRouter();

const insertForm = reactive({
  title: "",
  category: "MOVIE",
  rating: "",
  movieTitle: "",
  content: "",
  userId: "",
});

// const title = ref("");
// const category = ref("MOVIE");
// const rating = ref("");
// const movieTitle = ref("");
// const content = ref("");

const isMovieReview = computed(() => insertForm.category === "MOVIE");
const ratings = Array.from({ length: 10 }, (_, i) => (i + 1) * 0.5);

const submitPost = () => {
  if (!insertForm.title.trim()) return alert("제목을 입력해주세요.");
  if (!insertForm.category) return alert("카테고리를 선택해주세요.");
  if (isMovieReview.value && !insertForm.movieTitle.trim())
    return alert("영화 제목을 입력해주세요.");
  if (isMovieReview.value && !insertForm.rating)
    return alert("별점을 선택해주세요.");
  if (!insertForm.content.trim()) return alert("내용을 입력해주세요.");
  router.push("/board"); // 게시판으로 이동
};
</script>

<template>
  <HeaderComponent />
  <div class="write-page">
    <h1>📝 글쓰기</h1>

    <div class="form-group">
      <label>제 목</label>
      <input
        v-model="insertForm.title"
        type="text"
        placeholder="글 제목을 입력하세요"
      />
    </div>

    <div class="form-group">
      <label>카테고리</label>
      <select v-model="insertForm.category">
        <option value="MOVIE">영화후기</option>
        <option value="ANYTHING">아무이야기</option>
      </select>
    </div>

    <div class="form-group" v-if="isMovieReview">
      <label>영화 제목</label>
      <input
        v-model="insertForm.movieTitle"
        type="text"
        placeholder="영화 제목을 입력하세요"
      />
    </div>

    <div class="form-group" v-if="isMovieReview">
      <label>별점</label>
      <select v-model="insertForm.rating">
        <option value="">선택</option>
        <option v-for="score in ratings" :key="score" :value="score">
          {{ score }}
        </option>
      </select>
    </div>

    <div class="form-group">
      <label>내용</label>
      <textarea
        v-model="insertForm.content"
        rows="6"
        placeholder="내용을 입력하세요"
      ></textarea>
    </div>

    <div class="button-group">
      <button @click="submitPost">작성 완료</button>
      <button class="cancel-btn" @click="router.push('/board')">취소</button>
    </div>
  </div>
</template>

<style scoped>
.write-page {
  max-width: 600px;
  margin: 40px auto;
  padding: 20px;
  border: 1px solid #ddd;
  border-radius: 10px;
}

h1 {
  text-align: center;
  margin-bottom: 24px;
}

.form-group {
  margin-bottom: 16px;
  display: flex;
  flex-direction: column;
}

label {
  font-weight: bold;
  margin-bottom: 6px;
}

input,
select,
textarea {
  padding: 10px;
  font-size: 16px;
  border: 1px solid #ccc;
  border-radius: 6px;
}

textarea {
  resize: vertical;
}

.button-group {
  text-align: center;
  margin-top: 20px;
}

button {
  background-color: #4caf50;
  color: white;
  font-size: 16px;
  padding: 10px 24px;
  border: none;
  border-radius: 8px;
  cursor: pointer;
}

button:hover {
  background-color: #45a049;
}
.cancel-btn {
  background-color: #ccc;
  color: #333;
  margin-left: 10px;
}

.cancel-btn:hover {
  background-color: #bbb;
}
</style>
