package shop.me.back.end.Exeption;

public class ResourceNotAvailableExeption extends RuntimeException{
    public ResourceNotAvailableExeption(String message){
        super(message);
    }
}
