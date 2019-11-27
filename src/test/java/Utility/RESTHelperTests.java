package Utility;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class RESTHelperTests
{
    @Mock
    private URL url;
    @Mock
    private ISecrets apiSecrets;
    @Mock
    private HttpURLConnection connection;

    private RESTHelper helper;

    @Before
    public void setUp()
    {
        MockitoAnnotations.initMocks(this);
        this.helper = new RESTHelper();
    }


    @Test
    public void openHTTPConnection_GivenValidParameters_ReturnsHttpURLConnection() throws IOException
    {
        when(url.openConnection())
                .thenReturn(connection);
        assertNotNull(helper.openHTTPConnection("GET", url, apiSecrets));
    }

    @Test
    public void openHTTPConnection_GivenNullISecrets_ReturnsNull()
    {
        assertNull(helper.openHTTPConnection("GET", url, null));
    }

    @Test(expected=IOException.class)
    public void openHTTPConnection_GivenBadURL_ThrowsIOException() throws IOException
    {
        URL badURL = new URL(UUID.randomUUID().toString());
        assertNull(helper.openHTTPConnection("GET", badURL, apiSecrets));
    }

    @Test
    public void openHTTPConnection_GivenNullURL_ReturnsNull()
    {
        assertNull(helper.openHTTPConnection("GET", null, apiSecrets));
    }

    @Test
    public void openHTTPConnection_GivenNullRequestMethod_ReturnsNull()
    {
        assertNull(helper.openHTTPConnection(null, url, apiSecrets));
    }

    @Test
    public void openHTTPConnection_GivenEmptyRequestMethod_ReturnsNull()
    {
        assertNull(helper.openHTTPConnection("", url, apiSecrets));
    }

    @Test
    public void openHTTPConnection_GivenInvalidRequestMethod_ReturnsNull()
    {
        assertNull(helper.openHTTPConnection(UUID.randomUUID().toString(), url, apiSecrets));
    }


    @Test
    public void isValidRequestMethod_GivenInvalidRequestMethod_ReturnsFalse()
    {
        assertFalse(helper.isValidRequestMethod(UUID.randomUUID().toString()));
    }

    @Test
    public void isValidRequestMethod_GivenGETRequest_ReturnsTrue()
    {
        assertTrue(helper.isValidRequestMethod("GET"));
    }

    @Test
    public void isValidRequestMethod_GivenPUTRequest_ReturnsTrue()
    {
        assertTrue(helper.isValidRequestMethod("PUT"));
    }

    @Test
    public void isValidRequestMethod_GivenPOSTRequest_ReturnsTrue()
    {
        assertTrue(helper.isValidRequestMethod("POST"));
    }

    @Test
    public void isValidRequestMethod_GivenDELETERequest_ReturnsTrue()
    {
        assertTrue(helper.isValidRequestMethod("DELETE"));
    }
}
