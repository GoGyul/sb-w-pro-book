import apiClient from "../../axios";
import { CreateUserDto, CreateUserResponseDto } from "@/types/auth/userDto";

// 회원가입 API
export const signUpApi = {
  signUp: (signUpData: CreateUserDto): Promise<CreateUserResponseDto> => {
    return apiClient
      .post<CreateUserResponseDto>("/auth/public/create", signUpData)
      .then((response) => response.data);
  },
};
