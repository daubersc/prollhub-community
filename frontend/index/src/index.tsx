// src/main.tsx
import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './App';
import './services/i18n'; // Initialize i18next
import './output.css'; // Import Tailwind base styles (ensure this file exists and imports tailwind)

ReactDOM.createRoot(document.getElementById('root')!).render(
    <React.StrictMode>
        {/* Wrap with Suspense for i18next HttpApi backend */}
        <React.Suspense fallback="Loading...">
            <App />
        </React.Suspense>
    </React.StrictMode>,
);
