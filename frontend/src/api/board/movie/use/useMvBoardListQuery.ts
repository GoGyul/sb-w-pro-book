// src/composables/board/mvboard/useMvBoardListQuery.ts
import { Ref, unref } from "vue";
import { useMutation, useQuery } from "@tanstack/vue-query";
import { mvBoardApi } from "@/api/board/movie/mvBoardApi";
import { PageResponse } from "@/types/common/PageResponse";
import {
  MvBoardListResponseDto,
  MvBoardDto,
  MvBoardDetailResponseDto,
} from "@/types/board/mvboard/mvBoardListResponseDto";

export function useMvBoardListQuery(page: Ref<number>, size: number) {
  return useQuery<PageResponse<MvBoardListResponseDto>>({
    queryKey: ["mvBoardList", page],
    queryFn: () => mvBoardApi.getMvBoardList(unref(page), size),
  });
}

// 영화 게시판 상세 조회 쿼리
export function useMvBoardDetailQuery(bno: number) {
  return useQuery<MvBoardDetailResponseDto>({
    queryKey: ["mvBoardDetail", bno],
    queryFn: () => mvBoardApi.getMvBoardDetail(bno),
  });
}

export function useMvBoardMutation() {
  return useMutation({
    mutationFn: (mvBoardDto: MvBoardDto) => mvBoardApi.postMvBoard(mvBoardDto),
  });
}
