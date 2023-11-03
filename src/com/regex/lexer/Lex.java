package com.regex.lexer;
import java.util.ArrayList;
import java.lang.String;
import com.regex.token.Token;
import com.regex.ast.*;
public class Lex {
	protected eLex type;
	public Lex()
	{
		
	}
	public Lex(eLex type_)
	{
		type = type_;
	}
	public void add_token_list(ArrayList<Token> token_list)
	{
		
	}
	public String getString()
	{
		switch(type)
		{
		case AND_OPEN:
			return "AND_OPEN";
		case AND_CLOSE:
			return "AND_CLOSE";
		case OR_OPEN:
			return "OR_OPEN";
		case OR_CLOSE:
			return "OR_CLOSE";
		case REPEAT_OPEN:
			return "REPEAT";
		case REPEAT_CLOSE:
			return "REPEAT_CLOSE";
		case CHARCLASS:
			return "CHARCLASS";
		case FSTRING:
			return "FSTRING";
		default:
			return "NULL";
			
		}
	}
	public eLex getType()
	{
		return type;
	}
	public Node getNode()
	{
		switch(type)
		{
		case AND_OPEN:
			return new And();
		case OR_OPEN:
			return new Or();
		default:
			return null;
		}
	}
}
