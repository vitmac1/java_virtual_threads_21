package virtual.threads.webserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class App {

    /**
     * Main application loop. Parses the command line args then tries to start a server instance.
     *
     * @param args String array of command line arguments
     */
    public static void main(String[] args) {

        SpringApplication.run(App.class, args);
    }
}
