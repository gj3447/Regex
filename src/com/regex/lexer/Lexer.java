package com.regex.lexer;
import java.util.ArrayList;
import java.util.Stack;
import com.regex.token.*;
import com.regex.main.State;
public class Lexer {
	
	private Lex lex_buffer;
	private ArrayList<Lex> lex_list;
	private Stack<Lex> lex_stack;
	private ArrayList<Token> token_buffer;
	private eLex state;
	private Lex lex_stack_pop;
	
	private boolean semaphore;
	private boolean not;
	
	private boolean semaphore(boolean up)
	{
		if(semaphore == up)
		{
			return false;
		}
		else
		{
			semaphore = up;
			return true;
		}
	}
	public Lexer()
	{
		
	}
	public ArrayList<Lex> tokenlist2lexlist(ArrayList<Token> token_list)
	{	
		lex_list = new ArrayList<Lex>();
		lex_stack = new Stack<Lex>();
		token_buffer = new ArrayList<Token>();
		lex_buffer = null;
		not = false;
		semaphore(false);
		state = eLex.NULL;
		
		
		and_open();
		
		for(Token t :token_list)
		{
			State state = read(t);
			switch (state)
			{
			case WRITE:
				token_buffer.add(t);
				break;
			case MOVE: 
				move();
				break;
			case SKIP:
				break;
			case ERROR:
				System.out.println("lexer error!");
				return null;
			default:
				break;
			}
		}

		and_close();
		if(!lex_stack.empty())
			System.out.println("lexer error! stack is not empty");
		return lex_list;
	}
	private State read(Token t)
	{
		switch(state)
		{
		case NULL:
			return read_null(t);
		case FSTRING:
			return read_fstring(t);
		case CHARCLASS:
			return read_charclass(t);
		case REPEAT_OPEN:
			return read_repeat(t);
		case CHARCLASS_STRING:
			return read_charclass_string(t);
		default:
			return State.ERROR;
		
		}
	}
	private State read_fstring(Token t)
	{
		if(semaphore)
		{
			semaphore(false);
			switch(t.getType()){
			
		case BACKSLASH:
		return State.WRITE;
		
		case QUOTATION_MARK:
		return State.WRITE;
		/*
		case STRING:
		if(t.getString().charAt(0)=='n') {
			token_buffer.add(new Token(eToken.ENTER));
			return State.SKIP;}
		if(t.getString().charAt(0)=='t') {
			token_buffer.add(new Token(eToken.TAB));
			return State.SKIP;}
		return State.ERROR;*/
		default:
		return State.ERROR;
		
		}}
		else
		{switch(t.getType())
		{
		case BACKSLASH: // \
			semaphore(true);
			return State.SKIP;
			
		case QUOTATION_MARK:
			return fstring_close();
		
		default:
			return State.WRITE;
		}}
	}
	
	private State read_charclass(Token t)
	{
		switch(t.getType())
		{
		case COMMA: // ,
			if(semaphore(false)) return State.SKIP;
			else return State.ERROR;
		case QUOTATION_MARK: // "
			return charclass_string_open();
		case RIGHT_BRACKET: // ]
			return charclass_close();
		default:
			return State.ERROR;
		}
	}
	
	private State read_charclass_string(Token t)
	{
		switch(t.getType())
		{
		case QUOTATION_MARK:
			if(semaphore) return charclass_string_close();
			else return State.WRITE;
			
		case HYPHEN:
			if(semaphore) {
			semaphore(false);
			return State.SKIP;}
			else {
			semaphore(true);
			return State.WRITE;}
		
		default:
			if(semaphore) {
			return State.ERROR;}
			else{
			semaphore(true);
			return State.WRITE;}
			
		}
	}
	private State read_repeat(Token t)
	{
		switch(t.getType())
		{
		case COMMA:
			if(semaphore(false)) return State.SKIP;
			else return State.WRITE;
		case NUMBER:
			if(semaphore(true)) return State.WRITE;
			else return State.ERROR;
		case RIGHT_BRACE:
			return repeat_close();
		default:
			return State.ERROR;
		}
	}
	private State read_null(Token t)
	{
		switch(t.getType())
		{
		case LEFT_PARENTHESIS:// (
			return and_open();

		case RIGHT_PARENTHESIS:// )
			return and_close();
			
		case LEFT_BRACKET: // [
			return charclass_open();
			
		case LEFT_BRACE: // {
			return repeat_open();
			
		case VERTICAL_BAR: // |
			return or();
			
		case QUOTATION_MARK: // "
			return fstring_open();
			
		case TILDE://~
			return not();
			
		case QUESTION_MARK: // ?
			return repeat_0_1();
			
		case PLUS_SIGN:
			return repeat_1_();
		
		case ASTERISK:
			return repeat_0_();
			
		case COMMA:
			if(semaphore(false)) return State.SKIP;
			else return State.ERROR;
		
			default:
				return State.ERROR;
		}
	}
	
