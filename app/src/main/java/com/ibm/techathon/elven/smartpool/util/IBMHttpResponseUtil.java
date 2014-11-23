package com.ibm.techathon.elven.smartpool.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by meshriva on 11/17/2014.
 * Class has utility methods related to handling to IBMHttpResponse
 */
public class IBMHttpResponseUtil {

    /**
     * static utility class to get response String from InputStream
     * @param inputStream
     * @return String
     */
    public static String getResponseBody(InputStream inputStream){
        String responseString = "";
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
            String myString = "";
            while ((myString = in.readLine()) != null)
                responseString += myString;

            in.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
     return responseString;
    }
}
