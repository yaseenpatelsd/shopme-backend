package shop.me.back.end.Exeption;

public class ResourceALreadyAvailableExeption extends RuntimeException{
    public ResourceALreadyAvailableExeption(String message){
        super(message);
    }
}
