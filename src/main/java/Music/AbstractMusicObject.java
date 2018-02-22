package Music;

import static Resources.MusicValues.*;


abstract class AbstractMusicObject
{

    static String convertMSToTimeStamp(Long duration)
    {
        long durationInSeconds = duration / MS_IN_A_SECOND;
        long hours = durationInSeconds / SECONDS_IN_AN_HOUR;
        long minutes = (durationInSeconds / SECONDS_IN_A_MINUTE) % MINUTES_IN_AN_HOUR;
        long seconds = durationInSeconds % SECONDS_IN_A_MINUTE;

        String minString;
        String secString = (seconds < 10) ? ("0" + seconds) : Long.toString(seconds);

        if (hours > 0)
        {
            minString = (minutes < 10) ? ("0" + minutes) : Long.toString(minutes);
            return (hours + ":" + minString + ":" + secString);
        } else
        {
            minString = Long.toString(minutes);
            return (minString + ":" + secString);
        }
    }

    static String getYouTubeVideoThumbnail(String videoID)
    {
        return YOUTUBE_API_URL + videoID + GET_THUMBNAIL;
    }
}
