import http from "k6/http" // http test
import {sleep} from "k6" // sleep 기능 사용 시 추가 (sleep(n) -> 지정한 n 기간 동한 VU 실행을 일시 중지)
import {htmlReport} from "https://raw.githubusercontent.com/benc-uk/k6-reporter/main/dist/bundle.js"
import {textSummary} from "https://jslib.k6.io/k6-summary/0.0.1/index.js"; // K6 기본 포맷 불러오기

export let options = {

  vus: 10,          // 가상의 유저 수

  duration: '10s'   // 테스트 진행 시간

};

const BASE_URL = 'https://test.k6.io';
// k6 run base.js --summary-trend-stats="avg,min,med,max,p(90),p(95),p(99),p(99.9)"

export default function () {
  let userId = __VU;  // VU 번호를 id로 사용 (1 ~ 10)
  let getUrl = BASE_URL

  http.get(getUrl);
  console.log('userId', userId);

  sleep(1);

}

export function handleSummary(data) { // html 리포트

  return {
    "summary.html": htmlReport(data),
    stdout: textSummary(data, {indent: " ", enableColors: true}) // K6 기본 포맷 유지

  }

}
