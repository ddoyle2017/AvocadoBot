package ImagePosting.requests;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
public
class ImageUpload
{
    private String image;
    private int album;
    private String type;
    private String name;
    private String title;
    private String description;
}
