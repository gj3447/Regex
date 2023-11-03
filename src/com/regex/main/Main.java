package com.regex.main;
import java.lang.String;
import java.util.ArrayList;
import com.regex.token.*;
import com.regex.lexer.*;
import com.regex.parser.*;
import com.regex.ast.*;
public class Main {
	public static void main(String argv[])
	{
		String regex = Func.file_input("regex.txt");
		//String string = Func.file_input("string.txt");
		ArrayList<Token> token_list =  Tokenizer.string2tokenlist(regex);
		ArrayList<String> string_list = Tokenizer.tokenlist2stringlist(token_list);
		for(String stemp :string_list)
		{
			System.out.println(stemp);
		}
		Lexer lexer = new Lexer();
		
		ArrayList<Lex> lex_list = lexer.tokenlist2lexlist(token_list);
		for(Lex ltemp : lex_list)
		{
			System.out.println(ltemp.getString());
		}
		Parser parser = new Parser();
		RootNode root = parser.lexlist2tree(lex_list);
		root.print();
		String result = root.run("&^%&%aasdsasd@basda.col");
		
		System.out.println(result);
	}
}
