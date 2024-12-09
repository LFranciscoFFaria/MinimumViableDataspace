package org.eclipse.edc.connector.dataplane.sql.pipeline;

import org.eclipse.edc.connector.dataplane.spi.pipeline.DataSource;

import java.io.InputStream;

public record SqlPart(String name, InputStream content) implements DataSource.Part {

    @Override
    public long size() {
        return SIZE_UNKNOWN;
    }

    @Override
    public InputStream openStream() {
        return content;
    }

}
