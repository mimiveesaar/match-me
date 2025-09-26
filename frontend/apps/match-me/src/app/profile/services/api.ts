const API_BASE_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080';

export interface ProfileData {
  homeplanetId: number;
  bodyformId: number;
  lookingForId: number;
  bio: string;
  interestIds: number[];
  profilePic: string;
}

export interface Profile extends ProfileData {
  id: string;
  createdAt: string;
  updatedAt: string;
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

  // Get current user's profile
  async getCurrentProfile(): Promise<Profile> {
    return this.fetchApi('/api/profiles/me');
  }

  // Update current user's profile
  async updateCurrentProfile(data: ProfileData): Promise<Profile> {
    return this.fetchApi('/api/profiles/me', {
      method: 'PUT',
      body: JSON.stringify(data),
    });
  }

  // Create a new profile (for signup flow)
  async createProfile(data: ProfileData): Promise<Profile> {
    return this.fetchApi('/api/profiles', {
      method: 'POST',
      body: JSON.stringify(data),
    });
  }
}

export const apiService = new ApiService();