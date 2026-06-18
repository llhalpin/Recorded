let cachedUsername = null;
//llh 06-15-2025 added this to cache username
export const getUsername = () => {
    if (cachedUsername) 
        return cachedUsername;

    const token = localStorage.getItem('token');
    const tokenPayload = JSON.parse(atob(token.split('.')[1]));
    cachedUsername = tokenPayload.sub;
    return cachedUsername;
};
