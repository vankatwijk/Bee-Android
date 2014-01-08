package com.example.beeproject.utils;

import java.util.List;

public class StringUtils {
	public static String listToString(List list){
		String result = "";
		for(Object o: list){
			result += o + "\n";
		}
		return result;
	}

}
