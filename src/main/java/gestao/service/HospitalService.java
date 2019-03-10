package gestao.service;

import gestao.model.Hospital;
import gestao.repository.HospitalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.hibernate.internal.util.collections.ArrayHelper.toList;

@Service
public class HospitalService {

    @Autowired
    private HospitalRepository repository;

    public List<Hospital> listAll(){
        return toList(repository.findAll());
    }
}
