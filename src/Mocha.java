import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Random;

public class Mocha {
    public static Map<String, Object> vars = new HashMap<>();
    public static Map<String, Integer> types = new HashMap<>();
    //bool, int, double, string, array
    public static int linenum = 0;

    public static void main(String[] args) throws Exception {
        String[][] lines = readFile("src\\main.mocha");
        for (String[] line : lines) {
            if (line[0].startsWith(".")) {
                line[0] = line[0].substring(1);
                runLine(line,lines);
            }
        }

        linenum = 0;
        while (true) {
            runLine(lines[linenum],lines);
        }
    }

    // public static Object getVar() {
    //     return newObject;
    // }
    
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

    public static void runLine(String[] line, String[][] lines)
    {
        switch (line[0]) {
        case "out" -> {
            if (vars.containsKey(line[1])) {
                System.out.println((String) vars.get(line[1]));
            } else {
                System.out.println(line[1].substring(1, line[1].length() - 1));
            }
            linenum++;
        }
        case "end" -> {
            System.exit(0);
        }
        case "jmp" -> {
            if (vars.containsKey(line[1])) {
                linenum = Integer.parseInt(vars.get(line[1]).toString()) - 1;
            } else {
                linenum = Integer.parseInt(line[1]) - 1;
            }
        }
        case "try" -> {
            if ((Boolean) vars.get(line[2])){
                if (vars.containsKey(line[1])) {
                    linenum = Integer.parseInt(vars.get(line[1]).toString()) - 1;
                } else {
                    linenum = Integer.parseInt(line[1]) - 1;
                }
            }
        }
        case "slp" -> {
            try {
                if (vars.containsKey(line[1])) {
                    Thread.sleep((int) vars.get(line[2])); 
                }
                else {
                    Thread.sleep(Integer.parseInt(line[1]));
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        case "err" -> {
            try {
                runLine(lines[linenum+1],lines);
            } catch (Exception e) {
                if (vars.containsKey(line[1])) {
                    linenum = Integer.parseInt(vars.get(line[1]).toString()) - 1;
                } else {
                    linenum = Integer.parseInt(line[1]) - 1;
                }
            }
        }
        case "var" -> {
            switch (line[1]) {
            case "int" -> {
                switch (line[3]) {
                case "set" -> {
                    if (vars.containsKey(line[3])) {
                        vars.put(line[2], vars.get(line[4]));
                        types.put(line[2],1);
                    } else {
                        vars.put(line[2], line[4]);
                        types.put(line[2],1);
                    }
                }
                case "add" -> {
                    if (vars.containsKey(line[4])) {
                        vars.put(line[2], (int) vars.get(line[4]) + (int) vars.get(line[2]));
                        types.put(line[2],1);
                    } else {
                        vars.put(line[2], Integer.parseInt(line[4]) + (int) vars.get(line[2]));
                        types.put(line[2],1);
                    }
                }
                case "sub" -> {
                    if (vars.containsKey(line[4])) {
                        vars.put(line[2], (int) vars.get(line[4]) - (int) vars.get(line[2]));
                        types.put(line[2],1);
                    } else {
                        vars.put(line[2], Integer.parseInt(line[4]) - (int) vars.get(line[2]));
                        types.put(line[2],1);
                    }
                }
                case "mlt" -> {
                    if (vars.containsKey(line[4])) {
                        vars.put(line[2], (int) vars.get(line[4]) * (int) vars.get(line[2]));
                        types.put(line[2],1);
                    } else {
                        vars.put(line[2], Integer.parseInt(line[4]) * (int) vars.get(line[2]));
                        types.put(line[2],1);
                    }
                }
                case "div" -> {
                    if (vars.containsKey(line[4])) {
                        vars.put(line[2], (int) vars.get(line[4]) / (int) vars.get(line[2]));
                        types.put(line[2],1);
                    } else {
                        vars.put(line[2], Integer.parseInt(line[4]) / (int) vars.get(line[2]));
                        types.put(line[2],1);
                    }
                }
                case "mod" -> {
                    if (vars.containsKey(line[4])) {
                        vars.put(line[2], (int) vars.get(line[4]) % (int) vars.get(line[2]));
                        types.put(line[2],1);
                    } else {
                        vars.put(line[2], Integer.parseInt(line[4]) % (int) vars.get(line[2]));
                        types.put(line[2],1);
                    }
                }
                case "pow" -> {
                    if (vars.containsKey(line[4])) {
                        vars.put(line[2], Math.pow((int) vars.get(line[4]), (int) vars.get(line[2])));
                        types.put(line[2],1);
                    } else {
                        vars.put(line[2], Math.pow(Integer.parseInt(line[4]), (int) vars.get(line[2])));
                        types.put(line[2],1);
                    }
                }
                case "rng" -> {
                    Random rng = new Random();
                    //var int test rng 1 2
                    if (vars.containsKey(line[4])) {
                        if (vars.containsKey(line[5])) {
                            vars.put(line[2],new rng.nextInt((int) vars.get(line[5]) - (int) vars.get(line[4])) + (int) vars.get(line[4]));
                        } else {

                        }
                    } else {

                    }
                }
                }
            }
            }
        }
        }
    }
}
