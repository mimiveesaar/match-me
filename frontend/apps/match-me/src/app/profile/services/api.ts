const API_BASE_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080';

export interface ProfileData {
  homeplanetId?: string; 
  bodyformId?: string;  
  lookingForId?: string; 
  bio?: string;
  interestIds?: string[];
  profilePic?: string;
  username?: string;
  name?: string;
  age?: number;
}

export interface Profile extends ProfileData {
  id: string;
  createdAt?: string;
  updatedAt?: string;
}

export interface ReferenceDataOption {
  id: string;  // Changed to string (UUID)
  name: string;
}

class ApiService {
  private async fetchApi(endpoint: string, options?: RequestInit) {
    const url = `${API_BASE_URL}${endpoint}`;
    
    const response = await fetch(url, {
      headers: {
        'Content-Type': 'application/json',
        ...options?.headers,
      },
      ...options,
    });

    if (!response.ok) {
      throw new Error(`API call failed: ${response.statusText}`);
    }

    return response.json();
  }

  async getCurrentProfile(): Promise<Profile> {
    return this.fetchApi('/api/profiles/me');
  }

  async updateCurrentProfile(data: ProfileData): Promise<Profile> {
    return this.fetchApi('/api/profiles/me', {
      method: 'POST',
      body: JSON.stringify(data),
    });
  }

  async getInterests(): Promise<ReferenceDataOption[]> {
    return this.fetchApi('/api/interests');
  }

  async getHomeplanets(): Promise<ReferenceDataOption[]> {
    return this.fetchApi('/api/homeplanets');
  }

  async getBodyforms(): Promise<ReferenceDataOption[]> {
    return this.fetchApi('/api/bodyforms');
  }

  async getLookingFor(): Promise<ReferenceDataOption[]> {
    return this.fetchApi('/api/looking-for');
  }
}

export const apiService = new ApiService();