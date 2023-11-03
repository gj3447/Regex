package com.regex.lexer;

public class LexFunc {
	public static boolean isLeaf(Lex l)
	{
		switch(l.getType())
		{
		case FSTRING:
		case CHARCLASS:
			return true;
		default:
			return false;
		}
	}
	public static boolean isClose(Lex l)
	{
		switch(l.getType())
		{
		case OR_CLOSE:
		case AND_CLOSE:
			return true;
		default:
			return false;
		}
	}
}
