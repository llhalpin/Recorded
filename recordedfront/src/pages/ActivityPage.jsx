import React, { useState} from 'react';
//import React, {useEffect, useState} from 'react';
//import { Link } from 'react-router-dom';
//import axios from 'axios';
//import { useAuth } from '../context/AuthContext';
import ActivityList from '../components/activity/ActivityList';
import ActivityForm from '../components/activity/ActivityForm';

const ActivityPage =() => {
    //const [activities, setActivities] = useState([]);
    //const [loading, setLoading] = useState(true);
    //const [error, setError] = useState('');
    //const { user, handleApiError } = useAuth();

    const [refresh, setRefresh] = useState(false);

    const refreshActivities = () => {
        setRefresh((prev) => !prev);
    };

    return (
        <div className="activity-page">
            <h1>Activities Tracker</h1>
            <ActivityForm onActivityAdded={refreshActivities} />
            <ActivityList refresh={refresh} />
        </div>
    );

}
export default ActivityPage;