import java.util.ArrayList;
import java.util.List;

public class Lexer2 {

    public static class Token {
        public enum TokenType {
            EOI, KEYWORD, STRING, DIGIT, INVALID
        }

        private final TokenType tokenType;
        private final String lexeme;

        public Token(TokenType tokenType, String lexeme) {
            this.tokenType = tokenType;
            this.lexeme = lexeme;
        }

        public TokenType getTokenType() {
            return tokenType;
        }

        @Override
        public String toString() {
            return String.format("%-10s => [%s]", tokenType.name(), lexeme);
        }
    }

    public static class TestNextToken {
        private char[] buffer;
        private int idx = -1;
        private int maxlen = 0;
        private char ch;

        private char nextChar() {
            idx++;
            if (idx >= maxlen) return '\0';
            return buffer[idx];
        }

        public boolean testGetNextToken(Lexer2 lexer, String teststring, Token.TokenType target) {
            buffer = teststring.toCharArray();
            maxlen = teststring.length();
            ch = nextChar();  // Initialize the first character

            Token actual = lexer.getNextToken(this);

            return actual.getTokenType() == target;
        }

        public Token getNextToken() {
            do {
                if (ch == '\0') {
                    return new Token(Token.TokenType.EOI, "EOI");
                } else if (isLBracket(ch)) {
                    ch = nextChar();
                    boolean closingTag = isClosingTag(ch);
                    String keyword = "";
                    if (closingTag) {
                        ch = nextChar();
                    }
                    keyword = isValidHTMLTag(ch, closingTag);
                    if (keyword != null) {
                        return new Token(Token.TokenType.KEYWORD, keyword);
                    }
                    return new Token(Token.TokenType.INVALID, "Invalid keyword!");
                } else if (Character.isLetter(ch)) {
                    String string = concat(lettersAndDigits());
                    return new Token(Token.TokenType.STRING, string);
                } else if (Character.isDigit(ch)) {
                    String num = concat(digits());
                    return new Token(Token.TokenType.DIGIT, num);
                } else if (Character.isWhitespace(ch)) {
                    ch = nextChar();
                } else {
                    ch = nextChar();
                    return new Token(Token.TokenType.INVALID, "");
                }
            } while (true);
        }

        private boolean isLBracket(char ch) {
            return ch == '<';
        }

        private boolean isClosingTag(char ch) {
            return ch == '/';
        }

        private String isValidHTMLTag(char ch, boolean closingTag) {
            StringBuilder tag = new StringBuilder();
            if (Character.isLetter(ch)) {
                tag.append(ch);
                ch = nextChar();
                while (Character.isLetterOrDigit(ch)) {
                    tag.append(ch);
                    ch = nextChar();
                }
                if (closingTag && tag.toString().equals("validTag")) {
                    return tag.toString();
                } else if (!closingTag && tag.toString().equals("validTag")) {
                    return tag.toString();
                }
            }
            return null;
        }

        private String concat(char[] validChars) {
            StringBuilder sb = new StringBuilder();
            while (isValidChar(ch, validChars)) {
                sb.append(ch);
                ch = nextChar();
            }
            return sb.toString();
        }

        private boolean isValidChar(char ch, char[] validChars) {
            for (char validChar : validChars) {
                if (ch == validChar) {
                    return true;
                }
            }
            return false;
        }

        private char[] lettersAndDigits() {
            String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
            return chars.toCharArray();
        }

        private char[] digits() {
            String chars = "0123456789";
            return chars.toCharArray();
        }
    }

    public Token getNextToken(TestNextToken testNextToken) {
        return testNextToken.getNextToken();
    }

    public static void main(String[] args) {
        Lexer2 lexer = new Lexer2();
        TestNextToken testNextToken = new TestNextToken();

        String testString = "validTag 123 <invalid>";
        List<Token> tokens = new ArrayList<>();

        while (true) {
            Token token = lexer.getNextToken(testNextToken);
            tokens.add(token);
            if (token.getTokenType() == Token.TokenType.EOI) {
                break;
            }
        }

        for (Token token : tokens) {
            System.out.println(token);
        }
    }
}
