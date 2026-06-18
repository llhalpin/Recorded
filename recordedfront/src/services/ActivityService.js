//version as of 6/30/2025 renamed it to Activity from Exercise
import api from "../api/api.js";
import { getUsername } from "../Auth"; //llh 06-15-2025 added this to globally access username

// Update an existing activity by id
export const updateActivity = async (id, activity) => {
    try {
      const username = getUsername(); //llh 08-13-2025 getting username from Auth.js
      console.log("**ActivityService.js/Updating activity for user:", username); //llh 08-13-2025  
      const response = await api.put(`/activity/user/${username}/${id}`, activity); //llh 08-13-2025 add username to path
        return response.data;
    } catch (error) {
        console.error("ActivityService.js/updateActivity: Error updating activity:", error);
        throw error;
    }
};

 //Create a new activity by adding it to the database
 export const createActivity = async (activity) => {
  try {
  
    //const username = localStorage.getItem('username'); //llh 06-30-2025
    const username = getUsername(); //llh 06-15-2025 getting username from Auth.js
    console.log("**ActivityService.js/Creating activity for user:", username); //llh 06-30-2025
    const response =await api.post(`/activity/user/${username}`, activity); //llh 6-30-2025 add username to path
    return response.data;
  } catch (error) {
    console.error("Error creating activity:", error);
    throw error;
  }  
};

//Get all activity by fetching them from the database (Read)
export const getActivity = async () => {
  try {
    //const response = await api.get("/activity");
    const response = await api.get("/activity/user/" + getUsername()); //llh 7-30-2025 add username to path
    return response.data; // Extract the data from response
  } catch (error) {
    console.error("Error fetching activities:", error);
    return []; // Return empty array on error
  }
};

  //Delete an existing activity by id from the database
  export const deleteActivity = async (id) => {
    try {
      const username = getUsername(); //llh 08-13-2025 getting username from Auth.js
      //const response = await api.delete(`/activity/${id}`);
      const response = await api.delete(`/activity/user/${username}/${id}`); //llh 7-30-2025 add username to path
      return response.data;
    } catch (error) {
      console.error("Error deleting activity:", error);
      throw error;
    }
  };


