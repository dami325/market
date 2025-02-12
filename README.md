## E-Commerce

<hr>

현업에서 요구되는 복잡도를 고려하여, e커머스의 핵심 기능(잔액 충전/조회, 상품 조회, 선착순 쿠폰 발급, 주문/결제, 데이터 플랫폼 전송)을 클린 아키텍처 원칙에 따라
구현하고, 동시성 이슈와 단위·통합 테스트를 통해 실무에 준하는 품질과 유지보수성을 확보하는 것을 목표로 합니다.

<hr>

<details>
<summary>마일스톤</summary>
    
### 프로젝트 기간

- 2024-12-29 ~ 2025-01-16 (총 16 MD)
- 
### 총 소요 기간

총 16 MD (Man-days)

1. **설계 및 문서 작성 (2024-12-31)**
    - UML 작성, ERD 설계
2. **API 명세 및 Mock API 작성 (2025-01-03)**
    - 잔액 충전/조회 API, 상품 조회, 선착순 쿠폰 기능, 주문/결제, 상위 상품 조회
3. **테스트 환경 구성 (2025-01-04)**
    - 테스트 환경(테스트컨테이너 등) 구축 및 설정
4. **도메인 개발 (2025-01-09)**
    - 회원, 쿠폰, 상품, 결제, 주문 도메인
5. **인프라 개발 (2025-01-16)**
    - Redis, Kafka 설정 및 개발
6. **프로젝트 배포 (2025-01-17)**
    - PR 문서 작성, 최종 점검

```mermaid
gantt
    title E 커머스 서비스 프로젝트
    dateFormat YYYY-MM-DD

    section 설계 및 문서 작성
        UML 작성: a1, 2024-12-29, 1d
        ERD 설계: a2, 2024-12-30, 1d
        설계 및 문서 작성 완료: milestone, 2024-12-31, 0d

    section API 명세 및 Mock API 작성
        잔액 충전/조회 API: a3, 2024-12-31, 1d
        상품 조회: a4, 2025-01-01, 1d
        선착순 쿠폰 기능: a5, 2025-01-01, 1d
        주문/결제: a6, 2025-01-02, 1d
        상위 상품 조회: a7, 2025-01-02, 1d
        API 명세 및 Mock API 작성 완료: milestone, 2025-01-03, 0d

    section 테스트 환경 구성
        테스트 환경 구성: a8, 2025-01-03, 1d
        테스트 환경 구성 완료: milestone, 2025-01-04, 0d

    section 도메인 개발
        회원 도메인 개발: b1, 2025-01-04, 1d
        쿠폰 도메인 개발: b2, 2025-01-05, 1d
        상품 도메인 개발: b3, 2025-01-06, 1d
        결제 도메인 개발: b4, 2025-01-07, 1d
        주문 도메인 개발: b5, 2025-01-08, 1d
        도메인 개발 완료: milestone, 2025-01-09, 0d

    section 인프라 개발
        인프라 개발 (레디스, 카프카): c1, 2025-01-09, 7d
        인프라 개발 완료: milestone, 2025-01-16, 0d

    section 기타
        PR 문서 작성: d1, 2025-01-16, 1d
        프로젝트 완료: milestone, 2025-01-17, 0d
```

</details>

<details>
<summary>시퀀스 다이어그램</summary>

# 요구사항 기반 전체 시퀀스 다이어그램

아래 시퀀스 다이어그램은 **E 커머스 서비스의 주요 요구사항**에 따른 **도메인 간 상호작용**을 시각화한 것입니다.

**회원(사용자), 지갑, 쿠폰, 상품, 결제, 주문** 등 주요 도메인이 REST API 기반으로 어떻게 연동되는지 한눈에 파악할 수 있습니다.

### 주요 기능

- **사용자 잔액 충전 및 조회**
- **상품 정보 조회**
- **선착순 쿠폰 발급 및 조회**
- **주문 및 결제 처리**
- **데이터 플랫폼으로의 주문 정보 전송**

```mermaid
sequenceDiagram
    actor 사용자 as 사용자
    participant 쿠폰 as 쿠폰
    participant 상품 as 상품
    participant 주문 as 주문
    participant 결제 as 결제
    participant 지갑 as 지갑
    사용자 ->>+ 지갑: 잔액 조회 요청 (사용자 ID)
    지갑 ->> 지갑: 사용자 잔액 조회
    지갑 -->> 사용자: 잔액 정보 반환
    사용자 ->>+ 지갑: 잔액 충전 요청 (사용자 ID, 충전 금액)
    지갑 ->> 지갑: 잔액 업데이트 (+충전 금액)
    지갑 ->> 지갑: 충전 내역 저장
    지갑 -->> 사용자: 충전 완료 메시지
    사용자 ->>+ 상품: 일반 상품 조회 요청
    상품 ->> 상품: 상품 정보 조회 (ID, 이름, 가격, 잔여수량)
    상품 -->> 사용자: 상품 정보 반환
    사용자 ->>+ 상품: 상위 상품 조회 요청
    상품 ->> 상품: 최근 3일간 주문 데이터 조회
    상품 ->> 상품: 상위 5개 상품 선정
    상품 -->> 사용자: 상위 상품 정보 반환 (상품ID, 이름, 판매량)
    사용자 ->>+ 쿠폰: 선착순 쿠폰 정보 조회 (사용자 ID)
    쿠폰 -->> 사용자: 쿠폰 정보 반환 (쿠폰ID, 남은 수량 등)
    사용자 ->>+ 쿠폰: 쿠폰 신청 (사용자 ID)
    쿠폰 ->> 쿠폰: 쿠폰 상태 검증 (사용 가능 여부 확인)
    쿠폰 ->> 쿠폰: 발행량 +1
    쿠폰 ->> 쿠폰: 쿠폰 발급 내역 저장
    쿠폰 -->> 사용자: 200 OK (쿠폰 발행 완료 메시지)
    사용자 ->>+ 주문: 주문 및 결제 요청 (사용자 ID, 상품ID, 수량, 쿠폰ID 등)
    주문 ->> 주문: 주문 정보 저장
    주문 -->> 결제: 주문 정보 전달
    alt 결제 성공
        결제 ->>+ 지갑: 사용자 잔액 조회 (사용자 ID)
        지갑 -->> 결제: 사용자 잔액 정보 반환
        결제 ->>+ 쿠폰: 쿠폰 유효성 검증 및 할인 금액 확인 (사용자 ID, 쿠폰ID)
        쿠폰 -->> 결제: 할인 금액 반환
        결제 ->>+ 주문: 주문 정보 검증
        결제 ->>+ 지갑: 사용자 잔액 차감 (사용자 ID, 차감 금액)
        결제 ->>+ 상품: 상품 잔여 수량 차감 (상품ID, 수량)
        결제 ->> 결제: 결제 정보 저장
        결제 ->> 주문: 결제 상태 업데이트 (결제 완료)
        결제 ->> 결제: 주문 정보 전송
        결제 -->> 사용자: 결제 성공 메시지
    else 결제 실패
        결제 ->> 주문: 결제 상태 업데이트 (결제 실패)
        결제 -->> 사용자: 결제 실패 메시지
    end
```

</details>

<details>
<summary>테이블 설계</summary>
# ERD

