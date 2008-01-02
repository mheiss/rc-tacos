package at.rc.tacos.web.utils;

import at.rc.tacos.web.web.UserSession;

public class Statistik {
	
	private static Statistik instance;
	
	public Statistik(){
		
	}
	
	public String getUserLogInfo(){
		return "---";
	}
	
    /**
     * Creates a new instance of this class or returns the 
     * previousely used instance.
     * @return a instance of the <code>Timetable</code> class.
     */
    public static synchronized Statistik getInstance() //step 1
    {
        //do we have a valid instance?
        if(instance == null)
            //create a new and return it
            return new Statistik();
        else    
            return instance;
    }
}
