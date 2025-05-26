package virtual.threads.webserver.matriz.service;

import virtual.threads.webserver.matriz.Matriz;

public interface MatrizQueryService {

    Matriz processaMatriz(Integer tamanho, Integer threads);
}
