package ImagePosting;

import lombok.Getter;
import lombok.Setter;

/**
 * Imgur Secrets
 *
 * Object for encapsulating Imgur application authentication information held in a JSON file.
 */
@Getter
@Setter
class ImgurSecrets
{
    private String clientID;
    private String clientSecret;
}
