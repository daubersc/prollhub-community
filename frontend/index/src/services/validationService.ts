import axios from "axios";

const EMAIL_REGEX = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
const USERNAME_REGEX = /^[a-zA-Z0-9_.-]{3,50}$/; // Example: 3-50 chars, letters, numbers, _, ., -
const PASSWORD_REGEX = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?~])[A-Za-z0-9!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?~]{8,100}$/;

const apiClient = axios.create({
    baseURL: '/api/validate', // Base URL for auth endpoints
    headers: {
        'Content-Type': 'application/json',
    },
    // Important for session cookies if backend/frontend are on different ports during dev
    // withCredentials: true,
});

export const validateUsername = async (value: string): Promise<string | null> => {
    value = value.trim();
    if (!(USERNAME_REGEX.test(value))) return 'invalid_username';

    try {
        const response = await apiClient.get(`?username=${value}`)
        return (response?.status == 200) ? 'user_exists' : null;
    } catch (e: any) {
        if (axios.isAxiosError(e) && e.response?.status == 404) return null;
        return 'unknown_error';
    }


}

export const validateEmail = async (value: string): Promise<string | null> => {
    if (!EMAIL_REGEX.test(value)) return 'invalid_email';

    try {
        const response = await apiClient.get(`?email=${value}`)
        return (response?.status == 200) ? 'email_exists' : null;
    } catch (e: any) {
        if (axios.isAxiosError(e) && e.response?.status == 404) return null;
        return 'unknown_error';
    }



}

export const validatePassword = (value:string): string | null => {
    return (PASSWORD_REGEX.test(value)) ? null : 'invalid_password';
}

