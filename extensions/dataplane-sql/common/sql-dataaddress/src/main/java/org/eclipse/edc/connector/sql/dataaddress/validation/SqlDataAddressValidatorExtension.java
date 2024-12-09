package org.eclipse.edc.connector.sql.dataaddress.validation;

import org.eclipse.edc.connector.sql.dataaddress.SqlDataAddressSchema;
import org.eclipse.edc.runtime.metamodel.annotation.Extension;
import org.eclipse.edc.runtime.metamodel.annotation.Inject;
import org.eclipse.edc.spi.system.ServiceExtension;
import org.eclipse.edc.spi.system.ServiceExtensionContext;
import org.eclipse.edc.validator.spi.DataAddressValidatorRegistry;

@Extension(SqlDataAddressValidatorExtension.NAME)
public class SqlDataAddressValidatorExtension implements ServiceExtension {
    public static final String NAME = "Sql DataAddress Validator";

    @Inject
    private DataAddressValidatorRegistry validatorRegistry;

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public void initialize(ServiceExtensionContext context) {
        /** validatorRegistry.registerDestinationValidator(SqlDataAddressSchema.SQLDATA,
                new SqlSourceDataAddressValidator());**/
        validatorRegistry.registerSourceValidator(SqlDataAddressSchema.SQLDATA,
                new SqlSourceDataAddressValidator());
    }
}