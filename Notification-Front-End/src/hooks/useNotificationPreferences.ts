import { useCallback, useEffect, useState } from 'react';
import {
  companyApiGetNotificationPreferences,
  companyApiUpdateNotificationPreferences,
} from '../services/companyApiNotificationPreferences';
import { validateNotificationPreferencesForm } from '../services/notificationPreferencesValidation';
import {
  NotificationChannel,
  NotificationPreferencesApiError,
  NotificationPreferencesUpdateRequest,
} from '../common/types/notificationPreferences';

export interface UseNotificationPreferencesResult {
  preferences: NotificationPreferencesUpdateRequest | null;
  isLoading: boolean;
  isSubmitting: boolean;
  error: string | null;
  fieldErrors: Record<string, string>;
  isDirty: boolean;
  updateField: (field: NotificationChannel, value: boolean) => void;
  submit: () => Promise<void>;
}

function isApiError(value: unknown): value is NotificationPreferencesApiError {
  return typeof value === 'object' && value !== null && 'code' in value && 'userMessage' in value;
}

export function useNotificationPreferences(): UseNotificationPreferencesResult {
  const [savedPreferences, setSavedPreferences] = useState<NotificationPreferencesUpdateRequest | null>(null);
  const [preferences, setPreferences] = useState<NotificationPreferencesUpdateRequest | null>(null);
  const [isLoading, setIsLoading] = useState<boolean>(true);
  const [isSubmitting, setIsSubmitting] = useState<boolean>(false);
  const [error, setError] = useState<string | null>(null);
  const [fieldErrors, setFieldErrors] = useState<Record<string, string>>({});

  useEffect(() => {
    let cancelled = false;

    companyApiGetNotificationPreferences()
      .then((response) => {
        if (cancelled) {
          return;
        }
        const initial: NotificationPreferencesUpdateRequest = {
          emailEnabled: response.emailEnabled,
          smsEnabled: response.smsEnabled,
          pushEnabled: response.pushEnabled,
        };
        setSavedPreferences(initial);
        setPreferences(initial);
      })
      .catch((apiError: unknown) => {
        if (cancelled) {
          return;
        }
        setError(isApiError(apiError) ? apiError.userMessage : 'Something went wrong. Please try again later.');
      })
      .finally(() => {
        if (!cancelled) {
          setIsLoading(false);
        }
      });

    return () => {
      cancelled = true;
    };
  }, []);

  const updateField = useCallback((field: NotificationChannel, value: boolean) => {
    setPreferences((current) => (current ? { ...current, [field]: value } : current));
  }, []);

  const submit = useCallback(async () => {
    if (!preferences) {
      return;
    }

    const validation = validateNotificationPreferencesForm(preferences);
    setFieldErrors(validation.fieldErrors);
    if (!validation.isValid) {
      return;
    }

    setIsSubmitting(true);
    setError(null);
    try {
      const response = await companyApiUpdateNotificationPreferences(preferences);
      const saved: NotificationPreferencesUpdateRequest = {
        emailEnabled: response.emailEnabled,
        smsEnabled: response.smsEnabled,
        pushEnabled: response.pushEnabled,
      };
      setSavedPreferences(saved);
      setPreferences(saved);
      setFieldErrors({});
    } catch (apiError: unknown) {
      if (isApiError(apiError)) {
        setError(apiError.userMessage);
        const nextFieldErrors: Record<string, string> = {};
        apiError.fieldErrors.forEach((fieldError) => {
          nextFieldErrors[fieldError.field] = fieldError.message;
        });
        setFieldErrors(nextFieldErrors);
      } else {
        setError('Something went wrong. Please try again later.');
      }
    } finally {
      setIsSubmitting(false);
    }
  }, [preferences]);

  const isDirty = Boolean(
    preferences && savedPreferences && JSON.stringify(preferences) !== JSON.stringify(savedPreferences),
  );

  return { preferences, isLoading, isSubmitting, error, fieldErrors, isDirty, updateField, submit };
}
