import { act, renderHook, waitFor } from '@testing-library/react';
import { useNotificationPreferences } from './useNotificationPreferences';
import * as api from '../services/companyApiNotificationPreferences';

jest.mock('../services/companyApiNotificationPreferences');

const mockedApi = api as jest.Mocked<typeof api>;

describe('useNotificationPreferences', () => {
  afterEach(() => {
    jest.resetAllMocks();
  });

  it('loads preferences on mount', async () => {
    mockedApi.companyApiGetNotificationPreferences.mockResolvedValue({
      emailEnabled: true,
      smsEnabled: false,
      pushEnabled: true,
      updatedAt: null,
    });

    const { result } = renderHook(() => useNotificationPreferences());

    expect(result.current.isLoading).toBe(true);

    await waitFor(() => expect(result.current.isLoading).toBe(false));
    expect(result.current.preferences).toEqual({ emailEnabled: true, smsEnabled: false, pushEnabled: true });
    expect(result.current.error).toBeNull();
  });

  it('surfaces a user-facing error when the initial fetch fails', async () => {
    mockedApi.companyApiGetNotificationPreferences.mockRejectedValue({
      code: 'UNKNOWN',
      userMessage: 'Something went wrong. Please try again later.',
      fieldErrors: [],
    });

    const { result } = renderHook(() => useNotificationPreferences());

    await waitFor(() => expect(result.current.isLoading).toBe(false));
    expect(result.current.error).toBe('Something went wrong. Please try again later.');
  });

  it('tracks dirty state and submits the updated preferences', async () => {
    mockedApi.companyApiGetNotificationPreferences.mockResolvedValue({
      emailEnabled: true,
      smsEnabled: false,
      pushEnabled: true,
      updatedAt: null,
    });
    mockedApi.companyApiUpdateNotificationPreferences.mockResolvedValue({
      emailEnabled: true,
      smsEnabled: true,
      pushEnabled: true,
      updatedAt: '2026-08-05T00:00:00Z',
    });

    const { result } = renderHook(() => useNotificationPreferences());
    await waitFor(() => expect(result.current.isLoading).toBe(false));

    act(() => {
      result.current.updateField('smsEnabled', true);
    });
    expect(result.current.isDirty).toBe(true);

    await act(async () => {
      await result.current.submit();
    });

    expect(mockedApi.companyApiUpdateNotificationPreferences).toHaveBeenCalledWith({
      emailEnabled: true,
      smsEnabled: true,
      pushEnabled: true,
    });
    expect(result.current.isDirty).toBe(false);
    expect(result.current.isSubmitting).toBe(false);
  });

  it('maps backend field errors onto the form on submit failure', async () => {
    mockedApi.companyApiGetNotificationPreferences.mockResolvedValue({
      emailEnabled: true,
      smsEnabled: false,
      pushEnabled: true,
      updatedAt: null,
    });
    mockedApi.companyApiUpdateNotificationPreferences.mockRejectedValue({
      code: 'VALIDATION_ERROR',
      userMessage: 'Some of the values provided were invalid.',
      fieldErrors: [{ field: 'emailEnabled', message: 'must not be null' }],
    });

    const { result } = renderHook(() => useNotificationPreferences());
    await waitFor(() => expect(result.current.isLoading).toBe(false));

    await act(async () => {
      await result.current.submit();
    });

    expect(result.current.error).toBe('Some of the values provided were invalid.');
    expect(result.current.fieldErrors.emailEnabled).toBe('must not be null');
  });
});
