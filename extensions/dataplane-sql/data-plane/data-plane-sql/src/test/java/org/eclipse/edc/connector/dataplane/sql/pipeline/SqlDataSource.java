package org.eclipse.edc.connector.dataplane.sql.pipeline;

import org.eclipse.edc.connector.dataplane.spi.pipeline.StreamResult;
import org.eclipse.edc.connector.sql.dataaddress.SqlDataAddress;
import org.eclipse.edc.spi.EdcException;
import org.eclipse.edc.spi.monitor.Monitor;
import org.eclipse.edc.spi.query.QuerySpec;
import org.eclipse.edc.sql.QueryExecutor;
import org.eclipse.edc.transaction.spi.TransactionContext;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.eclipse.edc.connector.dataplane.spi.pipeline.StreamResult.success;

public class SqlDataSource implements org.eclipse.edc.connector.dataplane.spi.pipeline.DataSource {

    private String requestId;
    private Monitor monitor;
    private SqlDataAddress sqlDataAddress;
    private javax.sql.DataSource database;
    private QueryExecutor queryExecutor;
    private TransactionContext transactionContext;

    private SqlDataSource() {   
    }

    @Override
    public StreamResult<Stream<Part>> openPartStream() {
        monitor.debug(() -> "Executing SQL Query: " + sqlDataAddress.getDataSourceName() + "- " + sqlDataAddress.getQuery());
        try {
            var querySpec = QuerySpec.max();
            //var queryStmt = statements.createQuery(querySpec);
            try (var stream = queryExecutor.query(database.getConnection(), true, this::mapResultSet, sqlDataAddress.getQuery())) {
                return success(Stream.of(new SqlPart("text/csv", new ByteArrayInputStream(stream.collect(Collectors.joining("\n")).getBytes(StandardCharsets.UTF_8)))));
            }
        } catch (SQLException exception) {
            throw new EdcException(exception);
        }
        //return error("Error while opening stream");
    }

    @Override
    public void close() {

    }

    private String mapResultSet(ResultSet resultSet) throws Exception {
        String result = String.valueOf("");
        int colN = resultSet.getMetaData().getColumnCount();
        for (int i = 0; i < colN; i++) {
            result = result.concat(resultSet.getString(i) + ";");
        }

        return result;
    }

    public static class Builder {
        private final SqlDataSource dataSource;

        public static Builder newInstance() {
            return new Builder();
        }

        private Builder() {
            dataSource = new SqlDataSource();
        }

        public Builder requestId(String requestId) {
            dataSource.requestId = requestId;
            return this;
        }

        public Builder monitor(Monitor monitor) {
            dataSource.monitor = monitor;
            return this;
        }

        public Builder sqlDataAddress(SqlDataAddress sqlDataAddress) {
            dataSource.sqlDataAddress = sqlDataAddress;
            return this;
        }

        public Builder queryExecutor(QueryExecutor queryExecutor) {
            dataSource.queryExecutor = queryExecutor;
            return this;
        }

        public Builder database(javax.sql.DataSource database) {
            dataSource.database = database;
            return this;
        }

        public Builder transactionContext(TransactionContext transactionContext) {
            dataSource.transactionContext = transactionContext;
            return this;
        }

        public SqlDataSource build() {
            Objects.requireNonNull(dataSource.requestId, "requestId");
            Objects.requireNonNull(dataSource.monitor, "monitor");
            Objects.requireNonNull(dataSource.sqlDataAddress, "sqlDataAddress");
            Objects.requireNonNull(dataSource.queryExecutor, "queryExecutor");
            Objects.requireNonNull(dataSource.database, "database");
            Objects.requireNonNull(dataSource.transactionContext, "transactionContext");

            return dataSource;
        }
    }

}
