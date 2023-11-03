package com.regex.lexer;

import java.util.ArrayList;

import com.regex.token.Token;
import com.regex.token.Tokenizer;
import com.regex.token.eToken;
import com.regex.ast.*;
public class lRepeat extends Lex {
	protected int max;
	protected int min;
	public lRepeat()
	{
		super();
		max=  0;
		min = 0;
		type = eLex.REPEAT_OPEN;
	}
	public lRepeat(int min_, int max_)
	{
		super();
		type = eLex.REPEAT_OPEN;
		min  = min_;
		max = max_;
	}
	@Override
	public String getString()
	{
		return "REPEAT|"+min+"-"+max;
	}
	@Override
	public void add_token_list(ArrayList<Token> token_list)
	{
		System.out.println("repeattoken_add");
		if(token_list.size()==1)
		{min = 1; max = 1;return;}
		if(token_list.size()==2)
		{
			if(token_list.get(0).getType()==eToken.COMMA)
			{
				min = 0;
			}
			if(token_list.get(0).getType()==eToken.NUMBER)
			{
				min = Integer.parseInt(token_list.get(0).getString());
			}
			if(token_list.get(1).getType()==eToken.COMMA)
			{
				max = 0;
			}
			if(token_list.get(1).getType()==eToken.NUMBER)
			{
				max = Integer.parseInt(token_list.get(1).getString());
			}
			return;
		}
	}
	@Override
	public eLex getType()
	{
		return type;
	}
	@Override
	public Node getNode()
	{
		return new Repeat(min,max);
	}
}
