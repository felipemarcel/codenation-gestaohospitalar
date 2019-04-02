package gestao.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.time.LocalDateTime;

import static javax.persistence.CascadeType.ALL;

@Entity
@Table(name = "pacientes")
public class Paciente {

    @Id
    private Long id;

    @Column(name = "nome_completo")
    private String nomeCompleto;

    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    @Enumerated(EnumType.STRING)
    private Sexo sexo;

    // TODO Adicionar validação de cpf
    private String cpf;
    

    private String endereco;
    private Double latitude;
    private Double longitude;

    @OneToMany(mappedBy = "paciente", cascade = ALL, orphanRemoval = true)
    private List<Internacao> internacao;
    
    
    public Paciente(String Nome, String cpf, LocalDate dataNascimento, Sexo sexo){
    	this.nomeCompleto = Nome;
    	this.dataNascimento = dataNascimento;
    	this.sexo = sexo;
    	
    	if (ValidaCPF.isCPF(cpf) == true) {
    		this.cpf = cpf;
    	}
    	else {
    		// Adicionar excessão
    	}
    	    
    	
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public Sexo getSexo() {
        return sexo;
    }

    public void setSexo(Sexo sexo) {
        this.sexo = sexo;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
    
    public void updatePaciente(String Nome, String cpf, LocalDate dataNascimento, Sexo sexo){
    	this.nomeCompleto = Nome;
    	this.cpf = cpf;
    	this.dataNascimento = dataNascimento;
    	if (ValidaCPF.isCPF(cpf) == true) {
    		this.cpf = cpf;
    	}
    	else {
    		// Adicionar excessão
    	}   	
    }
    
    @Override
    public String toString() {
        return "Nome: " + this.nomeCompleto + "\n" +
        		"CPF: " + this.cpf +  "\n" +
        		"Data de Nascimento: " + this.dataNascimento +  "\n" +
        		"Sexo: " + sexo +  "\n" +
        		"Data de entrada: " + this.internacao.get(internacao.size() - 1).getDataEntrada();
        		
    }
    
    public void getInfo() {
    	System.out.println(this);	
    }
}
