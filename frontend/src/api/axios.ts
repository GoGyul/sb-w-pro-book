// src/api/axios.ts
import axios from "axios";

const apiClient = axios.create({
  baseURL: "http://localhost:8080", // 기본 URL
  headers: {
    "Content-Type": "application/json",
  },
  withCredentials: true, // 필요시 쿠키 전송 허용
});

export default apiClient;
