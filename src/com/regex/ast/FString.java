package com.regex.ast;
import java.lang.String;
import java.lang.StringBuilder;
public class FString extends Node{
	private String string;
	public FString(String string_)
	{
		super();
		string = string_;
	}
	public void print()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("#");
		sb.append(number);
		sb.append("FSTRING|");
		sb.append(string);
		System.out.println(sb.toString());
	}

	@Override
	public String getString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("#");
		sb.append(number);
		sb.append("FSTRING|");

		sb.append(string);
		return sb.toString();
	}

	protected void req(RootNode root_, int index_)
	{
		String root_string = root_.getString();
		int endindex = index_ + string.length();
		if(endindex> root_string.length())
		{	
			if(root_.getPrintRun())
			{
				StringBuilder sb= new StringBuilder();
				sb.append(getString());
				sb.append(endindex);
				sb.append(":");
				sb.append(root_string.length());
				String print = getString()+":OVER";
				System.out.println(print);
			}
			node_up.res(root_,index_,number,eRes.BREAK);
			return;
		}
		
		String subString = root_string.substring(index_, endindex);
		if(string.equals(subString))
		{
			if(root_.getPrintRun())
			{
				String print = getString()+":"+subString+"-COMPLETE";
				System.out.println(print);
			}
			node_up.res(root_,endindex,number,eRes.COMPLETE);
			return;
		}
		else
		{
			if(root_.getPrintRun())
			{
				String print = getString()+":"+subString+"-BERAK!";
				System.out.println(print);
			}
			node_up.res(root_, index_, number, eRes.BREAK);
			return;
		}
	}
	protected void res(RootNode root_, int index_,int number_,eRes res_type_)
	{
		node_up.res(root_, index_, number, eRes.ERROR);
	}
	
}
