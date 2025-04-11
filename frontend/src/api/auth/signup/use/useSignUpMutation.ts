import { useMutation } from "@tanstack/vue-query";
import { signUpApi } from "@/api/auth/signup/signUpApi";
import { CreateUserDto } from "@/types/auth/userDto";

export function useSignUpMutation() {
  return useMutation({
    mutationFn: (createUserData: CreateUserDto) =>
      signUpApi.signUp(createUserData),
  });
}
