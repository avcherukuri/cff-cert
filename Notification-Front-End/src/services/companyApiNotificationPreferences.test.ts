import {
  companyApiGetNotificationPreferences,
  companyApiPatchNotificationPreferences,
  companyApiUpdateNotificationPreferences,
} from './companyApiNotificationPreferences';

function mockFetchOnce(status: number, body: unknown): void {
  global.fetch = jest.fn().mockResolvedValue({
    ok: status >= 200 && status < 300,
    status,
    json: () => Promise.resolve(body),
  }) as unknown as typeof fetch;
}

describe('companyApiNotificationPreferences', () => {
  afterEach(() => {
    jest.restoreAllMocks();
  });

  it('companyApiGetNotificationPreferences returns the parsed response on success', async () => {
    mockFetchOnce(200, { emailEnabled: true, smsEnabled: false, pushEnabled: true, updatedAt: null });

    const result = await companyApiGetNotificationPreferences();

    expect(result.emailEnabled).toBe(true);
    expect(fetch).toHaveBeenCalledWith(
      expect.stringContaining('/notification-preferences'),
      expect.objectContaining({ method: 'GET', credentials: 'include' }),
    );
  });

  it('companyApiUpdateNotificationPreferences sends a PUT with the full payload', async () => {
    mockFetchOnce(200, { emailEnabled: false, smsEnabled: true, pushEnabled: false, updatedAt: '2026-08-05T00:00:00Z' });

    await companyApiUpdateNotificationPreferences({ emailEnabled: false, smsEnabled: true, pushEnabled: false });

    expect(fetch).toHaveBeenCalledWith(
      expect.stringContaining('/notification-preferences'),
      expect.objectContaining({ method: 'PUT' }),
    );
  });

  it('companyApiPatchNotificationPreferences sends a PATCH', async () => {
    mockFetchOnce(200, { emailEnabled: true, smsEnabled: true, pushEnabled: true, updatedAt: '2026-08-05T00:00:00Z' });

    await companyApiPatchNotificationPreferences({ smsEnabled: true });

    expect(fetch).toHaveBeenCalledWith(
      expect.stringContaining('/notification-preferences'),
      expect.objectContaining({ method: 'PATCH' }),
    );
  });

  it('translates a 401 into a sanitized UNAUTHENTICATED error without leaking the raw body', async () => {
    mockFetchOnce(401, { secretInternalDetail: 'should never surface' });

    await expect(companyApiGetNotificationPreferences()).rejects.toMatchObject({
      code: 'UNAUTHENTICATED',
    });
  });

  it('translates a 400 into a VALIDATION_ERROR carrying fieldErrors', async () => {
    mockFetchOnce(400, {
      error: 'VALIDATION_ERROR',
      message: 'Request validation failed',
      fieldErrors: [{ field: 'emailEnabled', message: 'must not be null' }],
    });

    await expect(companyApiUpdateNotificationPreferences({ emailEnabled: null as unknown as boolean, smsEnabled: true, pushEnabled: true }))
      .rejects.toMatchObject({
        code: 'VALIDATION_ERROR',
        fieldErrors: [{ field: 'emailEnabled', message: 'must not be null' }],
      });
  });

  it('translates a 500 into a generic UNKNOWN error without leaking internals', async () => {
    mockFetchOnce(500, { message: 'stack trace should not leak' });

    await expect(companyApiGetNotificationPreferences()).rejects.toMatchObject({
      code: 'UNKNOWN',
      userMessage: 'Something went wrong. Please try again later.',
    });
  });
});
