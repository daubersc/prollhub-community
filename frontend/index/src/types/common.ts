/** possible button states indicating api state */
export type ButtonState = 'idle' | 'loading' | 'success' | 'warning' |'error';

/**
 * Structure of the expected API Responses
 */
export interface ApiResponse<T = any> {
category: 'SUCCESS' | 'ERROR' | 'WARNING';
code: string;
message: string;
data?: T;
}

/**
 * Structure for messages displayed in the snackbar.
 */
export interface SnackbarMessage {
    id: number;
    category: 'SUCCESS' | 'ERROR' | 'WARNING';
    code: string;
    message: string;
}

/**
 * Structure of the UserInfoDTO - this is common also for other views.
 */
export interface UserInfo {
    id: string;
    username: string;
    email: string;
    did?: string | null;
    roles?: string[];
}