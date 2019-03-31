package gestao.apiTest;

import gestao.model.Procedimento;
import gestao.serviceTest.ProcedimentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/procedimentos")
public class ProcedimentoController {

    @Autowired
    private ProcedimentoService service;

    @PostMapping
    // TODO Implementar método para salvar procedimentos
    public ResponseEntity<?> save(@RequestBody Procedimento procedimento){
        return null;
    }
}
