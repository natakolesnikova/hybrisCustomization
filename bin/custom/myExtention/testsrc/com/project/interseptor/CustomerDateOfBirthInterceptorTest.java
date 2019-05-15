package com.project.interseptor;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.Clock;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class CustomerDateOfBirthInterceptorTest {

    private static Date VALID_DATE = new GregorianCalendar(2006, Calendar.JANUARY, 1).getTime();
    private static Date NOT_VALID_DATE = new GregorianCalendar(2008, Calendar.JANUARY, 1).getTime();
    private static Clock DAY_OF_TEST = Clock.fixed(new GregorianCalendar(2018, Calendar.JANUARY, 1).toInstant(), ZoneId.systemDefault());

    @InjectMocks
    private CustomerDateOfBirthInterceptor customerDateOfBirthInterceptor;

    @InjectMocks
    private UserModel userModel;

    @Before
    public void setTime() {
        customerDateOfBirthInterceptor.setClock(DAY_OF_TEST);
    }

    @Test
    public void shouldSetDateForValidCustomerDateOfBirth() throws InterceptorException {
        userModel.setDateOfBirth(VALID_DATE);
        customerDateOfBirthInterceptor.onValidate(userModel, null);
    }

    @Test(expected = InterceptorException.class)
    public void shouldNotSetDateForInvalidCustomerDateOfBirth() throws InterceptorException {
        userModel.setDateOfBirth(NOT_VALID_DATE);
        customerDateOfBirthInterceptor.onValidate(userModel, null);
    }
}