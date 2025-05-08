package virtual.threads.webserver.api;

/**
 * An HTTP response message.
 *
 * @author Vitor Machado
 */
public interface Response extends HttpMessage {

    /**
     * Gets the status of the response (e.g. 200 OK).
     *
     * @return Returns the response status.
     */
    HttpStatus status();
}
