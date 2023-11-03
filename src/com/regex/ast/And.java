package com.regex.ast;
import java.util.ArrayList;
import java.lang.String;
import java.lang.StringBuilder;
public class And extends Node{
	protected ArrayList<Node> node_list;
	public And()
	{
		node_list = new ArrayList<Node>();
	}
	@Override
	public boolean add(Node node)
	{
		node.node_up = this;
		node.number = node_list.size();
		node_list.add(node);
		return true;
	}
	public void print()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("#");
		sb.append(number);
		sb.append("AND(");
		System.out.println(sb.toString());
		for(Node n : node_list)
		{
			n.print();
		}
		sb = new StringBuilder();
		sb.append(")");
		System.out.println(sb.toString());
	}
	@Override
	public String getString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("#");
		sb.append(number);
		sb.append("AND");
		return sb.toString();
	}
	@Override
	protected void req(RootNode root_, int index_)
	{
		if(node_list.size()==0)
		{
			if(root_.getPrintRun())
			{
				String print = getString() + "-COMPLETE";
				System.out.println(print);
			}
			node_up.res(root_, index_,number,eRes.COMPLETE);
		}
		else
		{
			if(root_.getPrintRun())
			{
				String print = getString() + "-#0RUN";
				System.out.println(print);
			}
			node_list.get(0).req(root_, index_);
		}
	}
	@Override
	protected void res(RootNode root_, int index_,int number_,eRes res_type)
	{
		int next_number = number_+1;
		switch(res_type)
		{
			case COMPLETE:
			if(next_number < node_list.size())
			{
				if(root_.getPrintRun())
				{
					String print = getString() + "-#"+Integer.toString(next_number)+"RUN";
					System.out.println(print);
				}
				node_list.get(next_number).req(root_, index_);

				return;
			}
			else
			{
				if(root_.getPrintRun())
				{
					String print = getString() + "-COMPLETE";
					System.out.println(print);
				}
				node_up.res(root_, index_,number,eRes.COMPLETE);
				return;
			}
			default:
				node_up.res(root_, index_,number, res_type);
		}
	}
	
}
