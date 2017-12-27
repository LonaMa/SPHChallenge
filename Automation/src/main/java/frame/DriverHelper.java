package frame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class DriverHelper {
	private Logger logger = LogManager.getLogger(this.getClass());
	transient int reTryTime;
	transient int retryMaxTime;
	transient String taskId;
	transient int times;
	transient String line;
	transient Boolean running;
	transient Process pro;
	transient BufferedReader input;
	transient String[] lineItem;
	transient String item;
	
	public DriverHelper() {
		times = 0;
		retryMaxTime = 5;	
		reTryTime = 0;

	}
   
	
    /**
     * Kill app by name
     * @param	processName i.e. "notepad.exe"
     * @author	Ma Nan
     * @version	1.0
     */ 
	public void killProcess(String processName)
	{
		try{
			do{
				String cmd = "taskkill /F /IM " + processName;
				Runtime.getRuntime().exec(cmd);
				times ++;
				sleep(500L);
			}while(times < retryMaxTime && findProcessState(processName));

		} catch (IOException ex) {
			if (reTryTime < retryMaxTime) {
                logger.warn("Kill process get exception, retryTime: " + reTryTime);
                reTryTime++;
                sleep(500L);
                killProcess(processName);
	          } else {
	                reTryTime=0;
	                logger.error("Exception when kill process:"+ ex.getMessage());
	          }
			logger.trace(ex);
		}
		reTryTime=0;
	}
	
    /**
     * find process state
     * @param	processName i.e. "notepad.exe"
     * @return	true: running, false: no running
     * @author	Ma Nan
     * @version	1.0
     */ 
	public Boolean findProcessState(String processName)
	{
		running = false; 
		try {	      
		      pro = Runtime.getRuntime().exec("tasklist.exe /fo csv /nh");
		      input = new BufferedReader
		    		  (new InputStreamReader(pro.getInputStream()));
		      while( (line = input.readLine()) != null  ) {
		          if( line.contains(processName)) {
		        	  running = true;
		        	  break;
		          }
		      }
		      input.close();
		    }
		    catch (IOException ex) {
		    	logger.warn("Get process status exception:" + ex.getMessage(), ex);
		    }
		logger.info("Get process state, running is:" + running);
		return running;
	}
	
	private void sleep(Long sleeptime)
	{
		try {
			Thread.sleep(sleeptime);	
			logger.debug("Sleep: (" + sleeptime +")ms"); 
		} catch (InterruptedException e) {
			logger.error("Sleep <Thread.sleep> method error.", e);
		}
	}
	
    
}
