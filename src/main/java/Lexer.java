import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {
    private static enum Token {

        TAG("<[^>]+>"), // Matches HTML tags
        ATTRIBUTE("\\w+=\"[^\"]*\""), // Matches HTML attributes
        TEXT("[^<]+"), // Matches text outside of tags
        SKIP("[ \t\f\r\n]+"); // Matches whitespace
//        NUMBER("-?[0-9]+"), OPERATOR("[*|/|+|-]"), SKIP("[ \t\f\r\n]+");

        private final String pattern;

        private Token(String pattern) {
            this.pattern = pattern;
        }
    }

    private static class Word {
        private Token token;
        private String lexeme;

        private Word(Token token, String lexeme) {
            this.token = token;
            this.lexeme = lexeme;
        }

        @Override
        public String toString() {
            return String.format("%-10s => [%s]", token.name(), lexeme);
        }
    }

    private static ArrayList<Word> lex(String input) {
        // The tokens to return
        ArrayList<Word> words = new ArrayList<Word>();

        // Lexer logic begins here
        StringBuffer tokenPatternsBuffer = new StringBuffer();
        for (Token token : Token.values())
            tokenPatternsBuffer.append(String.format("|(?<%s>%s)", token.name(), token.pattern));
        Pattern tokenPatterns = Pattern.compile(new String(tokenPatternsBuffer.substring(1)));

        // Begin matching tokens
        Matcher matcher = tokenPatterns.matcher(input);
        while (matcher.find()) {
            for (Token token : Token.values())
                if (matcher.group(token.name()) != null) {
                    words.add(new Word(token, matcher.group(token.name())));
                    continue;
                }
        }
        return words;
    }

    // Utility method to read a file into a string
    private static String readFile(String filePath) throws IOException {
        StringBuilder content = new StringBuilder();
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        String line;
        while ((line = br.readLine()) != null) {
            content.append(line).append("\n");
        }
        br.close();
        return content.toString();
    }

    public static void main(String[] args) {

        try {
            // Read the HTML file
            String input = readFile("/Users/matthew123003/IdeaProjects/LexLuthor/testdata/simpletest3.html");

            // Lex the input
            ArrayList<Word> words = lex(input);

            // Print the tokens
            for (Word word : words)
                System.out.println(word);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
