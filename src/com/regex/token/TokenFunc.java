package com.regex.token;
import java.lang.String;
import java.lang.StringBuilder;
public class TokenFunc {
	public static char etoken2char(eToken t)
	{
		switch (t)
		{case EXCLAMATION_POINT:
			return '!';
		case SINGLE_QUOTATION:
			return '`';
		case TILDE:
			return '~';
		case AT_SIGN:
			return '@';
		case CROSSHATCH:
			return '#';
		case DOLLAR_SIGN:
			return '$';
		case PERCENT_SIGN:
			return '%';
		case CIRCUMFLEX:
			return '^';
		case AMPERSAND:
			return '&';
		case ASTERISK:
			return '*';
		case LEFT_PARENTHESIS:
			return '(';
		case RIGHT_PARENTHESIS:
			return ')';
		case HYPHEN:
			return '-';
		case UNDERSCORE:
			return '_';
		case EQUAL_SIGN:
			return '=';
		case PLUS_SIGN:
			return '+';
		case VERTICAL_BAR:
			return '|';
		case LEFT_BRACKET:
			return '[';
		case RIGHT_BRACKET:
			return ']';
		case LEFT_BRACE:
			return '{';
		case RIGHT_BRACE:
			return '}';
		case SEMICOLON:
			return ';';
		case COLON:
			return ':';
		case QUOTATION_MARK:
			return '"';
		case APOSTROPHE:
			return '\'';
		case COMMA:
			return ',';
		case PERIOD:
			return '.';
		case LESS_THAN:
			return '<';
		case GREATER_THAN:
			return '>';
		case SLASH:
			return '/';
		case QUESTION_MARK:
			return '?';
		case SPACE:
			return ' ';
			
		case BACKSLASH:
			return '\\';
		case TAB:
			return '\t';
		case ENTER:
			return '\n';
		default:
			return ' ';
		
		}
	}

	public static eToken char2etoken(char c)
	{
		switch (c)
		{
		case '!':
			return eToken.EXCLAMATION_POINT;
		case '`':
			return eToken.SINGLE_QUOTATION;
		case '~':
			return eToken.TILDE;
		case '@':
			return eToken.AT_SIGN;
		case '#':
			return eToken.CROSSHATCH;
		case '$':
			return eToken.DOLLAR_SIGN;
		case '%':
			return eToken.PERCENT_SIGN;
		case '^':
			return eToken.CIRCUMFLEX;
		case '&':
			return eToken.AMPERSAND;
		case '*':
			return eToken.ASTERISK;
		case '(':
			return eToken.LEFT_PARENTHESIS;
		case ')':
			return eToken.RIGHT_PARENTHESIS;
		case '-':
			return eToken.HYPHEN;
		case '_':
			return eToken.UNDERSCORE;
		case '=':
			return eToken.EQUAL_SIGN;
		case '+':
			return eToken.PLUS_SIGN;
		case '|':
			return eToken.VERTICAL_BAR;
		case '[':
			return eToken.LEFT_BRACKET;
		case ']':
			return eToken.RIGHT_BRACKET;
		case '{':
			return eToken.LEFT_BRACE;
		case '}':
			return eToken.RIGHT_BRACE;
		case ';':
			return eToken.SEMICOLON;
		case ':':
			return eToken.COLON;
		case '"':
			return eToken.QUOTATION_MARK;
		case '\'':
			return eToken.APOSTROPHE;
		case ',':
			return eToken.COMMA;
		case '.':
			return eToken.PERIOD;
		case '<':
			return eToken.LESS_THAN;
		case '>':
			return eToken.GREATER_THAN;
		case '/':
			return eToken.SLASH;
		case '?':
			return eToken.QUESTION_MARK;
		case ' ':
			return eToken.SPACE;
		case '\\':
			return eToken.BACKSLASH;
		case '\t':
			return eToken.TAB;
		case '\n':
			return eToken.ENTER;
			
		case '0':
		case '1':
		case '2':
		case '3':
		case '4':
		case '5':
		case '6':
		case '7':
		case '8':
		case '9':
			return eToken.NUMBER;
		default:
			return eToken.STRING;
		}
	}
	public static boolean isTokenData(eToken e)
	{
		switch(e)
		{
		case NUMBER:
		case SPACE:
		case STRING:
			return true;
		default:
			return false;
		}
	}
}
