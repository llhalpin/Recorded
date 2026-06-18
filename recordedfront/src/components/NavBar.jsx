import React from 'react';
import { Link, useLocation } from 'react-router-dom'; // For navigating between pages
import { useAuth } from '../context/AuthContext';     // Custom hook to get auth info
import LogoutButton from './LogoutButton';            // Button to log out
import "./NavBar.css";

const NavBar = () => {
    // Pull out whether the user is logged in and their info
    const { isAuthenticated, user } = useAuth();

    // Get current URL path (so we know what page we're on)
    const location = useLocation();

    // Helper function: checks if the current path matches the link
    const isActive = (path) => location.pathname === path;

    return (
        <nav className="navbar">
            {/* Brand/Logo */}
            <div className="navbar-brand">
                <Link to="/" className="brand-link">
                    <h2>Recorded</h2> 
                </Link>
            </div>

            {/* Navigation Links */}
            <div className="navbar-menu">
                <ul className="navbar-nav">
                    {/* Home link is always shown */}
                    <li className="nav-item">
                        <Link 
                            to="/" 
                            className={`nav-link ${isActive('/') ? 'active' : ''}`}
                        >
                            Home
                        </Link>
                    </li>

                    {/* If the user is logged in */}
                    {isAuthenticated ? (
                        <>
                            <li className="nav-item">
                                <Link 
                                    to="/dashboard" 
                                    className={`nav-link ${isActive('/dashboard') ? 'active' : ''}`}
                                >
                                    Dashboard
                                </Link>
                            </li>
                            <li className="nav-item user-info">
                                <span className="user-welcome">
                                    Welcome, {user?.firstName} {/* Greet the user */}
                                </span>
                            </li>
                            <li className="nav-item">
                                <LogoutButton /> {/* Log out button */}
                            </li>
                        </>
                    ) : (
                        // If the user is *not* logged in
                        <>
                            <li className="nav-item">
                                <Link 
                                    to="/auth/login" 
                                    className={`nav-link ${isActive('/auth/login') ? 'active' : ''}`}
                                >
                                    Login
                                </Link>
                            </li>
                            <li className="nav-item">
                                <Link 
                                    to="/auth/register" 
                                    className={`nav-link ${isActive('/auth/register') ? 'active' : ''}`}
                                >
                                    Register
                                </Link>
                            </li>
                        </>
                    )}
                </ul>
            </div>
        </nav>
    );
};

export default NavBar;
// This NavBar component provides a navigation bar for the application.