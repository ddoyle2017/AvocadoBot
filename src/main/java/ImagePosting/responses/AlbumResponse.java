package ImagePosting.responses;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AlbumResponse
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
        private String deletehash;
    }
}
