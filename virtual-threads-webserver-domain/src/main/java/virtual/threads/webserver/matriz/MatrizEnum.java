package virtual.threads.webserver.matriz;

import lombok.Getter;

import java.util.stream.Stream;

@Getter
public enum MatrizEnum {

    PEQUENA(1000),
    MEDIA(2000),
    GRANDE(3000);

    private final int tamanho;

    MatrizEnum(int tamanho) {
        this.tamanho = tamanho;
    }

    public static MatrizEnum getEnum(int tamanho) {

        return Stream.of(values())
                .filter(value -> value.getTamanho() == tamanho)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Tamanho inválido: " + tamanho));
    }
}
