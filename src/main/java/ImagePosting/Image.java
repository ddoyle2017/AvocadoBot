package ImagePosting;

import java.util.List;

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
    private int bandwidth;
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

    public String getType()
    {
        return type;
    }

    public boolean isAnimated()
    {
        return animated;
    }

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }

    public int getSize()
    {
        return size;
    }

    public int getViews()
    {
        return views;
    }

    public int getBandwidth()
    {
        return bandwidth;
    }

    public String getVote()
    {
        return vote;
    }

    public boolean isFavorite()
    {
        return favorite;
    }

    public boolean isNsfw()
    {
        return nsfw;
    }

    public String getSection()
    {
        return section;
    }

    public String getAccountUrl()
    {
        return account_url;
    }

    public int getAccountId()
    {
        return accountId;
    }

    public boolean isAd()
    {
        return is_ad;
    }

    public boolean isInMostViral()
    {
        return in_most_viral;
    }

    public boolean hasSound()
    {
        return has_sound;
    }

    public List<Object> getTags()
    {
        return tags;
    }

    public int getAdType()
    {
        return ad_type;
    }

    public String getAdUrl()
    {
        return ad_url;
    }

    public boolean isInGallery()
    {
        return in_gallery;
    }

    public String getLink()
    {
        return link;
    }
}
