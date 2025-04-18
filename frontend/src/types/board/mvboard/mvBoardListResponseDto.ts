// src/types/movie/movieDto.ts

export interface MvBoardListResponseDto {
  bno: number;
  title: string;
  content: string;
  userId: string;
  category: string;
  movieTitle: string;
  rating: number;
  views: number;
  regdate: string;
  updatedate: string;
  isDeleted: boolean;
  nickname: string;
}

export interface MvBoardDto {
  title: string;
  content: string;
  userId: string | null;
  category: string;
  movieTitle: string;
  rating: number | null;
}

export interface MvBoardDetailResponseDto {
  bno: number; // 게시글 번호
  title: string; // 제목
  content: string; // 내용
  userId: string; // 작성자 ID
  regdate: string; // 작성일 (ISO 날짜 문자열)
  updatedate: string; // 수정일 (ISO 날짜 문자열)
  category: string; // 카테고리 (예: "영화후기", "아무이야기" 등)
  movieTitle: string; // 영화 제목 (선택적 필드)
  rating: number; // 평점 (선택적 필드)
  isDeleted: number; // 삭제 여부
  views: number; // 조회수
  nickname: string;
}
