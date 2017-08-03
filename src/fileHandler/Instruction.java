package fileHandler;

import java.io.BufferedWriter;
import java.io.IOException;

import hexaOperations.HexaDecOperations;

public class Instruction {
    private String loc ;
    private String label;
    private String operation;
    private String operand ;
    private String objcode;
    private String comment ;
    private Optab opcodes;
    private HexaDecOperations hxop;
    private boolean register ;
    private boolean allComment ;
    
    public Instruction(){
    	loc = null ;
    	label = null ;
    	operation = null ;
    	operand = null ;
    	objcode = null ;
    	opcodes = null;
    	comment = null ;
    	register = false ;
    	allComment = false ;
    	hxop = new HexaDecOperations();
    }
    
    public Instruction(String loc , String label , String operation , String operand ,String objcode,Optab opcodes){
    	 this.loc = loc ;
    	 this.label = label ;
    	 this.operation = operation ;
    	 this.operand = operand ;
    	 this.objcode = objcode;
    	 this.opcodes = opcodes;
    	   	comment = null ;
        	register = false ;
        	allComment = false ;
        	hxop = new HexaDecOperations();
    }
    
    public void setloc(String loc ){
    	   this.loc = loc ;
    }
    public String getloc(){
    	 return this.loc;
    }
    
    public void setlabel(String label ){
 	   this.label = label ;
    }
    public String getlabel(){
 	   return this.label;
    }
 
     public void setoperation(String operation ){
	   this.operation = operation ;
     }
     public String getoperation(){
	 return this.operation;
     }
     
     public void setoperand(String operand){
  	   this.operand = operand ;
     }
     public String getoperand(){
  	 return this.operand;
     }
     
     public void setobjcode(String objcode){
  	   this.objcode = objcode ;
     }
     public String getobjcode(){
  	 return this.objcode;
     }
      
     public void setComment(String comment){
    	    this.comment = comment ;
     }
     public String getComment(){
    	   return this.comment;
     }
     
     public void setregister(boolean rg){
    	  this.register = rg ;
     }
     public boolean getregister(){
    	 return register; 
     }
     
     public void setAllcomment(boolean cm){
    	  this.allComment = cm;
     }
     
     public boolean getAllcomment(){
    	  return this.allComment ;
     }
     
     
     public void calObjcode(String operandLoc){
    	      boolean chk = opcodes.isDirective(operation);
    	      if(!chk){
    	    	  objcode1(operandLoc);
    	      }else{
    	    	  objcode2();
    	      }
     }
     
     private void objcode1(String operandLoc){
    	     String res = opcodes.getCode(operation) ;
    	     if(operation.equals("rsub")){
    	    	 res += "0000";
    	     }
    	     else if(register){
    	    	 res += hxop.HexOr(operandLoc, "8000") ;
    	     }
    	     else res += operandLoc ;
    	     setobjcode(res);
     }
     
     
     private void objcode2(){
    	     String res = "";
    	     if(operation.equals("word")){
    	    	     String s = hxop.toHex(operand);
    	    	     res = "" ;
    	    	     for(int i=0;i< 6-s.length() ; i++){
    	    	    	 res += "0";
    	    	     }
    	    	     res += s ;
    	    	     setobjcode(res);
    	     }else if(operation.equals("byte")){
    	    	      if(operand.charAt(0) == 'X' || operand.charAt(0) == 'x'){
    	    	    	    int ln = operand.length();
    	    	    	    for(int i=2 ; i<ln-1; i++){
    	    	    	    	 res += operand.charAt(i);
    	    	    	    }
    	    	    	  
    	    	      }else{
    	    	    	  int ln = operand.length();
  	    	    	      for(int i=2 ; i<ln-1; i++){
  	    	    	    	 res += getAscii(operand.charAt(i));
  	    	    	      }
    	    	      }
    	    	      setobjcode(res);
    	     }else setobjcode(null);
     }
     
     
     private String getAscii(char c){
    	   int asc = (int)c ;
    	   String s = String.valueOf(asc);
    	   return hxop.toHex(s);
     }
     public boolean operationIsDirective(){
    	  return opcodes.isDirective(this.operation);
     }
     
     public void print(BufferedWriter bufferedWriter) throws IOException{
    	 if(getAllcomment()){
    		 bufferedWriter.write(uppercase(comment));
    		 bufferedWriter.newLine();
    		 return ;
    	 }
    	 bufferedWriter.write(loc+"    ");
    	 printLabel(uppercase(label),bufferedWriter);
    	 bufferedWriter.write(" ");
    	 printOp(uppercase(operation),bufferedWriter);
    	 bufferedWriter.write("  ");
    	 printOperand(uppercase(operand),bufferedWriter);
    	 bufferedWriter.write(uppercase(comment)+ "     ");
    	 if(objcode != null )bufferedWriter.write(uppercase(objcode));
    	 bufferedWriter.newLine();
     }
     
     private void printLabel(String label , BufferedWriter bufferedWriter) throws IOException{
    	   int len ;
    	   if(label==null) len =0 ;
    	   else {
    		   bufferedWriter.write(label);
    		   len = label.length();
    	   }
    	  for(int i=8-len ; i>0 ; i--){
    		  bufferedWriter.write(" ");
      	  }
     }
     
     private void printOp(String op, BufferedWriter bufferedWriter) throws IOException{
    	 int len ;
  	   if(op==null) len =0 ;
  	   else {
  		 bufferedWriter.write(op);
  		   len = op.length();
  	   }
  	   for(int i=6-len ; i>0 ; i--){
  		 bufferedWriter.write(" ");
	    }  
     }
     
     private void printOperand(String operand, BufferedWriter bufferedWriter) throws IOException{
    	 int len ;
    	   if(operand==null) len =0 ;
    	   else {
    		   bufferedWriter.write(operand);
    		   len = operand.length();
    	   }
    	   for(int i=18-len ; i>0 ; i--){
    		   bufferedWriter.write(" ");
  	    }
     }
     
     private String uppercase(String s){
  	   if(Isemptystr(s)) return s ;
  	   String res = "" ;
  	   for(int i=0; i<s.length() ; i++){
  		   char c = s.charAt(i);
  		   if(c >= 'a' && c <= 'z') c = Character.toUpperCase(c);
  		   res += c ;
  	   }
  	   return res ;
    }
     
     private boolean Isemptystr(String s){
 	    for(int i=0; i<s.length() ; i++){
 	    	 if(s.charAt(i) != ' ') return false ;
 	    }
 	    return true ;
   }
}
