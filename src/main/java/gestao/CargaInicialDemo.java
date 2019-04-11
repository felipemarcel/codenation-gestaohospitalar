package gestao;

import gestao.model.*;
import gestao.service.HospitalService;
import gestao.service.LeitoService;
import gestao.service.PacienteService;
import gestao.service.ProdutoService;
import gestao.tipo.FatorRH;
import gestao.tipo.Sangue;
import gestao.tipo.Tipo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class CargaInicialDemo implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private HospitalService hospitalService;

    @Autowired
    private ProdutoService produtoService;

    @Autowired
    private PacienteService pacienteService;

    @Autowired
    private LeitoService leitoService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        this.initData();
    }

    private void initData() {
        this.loadHospitais();
        this.loadPacientes();
    }

    private void loadPacientes() {
        this.pacienteService.save(this.createPaciente());
    }

    private void loadProdutos(int qtdOp, int qtdOn, Hospital hospital) {
        Produto sangueOPositivo = this.createProduto("Bolsa Sangue O+",
                "Bolsa de sangue O+ de 500ml",
                Tipo.SANGUE,
                Sangue.O,
                FatorRH.POSITIVO);
        Produto sangueONegativo = this.createProduto("Bolsa Sangue O-",
                "Bolsa de sangue O- de 500ml",
                Tipo.SANGUE,
                Sangue.O,
                FatorRH.NEGATIVO);
        sangueOPositivo = this.produtoService.save(sangueOPositivo);
        sangueONegativo = this.produtoService.save(sangueONegativo);
        this.hospitalService.addProdutoNoEstoque(hospital.getId(), sangueOPositivo.getId(), qtdOp);
        this.hospitalService.addProdutoNoEstoque(hospital.getId(), sangueONegativo.getId(), qtdOn);
    }

    private void loadLeitos(Hospital hospital) {
        for (int i = 0; i < 5; i++) {
            this.leitoService.save(hospital.getId(), TipoLeito.CLINICO);
        }
        for (int i = 0; i < 2; i++) {
            this.leitoService.save(hospital.getId(), TipoLeito.CIRURGICO);
        }
        for (int i = 0; i < 2; i++) {
            this.leitoService.save(hospital.getId(), TipoLeito.OBSTETRICO);
        }
        for (int i = 0; i < 2; i++) {
            this.leitoService.save(hospital.getId(), TipoLeito.PEDIATRICO);
        }
    }

    private void loadHospitais() {
        Hospital santaCasa = this.createHospital("Santa Casa de Misericórdia",
                "Av. Independência, 75 - Independência, Porto Alegre - RS, 90035-072",
                new BigDecimal("-30.0354492"),
                new BigDecimal("-51.2352925"));
        Hospital puc = this.createHospital("Hospital São Lucas da PUCRS",
                "Av. Ipiranga, 6690 - Jardim Botânico, Porto Alegre - RS, 90619-900",
                new BigDecimal("-30.0551459"),
                new BigDecimal("-51.1771135"));
        Hospital divina = this.createHospital("Hospital Divina Providência",
                "R. da Gruta, 145 - Cascata, Porto Alegre - RS, 91712-160",
                new BigDecimal("-30.0847365"),
                new BigDecimal("-51.1904995"));

        santaCasa = this.hospitalService.save(santaCasa);
        this.loadProdutos(18, 12, santaCasa);
        this.loadLeitos(santaCasa);

        puc = this.hospitalService.save(puc);
        this.loadProdutos(8, 2, puc);
        this.loadLeitos(puc);

        divina = this.hospitalService.save(divina);
        this.loadProdutos(28, 25, divina);
        this.loadLeitos(divina);
    }


    private Hospital createHospital(String nome, String endereco, BigDecimal lat, BigDecimal lon) {
        Hospital h1 = new Hospital();
        h1.setNome(nome);
        h1.setEndereco(endereco);
        h1.setLatitude(lat);
        h1.setLongitude(lon);
        return h1;
    }

    private Produto createProduto(String nome, String descricao, Tipo tipo, Sangue sangue, FatorRH fator) {
        Produto produto = new Produto(nome, descricao);
        produto.setTipo(tipo);
        produto.setSangue(sangue);
        produto.setFatorrh(fator);
        return produto;
    }

    private Paciente createPaciente() {
        Paciente paciente = new Paciente();
        paciente.setNomeCompleto("Maria da Silva");
        paciente.setCpf("371.310.164-70");
        paciente.setSexo(Sexo.F);
        paciente.setDataNascimento(LocalDate.of(1992, 10, 20));
        paciente.setEndereco("R. Gen. Lima e Silva, 606 - Cidade Baixa - Porto Alegre - RS, 90050-102");
        paciente.setLatitude(new BigDecimal("-30.038260"));
        paciente.setLongitude(new BigDecimal(-51.221581));
        return paciente;
    }
}
