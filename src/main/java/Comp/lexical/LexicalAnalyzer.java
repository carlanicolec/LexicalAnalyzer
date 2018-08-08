package Comp.lexical;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale.Category;

import Comp.lexical.LexicalTable;
import Comp.lexical.Token;
import Comp.lexical.TokenCategory;

public class LexicalAnalyzer {

	private List<String> linesList;
	private int currentLine, currentColumn, tkBeginColumn = 0, tkBeginLine = 0;
	private String line;
	private String filePath;

	private final char LINE_BREAK = '\n';

	public LexicalAnalyzer(String filePath) {
		linesList = new ArrayList<String>();
		this.filePath = filePath;
	}

	public void readFile() {

		BufferedReader br;

		try {
			br = new BufferedReader(new FileReader(filePath));

			String brLine = br.readLine();

			while (brLine != null) {
				linesList.add(brLine);
				brLine = br.readLine();
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean hasMoreTokens() {

		if (!linesList.isEmpty()) {
			if (currentLine < linesList.size()) {

				line = linesList.get(currentLine);
				line = line.replace('\t', ' ');

				if (line.substring(currentColumn).matches("\\s*")) {
					currentLine++;
					currentColumn = 0;
					while (currentLine < linesList.size()) {
						line = linesList.get(currentLine);
						if (line.matches("\\s*")) {
							currentLine++;
						} else {
							return true;
						}
					}
				} else if (currentColumn < line.length()) {
					return true;
				} else {
					currentLine++;
					currentColumn = 0;
					while (currentLine < linesList.size()) {
						line = linesList.get(currentLine);
						if (line.matches("\\s*")) {
							currentLine++;
						} else {
							return true;
						}
					}
				}
			}
		}

		return false;

	}

	public Token nextToken() {

		Token token;

		char currentChar;
		String tkValue = "";

		tkBeginColumn = currentColumn;
		tkBeginLine = currentLine;

		currentChar = line.charAt(currentColumn);

		// Ignora sequência de espaços vazios
		while (currentChar == ' ' || currentChar == '\t') {
			currentChar = nextChar();
			tkBeginColumn++;
		}

		if (Character.toString(currentChar).matches("\\d")) {
			tkValue += currentChar;
			currentChar = nextChar();
			while (Character.toString(currentChar).matches("\\d")) {
				tkValue += currentChar;
				currentChar = nextChar();
			}
			if (currentChar == '.') {
				tkValue += currentChar;
				currentChar = nextChar();
				while (Character.toString(currentChar).matches("\\d")) {
					tkValue += currentChar;
					currentChar = nextChar();
				}
			}

			if (currentChar != ' ') {
				while (!LexicalTable.symbolList.contains(currentChar)) {
					tkValue += currentChar;

					// Vai para o proximo
					currentChar = nextChar();
					if (currentChar == LINE_BREAK) {
						break;
					}
				}
			}
		} else {

			// Enquanto nao for encontrado um simbolo especial, os
			// caracteres
			// serao concatenados em uma string que devera ser um token
			// identificador ou palavra chave.
			while (!LexicalTable.symbolList.contains(currentChar)) {
				tkValue += currentChar;

				// Vai para o proximo
				currentChar = nextChar();
				if (currentChar == LINE_BREAK) {
					break;
				}
			}
		}

		if (tkValue == "") {
			// Verificação de constantes inteiras ou decimais

			switch (currentChar) {

			case '"': // Compondo um token que possivelmente é um cchar

				tkValue += currentChar;
				currentChar = nextChar();

				if (currentChar == '"') {
					tkValue += currentChar;
					currentColumn++;
					break;
				}

				// Buscar os próximos caracteres até que encontre uma ", ou
				// acabe a linha
				while (currentChar != LINE_BREAK) {
					tkValue += currentChar;
					currentChar = nextChar();

					if (currentChar == '"') {
						tkValue += currentChar;
						currentColumn++;
						break;
					}
				}
				break;

			// TODO Verificação de comentários
			case '/':
				tkValue += currentChar;
				currentChar = nextChar();

				if (currentChar == '$') {
					tkValue += currentChar;
					currentLine++;
					currentColumn = 0;
				}
				break;

			// TODO AJEITAR -> Verificar se tem abre comentario antes;
			case '$':
				tkValue += currentChar;
				currentChar = nextChar();
				if (currentChar == '/') {
					tkValue += currentChar;
					currentChar = nextChar();
				}
				break;

			case '\'': // Compondo um token que possivelmente é um char

				tkValue += currentChar;

				// Buscar os próximos dois caracteres
				currentChar = nextChar();
				if (currentChar != LINE_BREAK) {
					tkValue += currentChar;
				}
				currentChar = nextChar();
				if (currentChar == '\'') {
					tkValue += currentChar;
					currentColumn++;
				}
				break;

			// TODO TRATAR N COISAS.... (=)...
			case '<':
			case '>':
			case '~':
			case '=': // Compondo um token que pode ser <=, >=, ~= ou ==

				tkValue += currentChar;
				currentChar = nextChar();
				if (currentChar == '=') {
					tkValue += currentChar;
					currentColumn++;
				}
				break;

			case '+': // Compondo um token que pode ser operador aditivo, de
						// concatenação ou constante numérica
				tkValue += currentChar;
				currentChar = nextChar();

				if (currentChar == '+') {
					tkValue += currentChar;
					currentChar = nextChar();
				}

				break;

			default:
				tkValue += currentChar;
				currentColumn++;
				break;
			}
		}

		tkValue = tkValue.trim();

		token = new Token();

		token.setValue(tkValue);
		token.setLine(tkBeginLine);
		token.setColumn(tkBeginColumn);
		token.setCategory(analyzeCategory(tkValue));

		if (token.getCategory().equals(TokenCategory.TCOMMENT)) {
			if (hasMoreTokens()) {
				return nextToken();
			}
		}
		return token;

	}

	private TokenCategory analyzeCategory(String tkValue) {

		if (isOpNegUnary(tkValue)) {
			return TokenCategory.OPNEGUN; //~~ DESCOBRIR A FINALIDADE 

		} else if (LexicalTable.lexemMap.containsKey(tkValue)) {
			return LexicalTable.lexemMap.get(tkValue);

		}// else if (isCchar(tkValue)) {			//~~ NÃO TEMOS CTE CHAR
			//return TokenCategory.CONSTCCHAR;

	//	} 
		else if (isChar(tkValue)) {
			return TokenCategory.TCHAR;

		} else if (isConstInt(tkValue)) {
			return TokenCategory.TCTEINT;

		} else if (isCteFloat(tkValue)) {
			return TokenCategory.TCTEFLOAT;

		} else if (isIdentifier(tkValue)) {
			return TokenCategory.TID;
		}

		return TokenCategory.UNKNOWN;		//~~DESCOBRIR O QUE É
	}

	private Character nextChar() {

		currentColumn++;

		if (currentColumn < line.length()) {
			return line.charAt(currentColumn);
		} else {
			return LINE_BREAK;
		}

	}

	private Character previousNotBlankChar() {

		int previousColumn = tkBeginColumn - 1;
		char previousChar;

		while (previousColumn >= 0) {
			previousChar = line.charAt(previousColumn);
			if (previousChar != ' ' && previousChar != '\t') {
				return previousChar;
			}
			previousColumn--;
		}
		return null;

	}

	
	//~~VER SE É NECESSÁRIO E FAZER AS MODIFICAÇÕES NECESSÁRIAS
	private boolean isOpNegUnary(String tkValue) {

		if (tkValue.equals("-")) { // Decide se o - é o operador aditivo ou se é
									// o unário negativo

			Character previousChar = previousNotBlankChar();
			if ((previousChar != null)
					&& Character.toString(previousChar).matches("[_a-zA-Z0-9]")) {
				return false;
			} else {
				return true;
			}
		}
		return false;
	}

	//~~OK
	private boolean isCteFloat(String tkValue) {
		if (tkValue.matches("(\\d)+\\.(\\d)+")) {
			return true;
		} else if (tkValue.matches("(\\d)+\\.")) {
			printError("constante float em formato errado.", tkValue);
		}
		return false;
	}

	private boolean isConstInt(String tkValue) {
		if (tkValue.matches("(\\d)+")) {
			return true;
		}
		return false;
	}

//	private boolean isCchar(String tkValue) {
//		if (tkValue.startsWith("\"") && tkValue.endsWith("\"")) {
//			return true;
//		} else if (tkValue.startsWith("\"")) {
//			printError(
//					"cadeia de caracteres não fechada corretamente com '\"'.",
//					tkValue);
//		}
//		return false;
//	}

	private boolean isChar(String tkValue) {
		if (tkValue.matches("'(.?)'")) {
			return true;
		} else if (tkValue.startsWith("'")) {
			printError("caracter não fechado corretamente com '.", tkValue);
		}
		return false;
	}

	private boolean isIdentifier(String tkValue) {

		if (tkValue.matches("[_a-zA-Z][_a-zA-Z0-9]*")) {
			if (tkValue.length() < 16) {
				return true;
			} else {
				printError("identificador muito longo.", tkValue);
			}

			// Caso em que o identificador não começa com o caractere esperado.
			// Também não considera tkValue que começa com ", ' ou número pois
			// caso
			// algum tkValue nessa condição chegue até aqui, é um cchar ou um
			// char que
			// não foi propriamente fechado, ou uma constante decimal em formato
			// errado.
		} else if (tkValue.matches("[^_a-zA-Z\"'].*")) {
			printError("identificador não iniciado com letra ou '_'.", tkValue);

			// Caso em que o identificador começa com o caracter esperado, mas
			// contém algum caracter inválido,
		} else if (tkValue.matches("[_a-zA-Z].*")) {
			printError("identificador contém caracter inválido.", tkValue);

		}
		return false;
	}

	private void printError(String string, String token) {
		System.err.println("Erro na <linha, coluna> " + "= <" + currentLine
				+ "," + currentColumn + ">. " + "'" + token + "'" + " "
				+ string);
		System.exit(1);
	}
}