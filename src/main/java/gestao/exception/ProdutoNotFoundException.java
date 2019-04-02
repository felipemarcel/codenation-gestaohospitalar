package gestao.exception;

public class ProdutoNotFoundException extends RuntimeException {
	
	public ProdutoNotFoundException(Long id) {
		super(String.format("Não foi encotrado nenhum produto com este id: %d", id));
	}

}
