package org.eclipse.edc.connector.sql.dataaddress;

public class SqlDataAddressSchema {
    String SQL_DATA = "SqlData";

    /** Name of the registered sql datasource from where to extract data. */
    String DATASOURCE_NAME = "dataSourceName";

    /** Query used to extract data by the Sql source. */
    String QUERY = "query";
}
