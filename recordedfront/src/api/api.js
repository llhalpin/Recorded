// 6/30/2025 This sets up a custom version of Axios to talk to backend API
import axios from "axios";

// Use the environment variable for the backend API
const API_BASE_URL = "/api";
// Create an Axios instance with default settings
const api = axios.create({
    baseURL: API_BASE_URL, // All requests will start with this URL
    headers: {
        "Content-Type": "application/json", // We're sending JSON data
    },
    withCredentials: true, // Send cookies along with requests (if needed)
});

// ================================
// Add auth token to every request
// ================================
api.interceptors.request.use(
    (config) => {
        // Check ALL possible token locations
        const tokenSources = {
            localStorage_authToken: localStorage.getItem("authToken"),
            localStorage_token: localStorage.getItem("token"),
            localStorage_accessToken: localStorage.getItem("accessToken"),
            sessionStorage_authToken: sessionStorage.getItem("authToken"),
            sessionStorage_token: sessionStorage.getItem("token"),
            existingHeader: config.headers.Authorization
        };

        console.log('** All token sources:', tokenSources);
        console.log('** API Base URL: ', API_BASE_URL);

// 6/30/2025 to see if jwt token is valid
const token = localStorage.getItem('token');
if (token) {
    const payload = JSON.parse(atob(token.split('.')[1]));
    console.log('** JWT Payload:', payload);
    console.log('** Token expires at:', new Date(payload.exp * 1000));
    console.log('** Current time:', new Date());
    console.log('** Token expired?', Date.now() / 1000 > payload.exp);
}

        // Find which token source has a value
        const tokenSource = Object.entries(tokenSources).find(([key, value]) => value && value !== 'undefined');
        
        if (tokenSource) {
            const [source, token] = tokenSource;
            console.log(`** Found token from: ${source}`);
            
            // Only add if it's not already in the header
            if (source !== 'existingHeader') {
                config.headers.Authorization = `Bearer ${token}`;
                console.log('** Added Authorization header with token');
            } else {
                console.log('** Token already exists in header');
            }
        } else {
            console.log('** No auth token found anywhere');
        }

        return config; // Continue with the request
    },
    (error) => {
        // If something went wrong setting up the request, reject it
        return Promise.reject(error);
    }
);

// ==========================================
// Handle errors like expired or missing token
// ==========================================
api.interceptors.response.use(
    (response) => {
        // If everything is fine, just return the response
        return response;
    },
    (error) => {
        // If we get a 401 (unauthorized), the token might be expired or invalid
        if (error.response?.status === 401) {
            // Remove the bad token from storage
            localStorage.removeItem("authToken");

            // Redirect the user to the login page
            window.location.href = "/login";
        }

        return Promise.reject(error);
    }
);

export default api;