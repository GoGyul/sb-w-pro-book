export interface CreateUserDto {
  userId: string;
  userPassword: string;
  nickname: string;
  gender: string | null;
  birthDate: string | null;
}

export interface CreateUserResponseDto {
  success: boolean;
  userId: string;
  message: string;
}
