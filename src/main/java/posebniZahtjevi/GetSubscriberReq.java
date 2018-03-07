package posebniZahtjevi;

public class GetSubscriberReq {

	public String getData(String node, String code, String dn)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:urn=\"urn:iskratel-si:itnbsp-1-0\">");
		sb.append("<soapenv:Header/>");
		sb.append("<soapenv:Body>");
		sb.append("<urn:getSubscriberRequest>");
		sb.append("<subscriber node = '"+node+"' areaCode = '"+code+"' dn='"+dn+"'/>");
		sb.append("</urn:getSubscriberRequest>");
		sb.append("</soapenv:Body>");
		sb.append("</soapenv:Envelope>");
		
		return sb.toString();
	}
}

