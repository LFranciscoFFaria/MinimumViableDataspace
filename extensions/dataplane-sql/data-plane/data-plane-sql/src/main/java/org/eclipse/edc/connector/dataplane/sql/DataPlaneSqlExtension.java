package org.eclipse.edc.connector.dataplane.sql;

import org.org.eclipse.edc.connector.dataplane.sql.pipeline.SqlDataSourceFactory;
import org.eclipse.edc.connector.dataplane.spi.pipeline.DataTransferExecutorServiceContainer;
import org.eclipse.edc.connector.dataplane.spi.pipeline.PipelineService;
import org.eclipse.edc.runtime.metamodel.annotation.Extension;
import org.eclipse.edc.runtime.metamodel.annotation.Inject;
import org.eclipse.edc.runtime.metamodel.annotation.Provides;
import org.eclipse.edc.runtime.metamodel.annotation.Setting;
import org.eclipse.edc.spi.security.Vault;
import org.eclipse.edc.spi.system.ServiceExtension;
import org.eclipse.edc.spi.system.ServiceExtensionContext;
import org.eclipse.edc.spi.types.TypeManager;
import org.eclipse.edc.transaction.datasource.spi.DataSourceRegistry;
import org.eclipse.edc.transaction.spi.TransactionContext;
import org.eclipse.edc.sql.SqlQueryExecutor;
import org.eclipse.edc.spi.types.TypeManager;
import org.eclipse.edc.sql.QueryExecutor;

/**
 * Provides support for reading data from an HTTP endpoint and sending data to an HTTP endpoint.
 */
//@Provides(HttpRequestParamsProvider.class)
@Extension(value = DataPlaneSqlExtension.NAME)
public class DataPlaneSqlExtension implements ServiceExtension {
    public static final String NAME = "Data Plane SQL";
    private static final int DEFAULT_PARTITION_SIZE = 5;

    /**@Setting( description = "Number of partitions for parallel message push in the HttpDataSink", defaultValue = DEFAULT_PARTITION_SIZE + "", key = "edc.dataplane.http.sink.partition.size")
    private int partitionSize;*/

    /* 
    @Inject
    private EdcHttpClient httpClient;
    */

    @Inject
    private PipelineService pipelineService;

    @Inject
    private DataTransferExecutorServiceContainer executorContainer;

    @Inject
    private Vault vault;

    @Inject
    private DataSourceRegistry dataSourceRegistry;

    @Inject
    private TransactionContext transactionContext;

    @Inject
    private QueryExecutor queryExecutor;

    @Inject
    private TypeManager typeManager;

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public void initialize(ServiceExtensionContext context) {
        var monitor = context.getMonitor();

        var sourceFactory = new SqlDataSourceFactory( monitor, dataSourceRegistry, transactionContext, queryExecutor, typeManager);
        pipelineService.registerFactory(sourceFactory);

        /**var sinkFactory = new HttpDataSinkFactory(httpClient, executorContainer.getExecutorService(), partitionSize, monitor, paramsProvider, httpRequestFactory);
        pipelineService.registerFactory(sinkFactory);*/
    }

}
