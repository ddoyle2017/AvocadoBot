package images;

import images.responses.CreditResponse;
import Utility.ISecrets;
import Utility.RESTHelper;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.io.Reader;
import java.net.URL;

/**
 * Manages the number of calls made to the Imgur API. Keeps track of their credit allocation system to make sure AvocadoBot
 * avoids being rate-limited and blocked.
 */
@RequiredArgsConstructor
class ImgurRateLimiter
{
    private static long POST_CREDIT_COST = 10L;
    private static long REQUEST_CREDIT_COST = 1L;
    private static long DAILY_REQUEST_LIMIT = 12500L;

    private long currentCredits;
    private final RESTHelper RESTHelper;
    private final Gson gson;
    private final ISecrets secrets;

    /**
     * Updates the bot's API credit amount depending on the type of REST request being made.
     * @param requestMethod A valid REST request method. Different methods have different API credit costs.
     * @return True, if the bot is rate limited. False, if the bot can still make requests to Imgur.
     * @throws IOException If the given request method is invalid. Doesn't update credit amount.
     */
    boolean updateCreditAmount(final String requestMethod) throws IOException
    {
        if (requestMethod == null || requestMethod.isEmpty()) throw new IOException("Empty or null REST request method. Credit has not been updated.");

        if ("POST".equalsIgnoreCase(requestMethod)) {
            this.currentCredits -= POST_CREDIT_COST;
        }
        else if ("GET".equalsIgnoreCase(requestMethod) || "PUT".equalsIgnoreCase(requestMethod) || "DELETE".equalsIgnoreCase(requestMethod)) {
            this.currentCredits -= REQUEST_CREDIT_COST;
        }
        else {
            System.err.println("Invalid REST request method. Credit has not been updated.");
        }
        return isRateLimited();
    }

    /**
     *
     * @param POSTRequests
     * @param otherRequests
     * @return
     */
    boolean canUploadEntireAlbum(final int POSTRequests, final int otherRequests)
    {
        final long totalCost = (POSTRequests * POST_CREDIT_COST) + (otherRequests * REQUEST_CREDIT_COST);
        return totalCost <= currentCredits;
    }

    long getRateLimitResetTime() {
        return 1575161540L;
    }

    private boolean isRateLimited() {
        return currentCredits <= 0;
    }

    private long getCurrentCredits() throws IOException {
        final Reader stream = RESTHelper.sendRESTRequest("GET", new URL("https://api.imgur.com/3/credits"), null, secrets);
        final CreditResponse response = gson.fromJson(stream, CreditResponse.class);

        if (!response.isSuccess()) throw new IOException("Retrieving rate limit credit amounts from Imgur returned " + response.getStatus());
        return response.getData().getUserRemaining();
    }
}
