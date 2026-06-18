import React from 'react';
import { Navigate, useLocation } from 'react-router-dom';
import { useAuth } from '../context/AuthContext'; // Gets login info

// This component protects pages from users who aren't logged in
const ProtectedRoute = ({ children }) => {
    const { isAuthenticated, loading } = useAuth(); // Are they logged in?
    const location = useLocation(); // Current page user is trying to access

    // While we're checking if the user is logged in, show a loading message
    if (loading) {
        return (
            <div className="loading-container">
                <div className="loading-spinner">
                    <div className="spinner"></div>
                    <p>Loading...</p>
                </div>
            </div>
        );
    }

    // If the user is not logged in, redirect them to the login page
    if (!isAuthenticated) {
        // "state={{ from: location }}" means after login, they can come back to this page
        return <Navigate to="/auth/login" state={{ from: location }} replace />;
    }

    // If logged in, show the page they were trying to visit
    return children;
};

export default ProtectedRoute;
