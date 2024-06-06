public class Token {

    public enum TokenType {
        DIGIT, LETTER, STRING, KEYWORD, EOI, INVALID,
    }

    private TokenType type;
    private String val;

    public Token (TokenType t, String v) {
        type = t;
        val = v;
    }
    public TokenType getTokenType () {
// Return token type
        return type;
    }
    public String getTokenValue () {
// Return value
        return val;
    }
}
