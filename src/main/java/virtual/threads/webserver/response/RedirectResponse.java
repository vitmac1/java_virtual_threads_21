package virtual.threads.webserver.response;

import virtual.threads.webserver.api.HttpStatus;
import virtual.threads.webserver.api.HttpVersion;
import virtual.threads.webserver.headers.Headers;
import virtual.threads.webserver.headers.HttpHeader;
import virtual.threads.webserver.headers.HttpHeaders;

/**
 * An response which sends a 301 redirect to the specified location.
 *
 * @author David Jessup
 */
public class RedirectResponse extends HttpResponse {

	private final String destination;

	/**
	 * Creates a new {@link RedirectResponse}.
	 *
	 * @param destination the location to redirect to
	 * @param version     the HTTP version of the response
	 */
	public RedirectResponse(String destination, HttpVersion version) {
		super(HttpStatus.MOVED_PERMANENTLY_301, version);
		this.destination = destination;
	}

	@Override
	public Headers headers() {
		return new HttpHeaders(new HttpHeader("Location", destination));
	}
}
