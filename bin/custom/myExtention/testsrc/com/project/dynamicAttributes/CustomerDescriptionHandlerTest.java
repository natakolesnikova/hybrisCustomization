package com.project.dynamicAttributes;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@UnitTest
@RunWith(MockitoJUnitRunner.class)
class CustomerDescriptionHandlerTest {

    private static final String NAME = "Spar";
    private static final String EMAIL = "spar@spar.com";
    private static final String EXPECTED_RESULT = "Spar : spar@spar.com. Order quantity is 0";

    private CustomerDescriptionHandler customerDescriptionHandler = new CustomerDescriptionHandler();

    @Mock
    private CustomerModel customerModel;

    @Mock
    private Collection<OrderModel> customerOrder;

    @Test
    public void getReturnCustomerNameAndEmail() {
        when(customerModel.getName()).thenReturn(NAME);
        when(customerModel.getContactEmail()).thenReturn(EMAIL);
        when(customerModel.getOrders()).thenReturn(customerOrder);

        assertEquals(EXPECTED_RESULT, customerDescriptionHandler.get(customerModel));
    }

}