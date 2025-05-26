package virtual.threads.webserver.matriz.endpoint;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import virtual.threads.webserver.matriz.Matriz;
import virtual.threads.webserver.matriz.service.MatrizQueryService;

@RestController
@RequestMapping("/matriz")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MatrizQueryEndpoint {

    private final @NonNull
    MatrizQueryService matrizQueryService;

    @GetMapping("/processamatriz")
    public ResponseEntity<Matriz> processaMatriz(@RequestParam("tamanho") Integer tamanho,
                                                 @RequestParam("threads") Integer threads)  {

        Matriz matriz = matrizQueryService.processaMatriz(tamanho, threads);

        return new ResponseEntity<>(matriz, HttpStatus.OK);
    }
}

