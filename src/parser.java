import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class parser {
    public static String[][] readFile(String path) throws IOException {
        String text = Files.readString(java.nio.file.Paths.get(path), java.nio.charset.StandardCharsets.UTF_8);
        //null character my beloved <3
        text = text.replaceAll("\\\\/", "\u0000");
        text = text.replaceAll("/(?s).*?/", "");
        text = text.replaceAll("\u0000","/");

        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("(\".*?\")");
        java.util.regex.Matcher matcher = pattern.matcher(text);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(0).replace(" ", "\u0000"));
        }
        matcher.appendTail(sb);
        text = sb.toString();

        List<String[]> processedText = Arrays.stream(text.split("\n"))
            .map(line -> Arrays.stream(line.strip().split("\\s+"))
                .map(i -> i.replace("\\n", "\n").replace("\u0000", " "))
                .toArray(String[]::new))
            .filter(i -> i.length > 0 && !i[0].isEmpty())
            .collect(Collectors.toList());
        
        return processedText.toArray(String[][]::new);
    }
}
