    package virtual.threads.webserver;

    import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;

    import java.io.IOException;
    import java.util.Collections;

    import virtual.threads.webserver.api.RequestHandler;
    import virtual.threads.webserver.handler.MatrizHandler;
    import virtual.threads.webserver.server.WebServer;

    /**
     * Main application bootstrap class. Parses CLI options and uses them to create a new server instance.
     *
     * @author David Jessup
     */
    public class App {

        private static final Logger LOG = LoggerFactory.getLogger(App.class);

        private App() {}

        /**
         * Main application loop. Parses the command line args then tries to start a server instance.
         *
         * @param args String array of command line arguments
         */
        public static void main(String[] args) {

            CommandLineOptions options = new CommandLineOptions(args);

            // If the help options is set, print usage and exit.
            if (options.help()) {
                printUsage();
                return;
            }

            // Otherwise read the CLI options (or defaults, as defined in CommandLineOptions)
            int port = options.port();
            int timeout = options.timeout();

            // Configure request handlers
            RequestHandler matrizHandler = new MatrizHandler();

            // Create the server
            WebServer server = new WebServer(port, timeout, Collections.singletonList(matrizHandler));

            // Register a shutdown hook to gracefully stop the server when the JVM is terminated
            Runtime.getRuntime().addShutdownHook(new Thread(new ShutdownHook(server)));

            // Start the server
            server.start();

            try {
                // This will keep the main thread alive as long as the server is running
                Thread.currentThread().join(); // Blocks the main thread indefinitely until shutdown
            } catch (InterruptedException e) {
                LOG.error("Main thread interrupted", e);
            }
        }

        /**
         * Displays usage information
         */
        // CHECKSTYLE:OFF - Allow use of System.out to display CLI usage
        @SuppressWarnings("squid:S106")
        private static void printUsage() {
            System.out.println("Java Web Server");
            System.out.println("---------------");
            System.out.println("\tA simple multi-threaded web server written in Java and implementing the HTTP/1.1 specification.\n");

            System.out.println("Usage");
            System.out.println("-----");
            System.out.println("\tjava -jar java-webserver.jar [options]\n\n");

            try {
                new CommandLineOptions().printHelpOn(System.out);
            } catch (IOException e) {
                LOG.error("Unable to display help/usage information", e);
            }
        }
        //CHECKSTYLE:ON

        /**
         * The ShutdownHook is registered to run when the JVM is terminated so the server can be terminated gracefully and
         * in-progress requests can be completed.
         */
        private static class ShutdownHook implements Runnable {

            private static final Logger LOG = LoggerFactory.getLogger(ShutdownHook.class);

            private WebServer server;

            ShutdownHook(WebServer server) {
                this.server = server;
            }

            @Override
            public void run() {
                if (server.running()) {
                    server.stop();
                } else {
                    LOG.debug("Server not running, nothing to do.");
                }
            }
        }

    }
