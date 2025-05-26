package virtual.threads.webserver.matriz.util;

import lombok.extern.slf4j.Slf4j;
import virtual.threads.webserver.matriz.Matriz;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
public class MatrizProcessor {

    public static Matriz geraMatrizFixa(int tamanho) {

        Matriz matriz = new Matriz(new int[tamanho][tamanho]);

        int valor = 0;

        for (int i = 0; i < tamanho; i++) {
            for (int j = 0; j < tamanho; j++) {
                matriz.getMatriz()[i][j] = valor++;
                if (valor > 100) valor = 0;
            }
        }
        return matriz;
    }

    public static Matriz processaMatriz(Matriz matrizA, Matriz matrizB, int threads, int tamanho) {

        Matriz matrizResultado = new Matriz(new int[tamanho][tamanho]);

        ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

        boolean tarefaFinalizada = false;

        try {
            for (int t = 0; t < threads; t++) {
                final int threadId = t;
                executor.submit(() -> {
                    for (int i = threadId; i < tamanho; i += threads) {
                        for (int j = 0; j < tamanho; j++) {
                            int soma = 0;
                            for (int k = 0; k < tamanho; k++) {
                                soma += matrizA.getMatriz()[i][k] * matrizB.getMatriz()[k][j];
                            }
                            matrizResultado.getMatriz()[i][j] = soma;
                        }
                    }
                });
            }

            executor.shutdown();

            tarefaFinalizada = executor.awaitTermination(5, TimeUnit.MINUTES);

            if (!tarefaFinalizada) {

                log.warn("Tempo limite excedido! Forçando o encerramento das tarefas.");
                executor.shutdownNow(); // Força a interrupção das tarefas
                throw new RuntimeException("Processamento da matriz excedeu o tempo limite.");
            }

            log.debug("Finalizando processamento da matriz de tamanho: {} e número de threads: {} às {}", tamanho,
                    threads, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));

            return matrizResultado;
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt(); // Preserva o estado de interrupção
            throw new RuntimeException("Thread interrompida", e);
        }
    }
}
