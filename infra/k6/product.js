import http from "data/k6/http";

export let options = {
  vus: 100,
  duration: "10s",
  iterations: 100,
};

const BASE_URL = "http://localhost:8080/api/v1/products";

export default function () {
  // 상위 상품 조회 API 요청
  let response = http.get(`${BASE_URL}/rank`);
}
