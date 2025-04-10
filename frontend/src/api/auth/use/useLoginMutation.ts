// src/composables/auth/useLoginMutation.ts
import { useMutation } from "@tanstack/vue-query";
import { loginApi } from "@/api/auth/loginApi";
import { LoginUserDto } from "@/types/auth/loginDto";

export function useLoginMutation() {
  return useMutation({
    mutationFn: (loginData: LoginUserDto) => loginApi.login(loginData),
  });
}
