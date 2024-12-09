package org.eclipse.edc.connector.sql.dataaddress;

import org.eclipse.edc.spi.types.domain.DataAddress;
import static org.eclipse.edc.connector.sql.dataaddress.SqlDataAddressSchema.QUERY;
import static org.eclipse.edc.connector.sql.dataaddress.SqlDataAddressSchema.DATASOURCENAME;
import static org.eclipse.edc.connector.sql.dataaddress.SqlDataAddressSchema.SQLDATA;



import java.util.Optional;

import static java.util.Collections.emptyMap;

public class SqlDataAddress extends DataAddress {
    private SqlDataAddress() {
        setType(SQLDATA);
    }

    public String getDataSourceName() {
        return getStringProperty(DATASOURCENAME);
    }

    public String getQuery() {
        return getStringProperty(QUERY);
    }

    public static final class Builder extends DataAddress.Builder<SqlDataAddress, Builder> {
        private Builder() {
            super(new SqlDataAddress());
        }

        public static Builder newInstance() {
            return new Builder();
        }

        public Builder dataSourceName(String dataSourceName) {
            property(DATASOURCENAME, dataSourceName);
            return this;
        }

        public Builder query(String query) {
            property(QUERY, query);
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
