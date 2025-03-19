import http from "data/k6/http";
import {sleep} from "k6";

export let options = {
  vus: 10,
  iterations: 10,
};

const BASE_URL = 'http://localhost:8080/api/v1';
const COUPON_ID = 1;
const USER_ID = 5; // Use a fixed user ID for all requests

export default function () {
  let url = `${BASE_URL}/orders`;

  let orderRequest = {
    userId: USER_ID,
    issuedCouponId: COUPON_ID,
    products: [
      {productId: 1, quantity: 2},
      {productId: 2, quantity: 1},
      {productId: 3, quantity: 5},
    ],
  };

  let res = http.post(url, JSON.stringify(orderRequest), {
    headers: {"Content-Type": "application/json"},
  });

  if (res.status === 400) {
    let errorResponse = res.json();
    console.warn(
        `User ${USER_ID} failed to create an order: ${errorResponse.message}`);
  }

  sleep(1);
}
