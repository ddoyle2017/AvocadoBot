package images.responses;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ImageResponse
{
    private Data data;
    private boolean success;
    private int status;

    @Getter
    @Setter
    @NoArgsConstructor
    public class Data
    {
        private String id;
    }
}
