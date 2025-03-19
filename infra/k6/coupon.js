import http from "data/k6/http";
import {sleep} from "k6";

export let options = {
  vus: 500,
  iterations: 500,
};

const BASE_URL = 'http://localhost:8080/api/v1';
const COUPON_ID1 = 1;
const COUPON_ID2 = 2;

export default function () {
  let userId = __VU;
  let url1 = `${BASE_URL}/coupons/${COUPON_ID1}?userId=${userId}`;
  let url2 = `${BASE_URL}/coupons/${COUPON_ID2}?userId=${userId}`;
  let url3 = `${BASE_URL}/coupons?userId=${userId}`;

  let res1 = http.post(url1, null, {
    headers: {"Content-Type": "application/json"},
  });
  let res2 = http.post(url2, null, {
    headers: {"Content-Type": "application/json"},
  });
  let res3 = http.get(url3, null, {
    headers: {"Content-Type": "application/json"},
  });

  if (res1.status === 400) {
    let errorResponse = res1.json();
    console.warn(
        `User ${userId} failed to get a coupon: ${errorResponse.message}`);
  }

  if (res2.status === 400) {
    let errorResponse = res2.json();
    console.warn(
        `User ${userId} failed to get a coupon: ${errorResponse.message}`);
  }

  sleep(1);
}
