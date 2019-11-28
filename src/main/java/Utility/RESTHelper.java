package Utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Helper class for making REST API calls.
 */
public class RESTHelper
{
    private final static String GET_REQUEST = "GET";
    private final static String PUT_REQUEST = "PUT";
    private final static String POST_REQUEST = "POST";
    private final static String DELETE_REQUEST = "DELETE";


    public RESTHelper() { }

    /**
     * Retrieves the response from making a REST call.
     * @param requestMethod the REST request method to make.
     * @param url The API endpoint to call.
     * @param apiSecret The API secrets for authorization.
     * @return A Reader object for streaming the REST call results.
     */
    public Reader getRESTContent(final String requestMethod, final URL url, final ISecrets apiSecret)
    {
        HttpURLConnection connection = openHTTPConnection(requestMethod, url, apiSecret);
        try
        {
            return new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
        }
        catch (IOException | NullPointerException ex)
        {
            ex.printStackTrace();
            connection.disconnect();
            return null;
        }
    }

    /**
     * Builds and opens an HTTP connection to the specified API endpoint.
     * @param requestMethod the REST request method to make.
     * @param url The API endpoint to call.
     * @param apiSecret The API secrets for authorization.
     * @return A HttpURLConnection object representing the HTTP connection to the API's endpoint.
     */
    HttpURLConnection openHTTPConnection(final String requestMethod, final URL url, final ISecrets apiSecret)
    {
        if (!isValidRequestMethod(requestMethod) || url == null)
        {
            System.err.println("RESTHelper: Invalid HTTPConnection parameters");
            return null;
        }

        try
        {
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod(requestMethod);

            if (apiSecret != null)
            {
                connection.setRequestProperty(apiSecret.getRequestKey(), apiSecret.getRequestKeyValue());
            }

            connection.connect();
            return connection;
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Given a string, determines whether or not its a valid HTTP REST call.
     * @param requestMethod A string representing a type of REST call.
     * @return True, if the string is a valid REST call. False, if not.
     */
    boolean isValidRequestMethod(final String requestMethod)
    {
        return (GET_REQUEST.equalsIgnoreCase(requestMethod) || PUT_REQUEST.equalsIgnoreCase(requestMethod) ||
                POST_REQUEST.equalsIgnoreCase(requestMethod) || DELETE_REQUEST.equalsIgnoreCase(requestMethod));
    }
}
