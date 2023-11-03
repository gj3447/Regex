package com.regex.token;
import java.lang.String;

public class TokenData extends Token {
	protected String data;
	public TokenData(eToken t) {
		super(t);
		data = Character.toString(TokenFunc.etoken2char(t));
	}
	public TokenData(eToken t,String s) {
		super(t);
		data = s;
	}
	public TokenData(eToken t, char c) {
		super(t);
		data = Character.toString(c);
	}
	public void add(char c)
	{
		data = data+c;
	}
	@Override
	public String getString()
	{
		return data;
	}
}
