package ma.enset.ebankingapp.exceptions;

public class CustomerNotFoundException extends Exception  {
    //RuntimeException c'est pas une exception survieller
    //exception est une exception surveiller
    public CustomerNotFoundException(String messageException) {
    super(messageException);
    }
}
