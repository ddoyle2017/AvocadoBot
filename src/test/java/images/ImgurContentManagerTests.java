package images;

import images.responses.Gallery;
import Utility.FileHelper;
import Utility.ISecrets;
import Utility.RESTHelper;
import com.google.gson.Gson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.Reader;
import java.net.URL;
import java.nio.file.Path;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ImgurContentManagerTests
{
    @Mock
    private Path filePath;
    @Mock
    private Reader reader;
    @Mock
    private Gson gson;
    @Mock
    private Gallery gallery;
    @Mock
    private ImgurSecrets imgurSecrets;
    @Mock
    private RESTHelper restHelper;
    @Mock
    private FileHelper fileHelper;

    @InjectMocks
    private ImgurContentManager contentManager;


    @Test
    public void getImgurGallery_GivenValidSearchQuery_ReturnsGallery()
    {
        when(restHelper.sendRESTRequest(anyString(), any(URL.class), anyString(), any(ISecrets.class)))
                .thenReturn(reader);
        when(gson.fromJson(any(Reader.class), any()))
                .thenReturn(imgurSecrets)
                .thenReturn(gallery);

        assertNotNull(contentManager.getImgurGallery(UUID.randomUUID().toString()));
    }

    @Test
    public void getImgurGallery_GivenNullSearchQuery_ReturnsNull()
    {
        assertNull(contentManager.getImgurGallery(null));
    }

    @Test
    public void getImgurGallery_GivenEmptySearchQuery_ReturnsNull()
    {
        assertNull(contentManager.getImgurGallery(""));
    }


    @Test
    public void getAuthenticationInfo_GivenValidParameters_ReturnsTrue()
    {
        when(fileHelper.getFileContent(any()))
                .thenReturn(reader);
        when(gson.fromJson(any(Reader.class), any()))
                .thenReturn(imgurSecrets);
        assertTrue(contentManager.getAuthenticationInfo(filePath, fileHelper));
    }

    @Test
    public void getAuthenticationInfo_GivenNullFilePath_ReturnsFalse()
    {
        assertFalse(contentManager.getAuthenticationInfo(null, fileHelper));
    }

    @Test
    public void getAuthenticationInfo_GivenNullFileHelper_ReturnsFalse()
    {
        assertFalse(contentManager.getAuthenticationInfo(filePath, null));
    }
}
