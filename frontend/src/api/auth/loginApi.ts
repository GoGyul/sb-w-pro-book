import apiClient from "../axios";
import { LoginUserDto, LoginResponseDto } from "@/types/auth/loginDto";

export const login = async (
  loginData: LoginUserDto
): Promise<LoginResponseDto> => {
  const response = await apiClient.post<LoginResponseDto>(
    "/auth/public/login",
    loginData
  );
  debugger;
  return response.data;
};
