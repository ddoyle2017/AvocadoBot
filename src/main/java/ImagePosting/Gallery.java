package ImagePosting;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Gallery
{
    private List<Album.Data> data;
    private boolean success;
    private int status;
}
