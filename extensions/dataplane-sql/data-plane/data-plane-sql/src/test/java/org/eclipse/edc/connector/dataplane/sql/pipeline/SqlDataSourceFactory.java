package org.eclipse.edc.connector.dataplane.sql.pipeline;

import org.eclipse.edc.connector.dataplane.spi.pipeline.DataSource;
import org.eclipse.edc.connector.dataplane.spi.pipeline.DataSourceFactory;
import org.eclipse.edc.connector.sql.dataaddress.SqlDataAddress;
import org.eclipse.edc.connector.sql.dataaddress.SqlDataAddressSchema;
import org.eclipse.edc.connector.sql.dataaddress.validation.SqlSourceDataAddressValidator;
import org.eclipse.edc.spi.monitor.Monitor;
import org.eclipse.edc.spi.result.Result;
import org.eclipse.edc.spi.types.domain.transfer.DataFlowStartMessage;
import org.eclipse.edc.sql.QueryExecutor;
import org.eclipse.edc.transaction.datasource.spi.DataSourceRegistry;
import org.eclipse.edc.transaction.spi.TransactionContext;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Instantiates {@link SqlDataSource}s for requests whose source data type is {@link SqlDataAddressSchema#sQLDATA}.
 */
public class SqlDataSourceFactory implements DataSourceFactory {

    private final Monitor monitor;
    private final QueryExecutor queryExecutor;
    private final DataSourceRegistry dataSourceRegistry;
    private final TransactionContext transactionContext;
    private final SqlSourceDataAddressValidator validation;

    public SqlDataSourceFactory(Monitor monitor, DataSourceRegistry dataSourceRegistry, TransactionContext transactionContext, QueryExecutor queryExecutor) {
        this.monitor = monitor;
        this.queryExecutor = queryExecutor;
        this.dataSourceRegistry = dataSourceRegistry;
        this.transactionContext = transactionContext;
        this.validation = new SqlSourceDataAddressValidator();
    }

    @Override
    public String supportedType() {
        return SqlDataAddressSchema.sQLDATA;
    }

    @Override
    public @NotNull Result<Void> validateRequest(DataFlowStartMessage request) {
        var source = request.getSourceDataAddress();
        return validation.validate(source).toResult();
    }

    @Override
    public DataSource createSource(DataFlowStartMessage request) {
        var dataAddress = SqlDataAddress.Builder.newInstance()
                .copyFrom(request.getSourceDataAddress())
                .build();
        return SqlDataSource.Builder.newInstance()
                .monitor(monitor)
                .requestId(request.getId())
                .sqlDataAddress(dataAddress)
                .queryExecutor(queryExecutor)
                .database(Objects.requireNonNull(dataSourceRegistry.resolve(dataAddress.getDataSourceName()), String.format("DataSource %s could not be resolved", dataAddress.getDataSourceName())))
                .transactionContext(transactionContext)
                .build();
    }
}
