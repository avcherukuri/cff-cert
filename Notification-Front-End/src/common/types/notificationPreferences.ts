export interface NotificationPreferencesResponse {
  emailEnabled: boolean;
  smsEnabled: boolean;
  pushEnabled: boolean;
  updatedAt: string | null;
}

export interface NotificationPreferencesUpdateRequest {
  emailEnabled: boolean;
  smsEnabled: boolean;
  pushEnabled: boolean;
}

export interface NotificationPreferencesPatchRequest {
  emailEnabled?: boolean;
  smsEnabled?: boolean;
  pushEnabled?: boolean;
}

export type NotificationChannel = 'emailEnabled' | 'smsEnabled' | 'pushEnabled';

export interface FieldErrorDetail {
  field: string;
  message: string;
}

export type NotificationPreferencesErrorCode =
  | 'UNAUTHENTICATED'
  | 'VALIDATION_ERROR'
  | 'CONFLICT'
  | 'UNKNOWN';

export interface NotificationPreferencesApiError {
  code: NotificationPreferencesErrorCode;
  userMessage: string;
  fieldErrors: FieldErrorDetail[];
}

export interface ValidationResult {
  isValid: boolean;
  fieldErrors: Record<string, string>;
}
