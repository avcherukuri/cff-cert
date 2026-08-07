import { NotificationPreferencesUpdateRequest, ValidationResult } from '../common/types/notificationPreferences';

export function validateNotificationPreferencesForm(form: NotificationPreferencesUpdateRequest): ValidationResult {
  const fieldErrors: Record<string, string> = {};

  if (typeof form.emailEnabled !== 'boolean') {
    fieldErrors.emailEnabled = 'Email preference must be set.';
  }
  if (typeof form.smsEnabled !== 'boolean') {
    fieldErrors.smsEnabled = 'SMS preference must be set.';
  }
  if (typeof form.pushEnabled !== 'boolean') {
    fieldErrors.pushEnabled = 'Push preference must be set.';
  }

  return { isValid: Object.keys(fieldErrors).length === 0, fieldErrors };
}
