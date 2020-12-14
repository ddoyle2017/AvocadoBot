package images.requests;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageUpload
{
    private String image;
    private String album;
    private String type;
    private String name;
    private String title;
    private String description;
}
