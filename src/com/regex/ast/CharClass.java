package com.regex.ast;
import java.util.ArrayList;
import java.lang.String;
import java.lang.StringBuilder;
public class CharClass extends Node{

	private ArrayList<Character> char_list;
	private ArrayList<CharRange> char_range_list;
	private boolean not;
	public CharClass(ArrayList<Character> char_list_,
			ArrayList<CharRange> char_range_list_,
			boolean not_)
	{
		char_list = char_list_;
		char_range_list = char_range_list_;
		not = not_;
	}
	public void print()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("#");
		sb.append(number);
		sb.append("CHARCLASS|char:");
		for(char n : char_list)
		{
			sb.append(n);
		}

		sb.append("|charrange:");
		for(CharRange cr : char_range_list)
		{
			sb.append(cr.getString());
		}
		System.out.println(sb.toString());
	}

	@Override
	public String getString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("#");
		sb.append(number);
		sb.append("CHARCLASS");
		return sb.toString();
	}
	protected void req(RootNode root_, int index_)
	{
		if(index_>=root_.getString().length())
		{
			if(root_.getPrintRun())
			{
				String print = getString()+"OVER";
				System.out.println(print);
			}
			return;
		}

		char target = root_.getString().charAt(index_);
		int nextindex = index_+1;
		for(Character c : char_list)
		{
			if(c.equals(target))
			{
				if(root_.getPrintRun())
				{
					String print = getString()+"-'" +Character.toString(target)+ "'COMPLETE";
					System.out.println(print);
				}
				node_up.res(root_, nextindex, number, eRes.COMPLETE);
				return;
			}
		}
		for(CharRange cr : char_range_list)
		{
			if(cr.isRange(target))
			{

				if(root_.getPrintRun())
				{
					String print = getString()+"-'" +Character.toString(target)+ "'COMPLETE";
					System.out.println(print);
				}
				node_up.res(root_, nextindex, number, eRes.COMPLETE);
				return;
			}
		}

		if(root_.getPrintRun())
		{
			String print = getString()+"-'" +Character.toString(target)+  "-BREAK";
			System.out.println(print);
		}
		node_up.res(root_, index_, number, eRes.BREAK);
	}
	protected void res(RootNode root_, int index_,int number_,eRes res_type_)
	{
		node_up.res(root_, index_, number, eRes.ERROR);
	}
	
}
