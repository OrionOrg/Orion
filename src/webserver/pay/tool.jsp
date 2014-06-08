<%@page contentType="text/html; charset=utf-8" %>
<%@page import="java.security.*" %>
<%@page import="java.io.*" %>
<%@page import="java.net.*" %>
<%@page import="org.json.*" %>
<%@page import="java.util.*" %>
<%@page import="java.sql.*" %>
<!-- MD5签名 -->
<%!
public String createSignString(String[] values)
throws UnsupportedEncodingException, NoSuchAlgorithmException 
{
	StringBuffer buf = new StringBuffer(512);
	for (int i = 0; i < values.length; i++) 
	{
		String value = values[i];
		if (value == null) 
		{
			continue;
		}
		buf.append(value);
	}
	byte[] bufTemp = buf.toString().getBytes("UTF-8");
	MessageDigest mdTemp = MessageDigest.getInstance("MD5");
	mdTemp.update(bufTemp);
	byte[] md5Result = mdTemp.digest();
	return byte2String(md5Result);
}

public String byte2String(byte[] buf) 
{
	StringBuffer result = new StringBuffer();
	for (int i = 0; i < buf.length; i++) 
	{
		if ((buf[i] & 0xff) < 0x10) 
		{
			result.append('0');
		}
		result.append((Integer.toHexString(buf[i] & 0xff)));
	}
	return result.toString();
}
%>

