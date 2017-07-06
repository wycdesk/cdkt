/**
 * QueryRecordServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package localhost.dcms.webservice.QueryRecord;

public class QueryRecordServiceLocator extends org.apache.axis.client.Service implements localhost.dcms.webservice.QueryRecord.QueryRecordService {

    public QueryRecordServiceLocator() {
    }


    public QueryRecordServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public QueryRecordServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for QueryRecord
    private java.lang.String QueryRecord_address = "http://ccod4.5d:8080/dcmsWebservice/webservice/QueryRecord";

    public java.lang.String getQueryRecordAddress() {
        return QueryRecord_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String QueryRecordWSDDServiceName = "QueryRecord";

    public java.lang.String getQueryRecordWSDDServiceName() {
        return QueryRecordWSDDServiceName;
    }

    public void setQueryRecordWSDDServiceName(java.lang.String name) {
        QueryRecordWSDDServiceName = name;
    }

    public localhost.dcms.webservice.QueryRecord.QueryRecord getQueryRecord() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(QueryRecord_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getQueryRecord(endpoint);
    }

    public localhost.dcms.webservice.QueryRecord.QueryRecord getQueryRecord(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            localhost.dcms.webservice.QueryRecord.QueryRecordSoapBindingStub _stub = new localhost.dcms.webservice.QueryRecord.QueryRecordSoapBindingStub(portAddress, this);
            _stub.setPortName(getQueryRecordWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setQueryRecordEndpointAddress(java.lang.String address) {
        QueryRecord_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (localhost.dcms.webservice.QueryRecord.QueryRecord.class.isAssignableFrom(serviceEndpointInterface)) {
                localhost.dcms.webservice.QueryRecord.QueryRecordSoapBindingStub _stub = new localhost.dcms.webservice.QueryRecord.QueryRecordSoapBindingStub(new java.net.URL(QueryRecord_address), this);
                _stub.setPortName(getQueryRecordWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("QueryRecord".equals(inputPortName)) {
            return getQueryRecord();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://localhost:8081/dcms/webservice/QueryRecord", "QueryRecordService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://localhost:8081/dcms/webservice/QueryRecord", "QueryRecord"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("QueryRecord".equals(portName)) {
            setQueryRecordEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
