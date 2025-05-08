package virtual.threads.webserver.response;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import virtual.threads.webserver.api.HttpStatus;
import virtual.threads.webserver.api.HttpVersion;
import virtual.threads.webserver.api.Response;
import virtual.threads.webserver.headers.Headers;
import virtual.threads.webserver.headers.HttpHeader;
import virtual.threads.webserver.headers.HttpHeaders;

public class CurrentTimeResponse implements Response {

    // A string contendo a hora atual formatada
    private final String timeString;

    /**
     * Construtor da resposta.
     * Calcula a hora atual e a formata como uma string.
     */
    public CurrentTimeResponse() {
        // Converte a hora atual em uma string
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mm a");
        timeString = "The time is " + LocalDateTime.now().format(formatter);
    }

    /**
     * Retorna o status HTTP da resposta (200 OK).
     *
     * @return o status da resposta (200 OK)
     */
    @Override
    public HttpStatus status() {
        return HttpStatus.OK_200;
    }

    /**
     * Retorna o corpo da resposta como um stream de bytes.
     * O corpo da resposta contém a hora atual como uma string.
     *
     * @return o corpo da resposta como um {@link InputStream}
     */
    @Override
    public InputStream stream() {
        return new ByteArrayInputStream(timeString.getBytes());
    }

    /**
     * Retorna os cabeçalhos HTTP para a resposta.
     * Define o tipo de conteúdo como "text/plain" e o comprimento do conteúdo.
     *
     * @return os cabeçalhos HTTP da resposta
     */
    @Override
    public Headers headers() {
        return new HttpHeaders(
                new HttpHeader("Content-type", "text/plain"),
                new HttpHeader("Content-length", String.valueOf(timeString.length())));
    }

    /**
     * Retorna a versão do protocolo HTTP utilizada na resposta (HTTP/1.0).
     *
     * @return a versão do protocolo HTTP
     */
    @Override
    public HttpVersion version() {
        return HttpVersion.HTTP_1_0;
    }
}
