import java.io.IOException;
import java.nio.file.Files;

public class Mocha {
    public static void main(String[] args) throws Exception {
        String result = readFile("src\\main.mocha");
        System.out.println(result);
    }

    public static String readFile(String path) throws IOException {
        String text = Files.readString(java.nio.file.Paths.get(path), java.nio.charset.StandardCharsets.UTF_8);
        text = text.replaceAll("\\/", "\u0000");
        return text;
        // text = text.replaceAll("\\\\/", "/");
        // text = text.replaceAll("(\".*?\")", m.group(0).replace(" ", "\u0000"));
        // String[] result = java.util.Arrays.stream(text.split("\n"))
        //     .map(line -> java.util.Arrays.stream(line.strip().split("\\s+"))
        //         .map(i -> i.replace("\\n", "\n").replace("\u0000", " "))
        //         .toArray(String[]::new))
        //     .filter(i -> i.length > 0 && !i[0].isEmpty())
        //     .map(java.util.Arrays::toString)
        //     .toArray(String[]::new);
        // return result;
    }
}