```mermaid
erDiagram

%% =========================
%% 1. 테이블 정의
%% =========================
    coupon {
        bigint id PK "쿠폰 고유 ID"
        datetime(6) created_at "생성 일시"
        datetime(6) updated_at "수정 일시"
        decimal_10_2 discount_amount "할인 금액"
        datetime(6) end_date "쿠폰 만료일"
        int issued_quantity "쿠폰 발급 수량"
        varchar(255) name "쿠폰 이름"
        int total_quantity "쿠폰 총 수량"
    }

    product {
        bigint id PK "상품 고유 ID"
        datetime(6) created_at "생성 일시"
        datetime(6) updated_at "수정 일시"
        varchar(255) name "상품 이름"
        decimal_10_2 price "상품 가격"
        int stock_quantity "상품 재고 수량"
    }

    user_point {
        bigint id PK "포인트 고유 ID"
        datetime(6) created_at "생성 일시"
        datetime(6) updated_at "수정 일시"
        decimal_10_2 balance "잔액"
    }

    user {
        bigint id PK "사용자 고유 ID"
        datetime(6) created_at "생성 일시"
        datetime(6) updated_at "수정 일시"
        varchar(255) email "이메일"
        varchar(255) phone_number "전화번호"
        varchar(255) username "사용자 이름"
        bigint point_id FK "user_point.id"
    }

    user_coupon {
        bigint id PK "쿠폰 이력 고유 ID"
        datetime(6) created_at "생성 일시"
        datetime(6) updated_at "수정 일시"
        datetime(6) issued_at "쿠폰 발급 일시"
        datetime(6) used_at "쿠폰 사용 일시"
        bigint coupon_id FK "coupon.id"
        bigint user_id FK "user.id"
    }

    user_point_history {
        bigint id PK
        datetime(6) created_at "생성 일시"
        datetime(6) updated_at "수정 일시"
        decimal_10_2 amount "거래 금액"
        varchar(255) description "거래 설명"
        varchar(255) transaction_type "거래 유형 (충전, 차감)"
        bigint user_point_id FK "user_point.id"
    }

    orders {
        bigint id PK "주문 이력 고유 ID"
        datetime(6) created_at "생성 일시"
        datetime(6) updated_at "수정 일시"
        decimal_10_2 discount_amount "할인 받은 금액"
        varchar(255) status "주문 상태 : 재고 소진 대기, 결제 대기, 결제 완료 ,주문 취소"
        decimal_10_2 total_price "총 결제 금액"
        bigint coupon_id FK "coupon.id (nullable)"
        bigint user_id FK "user.id"
    }

    payment {
        bigint id PK "결제 고유 ID"
        datetime(6) created_at "생성 일시"
        datetime(6) updated_at "수정 일시"
        decimal_10_2 amount "결제 금액"
        tinyint failure_reason "결제 실패 원인"
        varchar(255) payment_status "결제 상태 : 성공 , 실패"
        bigint order_id FK "orders.id"
    }

    order_detail {
        bigint id PK "주문 상세 ID"
        datetime(6) created_at "생성 일시"
        datetime(6) updated_at "수정 일시"
        int quantity "주문 수량"
        decimal_10_2 total_price "총 주문 금액"
        bigint order_id FK "orders.id"
        bigint product_id FK "product.id"
    }

%% =========================
%% 2. 테이블 간 관계 정의
%% =========================

%% user - user_point (1:1)
    user ||--|| user_point: ""
%% user_point_history - user_point (N:1)
    user_point_history }o--|| user_point: ""
%% user_coupon - user (N:1)
    user_coupon }o--|| user: ""
%% user_coupon - coupon (N:1)
    user_coupon }o--|| coupon: ""
%% orders - user (N:1)
    orders }o--|| user: ""
%% orders - coupon (N:1, nullable)
    orders }o--|| coupon: ""
%% payment - orders (N:1)
    payment }o--|| orders: ""
%% order_detail - orders (N:1)
    order_detail }o--|| orders: ""
%% order_detail - product (N:1)
    order_detail }o--|| product: ""
```

## 테이블/관계 설명

1. **`user` / `user_point`**
    - 사용자(`user`) 테이블은 포인트(`user_point`) 테이블을 참조(`point_id`)하여 1:1 관계를 형성합니다.
    - 한 명의 사용자는 하나의 포인트 계정(잔액 관리)을 갖습니다.
2. **`user_point_history`**
    - `user_point_history`는 **거래 내역**을 저장하는 테이블입니다.
    - `user_point_id`로 `user_point`를 참조하며, 사용자 포인트에 대한 충전·차감 기록을 저장합니다(다대일).
3. **`user_coupon`**
    - 쿠폰 발급 이력과 사용 이력을 관리하는 테이블입니다.
    - `coupon_id`로 `coupon`을, `user_id`로 `user`를 참조하며, 여러 사용자가 여러 쿠폰을 각각 발급·사용할 수 있습니다.
4. **`orders`**
    - 주문 이력을 저장하는 테이블로, 한 사용자(`user_id`)가 여러 주문을 가질 수 있습니다(다대일).
    - 옵션으로 `coupon_id`를 참조하며, 쿠폰을 사용하지 않은 경우 `NULL`이 가능합니다.
5. **`payment`**
    - 결제 정보를 저장하는 테이블로, 결제 상태(`payment_status`)와 실패 원인(`failure_reason`)을 함께 관리합니다.
    - 각 결제(`payment`)는 주문(`orders.id`)을 참조해 결제 대상을 명시합니다.
6. **`order_detail`**
    - 주문 상세 정보 테이블로, 주문(`order_id`)과 상품(`product_id`)을 참조합니다.
    - 한 주문에 여러 상품을 담을 수 있으므로, 주문-주문상세는 1:N 관계입니다.
7. **`coupon`**
    - 쿠폰의 메타정보(쿠폰 이름, 총 수량, 발급 수량, 할인 금액 등)을 저장합니다.
    - 유효기간(`end_date`)이 지나면 사용 불가능한 것으로 간주됩니다.
8. **`product`**
    - 상품 정보를 담고 있으며, `stock_quantity` 필드를 통해 재고 수량을 관리합니다.
    
</details>

<details>
<summary>API Swagger</summary>

