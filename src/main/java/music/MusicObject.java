package music;

import static constants.MusicValues.*;


abstract class MusicObject
{

    static String convertMSToTimeStamp(final Long duration)
    {
        final long durationInSeconds = duration / MS_IN_A_SECOND;
        final long hours = durationInSeconds / SECONDS_IN_AN_HOUR;
        final long minutes = (durationInSeconds / SECONDS_IN_A_MINUTE) % MINUTES_IN_AN_HOUR;
        final long seconds = durationInSeconds % SECONDS_IN_A_MINUTE;

        final String minString;
        final String secString = (seconds < 10) ? ("0" + seconds) : Long.toString(seconds);

        if (hours > 0) {
            minString = (minutes < 10) ? ("0" + minutes) : Long.toString(minutes);
            return (hours + ":" + minString + ":" + secString);
        }
        else {
            minString = Long.toString(minutes);
            return (minString + ":" + secString);
        }
    }

    static String getYouTubeVideoThumbnail(final String videoID) {
        return YOUTUBE_API_URL + videoID + GET_THUMBNAIL;
    }
}
