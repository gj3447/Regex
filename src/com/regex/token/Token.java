package com.regex.token;
import java.lang.String;
public class Token {
	protected eToken type;
	public Token(eToken type_)
	{
		type = type_;
	}
	public String getString()
	{
		return Character.toString(TokenFunc.etoken2char(type));
	}
	public eToken getType()
	{
		return type;
	}
}
