import { defineStore } from "pinia";

export const useAuthStore = defineStore("auth", {
  state: () => ({
    // 앱 시작 시 localStorage에서 데이터 불러오기
    accessToken: localStorage.getItem("accessToken") || (null as string | null),
    userId: localStorage.getItem("userId") || (null as string | null),
    nickname: localStorage.getItem("nickname") || (null as string | null),
  }),

  getters: {
    isLoggedIn: (state) => !!state.accessToken,
  },

  actions: {
    loginSuccess(token: string, userId: string, nickname: string) {
      // 상태 업데이트
      this.accessToken = token;
      this.userId = userId;
      this.nickname = nickname;

      // localStorage에 저장
      localStorage.setItem("accessToken", token);
      localStorage.setItem("userId", userId);
      localStorage.setItem("nickname", nickname);
    },

    logout() {
      // 상태 초기화
      this.accessToken = null;
      this.userId = null;
      this.nickname = null;

      // localStorage에서 제거
      localStorage.removeItem("accessToken");
      localStorage.removeItem("userId");
      localStorage.removeItem("nickname");
    },
  },
});
