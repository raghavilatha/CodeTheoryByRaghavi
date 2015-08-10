package com.sweety.testcases;

import java.util.Hashtable;

import org.testng.SkipException;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;



import com.sweety.util.Keywords;
import com.sweety.util.TestUtil;





public class VerifySubmenus {
	
	
	@Test(dataProvider = "getverifySubmenus")
	public void verifySubmenus(Hashtable<String, String> data) {

		if (!TestUtil.isTestCaseExecutable("verifySubmenus", Keywords.xls))
			throw new SkipException("Skipping the test as Runmode is NO");
		if (!data.get("RunMode").equals("Y"))
			throw new SkipException(
					"Skipping the test as data set Runmode is NO");

		Keywords k = Keywords.getKeywordsInstance();
		
		
		k.log("*******Started verifySubmenus********");
		
		k.executeKeywords("verifySubmenus", data);
		
			
		k.log("******verifySubmenus Finished******");

	}
	
	
	@DataProvider
	public Object[][] getverifySubmenus() {
		return TestUtil.getData("verifySubmenus", Keywords.xls);
	}
	
	
	
	

}
