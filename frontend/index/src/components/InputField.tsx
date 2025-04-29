// src/components/InputField.tsx
import React, {useState, ChangeEvent} from 'react';
import {EyeIcon, EyeSlashIcon} from '@heroicons/react/24/solid';
import {useTranslation} from "react-i18next";
import i18n from "../services/i18n.ts";

interface InputFieldProps {
    id: string;
    type: 'text' | 'email' | 'password';
    value: string;
    onChange: (e: ChangeEvent<HTMLInputElement>) => void;
    validate?: (value: string) => Promise<any> | string | null | undefined;
    isRequired?: boolean;
    placeholder?: string;
    autoComplete?: string;
    className?: string; // Allow passing additional classes
}

const InputField: React.FC<InputFieldProps> = ({
                                                   id,
                                                   type,
                                                   value,
                                                   onChange,
                                                   validate,
                                                   isRequired = false,
                                                   placeholder = ' ',
                                                   autoComplete = 'off',
                                                   className = '',
                                               }) => {


    const [isPasswordVisible, setIsPasswordVisible] = useState(false);
    const actualType = type === 'password' && isPasswordVisible ? 'text' : type;

    const [isFocused, setIsFocused] = useState(false);
    const [error, setError] = useState<string | null>(null);
    const [isValid, setIsValid] = useState<boolean | null>(null);
    const {t} = useTranslation();


    // Handle the blur event to trigger validation
    const handleBlur = async (e: any) => {
        setIsFocused(false); // Standard blur behavior

        if (validate && e.target) {
            const validationResult = await validate(e.target.value); // Validate the current value
            if (validationResult) {
                // Validation failed, set error code and invalid state
                setError(validationResult);
                setIsValid(false);
            } else {
                // Validation succeeded, clear error and set valid state
                setError(null);
                setIsValid(true);
            }
        } else {
            // If no validation function is provided, reset validation state
            // or decide on default behavior (e.g., always valid if not empty and required)
            if (isRequired && e.target && !e.target.value) {
                // Basic required check if no custom validation
                setError("field_required"); // Example error code
                setIsValid(false);
            } else {
                setError(null);
                setIsValid(null); // Or true if you want to consider it valid by default
            }
        }
    };

    const handleFocus = () => {
        setIsFocused(true);
        setError(null);
        setIsValid(null);
    }


    const togglePasswordVisibility = () => {
        if (type === 'password') {
            setIsPasswordVisible(!isPasswordVisible);
        }
    };

    const autofillClasses = `
        autofill:bg-transparent
        autofill:text-white
        [&:-webkit-autofill]:bg-transparent
        [&:-webkit-autofill]:text-white
        [&:-webkit-autofill:hover]:bg-transparent
        [&:-webkit-autofill:focus]:bg-transparent
        [&:-webkit-autofill:active]:bg-transparent
         [&:-webkit-autofill:hover]:text-white
        [&:-webkit-autofill:focus]:text-white
        [&:-webkit-autofill:active]:text-white
        [&:-webkit-autofill]:shadow-[0_0_0_30px_white_inset]
        dark:[&:-webkit-autofill]:shadow-[0_0_0_30px_#222222_inset] // Optional: Adjust shadow for dark mode if needed (using gray-900 color)
        dark:[&:-webkit-autofill]:text-white // Ensure text color is correct in dark mode autofill
    `;
    // --- End Autofill Override ---

    return (
        <div>
        <div className="relative my-2">
            <div className={`${className}`}>
                <input id={id} name={id} type={actualType}
                       placeholder={placeholder} value={value || ''}
                       required={isRequired}
                       onChange={onChange}
                       onFocus={handleFocus}
                       onBlur={handleBlur}
                       aria-required={isRequired}
                       autoComplete={autoComplete}
                       aria-invalid={!!error}
                       aria-describedby={error ? `${id}-error` : `${id}-description`}
                       className={`block px-2.5 pb-2.5 pt-4 w-full text-sm text-white bg-transparent border-b appearance-none 
                   focus:outline-none focus:ring-0 focus:border-yellow-500 peer ${autofillClasses}
                   ${isFocused ? 'border-yellow-500' :
                           isValid === true ? 'border-green-500' :
                               isValid === false ? 'border-red-500' : 'border-gray-300'}
                   `}
                />

                <label htmlFor={id} className={`absolute text-sm duration-300 transform -translate-y-4 scale-75 top-4 z-10 
            origin-[0] start-2.5 peer-placeholder-shown:scale-100
                peer-placeholder-shown:translate-y-0 peer-focus:scale-75 peer-focus:-translate-y-4
                rtl:peer-focus:translate-x-1/4 rtl:peer-focus:left-auto
                ${isFocused ? 'text-yellow-500' :
                    isValid === true ? 'text-green-500' :
                        isValid === false ? 'text-red-500' : 'text-gray-300'}
                `}
                >
                    {t(`input.${id}.label`)} {isRequired && <span>*</span>}
                </label>
                {type === 'password' && (
                    <button
                        type="button"
                        onClick={togglePasswordVisibility}
                        className="absolute inset-y-0 right-0 pr-3 flex items-center text-gray-500 hover:text-gray-700 focus:outline-none"
                        aria-label={isPasswordVisible ? 'input.password.hide' : 'input.password.show'} // TODO: Translate aria-label
                    >
                        {isPasswordVisible ? (
                            <EyeSlashIcon className="h-5 w-5"/>
                        ) : (
                            <EyeIcon className="h-5 w-5"/>
                        )}
                    </button>
                )}
            </div>
        </div>
            {error && <p className="text-red-500 text-sm">{t(`errors.${error}`)}</p> }
            {isFocused && i18n.exists(`input.${id}.description`) && <p className="text-yellow-500 text-sm">{t(`input.${id}.description`)}</p>}
        </div>

    );
};

export default InputField;
