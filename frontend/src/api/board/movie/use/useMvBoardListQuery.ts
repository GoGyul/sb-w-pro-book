// src/composables/board/mvboard/useMvBoardListQuery.ts
import { Ref, unref } from "vue";
import { useMutation, useQuery } from "@tanstack/vue-query";
import { mvBoardApi } from "@/api/board/movie/mvBoardApi";
import { PageResponse } from "@/types/common/PageResponse";
import {
  MvBoardListResponseDto,
  MvBoardDto,
} from "@/types/board/mvboard/mvBoardListResponseDto";

export function useMvBoardListQuery(page: Ref<number>, size: number) {
  return useQuery<PageResponse<MvBoardListResponseDto>>({
    queryKey: ["mvBoardList", page],
    queryFn: () => mvBoardApi.getMvBoardList(unref(page), size),
  });
}

export function useMvBoardMutation() {
  return useMutation({
    mutationFn: (mvBoardDto: MvBoardDto) => mvBoardApi.postMvBoard(mvBoardDto),
  });
}
