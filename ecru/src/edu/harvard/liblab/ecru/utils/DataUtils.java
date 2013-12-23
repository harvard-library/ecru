package edu.harvard.liblab.ecru.utils;

import java.util.List;


/**********************************************************************
 * [INSERT COPYRIGHT INFO]
 **********************************************************************/
/**
 * @author Bobbi Fox
 *
 *   Project:  unleashed
 *   
 *   This class contains utilities for manipulating data
 *  
 */
public class DataUtils {

	
	/**
	 * @param input  - a String or null
	 * @return  -- a trimmed String, with normalized white space
	 */
	public static String trimStr(String input) {
		String retVal = input;
		if (retVal != null) {
			retVal = retVal.trim().replaceAll("\\s+", " ");;
		}
		return retVal;
	}
	
	public static List<String> trimList(List<String> list) {
		if (list != null) {
			for (int i = 0; i < list.size(); i++){
				String val = list.get(i);
				list.set(i, trimStr(val));
			}
		}
		return list;
	}
}
