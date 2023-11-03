package com.regex.ast;
import java.util.Stack;
import java.lang.String;
import java.lang.StringBuilder;
public class RootNode extends Node {
	
	public Node root;
	
	protected String string;
	protected String result;
	protected Stack<Branch> branch_stack;
	protected int index;
	protected boolean error;
	protected boolean print_run;
	public RootNode()
	{
		branch_stack = new Stack<Branch>();
		root = null;
		print_run = true;
	}
	@Override
	public boolean add(Node node)
	{
		if(root == null)
		{
			root = node;
			node.number = 0;
			node.node_up = this;
			return true;
		}
		else return false;
	}
	@Override
	public void print()
	{
		System.out.println("ROOT");
		root.print();
	}

	protected void req(RootNode root_, int index_)
	{
		
	}
	protected void res(RootNode root_, int index_,int number_,eRes res_type)
	{
		switch(res_type)
		{
		case BREAK:
			run_break();
			return;
		case ERROR:
			error = true;
			return;
		case COMPLETE:
			run_complete(index_);
			return;
		}
	}
	public void run_break()
	{
		if(!branch_stack.isEmpty())
		{
			branch_stack.pop().run(this);
		}
	}
	public void run_complete(int index_)
	{
		String substring = string.substring(index, index_);
		if(result == null)
		{
			result = substring;
		}
		else
		{
			if(substring.length() >= result.length())
			{
				result = substring;
			}
		}
	}
	public String run(String s)
	{
		string =s;
		branch_stack = new Stack<Branch>();
		index = 0;
		error = false;
		result = null;
		while(index<string.length())
		{

			if(getPrintRun())
			{
				StringBuilder sb = new StringBuilder();
				sb.append("ROOT|index:");
				sb.append(index);
				sb.append("|");
				sb.append(string.substring(index));
				sb.append("-RUN");
				System.out.println(sb.toString());
			}
			root.req(this, index);
			if(error)
			{
				System.out.println("ast tree error");
				return null;
			}
			else
			{
				index++;
			}
		}
		return result;
	}
	public void branch(Branch branch)
	{
		branch_stack.push(branch);
	}
	public String getString()
	{
		return string;
	}
	public int getIndex()
	{
		return index;
	}
	public boolean getPrintRun()
	{
		return print_run;
	}

}
