package ImagePosting.responses;

import ImagePosting.responses.Album;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 *
 */
@Getter
@Setter
@NoArgsConstructor
public
class Gallery
{
    private List<Album.Data> data;
    private boolean success;
    private int status;
}
