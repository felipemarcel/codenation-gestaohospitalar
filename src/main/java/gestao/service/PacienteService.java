package gestao.service;

import com.google.maps.DistanceMatrixApi;
import com.google.maps.DistanceMatrixApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.LatLng;
import gestao.exception.PacienteNotFoundException;
import gestao.model.Hospital;
import gestao.model.Paciente;
import gestao.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.google.maps.DistanceMatrixApi.getDistanceMatrix;
import static com.google.maps.DistanceMatrixApi.newRequest;
import static java.util.stream.Collectors.toList;

@Service
public class PacienteService {

    @Autowired
    private PacienteRepository repository;

    @Autowired
    private HospitalService hospitalService;

    GeoApiContext context = new GeoApiContext.Builder()
            .apiKey("AIzaSyCBhDVDAgb81AG7HMiIajkSywvDkQ_M5rE")
            .build();

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

    public List<Paciente> listPacientesInternadosSemAlta(Long idHospital) {
        return this.repository.listPacientesInternadosSemAlta(idHospital);
    }

    public List<Paciente> listPacientesInternados(Long idHospital) {
        return this.repository.listPacientesInternados(idHospital);
    }

    public Hospital listAllNeraby(Long id) {
        Paciente paciente = findById(id);
        List<Hospital> hospitais = hospitalService.listAll();

        DistanceMatrix distanceMatrixApi = null;
        try {
            distanceMatrixApi = newRequest(context)
                    .origins(new LatLng(paciente.getLatitude().doubleValue(), paciente.getLongitude().doubleValue()))
                    .destinations(coordinates.toArray(new LatLng[coordinates.size()]))
                    .await();
        } catch (ApiException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public DistanceMatrix getDistanceMatrixApi(Paciente paciente, List<Hospital> hospitais) throws InterruptedException, ApiException, IOException {
        List<LatLng> coordinates = hospitais.stream()
                .map(hospital -> new LatLng(hospital.getLatitude().doubleValue(), hospital.getLongitude().doubleValue()))
                .collect(toList());
        return newRequest(context)
                .origins(new LatLng(paciente.getLatitude().doubleValue(), paciente.getLongitude().doubleValue()))
                .destinations(coordinates.toArray(new LatLng[coordinates.size()]))
                .await();
    }
}
