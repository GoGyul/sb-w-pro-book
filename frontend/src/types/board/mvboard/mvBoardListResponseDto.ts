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
}

export interface MvBoardDto {
  title: string;
  content: string;
  userId: string | null;
  category: string;
  movieTitle: string;
  rating: number | null;
}
