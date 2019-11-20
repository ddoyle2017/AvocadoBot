package ImagePosting;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
class Album
{
    private Data data;
    private boolean success;
    private int status;

    @Getter
    @Setter
    @NoArgsConstructor
    class Data
    {
        private String id;
        private String title;
        private String description;
        private int datetime;
        private String cover;
        private int cover_width;
        private int cover_height;
        private String account_url;
        private int account_id;
        private String privacy;
        private String layout;
        private int views;
        private String link;
        private boolean favorite;
        private boolean nsfw;
        private String section;
        private int images_count;
        private boolean in_gallery;
        private boolean is_ad;
        private List<Image> images;
    }
}
