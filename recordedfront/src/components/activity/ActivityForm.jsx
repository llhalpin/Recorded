//version as of 3/25/2025
import React, {useState} from "react";
import { createActivity } from "../../services/ActivityService.js";
import "./ActivityForm.css";

const ActivityForm =({ onActivityAdded }) => {
    const [activity, setActivity] = useState({
        activityName: "",
        date: "",
        duration: "",
        calories:"",
        notes:"",
    });

    const handleChange =(e) => {
        setActivity({ ...activity, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            await createActivity(activity);
            console.log("**ActivityForm/handleSubmit: Activity added successfully:", activity);
            onActivityAdded(); //refresh list
            setActivity({activityName:"", date:"", duration:"", calories:"", notes:""});

        } catch (error) {
            console.error("Error adding activity", error);
        }
    };

    return(
        <form className="activity-form" onSubmit={handleSubmit}>
            <input type="text" name="activityName" placeholder="activityName" value={activity.activityName} onChange={handleChange} required />
            <input type="date" name="date" value={activity.date} onChange={handleChange} required />
            <input type="number" name="duration" placeholder="Duration (min)" value={activity.duration} onChange={handleChange} required />
            <input type="number" name="calories" placeholder="Calories" value={activity.calories} onChange={handleChange} />
            <textarea name="notes" placeholder="Notes" value={activity.notes} onChange={handleChange} />
            <button type="submit">➕ Add New Activity</button>
        </form>
    );
};

export default ActivityForm;

