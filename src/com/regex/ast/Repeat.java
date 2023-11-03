package com.regex.ast;
import java.lang.String;
import java.lang.StringBuilder;
public class Repeat extends Branch{
	protected int min;
	protected int max;
	protected Node node_repeat;
	public Repeat(int min_, int max_)
	{
		super();
		min = min_;
		max = max_;
		node_repeat = null;
	}
	@Override
	public boolean add(Node node)
	{
		if(node_repeat==null)
		{
			node_repeat = node;
			node.node_up = this;
			node.number = 0;
			return true;
		}
		else
		{
			return false;
		}
	}
	@Override
	public void print()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("#");
		sb.append(number);
		sb.append("REPEAT|");
		sb.append(min);
		sb.append("-");
		sb.append(max);
		System.out.print(sb.toString());
		node_repeat.print();
	}
	

	@Override
	public String getString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("#");
		sb.append(number);
		sb.append("REPEAT|");

		sb.append(min);
		sb.append("<");
		sb.append(count);

		sb.append("<");
		sb.append(max);
		sb.append(count);
		return sb.toString();
	}
	protected void req(RootNode root_, int index_)
	{
		count = 0;
		if(max <= 0)
		{
			node_up.res(root_, index_, index_, eRes.COMPLETE);
			return;
		}
		else
		{

			if(root_.getPrintRun())
			{
				String print = getString() + "-START {";
				System.out.println(print);
			}
			
			node_repeat.req(root_, index_);
		}
	}
	protected void res(RootNode root_, int index_,int number_,eRes res_type_)
	{
		switch(res_type_)
		{
		case COMPLETE:
			count++;
			
			if(min>count)
			{

				if(root_.getPrintRun())
				{
					String print = getString() + "-NEED |";
					System.out.println(print);
				}
				
				node_repeat.req(root_, index_);
				
				return;
			}
			else if (count<max)
			{

				if(root_.getPrintRun())
				{
					String print = getString() + "-BRANCH V";
					System.out.println(print);
				}
				
				index = index_;
				root_.branch(this);
				node_up.res(root_, index_, number,eRes.COMPLETE);
			}
			else if (count==max)
			{

				if(root_.getPrintRun())
				{
					String print = getString() + "-COMPLETE }";
					System.out.println(print);
				}
				
				node_up.res(root_, index_, number, eRes.COMPLETE);
			}
			else
			{
				node_up.res(root_, index_, number,eRes.BREAK);
			}
		default:
			node_up.res(root_, index_, number, res_type_);
			return;
		}
	}
	@Override
	public void run(RootNode root_)
	{
		if(root_.getPrintRun())
		{
			String print = getString() +"-REPEAT @";
			System.out.println(print);
		}
		node_repeat.req(root_,index);
	}
	
}
