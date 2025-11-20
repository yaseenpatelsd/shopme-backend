package shop.me.back.end.Exeption;

public class UserNotFoundExeption extends RuntimeException{
    public UserNotFoundExeption(String message){
        super(message);
    }
}
