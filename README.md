# 모의 주식 사이트 개발기

[모의 투자 사이트 개발기 (1)](https://happyzodiac.tistory.com/110)

[모의 투자 사이트 개발기 (2)](https://happyzodiac.tistory.com/115)

[모의 투자 사이트 개발기 (3)](https://happyzodiac.tistory.com/119)

# 주요 내용

- [실시간 시세 데이터 가공 및 관리](https://happyzodiac.tistory.com/117)
- [주문 동시성 제어 방법 비교](https://happyzodiac.tistory.com/116)
- [WebSocket 에서 JWT 토큰 처리 방법](https://happyzodiac.tistory.com/118)
- [BigDecimal 을 통한 부동소수점 부정확성 극복](https://happyzodiac.tistory.com/109)
- [Java 언어에서의 동기화 방법 비교](https://happyzodiac.tistory.com/107)

# 기능 및 화면

## (1) 홈 화면

![image](https://github.com/user-attachments/assets/8b12d4c5-8d80-4a7b-a0c6-dd65f85c82ee)

- 실시간 금액 데이터 반영

## (2) 종목 골라보기

![image](https://github.com/user-attachments/assets/c70f3506-f99e-40f5-93ae-3440a9179944)

- 필터링 검색 및 거래 금액 순 정렬
- 검색 종목 실시간 데이터 반영

## (3) 차트 및 거래소

![image](https://github.com/user-attachments/assets/21d76841-6ab1-44f1-aff0-a9643b222330)

- 백엔드 서버로부터 가공된 과거 캔들 및 실시간 캔들 데이터로 구성
- 주문 시 동시성 제어 처리

## (4) 계좌

![image](https://github.com/user-attachments/assets/95f1c5e7-6799-4991-b909-733e57dfe657)

- 실시간 평가 금액 계산
