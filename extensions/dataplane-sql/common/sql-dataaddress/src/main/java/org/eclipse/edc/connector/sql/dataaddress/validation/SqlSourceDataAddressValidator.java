package org.eclipse.edc.connector.sql.dataaddress.validation;

import org.eclipse.edc.connector.sql.dataaddress.SqlDataAddressSchema;
import org.eclipse.edc.spi.types.domain.DataAddress;
import org.eclipse.edc.util.string.StringUtils;
import org.eclipse.edc.validator.spi.ValidationResult;
import org.eclipse.edc.validator.spi.Validator;
import org.eclipse.edc.validator.spi.Violation;

import java.util.ArrayList;

import static org.eclipse.edc.validator.spi.ValidationResult.failure;
import static org.eclipse.edc.validator.spi.ValidationResult.success;

public class SqlSourceDataAddressValidator implements Validator<DataAddress> {
    @Override
    public ValidationResult validate(DataAddress input) {
        var violations = new ArrayList<Violation>();

        if (StringUtils.isNullOrBlank(input.getStringProperty(SqlDataAddressSchema.qUERY, null))) {
            violations.add(Violation.violation("Must have a %s property".formatted(SqlDataAddressSchema.qUERY), SqlDataAddressSchema.qUERY));
        }

        if (!violations.isEmpty()) {
            return failure(violations);
        }
        return success();
    }
}
