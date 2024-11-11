package com.mock.investment.stock.global.websocket;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;

@Component
@Slf4j
@RequiredArgsConstructor
public class BybitWebSocketClient {
    private static final String BYBIT_WS_URL = "wss://stream.bybit.com/v5/public/spot";
    private WebSocketClient client;
    private WebSocketSession session;
    private final ObjectMapper objectMapper;
    private final StockInfoHolder stockInfoHolder;

    @PostConstruct
    public void connect() {
        try {
            client = new StandardWebSocketClient();
            WebSocketHandler handler = new WebSocketHandler() {
                @Override
                public void afterConnectionEstablished(WebSocketSession session) {
                    log.info("Connected to Bybit WebSocket");
                    // 구독 메시지 전송
                    String subscribeMessage = "{" +
                            "\"op\":\"subscribe\"," +
                            "\"args\":[" +
                            "\"tickers.BTCUSDT\"," +
                            "\"tickers.ETHUSDT\"," +
                            "\"tickers.SOLUSDT\"," +
                            "\"tickers.BNBUSDT\"," +
                            "\"tickers.BTCUSDC\"," +
                            "\"tickers.ETHUSDC\"" +
                            "]" +
                            "}";                    try {
                        session.sendMessage(new TextMessage(subscribeMessage));
                    } catch (IOException e) {
                        log.error("Subscription failed", e);
                    }
                }

                @Override
                public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) {
                    try {
                        String payload = message.getPayload().toString();
                        JsonNode jsonNode = objectMapper.readTree(payload);

                        // ping 메시지 처리
                        if (jsonNode.has("op") && "ping".equals(jsonNode.get("op").asText())) {
                            String pongMessage = "{\"op\":\"pong\"}";
                            session.sendMessage(new TextMessage(pongMessage));
                            return;
                        }

                        // 가격 데이터 처리
                        if (jsonNode.has("data")) {
                            JsonNode data = jsonNode.get("data");
                            String lastPrice = data.get("lastPrice").asText();
                            stockInfoHolder.updatePrice(data.get("symbol").asText(), new BigDecimal(lastPrice));
                            log.debug("Price updated: {}", lastPrice);
                        }
                    } catch (Exception e) {
                        log.error("Error handling message", e);
                    }
                }

                @Override
                public void handleTransportError(WebSocketSession session, Throwable exception) {
                    log.error("Transport error", exception);
                    reconnect();
                }

                @Override
                public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) {
                    log.info("Connection closed: {}", closeStatus);
                    reconnect();
                }

                @Override
                public boolean supportsPartialMessages() {
                    return false;
                }
            };

            session = client.doHandshake(handler, new WebSocketHttpHeaders(), URI.create(BYBIT_WS_URL)).get();
        } catch (Exception e) {
            log.error("Connection failed", e);
            reconnect();
        }
    }

    private void reconnect() {
        log.info("Attempting to reconnect...");
        try {
            Thread.sleep(5000); // 5초 대기
            connect();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Reconnection interrupted", e);
        }
    }

    @PreDestroy
    public void disconnect() {
        if (session != null && session.isOpen()) {
            try {
                session.close();
            } catch (IOException e) {
                log.error("Error closing websocket", e);
            }
        }
    }

}
