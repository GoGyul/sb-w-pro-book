import { defineStore } from "pinia";

export const useAuthStore = defineStore("auth", {
  state: () => ({
    accessToken: null as string | null,
    userId: null as string | null,
  }),
  getters: {
    isLoggedIn: (state) => !!state.accessToken,
  },
  actions: {
    loginSuccess(token: string, userId: string) {
      this.accessToken = token;
      this.userId = userId;
    },
    logout() {
      this.accessToken = null;
      this.userId = null;
    },
  },
});
