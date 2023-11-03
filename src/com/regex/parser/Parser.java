package com.regex.parser;
import com.regex.lexer.*;
import java.util.ArrayList;
import java.util.Stack;
import com.regex.ast.*;
import com.regex.main.State;
public class Parser {
	public Parser()
	{
		
	}
	public RootNode lexlist2tree(ArrayList<Lex> lex_list)
	{

		RootNode root = new RootNode();
		Stack<Node> node_stack = new Stack<Node>();
		Node nodebuffer;
		node_stack.add(root);
		for(Lex l : lex_list)
		{
			State state = read(l,node_stack);
			switch(state)
			{
			case FUNC:
				node_stack.push(l.getNode());
				break;
			case BREAK:
				Node node2 = node_stack.pop();
				node_stack.peek().add(node2);
				break;
			case ERROR:
				System.out.println("parser error");
				return null;
			case READ:
				Node node3 = l.getNode();
				node_stack.peek().add(node3);
				break;
				
			}
		}
		root.add(node_stack.pop());
		return root;
	}
	public State read(Lex l,Stack<Node> node_stack)
	{
		switch(l.getType())
		{
		case REPEAT_OPEN:
			return State.FUNC;
		case REPEAT_CLOSE:
			if(node_stack.peek().getClass()==Repeat.class)
				return State.BREAK;
			else
				return State.ERROR;
		case AND_OPEN:
			return State.FUNC;
		case AND_CLOSE:
			if(node_stack.peek().getClass()==And.class)
				return State.BREAK;
			else
				return State.ERROR;
		case OR_OPEN:
				return State.FUNC;
		case OR_CLOSE:
			if(node_stack.peek().getClass()==Or.class)
				return State.BREAK;
			else
				return State.ERROR;
			
		case FSTRING:
			return State.READ;
		case CHARCLASS:
			return State.READ;
		default:
			return State.ERROR;
		}
	}
}
