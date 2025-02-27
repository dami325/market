import http from "k6/http";
import {sleep} from "k6";

export let options = {
  vus: 100,         // 100명의 가상 사용자
  iterations: 100,  // 각 VU가 한 번씩 실행
};

const BASE_URL = 'http://localhost:8080/api/v1';
const COUPON_ID = 1;

export default function () {
  const userId = __VU; // 각 VU마다 고유한 userId 할당

  // 1. 쿠폰 발급 요청
  let couponUrl = `${BASE_URL}/coupons/${COUPON_ID}?userId=${userId}`;
  let couponRes = http.post(couponUrl, null, {
    headers: {"Content-Type": "application/json"},
  });

  sleep(10);

  if (couponRes.status === 400) {
    let errorResponse = couponRes.json();
    console.warn(
        `User ${userId} failed to get a coupon: ${errorResponse.message}`);
    return; // 쿠폰 발급 실패 시 이후 요청 중단
  }

  // 2. 주문 생성 요청 (발급받은 쿠폰 사용)
  let orderUrl = `${BASE_URL}/orders`;
  let orderRequest = {
    userId: userId,
    products: [
      {productId: 1, quantity: 2},
      {productId: 2, quantity: 1},
      {productId: 3, quantity: 5},
    ],
  };

  let orderRes = http.post(orderUrl, JSON.stringify(orderRequest), {
    headers: {"Content-Type": "application/json"},
  });

  if (orderRes.status === 400) {
    let errorResponse = orderRes.json();
    console.warn(
        `User ${userId} failed to create an order: ${errorResponse.message}`);
    return; // 주문 생성 실패 시 이후 요청 중단
  }

  // 주문 응답에서 orderId 추출 (응답 형식에 따라 수정 필요)
  let orderData = orderRes.json();
  let orderId = orderData.orderId;  // orderId가 없으면 임시로 userId 사용

  // 3. 결제 요청
  let paymentUrl = `${BASE_URL}/payment`;
  let payRequest = {
    userId: userId,
    orderId: orderId,
  };

  let payRes = http.post(paymentUrl, JSON.stringify(payRequest), {
    headers: {"Content-Type": "application/json"},
  });

  // 결제 결과 로깅 (필요 시 응답 체크)
  if (payRes.status === 400) {
    let errorResponse = payRes.json();
    console.warn(
        `User ${userId} failed to process payment: ${errorResponse.message}`);
  }

  sleep(1);
}
