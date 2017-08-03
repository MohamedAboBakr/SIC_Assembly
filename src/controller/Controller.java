package controller;

import fileHandler.Optab;
import fileHandler.Source;

public class Controller {
    
	private static final Controller instance = new Controller();
	private static Optab optabcodes ;
	private static Source src ;
	private Controller(){}
	public static Controller getInstance(){
		optabcodes = new Optab();
		src = new Source(optabcodes);
		return instance;
	}
	
	public void work(){
		  src.read();
	}
	
	public void run(){
		  src.run();
	}
	
}
