package ImagePosting.requests;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
public class AlbumCreation
{
    private List<Integer> ids;
    private List<Integer> deletehashes;
    private String title;
    private String description;
    private String privacy;
    private Integer cover;
}
