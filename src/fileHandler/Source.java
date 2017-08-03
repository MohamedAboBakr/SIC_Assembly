package fileHandler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import hexaOperations.HexaDecOperations;

public class Source {
      private List<Instruction> file ;
      private File sourceFile;
      private Scanner in;
      private Optab opcodes ;
      private String locCounter ;
      private String progname ;
      private String startloc , endloc , proglen;
      private Symtab symtab ;
      private HexaDecOperations hxdcop ;
      private List<String> finalObjCode;
      private boolean eof ;
      private boolean stof ;
      
      public Source(Optab opcodes){
    	     locCounter = "0000" ; 
    	     proglen = "0000";
    	     progname = null ;
    	     eof = false;
    	     stof = false ;
    	     file = new ArrayList<>();
    	     symtab = new Symtab();
    	     hxdcop = new HexaDecOperations();
    	     finalObjCode = new ArrayList<>();
    	     sourceFile = new File("5.txt");
    	     
    	     try {
				   in = new Scanner(sourceFile);
			     } catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			    }
    	     
    	    this.opcodes = opcodes;
      }
      
      public void read(){
    	      while(in.hasNextLine()){
    	    	     String line = in.nextLine();
    	    	     if(line.length() == 0) continue ;
    	    	     line = line.replaceAll("\t","    ");
    	    	     if(line.charAt(0)=='.'){
    	    	    	   Instruction ins = new Instruction(null , null , null , null , null , null);
    	    	    	   ins.setComment(line);
    	    	    	   ins.setAllcomment(true);
    	    	    	   file.add(ins);
    	    	    	   continue ;
    	    	     }
    	    	     String[] words = new String[4];
    	    	     words[0] = substr(line , 0 , 7); words[0] = lowercase(words[0]);
    	    	     words[1] = substr(line , 9 , 14);words[1] = lowercase(words[1]);
    	    	     words[2] = substr(line , 17 , 34);
    	    	     words[0] = words[0].trim();
    	    	     words[1] = words[1].trim();
    	    	     words[2] = words[2].trim();
    	    	     if(!words[1].equals("byte")){
    	    	    	  words[2] = lowercase(words[2]);
    	    	     }
    	    	     words[3] = substr(line , 35 , 65);
    	    	     words[3] = words[3].trim();
    	    	          
    	    	     boolean ch1 = checkLine(words);
    	    	     if(!ch1){
    	    	    	 // System.out.println(ch1 + "   " + progname + "  " + eof);
    	    	    	  throw new RuntimeException("Error in file");
    	    	     }
    	      }
    	      
    	      if(stof==false || eof==false){
    	    	  throw new RuntimeException("Error in file");
    	      }
    	      
    	      fill_symtab();
    	      checkAllOperands();
    	      setobjcodes();
      }
      
      
      public void run(){
    	  printinsts();
	      collectobjCodes();
      }
      private void collectobjCodes(){
    	      proglen = symtab.SymtabLen() ;
    	      String head = "" ;
    	      head += "H^"+progname ;
    	      for(int i=0;i<6-progname.length();i++){
    	    	   head +=" ";
    	      } head +="^" ;
    	      head += fitstr(startloc,6)+"^";
    	      head += fitstr(proglen,6);
    	      finalObjCode.add(head);
    	      
    	      String loc = null ;
    	      int recordlen = 0 ;
    	      
    	      List<String> record = new ArrayList<>();
    	      for(int i=0; i<file.size() ; i++){
    	    	      Instruction ins = file.get(i);
    	    	      if(ins.getAllcomment()) continue;
    	    	      if(loc == null) loc = ins.getloc();
    	    	      if(ins.getoperation().equals("resw") || ins.getoperation().equals("resb")){
    	    	    	  if(record.size() != 0){
    	    	    		  String rec = getRecord(record , recordlen/2 , loc);
    	    	    		  finalObjCode.add(rec);
    	    	    	  }
    	    	    	  loc = null ;
    	    	    	  recordlen = 0 ;
    	    	    	  record.clear();
    	    	    	  continue ;
    	    	      }
    	    	      String objc = ins.getobjcode();
    	    	      if(ins.getoperation().equals("end")){
    	    	    	  String rec = getRecord(record , recordlen/2 , loc);
	    	    		  finalObjCode.add(rec);
    	    	      }
    	    	      if(objc == null) continue;
    	    	      if(objc.length() + recordlen > 60){
    	    	    	  String rec = getRecord(record , recordlen/2 , loc);
	    	    		  finalObjCode.add(rec);
	    	    		  record.clear();
	    	    		  loc = ins.getloc();
	    	    		  recordlen =0;
	    	    		 
    	    	      }
    	    	      recordlen += objc.length() ;
    	    	      record.add(objc);
    	    	      
    	      }
    	      
    	     String tale = "E^"+fitstr(startloc,6);
    	     finalObjCode.add(tale);
    	     
    	     
    	   String fileName = "ObjectCode.txt";
      	   try{
      		   FileWriter fileWriter = new FileWriter(fileName);
      	       BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
      	         for(String s : finalObjCode){
      	    	    bufferedWriter.write(uppercase(s));
      	    	    bufferedWriter.newLine();
      	         }
      	       bufferedWriter.close();
      	   }catch(IOException ex) {
                 System.out.println("Error writing to file '" + fileName + "'");
            }
      }
      
      private String getRecord(List<String> objcodes , int len , String stloc){
    	     String res = "T^" ;
    	     res += fitstr(stloc,6)+"^";
    	     String hxln = hxdcop.toHex(String.valueOf(len)) ;
    	     res += fitstr(hxln,2);
    	     for(String s : objcodes){
    	    	   res += "^"+s ;
    	     }
    	     return res;
      }
      
      
      private String fitstr(String s , int num){
    	    String res = "";
    	    for(int i=0 ; i< num-s.length(); i++){
    	    	  res += "0";
    	    }
    	    res += s ;
    	    return res ;
      }
      
      
      private void setobjcodes(){
    	    for(Instruction inst : file){
    	    	if(inst.getAllcomment()) continue;
    	    	if(inst.operationIsDirective()){
    	    		 inst.calObjcode(null);
    	    	}else{
    	    		String op = inst.getoperand().split(",")[0] ;
    	    		String oploc = symtab.search(op, 1) ;
    	    		 inst.calObjcode(oploc);
    	    	}
    	    }
      }
      
      private void printinsts(){
    	   String fileName = "SourceFile.txt";
    	   try{
    		   FileWriter fileWriter = new FileWriter(fileName);
    	       BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
    	         for(Instruction ints : file){
    		           ints.print(bufferedWriter);
    	         }
    	       bufferedWriter.close();
    	   }catch(IOException ex) {
               System.out.println("Error writing to file '"+ fileName + "'");
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
      
      private String lowercase(String s){
    	   if(Isemptystr(s)) return s ;
    	   String res = "" ;
    	   for(int i=0; i<s.length() ; i++){
    		   char c = s.charAt(i);
    		   if(c >= 'A' && c <= 'Z') c = Character.toLowerCase(c);
    		   res += c ;
    	   }
    	   return res ;
      }
      
      private String substr(String s , int i1 , int i2){
    	     String res = "";
    	     for(int i = i1 ; i<=i2&&i<s.length() ; i++){
    	    	  res += s.charAt(i);
    	     }
    	     return res ;
      }
      
      private boolean Isemptystr(String s){
    	    for(int i=0; i<s.length() ; i++){
    	    	 if(s.charAt(i) != ' ') return false ;
    	    }
    	    return true ;
      }
      public Symtab getSymtab(){
    	   return symtab ;
      }
      
      private void checkAllOperands(){
    	   for(Instruction inst : file){
    		     if(inst.getAllcomment()) continue;
    		     if(inst.operationIsDirective()) continue;
    		     String st = inst.getoperand();
    		     if(st.length() == 0) continue;
    		     String stt = st.split(",")[0];
    		     if(!symtab.exist(stt)) throw new RuntimeException( stt + " doesn't exist ");
    	   }
      }
      
      
      private void fill_symtab(){
    	  String temp = "0000";
    	  List<String> locs  = new ArrayList<>();
    	  for(Instruction inst : file){
    		    if(inst.getAllcomment()) continue;
    		    if(inst.getlabel().length() != 0 ) locs.add(inst.getloc());
    	  }
    	  locs.add(endloc);
    	  int i=1 ;
    	  for(Instruction inst : file){
    		  if(inst.getAllcomment()) continue;
    		  if(inst.getlabel().length() != 0 ){
    			    String diff = hxdcop.subHex(locs.get(i) , inst.getloc());
    			    i++ ;
    			    boolean chk = symtab.insert(inst.getlabel(), inst.getloc(), inst.getoperation(), diff , null);
  			        if(!chk){
  			    	  throw new RuntimeException(" Error this label already exists ");
  			      }
    		  }
    	  }
      }
      
      
      private boolean checkLine(String[] words){
    	   String flabel=words[0],ftype=words[1],foperand=words[2],currloc=locCounter;
    	   boolean result = true ;
    	   
    	   if(!checktype(ftype)){
    		   System.out.println("Error 1");
    		   return false ;
    	   }
    	   
    	   if(ftype.equals("start")){
    		       if(stof){
    		    	   System.out.println("Error 2");
    		    	   return false;
    		       }
    		       if(Isemptystr(words[0]) || (!hxdcop.IsHex(words[2]))){
    		    	   System.out.println("Error 3");
    		    	    return false ;
    		       }
    		       stof = true ;
    	   }
    	   
    	   if(ftype.equals("end")){
    		     if(eof){
    		    	 System.out.println("Error 4");
    		    	 return false;
    		     }
    		     eof = true ;
    	   }
    		   
    	   
    	   if(ftype.equals("word") || ftype.equals("resw") || ftype.equals("resb")){
    		      boolean chk = isInt(foperand);
    		      if(!chk){
    		    	  System.out.println("Error 5");
    		    	  return false ;
    		      }
    	   }
    	   
    	   if(ftype.equals("byte")) {
    		      if(foperand.charAt(0)=='X' || foperand.charAt(0)=='C' || foperand.charAt(0)=='x' || foperand.charAt(0)=='c'){}
    		      else {
    		    	  System.out.println("Error 6");
    		    	  return false ;
    		      }
    	   }
    	   
    	   if(ftype.equals("rsub")){
    		    if(Isemptystr(words[0]) && Isemptystr(words[2])) {}
    		    else {
    		    	System.out.println("Error 7");
    		    	return false ;
    		    }
    	   }
    	   
    	   if(ftype.equals("start")){
    		   progname = flabel ;
    		   startloc = foperand ;
    		   locCounter = startloc ;
    		   currloc = locCounter ;
    	   }else if(ftype.equals("word")){
    		   locCounter = hxdcop.addHex(locCounter, "3") ;
    	   }else if(ftype.equals("byte")){
    		   char fc = foperand.charAt(0);
    		   if(fc == 'X' || fc == 'x') locCounter = hxdcop.addHex(locCounter, "1") ;
    		   else {
    			   int len = foperand.length()-3 ;
    			   locCounter = hxdcop.addHex(locCounter, hxdcop.toHex(String.valueOf(len))) ;
    		   }
    	   }else if(ftype.equals("resw")){
    		     int len = 3*Integer.parseInt(foperand);
    		     locCounter = hxdcop.addHex(locCounter, hxdcop.toHex(String.valueOf(len))) ;
    	   }else if(ftype.equals("resb")){
  		         int len = Integer.parseInt(foperand);
  		         locCounter = hxdcop.addHex(locCounter, hxdcop.toHex(String.valueOf(len))) ;
  	      }else if(ftype.equals("end")){ eof = true ;}
  	       else  locCounter = hxdcop.addHex(locCounter, "3") ;
    	   
    	   endloc = currloc ;
    	   Instruction ins = new Instruction(currloc , flabel , ftype , foperand , null , opcodes);
    	   ins.setComment(words[3]);
    	   String[] registerchk = foperand.split(",");
    	   if(registerchk.length > 1) ins.setregister(true);
    	   file.add(ins);
    	   return true ;
    	   
    	  /* if(words.length == 1){
    		    if(words[0].equals("rsub")){
    		    	 flabel = null ;
    		    	 ftype = words[0] ;
    		    	 foperand = null ;
    		    }else result = false;
    	   }
    	   else if(words.length == 2) {
    		   flabel = null ;
    		   ftype = words[0];
    		   foperand = words[1];
    		   boolean c  = checkDirective(ftype , foperand);
    		   if(!c) result = false;
    	   }else {
    		   flabel = words[0] ;
    		   ftype = words[1];
    		   foperand = words[2];
    	   }
    	   
    	   if(result == false) return false ;  */
    	   
      }
      
      private boolean checklabel(String label){
    	   if(label == null) return true ;
    	   return !symtab.exist(label);
      }
      
      private boolean checktype(String type){
    	   return opcodes.IsExist(type);
      }
      
      private boolean checkDirective(String ftype , String foperand){
    	    if(ftype.equals("word")){
    	    	return isInt(foperand);
    	    }else if(ftype.equals("byte")){
    	    	return isInt(foperand);
    	    }else if(ftype.equals("resw")){
    	    	return isInt(foperand);
    	    }else if(ftype.equals("resb")){
    	    	return isInt(foperand);
    	    }else if(ftype.equals("end")){
    	    	return true ;
    	    }else return false;
      }
      
      private boolean isInt(String st){
    	   try{
    		   int num = Integer.parseInt(st);
    		   return true ;
    	   }catch(NumberFormatException e){
    		   return false ;
    	   }
      }
}
