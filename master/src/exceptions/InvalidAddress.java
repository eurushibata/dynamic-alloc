package exceptions;

public class InvalidAddress extends Exception {
    public InvalidAddress() {
        super();
    }

    public InvalidAddress(String msg) {
        super(msg);
    }
}