package com.clockworks.android.tablet.bigture.utils;

public class StringUtils {
	 private static final char[] firstSounds = {  
	        'ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ', 'ㄹ', 'ㅁ',  
	        'ㅂ', 'ㅃ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅉ',  
	        'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'  
	    };
	
	 public static boolean isHangul(String str){
		 if( str == null )  
	            return false;  
	         
	        int len = str.length();  
	        if( len == 0 )  
	            return false;  
	         
	        for( int i = 0; i < len; i++ ) {  
	            if( !isHangul(str.charAt(i)) )  
	                return false;  
	        }  
	        return true;  	 
	 }
	 
	 public static boolean checkHangul(char c){
		 for(char ch : firstSounds){
			  if(c == ch)
				  return true;
		  }
		 return false;
	 }
	 
	 public static boolean isHangul(char c){
		 
		  if( c < 0xAC00 || c > 0xD7A3 )  
	            return false;  
		
	      return true;  
	 }
	 
	 public static char getFirstElement(char c) {  
	        if( ! isHangul(c) )  
	            return c;  
	        return firstSounds[(c - 0xAC00) / (21 * 28)];  
	 }
	 
	 public static char getFirstElement(String str) {  
	        if( str == null )  
	            return '\u0000';  
	         
	        int len = str.length();  
	        if( len == 0 )  
	            return '\u0000';  
	         
	        return getFirstElement(str.charAt(0));  
	    }  
}
