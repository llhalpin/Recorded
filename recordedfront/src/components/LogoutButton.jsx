import React from 'react';
import { useAuth } from '../context/AuthContext';
import { useNavigate } from 'react-router-dom';

const LogoutButton = () => {
    const { logout } = useAuth();
    const navigate = useNavigate();

    const handleLogout = async () => {
        await logout();
        navigate('/');
    };

    return (
        <button onClick={handleLogout} className="logout-button">
            Log out
        </button>
    );
};

export default LogoutButton;
// This LogoutButton component provides a button that, when clicked, logs the user out and redirects them to the login page.
// It uses the `useAuth` hook to access the logout function from the authentication context and the `useNavigate` hook from React Router for navigation.
// The button is styled with a class name "logout-button"