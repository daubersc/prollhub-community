// src/components/CookieConsent.tsx
import React, { useState, useEffect } from 'react';
import { useTranslation } from 'react-i18next';

const CookieConsent: React.FC = () => {
    const { t } = useTranslation();
    const [isVisible, setIsVisible] = useState(false);

    useEffect(() => {
        try {
            const consentGiven = localStorage.getItem('cookieConsent');
            if (!consentGiven) {
                setIsVisible(true);
            }
        } catch (error) {
            console.error("Could not access localStorage for cookie consent:", error);
            // Decide how to handle this - maybe show consent anyway or assume decline?
        }
    }, []);

    const handleAccept = () => {
        try {
            localStorage.setItem('cookieConsent', 'true');
            setIsVisible(false);
            // Add logic here to initialize tracking, etc.
            console.log("Cookies accepted");
            // You might want to dispatch a custom event or call a function here
            // window.dispatchEvent(new CustomEvent('cookieConsentChanged', { detail: 'accepted' }));
        } catch (error) {
            console.error("Could not set cookie consent in localStorage:", error);
        }
    };

    const handleDecline = () => {
        try {
            localStorage.setItem('cookieConsent', 'false');
            setIsVisible(false);
            // Add logic here to disable non-essential cookies/tracking
            console.log("Cookies declined");
            // window.dispatchEvent(new CustomEvent('cookieConsentChanged', { detail: 'declined' }));
        } catch (error) {
            console.error("Could not set cookie consent in localStorage:", error);
        }
    }

    if (!isVisible) return null;

    return (
        <div className="fixed bottom-0 left-0 right-0 bg-gray-800 text-white p-4 shadow-lg z-50 flex flex-col sm:flex-row items-center justify-between">
            <p className="text-sm mb-2 sm:mb-0 sm:mr-4">
                {t('cookie_consent_message', 'We use cookies to enhance your experience. By continuing to visit this site you agree to our use of cookies.')}
                <a href="/legal" target="_blank" rel="noopener noreferrer" className="underline ml-1 hover:text-gray-300">{t('learn_more', 'Learn More')}</a>
            </p>
            <div className="flex space-x-2 flex-shrink-0">
                <button
                    onClick={handleAccept}
                    className="px-4 py-2 bg-green-600 hover:bg-green-700 rounded-md text-sm font-medium"
                >
                    {t('accept', 'Accept')}
                </button>
                <button
                    onClick={handleDecline}
                    className="px-4 py-2 bg-red-600 hover:bg-red-700 rounded-md text-sm font-medium"
                >
                    {t('decline', 'Decline')}
                </button>
            </div>
        </div>
    );
};

export default CookieConsent;
