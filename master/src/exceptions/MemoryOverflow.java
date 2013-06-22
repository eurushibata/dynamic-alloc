package exceptions;

public class MemoryOverflow extends Exception {
    public MemoryOverflow() {
        super("Não há memória disponível");
    }
}