![API Swagger Image](https://github.com/user-attachments/assets/78a4d9d8-5450-490b-99b6-b7db15d00da5)

</details>

<details>
<summary>동시성 제어</summary>
    
# E-Commerce 프로젝트의 주요 동시성 제어 방식 분석

현재 진행 중인 E-Commerce 프로젝트에서 여러 동시성 문제를 효과적으로 해결하기 위해 다양한 동시성 제어 방식을 도입하고, 이를 성능을 기준으로 분석할 계획입니다.

# 1. 주요 동시성 이슈

---

### 1. **선착순 쿠폰 발급**

- 발급 개수가 제한된 쿠폰의 동시 발급 요청

### 2. **결제 처리**

- 동시에 같은 상품 주문에 대한 결제를 진행할 때 상품 재고 차감

### 3. **포인트 충전 / 차감**

- 특정 유저의 포인트 충전과 차감이 동시에 진행될 경우

# 2. 동시성 제어 방식

---

분석해 볼 주요 동시성 제어 방식은 3가지로 아래와 같습니다.

1. 비관적 락(Pessimistic Lock)
2. 낙관적 락(Optimistic Lock)
3. Redis와 Redisson 라이브러리를 활용한 Pub/Sub 기반 락

# 3. 테스트 환경

---

- 테스트는 local 환경에서 진행할 예정이며 `@Profile("local")`에서만 부하 테스트를 진행할 데이터(유저, 포인트, 상품, 쿠폰, 주문 등)들을 미리 세팅해 두었습니다.

테스트 데이터 초기화

```java
@Component
@Profile("local")
@RequiredArgsConstructor
public class LocalDataInitializer {

    private final LocalDummyData localDummyData;

    @PostConstruct
    public void init() {
        localDummyData.init();
    }
}
```

# 4. 테스트 도구 및 방법론

---

**테스트 도구**

- `K6`를 활용하여 동시성 테스트를 수행하고, `p90 ~ p99.9` 지표를 기준으로 성능을 분석.
- K6는 부하 테스트를 수행할 수 있는 오픈소스 도구로, 고성능 분산 테스트가 가능하며, Grafana와의 통합을 통해 시각화할 수 있음.
    
    [Grafana k6 |  Grafana k6 documentation](https://grafana.com/docs/k6/latest/)
    
- 분석 지표
    - 응답 시간(Response Time)
    - 초당 처리량(RPS, Requests Per Second)
    - 실패율(Error Rate)
    - 성공률(Success Rate)

# 5. 테스트 시나리오 및 분석

---

동시성 제어 방식의 성능을 분석할 테스트 시나리오는 다음과 같습니다.

### **1) 선착순 쿠폰 발급 테스트**

- 시나리오
    - 비관적 락 / 낙관적 락(재시도x) / 낙관적 락(재시도o) / redisson
    - 100명의 사용자가 하나의 쿠폰에 대해 발급 요청을 동시에 전송.
    - 100개의 재고에 대해 모든 요청이 성공해야함.

쿠폰 발급

```java
@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;

    @Transactional
    public void issueACoupon(Long couponId, Long userId) {
        Coupon coupon = couponRepository.getCouponWithLock(couponId);
        coupon.issuedCoupon(userId);
    }
}
```

### **비관적 락 (Pessimistic Lock) 테스트 결과 분석**

```bash
data_received..................: 7.3 kB 1.2 kB/s
data_sent......................: 16 kB  2.6 kB/s
http_req_blocked...............: avg=7.26ms  min=5ms      med=7ms    max=8.99ms p(50)=7ms    p(90)=8.99ms   p(95)=8.99ms   p(99)=8.99ms p(99.9)=8.99ms
http_req_connecting............: avg=3.26ms  min=1ms      med=2.99ms max=5.99ms p(50)=2.99ms p(90)=4.99ms   p(95)=5.99ms   p(99)=5.99ms p(99.9)=5.99ms
http_req_duration..............: avg=2.76s   min=396.54ms med=3.02s  max=5.17s  p(50)=3.02s  p(90)=4.64s    p(95)=4.94s    p(99)=5.1s   p(99.9)=5.16s
{ expected_response:true }...: avg=2.76s   min=396.54ms med=3.02s  max=5.17s  p(50)=3.02s  p(90)=4.64s    p(95)=4.94s    p(99)=5.1s   p(99.9)=5.16s
http_req_failed................: 0.00%  0 out of 100
http_req_receiving.............: avg=98.32µs min=0s       med=0s     max=1.68ms p(50)=0s     p(90)=517.54µs p(95)=534.03µs p(99)=1ms    p(99.9)=1.61ms
http_req_sending...............: avg=60.09µs min=0s       med=0s     max=1ms    p(50)=0s     p(90)=0s       p(95)=996.84µs p(99)=1ms    p(99.9)=1ms
http_req_tls_handshaking.......: avg=0s      min=0s       med=0s     max=0s     p(50)=0s     p(90)=0s       p(95)=0s       p(99)=0s     p(99.9)=0s
http_req_waiting...............: avg=2.76s   min=396.35ms med=3.02s  max=5.17s  p(50)=3.02s  p(90)=4.64s    p(95)=4.94s    p(99)=5.1s   p(99.9)=5.16s
http_reqs......................: 100    16.182417/s
iteration_duration.............: avg=3.77s   min=1.4s     med=4.03s  max=6.17s  p(50)=4.03s  p(90)=5.65s    p(95)=5.95s    p(99)=6.11s  p(99.9)=6.17s
iterations.....................: 100    16.182417/s
vus............................: 5      min=5        max=100
vus_max........................: 100    min=100      max=100
```

- **응답 시간 (Response Time)**
    - 평균 `2.76초`, 최대 `5.17초`로 요청 처리에 지연 발생.
    - P(50)=3.02s → 50%의 요청은 3초 내 처리되었으나, 상위 10%(P90=4.64s)부터 응답 시간이 증가하며, 상위 1%(P99=5.1s)는 처리 시간이 더 길어짐.
- **초당 처리량 (RPS, Requests Per Second)**
    - 평균 `16.18 RPS`
- **실패율 (Error Rate)**
    - 실패율 `0%`
- **성공률 (Success Rate)**
    - 성공률 `100%`

### **낙관적 락 (Optimistic Lock) + 재시도 X 테스트 결과 분석**

```bash
data_received..................: 24 kB  10 kB/s
data_sent......................: 16 kB  7.0 kB/s
http_req_blocked...............: avg=3.8ms    min=2ms      med=4ms      max=5ms   p(50)=4ms      p(90)=5ms   p(95)=5ms    p(99)=5ms    p(99.9)=5ms
http_req_connecting............: avg=1.97ms   min=1ms      med=2ms      max=3ms   p(50)=2ms      p(90)=3ms   p(95)=3ms    p(99)=3ms    p(99.9)=3ms
http_req_duration..............: avg=823.1ms  min=425.94ms med=773.26ms max=1.26s p(50)=773.26ms p(90)=1.19s p(95)=1.26s  p(99)=1.26s  p(99.9)=1.26s
 { expected_response:true }...: avg=791.3ms  min=425.94ms med=766.83ms max=1.22s p(50)=766.83ms p(90)=1.12s p(95)=1.17s  p(99)=1.21s  p(99.9)=1.22s
http_req_failed................: 87.00% 87 out of 100
http_req_receiving.............: avg=335.34µs min=0s       med=0s       max=3.7ms p(50)=0s       p(90)=992µs p(95)=1.12ms p(99)=1.84ms p(99.9)=3.51ms
http_req_sending...............: avg=250.19µs min=0s       med=0s       max=1ms   p(50)=0s       p(90)=1ms   p(95)=1ms    p(99)=1ms    p(99.9)=1ms
http_req_tls_handshaking.......: avg=0s       min=0s       med=0s       max=0s    p(50)=0s       p(90)=0s    p(95)=0s     p(99)=0s     p(99.9)=0s
http_req_waiting...............: avg=822.51ms min=424.94ms med=772.76ms max=1.26s p(50)=772.76ms p(90)=1.19s p(95)=1.26s  p(99)=1.26s  p(99.9)=1.26s
http_reqs......................: 100    44.033454/s
iteration_duration.............: avg=1.82s    min=1.43s    med=1.77s    max=2.27s p(50)=1.77s    p(90)=2.2s  p(95)=2.26s  p(99)=2.27s  p(99.9)=2.27s
iterations.....................: 100    44.033454/s
vus............................: 27     min=27        max=100
vus_max........................: 100    min=100       max=100

```

- **응답 시간 (Response Time)**
    - 평균 `823.1ms`, 최대 `1.26초`로 빠른 응답 속도.
- **초당 처리량 (RPS, Requests Per Second)**
    - 평균 `44.03 RPS`로, 비관적 락 대비 처리량이 **약 2.7배 증가.**
    - 그러나 처리량 증가는 높은 충돌 발생으로 이어져 실질적 효과 제한.
- **실패율 (Error Rate)**
    - 실패율 `87%` (100건 중 87건 실패)로, 동시 요청 시 충돌 빈도가 매우 높음.
    - 트랜잭션 경합이 심각하며, 재시도 로직 도입이 필수적.
- **성공률 (Success Rate)**
    - 성공률 `13%`
    - 재시도 전략 없이는 안정적인 처리가 어려움.

### **낙관적 락 (Optimistic Lock) + 재시도(1초 간격, 최대 3회) 테스트 결과 분석**

```bash
data_received..................: 21 kB  4.2 kB/s
data_sent......................: 16 kB  3.2 kB/s
http_req_blocked...............: avg=5.29ms   min=4ms     med=5ms   max=7ms    p(50)=5ms   p(90)=6ms      p(95)=6ms    p(99)=7ms    p(99.9)=7ms
http_req_connecting............: avg=2.15ms   min=999.1µs med=2ms   max=4ms    p(50)=2ms   p(90)=3ms      p(95)=3ms    p(99)=4ms    p(99.9)=4ms
http_req_duration..............: avg=2.83s    min=508.2ms med=2.97s max=3.96s  p(50)=2.97s p(90)=3.85s    p(95)=3.91s  p(99)=3.94s  p(99.9)=3.96s
 { expected_response:true }...: avg=1.88s    min=508.2ms med=1.61s max=3.96s  p(50)=1.61s p(90)=3.56s    p(95)=3.81s  p(99)=3.93s  p(99.9)=3.96s
http_req_failed................: 72.00% 72 out of 100
http_req_receiving.............: avg=343.8µs  min=0s      med=0s    max=3.38ms p(50)=0s    p(90)=997.88µs p(95)=1.19ms p(99)=3.01ms p(99.9)=3.34ms
http_req_sending...............: avg=309.68µs min=0s      med=0s    max=999µs  p(50)=0s    p(90)=999µs    p(95)=999µs  p(99)=999µs  p(99.9)=999µs
http_req_tls_handshaking.......: avg=0s       min=0s      med=0s    max=0s     p(50)=0s    p(90)=0s       p(95)=0s     p(99)=0s     p(99.9)=0s
http_req_waiting...............: avg=2.83s    min=508.2ms med=2.96s max=3.96s  p(50)=2.96s p(90)=3.85s    p(95)=3.91s  p(99)=3.94s  p(99.9)=3.96s
http_reqs......................: 100    20.117727/s
iteration_duration.............: avg=3.84s    min=1.51s   med=3.97s max=4.96s  p(50)=3.97s p(90)=4.86s    p(95)=4.92s  p(99)=4.94s  p(99.9)=4.96s
iterations.....................: 100    20.117727/s
vus............................: 50     min=50        max=100
vus_max........................: 100    min=100       max=100

```

- **응답 시간 (Response Time)**
    - 평균 `3.45초`, 최대 `4.49초`로 재시도 적용 후 처리 시간이 증가.
    - P(50)=3.63s → 50%의 요청이 3.63초 내 처리되었으며, 상위 10%(P90=4.39s), 상위 1%(P99=4.49s)에서 응답 시간이 증가.
- **초당 처리량 (RPS, Requests Per Second)**
    - 평균 `18.20 RPS`로 재시도 적용 후 처리량이 다소 감소.
- **실패율 (Error Rate)**
    - 실패율 `70%` (100건 중 70건 실패)로, 재시도 적용에도 충돌이 지속적으로 발생.
- **성공률 (Success Rate)**
    - 성공률 `30%`로, 일부 요청만 성공적으로 처리됨.

### **Redisson 기반 Redis Pub/Sub 락 테스트 결과 분석**

```bash
data_received..................: 7.3 kB 1.1 kB/s
data_sent......................: 16 kB  2.5 kB/s
http_req_blocked...............: avg=3ms      min=1ms      med=3ms   max=4.51ms p(50)=3ms   p(90)=4.26ms   p(95)=4.26ms   p(99)=4.26ms p(99.9)=4.48ms
http_req_connecting............: avg=425.95µs min=0s       med=0s    max=2.99ms p(50)=0s    p(90)=999.5µs  p(95)=1ms      p(99)=2ms    p(99.9)=2.89ms
http_req_duration..............: avg=4.44s    min=762.16ms med=5.26s max=5.4s   p(50)=5.26s p(90)=5.3s     p(95)=5.34s    p(99)=5.36s  p(99.9)=5.4s
{ expected_response:true }...: avg=4.44s    min=762.16ms med=5.26s max=5.4s   p(50)=5.26s p(90)=5.3s     p(95)=5.34s    p(99)=5.36s  p(99.9)=5.4s
http_req_failed................: 0.00%  0 out of 100
http_req_receiving.............: avg=174.74µs min=0s       med=0s    max=1.39ms p(50)=0s    p(90)=636.46µs p(95)=890.72µs p(99)=1.39ms p(99.9)=1.39ms
http_req_sending...............: avg=119.89µs min=0s       med=0s    max=1.99ms p(50)=0s    p(90)=998.5µs  p(95)=998.5µs  p(99)=1.01ms p(99.9)=1.89ms
http_req_tls_handshaking.......: avg=0s       min=0s       med=0s    max=0s     p(50)=0s    p(90)=0s       p(95)=0s       p(99)=0s     p(99.9)=0s
http_req_waiting...............: avg=4.44s    min=761.64ms med=5.26s max=5.4s   p(50)=5.26s p(90)=5.3s     p(95)=5.34s    p(99)=5.36s  p(99.9)=5.4s
http_reqs......................: 100    15.597744/s
iteration_duration.............: avg=5.44s    min=1.76s    med=6.26s max=6.4s   p(50)=6.26s p(90)=6.3s     p(95)=6.34s    p(99)=6.36s  p(99.9)=6.4s
iterations.....................: 100    15.597744/s
vus............................: 66     min=66       max=100
vus_max........................: 100    min=100      max=100

```

- **응답 시간 (Response Time)**
    - 평균 `4.44초`, 최대 `5.4초`로, 락 획득 및 처리에 시간이 소요됨.
    - P(50)=5.26s → 50%의 요청이 5.26초 내 처리되었으며, 상위 10%(P90=5.3s), 상위 1%(P99=5.36s)에서도 비슷한 처리 시간이 소요됨.
    - 전체적으로 일정한 응답 시간이 유지되고 있으며, 락 경합으로 인한 추가적인 지연은 크지 않음.
- **초당 처리량 (RPS, Requests Per Second)**
    - 평균 `15.59 RPS`로 비관적 락이나 낙관적 락 대비 준수한 처리량을 보임.
- **실패율 (Error Rate)**
    - 실패율 `0%`
- **성공률 (Success Rate)**
    - 성공률 `100%`

---

### **2) 결제 처리 테스트**

- 시나리오
    - 비관적 락 / 낙관적 락
    - 100명의 사용자가 동시에 동일한 상품 3개에 대해 각 10개씩 주문한 상태에서 결제 요청을 전송
    - 상품 A, B, C의 재고는 각각 1000개로 설정
    - 각각의 사용자는 모두 결제에 성공해야 하며, 재고 차감도 정확히 이루어져야 함

결제

```java
@Component
@RequiredArgsConstructor
public class PaymentFacade {

    private final PaymentService paymentService;
    private final ProductService productService;
    private final PointService pointService;
    private final OrderService orderService;
    private final DataPlatform dataPlatform;

    @Transactional
    public void processOrderPayment(Long userId, Long orderId) {
        Order order = orderService.getCompleteOrder(orderId);
        productService.quantitySubtract(order.getProductQuantityMap());
        Payment payment = paymentService.pay(orderId, order.getTotalAmount());
        pointService.usePoints(userId, payment.getTotalAmount());
        dataPlatform.publish(orderId, payment.getId());
    }
}
```

동시성 제어 로직

- 상품 재고 차감 - productService.quantitySubtract(order.getProductQuantityMap());
- 유저 포인트 차감 - pointService.usePoints(userId, payment.getTotalAmount());

### **비관적 락 (Pessimistic Lock) 테스트 결과 분석**

```bash
data_received..................: 7.3 kB 1.0 kB/s
data_sent......................: 17 kB  2.4 kB/s
http_req_blocked...............: avg=4.23ms   min=1.61ms   med=3.9ms  max=6.39ms  p(50)=3.9ms  p(90)=5.62ms   p(95)=5.74ms   p(99)=6.24ms   p(99.9)=6.37ms
http_req_connecting............: avg=2.13ms   min=0s       med=2.19ms max=3.27ms  p(50)=2.19ms p(90)=2.76ms   p(95)=3.12ms   p(99)=3.27ms   p(99.9)=3.27ms
http_req_duration..............: avg=3.37s    min=588.81ms med=3.36s  max=6.16s   p(50)=3.36s  p(90)=5.6s     p(95)=5.9s     p(99)=6.11s    p(99.9)=6.16s
 { expected_response:true }...: avg=3.37s    min=588.81ms med=3.36s  max=6.16s   p(50)=3.36s  p(90)=5.6s     p(95)=5.9s     p(99)=6.11s    p(99.9)=6.16s
http_req_failed................: 0.00%  0 out of 100
http_req_receiving.............: avg=79.03µs  min=0s       med=0s     max=589µs   p(50)=0s     p(90)=514.36µs p(95)=527.45µs p(99)=533.26µs p(99.9)=583.42µs
http_req_sending...............: avg=192.49µs min=0s       med=0s     max=765.2µs p(50)=0s     p(90)=517.9µs  p(95)=620.42µs p(99)=654.32µs p(99.9)=754.11µs
http_req_tls_handshaking.......: avg=0s       min=0s       med=0s     max=0s      p(50)=0s     p(90)=0s       p(95)=0s       p(99)=0s       p(99.9)=0s
http_req_waiting...............: avg=3.37s    min=588.14ms med=3.36s  max=6.16s   p(50)=3.36s  p(90)=5.6s     p(95)=5.89s    p(99)=6.11s    p(99.9)=6.15s
http_reqs......................: 100    13.943314/s
iteration_duration.............: avg=4.38s    min=1.59s    med=4.37s  max=7.16s   p(50)=4.37s  p(90)=6.6s     p(95)=6.9s     p(99)=7.12s    p(99.9)=7.16s
iterations.....................: 100    13.943314/s
vus............................: 4      min=4        max=100
vus_max........................: 100    min=100      max=100

```

- **응답 시간 (Response Time)**
    - 평균 응답 시간은 `3.37초`로, 최대 응답 시간은 `6.16초` 로 요청 처리에 지연 발생.
    - P(50)=3.36s → 50%의 요청은 3.36초 이내에 처리되었으나, 상위 10%(P90=5.6s)부터 응답 시간이 증가하며, 상위 1%(P99=6.11s)는 처리 시간이 더 길어짐
- **초당 처리량 (RPS, Requests Per Second)**
    - 평균 `13.94 RPS`
- **실패율 (Error Rate)**
    - 실패율`0%`
- **성공률 (Success Rate)**
    - 성공률 `100%`지만, 처리 지연시간이 누적됨.

### **낙관적 락 (Optimistic Lock) + 재시도(1초 간격, 최대 3회) 테스트 결과 분석**

```bash
data_received..................: 20 kB  3.8 kB/s
data_sent......................: 17 kB  3.3 kB/s
http_req_blocked...............: avg=5.55ms   min=3.64ms   med=5.71ms   max=7.58ms p(50)=5.71ms   p(90)=6.57ms p(95)=6.57ms p(99)=6.75ms p(99.9)=7.49ms
http_req_connecting............: avg=2.72ms   min=1.63ms   med=2.63ms   max=5.16ms p(50)=2.63ms   p(90)=3.64ms p(95)=4.17ms p(99)=5.16ms p(99.9)=5.16ms
http_req_duration..............: avg=3.4s     min=745.11ms med=3.56s    max=4.29s  p(50)=3.56s    p(90)=4.23s  p(95)=4.23s  p(99)=4.29s  p(99.9)=4.29s
{ expected_response:true }...: avg=2.58s    min=745.11ms med=2.64s    max=4.25s  p(50)=2.64s    p(90)=3.91s  p(95)=4.12s  p(99)=4.23s  p(99.9)=4.25s
http_req_failed................: 69.00% 69 out of 100
http_req_receiving.............: avg=309.06µs min=0s       med=0s       max=2.66ms p(50)=0s       p(90)=1.01ms p(95)=1.16ms p(99)=1.55ms p(99.9)=2.55ms
http_req_sending...............: avg=433.39µs min=0s       med=230.45µs max=1.51ms p(50)=230.45µs p(90)=1ms    p(95)=1.47ms p(99)=1.51ms p(99.9)=1.51ms
http_req_tls_handshaking.......: avg=0s       min=0s       med=0s       max=0s     p(50)=0s       p(90)=0s     p(95)=0s     p(99)=0s     p(99.9)=0s
http_req_waiting...............: avg=3.4s     min=742.45ms med=3.56s    max=4.29s  p(50)=3.56s    p(90)=4.23s  p(95)=4.23s  p(99)=4.29s  p(99.9)=4.29s
http_reqs......................: 100    18.849934/s
iteration_duration.............: avg=4.41s    min=1.75s    med=4.57s    max=5.3s   p(50)=4.57s    p(90)=5.24s  p(95)=5.24s  p(99)=5.3s   p(99.9)=5.3s
iterations.....................: 100    18.849934/s
vus............................: 24     min=24        max=100
vus_max........................: 100    min=100       max=100

```

- **응답 시간 (Response Time)**
    - 평균 응답 시간은 `3.4초`로, 최대 응답 시간은 `4.29초`로 요청 처리에 지연 발생.
    - P(50)=3.56s → 50%의 요청은 3.56초 이내에 처리, 상위 10%(P90=4.23s)부터 상위 1%(P99=4.29s)는 비슷하게 유지됨.
- **초당 처리량 (RPS, Requests Per Second)**
    - 평균 `18.85 RPS`.
- **실패율 (Error Rate)**
    - 실패율`69%`
- **성공률 (Success Rate)**
    - 성공률`31%`

---

### **3) 포인트 충전/차감 테스트**

- 시나리오
    - 비관적 락 / 낙관적 락(재시도o) / redisson
    - 특정 사용자 `userId`를 대상으로, 100번의 동시 요청을 번갈아 충전/차감 전송.
    - 모든 충전 및 차감이 정합성 있게 적용되어야 함.

포인트 충전 차감

```java
@Service
@RequiredArgsConstructor
public class PointService {

  private final PointRepository pointRepository;

  @Transactional
  public void chargePoint(Long userId, BigDecimal amount) {
    pointRepository.getPointByUserIdWithLock(userId)
        .charge(amount);
  }

  @Transactional
  public void usePoints(Long userId, BigDecimal totalAmount) {
    pointRepository.getPointByUserIdWithLock(userId)
        .subtract(totalAmount);
  }
}
```

### **비관적 락 (Pessimistic Lock) 테스트 결과 분석**

```bash
data_received..................: 15 kB 985 B/s
data_sent......................: 36 kB 2.4 kB/s
http_req_blocked...............: avg=829.11µs min=0s       med=0s     max=5.09ms   p(50)=0s     p(90)=2.67ms   p(95)=3.44ms   p(99)=4.82ms   p(99.9)=5.07ms
http_req_connecting............: avg=213.43µs min=0s       med=0s     max=2.18ms   p(50)=0s     p(90)=591.3µs  p(95)=1ms      p(99)=1.35ms   p(99.9)=2.01ms
http_req_duration..............: avg=5.32s    min=489.14ms med=6.38s  max=7.39s    p(50)=6.38s  p(90)=7.21s    p(95)=7.32s    p(99)=7.34s    p(99.9)=7.38s
{ expected_response:true }...: avg=5.32s    min=489.14ms med=6.38s  max=7.39s    p(50)=6.38s  p(90)=7.21s    p(95)=7.32s    p(99)=7.34s    p(99.9)=7.38s
http_req_failed................: 0.00% 0 out of 200
http_req_receiving.............: avg=94.19µs  min=0s       med=0s     max=694.5µs  p(50)=0s     p(90)=515.54µs p(95)=526.97µs p(99)=559.25µs p(99.9)=670.69µs
http_req_sending...............: avg=47.75µs  min=0s       med=0s     max=909.59µs p(50)=0s     p(90)=4.12µs   p(95)=505.49µs p(99)=909.59µs p(99.9)=909.59µs
http_req_tls_handshaking.......: avg=0s       min=0s       med=0s     max=0s       p(50)=0s     p(90)=0s       p(95)=0s       p(99)=0s       p(99.9)=0s
http_req_waiting...............: avg=5.32s    min=488.67ms med=6.38s  max=7.39s    p(50)=6.38s  p(90)=7.21s    p(95)=7.32s    p(99)=7.34s    p(99.9)=7.38s
http_reqs......................: 200   13.499094/s
iteration_duration.............: avg=11.65s   min=8.45s    med=11.59s max=14.8s    p(50)=11.59s p(90)=14.12s   p(95)=14.39s   p(99)=14.7s    p(99.9)=14.79s
iterations.....................: 100   6.749547/s
vus............................: 13    min=13       max=100
vus_max........................: 100   min=100      max=100

```

- **응답 시간 (Response Time)**
    - 평균 응답 시간은 `5.32초`, 최대 응답 시간은 `7.39초`로 전반적으로 높은 응답 지연이 발생.
    - 50%의 요청(P50)은 `6.38초` 이내에 처리되었으며, 상위 10%(P90)는 `7.21초`로 큰 차이는 없음.
- **초당 처리량 (RPS, Requests Per Second)**
    - 평균 `13.49 RPS`
- **실패율 (Error Rate)**
    - 실패율 `0%`
- **성공률 (Success Rate)**
    - 성공률 `100%`

### **낙관적 락 (Optimistic Lock) + 재시도(1초 간격, 최대 3회) 테스트 결과 분석**

```bash
data_received..................: 42 kB  5.5 kB/s
data_sent......................: 36 kB  4.7 kB/s
http_req_blocked...............: avg=4.3ms    min=0s       med=4ms     max=11ms   p(50)=4ms     p(90)=9ms      p(95)=9.99ms p(99)=10.01ms p(99.9)=11ms
http_req_connecting............: avg=1.07ms   min=0s       med=997.9µs max=6ms    p(50)=997.9µs p(90)=2.63ms   p(95)=3.64ms p(99)=6ms     p(99.9)=6ms
http_req_duration..............: avg=2.74s    min=125.44ms med=2.85s   max=3.98s  p(50)=2.85s   p(90)=3.73s    p(95)=3.86s  p(99)=3.92s   p(99.9)=3.97s
{ expected_response:true }...: avg=1.81s    min=125.44ms med=1.8s    max=3.93s  p(50)=1.8s    p(90)=3.14s    p(95)=3.69s  p(99)=3.89s   p(99.9)=3.93s
http_req_failed................: 72.00% 144 out of 200
http_req_receiving.............: avg=292.26µs min=0s       med=0s      max=5.62ms p(50)=0s      p(90)=739.52µs p(95)=1.04ms p(99)=3.49ms  p(99.9)=5.22ms
http_req_sending...............: avg=374.29µs min=0s       med=0s      max=2ms    p(50)=0s      p(90)=1ms      p(95)=2ms    p(99)=2ms     p(99.9)=2ms
http_req_tls_handshaking.......: avg=0s       min=0s       med=0s      max=0s     p(50)=0s      p(90)=0s       p(95)=0s     p(99)=0s      p(99.9)=0s
http_req_waiting...............: avg=2.74s    min=125.44ms med=2.85s   max=3.98s  p(50)=2.85s   p(90)=3.73s    p(95)=3.86s  p(99)=3.92s   p(99.9)=3.97s
http_reqs......................: 200    26.121487/s
iteration_duration.............: avg=6.5s     min=2s       med=6.62s   max=7.65s  p(50)=6.62s   p(90)=7.55s    p(95)=7.55s  p(99)=7.65s   p(99.9)=7.65s
iterations.....................: 100    13.060743/s
vus............................: 43     min=43         max=100
vus_max........................: 100    min=100        max=100

```

- **응답 시간 (Response Time)**
    - 평균 응답 시간은 `2.74초`, 최대 `3.98초`
    - 50%의 요청(P50)은 `2.85초` 이내에 처리되었으며, 상위 10%(P90)는 `3.73초`, 상위 1%(P99)는 `3.92초`
- **초당 처리량 (RPS, Requests Per Second)**
    - 평균 `26.12 RPS`
- **실패율 (Error Rate)**
    - 전체 요청의 `72%`가 실패(144건/200건)하여 심각한 장애 발생.
    - 응답이 늦어지거나 재시도 로직 적용에도 불구하고 요청이 정상적으로 처리되지 않는 문제 발생.
- **성공률 (Success Rate)**
    - 성공률 `28%`

### **Redisson 기반 Redis Pub/Sub 락 테스트 결과 분석**

```bash
data_received..................: 15 kB 803 B/s
data_sent......................: 36 kB 2.0 kB/s
http_req_blocked...............: avg=3.62ms   min=0s       med=1.28ms max=13.16ms p(50)=1.28ms p(90)=10ms     p(95)=11ms     p(99)=11.01ms  p(99.9)=12.96ms
http_req_connecting............: avg=445.86µs min=0s       med=0s     max=3ms     p(50)=0s     p(90)=1.24ms   p(95)=2ms      p(99)=2ms      p(99.9)=2.8ms
http_req_duration..............: avg=6.57s    min=709.34ms med=8.01s  max=9.07s   p(50)=8.01s  p(90)=8.63s    p(95)=8.66s    p(99)=8.85s    p(99.9)=9.06s
{ expected_response:true }...: avg=6.57s    min=709.34ms med=8.01s  max=9.07s   p(50)=8.01s  p(90)=8.63s    p(95)=8.66s    p(99)=8.85s    p(99.9)=9.06s
http_req_failed................: 0.00% 0 out of 200
http_req_receiving.............: avg=57.06µs  min=0s       med=0s     max=565µs   p(50)=0s     p(90)=505.41µs p(95)=522.34µs p(99)=537.31µs p(99.9)=561.75µs
http_req_sending...............: avg=424.18µs min=0s       med=0s     max=9.16ms  p(50)=0s     p(90)=1ms      p(95)=1ms      p(99)=6.01ms   p(99.9)=8.73ms
http_req_tls_handshaking.......: avg=0s       min=0s       med=0s     max=0s      p(50)=0s     p(90)=0s       p(95)=0s       p(99)=0s       p(99.9)=0s
http_req_waiting...............: avg=6.57s    min=709.32ms med=8.01s  max=9.07s   p(50)=8.01s  p(90)=8.63s    p(95)=8.66s    p(99)=8.85s    p(99.9)=9.06s
http_reqs......................: 200   10.996321/s
iteration_duration.............: avg=14.15s   min=10.25s   med=14.1s  max=18.18s  p(50)=14.1s  p(90)=17.38s   p(95)=17.74s   p(99)=18.02s   p(99.9)=18.16s
iterations.....................: 100   5.49816/s
vus............................: 2     min=2        max=100
vus_max........................: 100   min=100      max=100

```

- **응답 시간 (Response Time)**
    - 평균 **`6.57`초**, 최대 `**9.07`초**로 응답 지연이 크며, 50%의 요청이 **8.01초** 이상으로 처리됨.
- **초당 처리량 (RPS, Requests Per Second)**
    - 평균 **`10.99 RPS`**, 락 경쟁으로 처리 속도가 제한됨.
- **실패율 (Error Rate)**
    - 실패율 **`0%`**
- **성공률 (Success Rate)**
    - 성공률 **`100%`**

# 결론

---

**선착순 쿠폰 발급**의 경우, **Redisson 분산 락**이 가장 적절한 선택이라고 판단되었습니다. 

그 이유는 테스트 결과, 비관적 락이 다소 더 나은 응답 속도를 보였지만, 락을 대기하는 동안 데이터베이스(DB) 커넥션을 지속적으로 유지해야 합니다. 

트래픽이 증가할수록 DB의 커넥션 수가 병목 현상을 유발할 수 있으며, DB의 확장은 매우 어렵고 비용이 높습니다. 

반면, **Redis는 DB에 부하를 주지 않고, 수평적 확장이 용이**하기 때문에 높은 동시성을 요구하는 환경에서 적합하다 판단했습니다.

**상품 재고 차감**의 경우, 현재 결제 파사드(Facade) 구조에서는 분산 락 적용이 어렵다고 분석되었습니다. 

파사드 자체에 락을 적용하는 것은 불가능하며, **재고 차감 및 포인트 차감** 두 영역에 각각 락을 적용해야 합니다. 

하지만, 분산 락을 적용하려면 트랜잭션 시작 전에 락을 획득해야 하며, 이로 인해 **새로운 트랜잭션 생성 비용이 추가**됩니다. 

또한, **재고 차감이 성공했지만 포인트 차감이 실패할 경우**, 이미 커밋된 재고를 복구해야 하는 추가 작업이 필요합니다. 

이러한 점을 종합적으로 고려했을 때, 현재 구조에 가장 적합한 해결책은 **비관적 락을 적용하는 것**으로 결정하였습니다.

**포인트 충전**의 경우, 사용자가 동일한 요청을 여러 번 호출하는 상황에서는 충돌 발생 시 재시도 로직이 필요하지 않아 **낙관적 락**을 고려했습니다. 

그러나 테스트 결과, 특정 시나리오(예: 충전 및 차감이 동시에 이루어지는 경우)에서는 **모든 요청이 정확하게 처리되어야 하는 비즈니스 요구사항**이 존재했습니다. 

따라서, 선착순 쿠폰 발급과 마찬가지로 **Redisson 분산 락을 적용**하여 높은 정합성을 유지하도록 결정했습니다.

---

### **각 동시성 제어 방식의 장단점 분석**

테스트 결과를 통해 각 락의 장단점을 파악한 결과는 다음과 같습니다.

1. **비관적 락 (Pessimistic Lock)**
    - **장점:** 높은 정합성 보장, 모든 요청이 순차적으로 성공하도록 보장.
    - **단점:** 트래픽 증가 시 대기 시간이 증가하고, 응답 속도가 저하됨.
    - **적용 사례:** 반드시 성공해야 하는 비즈니스 로직, 재시도를 하기엔 비용이 많이 드는 작업(예: 결제 시 재고 차감).
2. **낙관적 락 (Optimistic Lock)**
    - **장점:** 애플리케이션 단에서 처리되어 빠른 응답 속도 제공.
    - **단점:** 동시성 충돌이 빈번할 경우, 성공할 때까지 재시도로 인해 성능이 저하될 수 있음.
    - **적용 사례:** 실패해도 재시도 로직이 필요없으며 빠른 응답이 더 중요한 작업(예: 좌석 예약).
3. **Redisson 분산 락 (Distributed Lock)**
    - **장점:** 비관적 락 수준의 정합성을 제공하며, DB 부하 없이 수평적 확장 가능.
    - **단점:** Redis 장애 발생 시 복구 전략이 복잡하며, 운영 및 학습 비용이 높음.
    - **적용 사례:** 확장성과 정합성이 동시에 요구되는 작업(예: 선착순 쿠폰, 포인트 충전/차감).

이러한 분석을 기반으로, **상품 재고 관리에는 비관적 락을 적용하고, 쿠폰 발급 및 포인트 충전과 같은 기능에는 Redisson 분산 락을 적용**하기로 결정했습니다.

</details>

<details>
<summary>Redis 캐시 전략 정리 및 성능 개선</summary>

# e커머스 서비스에서 Redis 캐싱을 활용한 성능 개선 분석 및 적용 방안

# 캐시란?

---

- 데이터의 원본보다 더 빠르고 효율적으로 액세스할 수 있는 임시 데이터 저장소를 의미합니다.

```mermaid
sequenceDiagram
    participant 사용자
    participant 캐시
    participant 원본 저장소

    사용자->>캐시: 요청 (데이터 A)
    캐시-->>사용자: 캐시된 데이터 A (있음)
    Note right of 사용자: 캐시에서 데이터를 반환 (원본 접근 없음)

    사용자->>캐시: 요청 (데이터 B)
    캐시-->>원본 저장소: 캐시에 없음, 원본 요청
    원본 저장소-->>캐시: 데이터 B 반환 후 캐시에 저장
    캐시-->>사용자: 캐시에 저장된 데이터 B 반환
    Note right of 사용자: 원본에서 가져온 후 캐시에 저장

```

- 위와 같은 과정을 거쳐 사용자가 동일한 정보를 반복적으로 액세스할때 원본이 아니라 캐시에서 데이터를 가지고 옴으로써 리소스를 줄일 수 있습니다.

# **캐시를 사용하면 좋은 경우**

---

1. 원본 데이터 저장소에서 데이터를 찾는 데 시간이 오래 걸리거나, 매번 계산이 필요한 경우
2. 데이터가 자주 조회되지만 변화가 적은 경우

애플리케이션에서 캐시의 궁극적인 목적은 사용자의 대기 시간을 단축하는 것입니다. 따라서 위 조건을 만족하더라도, 캐시에서 데이터를 가져오는 속도가 원본 데이터 저장소에서 직접 가져오는 것보다 빨라야 합니다.

# 레디스를 캐시로 사용할 때 주요 캐싱 전략 4가지

---

캐싱 전략은 캐싱되는 데이터의 유형과 데이터에 대한 액세스 패턴에 따라 다르기 때문에 서비스에 맞는 적절한 캐싱 전략을 선택하는 것이 중요합니다.

## 1. 읽기 전략(look aside)

---

데이터를 읽어갈 때 주로 사용하는 전략입니다.

애플리케이션이 레디스를 캐시로 사용할 때 데이터를 조회하는 과정에는 캐시 히트와 캐시 미스라는 용어가 나오는데, 그 과정은 아래와 같습니다.

### **캐시 히트 (Cache Hit)**

애플리케이션이 요청한 데이터가 캐시에 존재하는 경우 캐시에서 데이터를 읽어옵니다.

```mermaid
sequenceDiagram
    participant 애플리케이션
    participant 레디스
    participant 원본 데이터베이스

    애플리케이션->>레디스: 데이터 요청 (예: 사용자 정보)
    레디스-->>애플리케이션: 캐시된 데이터 반환 (Cache Hit)
    Note right of 애플리케이션: 원본 DB 접근 없이 빠르게 응답

```

### **캐시 미스 (Cache Miss)**

애플리케이션이 요청한 데이터가 캐시에 없어서 원본 데이터베이스에서 가져옵니다. 이후 애플리케이션은 이를 다시 캐시에 저장합니다.

```mermaid
sequenceDiagram
    participant 애플리케이션
    participant 레디스
    participant 원본 데이터베이스

    애플리케이션->>레디스: 데이터 요청 (예: 사용자 정보)
    레디스-->>애플리케이션: 캐시 없음 (Cache Miss)
    애플리케이션->>원본 데이터베이스: 원본 DB에서 데이터 조회
    원본 데이터베이스-->>애플리케이션: 데이터 반환
    애플리케이션->>레디스: 데이터 캐싱 (향후 요청 대비)
    레디스-->>애플리케이션: 응답 완료
    Note right of 애플리케이션: 다음 요청부터는 캐시에서 제공 가능

```

장점

- 레디스에 문제가 생겨 접근을 할 수 없는 상황이더라도 바로 서비스 장애로 이어지지 않고 데이터베이스에서 데이터를 가져올 수 있습니다.

단점

- 기존에 애플리케이션에서 레디스를 통해 데이터를 가져오는 연결이 많았으면, 모든 커넥션이 한꺼번에 원본 데이터베이스로 몰리기 때문에 애플리케이션의 성능에 영향을 미칠 수 있습니다.
- lazy loading 구조로 저장되기 때문에 애플리케이션은 데이터를 찾기 위해 레디스에 매번 먼저 접근해 캐시 미스 과정을 거쳐서 성능에 영향을 미칠 수 있습니다. (이를 방지 하기 위한 캐시 워밍 작업이 있는데, 캐시 워밍이란 데이터베이스에 저장된 데이터를 레디스로 밀어넣는 것을 말함)
- 캐시는 데이터베이스에 저장돼 있는 데이터를 단순히 복사해 온 값이기 때문에 원본 데이터와 동일한 값을 갖도록 유지하지 않으면 캐시 불일치(데이터 간 불일치)가 발생합니다.

## 2. 쓰기 전략(write through)

---

write through 방식은 데이터베이스에 업데이트 할 때마다 매번 캐시에도 데이터를 함께 업데이트 시키는 방식입니다.

```mermaid
sequenceDiagram
    participant 애플리케이션
    participant 레디스
    participant 원본 데이터베이스
    
    par 데이터 저장
        애플리케이션->>레디스: 데이터 저장 (Cache 업데이트)
        애플리케이션->>원본 데이터베이스: 데이터 저장 (DB 업데이트)
    end

```

장점

- 캐시는 항상 최신 데이터를 가지고 있을 수 있습니다.

단점

- 데이터는 매번 2개의 저장소에 저장돼야 하기 때문에 쓰기 비용이 많이 발생합니다.

## 3. 쓰기 전략(cache invalidation)

---

cache invalidation은 데이터베이스에 값을 업데이트 할 때마다 캐시에서는 데이터를 삭제하는 전략입니다.

```mermaid
sequenceDiagram
    par 데이터 저장
        애플리케이션->>레디스: 데이터 삭제 (Cache 삭제)
        애플리케이션->>원본 데이터베이스: 데이터 저장 (DB 업데이트)
    end

```

장점

- 레디스의 데이터 삭제는 데이터를 저장하는 것보다 훨씬 리소스를 적게 사용하기 때문에 write through의 단점인 캐시 불일치 해소를 위한 쓰기 비용을 줄일 수 있습니다.

단점

- 조회 시 캐시 미스 과정이 발생합니다.

## 4. 쓰기 전략(write behind[write back])

쓰기가 많이 발생하는 서비스라면 아래와 같이 write behind 방식을 적용해볼 수 있습니다.

```mermaid
sequenceDiagram
    participant 애플리케이션
    participant 레디스
    participant 원본 데이터베이스

    애플리케이션->>레디스: 데이터 저장 (Cache 업데이트)
    
    par 일정 시간마다 저장
        레디스->>원본 데이터베이스: 데이터 저장 (Write-Behind)
    end

```

위와 같이 먼저 데이터를 빠르게 접근할 수 있는 캐시에 업데이트 한 뒤 설정한 건수나 시간 간격에 따라 비동기적으로 데이터베이스에 저장하는 방식입니다.

장점

- 대용량 트래픽이 발생할 경우 실시간으로 원본 데이터베이스에 저장하지 않기 때문에 데이터베이스의 성능을 향상시켜 애플리케이션 전체의 성능도 향상시킬 수 있습니다.

단점

- 캐시에 문제가 생겨 데이터가 날라갈 경우 설정한 건수나 시간만큼의 데이터가 날라갈 수 있다는 위험이 있습니다.

# 캐시 스탬피드 현상

---

캐시 스탬피드는 동시 다발적인 캐시 미스로 인해 DB 부하가 급증하는 현상이며, 이를 방지하려면 TTL 최적화 및 사전 갱신 전략이 필요합니다.

```mermaid
stateDiagram
    [*] --> 애플리케이션1_요청대기
    [*] --> 애플리케이션2_요청대기

    애플리케이션1_요청대기 --> 레디스_조회 : 요청 전송
    애플리케이션2_요청대기 --> 레디스_조회 : 요청 전송

    레디스_조회 --> 캐시미스 : 캐시 없음
    캐시미스 --> 애플리케이션1_DB조회 : 동시에 DB 조회
    캐시미스 --> 애플리케이션2_DB조회 : 동시에 DB 조회

    애플리케이션1_DB조회 --> DB_부하증가
    애플리케이션2_DB조회 --> DB_부하증가

    DB_부하증가 --> 애플리케이션1_데이터저장 : 중복 읽기 완료
    DB_부하증가 --> 애플리케이션2_데이터저장 : 중복 읽기 완료

    애플리케이션1_데이터저장 --> 레디스_캐시업데이트
    애플리케이션2_데이터저장 --> 레디스_캐시업데이트

    레디스_캐시업데이트 --> 캐시_오버로드 : 중복 쓰기 발생

    캐시_오버로드 --> [*]

```

Redis를 캐시로 사용할 때 모든 키에 만료 시간을 설정하는 것이 일반적이지만, 특정 키가 만료되는 순간 다수의 클라이언트가 동시에 요청하면 캐시 미스가 발생하여 다음과 같은 문제가 생길 수 있습니다.

1. 캐시 미스로 인한 중복 읽기로 DB에 대량의 요청이 발생합니다.
2. 캐시 업데이트 과정에서 중복 읽기 만큼의 중복 쓰기가 발생합니다.
3. 캐시는 무거운 쿼리에 많이 활용되는데 무거운 쿼리가 동시에 실행되어 시스템 부하가 가중됩니다.

해결 방법

- 캐시 스탬피드를 방지하려면 만료 시간을 너무 짧게 설정하지 않고, 동시에 데이터가 만료되기 전에 미리 갱신(cache warming)하여 여러 요청이 한꺼번에 DB로 몰리는 상황을 방지하는 작업이 필요할 수 있습니다.

# E 커머스 서비스에서 Redis를 이용해 성능 개선할 수 있는 로직 분석

---

## 1. 상위 주문 상품 5개 조회

```java
public List<ProductResponse.Top5ProductDetails> getProductsTop5() {
    return queryFactory
            .select(new QProductResponse_Top5ProductDetails(
                    product.id,
                    product.name,
                    product.price,
                    product.stockQuantity,
                    orderDetail.quantity.sum()
            ))
            .from(orderDetail)
            .innerJoin(orderDetail.order, order)
            .innerJoin(product)
            .on(
                    product.id.eq(orderDetail.productId)
            )
            .where(
                    order.status.eq(Order.OrderStatus.ORDER_COMPLETE)
            )
            .groupBy(
                    product.id,
                    product.name,
                    product.price,
                    product.stockQuantity
            )
            .orderBy(orderDetail.quantity.sum().desc())
            .limit(5)
            .fetch();
}
```

### 문제점

1. 집계 연산 부하
    - `SUM(quantity)`를 통해 정렬하는 과정에서 전체 주문 데이터를 스캔해야 함
    - 데이터 양이 많아질수록 DB 부하 증가
2. 동시 요청 시 성능 저하
    - 많은 사용자가 상위 주문 상품 목록을 조회할 경우, 동일한 무거운 쿼리가 반복 실행됨
    - CPU 사용량 증가 및 DB 커넥션 과부하 발생 가능
3. 응답 속도 지연
    - 실시간으로 DB에서 주문량 상위 5개 상품을 조회할 경우 쿼리 실행 시간이 길어져 사용자 경험 저하

### 레디스 캐싱 선택 이유 및 예상 결과

DB 부하 감소

- 모든 요청이 DB로 가는 것이 아니라 캐시에서 처리
- 상위 주문 상품 목록 조회 트래픽이 증가해도 원본 DB 부하가 증가하지 않음

응답 속도 개선

- 요청 시 매번 무거운 쿼리를 실행 하지 않기 때문에 응답 속도 개선 효과

Redis의 빠른 읽기 성능 활용

- Redis는 메모리 기반으로 동작하여 읽기 속도가 매우 빠름

### 성능 개선을 위한 해결 방안 (Redis 캐싱 적용)

- Look-Aside 전략으로 Spring Cache 인터페이스를 활용한 캐싱
- 상위 주문 상품 목록은 생각보다 자주 변경되지 않으며, 실시간 데이터보다 빠른 응답이 중요
    - 자주 변경된다면 TTL을 더 짧게 적용하여 어느정도 해소 가능
- 캐시 워밍(Cache Warming) 으로 캐시 스탬피드 문제 방지

### 코드 구현

> RedisConfig.java
> 

```java
@Bean
public CacheManager redisCacheManager() {
  RedisCacheManager.RedisCacheManagerBuilder builder = RedisCacheManager
      .RedisCacheManagerBuilder
      .fromConnectionFactory(redisConnectionFactory());

  ObjectMapper objectMapper = new ObjectMapper();
  objectMapper.registerModule(new JavaTimeModule()); // LocalDateTime 지원
  objectMapper.activateDefaultTyping(
      objectMapper.getPolymorphicTypeValidator(),
      ObjectMapper.DefaultTyping.NON_FINAL
  );

  RedisCacheConfiguration configuration = RedisCacheConfiguration
      .defaultCacheConfig()
      .serializeKeysWith(
          RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
      .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(
          new GenericJackson2JsonRedisSerializer(objectMapper)))
      .entryTtl(Duration.ofMinutes(90));
  builder.cacheDefaults(configuration);
  return builder.build();
}
```

- **`objectMapper.activateDefaultTyping(...)`** 설정으로 `GenericJackson2JsonRedisSerializer` 를 사용한 리스트 자료 구조의 직렬화/역직렬화 문제를 방지
- **`.entryTtl(Duration.ofMinutes(90))`** 설정으로 캐시 TTL을 90분으로 지정

> ProductService.java
> 

```java
@Cacheable(value = "top5ProductDetails", key = "'top5'", cacheManager = "redisCacheManager")
@Transactional(readOnly = true)
public List<ProductResponse.Top5ProductDetails> getProductsTop5() {
  return productRepository.getProductsTop5();
}
```

- 서비스에서 @Cacheable을 활용해 지정한 redisCacheManager로 요청 결과를 캐싱

> RedisCacheRefresher.java
> 

```java
@Component
@RequiredArgsConstructor
public class RedisCacheRefresher {

  private final ProductQuerydslRepository productQuerydslRepository;
  private final CacheManager redisCacheManager;

  @Scheduled(fixedRate = 3600000)
  public void refreshTop5ProductsCache() {
    List<ProductResponse.Top5ProductDetails> updatedProducts = productQuerydslRepository.getProductsTop5();
    redisCacheManager.getCache("top5ProductDetails").put("top5", updatedProducts);
  }
}
```

- 1시간마다 TTL(1시간 30분)이 만료되기 전, repository를 통해 DB에서 데이터를 직접 읽어와 새로 갱신

> k6를 사용한 100번의 동시 요청 부하 테스트
> 

테스트 조건

- 100명의 동시 사용자가 요청
- 캐시 적용 전 vs 캐시 적용 후
- 요청당 응답 대기 시간(http_req_waiting) 비교

> 캐시 적용 전
> 

```java
http_req_waiting...............: avg=1.31s    min=377.91ms med=1.35s  max=2.27s  p(50)=1.35s  p(90)=2.14s    p(95)=2.21s   p(99)=2.27s   p(99.9)=2.27s
http_reqs......................: 100   43.669144/s
```

> 캐시 적용 후
> 

```java
http_req_waiting...............: avg=330.07ms min=323.99ms med=330.1ms  max=335.62ms p(50)=330.1ms  p(90)=333.28ms p(95)=333.77ms p(99)=335.11ms p(99.9)=335.57ms
http_reqs......................: 100   287.66821/s
```

- 캐시 적용 후 평균 응답 속도가 **약 4배(3.97배, 1310ms → 330.07ms) 향상**되었으며, 일반적인 요청(`p50`)에서는 **4.09배(1350ms → 330.1ms)**, 높은 부하(`p90`)에서는 **6.42배(2140ms → 333.28ms)**, 최악의 경우(`p99`)에도 **6.77배(2270ms → 335.11ms)** 개선되어 전반적인 성능이 크게 향상됨.

### 결론

개선 전

- 집계 연산으로 인해 DB 부하 발생
- 동시 요청 시 성능 저하 및 응답 지연

개선 후

- 상위 주문 상품 목록 데이터를 Redis에 1시간마다 캐싱하여 DB 부하를 줄이고 빠르게 응답

---

## 2. 선착순 쿠폰 발급 레디스 로직 이관 구상

> CouponService.java
> 

```java
  @DistributedLock(key = "'COUPON_' + #couponId")
  @Transactional
  public void issueACoupon(Long couponId, Long userId) {
    Coupon coupon = couponRepository.getCoupon(couponId);
    coupon.issuedCoupon(userId);
  }
  
  // Coupon.java issuedCoupon
  public void issuedCoupon(Long userId) {
    checkExpiry();
    if (userId == null) {
        throw new IllegalArgumentException("유효하지 않은 사용자.");
    }
    if (this.issuedQuantity == this.totalQuantity) {
        throw new IllegalArgumentException("발급 수량 소진");
    }
    if (this.issuedCoupons.stream().anyMatch(issuedCoupon -> issuedCoupon.getUserId().equals(userId))) {
        throw new IllegalArgumentException("같은 쿠폰은 하나만 발급 가능합니다.");
    }
    this.issuedQuantity++;

    IssuedCoupon issuedCoupon = createIssuedCoupon(this, userId);
    this.issuedCoupons.add(issuedCoupon);
}
```

### 문제점

1. 분산락으로 인한 성능 저하
    - 기존 방식은 `@DistributedLock`을 사용하여 동시성 제어를 진행했으나, 고트래픽(수십만 건 이상) 환경에서 성능 병목이 발생할 가능성이 높음
    - 분산락이 설정되어 있어, 쿠폰 발급 시 동시 요청이 많아질 경우 시스템 처리 속도가 저하될 수 있음
2. 중복 발급 방지 미흡
    - 기존 로직에서는 `issuedCoupons` 리스트를 순회하며 사용자별 중복 발급을 방지하나, 데이터베이스에 직접 접근하는 방식으로 인해 성능 저하 가능성이 존재함

### 레디스 캐싱 선택 이유 및 예상 결과

레디스 활용 기대 효과

- 고성능 처리
    - Redis의 `Sorted Set(ZSET)`을 활용하여 선착순 발급을 보장하고, `Set(SADD)`을 이용해 중복 발급을 방지함으로써 레디스의 부하를 줄일 수 있음.
- 비동기 처리
    - 쿠폰 발급 요청을 바로 데이터베이스에서 처리하는 것이 아니라, Redis에서 먼저 처리한 후 일정 주기로 배치 작업을 통해 데이터베이스에 반영.

### 성능 개선을 위한 해결 방안 (Redis 캐싱 적용)

**1. 쿠폰 선착순 요청 처리 (Sorted Set 활용)**

- `Sorted Set(ZSET)` 자료구조를 이용하여 쿠폰 발급 요청을 저장하고, 요청 순서에 따라 발급자를 선정.
- `score` 값을 요청 시각(timestamp)으로 설정하여 요청 순서 보장.

### **의사 코드**

```
ZADD coupon-135135-requests <timestamp> "user1"
ZADD coupon-135135-requests <timestamp> "user2"
ZADD coupon-135135-requests <timestamp> "user3"

ZPOPMIN coupon-135135-requests 2 // 상위 2명 발급
// user1, user3
```

- `ZADD` - 쿠폰 발급 요청자를 추가 (사용자 식별자, 요청 시간)
- `ZPOPMIN` - 특정 개수만큼 발급할 사용자 조회 및 제거

**2. 쿠폰 중복 발급 방지 (Set 활용)**

- `Set(SADD)` 자료구조를 활용하여 이미 발급된 사용자를 저장.
- `SISMEMBER`를 이용해 특정 사용자가 이미 쿠폰을 발급받았는지 확인.

### **의사 코드**

```
SADD coupon-135135-issued "user1"
SADD coupon-135135-issued "user2"

SCARD coupon-135135-issued // 현재 발급 개수 조회
SISMEMBER coupon-135135-issued "user1" // 이미 발급받은 사용자 확인
```

- `SADD` - 신규 발급 사용자 추가
- `SCARD` - 현재 발급된 쿠폰 개수 조회
- `SISMEMBER` - 특정 사용자의 발급 여부 확인

**3. 쿠폰 발급 방식 (비동기 처리)**

- 요청이 오면 **Redis에서 선착순 요청을 등록**하고, 별도의 **스케줄러가 주기적으로 요청을 처리하여 DB에 반영**하는 구조.
- 이렇게 하면 쿠폰 발급 요청이 몰려도 DB 부하 없이 처리가 가능하며, 순차적인 발급이 보장됨.

### **스케줄러 로직 (의사 코드)**

1. DB에서 쿠폰의 남은 수량 조회.
2. Redis `ZPOPMIN`을 통해 해당 개수만큼 요청자를 가져옴.
3. Redis `SADD`를 사용해 쿠폰 발급 완료 처리.
4. DB에 실제로 쿠폰 발급 내역을 반영.
5. **남은 요청자들에게 발급 실패 메시지 전송.**

```java
int availableCoupons = getAvailableCouponsFromDB(); // DB에서 발급 가능 수량 조회
List<String> selectedUsers = redis.zpopmin("coupon-135135-requests", availableCoupons); // 선착순 n명 선택
List<String> failedUsers = redis.zrange("coupon-135135-requests", 0, -1); // 남은 요청자 조회

for (String user : selectedUsers) {
    if (!redis.sismember("coupon-135135-issued", user)) { // 중복 발급 여부 체크
        redis.sadd("coupon-135135-issued", user); // 발급 완료 처리
        issueCouponInDB(user); // DB에 반영
    }
}

// 발급 실패한 사용자들에게 메시지 전송
for (String failedUser : failedUsers) {
    sendFailureMessage(failedUser);
}
```

### 기대 효과

- **데이터베이스 부하 감소**
    - 실시간 트랜잭션을 줄이고, 비동기 처리로 부하를 최소화함.
- **고속 처리 가능**
    - Redis를 활용하여 수십만 건 이상의 트래픽도 원활히 처리 가능.
- **선착순 보장**
    - `Sorted Set`을 통해 요청 순서를 보장하며, 스케줄러를 활용해 정해진 수량만큼만 발급.
- **중복 발급 방지**
    - `Set`을 사용하여 사용자가 중복으로 발급받는 것을 방지.
- **발급 실패 사용자 대응**
    - 선착순에서 밀려난 사용자들에게 적절한 피드백 제공.

이러한 방식으로 Redis 기반으로 선착순 쿠폰 발급을 개선하면, 높은 트래픽 환경에서도 효율적인 처리가 가능할 것으로 예상됨.

# Reference

- [개발자를 위한 레디스 - 김가림](https://product.kyobobook.co.kr/detail/S000210785682)

</details>

