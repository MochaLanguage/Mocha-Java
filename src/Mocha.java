import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Mocha {
    public static Map<String, Object> vars = new HashMap<>();
    public static int linenum = 0;

    public static void main(String[] args) throws Exception {
        String[][] lines = readFile("src\\main.mocha");

        for (String[] line : lines) {
            if (line[0].startsWith(".")) {
                line[0] = line[0].substring(1);
                runLine(line);
            }
        }

        while (true) {
            runLine(lines[linenum]);
        }
    }

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
        
        return processedText.toArray(new String[0][]);
    }

    public static void runLine(String[] line)
    {
        switch (line[0]) {
            case "out":
                if (vars.containsKey(line[1])) {
                    System.out.println(vars.get(line[1]));
                } else {
                    System.out.println(line[1].substring(1,line[1].length()-1));
                }
                linenum++;
                break;
            case "end":
                System.exit(0);
                break;
            case "jmp":
                if (vars.containsKey(line[1])) {
                    linenum = Integer.parseInt(vars.get(line[1]).toString())-1;
                } else {
                    linenum = Integer.parseInt(line[1])-1;
                }
                break;
            case "try":
                if (vars.get(line[2])){
                    if (vars.containsKey(line[1])) {
                        linenum = Integer.parseInt(vars.get(line[1]).toString())-1;
                    } else {
                        linenum = Integer.parseInt(line[1])-1;
                    }
                }
                break;
        }
    }
}
