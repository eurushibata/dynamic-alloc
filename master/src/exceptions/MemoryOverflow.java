package exceptions;

/**
 * 
 * @author Yuji
 */
public class MemoryOverflow extends Exception {
    /**
     * Exceção gerada para informar que uma solicitação excede a capacidade de memória disponível.
     */
    public MemoryOverflow() {
        super("Não há memória disponível");
    }
}