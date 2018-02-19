package ImagePosting;

import java.util.List;

public class Gallery
{
    private List<Album.Data> data;
    private boolean success;
    private int status;


    public List<Album.Data> getData()
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
}
