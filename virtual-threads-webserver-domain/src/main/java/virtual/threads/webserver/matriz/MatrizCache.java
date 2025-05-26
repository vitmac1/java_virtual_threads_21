package virtual.threads.webserver.matriz;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;

import static virtual.threads.webserver.matriz.util.MatrizProcessor.geraMatrizFixa;

/**
 * Componente responsável por gerar e armazenar, em memória transitória,
 * matrizes fixas associadas aos tamanhos definidos no enum {@link MatrizEnum}.
 */
@Component
public class MatrizCache {

    private final Map<MatrizEnum, Matriz> matrizEnumMap = new EnumMap<>(MatrizEnum.class);

    @PostConstruct
    public void init() {
        for (MatrizEnum matriz : MatrizEnum.values()) {
            matrizEnumMap.put(matriz, geraMatrizFixa(matriz.getTamanho()));
        }
    }

    public Matriz getMatriz(int tamanho) {
        return matrizEnumMap.get(MatrizEnum.getEnum(tamanho));
    }
}
