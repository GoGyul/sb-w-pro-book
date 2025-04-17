// src/composables/board/mvboard/useMvBoardListQuery.ts

import { useMutation, useQuery } from "@tanstack/vue-query";
import { mvBoardApi } from "@/api/board/movie/mvBoardApi";
import {
  MvBoardListResponseDto,
  MvBoardDto,
} from "@/types/board/mvboard/mvBoardListResponseDto";

export function useMvBoardListQuery() {
  return useQuery<MvBoardListResponseDto[]>({
    queryKey: ["mvBoardList"],
    queryFn: mvBoardApi.getMvBoardList,
  });
}

export function useMvBoardMutation() {
  return useMutation({
    mutationFn: (mvBoardDto: MvBoardDto) => mvBoardApi.postMvBoard(mvBoardDto),
  });
}
