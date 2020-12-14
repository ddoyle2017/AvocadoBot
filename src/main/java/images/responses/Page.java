package images.responses;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Page
{
    private int page;
    private List<ThreadMetaData> threads;

    @Getter
    @Setter
    @NoArgsConstructor
    public class ThreadMetaData
    {
        private String no;
        private long last_modified;
        private int replies;
    }
}