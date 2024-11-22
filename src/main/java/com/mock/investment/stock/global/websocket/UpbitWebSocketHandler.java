package com.mock.investment.stock.global.websocket;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class UpbitWebSocketHandler extends TextWebSocketHandler {
    private static final String UPBIT_WS_URL = "wss://api.upbit.com/websocket/v1";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Set<WebSocketSession> clientSessions = ConcurrentHashMap.newKeySet();
    private WebSocketClient upbitClient;

    @PostConstruct
    public void init() {
        connectToUpbit();
    }

    private void connectToUpbit() {
        try {
            WebSocketClient client = new StandardWebSocketClient();

            WebSocketHandler upbitHandler = new WebSocketHandler() {
                @Override
                public void afterConnectionEstablished(WebSocketSession session) {
                    log.info("Connected to Upbit WebSocket");
                    // 구독 요청 보내기
                    String subscribeMessage = "[{\"ticket\":\"UNIQUE_TICKET\"},{\"type\":\"ticker\",\"codes\":[\"KRW-BTC\"]}, {\"format\":\"DEFAULT\"}]";
                    try {
                        session.sendMessage(new TextMessage(subscribeMessage));
                    } catch (IOException e) {
                        log.error("Error sending subscribe message", e);
                    }
                }

                @Override
                public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) {
                    try {
                        // ByteBuffer를 문자열로 변환
                        ByteBuffer buffer = (ByteBuffer) message.getPayload();
                        String data = new String(buffer.array(), StandardCharsets.UTF_8);

                        // JSON 포맷팅을 위한 변환
                        ObjectMapper objectMapper = new ObjectMapper();
                        JsonNode jsonNode = objectMapper.readTree(data);
                        String prettyJson = objectMapper.writerWithDefaultPrettyPrinter()
                                .writeValueAsString(jsonNode);

                        broadcastToClients(prettyJson);
                    } catch (Exception e) {
                        log.error("Error processing message", e);
                    }
                }

                @Override
                public void handleTransportError(WebSocketSession session, Throwable exception) {
                    log.error("Transport error", exception);
                }

                @Override
                public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) {
                    log.info("Connection closed: {}", closeStatus);
                    // 재연결 로직
                    connectToUpbit();
                }

                @Override
                public boolean supportsPartialMessages() {
                    return false;
                }
            };

            client.doHandshake(upbitHandler, new WebSocketHttpHeaders(), URI.create(UPBIT_WS_URL));
            this.upbitClient = client;
            log.info("Connected to Upbit WebSocket: {}", UPBIT_WS_URL);

        } catch (Exception e) {
            log.error("Error connecting to Upbit", e);
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        clientSessions.add(session);
        log.info("Client connected: {}", session.getId());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        clientSessions.remove(session);
        log.info("Client disconnected: {}", session.getId());
    }

    private void broadcastToClients(String message) {
        log.debug("Broadcasting to {} clients", clientSessions.size());
        clientSessions.forEach(session -> {
            try {
                if (session.isOpen()) {
                    session.sendMessage(new TextMessage(message));
                    log.debug("Sent message to client: {}", session.getId());
                }
            } catch (IOException e) {
                log.error("Error sending message to client", e);
            }
        });
    }
}
