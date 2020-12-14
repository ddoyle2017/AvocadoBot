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
    private final static String TEST_JSON = "{ }";

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
        assertNotNull(helper.openHTTPConnection("GET", url, TEST_JSON, apiSecrets));
    }

    @Test
    public void openHTTPConnection_GivenNullISecrets_ReturnsNull()
    {
        assertNull(helper.openHTTPConnection("GET", url, TEST_JSON,null));
    }

    @Test(expected=IOException.class)
    public void openHTTPConnection_GivenBadURL_ThrowsIOException() throws IOException
    {
        URL badURL = new URL(UUID.randomUUID().toString());
        assertNull(helper.openHTTPConnection("GET", badURL, TEST_JSON, apiSecrets));
    }

    @Test
    public void openHTTPConnection_GivenNullURL_ReturnsNull()
    {
        assertNull(helper.openHTTPConnection("GET", null, TEST_JSON, apiSecrets));
    }

    @Test
    public void openHTTPConnection_GivenNullRequestMethod_ReturnsNull()
    {
        assertNull(helper.openHTTPConnection(null, url, TEST_JSON, apiSecrets));
    }

    @Test
    public void openHTTPConnection_GivenEmptyRequestMethod_ReturnsNull()
    {
        assertNull(helper.openHTTPConnection("", url, TEST_JSON, apiSecrets));
    }

    @Test
    public void openHTTPConnection_GivenInvalidRequestMethod_ReturnsNull()
    {
        assertNull(helper.openHTTPConnection(UUID.randomUUID().toString(), url, TEST_JSON, apiSecrets));
    }
}
