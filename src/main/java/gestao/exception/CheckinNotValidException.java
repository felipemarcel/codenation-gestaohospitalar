package gestao.exception;

public class CheckinNotValidException extends RuntimeException {
    public CheckinNotValidException() {
        super("O paciente já possui um checkin aberto.");
    }
}
