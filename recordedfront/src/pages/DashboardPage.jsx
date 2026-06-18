import React, { useEffect, useState } from "react";
import { Link } from "react-router-dom";
//3-27-2026 import axios from "axios"; //not needed with Vite
import api from '../api/api'; //3/27/2026 use this for Vite
import { useAuth } from "../context/AuthContext";
import "../components/activity/ActivityList.css";
//import ActivityPage from "../pages/ActivityPage.jsx"; // Import ActivityPage for navigation
//import LogoutButton from "../components/LogoutButton.jsx";


export default function Dashboard() {
    const [activities, setActivities] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    
    const { user, handleApiError } = useAuth();

    useEffect(() => {
        const fetchActivities = async () => {
            try {
                setLoading(true);
                
                // Debug: Log the URL being called
                //const url = `${process.env.REACT_APP_API_BASE_URL}/activity`;
                //const url = `${import.meta.env.VITE_API_URL}/activity/user/${user?.username}`; //llh 7-29-2025 need username in url
                const url =`/activity/user/${user?.username}`; //3-27-2026 changed to this for Vite
                console.log("**Dashboard/fetchActivities:Fetching from URL:", url);
                console.log("**Dashboard/fetchActivities: Current user:", user);
                console.log("**Dashboard/fetchActivities: Current username:", user?.username);
                
                // Try with authentication token if available
                //3-27-2026 comment out since api.js handles this
                //const token = localStorage.getItem('token') || sessionStorage.getItem('token');
                /*const config = token ? {
                    headers: {
                        'Authorization': `Bearer ${token}`,
                        'Content-Type': 'application/json'
                    }
                } : {}; */
                
                //console.log("**Dashboard/fetchActivities: Request config:", config);
                
                //const response = await axios.get(url, config); 
                const response = await api.get(url); //3-27-2026 changed to this when Vite was used instead of CRA

                console.log("**Dashboard/fetchActivities: Response data:", response.data);
                
                setActivities(response.data);
                setError('');
            } catch (err) {
                console.error("**Dashboard/fetchActivities: Full error object:", err);
                console.error("**Dashboard/fetchActivities: Error status:", err.response?.status);
                console.error("**Dashboard/fetchActivities: Error data:", err.response?.data);
                console.error("**Dashboard/fetchActivities: Error headers:", err.response?.headers);
                
                // Provide specific error messages
                if (err.response?.status === 403) {
                    setError("**Dashboard/fetchActivities: Access denied. You may need to log in again.");
                } else if (err.response?.status === 404) {
                    setError("**Dashboard/fetchActivities: Activities endpoint not found. Check your backend configuration.");
                } else {
                    const errorMessage = handleApiError(err);
                    setError(errorMessage);
                }
            } finally {
                setLoading(false);
            }
        };

        // Only fetch if user is logged in
        //if (user) {  //3-27-2026 added username
        if (user && user.username) {
            fetchActivities();
        } else {
            setLoading(false);
            setError("Dashboard.jsx: Please log in to view your activities.");
        }
    }, [handleApiError, user]);

    if (loading) {
        return (
            <div className="dashboard">
                <div className="loading">Loading your activities...</div>
            </div>
        );
    }

    return (
        <div className="dashboard">
            <header className="dashboard-header">
                <h1>Welcome back, {user?.firstName} ({user?.username})!</h1>
                
            </header>

            <main className="dashboard-content">
                <section className="activity-section">
                    <Link to="/activity-page" className="primary-button">
                                Add New Activity
                            </Link>
                    
                
                    {error && (
                        <div className="error-message" style={{ 
                            color: 'red', 
                            backgroundColor: '#ffe6e6', 
                            padding: '10px', 
                            borderRadius: '5px',
                            margin: '10px 0' 
                        }}>
                            <strong>Error:</strong> {error}
                            <br />
                            <small>Check the browser console for more details.</small>
                        </div>
                    )}

                    {activities.length === 0 && !error ? (
                        <div className="no-activities">
                            <p>No activities logged yet. Start your fitness journey today!</p>
                            <Link to="/ActivityPage" className="primary-button">
                                Go to Activity Form
                            </Link>
                        </div>
                    ) : (
                        <div className="activities-list">
                            <h2>Recent Activities:</h2>
                            <table className="activity-table">
                                <thead>
                                    <tr>
                                        <th>Date</th>
                                        <th>Activity Name</th>
                                        <th>Duration</th>
                                        <th>Calories</th>
                                        <th>Notes</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {activities.map((activity) => (
                                        <tr key={activity.id}>
                                            <>
                                                <td>
                                                    <strong>{activity.date}</strong>
                                                </td>
                                                <td>
                                                    {activity.activityName}
                                                </td>
                                                <td>
                                                    {activity.duration}
                                                </td>
                                                <td>
                                                    {activity.calories} 
                                                </td>
                                                <td>
                                                    {activity.notes} 
                                                </td>
                                            </>
                                        </tr>
                                    ))}
                                </tbody>
                            </table>
                           
                        </div>
                    )}
                </section>
            </main>
        </div>
    );
}
