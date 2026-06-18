// LLHalpin 2025
import React from 'react';
import { 
  BrowserRouter as Router, 
  Routes, 
  Route, 
  Navigate 
} from 'react-router-dom';


import { AuthProvider } from './context/AuthContext'; // Handles login/auth state
import LoginForm from './components/LoginForm';
import RegisterForm from './components/RegisterForm';
import NavBar from './components/NavBar';
import Dashboard from './pages/DashboardPage';
import ProtectedRoute from './components/ProtectedRoute'; // Keeps private pages safe
import Home from './pages/HomePage';
import ActivityPage from './pages/ActivityPage';
//import ActivityForm from './components/activity/ActivityForm.jsx'; // Form for adding activities

function App() {
  return (
    
    <Router future={{ v7_startTransition: true, v7_relativeSplatPath: true }}>

      {/* AuthProvider lets the app know if you're logged in or not */}
      <AuthProvider>

        {/* App container */}
        <div className="app">

          {/* This is the top navigation bar which shows on every page */}
          <NavBar />

          {/* Main content area – which will change based on which page you're on */}
          <main className="main-content">
            <Routes>

              {/* Public Routes: No login needed */}
              <Route path="/" element={<Home />} />
              <Route path="/auth/login" element={<LoginForm />} />
              <Route path="/auth/register" element={<RegisterForm />} />

              {/* Protected Routes: You must be logged in. Here the dashboard and activity-page are displayed */}
              <Route 
                path="/dashboard" 
                element={
                  <ProtectedRoute>
                    <Dashboard />
                  </ProtectedRoute>
                } 
              />

              <Route 
                path="/activity-page"
                element={
                  <ProtectedRoute>
                    <ActivityPage />
                  </ProtectedRoute>
                }
              />


              {/* Catch-All: If you go to a page that doesn’t exist, send 'em home */}
              <Route path="*" element={<Navigate to="/" replace />} />

            </Routes>
          </main>
        </div>
      </AuthProvider>
    </Router>
  );
}

export default App;
