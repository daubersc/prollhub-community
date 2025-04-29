// src/App.tsx
import React, { useState } from 'react';
import { useTranslation } from 'react-i18next';

// Import Views
import AboutView from './views/AboutView';
import LoginView from './views/LoginView';
import RegisterView from './views/RegisterView';

// Import Global Components
import CookieConsent from './components/CookieConsent';
import Snackbar from './components/Snackbar'; // Import Snackbar component
import { useSnackbar } from './hooks/useSnackbar'; // Import the hook

type ActiveTabView = 'about' | 'login' | 'register';

function App() {
    const { t } = useTranslation();
    const [activeTab, setActiveTab] = useState<ActiveTabView>('about');
    const { snackbar, dismissSnackbar } = useSnackbar(); // Use the hook for state

    const renderTabContent = () => {
        switch (activeTab) {
            case 'login':
                // LoginView now uses the useSnackbar hook internally
                return <LoginView />;
            case 'register':
                // RegisterView also uses the useSnackbar hook internally
                return <RegisterView />;
            case 'about':
            default:
                return <AboutView />;
        }
    };

    return (
        <div className="min-h-screen bg-black flex items-center justify-center">
            <div className="container mx-auto p-4 max-w-6xl">
                <div className="flex flex-col md:flex-row rounded-lg shadow-xl overflow-hidden min-h-[70vh]">
                    {/* Left Section: Application */}
                    <div className="w-full md:w-1/2 p-8 lg:p-12 flex flex-col">

                        {/* Tab Navigation */}
                        {activeTab !== 'about' && (
                            <div className="mb-6 border-b border-gray-200">
                                <nav className="-mb-px flex space-x-8" aria-label="Tabs">
                                    {/* Login Tab Button */}
                                    <button
                                        onClick={() => setActiveTab('login')}
                                        className={`whitespace-nowrap py-4 px-1 border-b-2 font-medium text-sm ${
                                            activeTab === 'login'
                                                ? 'border-indigo-500 text-indigo-600'
                                                : 'border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300'
                                        }`}
                                        aria-current={activeTab === 'login' ? 'page' : undefined}
                                    >
                                        {t('nav.login')}
                                    </button>
                                    {/* Register Tab Button */}
                                    <button
                                        onClick={() => setActiveTab('register')}
                                        className={`whitespace-nowrap py-4 px-1 border-b-2 font-medium text-sm ${
                                            activeTab === 'register'
                                                ? 'border-indigo-500 text-indigo-600'
                                                : 'border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300'
                                        }`}
                                        aria-current={activeTab === 'register' ? 'page' : undefined}
                                    >
                                        {t('nav.register')}
                                    </button>
                                    {/* About Tab Button (appears on Login/Register) */}
                                    <button
                                        onClick={() => setActiveTab('about')}
                                        className="ml-auto whitespace-nowrap py-4 px-1 border-b-2 border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300 font-medium text-sm"
                                    >
                                        {t('nav.about')}
                                    </button>
                                </nav>
                            </div>
                        )}

                        {/* Tab Content Area */}
                        <div className="flex-grow">
                            {renderTabContent()}
                        </div>

                        {/* Links to switch tabs (only shown on 'about' tab) */}
                        {activeTab === 'about' && (
                            <div className="mt-8 text-center border-t pt-6">
                                <p className="text-sm text-gray-600 mb-2">{t('about.actions.section')}</p>
                                <button
                                    onClick={() => setActiveTab('login')}
                                    className="font-medium text-indigo-600 hover:text-indigo-500 mr-4"
                                >
                                    {t('about.actions.login')}
                                </button>
                                <button
                                    onClick={() => setActiveTab('register')}
                                    className="font-medium text-indigo-600 hover:text-indigo-500"
                                >
                                    {t('about.actions.register')}
                                </button>
                            </div>
                        )}
                    </div>

                    {/* Right Section: Image */}
                    <div className="hidden md:block md:w-1/2 bg-gradient-to-br from-indigo-500 to-purple-600">
                        <img
                            src="/images/index-bg.png"
                            alt={t('community_hub_alt', 'Community Hub Illustration')}
                            className="w-full h-full object-cover"
                            onError={(e) => (e.currentTarget.src = 'https://placehold.co/800x1000/cccccc/333333?text=Image+Error')}
                        />
                    </div>
                </div>
            </div>

            {/* Global Components */}
            <CookieConsent />
            {/* Render Snackbar globally using state from hook */}
            <Snackbar message={snackbar} onDismiss={dismissSnackbar} />
        </div>
        // </SnackbarProvider> // Close provider if using Context
    );
}

export default App;

