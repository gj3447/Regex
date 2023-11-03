package com.regex.ast;
import java.lang.String;
public class Node {
	protected int number;
	protected Node node_up;
	
	public Node()
	{
		
	}
	protected void req(RootNode root_, int index_)
	{
		if(root_.getPrintRun())
		{
			String print = getString() + "-RUN";
			System.out.println(print);
		}
	}
	protected void res(RootNode root, int index,int number,eRes res_type)
	{
	}
	protected void print()
	{
		
	}
	public boolean add(Node node)
	{
		return false;
	}

	public String getString()
	{
		return "NODE";
	}
}
