// src/components/Snackbar.tsx
import React, { useEffect } from 'react';
import { useTranslation } from 'react-i18next';
import {
    CheckCircleIcon,
    ExclamationTriangleIcon,
    XCircleIcon
} from '@heroicons/react/24/solid';
import type { SnackbarMessage } from '../types/common';

interface SnackbarProps {
    message: SnackbarMessage | null;
    onDismiss: () => void;
    className?: string; // Allow passing additional classes
}

const Snackbar: React.FC<SnackbarProps> = ({ message, onDismiss, className = '' }) => {
    const { t } = useTranslation(); // Hook for translations

    useEffect(() => {
        if (message) {
            const timer = setTimeout(() => {
                onDismiss();
            }, 5000); // Auto-dismiss after 5 seconds
            return () => clearTimeout(timer);
        }
    }, [message, onDismiss]);

    if (!message) return null;

    const bgColor = {
        SUCCESS: 'bg-green-100 border-green-400 text-green-700',
        WARNING: 'bg-yellow-100 border-yellow-400 text-yellow-700',
        ERROR: 'bg-red-100 border-red-400 text-red-700',
    }[message.category];

    const IconComponent = {
        SUCCESS: CheckCircleIcon,
        WARNING: ExclamationTriangleIcon,
        ERROR: XCircleIcon,
    }[message.category];

    // Attempt to translate the message code, fall back to the message field
    const displayMessage = t(message.code, message.message); // t(key, fallback)

    return (
        <div
            className={`fixed bottom-5 right-5 z-50 p-4 rounded-md border shadow-lg ${bgColor} ${className}`}
            role="alert"
            aria-live="assertive"
            aria-atomic="true"
        >
            <div className="flex items-center">
                {IconComponent && <IconComponent className="h-5 w-5 mr-2" aria-hidden="true" />}
                <span className="text-sm font-medium">{displayMessage}</span>
                <button
                    onClick={onDismiss}
                    className="ml-auto -mx-1.5 -my-1.5 bg-transparent rounded-lg focus:ring-2 focus:ring-current p-1.5 inline-flex h-8 w-8"
                    aria-label={t('dismiss', 'Dismiss')}
                >
                    <span className="sr-only">{t('dismiss', 'Dismiss')}</span>
                    {/* Use XCircleIcon for dismiss button */}
                    <XCircleIcon className="h-5 w-5" aria-hidden="true" />
                </button>
            </div>
        </div>
    );
};

export default Snackbar;
