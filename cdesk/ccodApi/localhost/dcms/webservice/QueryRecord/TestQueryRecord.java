package localhost.dcms.webservice.QueryRecord;

import java.net.MalformedURLException;
import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;


public class TestQueryRecord {

	public static void main(String[] args) throws RemoteException, MalformedURLException, ServiceException {
		// TODO Auto-generated method stub

			
			/*QueryRecordServiceLocator lo=new QueryRecordServiceLocator();
			java.net.URL portAddress=new java.net.URL("http://ccod4.5d:8080/dcmsWebservice/webservice/QueryRecord?wsdl");
			localhost.dcms.webservice.QueryRecord.QueryRecord stub =lo.getQueryRecord(portAddress);
			String s = stub.queryRecordAddr("CCOD9527", "1001", "1001", "5b56e0e435000326:CCOD9527:1001");
			System.out.println(s);*/
			
			QueryRecordServiceLocator service = new QueryRecordServiceLocator();
			java.net.URL url=new java.net.URL("http://ccod4.5d:8080/dcmsWebservice/webservice/QueryRecord?wsdl");
			QueryRecordSoapBindingStub stub = new QueryRecordSoapBindingStub(url,service);
			String s = stub.queryRecordAddr("CCOD9527", "1001", "1001", "5b56e0e435000326:CCOD9527:1001");
			System.out.println(s);
	
	}

}
