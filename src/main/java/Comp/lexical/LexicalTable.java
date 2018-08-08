package Comp.lexical;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LexicalTable {

	public static Map<String, TokenCategory> lexemMap = new HashMap<String, TokenCategory>();
	public static Map<String, TokenCategory> delimitadorMap = new HashMap<String, TokenCategory>();
	public static List<Character> symbolList = new ArrayList<Character>();

	static {

		// Operadores

		lexemMap.put("+", TokenCategory.TOPA);
		lexemMap.put("-", TokenCategory.TOPA);
		lexemMap.put("*", TokenCategory.TOPM);
		lexemMap.put("/", TokenCategory.TOPM);
		lexemMap.put("^", TokenCategory.TOPE);
		lexemMap.put("<", TokenCategory.TORC);
		lexemMap.put(">", TokenCategory.TORC);
		lexemMap.put("<=", TokenCategory.TORC);
		lexemMap.put(">=", TokenCategory.TORC);
		lexemMap.put("==", TokenCategory.TORE);
		lexemMap.put("!=", TokenCategory.TORE);
		lexemMap.put("=", TokenCategory.TATR);
		lexemMap.put("++", TokenCategory.TCONCAT);
		lexemMap.put("&&", TokenCategory.TAND);
		lexemMap.put("||", TokenCategory.TOR);
		lexemMap.put("~", TokenCategory.TNOT);
		
		// Delimitadores

		lexemMap.put("(", TokenCategory.TOP);
		lexemMap.put(")", TokenCategory.TCP);
		lexemMap.put("[", TokenCategory.TOB);
		lexemMap.put("]", TokenCategory.TCB);
		lexemMap.put("{", TokenCategory.TOK);
		lexemMap.put("}", TokenCategory.TCK);
		lexemMap.put("#", TokenCategory.TCOMMENT);

		// Terminador

		lexemMap.put(";", TokenCategory.TSCO);
		
		// Separador

		lexemMap.put(",", TokenCategory.TSPTR);
		lexemMap.put(":", TokenCategory.TCO);
		

		// Palavras-reservadas (comandos e operadores)
		
		lexemMap.put("null", TokenCategory.TNULL);
		lexemMap.put("empty", TokenCategory.TEMPTY);
		lexemMap.put("int", TokenCategory.TINT);
		lexemMap.put("float", TokenCategory.TFLOAT);
		lexemMap.put("char", TokenCategory.TCHAR);
		lexemMap.put("string", TokenCategory.TSTRING);
		lexemMap.put("bool", TokenCategory.TBOOL);
		lexemMap.put("module", TokenCategory.TMODULE);
		lexemMap.put("fun", TokenCategory.TFUN);
		lexemMap.put("return", TokenCategory.TRETURN);
		lexemMap.put("main", TokenCategory.TMAIN);
		lexemMap.put("read", TokenCategory.TREAD);
		lexemMap.put("show", TokenCategory.TSHOW);
		lexemMap.put("if", TokenCategory.TIF);
		lexemMap.put("elif", TokenCategory.TELIF);
		lexemMap.put("else", TokenCategory.TELSE);
		lexemMap.put("when", TokenCategory.TWHEN);
		lexemMap.put("repeater", TokenCategory.TREPEATER);
		lexemMap.put("true", TokenCategory.TTRUE);
		lexemMap.put("false", TokenCategory.TFALSE);
		
		// Símbolos que podem indicador próximo token
		symbolList.add(' ');
		symbolList.add(',');
		symbolList.add(';');
		symbolList.add('+');
		symbolList.add('-');
		symbolList.add('*');
		symbolList.add('\\');
		symbolList.add('/');
		symbolList.add('#');
		symbolList.add('$');
		symbolList.add('<');
		symbolList.add('>');
		symbolList.add('=');
		symbolList.add('~');
		symbolList.add('(');
		symbolList.add(')');
		symbolList.add('[');
		symbolList.add(']');
		symbolList.add('{');
		symbolList.add('}');
		symbolList.add('\'');
		symbolList.add('"');

	}

}