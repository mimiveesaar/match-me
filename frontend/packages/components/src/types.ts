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

// Profile-related interfaces
export interface ProfileCardData {
  name: string;
  age: string;
  bodyform: string;
  lookingfor: string;
  planet: string;
  homeplanetId?: number;
  bodyformId?: number;
  lookingForId?: number;
  profilePic?: string;
}

export interface MyProfilePageProps {
  initialProfile?: any;
  onSave?: (profileData: any) => void | Promise<void>;
}

export interface Option {
  value: string;
  label: string;
}