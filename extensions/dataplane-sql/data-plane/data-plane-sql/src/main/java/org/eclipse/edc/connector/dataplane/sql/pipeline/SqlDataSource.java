/*
 *  Copyright (c) 2021 Microsoft Corporation
 *
 *  This program and the accompanying materials are made available under the
 *  terms of the Apache License, Version 2.0 which is available at
 *  https://www.apache.org/licenses/LICENSE-2.0
 *
 *  SPDX-License-Identifier: Apache-2.0
 *
 *  Contributors:
 *       Microsoft Corporation - initial API and implementation
 *
 */

package org.eclipse.edc.connector.dataplane.sql.pipeline;


import okhttp3.MediaType;
import okhttp3.ResponseBody;
import org.eclipse.edc.connector.sql.dataaddress.SqlDataAddress;
import org.eclipse.edc.connector.dataplane.spi.pipeline.DataSource;
import org.eclipse.edc.connector.dataplane.spi.pipeline.StreamResult;
import org.eclipse.edc.spi.EdcException;
import org.eclipse.edc.spi.monitor.Monitor;
import org.eclipse.edc.spi.query.QuerySpec;
import org.eclipse.edc.sql.store.AbstractSqlStore;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.lang.String.format;
import static org.eclipse.edc.connector.dataplane.spi.pipeline.StreamResult.error;
import static org.eclipse.edc.connector.dataplane.spi.pipeline.StreamResult.success;

public class SqlDataSource {

    private String name;
    private String requestId;
    private Monitor monitor;
    private SqlDataAddress sqlDataAddress;
    private Connection connection;
    private final SqlQueryExecutor sqlQueryExecutor;
    private final AtomicReference<ResponseBodyStream> responseBodyStream = new AtomicReference<>();

    private SqlDataSource() {   
    }

    @Override
    public StreamResult<Stream<Part>> openPartStream() {
        monitor.debug(() -> "Executing SQL Query: " + request.url());
        transactionContext.execute(() -> {
            Objects.requireNonNull(querySpec);
            try {
                var querySpec = QuerySpec.max();
                //var queryStmt = statements.createQuery(querySpec);
                try (var stream = sqlQueryExecutor.query(connection, true, this::mapResultSet, sqlDataAddress.getQuery(), new ArrayList<>[].toArray(Object[]::new))) {
                    return success(Stream.of(new SqlPart(name, stream, sqlDataAddress.getQuery())));
                }
            } catch (SQLException exception) {
                throw new EdcPersistenceException(exception);
            }
        });
    }

    private String mapResultSet(ResultSet resultSet) throws Exception {
        String result;
        int col_n = resultSet.getMetaData().getColumnCount();

        for(int i = 0; i < col_n; i++){
            result.add(resultSet.getString(i) + ";");
        }
        result.add("\n");
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

        public Builder name(String name) {
            dataSource.name = name;
            return this;
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

        public Builder sqlQueryExecutor(SqlQueryExecutor sqlQueryExecutor) {
            dataSource.sqlQueryExecutor = sqlQueryExecutor;
            return this;
        }

        public Builder connection(Connection connection) {
            dataSource.connection = connection;
            return this;
        }

        public SqlDataSource build() {
            Objects.requireNonNull(dataSource.requestId, "requestId");
            Objects.requireNonNull(dataSource.monitor, "monitor");
            Objects.requireNonNull(dataSource.sqlDataAddress, "sqlDataAddress");
            Objects.requireNonNull(dataSource.sqlQueryExecutor, "sqlQueryExecutor");
            Objects.requireNonNull(dataSource.connection, "connection");

            return dataSource;
        }
    }

}
