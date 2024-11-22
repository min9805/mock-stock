package com.mock.investment.stock.domain.stock.dto;

import lombok.Data;
import lombok.NoArgsConstructor; /**
 * 주식 코드 요청 DTO from Bybit
 *
 * {
 *   "retCode": 0,
 *   "retMsg": "OK",
 *   "result": {
 *     "category": "spot",
 *     "list": [
 *       {
 *         "symbol": "BTCUSDT",
 *         "baseCoin": "BTC",
 *         "quoteCoin": "USDT",
 *         "innovation": "0",
 *         "status": "Trading",
 *         "marginTrading": "utaOnly",
 *         "lotSizeFilter": {
 *           "basePrecision": "0.000001",
 *           "quotePrecision": "0.00000001",
 *           "minOrderQty": "0.000048",
 *           "maxOrderQty": "71.73956243",
 *           "minOrderAmt": "1",
 *           "maxOrderAmt": "4000000"
 *         },
 *         "priceFilter": {
 *           "tickSize": "0.01"
 *         },
 *         "riskParameters": {
 *           "limitParameter": "0.02",
 *           "marketParameter": "0.02"
 *         }
 *       },
 *       ...
 *       ]
 */

@Data
@NoArgsConstructor
public class BybitResponse {
	private int retCode;
	private String retMsg;
	private BybitResult result;
}
