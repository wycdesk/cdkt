/**
 * QueryRecordService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package localhost.dcms.webservice.QueryRecord;

public interface QueryRecordService extends javax.xml.rpc.Service {
    public java.lang.String getQueryRecordAddress();

    public localhost.dcms.webservice.QueryRecord.QueryRecord getQueryRecord() throws javax.xml.rpc.ServiceException;

    public localhost.dcms.webservice.QueryRecord.QueryRecord getQueryRecord(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
