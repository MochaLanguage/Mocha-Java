import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;

public class Parser {

    public static void main(String[] args) throws Exception {
        String[][] lines = readFile("src\\main.mocha");
        
        // System.out.println(Arrays.deepToString(lines));
    }

    public static String[][] readFile(String path) throws IOException {
        String content = Files.readString(java.nio.file.Paths.get(path), java.nio.charset.StandardCharsets.UTF_8);
        content = replaceSpacesInStrings(content);
        System.out.println(content);
        String[][] tokens = splitContentToTokens(content);
        tokens = fixSpacesInStrings(tokens);
        System.out.println(replaceSpacesInStrings("hello world\\n test!\"!!!\\n a\"a"));

        return tokens;      
    }

    public static String replaceSpacesInStrings(String content) {
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("(\".*?\")");
        java.util.regex.Matcher matcher = pattern.matcher(content);
        StringBuffer output = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(output, matcher.group(0).replace(" ", "\u0000"));
        }
        matcher.appendTail(output);
        return output.toString();
    }

    public static String[][] splitContentToTokens(String content) {
        String[] lines = content.split("\\R");
        
        String[][] splitLines = Arrays.stream(lines)
            .filter(line -> !line.isBlank())
            .map(line -> line.strip().split("\\s+"))
            .map(tokens -> Arrays.stream(tokens)
                .map(token -> token.replace("\\n", "\n"))
                .toArray(String[]::new))
            .toArray(String[][]::new);

        return splitLines;
    }

    public static String[][] fixSpacesInStrings(String[][] content) {
        for (int i=0; i<content.length;i++) {
            for (int j=0;j<content[i].length;j++) {
                content[i][j] = content[i][j].replace("\u0000"," ");
            }
        }
        return content;
    }
}
