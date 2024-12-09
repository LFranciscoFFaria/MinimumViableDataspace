package org.eclipse.edc.connector.dataplane.sql.pipeline;

import org.eclipse.edc.connector.sql.dataaddress.SqlDataAddress;
import org.eclipse.edc.connector.dataplane.spi.pipeline.DataSource;
import org.eclipse.edc.connector.dataplane.spi.pipeline.DataSourceFactory;
import org.eclipse.edc.spi.monitor.Monitor;
import org.eclipse.edc.spi.result.Result;
import org.eclipse.edc.spi.types.domain.transfer.DataFlowStartMessage;
import org.eclipse.edc.transaction.datasource.spi.DataSourceRegistry;
import org.eclipse.edc.transaction.spi.TransactionContext;
import org.eclipse.edc.sql.SqlQueryExecutor;
import org.eclipse.edc.spi.types.TypeManager;
import org.eclipse.edc.runtime.metamodel.annotation.Inject;
import org.jetbrains.annotations.NotNull;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.eclipse.edc.sql.QueryExecutor;

import org.eclipse.edc.connector.sql.dataaddress.SqlDataAddressSchema;

/**
 * Instantiates {@link SqlDataSource}s for requests whose source data type is {@link SqlDataAddressSchema#SQLDATA}.
 */
public class SqlDataSourceFactory implements DataSourceFactory {

    private final Monitor monitor;
    private final QueryExecutor queryExecutor;
    private DataSourceRegistry dataSourceRegistry;
    private TransactionContext transactionContext;
    private TypeManager typeManager;

    public SqlDataSourceFactory( Monitor monitor, DataSourceRegistry dataSourceRegistry, TransactionContext transactionContext, QueryExecutor queryExecutor, TypeManager typeManager) {
        this.monitor = monitor;
        this.queryExecutor = queryExecutor;
        this.dataSourceRegistry = dataSourceRegistry;
        this.transactionContext = transactionContext;
        this.typeManager = typeManager;
    }

    @Override
    public String supportedType() {
        return SqlDataAddressSchema.SQLDATA;
    }

    @Override
    public @NotNull Result<Void> validateRequest(DataFlowStartMessage request) {
        try {
            createSource(request);
        } catch (Exception e) {
            return Result.failure("Failed to build SqlDataSource: " + e.getMessage());
        }
        return Result.success();
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
