## E-Commerce

<hr>

현업에서 요구되는 복잡도를 고려하여, e커머스의 핵심 기능(잔액 충전/조회, 상품 조회, 선착순 쿠폰 발급, 주문/결제, 데이터 플랫폼 전송)을 클린 아키텍처 원칙에 따라 구현하고, 동시성 이슈와 단위·통합 테스트를 통해 실무에 준하는 품질과 유지보수성을 확보하는 것을 목표로 합니다.

### 프로젝트 기간
- 2024-12-29 ~ 2025-01-16 (총 16 MD)

### 총 소요 기간
  총 16 MD (Man-days)

### 주요 마일스톤
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
   dateFormat  YYYY-MM-DD

   section 설계 및 문서 작성
      UML 작성                 :a1, 2024-12-29, 1d
      ERD 설계                 :a2, 2024-12-30, 1d
      설계 및 문서 작성 완료                 :milestone, 2024-12-31, 0d

   section API 명세 및 Mock API 작성
      잔액 충전/조회 API        :a3, 2024-12-31, 1d
      상품 조회                :a4, 2025-01-01, 1d
      선착순 쿠폰 기능          :a5, 2025-01-01, 1d
      주문/결제                :a6, 2025-01-02, 1d
      상위 상품 조회           :a7, 2025-01-02, 1d
      API 명세 및 Mock API 작성 완료             :milestone, 2025-01-03, 0d

   section 테스트 환경 구성
      테스트 환경 구성          :a8, 2025-01-03, 1d
      테스트 환경 구성 완료     :milestone, 2025-01-04, 0d

   section 도메인 개발
      회원 도메인 개발          :b1, 2025-01-04, 1d
      쿠폰 도메인 개발          :b2, 2025-01-05, 1d
      상품 도메인 개발          :b3, 2025-01-06, 1d
      결제 도메인 개발          :b4, 2025-01-07, 1d
      주문 도메인 개발          :b5, 2025-01-08, 1d
      도메인 개발 완료          :milestone, 2025-01-09, 0d

   section 인프라 개발
      인프라 개발 (레디스, 카프카) :c1, 2025-01-09, 7d
      인프라 개발 완료           :milestone, 2025-01-16, 0d

   section 기타
      PR 문서 작성              :d1, 2025-01-16, 1d
      프로젝트 완료             :milestone, 2025-01-17, 0d
```
<hr>

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

사용자->>+지갑: 잔액 조회 요청 (사용자 ID)
지갑->>지갑: 사용자 잔액 조회
지갑-->>사용자: 잔액 정보 반환

사용자->>+지갑: 잔액 충전 요청 (사용자 ID, 충전 금액)
지갑->>지갑: 잔액 업데이트 (+충전 금액)
지갑->>지갑: 충전 내역 저장
지갑-->>사용자: 충전 완료 메시지

사용자->>+상품: 일반 상품 조회 요청
상품->>상품: 상품 정보 조회 (ID, 이름, 가격, 잔여수량)
상품-->>사용자: 상품 정보 반환

사용자->>+상품: 상위 상품 조회 요청
상품->>상품: 최근 3일간 주문 데이터 조회
상품->>상품: 상위 5개 상품 선정
상품-->>사용자: 상위 상품 정보 반환 (상품ID, 이름, 판매량)

사용자->>+쿠폰: 선착순 쿠폰 정보 조회 (사용자 ID)
쿠폰-->>사용자: 쿠폰 정보 반환 (쿠폰ID, 남은 수량 등)

사용자->>+쿠폰: 쿠폰 신청 (사용자 ID)
쿠폰->>쿠폰: 쿠폰 상태 검증 (사용 가능 여부 확인)
쿠폰->>쿠폰: 발행량 +1
쿠폰->>쿠폰: 쿠폰 발급 내역 저장
쿠폰-->>사용자: 200 OK (쿠폰 발행 완료 메시지)

사용자->>+주문: 주문 및 결제 요청 (사용자 ID, 상품ID, 수량, 쿠폰ID 등)
주문->>주문: 주문 정보 저장
주문-->>결제: 주문 정보 전달
alt 결제 성공
    결제->>+지갑: 사용자 잔액 조회 (사용자 ID)
    지갑-->>결제: 사용자 잔액 정보 반환
    결제->>+쿠폰: 쿠폰 유효성 검증 및 할인 금액 확인 (사용자 ID, 쿠폰ID)
    쿠폰-->>결제: 할인 금액 반환
    결제->>+주문: 주문 정보 검증
    결제->>+지갑: 사용자 잔액 차감 (사용자 ID, 차감 금액)
    결제->>+상품: 상품 잔여 수량 차감 (상품ID, 수량)
    결제->>결제: 결제 정보 저장
    결제->>주문: 결제 상태 업데이트 (결제 완료)
    결제->>결제: 주문 정보 전송
    결제-->>사용자: 결제 성공 메시지
else 결제 실패
    결제->>주문: 결제 상태 업데이트 (결제 실패)
    결제-->>사용자: 결제 실패 메시지
end
```
<hr>

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
      bigint user_id   FK "user.id"
   }

   user_point_history {
      bigint id PK
      datetime(6) created_at "생성 일시"
      datetime(6) updated_at "수정 일시"
      decimal_10_2  amount "거래 금액"
      varchar(255) description "거래 설명"
      varchar(255) transaction_type "거래 유형 (충전, 차감)"
      bigint user_point_id FK "user_point.id"
   }

   orders {
      bigint id PK "주문 이력 고유 ID"
      datetime(6) created_at "생성 일시"
      datetime(6) updated_at "수정 일시"
      decimal_10_2  discount_amount "할인 받은 금액"
      varchar(255) status "주문 상태 : 재고 소진 대기, 결제 대기, 결제 완료 ,주문 취소"
      decimal_10_2 total_price "총 결제 금액"
      bigint coupon_id FK "coupon.id (nullable)"
      bigint user_id   FK "user.id"
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
      bigint order_id   FK "orders.id"
      bigint product_id FK "product.id"
   }

%% =========================
%% 2. 테이블 간 관계 정의
%% =========================

%% user - user_point (1:1)
   user ||--|| user_point : ""

%% user_point_history - user_point (N:1)
   user_point_history }o--|| user_point : ""

%% user_coupon - user (N:1)
   user_coupon }o--|| user : ""

%% user_coupon - coupon (N:1)
   user_coupon }o--|| coupon : ""

%% orders - user (N:1)
   orders }o--|| user : ""

%% orders - coupon (N:1, nullable)
   orders }o--|| coupon : ""

%% payment - orders (N:1)
   payment }o--|| orders : ""

%% order_detail - orders (N:1)
   order_detail }o--|| orders : ""

%% order_detail - product (N:1)
   order_detail }o--|| product : ""
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

<hr>

<details>
<summary>API Swagger</summary>
![api-swagger](https://github.com/user-attachments/assets/6a4419d8-09c0-4696-b537-32e878e031a8)
</details>
