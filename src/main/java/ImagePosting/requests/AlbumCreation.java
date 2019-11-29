package ImagePosting.requests;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlbumCreation
{
    private List<Integer> ids;
    private List<Integer> deletehashes;
    private String title;
    private String description;
    private String privacy;
    private Integer cover;
}
