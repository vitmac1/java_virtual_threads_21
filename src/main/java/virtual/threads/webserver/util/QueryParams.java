package virtual.threads.webserver.util;

import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Utility class for parsing query parameters from a URI.
 */
public class QueryParams {

    /**
     * Parses the query string from a given URI and returns a map of key-value pairs.
     *
     * @param uri the URI containing the query string
     * @return a map of query parameters
     */

    public static Map<String, String> parse(URI uri) {

        Map<String, String> params = new HashMap<>();

        String query = uri.getQuery();

        if (query == null || query.isEmpty()) {
            return params;
        }

        String[] pairs = query.split("&");

        for (String pair : pairs) {
            String[] kv = pair.split("=", 2);
            String key = URLDecoder.decode(kv[0], StandardCharsets.UTF_8);
            String value = kv.length > 1 ? URLDecoder.decode(kv[1], StandardCharsets.UTF_8) : "";
            params.put(key, value);
        }

        return params;
    }
}
