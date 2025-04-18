import apiClient from "../../axios";
import { PageResponse } from "@/types/common/PageResponse";
import {
  MvBoardListResponseDto,
  MvBoardDto,
} from "@/types/board/mvboard/mvBoardListResponseDto";

export const mvBoardApi = {
  getMvBoardList: (
    page: number,
    size: number
  ): Promise<PageResponse<MvBoardListResponseDto>> => {
    return apiClient
      .get(`/board/movie/list?page=${page}&size=${size}`)
      .then((res) => res.data);
  },
  postMvBoard: (mvBoardDto: MvBoardDto): Promise<boolean> => {
    return apiClient
      .post<boolean>("/board/movie/write", mvBoardDto)
      .then((response) => response.data);
  },
};
