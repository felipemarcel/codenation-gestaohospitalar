package gestao.api;

import gestao.model.Hospital;
import gestao.service.HospitalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@Controller
public class HospitalController {

    @Autowired
    private HospitalService service;

    @ResponseBody
    @GetMapping("/hospitais")
    public ResponseEntity<List<Hospital>> listAll() {
        return ok(service.listAll());
    }
}
