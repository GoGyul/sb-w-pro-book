import { useMutation } from "@tanstack/vue-query";
import { logoutApi } from "@/api/auth/logout/logoutApi";

export function useLogoutMutation() {
  return useMutation({
    mutationFn: (accessToken: string) => logoutApi.logout(accessToken),
  });
}
