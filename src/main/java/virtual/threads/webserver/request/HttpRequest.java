package virtual.threads.webserver.request;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;

import virtual.threads.webserver.api.HttpMethod;
import virtual.threads.webserver.api.HttpVersion;
import virtual.threads.webserver.api.Request;
import virtual.threads.webserver.headers.Headers;
import virtual.threads.webserver.headers.UnmodifiableHttpHeaders;

/**
 * An HTTP request message.
 *
 * @author David Jessup
 */
public class HttpRequest implements Request {

    private final RequestLine requestLine;
    private final Headers headers;
    private final String body;

    /**
     * Creates a new {@link HttpRequest} object.
     *
     * @param requestLine the request line, containing the method, URI, and HTTP version of the request.
     * @param headers     the collection of headers attached to the request
     * @param body        a string of data for the request's entity body
     */
    public HttpRequest(RequestLine requestLine, Headers headers, String body) {
        this.requestLine = requestLine;
        this.headers = new UnmodifiableHttpHeaders(headers);
        this.body = body;
    }

    @Override
    public InputStream stream() {
        return new ByteArrayInputStream(body.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public Headers headers() {
        return headers;
    }

    @Override
    public HttpVersion version() {
        return requestLine.version();
    }

    @Override
    public HttpMethod method() {
        return requestLine.method();
    }

    @Override
    public URI uri() {
        return requestLine.uri();
    }
}
