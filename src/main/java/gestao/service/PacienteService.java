package gestao.service;

import gestao.exception.PacienteNotFoundException;
import gestao.model.Paciente;
import gestao.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PacienteService {

    @Autowired
    private PacienteRepository repository;

    public List<Paciente> listAll() {
        return this.repository.findAll();
    }

    public Paciente save(Paciente paciente) {
        return this.repository.save(paciente);
    }

    public Paciente findById(Long id) {
        Optional<Paciente> paciente = this.repository.findById(id);
        if (paciente == null || !paciente.isPresent()) {
            throw new PacienteNotFoundException(id);
        }
        return paciente.get();
    }

    public void update(Long id, Paciente paciente) {
        Paciente saved = this.findById(id);
        saved.setNomeCompleto(paciente.getNomeCompleto());
        saved.setCpf(paciente.getNomeCompleto());
        saved.setSexo(paciente.getSexo());
        saved.setDataNascimento(paciente.getDataNascimento());
        saved.setEndereco(paciente.getEndereco());
        saved.setLatitude(paciente.getLatitude());
        saved.setLongitude(paciente.getLongitude());
        this.save(saved);
    }
}
