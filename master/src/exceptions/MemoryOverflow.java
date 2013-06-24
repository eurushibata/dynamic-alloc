package exceptions;

/**
 * Exceção gerada para informar que uma solicitação excede a capacidade de memória disponível.
 * @author Yuji
 */
public class MemoryOverflow extends Exception {
    /**
     * Exceção gerada para informar que uma solicitação excede a capacidade de memória disponível.
     */
    public MemoryOverflow() {
        super("Solicitação excede a capacidade de memória disponível");
    }
}