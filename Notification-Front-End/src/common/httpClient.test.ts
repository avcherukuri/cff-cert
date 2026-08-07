import { httpRequest } from './httpClient';

function mockFetchOnce(): jest.Mock {
  const mockFetch = jest.fn().mockResolvedValue({ ok: true, status: 200 });
  global.fetch = mockFetch as unknown as typeof fetch;
  return mockFetch;
}

describe('httpRequest', () => {
  afterEach(() => {
    document.cookie = 'XSRF-TOKEN=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;';
    jest.restoreAllMocks();
  });

  it('sends requests to the configured API base URL with credentials included', async () => {
    const mockFetch = mockFetchOnce();

    await httpRequest('/notification-preferences');

    expect(mockFetch).toHaveBeenCalledWith(
      expect.stringContaining('/notification-preferences'),
      expect.objectContaining({ credentials: 'include' }),
    );
  });

  it('sets the Content-Type header by default', async () => {
    const mockFetch = mockFetchOnce();

    await httpRequest('/notification-preferences');

    const [, init] = mockFetch.mock.calls[0];
    expect(init.headers).toMatchObject({ 'Content-Type': 'application/json' });
  });

  it('attaches the X-XSRF-TOKEN header when a CSRF cookie is present', async () => {
    document.cookie = 'XSRF-TOKEN=test-token-value';
    const mockFetch = mockFetchOnce();

    await httpRequest('/notification-preferences');

    const [, init] = mockFetch.mock.calls[0];
    expect(init.headers).toMatchObject({ 'X-XSRF-TOKEN': 'test-token-value' });
  });

  it('omits the X-XSRF-TOKEN header when no CSRF cookie is present', async () => {
    const mockFetch = mockFetchOnce();

    await httpRequest('/notification-preferences');

    const [, init] = mockFetch.mock.calls[0];
    expect(init.headers).not.toHaveProperty('X-XSRF-TOKEN');
  });

  it('lets caller-supplied headers override the defaults', async () => {
    const mockFetch = mockFetchOnce();

    await httpRequest('/notification-preferences', { headers: { 'Content-Type': 'text/plain' } });

    const [, init] = mockFetch.mock.calls[0];
    expect(init.headers).toMatchObject({ 'Content-Type': 'text/plain' });
  });

  it('preserves caller-supplied init options such as method and body', async () => {
    const mockFetch = mockFetchOnce();

    await httpRequest('/notification-preferences', { method: 'POST', body: JSON.stringify({ a: 1 }) });

    const [, init] = mockFetch.mock.calls[0];
    expect(init.method).toBe('POST');
    expect(init.body).toBe(JSON.stringify({ a: 1 }));
  });
});
