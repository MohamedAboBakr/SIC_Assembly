package fileHandler;

import java.util.HashMap;
import java.util.HashSet;

import hexaOperations.HexaDecOperations;

class node{
	
	  private String loc ;
	  private String type ;
	  private String length ;
	  private String flag ;
	  
	  public node(String loc ,String type , String length , String flag){
		   this.loc = loc ;
		   this.type = type ;
		   this.length = length ;
		   this.flag = flag ;
	  }
	  
	  public void setloc(String loc){
		   this.loc = loc ;
	  }
	  public String getloc(){
		  return this.loc ;
	  }
	  
	  public void setType(String type){
		   this.type = type ;
	  }
	  public String getType(){
		  return this.type ;
	  }
	  
	  public void setlength(String length){
		   this.length = length ;
	  }
	  public String getlength(){
		  return this.length ;
	  }
	  
	  public void setflag(String flag){
		   this.flag = flag ;
	  }
	  public String getflag(){
		  return this.flag ;
	  }
	
	  public String search(int index){
		    if(index == 1) return loc ;
		    else if(index == 2) return type ;
		    else if(index == 3) return length ;
		    else  return flag ;
	  }
	  
}


public class Symtab {
        private HashMap<String , node> symtab ;
        private HexaDecOperations hxdcop ;
        
        public Symtab(){
        	 symtab = new HashMap<String , node>();
        	 hxdcop = new HexaDecOperations();
        }
        
        public boolean insert(String label , String loc , String type , String length , String flag){
        	   if(symtab.containsKey(label)) return false ;
        	   node n = new node(loc,type,length,flag);
        	   symtab.put(label, n) ;
        	   return true ;
        }
        
        public boolean delete(String label){
        	if(!symtab.containsKey(label)) return false ;
        	symtab.remove(label);
        	return true ;
        }
        
        public String search(String label , int index){
        	if(!symtab.containsKey(label)) return null ;
        	String prop = symtab.get(label).search(index);
        	return prop ;
        }
       
        public boolean exist(String label){
        	 return symtab.containsKey(label);
        }
        
        public void print(){
        	  for(String s : symtab.keySet()){
        		    System.out.println(s + "  ---> " + symtab.get(s).getloc() + "  --> " + symtab.get(s).getType() + "  --> " + symtab.get(s).getlength());
        	  }
        }
        
        public String SymtabLen(){
        	String len = "0000";
        	for(String k : symtab.keySet()){
        		   len = hxdcop.addHex(len,symtab.get(k).getlength());
        	}
        	return len ;
        }
}
