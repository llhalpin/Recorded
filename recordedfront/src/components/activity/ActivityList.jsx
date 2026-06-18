//version as of 3/25/2025
import React, { useEffect, useState } from "react";
import { getActivity, deleteActivity, updateActivity } from "../../services/ActivityService.js";
import "./ActivityList.css";

const ActivityList = ({ refresh }) => {
    const [activity, setActivity] = useState([]);
    const [editingId, setEditingId] = useState(null);
    const [editedActivity, setEditedActivity] = useState({});

    useEffect(() => {
        fetchActivity();
    }, [refresh]);

    const fetchActivity = async () => {
        try {
            const data = await getActivity();
            setActivity(data);
        } catch (error) {
            console.error("Failed to load activities", error);
        }
    };

    const handleDelete = async (id) => {
        try {
            await deleteActivity(id);
            setActivity((prevActivity) => prevActivity.filter(activity => activity.id !== id));
        } catch (error) {
            console.error("Failed to delete activity", error);
        }
    };

    const handleEditClick = (activity) => {
        setEditingId(activity.id);
        setEditedActivity({ ...activity }); // Copy current activity data for editing
    };

    const handleChange = (e) => {
        setEditedActivity({ ...editedActivity, [e.target.name]: e.target.value });
    };

    const handleSave = async (id) => {
        try {
            await updateActivity(id, editedActivity);
            setActivity((prevActivity) =>
                prevActivity.map(ex => (ex.id === id ? editedActivity : ex))
            );
            setEditingId(null); // Exit edit mode
        } catch (error) {
            console.error("ActivityList/handleSave: Failed to update activity", error);
        }
    };

    return (
        <div className="activity-list">
            <h2>Activities Table</h2>
            {activity.length === 0 ? (
                <p>No activities recorded yet. Add one to get started!</p>
            ) : (
                <table className="activity-table">
                    <thead>
                        <tr>
                            <th>Date</th>
                            <th>Activity Name</th>
                            <th>Duration</th>
                            <th>Calories</th>
                            <th>Notes</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        {activity.map((activity) => (
                            <tr key={activity.id}>
                                {editingId === activity.id ? (
                                    <>
                                        <td>
                                            <input type="date" name="date" value={editedActivity.date} onChange={handleChange} />
                                        </td>
                                        <td>
                                            <input type="text" name="activityName" value={editedActivity.activityName} onChange={handleChange} />
                                        </td>
                                        <td>
                                            <input type="number" name="duration" value={editedActivity.duration} onChange={handleChange} />
                                        </td>
                                        <td>
                                            <input type="number" name="calories" value={editedActivity.calories} onChange={handleChange} />
                                        </td>
                                        <td>
                                            <input type="text" name="notes" value={editedActivity.notes} onChange={handleChange} />
                                        </td>
                                        <td>
                                            <button onClick={() => handleSave(activity.id)}>💾 Save</button>
                                            <button onClick={() => setEditingId(null)}>❌ Cancel</button>
                                        </td>
                                    </>
                                ) : (
                                    <>
                                        <td>{activity.date}</td>
                                        <td>{activity.activityName}</td>
                                        <td>{activity.duration} mins</td>
                                        <td>{activity.calories}</td>
                                        <td>{activity.notes}</td>
                                        <td>
                                            <button onClick={() => handleEditClick(activity)}>✏️ Edit</button>
                                            <button onClick={() => handleDelete(activity.id)}>❌ Delete</button>
                                        </td>
                                    </>
                                )}
                            </tr>
                        ))}
                    </tbody>
                </table>
            )}
        </div>
    );
};

export default ActivityList;

