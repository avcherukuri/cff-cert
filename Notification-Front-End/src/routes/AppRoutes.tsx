import React from 'react';
import { Navigate, Route, Routes } from 'react-router-dom';
import { NotificationPreferencesPage } from '../components/NotificationPreferencesPage';

export function AppRoutes(): JSX.Element {
  return (
    <Routes>
      <Route path="/" element={<Navigate to="/settings/notifications" replace />} />
      <Route path="/settings/notifications" element={<NotificationPreferencesPage />} />
    </Routes>
  );
}
