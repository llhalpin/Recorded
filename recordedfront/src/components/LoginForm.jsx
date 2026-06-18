import React, { useState } from 'react';
import { Link, useNavigate, useLocation } from 'react-router-dom';
//import axios from 'axios';
import api from '../api/api';
import { useAuth } from '../context/AuthContext';

//llh 6-21-2025
const LoginForm = () => {
    // Grab login method from our custom auth context
    const { login } = useAuth();

    const navigate = useNavigate();       // Used to redirect after login
    const location = useLocation();       // Used to remember where user came from

    // Form state: username + password
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [errorMessage, setErrorMessage] = useState('');
    const [isLoading, setIsLoading] = useState(false);

    // Go back to the page user was trying to visit, or default to dashboard
    const redirectTo = location.state?.from?.pathname || '/dashboard';

    const handleUsernameChange = (e) => {
        setUsername(e.target.value);
        if (errorMessage) setErrorMessage('');
    };

    const handlePasswordChange = (e) => {
        setPassword(e.target.value);
        if (errorMessage) setErrorMessage('');
    };

    const handleFormSubmit = async (e) => {
        e.preventDefault(); // Stop page reload
        setIsLoading(true);
        setErrorMessage('');

        try {
            // Send login data to backend
            const response = await api.post(
                '/auth/login',
                {
                    username: username,
                    password: password,
                }
            );

            // Try logging in through our context
            //llh 8-5-2025 const loginResult = await login(response.data.token, username);
            const { token, username: returnedUsername, firstName, lastName, email, role } = response.data; //llh 8-5-2025 added firstName, lastName, email, role along with originally username and token
            const userData ={ username: returnedUsername, firstName, lastName, email, role }; //llh 8-5-2025 added firstName, lastName, email, role
            const loginResult = await login(token, userData); //llh 8-5-2025 pass userData instead of username

            if (loginResult.success) {
                // Redirect to the original page or dashboard
                navigate(redirectTo, { replace: true });
            } else {
                setErrorMessage(loginResult.error);
            }
        } catch (err) {
            console.error('Login failed:', err);
            const fallbackMessage = err.response?.data?.message ||
                                    err.response?.data?.error ||
                                    'Login failed. Please try again.';
            setErrorMessage(fallbackMessage);
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <div className="auth-container">
            <div className="auth-card">
                <form onSubmit={handleFormSubmit} className="auth-form">
                    <h2>Welcome Back</h2>
                    <p>Sign in to your account</p>

                    {errorMessage && (
                        <div className="error-message" role="alert">
                            {errorMessage}
                        </div>
                    )}

                    <div className="form-group">
                        <label htmlFor="username">Username</label>
                        <input
                            id="username"
                            type="text"
                            placeholder="Enter your username"
                            value={username}
                            onChange={handleUsernameChange}
                            required
                            disabled={isLoading}
                            autoComplete="username"
                        />
                    </div>

                    <div className="form-group">
                        <label htmlFor="password">Password</label>
                        <input
                            id="password"
                            type="password"
                            placeholder="Enter your password"
                            value={password}
                            onChange={handlePasswordChange}
                            required
                            disabled={isLoading}
                            autoComplete="current-password"
                        />
                    </div>

                    <button 
                        type="submit" 
                        className="submit-button"
                        disabled={isLoading}
                    >
                        {isLoading ? 'Signing in...' : 'Sign In'}
                    </button>

                    <div className="auth-footer">
                        <p>
                            Don't have an account?{' '}
                            <Link to="/auth/register" className="auth-link">
                                Create one here
                            </Link>
                        </p>
                    </div>
                </form>
            </div>
        </div>
    );
};

export default LoginForm;
// This component handles user login, including form submission, error handling, and redirection after successful login.
// It uses the `useAuth` context to access the login function, and `useNavigate` and `useLocation` from React Router for navigation and remembering the page the user was trying to access.
// The form includes fields for username and password, and displays error messages if login fails.
// The component also manages loading state to disable the form while the login request is in progress.
// The form is styled with a class name "auth-container" and includes a link to the registration page for users who don't have an account yet.
// The `useState` hook is used to manage the form state, error messages, and loading state.
// The `axios` library is used to make the HTTP request to the backend for user authentication.
// The `import.meta.env.VITE_API_URL` is used to get the base URL for the API, allowing for easy configuration of the backend endpoint
//3-12-2026 changed to `import.meta.env.VITE_API_URL` from `process.env.REACT_APP_API_URL` due to using Vite 