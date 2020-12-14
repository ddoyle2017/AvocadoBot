package images.requests;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlbumCreation
{
    private List<String> ids;
    private List<String> deletehashes;
    private String title;
    private String description;
    private String privacy;
    private Integer cover;
}
