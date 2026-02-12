# Testcases (MVP)

| ID | Description | Inputs | Expected |
| --- | --- | --- | --- |
| S1-PERCENT | %쿠폰 적용 (cap 포함) | subtotal=59,000; shipping=3,000; rate=20; minSpend=50,000; cap=8,000 | discount=8,000; final=54,000 |
| S1-FIXED | 정액쿠폰 적용 | subtotal=59,000; shipping=3,000; amount=7,000; minSpend=40,000 | discount=7,000; final=55,000 |
| S2-SHIPPING | 배송쿠폰 적용 | subtotal=19,900; shipping=3,000; shippingDiscount=3,000; cap=3,000 | shipping fee=0; final=19,900; shipping discount=3,000 |
| S3-MINSPEND | 최소금액 미달 | subtotal=28,000; shipping=3,000; amount=6,000; minSpend=30,000 | applied=false; discount=0; final=31,000; reason=MIN_SPEND_NOT_MET |
| TOP3-ORDER | Top3 정렬 규칙 확인 | subtotal=10,000; shipping=3,000; P1=10%; P2=1,500; S=3,000 | finals: 8,500 -> 9,000 -> 10,000 |

## Edge Cases
| ID | Description | Inputs | Expected |
| --- | --- | --- | --- |
| E1-CAP-CLAMP | cap 초과 클램프 | subtotal=100,000; rate=50%; cap=10,000 | discount=10,000 |
| E2-BASE-CLAMP | base 초과 클램프 | subtotal=5,000; amount=10,000 | discount=5,000 |
| E3-SHIPPING-CLAMP | 배송비 초과 클램프 | shipping=2,500; shippingDiscount=5,000 | shipping fee=0 |
| E4-MINSPEND | minSpend 미달 | subtotal=29,000; minSpend=30,000 | applied=false; reason=MIN_SPEND_NOT_MET |
