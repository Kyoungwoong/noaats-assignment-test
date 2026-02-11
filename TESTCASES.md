# Testcases (MVP)

| ID | Description | Inputs | Expected |
| --- | --- | --- | --- |
| S1-PERCENT | %쿠폰 적용 (cap 포함) | subtotal=59,000; shipping=3,000; rate=20; minSpend=50,000; cap=8,000 | discount=8,000; final=54,000 |
| S1-FIXED | 정액쿠폰 적용 | subtotal=59,000; shipping=3,000; amount=7,000; minSpend=40,000 | discount=7,000; final=55,000 |
| S3-MINSPEND | 최소금액 미달 | subtotal=28,000; shipping=3,000; amount=6,000; minSpend=30,000 | applied=false; discount=0; final=31,000; reason=MIN_SPEND_NOT_MET |
