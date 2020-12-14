package images;

import Utility.ISecrets;
import lombok.Getter;
import lombok.Setter;

/**
 * Encapsulates Imgur API authentication information held in a JSON file.
 */
@Getter
@Setter
class ImgurSecrets implements ISecrets
{
    private String clientID;
    private String clientSecret;
    private String requestKey;
    private String requestKeyValue;
}
