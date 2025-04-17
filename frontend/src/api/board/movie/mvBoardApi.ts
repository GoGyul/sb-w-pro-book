import apiClient from "../../axios";
import {
  MvBoardListResponseDto,
  MvBoardDto,
} from "@/types/board/mvboard/mvBoardListResponseDto";

export const mvBoardApi = {
  getMvBoardList: (): Promise<MvBoardListResponseDto[]> => {
    return apiClient
      .get<MvBoardListResponseDto[]>("/board/movie/list")
      .then((response) => response.data);
  },
  postMvBoard: (mvBoardDto: MvBoardDto): Promise<boolean> => {
    return apiClient
      .post<boolean>("/board/movie/write", mvBoardDto)
      .then((response) => response.data);
  },
};
