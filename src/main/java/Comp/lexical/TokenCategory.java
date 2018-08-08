package Comp.lexical;

public enum TokenCategory {

	TID(1), 
	TEMPTY(2), 
	TINT(3), 
	TFLOAT(4), 
	TCHAR(5), 
	TSTRING(6), 
	TBOOL(7), 
	TMODULE(8), 
	TFUN(9), 
	TRETURN(10), 
	TMAIN(11),
	TREAD(12), 
	TSHOW(13), 
	TIF(14), 
	TELIF(15), 
	TELSE(16), 
	TWHEN(17), 
	TREPEATER(18), 
	TTRUE(19),
	TFALSE(20), 
	TCTEINT(21), 
	TOK(22), 
	TCTEFLOAT(23), 
	TCTECHAR(24),
	TCTESTRING(25), 
	TSPTR(26), 
	TCO(27), 
	TSCO(28), 
	TOP(29), 
	TCP(30), 
	TOB(31), 
	TCB(32),
	TOPA(33),
	TOPM(34), 
	TOPE(35),
	TOPU(36), 
	TATR(37),
	TORC(38), 
	TORE(39), 
	TAND(40), 
	TOR(41), 
	TNOT(42), 
	TCONCAT(43), 
	TNULL(44), 
	TCK(45),
	TCOMMENT(46);
	
	private int value;
	
	private TokenCategory(int value) {
		this.value = value;
	}
	
	public int getCategoryValue() {
		return value;
	}
}