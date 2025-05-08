package virtual.threads.webserver.response;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import virtual.threads.webserver.api.HttpStatus;
import virtual.threads.webserver.api.HttpVersion;
import virtual.threads.webserver.api.Response;
import virtual.threads.webserver.headers.Headers;
import virtual.threads.webserver.headers.HttpHeader;
import virtual.threads.webserver.headers.HttpHeaders;

/**
 * An abstract HTTP response message. Intended for building ad-hoc responses, or as a base class for specialised
 * response types.
 *
 * @author David Jessup
 */
public class HttpResponse implements Response {

	private HttpStatus status;
	private Headers headers;
	private String body;
	private HttpVersion version;

	/**
	 * Creates an new {@link HttpResponse} with no headers or response body.
	 *
	 * @param status  the HTTP status code of the response
	 * @param version the HTTP version of the response
	 */
	HttpResponse(HttpStatus status, HttpVersion version) {
		this(status, new HttpHeaders(), "", version);
	}

	/**
	 * Creates a new {@link HttpResponse}
	 *
	 * @param status  the HTTP status code of the response
	 * @param headers the HTTP header collection of the response
	 * @param body    the response body
	 * @param version the HTTP version of the response
	 */
	public HttpResponse(HttpStatus status, Headers headers, String body, HttpVersion version) {
		this.status = status;
		this.headers = headers;
		this.body = body;
		this.version = version;
	}

	/**
	 * Creates a new {@link HttpResponse} with a string-based response body and automatic Content-length header.
	 *
	 * @param status  the HTTP status code of the response
	 * @param body    the response body
	 * @param version the HTTP version of the response
	 */
	public HttpResponse(HttpStatus status, String body, HttpVersion version) {
		this(status, new HttpHeaders(new HttpHeader("Content-length", String.valueOf(body.length()))), body, version);
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
		return version;
	}

	@Override
	public HttpStatus status() {
		return status;
	}
}
