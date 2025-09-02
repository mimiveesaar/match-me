export interface MatchUser {
  id: string;
  profilepicSrc?: string;
  username?: string;
  age?: number;
  homeplanet: string;
  lookingFor: string;
  bodyform: string;
  bio?: string;
  interests?: string[];
  supermatch?: boolean;
}

export interface Filters {
  minAge: number;      
  maxAge: number;        
  maxDistanceLy: number;
  bodyform: string;
  interests: string[];   
  homeplanet: string;
  lookingFor: string;
}