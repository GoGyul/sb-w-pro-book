// src/types/dto.ts

export interface LoginUserDto {
  userId: string;
  userPassword: string;
}

export interface LoginResponseDto {
  success: boolean;
  accessToken: string;
  refreshToken: string;
  userId: string;
  message: string;
}
