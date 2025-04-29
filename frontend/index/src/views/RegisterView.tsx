// src/views/RegisterView.tsx
import React, { useState, ChangeEvent, FormEvent } from 'react';
import { useTranslation } from 'react-i18next';
import InputField from '../components/InputField';
import Button from '../components/Button';
import type { ButtonStatus, SnackbarMessage, ApiResponse, UserInfo } from '../types/common';
import { registerUser } from '../services/apiService'; // Import placeholder API function
import { useSnackbar } from '../hooks/useSnackbar'; // Import custom hook
import {validateEmail, validatePassword, validateUsername } from "../services/validationService";


const RegisterView: React.FC = () => {
    const { t } = useTranslation();
    const { showSnackbar } = useSnackbar();
    const [formData, setFormData] = useState({ username: '', email: '', password: '', token: ''});
    const [formStatus, setFormStatus] = useState<ButtonStatus>('idle');
    const [errors, setErrors] = useState<{ [key: string]: string | null }>({});

    const handleChange = (e: ChangeEvent<HTMLInputElement>) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
        setErrors({ ...errors, [e.target.name]: null }); // Clear error on change
    };

    const handleRegister = async (e: FormEvent) => {
        e.preventDefault();
        setFormStatus('loading');
        setErrors({});

        try {
            // --- Actual API Call ---
            const response: ApiResponse<UserInfo> = await registerUser(formData);
            // ---------------------

            if (response.category === 'SUCCESS') {
                setFormStatus('success');
                showSnackbar({ id: Date.now(), ...response });
                console.log("Registration successful:", response.data);
                // TODO: Handle success (e.g., redirect to login, clear form)
                // setActiveTab('login'); // If managed by parent
                setFormData({ username: '', email: '', password: '' }); // Clear form
            } else {
                setFormStatus('error'); // Or warning
                showSnackbar({ id: Date.now(), ...response });
            }
        } catch (error: any) {
            console.error("Registration failed:", error);
            setFormStatus('error');
            if (error.response?.data && error.response.data.category === 'ERROR') {
                const errorResponse = error.response.data as ApiResponse;
                showSnackbar({ id: Date.now(), ...errorResponse });
                // Example: Set field errors if API provides them
                if (errorResponse.code === 'USERNAME_EXISTS') {
                    setErrors({ username: t(errorResponse.code, errorResponse.message) });
                } else if (errorResponse.code === 'EMAIL_EXISTS') { // Assuming EMAIL_EXISTS code
                    setErrors({ email: t(errorResponse.code, errorResponse.message) });
                } else {
                    // Indicate generic error on relevant fields if possible
                    setErrors({ username: ' ', email: ' ', password: ' ' });
                }
            } else {
                showSnackbar({ id: Date.now(), category: 'ERROR', code: 'GENERIC_ERROR', message: t('GENERIC_ERROR') });
            }
        } finally {
            setTimeout(() => {
                if(formStatus !== 'success') {
                    setFormStatus('idle');
                }
            }, 2000);
        }
    };

    return (
        <div>
            <h2 className="text-2xl font-semibold mb-2">{t('register_title', 'Create Account')}</h2>
            <p className="text-sm text-gray-600 mb-4">
                {t('register_description', 'Join the community by creating your account.')}
            </p>
            <form onSubmit={handleRegister}>
                <InputField
                    id="username"
                    type="text"
                    value={formData.username}
                    onChange={handleChange}
                    validate={validateUsername}
                    isRequired
                    autoComplete="username"
                />
                <InputField
                    id="email"
                    type="email"
                    value={formData.email}
                    onChange={handleChange}
                    validate={validateEmail}
                    isRequired
                    autoComplete="email"
                />
                <InputField
                    id="token"
                    type="text"
                    value={formData.token}
                    onChange={handleChange}
                    isRequired
                />
                <InputField
                    id="password"
                    type="password"
                    value={formData.password}
                    validate={validatePassword}
                    onChange={handleChange}
                    isRequired
                    autoComplete="new-password"
                />
                <div className="mt-6">
                    <Button
                        type="submit"
                        status={formStatus}
                        disabled={!formData.username || !formData.email || !formData.password || formStatus === 'loading'}
                    >
                        {t('register.actions.register')}
                    </Button>
                </div>
            </form>
        </div>
    );
};

export default RegisterView;

