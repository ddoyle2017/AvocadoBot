package ImagePosting;

/**
 * Imgur Secrets
 *
 * Object for encapsulating Imgur application authentication information held in a JSON file.
 */
public class ImgurSecrets
{
    private String clientID;
    private String clientSecret;

    public String getClientID()
    {
        return clientID;
    }

    public String getClientSecret()
    {
        return clientSecret;
    }
}
