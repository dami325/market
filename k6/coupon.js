import http from "k6/http";
import {sleep} from "k6";

export let options = {
  vus: 500,
  iterations: 500,
};

const BASE_URL = 'http://localhost:8080/api/v1';
const COUPON_ID = 1;

export default function () {
  let userId = __VU;
  let url = `${BASE_URL}/coupons/${COUPON_ID}?userId=${userId}`;

  let res = http.post(url, null, {
    headers: {"Content-Type": "application/json"},
  });

  if (res.status === 400) {
    let errorResponse = res.json();
    console.warn(
        `User ${userId} failed to get a coupon: ${errorResponse.message}`);
  }

  sleep(1);
}
