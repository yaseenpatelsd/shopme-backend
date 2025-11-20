package shop.me.back.end.Exeption;

public class SomeThingIsWrongExeption extends RuntimeException{
    public SomeThingIsWrongExeption(String message){
        super(message);
    }
}
