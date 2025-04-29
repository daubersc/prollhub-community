// src/components/Button.tsx
import React from 'react';
import {
    CheckCircleIcon,
    ExclamationTriangleIcon,
    XCircleIcon,
    ArrowPathIcon
} from '@heroicons/react/24/solid';
import type { ButtonState } from '../types/common';
import {useTranslation} from "react-i18next";
import {PaperAirplaneIcon} from "@heroicons/react/24/outline";


interface ButtonProps extends React.ButtonHTMLAttributes<HTMLButtonElement> {
    status?: ButtonState;
    loadingText?: string; // Optional text for screen readers during loading
}

const Button: React.FC<ButtonProps> = ({
                                           children,
                                           status = 'idle',
                                           className = '',
                                           ...props
                                       }) => {
    const isLoading = status === 'loading';
    const isDisabled = isLoading || props.disabled;

    const { t } = useTranslation();

    const baseClasses = "w-full flex justify-between items-center px-4 py-2 border border-2 rounded-md shadow-sm text-sm font-medium text-white focus:outline-none transition duration-150 ease-in-out";
    const colorClasses = {
        idle: 'bg-cyan-900 border-cyan-500',
        loading: 'bg-yellow-900 border-yellow-500 cursor-not-allowed',
        success: 'bg-green-900 border-green-500',
        warning: 'bg-orange-900 border-orange-500',
        error: 'bg-red-900 border-red-500',
    };

    const IconComponent = {
        idle: PaperAirplaneIcon,
        loading: ArrowPathIcon,
        success: CheckCircleIcon,
        warning: ExclamationTriangleIcon,
        error: XCircleIcon,
    }[status];

    return (
        <button
            type="submit" // Default to submit, can be overridden
            className={`${baseClasses} ${colorClasses[status]} ${isDisabled ? 'opacity-75 cursor-not-allowed' : ''} ${className}`}
            disabled={isDisabled}
            aria-busy={isLoading}
            {...props}
        >
            <div></div>
            {/* For screen readers during loading */}
            {isLoading && <span className="">{t('button.state.loading')}</span>}
            {/* Keep original button text visible unless loading */}
            {!isLoading && children}

            {IconComponent && (
                <IconComponent
                    className={`h-5 w-5 ml-2 ${isLoading ? 'animate-spin' : ''} 
                    ${status === 'loading' ? 'text-yellow-500' :
                        status === 'success' ? 'text-green-500' :
                            status === 'warning' ? 'text-orange-500' :
                                status === 'error' ? 'text-red-500' : 'text-cyan-500'}`}
                    aria-hidden="true"
                />
            )}
        </button>
    );
};

export default Button;
