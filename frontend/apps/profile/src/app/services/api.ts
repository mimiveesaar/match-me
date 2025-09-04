const API_BASE_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:3000';

export interface ProfileData {
  planet: string;
  lookingFor: string;
  interests: string;
  bio: string;
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

  // Create a new profile
  async createProfile(data: ProfileData): Promise<Profile> {
    return this.fetchApi('/api/profiles', {
      method: 'POST',
      body: JSON.stringify(data),
    });
  }

  // Get all profiles
  async getProfiles(): Promise<Profile[]> {
    return this.fetchApi('/api/profiles');
  }

  // Get a specific profile
  async getProfile(id: string): Promise<Profile> {
    return this.fetchApi(`/api/profiles/${id}`);
  }

  // Update a profile
  async updateProfile(id: string, data: Partial<ProfileData>): Promise<Profile> {
    return this.fetchApi(`/api/profiles/${id}`, {
      method: 'PUT',
      body: JSON.stringify(data),
    });
  }

  // Delete a profile
  async deleteProfile(id: string): Promise<void> {
    return this.fetchApi(`/api/profiles/${id}`, {
      method: 'DELETE',
    });
  }
}

export const apiService = new ApiService();