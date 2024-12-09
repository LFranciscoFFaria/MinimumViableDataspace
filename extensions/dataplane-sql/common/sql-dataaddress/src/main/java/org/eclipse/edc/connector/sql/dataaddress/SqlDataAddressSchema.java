package org.eclipse.edc.connector.sql.dataaddress;

public class SqlDataAddressSchema {
    public static String SQLDATA = "SqlData";

    /** Name of the registered sql datasource from where to extract data. */
    public static String DATASOURCENAME = "dataSourceName";

    /** Query used to extract data by the Sql source. */
    public static String QUERY = "query";
}
