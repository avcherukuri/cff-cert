import { validateNotificationPreferencesForm } from './notificationPreferencesValidation';

describe('validateNotificationPreferencesForm', () => {
  it('passes when all three channel values are booleans', () => {
    const result = validateNotificationPreferencesForm({ emailEnabled: true, smsEnabled: false, pushEnabled: true });

    expect(result.isValid).toBe(true);
    expect(result.fieldErrors).toEqual({});
  });

  it('reports a field error for each non-boolean value', () => {
    const result = validateNotificationPreferencesForm({
      emailEnabled: undefined as unknown as boolean,
      smsEnabled: false,
      pushEnabled: undefined as unknown as boolean,
    });

    expect(result.isValid).toBe(false);
    expect(result.fieldErrors.emailEnabled).toBeDefined();
    expect(result.fieldErrors.pushEnabled).toBeDefined();
    expect(result.fieldErrors.smsEnabled).toBeUndefined();
  });
});
