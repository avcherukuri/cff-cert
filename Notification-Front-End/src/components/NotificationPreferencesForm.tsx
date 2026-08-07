import React from 'react';
import { ToggleControl } from './ToggleControl';
import { useNotificationPreferences } from '../hooks/useNotificationPreferences';

export function NotificationPreferencesForm(): JSX.Element {
  const { preferences, isLoading, isSubmitting, error, fieldErrors, isDirty, updateField, submit } =
    useNotificationPreferences();

  if (isLoading) {
    return <p role="status">Loading your notification preferences…</p>;
  }

  if (!preferences) {
    return <p role="alert">{error ?? 'Unable to load your notification preferences.'}</p>;
  }

  return (
    <form
      aria-label="Notification preferences"
      onSubmit={(event) => {
        event.preventDefault();
        void submit();
      }}
    >
      {error && (
        <div role="alert" aria-live="polite">
          {error}
        </div>
      )}

      <ToggleControl
        id="emailEnabled"
        label="Email notifications"
        checked={preferences.emailEnabled}
        onChange={(checked) => updateField('emailEnabled', checked)}
        disabled={isSubmitting}
        errorMessage={fieldErrors.emailEnabled}
      />
      <ToggleControl
        id="smsEnabled"
        label="SMS notifications"
        checked={preferences.smsEnabled}
        onChange={(checked) => updateField('smsEnabled', checked)}
        disabled={isSubmitting}
        errorMessage={fieldErrors.smsEnabled}
      />
      <ToggleControl
        id="pushEnabled"
        label="Push notifications"
        checked={preferences.pushEnabled}
        onChange={(checked) => updateField('pushEnabled', checked)}
        disabled={isSubmitting}
        errorMessage={fieldErrors.pushEnabled}
      />

      <button type="submit" disabled={isSubmitting || !isDirty}>
        {isSubmitting ? 'Saving…' : 'Save changes'}
      </button>
    </form>
  );
}
