import apiClient from "../../axios";
import { LogoutResponseDto } from "@/types/auth/logoutDto";

// mutation을 위한 함수는 데이터를 인자로 받는 형태로 정의
export const logoutApi = {
  logout: (accessToken: string): Promise<LogoutResponseDto> => {
    return apiClient
      .post<LogoutResponseDto>(
        "/auth/public/logout",
        {}, // 비어있는 요청 본문 또는 필요한 데이터
        {
          headers: {
            Authorization: `Bearer ${accessToken}`,
          },
        }
      )
      .then((response) => response.data);
  },
};
