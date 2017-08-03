package interfaces;

public interface HexaOperations {
    public String toHex(String s);
    public String toDec(String s);
    public String addHex(String s1 , String s2);
    public String addDec(String s1 , String s2); 
    public String subHex(String s1 , String s2);
    public String HexOr(String s1 , String s2);
    public boolean IsHex(String s);
}
