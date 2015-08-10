package com.sweety.testcases;

import java.util.Hashtable;

import org.testng.SkipException;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;



import com.sweety.util.Keywords;
import com.sweety.util.TestUtil;





public class AddNewlevel {
	
	
	@Test(dataProvider = "getverifyAddNewlevel")
	public void verifyAddNewlevel(Hashtable<String, String> data) {

		if (!TestUtil.isTestCaseExecutable("verifyAddNewlevel", Keywords.xls))
			throw new SkipException("Skipping the test as Runmode is NO");
		if (!data.get("RunMode").equals("Y"))
			throw new SkipException(
					"Skipping the test as data set Runmode is NO");

		Keywords k = Keywords.getKeywordsInstance();
		
		
		k.log("*******Started verifyAddNewlevel********");
		
		k.executeKeywords("verifyAddNewlevel", data);
		
			
		k.log("******verifyAddNewlevel Finished******");

	}
	
	
	@DataProvider
	public Object[][] getverifyAddNewlevel() {
		return TestUtil.getData("verifyAddNewlevel", Keywords.xls);
	}
	
	
	
	

}