	private State fstring_open()
	{
		lex_buffer = new lFString();
		state = eLex.FSTRING;
		semaphore(false);
		return State.SKIP;
	}
	private State fstring_close()
	{
		move();
		lex_list.add(lex_buffer);
		lex_buffer = null;
		state = eLex.NULL;
		semaphore(true);
		return State.SKIP;
	}
	private State charclass_open()
	{
		lex_buffer = new lCharClass(not);
		token_buffer.clear();
		state = eLex.CHARCLASS;
		not = false;
		semaphore(false);
		return State.SKIP;
	}
	private State charclass_close()
	{
		lex_list.add(lex_buffer);
		lex_buffer = null;
		state = eLex.NULL;
		semaphore(true);
		return State.SKIP;
	}
	private State charclass_string_open()
	{
		state = eLex.CHARCLASS_STRING;
		semaphore(false);
		return State.SKIP;
	}
	private State charclass_string_close()
	{
		state = eLex.CHARCLASS;
		move();
		semaphore(true);
		return State.MOVE;
	}
	private State repeat_open()
	{
		lex_buffer = new lRepeat();
		token_buffer.clear();
		state = eLex.REPEAT_OPEN;
		semaphore(false);
		return State.SKIP;
	}
	private State repeat_close()
	{
		if(lex_list.size()-1<0)
			return State.ERROR;
		int index;
		if(LexFunc.isClose(lex_list.get(lex_list.size()-1)))
			index = lex_list.indexOf(lex_stack_pop);
		else
			index = lex_list.size()-1;
		
		lex_list.add(index,lex_buffer);
		lex_list.add(new Lex(eLex.REPEAT_CLOSE));
		move();
		lex_buffer = null;
		state = eLex.NULL;
		semaphore(true);
		return State.SKIP;
	}
	private State repeat_0_1()
	{
		if(lex_list.size()-1<0)
			return State.ERROR;
		int index;
		Lex lex =  lex_list.get(lex_list.size()-1);
		if(LexFunc.isClose(lex_list.get(lex_list.size()-1)))
			index = lex_list.indexOf(lex_stack_pop);
		else
			index = lex_list.size()-1;
		
		lRepeat r = new lRepeat(0,1);
		lex_list.add(index,r);
		lex_list.add(new Lex(eLex.REPEAT_CLOSE));
		semaphore(true);
		return State.SKIP;
	}
	private State repeat_0_()
	{
		if(lex_list.size()-1<0)
			return State.ERROR;
		int index;
		if(LexFunc.isClose(lex_list.get(lex_list.size()-1)))
			index = lex_list.indexOf(lex_stack_pop);
		else
			index = lex_list.size()-1;
		
		lRepeat r = new lRepeat(0,Integer.MAX_VALUE);
		lex_list.add(index,r);
		lex_list.add(new Lex(eLex.REPEAT_CLOSE));
		semaphore(true);
		return State.SKIP;
	}
	private State repeat_1_()
	{
		if(lex_list.size()-1<0)
			return State.ERROR;
		int index;
		if(LexFunc.isClose(lex_list.get(lex_list.size()-1)))
			index = lex_list.indexOf(lex_stack_pop);
		else
			index = lex_list.size()-1;
		
		lRepeat r = new lRepeat(1,Integer.MAX_VALUE);
		lex_list.add(index,r);
		lex_list.add(new Lex(eLex.REPEAT_CLOSE));
		semaphore(true);
		return State.SKIP;
	}
	private State not()
	{
		not = true;
		return State.SKIP;
	}
	private State and_open()
	{
		Lex l = new Lex(eLex.AND_OPEN);
		lex_list.add(l);
		lex_stack.push(l);
		state = eLex.NULL;	
		semaphore(false);
		return State.SKIP;
	}
	private State and_close()
	{
		if(lex_stack.empty()) return State.ERROR;
		lex_stack_pop = lex_stack.pop();
		if(lex_stack_pop.getType()==eLex.AND_OPEN)
			lex_list.add(new Lex(eLex.AND_CLOSE));
		else return State.ERROR;
		if(lex_stack.empty()) return State.SKIP;
		if(lex_stack.peek().getType()==eLex.OR_OPEN)
		{
			lex_stack_pop = lex_stack.pop();
			lex_list.add(new Lex(eLex.OR_CLOSE));
		}
		return State.SKIP;
	}
	private State or()
	{
		lex_stack_pop = lex_stack.pop();
		lex_list.add(new Lex(eLex.AND_CLOSE));
		if(lex_stack.empty())
		{
			int i =lex_list.indexOf(lex_stack_pop);
			Lex or_open = new Lex(eLex.OR_OPEN);
			lex_list.add(i,or_open);
			lex_stack.push(or_open);
			Lex and_open = new Lex(eLex.AND_OPEN);
			lex_list.add(and_open);
			lex_stack.push(and_open);
			return State.SKIP;
		}
		if(lex_stack.peek().getType()==eLex.AND_OPEN)
		{
			int i =lex_list.indexOf(lex_stack_pop);
			Lex or_open = new Lex(eLex.OR_OPEN);
			lex_list.add(i,or_open);
			lex_stack.push(or_open);
			Lex and_open = new Lex(eLex.AND_OPEN);
			lex_list.add(and_open);
			lex_stack.push(and_open);
		}
		else
		{
			Lex and_open = new Lex(eLex.AND_OPEN);
			lex_list.add(and_open);
			lex_stack.push(and_open);
		}
		return State.SKIP;
	}
	
	private void move()
	{
		lex_buffer.add_token_list(token_buffer);
		token_buffer.clear();
	}
}