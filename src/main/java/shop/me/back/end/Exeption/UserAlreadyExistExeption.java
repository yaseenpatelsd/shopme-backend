package shop.me.back.end.Exeption;

public class UserAlreadyExistExeption extends RuntimeException{
    public UserAlreadyExistExeption(String message){
        super(message);
    }
}
