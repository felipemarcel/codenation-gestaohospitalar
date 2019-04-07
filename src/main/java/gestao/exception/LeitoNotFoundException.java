package gestao.exception;

public class LeitoNotFoundException extends RuntimeException {
    public LeitoNotFoundException(Long id) {
        super(String.format("Não foi encotrado nenhum Leito com este id: %d", id));
    }
}
