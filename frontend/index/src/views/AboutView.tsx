// src/views/AboutView.tsx
import React from 'react';
import { useTranslation } from 'react-i18next';


const parseMarkdown = (text:string): React.ReactNode[] => {
    const parts = text.split('**')
    return parts.map((part, index) => {
        // If the index is odd, it means this part was between delimiters
        if (index % 2 === 1) {
            // Use index as key within this specific item's parsing
            return <strong key={index}>{part}</strong>;
        } else {
            // Even index parts are regular text
            // Use React.Fragment for keys when returning just text or null
            return <React.Fragment key={index}>{part}</React.Fragment>;
        }
    });
}

const AboutView: React.FC = () => {
    const { t } = useTranslation();
    const featureList = t('about.features', { returnObjects: true }) || [];

    return (
        <div>
            <h2 className="text-2xl font-semibold mb-4">{t('about.title')}</h2>
            <p className="text-gray-200">{t('about.description')}</p>
            {Array.isArray(featureList) && featureList.length > 0 &&(
                <ul className="list-disc list-inside space-y-2 text-gray-100">
                    {featureList.map((item, index) => (
                        // Use index as key for simple lists, consider stable IDs if available
                        <li key={index}>
                            {parseMarkdown(item)}
                        </li>
                    ))}

                </ul>
            )}
        </div>
    );
};

export default AboutView;
