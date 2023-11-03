package com.regex.token;
import java.util.ArrayList;
import java.lang.String;
import java.lang.StringBuilder;
import com.regex.main.State;
public class Tokenizer {
	public static ArrayList<String> tokenlist2stringlist(ArrayList<Token> token_list)
	{
		ArrayList<String> result = new ArrayList<String>();
		for(Token t:token_list)
		{
			result.add(t.getString());
		}
		return result;
	}
	public static String tokenlist2string(ArrayList<Token> token_list)
	{
		StringBuilder sb = new StringBuilder();
		for(Token t:token_list)
		{
			sb.append(t.getString());
		}
		return sb.toString();
	}
	public static ArrayList<Token> string2tokenlist(String s)
	{
		ArrayList<Token> result = new ArrayList<Token>();
		TokenData token_buffer = null;
		for(int i = 0; i< s.length();)
		{
			char c = s.charAt(i);
			eToken t = TokenFunc.char2etoken(c);
			State state = Tokenizer.read(c,t,token_buffer);
			switch(state)
			{
			case READ:
				result.add(new Token(t));
				i++;
				break;
			case WRITE:
				token_buffer.add(c);
				i++;
				break;
			case MOVE:
				result.add(token_buffer);
				token_buffer = null;
				break;
			case FUNC:
				token_buffer = new TokenData(t,c);
				i++;
				break;
			case ERROR:
				System.out.println("tokenizer error!");
				return null;
			default:
				break;
			}
		}
		return result;
	}
	private static State read(char c ,eToken t, TokenData token_buffer)
	{
		if(token_buffer==null)
			return Tokenizer.read_null(c,t);
		else
			return Tokenizer.read_data(c,t,token_buffer.getType());
	}
	private static State read_null(char c, eToken t)
	{
		if(TokenFunc.isTokenData(t))
			return State.FUNC;
		else
			return State.READ;
	}
	private static State read_data(char c, eToken t,eToken buffer_t)
	{
		if(TokenFunc.isTokenData(t)&& 
			t==buffer_t)
		{
			return State.WRITE;
			
		}
		return State.MOVE;
	}
}
