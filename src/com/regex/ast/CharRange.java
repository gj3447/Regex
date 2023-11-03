package com.regex.ast;
import java.lang.String;
public class CharRange {
	private char min;
	private char max;
	public CharRange(char char1, char char2)
	{
		if(char1>char2)
		{
			min = char2;
			max = char1;
		}
		else
		{
			min = char1;
			max = char2;
		}
	}
	public String getString()
	{
		return min+"-"+max;
	}
	public boolean isRange(char c)
	{
		int cint = (int)c;
		if((int)min <=cint && cint<= (int)max)
			return true;
		else
			return false;
	}
}
