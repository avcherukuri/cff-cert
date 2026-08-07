import { getEnvironment } from '../environment/environment';

function readCsrfCookie(): string | null {
  const match = document.cookie.match(/(?:^|;\s*)XSRF-TOKEN=([^;]+)/);
  return match ? decodeURIComponent(match[1]) : null;
}

export async function httpRequest(path: string, init: RequestInit = {}): Promise<Response> {
  const csrfToken = readCsrfCookie();
  const headers: HeadersInit = {
    'Content-Type': 'application/json',
    ...(csrfToken ? { 'X-XSRF-TOKEN': csrfToken } : {}),
    ...init.headers,
  };

  return fetch(`${getEnvironment().apiBaseUrl}${path}`, {
    ...init,
    credentials: 'include',
    headers,
  });
}
