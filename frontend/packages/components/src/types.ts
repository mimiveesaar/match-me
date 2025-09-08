export interface ChatMessage {
  conversationId: string;
  senderId: string;
  senderName: string;
  content: string;
  timestamp: string; // always required
  type: string;
  typing?: boolean; 
}

export interface ChatWindowProps {
  messages: ChatMessage[];
  currentUserId: string;
}