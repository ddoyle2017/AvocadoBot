package Utility;

import java.io.*;
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
     * Connects to the given API endpoint and sends a request.
     * @param requestMethod A type of REST request method.
     * @param url An API endpoint.
     * @param jsonBody A JSON payload to send to the API endpoint.
     * @param apiSecret The API secrets used for authorization.
     * @return A stream for reading the API's response or NULL if the connection failed.
     */
    public Reader sendRESTRequest(final String requestMethod, final URL url, String jsonBody, final ISecrets apiSecret)
    {
        HttpURLConnection connection = openHTTPConnection(requestMethod, url, jsonBody, apiSecret);
        try
        {
            connection.connect();
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
     * Builds a HTTP connection to the specified API endpoint.
     * @param requestMethod the REST request method to make.
     * @param url The API endpoint to call.
     * @param apiSecret The API secrets for authorization.
     * @return A HttpURLConnection object representing the HTTP connection to the API's endpoint, or NULL if the request is malformed.
     */
    HttpURLConnection openHTTPConnection(final String requestMethod, final URL url, String jsonBody, final ISecrets apiSecret)
    {
        if (url == null)
        {
            System.err.println("Endpoint URL can NOT be null.");
            return null;
        }

        try
        {
            if (GET_REQUEST.equalsIgnoreCase(requestMethod))
            {
                return createGETRequest(url, apiSecret);
            }
            else if (POST_REQUEST.equalsIgnoreCase(requestMethod))
            {
                return createPOSTRequest(url, jsonBody, apiSecret);
            }
            else if (PUT_REQUEST.equalsIgnoreCase(requestMethod))
            {
                return createPUTRequest(url, jsonBody, apiSecret);
            }
            else if (DELETE_REQUEST.equalsIgnoreCase(requestMethod))
            {
                return createDELETERequest(url, jsonBody, apiSecret);
            }
            else
            {
                System.err.println("Invalid REST request method.");
                return null;
            }
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     *
     * @param url
     * @return
     * @throws IOException
     */
    HttpURLConnection createGETRequest(final URL url, final ISecrets apiSecret) throws IOException
    {
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestMethod(GET_REQUEST);

        if (apiSecret != null)
        {
            connection.setRequestProperty(apiSecret.getRequestKey(), apiSecret.getRequestKeyValue());
        }
        return connection;
    }

    /**
     *
     * @param url
     * @param jsonBody
     * @return
     * @throws IOException
     */
    HttpURLConnection createPOSTRequest(final URL url, final String jsonBody, final ISecrets apiSecret) throws IOException
    {
        if (jsonBody == null || jsonBody.isEmpty())
        {
            System.err.println("A POST request cannot have a null/empty JSON body.");
            throw new IOException();
        }

        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestMethod(POST_REQUEST);
        connection.setRequestProperty("Content-Type", "application/json; utf-8");
        connection.setRequestProperty("Accept", "application/json");

        if (apiSecret != null)
        {
            connection.setRequestProperty(apiSecret.getRequestKey(), apiSecret.getRequestKeyValue());
        }

        final OutputStream outputStream = connection.getOutputStream();
        final byte[] input = jsonBody.getBytes(StandardCharsets.UTF_8);
        outputStream.write(input, 0, input.length);

        return connection;
    }

    //
    // TO-DO: Not fully implemented yet
    //
    HttpURLConnection createPUTRequest(final URL url, final String jsonBody, final ISecrets apiSecret) throws IOException
    {
        if (jsonBody == null || jsonBody.isEmpty())
        {
            System.err.println("A POST request cannot have a null/empty JSON body.");
            throw new IOException();
        }

        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestMethod(PUT_REQUEST);

        if (apiSecret != null)
        {
            connection.setRequestProperty(apiSecret.getRequestKey(), apiSecret.getRequestKeyValue());
        }
        return connection;
    }

    //
    // TO-DO: Not fully implemented yet
    //
    HttpURLConnection createDELETERequest(final URL url, final String jsonBody, final ISecrets apiSecret) throws IOException
    {
        if (jsonBody == null || jsonBody.isEmpty())
        {
            System.err.println("A POST request cannot have a null/empty JSON body.");
            throw new IOException();
        }

        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestMethod(DELETE_REQUEST);

        if (apiSecret != null)
        {
            connection.setRequestProperty(apiSecret.getRequestKey(), apiSecret.getRequestKeyValue());
        }
        return connection;
    }
}
