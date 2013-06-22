package exceptions;

public class InvalidAddress extends Exception {
    public InvalidAddress() {
        super("Referência de endereço de memória inválida");
    }
}