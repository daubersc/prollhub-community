import i18n from 'i18next';
import { initReactI18next } from 'react-i18next';
import HttpApi from 'i18next-http-backend';
import LanguageDetector from 'i18next-browser-languagedetector';

i18n
    .use(HttpApi) // Load translations using http -> /locales/(lng)/translation.json
    .use(LanguageDetector) // Detect user language
    .use(initReactI18next) // Pass the i18n instance to react-i18next.
    .init({
        fallbackLng: 'en', // Use en if detected lng is not available
        debug: false,
        ns: ['translation'], // Namespace for translation files
        defaultNS: 'translation',
        detection: {
            order: ['querystring', 'cookie', 'localStorage', 'sessionStorage', 'navigator', 'htmlTag'],
            caches: ['cookie'], // Cache the language in a cookie (configure cookie options if needed)
        },
        interpolation: {
            escapeValue: false, // React already safes from xss
        },
        backend: {
            loadPath: '/locales/{{lng}}/{{ns}}.json', // Path to translation files (e.g., /locales/en/translation.json)
        },
        react: {
            useSuspense: true // Recommended with HttpApi backend
        }
    });

export default i18n;
