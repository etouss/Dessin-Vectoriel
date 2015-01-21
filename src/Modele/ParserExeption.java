package Modele;

/**
 * Created by renaud on 23/11/14.
 */
public class ParserExeption extends Exception {
    String message;
    public ParserExeption(String s){
        super();
        this.message = s;
    }

    public String printMsg(){
        return message;
    }


}
