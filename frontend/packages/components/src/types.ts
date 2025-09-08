export interface ChatMessage {
  conversationId: string;
  senderId: string;
  content: string;
  timestamp: string; // always required
  type: string;
}

export interface ChatWindowProps {
  messages: ChatMessage[];
  currentUserId: string;
}