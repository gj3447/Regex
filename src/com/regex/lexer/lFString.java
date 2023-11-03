package com.regex.lexer;
import java.lang.String;
import java.util.ArrayList;

import com.regex.token.Token;
import com.regex.token.Tokenizer;
import com.regex.ast.*;
public class lFString extends Lex{
	protected String string;
	public lFString()
	{
		super();
		string = "";
		type = eLex.FSTRING;
	}
	@Override
	public String getString()
	{
		return "FSTRING|"+string;
	}
	@Override
	public void add_token_list(ArrayList<Token> token_list)
	{
		String s = Tokenizer.tokenlist2string(token_list);
		string = string+s;
	}
	@Override
	public eLex getType()
	{
		return type;
	}
	@Override
	public Node getNode()
	{
		return new FString(string);
	}
}
