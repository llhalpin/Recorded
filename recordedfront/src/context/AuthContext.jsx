import React, { createContext, useContext, useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
//import './AuthContext.css'; // Import any styles you need for this context

//llh 2025-06-21
// Create a context to store our auth data (like if user is logged in)
const AuthContext = createContext();

// This function lets any component use our auth context easily
export const useAuth = () => {
    const context = useContext(AuthContext);
    if (!context) {
        throw new Error('useAuth must be used within an AuthProvider');
    }
    return context;
};

// This component wraps around parts of the app that need to know if the user is logged in
export const AuthProvider = ({ children }) => {
    const [isAuthenticated, setIsAuthenticated] = useState(false); 
    const [user, setUser] = useState(null); // User info like username
    const [loading, setLoading] = useState(true); 
    const navigate = useNavigate(); // To redirect users in the app

    // When the app starts, check if the user is already logged in
    useEffect(() => {
        const token = localStorage.getItem('token'); // Check browser for saved token
        const storedUser = localStorage.getItem('user'); // llh 8-5-2025 changed from username to user data
        
        if (token && storedUser) {
            setIsAuthenticated(true); //they're logged in
            setUser(JSON.parse(storedUser)); // llh 8-5-2025 changed from wrapping in an object to just using the user data directly
            axios.defaults.headers.common['Authorization'] = `Bearer ${token}`; // Send token with every axios request
        }

        setLoading(false); // Done checking
    }, []);

    // When a user logs in, call this
    const login = async (token, userData) => {
        try {
            // Save token + username in the browser
            localStorage.setItem('token', token);
            localStorage.setItem('username', userData.username); // Save username
            localStorage.setItem('user', JSON.stringify(userData));

            // Attach token to axios for future requests
            axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;

            setIsAuthenticated(true);
            setUser(userData );

            return { success: true };
        } catch (error) {
            console.error('AuthContext: Login error:', error);
            return { success: false, error: 'AuthContext: Login failed' };
        }
    };

    // When user logs out, call this
    const logout = async () => {
        try {
            // Remove token + username from browser
            localStorage.removeItem('token');
            localStorage.removeItem('user'); //llh 8-5-2025 remove user data instead of just username

            // Remove token from axios
            delete axios.defaults.headers.common['Authorization'];

            // Reset our app's state
            setIsAuthenticated(false);
            setUser(null);

            // Send user back to homepage
            navigate('/');

            return { success: true };
        } catch (error) {
            console.error('Logout error:', error);
            return { success: false, error: 'Logout failed' };
        }
    };

    // Handle errors from the server, especially if the token is no longer valid
    const handleApiError = (error) => {
        if (error.response?.status === 401) {
            logout(); // Log out user if token is expired
            return 'Session expired. Please log in again.';
        }
        return error.response?.data?.message || 'An error occurred';
    };

    // All the info and functions we want other components to use
    const value = {
        isAuthenticated,
        user,
        loading,
        login,
        logout,
        handleApiError
    };

    // Wrap children in the AuthContext so they can access everything above
    return (
        <AuthContext.Provider value={value}>
            {children}
        </AuthContext.Provider>
    );
};
