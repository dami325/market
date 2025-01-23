import http from "k6/http";
import {sleep} from "k6";

export let options = {
  vus: 100,
  iterations: 100,
};

const BASE_URL = 'http://localhost:8080/api/v1';

export default function () {
  let url = `${BASE_URL}/payment`;
  const userId = __VU;
  const orderId = __VU;

  let payRequest = {
    userId: userId,
    orderId: orderId,
  };

  let res = http.post(url, JSON.stringify(payRequest), {
    headers: {"Content-Type": "application/json"},
  });

  sleep(1);
}
