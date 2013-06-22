package exceptions;

/**
 *
 * @author takeshi
 */
public class InvalidAddress extends Exception {
    /**
     * @param name
     */
    public InvalidAddress() {
        super("Referência de endereço de memória inválida");
    }
}