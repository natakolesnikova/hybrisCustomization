package com.project.interseptor;

import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;

@Component
public class CustomerDateOfBirthInterceptor implements ValidateInterceptor<UserModel> {

    private final int USER_MIN_AGE = 12;
    private Clock clock = Clock.systemUTC();

    @Override
    public void onValidate(final UserModel userModel, final InterceptorContext interceptorContext) throws InterceptorException {
        final LocalDate localDate = userModel.getDateOfBirth().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        final Period age = Period.between(localDate, LocalDate.now());

        if (age.getYears() < USER_MIN_AGE) {
            throw new InterceptorException("User should be at least " + USER_MIN_AGE + " years old");
        }
    }
    void setClock(final Clock clock) {
        this.clock = clock;
    }
}
