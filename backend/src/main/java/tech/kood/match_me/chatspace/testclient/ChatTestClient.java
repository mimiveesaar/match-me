package tech.kood.match_me.chatspace.testclient;

import java.lang.reflect.Type;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import tech.kood.match_me.chatspace.dto.ChatMessageDTO;

public class ChatTestClient {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        // Create STOMP client
        WebSocketStompClient stompClient = new WebSocketStompClient(new StandardWebSocketClient());
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        // Connect to WebSocket endpoint
        StompSession session = stompClient
                .connectAsync("ws://localhost:8080/ws", new StompSessionHandlerAdapter() {
                })
                .get(); // waits until connection is established

        // Subscribe to /topic/messages
        session.subscribe("/topic/messages", new StompFrameHandler() {
            @Override
            @NonNull
            public Type getPayloadType(@NonNull StompHeaders headers) {
                return ChatMessageDTO.class;
            }

            @Override
            public void handleFrame(@NonNull StompHeaders headers, @Nullable Object payload) {
                if (payload != null) {
                    ChatMessageDTO message = (ChatMessageDTO) payload;
                    System.out.println("Received message: " + message.getContent());
                }
            }
        });

        // Create a mock chat message
        ChatMessageDTO mock = new ChatMessageDTO();
        mock.setConversationId(UUID.randomUUID());
        mock.setSenderId(UUID.randomUUID());
        mock.setContent("Hello, this is a mock message!");
        mock.setType("MESSAGE");

        // Send the message
        session.send("/app/chat.sendMessage", mock);

        // Wait to receive the broadcasted message
        Thread.sleep(2000);

        // Disconnect
        session.disconnect();
    }
}
