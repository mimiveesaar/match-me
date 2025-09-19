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

export interface Filters {
    minAge: number;
    maxAge: number;
    maxDistanceLy: number;
    bodyform: string;
    interests: string[];
    lookingFor: string;
    homeplanet: string;
}