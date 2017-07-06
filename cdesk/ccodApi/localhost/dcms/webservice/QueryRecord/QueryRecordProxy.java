package localhost.dcms.webservice.QueryRecord;

public class QueryRecordProxy implements localhost.dcms.webservice.QueryRecord.QueryRecord {
  private String _endpoint = null;
  private localhost.dcms.webservice.QueryRecord.QueryRecord queryRecord = null;
  
  public QueryRecordProxy() {
    _initQueryRecordProxy();
  }
  
  public QueryRecordProxy(String endpoint) {
    _endpoint = endpoint;
    _initQueryRecordProxy();
  }
  
  private void _initQueryRecordProxy() {
    try {
      queryRecord = (new localhost.dcms.webservice.QueryRecord.QueryRecordServiceLocator()).getQueryRecord();
      if (queryRecord != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)queryRecord)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)queryRecord)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (queryRecord != null)
      ((javax.xml.rpc.Stub)queryRecord)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public localhost.dcms.webservice.QueryRecord.QueryRecord getQueryRecord() {
    if (queryRecord == null)
      _initQueryRecordProxy();
    return queryRecord;
  }
  
  public java.lang.String queryRecordAddr(java.lang.String enterpriseID, java.lang.String USER, java.lang.String PASSWORD, java.lang.String sessionID) throws java.rmi.RemoteException{
    if (queryRecord == null)
      _initQueryRecordProxy();
    return queryRecord.queryRecordAddr(enterpriseID, USER, PASSWORD, sessionID);
  }
  
  
}