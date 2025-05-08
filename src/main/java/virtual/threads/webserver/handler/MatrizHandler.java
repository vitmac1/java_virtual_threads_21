package virtual.threads.webserver.handler;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import virtual.threads.webserver.api.HttpMethod;
import virtual.threads.webserver.api.HttpStatus;
import virtual.threads.webserver.api.Request;
import virtual.threads.webserver.api.RequestHandler;
import virtual.threads.webserver.api.Response;
import virtual.threads.webserver.response.HttpResponse;
import virtual.threads.webserver.util.QueryParams;

@Slf4j
public class MatrizHandler implements RequestHandler {

    // Caminho da rota que o handler irá processar
    private static final String MATRIX_PATH = "/matrix";

    private static final ObjectMapper objectMapper = new ObjectMapper();


    @Override
    public boolean canHandle(Request request) {

        return request.method() == HttpMethod.GET && MATRIX_PATH.equals(request.uri().getPath());
    }

    @Override
    public Response handle(Request request) {

        try {

            Map<String, String> params = QueryParams.parse(request.uri());

            int tamanho = Integer.parseInt(params.get("tamanho"));
            int threads = Integer.parseInt(params.get("threads"));

            // Gerar matrizes
            int[][] matrizA = gerarMatrizAleatoria(tamanho);
            int[][] matrizB = gerarMatrizAleatoria(tamanho);

            // Multiplicar as matrizes (processamento concorrente)
            int[][] matrizFinal = multiplicarMatrizes(matrizA, matrizB, threads);

            String matrizJSON = converteMatrizToJSON(matrizFinal);

            log.debug("Fim do processamento da matriz ({}x{}) às {}", tamanho, tamanho, LocalDateTime.now());

            return new HttpResponse(HttpStatus.OK_200, matrizJSON, request.version());
        } catch (Exception e) {
            return new HttpResponse(HttpStatus.INTERNAL_SERVER_ERROR_500, e.getMessage(), request.version());
        }
    }

    /**
     * Gera uma matriz aleatória de inteiros.
     *
     * @param tamanho Tamanho da matriz (número de linhas e colunas)
     * @return Matriz gerada aleatoriamente
     */
    private int[][] gerarMatrizAleatoria(int tamanho) {
        int[][] matriz = new int[tamanho][tamanho];
        for (int i = 0; i < tamanho; i++) {
            for (int j = 0; j < tamanho; j++) {
                matriz[i][j] = 1; // Preenche com valor fixo 1 -- TODO Provávelmente vamos alterar a lógica aqui.
            }
        }
        return matriz;
    }

    /**
     * Multiplica duas matrizes usando processamento paralelo.
     *
     * @param matrizA Matriz A
     * @param matrizB Matriz B
     * @return Matriz resultado
     */
    private int[][] multiplicarMatrizes(int[][] matrizA, int[][] matrizB, int threads) throws InterruptedException {
        int tamanho = matrizA.length;
        int[][] resultado = new int[tamanho][tamanho];

        ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

        for (int t = 0; t < threads; t++) {
            final int threadId = t;
            executor.submit(() -> {
                for (int i = threadId; i < tamanho; i += threads) {
                    for (int j = 0; j < tamanho; j++) {
                        int soma = 0;
                        for (int k = 0; k < tamanho; k++) {
                            soma += matrizA[i][k] * matrizB[k][j];
                        }
                        resultado[i][j] = soma;
                    }
                }
            });
        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);

        return resultado;
    }

    private String converteMatrizToJSON(int[][] matriz) {

        try {

            return objectMapper.writeValueAsString(matriz);

        } catch (JsonProcessingException e) {

            throw new RuntimeException("Erro ao converter matriz para JSON", e);
        }
    }
}
