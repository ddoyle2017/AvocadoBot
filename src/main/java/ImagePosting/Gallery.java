package ImagePosting;

import lombok.Builder;
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
class Gallery
{
    private List<Album.Data> data;
    private boolean success;
    private int status;
}
