import apiClient from "../../axios";
import { LoginUserDto, LoginResponseDto } from "@/types/auth/loginDto";

// mutation을 위한 함수는 데이터를 인자로 받는 형태로 정의
export const loginApi = {
  login: (loginData: LoginUserDto): Promise<LoginResponseDto> => {
    return apiClient
      .post<LoginResponseDto>("/auth/public/login", loginData)
      .then((response) => response.data);
  },
};
