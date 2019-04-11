package gestao.exception;

public class EstoqueNotEnoughException extends RuntimeException {
    public EstoqueNotEnoughException(int qtd) {
        super(String.format("Quantidade de produtos insuficientes. Há apenas %d produtos no estoque", qtd));
    }

    public EstoqueNotEnoughException(String msg) {
        super(msg);
    }
}
