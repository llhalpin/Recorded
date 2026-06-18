import React, { useState } from 'react';
import { Link, useNavigate, useLocation } from 'react-router-dom';
//import axios from 'axios';
import api from '../api/api';  //3-16-2026 use this instead of axios when moved to using vite
import { useAuth } from '../context/AuthContext'; // custom context for auth

//llh 06-23-2025
const RegisterForm = () => {
  const { login } = useAuth(); // login function from AuthContext
  const navigate = useNavigate(); // used to redirect after login
  const location = useLocation(); // used to check where user was trying to go before

  // Form fields
  const [formData, setFormData] = useState({
    username: '',
    email: '',
    firstName: '',
    lastName: '',
    password: '',
    confirmPassword: '',
  });

  // Error message and loading state
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  // Where to go after successful login (or default to dashboard)
  const from = location.state?.from?.pathname || '/dashboard';

  // Handles typing in the form fields
  const handleChange = (e) => {
    const { name, value } = e.target;

    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));

    // Clear error when user starts typing
    if (error) setError('');
  };

  // Check if all inputs are valid before submitting
  const validateForm = () => {
    const { username, email, firstName, lastName, password, confirmPassword } = formData;

    if (username.length < 3) {
      setError('Username must be at least 3 characters long');
      return false;
    }

    if (password.length < 6) {
      setError('Password must be at least 6 characters long');
      return false;
    }

    if (password !== confirmPassword) {
      setError('Passwords do not match');
      return false;
    }

    const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailPattern.test(email)) {
      setError('Please enter a valid email address');
      return false;
    }

    if (!firstName.trim() || !lastName.trim()) {
      setError('First and last name are required');
      return false;
    }

    return true;
  };

  // Submit the form
  const handleSubmit = async (e) => {
    e.preventDefault(); // stop page reload

    // Validate before sending to backend
    if (!validateForm()) return;

    setLoading(true);
    setError('');

    try {
      // Send registration data to backend
      //3-16-2026 changed from axios.post to api.post and from `${import.meta.env.VITE_API_URL}/auth/register` to '/auth/register
      const response = await api.post(
        '/auth/register',
        {
          username: formData.username,
          email: formData.email,
          firstName: formData.firstName,
          lastName: formData.lastName,
          password: formData.password,
        }
      );

      // Automatically log them in
      const result = await login(response.data.token, formData.username);

      if (result.success) {
        navigate(from, { replace: true }); // send to dashboard
      } else {
        setError(result.error);
      }
    } catch (err) {
      console.error('Registration error:', err);
      const errorMessage =
        err.response?.data?.message ||
        err.response?.data?.error ||
        'Registration failed. Please try again.';
      setError(errorMessage);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="auth-container">
      <div className="auth-card">
        <form onSubmit={handleSubmit} className="auth-form">
          <h2>Create Account</h2>
          <p>Join us to start tracking your fitness journey</p>

          {error && <div className="error-message">{error}</div>}

          {/* First and Last Name */}
          <div className="form-row">
            <div className="form-group">
              <label htmlFor="firstName">First Name</label>
              <input
                id="firstName"
                name="firstName"
                type="text"
                value={formData.firstName}
                onChange={handleChange}
                placeholder="First Name"
                disabled={loading}
                required
              />
            </div>
            <div className="form-group">
              <label htmlFor="lastName">Last Name</label>
              <input
                id="lastName"
                name="lastName"
                type="text"
                value={formData.lastName}
                onChange={handleChange}
                placeholder="Last Name"
                disabled={loading}
                required
              />
            </div>
          </div>

          {/* Username */}
          <div className="form-group">
            <label htmlFor="username">Username</label>
            <input
              id="username"
              name="username"
              type="text"
              value={formData.username}
              onChange={handleChange}
              placeholder="Username (min 3 chars)"
              disabled={loading}
              required
            />
          </div>

          {/* Email */}
          <div className="form-group">
            <label htmlFor="email">Email</label>
            <input
              id="email"
              name="email"
              type="email"
              value={formData.email}
              onChange={handleChange}
              placeholder="you@example.com"
              disabled={loading}
              required
            />
          </div>

          {/* Password and Confirm */}
          <div className="form-group">
            <label htmlFor="password">Password</label>
            <input
              id="password"
              name="password"
              type="password"
              value={formData.password}
              onChange={handleChange}
              placeholder="Password (min 6 chars)"
              disabled={loading}
              required
            />
          </div>

          <div className="form-group">
            <label htmlFor="confirmPassword">Confirm Password</label>
            <input
              id="confirmPassword"
              name="confirmPassword"
              type="password"
              value={formData.confirmPassword}
              onChange={handleChange}
              placeholder="Re-type password"
              disabled={loading}
              required
            />
          </div>

          {/* Submit Button */}
          <button type="submit" className="submit-button" disabled={loading}>
            {loading ? 'Creating Account...' : 'Create Account'}
          </button>

          {/* Link to Login */}
          <div className="auth-footer">
            <p>
              Already have an account?{' '}
              <Link to="/auth/login" className="auth-link">
                Sign in here
              </Link>
            </p>
          </div>
        </form>
      </div>
    </div>
  );
};

export default RegisterForm;
