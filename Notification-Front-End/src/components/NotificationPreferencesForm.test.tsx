import { render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { NotificationPreferencesForm } from './NotificationPreferencesForm';
import { useNotificationPreferences, UseNotificationPreferencesResult } from '../hooks/useNotificationPreferences';

jest.mock('../hooks/useNotificationPreferences');

const mockedUseNotificationPreferences = useNotificationPreferences as jest.MockedFunction<
  typeof useNotificationPreferences
>;

function buildResult(overrides: Partial<UseNotificationPreferencesResult> = {}): UseNotificationPreferencesResult {
  return {
    preferences: { emailEnabled: true, smsEnabled: false, pushEnabled: true },
    isLoading: false,
    isSubmitting: false,
    error: null,
    fieldErrors: {},
    isDirty: false,
    updateField: jest.fn(),
    submit: jest.fn().mockResolvedValue(undefined),
    ...overrides,
  };
}

describe('NotificationPreferencesForm', () => {
  afterEach(() => {
    jest.resetAllMocks();
  });

  it('renders a loading status while preferences are being fetched', () => {
    mockedUseNotificationPreferences.mockReturnValue(buildResult({ isLoading: true, preferences: null }));

    render(<NotificationPreferencesForm />);

    expect(screen.getByRole('status')).toHaveTextContent('Loading your notification preferences…');
  });

  it('renders an error and no form when preferences failed to load', () => {
    mockedUseNotificationPreferences.mockReturnValue(
      buildResult({ preferences: null, error: 'Unable to load your notification preferences.' }),
    );

    render(<NotificationPreferencesForm />);

    expect(screen.getByRole('alert')).toHaveTextContent('Unable to load your notification preferences.');
    expect(screen.queryByRole('form', { name: 'Notification preferences' })).not.toBeInTheDocument();
  });

  it('disables the save button when the form is not dirty', () => {
    mockedUseNotificationPreferences.mockReturnValue(buildResult({ isDirty: false }));

    render(<NotificationPreferencesForm />);

    expect(screen.getByRole('button', { name: 'Save changes' })).toBeDisabled();
  });

  it('enables the save button when the form is dirty and calls submit on click', async () => {
    const submit = jest.fn().mockResolvedValue(undefined);
    mockedUseNotificationPreferences.mockReturnValue(buildResult({ isDirty: true, submit }));

    render(<NotificationPreferencesForm />);

    const saveButton = screen.getByRole('button', { name: 'Save changes' });
    expect(saveButton).toBeEnabled();

    await userEvent.click(saveButton);

    expect(submit).toHaveBeenCalledTimes(1);
  });

  it('shows "Saving…" and disables toggles while submitting', () => {
    mockedUseNotificationPreferences.mockReturnValue(buildResult({ isSubmitting: true, isDirty: true }));

    render(<NotificationPreferencesForm />);

    expect(screen.getByRole('button', { name: 'Saving…' })).toBeDisabled();
    expect(screen.getByRole('switch', { name: 'Email notifications' })).toBeDisabled();
  });

  it('forwards field errors to the corresponding toggle', () => {
    mockedUseNotificationPreferences.mockReturnValue(
      buildResult({ fieldErrors: { emailEnabled: 'must not be null' } }),
    );

    render(<NotificationPreferencesForm />);

    const toggle = screen.getByRole('switch', { name: 'Email notifications' });
    const alerts = screen.getAllByRole('alert');
    const fieldAlert = alerts.find((alert) => alert.textContent === 'must not be null');

    expect(fieldAlert).toBeDefined();
    expect(toggle).toHaveAttribute('aria-describedby', fieldAlert!.id);
  });

  it('calls updateField when a toggle is changed', async () => {
    const updateField = jest.fn();
    mockedUseNotificationPreferences.mockReturnValue(buildResult({ updateField }));

    render(<NotificationPreferencesForm />);

    await userEvent.click(screen.getByRole('switch', { name: 'SMS notifications' }));

    expect(updateField).toHaveBeenCalledWith('smsEnabled', true);
  });

  it('shows a top-level error banner alongside the form when a submit error occurs', () => {
    mockedUseNotificationPreferences.mockReturnValue(
      buildResult({ error: 'Some of the values provided were invalid.' }),
    );

    render(<NotificationPreferencesForm />);

    expect(screen.getByText('Some of the values provided were invalid.')).toBeInTheDocument();
    expect(screen.getByRole('button', { name: 'Save changes' })).toBeInTheDocument();
  });
});
