package hexaOperations;

import interfaces.HexaOperations;

public class HexaDecOperations implements HexaOperations{

	@Override
	public String toHex(String s) {
		// TODO Auto-generated method stub
		return Integer.toHexString((Integer.parseInt(s)));
	}

	@Override
	public String toDec(String s) {
		// TODO Auto-generated method stub
		return String.valueOf(Integer.parseInt(s,16));
	}

	@Override
	public String addHex(String s1, String s2) {
		//System.out.println(s1 + " ****** " + s2);
		// TODO Auto-generated method stub
		String ss1 = toDec(s1);
		String ss2 = toDec(s2);
		int sss = Integer.parseInt(ss1) + Integer.parseInt(ss2);
		String ssss = String.valueOf(sss) ;
		String ans = toHex(ssss);
		//System.out.println(ss1 + "  " + ss2  + "  " + sss + "   " + ssss + "   " + ans);
		return ans ;
	}

	@Override
	public String addDec(String s1, String s2) {
		// TODO Auto-generated method stub
		return String.valueOf(Integer.parseInt(toDec(s1)+Integer.parseInt(toDec(s2))));
	}

	@Override
	public String subHex(String s1, String s2) {
		// TODO Auto-generated method stub
		String s1dc = toDec(s1);
		String s2dc = toDec(s2);
		int diff = Integer.parseInt(s1dc)-Integer.parseInt(s2dc);
		String diffst = String.valueOf(diff);
		return toHex(diffst);
	}

	@Override
	public String HexOr(String s1, String s2) {
		// TODO Auto-generated method stub
		String s1dec = toDec(s1);
		String s2dec = toDec(s2);
		int s1int = Integer.valueOf(s1dec);
		int s2int = Integer.valueOf(s2dec);
		int res  = s1int|s2int ;
		return toHex(String.valueOf(res));
	}

	@Override
	public boolean IsHex(String s) {
		// TODO Auto-generated method stub
		 for(int i=0 ; i<s.length() ; i++){
			   char c = s.charAt(i);
			   if(c >='0' && c<='9') continue; 
			   if(c >='a' && c<= 'f') continue ;
			   if(c >='A' && c<= 'F') continue ;
			   else return false ;
		 }
		 return true ;
	}
	

}
