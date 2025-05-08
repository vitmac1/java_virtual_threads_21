	package virtual.threads.webserver.server;

	import java.io.IOException;
	import java.net.ServerSocket;
	import java.net.Socket;
	import java.net.SocketException;
	import java.net.SocketTimeoutException;
	import java.util.concurrent.ExecutorService;
	import java.util.concurrent.Executors;
	import java.util.concurrent.TimeUnit;

	import org.slf4j.Logger;
	import org.slf4j.LoggerFactory;

	import virtual.threads.webserver.api.Request;
	import virtual.threads.webserver.api.Response;
	import virtual.threads.webserver.response.ResponseFactory;

	/**
	 * The executor maintains a thread pool for worker threads servicing individual client connections.
	 *
	 * @author David Jessup
	 */
	public class WebServerExecutor implements Runnable {

		private static final Logger LOG = LoggerFactory.getLogger(WebServerExecutor.class);
		private final ResponseFactory responseFactory;
		private int port;
		private int timeout;
		private ExecutorService threadPool;
		private boolean running;

		/**
		 * Creates a new {@link WebServerExecutor}.
		 *
		 * @param port            the port the executor will listen for client connections on
		 * @param timeout         the timeout in seconds for client connections
		 * @param responseFactory the response factory to use to generate {@link Response}s for
		 *                        incoming {@link Request}s
		 */
		public WebServerExecutor(int port, int timeout, ResponseFactory responseFactory) {
			this.port = port;
			this.timeout = timeout;
			this.responseFactory = responseFactory;

			running = false;
		}

		@Override
		public void run() {
			running = true;

			threadPool = Executors.newVirtualThreadPerTaskExecutor();

			try (ServerSocket serverSocket = new ServerSocket(port)) {

				// Only block for 1 second so the running loop can escape if the server is stopped.
				serverSocket.setSoTimeout(1000);

				LOG.info("Server listening on port {}", serverSocket.getLocalPort());

				while (running()) {
					handleConnection(serverSocket);
				}

			} catch (IOException e) {
				LOG.warn("Error listening for client connection.", e);
				stop();
			}
		}

		/**
		 * Checks if the executor is running.
		 *
		 * @return Returns true if the executor is running.
		 */
		public synchronized boolean running() {
			return running;
		}

		/**
		 * Stops the executor.
		 */
		public synchronized void stop() {
			running = false;

			LOG.info("Shutting down web server executor.");

			try {
				if (threadPool != null) {
					threadPool.shutdown();
					threadPool.awaitTermination(timeout, TimeUnit.SECONDS);
				}
			} catch (InterruptedException e) {
				LOG.error("Worker thread pool was interrupted while shutting down. Some client connections may have been terminated prematurely.", e);
				Thread.currentThread().interrupt();
			}

			LOG.info("Web server executor has shutdown.");
		}

		@SuppressWarnings("squid:S1166") // Ignore the suppressed SocketTimeoutException
		private void handleConnection(ServerSocket serverSocket) throws IOException {
			try {
				// Wait for a client connection
				Socket client = serverSocket.accept();

				// Assign the connection to a worker
				Runnable worker = createWorker(client);

				// Queue the worker for execution
				threadPool.execute(worker);
			} catch (SocketTimeoutException e) {
				/* This exception is expected due to the short socket timeout set in run(), which gives the connection loop
				 a chance to escape if the server is stopped. Here we just swallow the exception and return control to the
				 connection loop. */
			}
		}

		private Runnable createWorker(Socket client) {
			try {
				client.setSoTimeout(timeout * 1000);
			} catch (SocketException e) {
				LOG.warn("Unable to set socket timeout", e);
			}
			return new WebWorker(client, responseFactory);
		}

	}
