// src/services/authApi.ts
import axios from 'axios'; // Assuming axios is installed
import type { ApiResponse, UserInfo } from '../types/common';

// Configure axios instance (optional, but good practice)
const apiClient = axios.create({
    baseURL: '/api/auth', // Base URL for auth endpoints
    headers: {
        'Content-Type': 'application/json',
    },
    // Important for session cookies if backend/frontend are on different ports during dev
    // withCredentials: true,
});

// Add request interceptor for CSRF token if needed (and enabled in Spring Security)
// apiClient.interceptors.request.use(config => {
//     const token = getCookie('XSRF-TOKEN'); // Function to get cookie
//     if (token && config.headers && (config.method === 'post' || config.method === 'put' || config.method === 'delete')) {
//         config.headers['X-XSRF-TOKEN'] = token;
//     }
//     return config;
// });


// --- Placeholder API Functions ---
// Replace these with actual API calls using apiClient

interface LoginCredentials {
    email: string;
    password?: string; // Password might be optional
}

/**
 * Placeholder function to simulate logging in a user.
 * @param credentials - Login credentials.
 * @returns Promise resolving to ApiResponse containing UserInfo.
 */
export const loginUser = async (credentials: LoginCredentials): Promise<ApiResponse<UserInfo>> => {
    console.log('API CALL: loginUser', credentials);
    // Replace with actual Axios call:
    // const response = await apiClient.post<ApiResponse<UserInfo>>('/login', credentials);
    // return response.data;

    // --- Mock Implementation ---
    await new Promise(resolve => setTimeout(resolve, 1000)); // Simulate delay
    const success = Math.random() > 0.3 || !!credentials.password; // Higher chance of success if password provided
    if (success) {
        return {
            category: 'SUCCESS',
            code: 'LOGIN_SUCCESS',
            message: 'Successfully logged in!',
            data: { // Mock UserInfoDTO structure
                id: 'uuid-' + Math.random().toString(36).substring(7),
                username: "foobar",
                email: credentials.email,
                did: null
            }
        };
    } else {
        // Simulate throwing an error like Axios would on 4xx/5xx
        const error: any = new Error('Request failed with status code 401');
        error.response = {
            data: {
                category: 'ERROR',
                code: 'BAD_CREDENTIALS',
                message: 'Invalid username or password provided.',
            } as ApiResponse,
            status: 401,
            headers: {},
            config: {}
        };
        throw error;
    }
    // --- End Mock ---
};

/**
 * Placeholder function to simulate sending a magic link.
 * @param emailOrUsername - User identifier.
 * @returns Promise resolving to ApiResponse (usually no data).
 */
export const sendMagicLink = async (emailOrUsername: string): Promise<ApiResponse<null>> => {
    console.log('API CALL: sendMagicLink', emailOrUsername);
    // Replace with actual Axios call:
    // const response = await apiClient.post<ApiResponse<null>>('/magic-link', { emailOrUsername }); // Adjust endpoint/payload
    // return response.data;

    // --- Mock Implementation ---
    await new Promise(resolve => setTimeout(resolve, 1000));
    return {
        category: 'SUCCESS',
        code: 'MAGIC_LINK_SENT',
        message: 'Check your email for the magic link!',
    };
    // --- End Mock ---
};

interface RegisterData {
    username: string;
    email: string;
    password?: string;
}

/**
 * Placeholder function to simulate registering a user.
 * @param data - Registration data.
 * @returns Promise resolving to ApiResponse containing UserInfo.
 */
export const registerUser = async (data: RegisterData): Promise<ApiResponse<UserInfo>> => {
    console.log('API CALL: registerUser', data);
    // Replace with actual Axios call:
    // const response = await apiClient.post<ApiResponse<UserInfo>>('/register', data);
    // return response.data;

    // --- Mock Implementation ---
    await new Promise(resolve => setTimeout(resolve, 1000));
    const success = Math.random() > 0.3;
    if (success) {
        return {
            category: 'SUCCESS',
            code: 'USER_REGISTERED',
            message: 'Registration successful! You can now log in.',
            data: { // Mock UserInfoDTO structure
                id: 'uuid-' + Math.random().toString(36).substring(7),
                username: data.username,
                email: data.email,
                did: null
            }
        };
    } else {
        const error: any = new Error('Request failed with status code 400');
        error.response = {
            data: {
                category: 'ERROR',
                code: 'USERNAME_EXISTS', // Or EMAIL_EXISTS
                message: 'Username is already taken.',
            } as ApiResponse,
            status: 400,
            headers: {},
            config: {}
        };
        throw error;
    }
    // --- End Mock ---
};

// Add functions for logout, getCurrentUser (/me), etc. later

