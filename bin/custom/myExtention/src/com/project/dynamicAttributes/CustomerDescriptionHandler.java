package com.project.dynamicAttributes;

import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.model.attribute.AbstractDynamicAttributeHandler;
import org.springframework.stereotype.Component;

import static java.util.Objects.isNull;

@Component
public class CustomerDescriptionHandler extends AbstractDynamicAttributeHandler<String, CustomerModel> {

    @Override
    public String get(CustomerModel model) {
        if (isNull(model)) {
            throw new IllegalArgumentException("Customer model is null");
        }

        return model.getName() + " : " + model.getContactEmail() + ". Order quantity is " + model.getOrders().size();
    }
}
