package ImagePosting.responses;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Image
{
    private String id;
    private String title;
    private String description;
    private int datetime;
    private String type;
    private boolean animated;
    private int width;
    private int height;
    private int size;
    private int views;
    private long bandwidth;
    private String vote;
    private boolean favorite;
    private boolean nsfw;
    private String section;
    private String account_url;
    private int accountId;
    private boolean is_ad;
    private boolean in_most_viral;
    private boolean has_sound;
    private List<Object> tags = null;
    private int ad_type;
    private String ad_url;
    private boolean in_gallery;
    private String link;
}
