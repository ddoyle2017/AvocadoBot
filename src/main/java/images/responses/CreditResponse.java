package images.responses;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreditResponse
{
    private Data data;
    private boolean success;
    private int status;

    @Getter
    @Setter
    @NoArgsConstructor
    public class Data
    {
        private long UserLimit;
        private long UserRemaining;
        private long UserReset;
        private long ClientLimit;
        private long ClientRemaining;
    }
}
