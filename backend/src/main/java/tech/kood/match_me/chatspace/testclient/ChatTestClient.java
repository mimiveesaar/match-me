package tech.kood.match_me.chatspace.testclient;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import tech.kood.match_me.chatspace.dto.ChatMessageDTO;

public class ChatTestClient {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        // create WebSocket transport
        WebSocketClient transport = new StandardWebSocketClient();
        SockJsClient sockJsClient = new SockJsClient(Collections.singletonList(new WebSocketTransport(transport)));

        // create STOMP client using SockJS
        WebSocketStompClient stompClient = new WebSocketStompClient(sockJsClient);
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
        ChatMessageDTO mock = new ChatMessageDTO(
                UUID.fromString("11111111-1111-1111-1111-111111111111"), // senderId
                UUID.fromString("22222222-2222-2222-2222-222222222222"), // receiverId
                "Hello, this is a mock message!", // content/message
                "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa", // conversation/chatId
                LocalDateTime.now(), // timestamp
                "MESSAGE", // type/status
                false // isRead
        );
        mock.setConversationId(UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"));
        mock.setSenderId(UUID.fromString("11111111-1111-1111-1111-111111111111"));
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
