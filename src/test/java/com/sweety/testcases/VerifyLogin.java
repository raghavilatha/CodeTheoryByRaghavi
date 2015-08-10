package com.sweety.testcases;

import java.util.Hashtable;

import org.testng.SkipException;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;



import com.sweety.util.Keywords;
import com.sweety.util.TestUtil;





public class VerifyLogin {
	
	
	@Test(dataProvider = "getverifyLogin")
	public void verifyLogin(Hashtable<String, String> data) {

		if (!TestUtil.isTestCaseExecutable("verifyLoginprocess", Keywords.xls))
			throw new SkipException("Skipping the test as Runmode is NO");
		if (!data.get("RunMode").equals("Y"))
			throw new SkipException(
					"Skipping the test as data set Runmode is NO");

		Keywords k = Keywords.getKeywordsInstance();
		
		
		k.log("*******Started verifyLoginprocess********");
		
		k.executeKeywords("verifyLoginprocess", data);
		
			
		k.log("******verifyLoginprocess Finished******");

	}
	
	
	@DataProvider
	public Object[][] getverifyLogin() {
		return TestUtil.getData("verifyLoginprocess", Keywords.xls);
	}
	
	
	
	

}
