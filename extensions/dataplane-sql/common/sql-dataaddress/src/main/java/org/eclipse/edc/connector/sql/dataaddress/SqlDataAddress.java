package org.eclipse.edc.connector.sql.dataaddress;

import org.eclipse.edc.spi.types.domain.DataAddress;

import java.util.Optional;

import static java.util.Collections.emptyMap;
import static org.eclipse.edc.connector.sql.dataaddress.SqlDataAddressSchema.dATASOURCENANE;
import static org.eclipse.edc.connector.sql.dataaddress.SqlDataAddressSchema.qUERY;
import static org.eclipse.edc.connector.sql.dataaddress.SqlDataAddressSchema.sQLDATA;

public class SqlDataAddress extends DataAddress {
    private SqlDataAddress() {
        setType(sQLDATA);
    }

    public String getDataSourceName() {
        return getStringProperty(dATASOURCENANE);
    }

    public String getQuery() {
        return getStringProperty(qUERY);
    }

    public static final class Builder extends DataAddress.Builder<SqlDataAddress, Builder> {
        private Builder() {
            super(new SqlDataAddress());
        }

        public static Builder newInstance() {
            return new Builder();
        }

        public Builder dataSourceName(String dataSourceName) {
            property(dATASOURCENANE, dataSourceName);
            return this;
        }

        public Builder query(String query) {
            property(qUERY, query);
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
