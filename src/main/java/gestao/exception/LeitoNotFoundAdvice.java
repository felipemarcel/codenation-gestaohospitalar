package gestao.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
public class LeitoNotFoundAdvice {

    /**
     * Produto sugerido não foi localizado
     * @param ex Parametro do erro
     * @return Mensagem de erro
     */
	@ResponseBody
    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(LeitoNotFoundException.class)
    String produtoNotFoundHandler(LeitoNotFoundException ex){
        return ex.getMessage();
    }

}
