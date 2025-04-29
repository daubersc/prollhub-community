// src/views/LoginView.tsx
import React, { useState, ChangeEvent, FormEvent } from 'react';
import { useTranslation } from 'react-i18next';
import InputField from '../components/InputField';
import Button from '../components/Button';
import type { ButtonState, SnackbarMessage, ApiResponse, UserInfo } from '../types/common';
import { loginUser, sendMagicLink } from '../services/apiService'; // Import placeholder API functions
import { useSnackbar } from '../hooks/useSnackbar'; // Import custom hook for snackbar
import {validateEmail } from "../services/validationService";

const LoginView: React.FC = () => {
    const { t } = useTranslation();
    const { showSnackbar } = useSnackbar(); // Use the hook
    const [formData, setFormData] = useState({ email: '', password: '' });
    const [loginStatus, setLoginStatus] = useState<ButtonState>('idle');
    const [magicLinkStatus, setMagicLinkStatus] = useState<ButtonState>('idle');
    const [errors, setErrors] = useState<{ [key: string]: string | null }>({});


    // validation


    const handleChange = (e: ChangeEvent<HTMLInputElement>) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
        setErrors({ ...errors, [e.target.name]: null }); // Clear error on change
    };

    const handleLogin = async (e: FormEvent) => {
        e.preventDefault();
        setLoginStatus('loading');
        setMagicLinkStatus('idle'); // Reset other button
        setErrors({});

        try {
            // --- Actual API Call ---
            const response: ApiResponse<UserInfo> = await loginUser(formData); // Use imported function
            // ---------------------

            if (response.category === 'SUCCESS') {
                setLoginStatus('success');
                console.log("Login successful, data:", response.data);
                // TODO: Update global auth state (e.g., using Context or Zustand)
                // TODO: Redirect user (e.g., using useNavigate from react-router-dom)
                // navigate('/dashboard');
            } else {
                // Should not happen if API call throws error, but handle defensively
                setLoginStatus('error');
                showSnackbar({id: Date.now(), ...response});
            }
        } catch (error: any) {
            console.error("Login failed:", error);
            setLoginStatus('error');
            if (error.response?.data && error.response.data.category === 'ERROR') {
                const errorResponse = error.response.data as ApiResponse;
                showSnackbar({ id: Date.now(), ...errorResponse });
                // Example: Set field errors if API provides them (adjust based on actual API)
                if (errorResponse.code === 'BAD_CREDENTIALS') {
                    setErrors({ emailOrUsername: t(errorResponse.code, errorResponse.message), password: ' ' });
                } else {
                    // Generic error indication
                    setErrors({ emailOrUsername: ' ', password: ' ' });
                }
            } else {
                // Network error or unexpected issue
                showSnackbar({
                    id: Date.now(),
                    category: 'ERROR',
                    code: 'GENERIC_ERROR',
                    message: t('GENERIC_ERROR', 'An unexpected error occurred.')
                });
            }
        } finally {
            // Reset status after a short delay, unless successful navigation happens
            setTimeout(() => {
                // Check if still loading before resetting (might have succeeded/navigated)
                if (loginStatus !== 'success') {
                    setLoginStatus('idle');
                }
            }, 2000);
        }
    };

    const handleMagicLink = async () => {
        if (!formData.email) {
            setErrors({ email: t('email_or_username_required', 'Email or username is required for magic link.') });
            return;
        }
        setMagicLinkStatus('loading');
        setLoginStatus('idle'); // Reset other button
        setErrors({});

        try {
            // --- Actual API Call ---
            const response = await sendMagicLink(formData.email);
            // ---------------------
            if (response.category === 'SUCCESS') {
                setMagicLinkStatus('success');
                showSnackbar({id: Date.now(), ...response});
            } else {
                setMagicLinkStatus('warning'); // Or error depending on API logic
                showSnackbar({id: Date.now(), ...response});
            }
        } catch (error: any) {
            console.error("Magic link failed:", error);
            setMagicLinkStatus('error');
            if (error.response?.data && error.response.data.category === 'ERROR') {
                showSnackbar({id: Date.now(), ...error.response.data});
            } else {
                showSnackbar({id: Date.now(), category: 'ERROR', code: 'GENERIC_ERROR', message: t('GENERIC_ERROR')});
            }
        } finally {
            setTimeout(() => setMagicLinkStatus('idle'), 2000);
        }
    }

    return (
        <div>
            <h2 className="text-2xl font-semibold mb-4">{t('login_title', 'Log In')}</h2>
            <form onSubmit={handleLogin}>
                <InputField
                    id="email"
                    type="email"
                    value={formData.email}
                    onChange={handleChange}
                    isRequired
                    validate={validateEmail}
                    autoComplete="email"
                />
                <InputField
                    id="password"
                    type="password"
                    value={formData.password}
                    onChange={handleChange}
                    // Password might be optional if magic link is primary
                    isRequired={false}
                    autoComplete="current-password"
                />

                {/* Keep Logged In Switch - Disabled */}
                <div className="flex items-center justify-between mb-4">
                    <div className="flex items-center">
                        <input
                            id="keepLoggedIn"
                            name="keepLoggedIn"
                            type="checkbox"
                            className="h-4 w-4 text-indigo-600 focus:ring-indigo-500 border-gray-300 rounded cursor-not-allowed"
                            disabled // Disabled as requested
                        />
                        <label htmlFor="keepLoggedIn" className="ml-2 block text-sm text-gray-500 cursor-not-allowed">
                            {t('input.rememberMe.label')}
                        </label>
                    </div>
                </div>

                <div className="space-y-3">
                    <Button
                        type="submit"
                        status={loginStatus}
                        disabled={!formData.email || loginStatus === 'loading' || magicLinkStatus === 'loading'}
                    >
                        {t('login.actions.login')}
                    </Button>
                    <Button
                        type="button"
                        onClick={handleMagicLink}
                        status={magicLinkStatus}
                        className="bg-gray-600 hover:bg-gray-700 focus:ring-gray-500"
                        disabled={!formData.email || loginStatus === 'loading' || magicLinkStatus === 'loading'}
                        loadingText={t('sending', 'Sending...')}
                    >
                        {t('login.actions.link')}
                    </Button>
                </div>
            </form>
        </div>
    );
};

export default LoginView;

