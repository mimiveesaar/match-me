export interface ChatMessage {
  conversationId: string;
  senderId: string;
  senderName: string;
  content: string;
  timestamp: string;
  type: string;
  typing?: boolean;
}

export interface ChatWindowProps {
  messages: ChatMessage[];
  currentUserId: string;
}