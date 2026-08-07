import { httpRequest } from '../common/httpClient';
import {
  FieldErrorDetail,
  NotificationPreferencesApiError,
  NotificationPreferencesErrorCode,
  NotificationPreferencesPatchRequest,
  NotificationPreferencesResponse,
  NotificationPreferencesUpdateRequest,
} from '../common/types/notificationPreferences';

const PATH = '/notification-preferences';

interface BackendValidationErrorBody {
  error?: string;
  message?: string;
  fieldErrors?: FieldErrorDetail[];
}

async function toApiError(response: Response): Promise<NotificationPreferencesApiError> {
  if (response.status === 401) {
    return { code: 'UNAUTHENTICATED', userMessage: 'Your session has expired. Please sign in again.', fieldErrors: [] };
  }
  if (response.status === 409) {
    return { code: 'CONFLICT', userMessage: 'Your preferences were updated elsewhere. Please refresh and try again.', fieldErrors: [] };
  }
  if (response.status === 400) {
    let fieldErrors: FieldErrorDetail[] = [];
    try {
      const body = (await response.json()) as BackendValidationErrorBody;
      fieldErrors = body.fieldErrors ?? [];
    } catch {
      fieldErrors = [];
    }
    return { code: 'VALIDATION_ERROR', userMessage: 'Some of the values provided were invalid.', fieldErrors };
  }
  return { code: 'UNKNOWN' as NotificationPreferencesErrorCode, userMessage: 'Something went wrong. Please try again later.', fieldErrors: [] };
}

async function parseOrThrow(response: Response): Promise<NotificationPreferencesResponse> {
  if (!response.ok) {
    throw await toApiError(response);
  }
  return (await response.json()) as NotificationPreferencesResponse;
}

export async function companyApiGetNotificationPreferences(): Promise<NotificationPreferencesResponse> {
  const response = await httpRequest(PATH, { method: 'GET' });
  return parseOrThrow(response);
}

export async function companyApiUpdateNotificationPreferences(
  payload: NotificationPreferencesUpdateRequest,
): Promise<NotificationPreferencesResponse> {
  const response = await httpRequest(PATH, { method: 'PUT', body: JSON.stringify(payload) });
  return parseOrThrow(response);
}

export async function companyApiPatchNotificationPreferences(
  payload: NotificationPreferencesPatchRequest,
): Promise<NotificationPreferencesResponse> {
  const response = await httpRequest(PATH, { method: 'PATCH', body: JSON.stringify(payload) });
  return parseOrThrow(response);
}
