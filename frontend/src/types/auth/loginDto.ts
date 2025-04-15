// src/types/dto.ts

export interface LoginUserDto {
  userId: string;
  userPassword: string;
}

export interface User {
  userId: string;
  nickname: string;
  gender: string;
  birthDate: string;
  role: string;
}

export interface LoginResponseDto {
  success: boolean;
  accessToken: string;
  refreshToken: string;
  message: string;
  user: User;
}
