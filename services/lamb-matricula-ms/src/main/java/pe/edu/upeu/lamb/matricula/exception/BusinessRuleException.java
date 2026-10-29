package pe.edu.upeu.lamb.matricula.exception;

public class BusinessRuleException extends RuntimeException {
    public BusinessRuleException(String mensaje) {
        super(mensaje);
    }
}
