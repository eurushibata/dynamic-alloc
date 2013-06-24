package exceptions;

/**
 * Exceção gerada para informar que uma solicitação está tentando acessar um endereço de memória inválido.
 * @author takeshi
 */
public class InvalidAddress extends Exception {
    /**
     * Exceção gerada para informar que uma solicitação está tentando acessar um endereço de memória inválido.
     */
    public InvalidAddress() {
        super("Referência de endereço de memória inválida");
    }
}