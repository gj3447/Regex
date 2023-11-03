package com.regex.ast;
import java.util.ArrayList;
import java.lang.String;
import java.lang.StringBuilder;
public class Or extends Branch{
	protected ArrayList<Node> node_list;
	public Or()
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
	@Override
	public void print()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("#");
		sb.append(number);
		sb.append("OR(");
		System.out.println(sb.toString());
		for(Node n : node_list)
		{
			n.print();
		}
		sb = new StringBuilder();
		sb.append(")");
		System.out.println(sb.toString());
	}
	

	protected void req(RootNode root_, int index_)
	{
		count = 0;
		node_list.get(count).req(root_, index_);
	}
	protected void res(RootNode root_, int index_,int number_,eRes res_type_)
	{
		switch(res_type_)
		{
		case COMPLETE:
			count = number_+1;
			index = index_;
			if(count<node_list.size()){root_.branch(this);}
			node_up.res(root_,index_,number,eRes.COMPLETE);
			return;
		case BREAK:
			count = number_ +1;
			if(count<node_list.size())
			{
				node_list.get(count).req(root_, index_);
				return;
			}
			else
			{
				node_up.res(root_, index_, number, eRes.BREAK);
				return;
			}
		case ERROR:
			node_up.res(root_, index_, number, eRes.ERROR);
			return;
		}
	}
	@Override
	public void run(RootNode root_)
	{
		node_list.get(count).req(root_, index);
	}
	
}
