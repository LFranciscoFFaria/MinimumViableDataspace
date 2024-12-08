package org.eclipse.edc.connector.sql.dataaddress;

import org.eclipse.edc.spi.types.domain.DataAddress;
import org.eclipse.edc.connector.sql.dataaddress.SqlDataAddressSchema;


import java.util.Optional;

import static java.util.Collections.emptyMap;

public class SqlDataAddress extends DataAddress {
    private SqlDataAddress() {
        setType(SqlDataAddressSchema.SQL_DATA);
    }

    public String getDataSourceName() {
        return getStringProperty(SqlDataAddressSchema.dataSourceName);
    }

    public String getQuery() {
        return getStringProperty(SqlDataAddressSchema.query);
    }

    public static final class Builder extends DataAddress.Builder<SqlDataAddress, Builder> {
        private Builder() {
            super(new SqlDataAddress());
        }

        public static Builder newInstance() {
            return new Builder();
        }

        public Builder dataSourceName(String dataSourceName) {
            property(SqlDataAddressSchema.dataSourceName, dataSourceName);
            return this;
        }

        public Builder query(String query) {
            property(SqlDataAddressSchema.query, query);
            return this;
        }

        @Override
        public SqlDataAddress build() {
            return address;
        }

        public Builder copyFrom(DataAddress other) {
            Optional.ofNullable(other)
                .map(DataAddress::getProperties)
                .orElse(emptyMap()).forEach(this::property);
            return this;
        }
    }
}
