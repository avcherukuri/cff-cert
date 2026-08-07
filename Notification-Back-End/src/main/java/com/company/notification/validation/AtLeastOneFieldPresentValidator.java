package com.company.notification.validation;

import com.company.notification.dto.NotificationPreferencesPatchRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AtLeastOneFieldPresentValidator implements ConstraintValidator<AtLeastOneFieldPresent, NotificationPreferencesPatchRequest> {

    @Override
    public boolean isValid(NotificationPreferencesPatchRequest request, ConstraintValidatorContext context) {
        if (request == null) {
            return true;
        }
        return request.emailEnabled() != null || request.smsEnabled() != null || request.pushEnabled() != null;
    }
}
