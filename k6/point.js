import http from "k6/http";
import {sleep} from "k6";

// 테스트 옵션 설정
export let options = {
  vus: 100,  // 동시 사용자 수
  iterations: 100,  // 총 실행 횟수
};

const BASE_URL = "http://localhost:8080/api/v1/points";
const USER_ID = 1;  // 테스트할 사용자 ID
const AMOUNT = 1000;  // 테스트할 금액

export default function () {
  let headers = {"Content-Type": "application/json"};

  // 잔액 충전 요청
  let rechargePayload = JSON.stringify({
    userId: USER_ID,
    amount: AMOUNT
  });

  let rechargeResponse = http.post(`${BASE_URL}/recharge`, rechargePayload,
      {headers});

  // 잔액 차감 요청
  let deductPayload = JSON.stringify({
    userId: USER_ID,
    amount: AMOUNT
  });

  let deductResponse = http.post(`${BASE_URL}/deduct`, deductPayload,
      {headers});

  sleep(1);
}
