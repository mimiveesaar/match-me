export interface ChatMessage {
  conversationId: string;
  senderId: string;
  senderUsername: string;
  content: string;
  timestamp: number[]; 
  type: string;
  typing?: boolean; 
}

export interface ChatWindowProps {
  messages: ChatMessage[];
  currentUserId: string;
}