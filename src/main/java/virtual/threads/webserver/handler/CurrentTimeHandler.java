package virtual.threads.webserver.handler;

import virtual.threads.webserver.api.HttpMethod;
import virtual.threads.webserver.api.Request;
import virtual.threads.webserver.api.RequestHandler;
import virtual.threads.webserver.api.Response;
import virtual.threads.webserver.response.CurrentTimeResponse;

public class CurrentTimeHandler implements RequestHandler {

    // Caminho da rota que o handler irá processar
    private static final String PATH = "/time";

    /**
     * Verifica se este handler pode processar a requisição recebida.
     * O handler processa apenas requisições GET para o caminho "/time".
     *
     * @param request a requisição recebida
     * @return true se o handler pode processar a requisição, false caso contrário
     */
    @Override
    public boolean canHandle(Request request) {
        return request.method() == HttpMethod.GET && PATH.equals(request.uri().getPath());
    }

    /**
     * Processa a requisição e retorna uma resposta com a hora atual.
     * A resposta será do tipo CurrentTimeResponse, que contém a hora formatada como texto.
     *
     * @param request a requisição recebida
     * @return a resposta contendo a hora atual
     */
    @Override
    public Response handle(Request request) {
        return new CurrentTimeResponse();
    }
}
