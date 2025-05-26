package virtual.threads.webserver.matriz.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import virtual.threads.webserver.matriz.Matriz;
import virtual.threads.webserver.matriz.MatrizCache;
import virtual.threads.webserver.matriz.util.MatrizProcessor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MatrizQueryServiceImpl implements MatrizQueryService {

    private final @NonNull
    MatrizCache matrizCache;

    @Override
    public Matriz processaMatriz(Integer tamanho, Integer threads) {

        log.debug("Iniciando processamento da matriz de tamanho: {} e número de threads: {} às {}",
                tamanho, threads, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));

        Matriz matriz = matrizCache.getMatriz(tamanho);

        return MatrizProcessor.processaMatriz(matriz, matriz, threads, tamanho);
    }
}
