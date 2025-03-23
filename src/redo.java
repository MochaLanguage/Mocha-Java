import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class redo {
    public static Map<String, String> strvars = new HashMap<>();
    public static Map<String, Integer> intvars = new HashMap<>();
    public static Map<String, Double> doublevars = new HashMap<>();
    public static Map<String, Boolean> boolvars = new HashMap<>();

    public static Map<String, Integer> types = new HashMap<>();
    //bool, int, double, string, array

    public static void main(String[] args) throws Exception {
        String[][] lines = readFile("src\\main.mocha");
    }

    public static String[][] readFile(String path) throws IOException {
        String content = Files.readString(java.nio.file.Paths.get(path), java.nio.charset.StandardCharsets.UTF_8);
        content = replaceSpacesInStrings(content);
        String[] lines = content.split("\n");

        String[] test = new String[]{"aaaa", "hello world", "hi there"};
        String othertest = "aaa\nhrllo world\nhi there"; 
        String[] extratest = othertest.split("\n");
        System.out.println(Arrays.toString(lines));
        System.out.println(Arrays.toString(extratest));
        return new String[][]{{"a", "b"}, {"c", "d"}};
    }

    public static String replaceSpacesInStrings(String content) {
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("(\".*?\")");
        java.util.regex.Matcher matcher = pattern.matcher(content);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(0).replace(" ", "\u0000"));
        }
        matcher.appendTail(sb);
        return sb.toString();
    }
}
