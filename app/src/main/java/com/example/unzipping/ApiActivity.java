package com.example.unzipping;

import android.content.Context;
import android.os.Environment;
import android.util.Base64;
import android.widget.Toast;
import android.widget.Toolbar;

import org.apache.commons.io.IOUtils;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.Inflater;
import java.util.zip.ZipOutputStream;

public class ApiActivity {
    public static ApiActivity instance=null;
    public static String WSDL_URL="http://98.173.13.62:8080/web/getroutes.asmx?WSDL";
    String SOAP_ACTION="Avanti";
    String NAME_SPACE="Avanti";
    String METHOD_NAME_getRoutesDataCompressedJSON="getRoutesDataCompressedJSON";
    SoapObject result;
Context context;
    public ApiActivity(Context context) {this.context=context;
    }

    public void getRoutesDataCompressedJSON(int routeID, int workorder){
        String resultString="";
        SSLConection.allowAllSSL();
        SoapObject soapObject=new SoapObject(NAME_SPACE,METHOD_NAME_getRoutesDataCompressedJSON);
        soapObject.addProperty("RouteID",routeID);
        soapObject.addProperty("WorkID",workorder);
        SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(soapObject);
        SSLConection.allowAllSSL();
        HttpTransportSE httpTransportSE=new HttpTransportSE(WSDL_URL,1000000000);
        try {
            httpTransportSE.call(SOAP_ACTION+"/"+METHOD_NAME_getRoutesDataCompressedJSON,envelope);
            result = (SoapObject) envelope.bodyIn;
            resultString=result.getProperty(0).toString();
//            String line;
//            ByteArrayOutputStream bos = new ByteArrayOutputStream(resultString.length());
//            GZIPOutputStream gzip = new GZIPOutputStream(bos);
//            gzip.write(resultString.getBytes("UTF-8"));
//            gzip.close();
//            byte[] compressed = bos.toByteArray();
//            bos.close();
//
//            ByteArrayInputStream bis = new ByteArrayInputStream(compressed);
//            GZIPInputStream gis = new GZIPInputStream(bis);
//            BufferedReader br = new BufferedReader(new InputStreamReader(gis, "UTF-8"));
//            StringBuilder sb = new StringBuilder();
//            while((line = br.readLine()) != null) {
//                sb.append(line);
//            }
//            br.close();
//            gis.close();
//            bis.close();
            byte[] compressed = Base64.decode(resultString,0);

                GZIPInputStream gzipInputStream = new GZIPInputStream(
                        new ByteArrayInputStream(compressed, 4,
                                compressed.length - 4));
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                for (int value = 0; value != -1;) {
                    value = gzipInputStream.read();
                    if (value != -1) {
                        baos.write(value);
                    }
                }
                gzipInputStream.close();
                baos.close();
                String sReturn = new String(baos.toByteArray(), "UTF-8");
                //System.out.println("Output::"+sReturn);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }

    }

}
