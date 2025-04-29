// src/hooks/useSnackbar.ts
import { useState, useCallback } from 'react';
import type { SnackbarMessage } from '../types/common';

// Interface for the hook's return value
interface UseSnackbarReturn {
    snackbar: SnackbarMessage | null;
    showSnackbar: (message: {
        id: number;
        category: "SUCCESS" | "ERROR" | "WARNING";
        code: string;
        message: string;
        data?: any
    }) => void; // Allow passing message without ID
    dismissSnackbar: () => void;
}

export const useSnackbar = (): UseSnackbarReturn => {
    const [snackbar, setSnackbar] = useState<SnackbarMessage | null>(null);

    // Use useCallback to memoize the functions
    const showSnackbar = useCallback((message: Omit<SnackbarMessage, 'id'>) => {
        // Add a unique ID (timestamp) when showing the message
        setSnackbar({ ...message, id: Date.now() });
    }, []); // No dependencies, function doesn't change

    const dismissSnackbar = useCallback(() => {
        setSnackbar(null);
    }, []); // No dependencies

    return { snackbar, showSnackbar, dismissSnackbar };
};

// Optional: Context approach if needed across deeply nested components
// // src/contexts/SnackbarContext.tsx
// import React, { createContext, useContext } from 'react';
// import { useSnackbar } from '../hooks/useSnackbar';
// import type { UseSnackbarReturn } from '../hooks/useSnackbar';

// const SnackbarContext = createContext<UseSnackbarReturn | undefined>(undefined);

// export const SnackbarProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
//     const snackbarState = useSnackbar();
//     return (
//         <SnackbarContext.Provider value={snackbarState}>
//             {children}
//             {/* Render Snackbar globally here */}
//             <Snackbar message={snackbarState.snackbar} onDismiss={snackbarState.dismissSnackbar} />
//         </SnackbarContext.Provider>
//     );
// };

// export const useSnackbarContext = (): UseSnackbarReturn => {
//     const context = useContext(SnackbarContext);
//     if (context === undefined) {
//         throw new Error('useSnackbarContext must be used within a SnackbarProvider');
//     }
//     return context;
// };

