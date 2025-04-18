<script setup lang="ts">
import HeaderComponent from "@/components/common/HeaderComponent.vue";
import { ref, computed, reactive } from "vue";
import { useRouter } from "vue-router";
import {
  MvBoardListResponseDto,
  MvBoardDto,
} from "@/types/board/mvboard/mvBoardListResponseDto";
import { useAuthStore } from "@/stores/useAuthStore";
import { useMvBoardMutation } from "@/api/board/movie/use/useMvBoardListQuery";

const router = useRouter();
const authStore = useAuthStore();

const insertForm = reactive({
  title: "",
  category: "MOVIE",
  rating: 0,
  movieTitle: "",
  content: "",
  userId: authStore.userId,
});

// const title = ref("");
// const category = ref("MOVIE");
// const rating = ref("");
// const movieTitle = ref("");
// const content = ref("");

const isMovieReview = computed(() => insertForm.category === "MOVIE");
const ratings = Array.from({ length: 10 }, (_, i) => (i + 1) * 0.5);

const {
  mutate: mvBoardMutation,
  isPending,
  isError,
  error,
} = useMvBoardMutation();

const submitPost = () => {
  if (!insertForm.title.trim()) return alert("제목을 입력해주세요.");
  if (!insertForm.category) return alert("카테고리를 선택해주세요.");
  if (isMovieReview.value && !insertForm.movieTitle.trim())
    return alert("영화 제목을 입력해주세요.");
  if (isMovieReview.value && !insertForm.rating)
    return alert("별점을 선택해주세요.");
  if (!insertForm.content.trim()) return alert("내용을 입력해주세요.");

  const mvBoardDto: MvBoardDto = {
    title: insertForm.title,
    content: insertForm.content,
    userId: authStore.userId,
    category: insertForm.category,
    movieTitle: insertForm.movieTitle,
    rating: insertForm.rating,
  };

  if (insertForm.category === "ANYTHING") {
    mvBoardDto.movieTitle = "";
    mvBoardDto.rating = null;
  }

  console.log(mvBoardDto);

  // useMutation의 mutate 함수 호출
  mvBoardMutation(mvBoardDto, {
    onSuccess: (res) => {
      alert("게시글 작성 성공");
      router.push("/board"); // 게시판으로 이동
    },
    onError: (error) => {
      console.error("❌ 작성실패", error);
      alert("게시글 작성 실패");
    },
  });
};
</script>

<template>
  <HeaderComponent />
  <div class="write-page">
    <span>{{ insertForm }}</span>
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
