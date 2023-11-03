package com.regex.lexer;
import java.util.ArrayList;
import com.regex.ast.CharRange;
import com.regex.ast.Node;
import com.regex.token.Token;
import com.regex.token.Tokenizer;
import java.lang.String;
import java.lang.StringBuilder;
import com.regex.ast.CharClass;
public class lCharClass extends Lex{
	protected ArrayList<Character> char_list;
	protected ArrayList<CharRange> char_range_list;
	protected boolean not;
	public lCharClass(boolean not_)
	{
		not = not_;
		char_list=  new ArrayList<Character>();
		char_range_list = new ArrayList<CharRange>();
		type = eLex.CHARCLASS;
	}
	@Override
	public String getString()
	{
		StringBuilder sb=  new StringBuilder();
		sb.append("CHARCLASS|char:");
		for(Character c: char_list)
		{
			sb.append(c);
		}
		sb.append("|charrange:");
		for(CharRange cr : char_range_list)
		{
			sb.append(cr.getString());
		}
		return sb.toString();
	}
	@Override
	public void add_token_list(ArrayList<Token> token_list)
	{
		if(token_list.size()==0)
		{}
		if(token_list.size()==1)
		{
			char_list.add(token_list.get(0).getString().charAt(0));
			return;
		}
		if(token_list.size()==2)
		{
			char c1 = token_list.get(0).getString().charAt(0);
			char c2 = token_list.get(1).getString().charAt(0);
			CharRange cr = new CharRange(c1,c2);
			char_range_list.add(cr);
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
		return new CharClass(char_list,char_range_list,not);
	}
}