import React from 'react';
// Link lets you move around the app without refreshing the page
import { Link } from 'react-router-dom';
// useAuth gives us access to whether the user is logged in and who they are
import { useAuth } from '../context/AuthContext';

const HomePage = () => {
    // Pull login status and user info from our context
    const { isAuthenticated, user } = useAuth();

    // If user is logged in, show personalized welcome
    if (isAuthenticated) {
        return (
            <div className="home-container">
                <div className="home-content">
                    <h1>Welcome back, {user?.name} ({user?.username})!</h1>
                    <p>Ready to continue your fitness journey?</p>
                    <div className="home-actions">
                        <Link to="/dashboard" className="primary-button">
                            Go to Dashboard
                        </Link>
                    </div>
                </div>
            </div>
        );
    }

    // If user is NOT logged in, show landing page with info and signup/login buttons
    return (
        <div className="home-container">
            <div className="hero-section">
                <div className="hero-content">
                    <h1>Welcome to Recorded</h1>
                    <p className="hero-subtitle">
                        Track your fitness journey, monitor your progress, and achieve your goals.
                    </p>
                    <p className="hero-description">
                        Keep a detailed log of your workouts, track your activity duration, 
                        and watch your fitness progress over time.
                    </p>
                    
                    <div className="hero-actions">
                        {/* Link to Register */}
                        <Link to="/auth/register" className="primary-button">
                            Get Started By Registering
                        </Link>
                        {/* Link to Login */}
                        <Link to="/auth/login" className="secondary-button">
                        <p>Have an account already, sign in </p>
                        </Link>
                    </div>
                </div>
            </div>

            <div className="features-section">
                <h2>Why Choose Recorded?</h2>
                <div className="features-grid">
                    <div className="feature-card">
                        <h3>📊 Track Progress</h3>
                        <p>Monitor your workout duration and see your improvement over time.</p>
                    </div>
                    <div className="feature-card">
                        <h3>📅 Activity Log</h3>
                        <p>Keep a detailed record of all your workouts with dates and activities.</p>
                    </div>
                    <div className="feature-card">
                        <h3>🎯 Stay Motivated</h3>
                        <p>Visualize your fitness journey and stay motivated to reach your goals.</p>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default HomePage;
