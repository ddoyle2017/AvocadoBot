package ImagePosting;

import java.util.List;

public class ImgurAlbum
{
    private Data data;
    private boolean success;
    private int status;


    public Data getData()
    {
        return data;
    }

    public boolean isSuccess()
    {
        return success;
    }

    public int getStatus()
    {
        return status;
    }

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
        private List<ImgurImage> images;


        public String getId()
        {
            return id;
        }

        public String getTitle()
        {
            return title;
        }

        public String getDescription()
        {
            return description;
        }

        public int getDatetime()
        {
            return datetime;
        }

        public String getCover()
        {
            return cover;
        }

        public int getCoverWidth()
        {
            return cover_width;
        }

        public int getCoverHeight()
        {
            return cover_height;
        }

        public String getAccountUrl()
        {
            return account_url;
        }

        public int getAccountId()
        {
            return account_id;
        }

        public String getPrivacy()
        {
            return privacy;
        }

        public String getLayout()
        {
            return layout;
        }

        public int getViews()
        {
            return views;
        }

        public String getLink()
        {
            return link;
        }

        public boolean isFavorite()
        {
            return favorite;
        }

        public boolean isNSFW()
        {
            return nsfw;
        }

        public String getSection()
        {
            return section;
        }

        public int getImagesCount()
        {
            return images_count;
        }

        public boolean isInGallery()
        {
            return in_gallery;
        }

        public boolean isAd()
        {
            return is_ad;
        }

        public List<ImgurImage> getImages()
        {
            return images;
        }
    }
}
