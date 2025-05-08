package virtual.threads.webserver.server;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import virtual.threads.webserver.api.RequestHandler;
import virtual.threads.webserver.response.ResponseFactory;

/**
 * The main web server class,
 * @author David Jessup
 */
public class WebServer {

	private static final Logger LOG = LoggerFactory.getLogger(WebServer.class);

	private int port;

	private boolean running;

	private WebServerExecutor executor;
	private Thread executorThread;

	/**
	 * Creates a WebServer instance on the port specified. It will use a pool of up to <code>maxThreads</code> workers
	 * to handle incoming requests.
	 *
	 * @param port            The port number to bind the server to.
	 * @param timeout         Request timeout (in seconds). Requests which take longer than this will be terminated.
	 * @param requestHandlers A list of handlers to use to service requests. The order of the list determines the
	 *                        priority of the handlers (handlers appearing first will be given first opportunity to
	 *                        handle requests).
	 * @throws IllegalArgumentException if the port number is outside the valid range (i.e. 0-65535)
	 * @throws IllegalArgumentException if maxThreads is less than 1
	 * @throws IllegalArgumentException if no {@link RequestHandler}s are provided
	 */
	public WebServer(int port, int timeout, List<RequestHandler> requestHandlers) {

		if (port < 0 || port > 65535) {
			throw new IllegalArgumentException("Port must be in the range 0-65535.");
		}

		if (requestHandlers == null || requestHandlers.isEmpty()) {
			throw new IllegalArgumentException("At least one request handler must be provided, otherwise the server won't be able to do anything!");
		}

		this.port = port;

		ResponseFactory responseFactory = new ResponseFactory(requestHandlers);
		this.executor = new WebServerExecutor(port, timeout, responseFactory);
	}

	/**
	 * Starts the web server.
	 *
	 * @throws IllegalStateException if the server is already running or is not correctly configured.
	 */
	public synchronized void start() {

		if (running()) {
			throw new IllegalStateException("Server is already running.");
		}

		LOG.info("Starting web server on {} with {} worker threads.", port == 0 ? "dynamically allocated port" : "port " + port);

		executorThread = Thread.ofVirtual().start(executor);
		//executorThread.start();

		running = true;
	}

	/**
	 * Gracefully stops the server, waiting for established connections to either complete or timeout.
	 *
	 * @throws IllegalStateException if the server is not started
	 */
	public synchronized void stop() {

		if (!running()) {
			throw new IllegalStateException("Server isn't running.");
		}

		LOG.info("Stopping server");

		executor.stop();
		running = false;

		LOG.info("Server stopped.");

	}

	/**
	 * Checks if the server is currently running.
	 *
	 * @return Returns true if the server is running
	 */
	public synchronized boolean running() {
		return running;
	}

}
