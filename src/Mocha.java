import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Mocha extends Parser {
    public static Map<String, Object> vars = new HashMap<>();
    public static Map<String, Short> types = new HashMap<>();
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
            // System.out.println(linenum);
            runLine(lines[linenum],lines);
        }
    }
    
    public static String[][] readFile(String path) throws IOException {
        return Parser.readFile(path);
    }

    public static void runLine(String[] line, String[][] lines)
    {
        switch (line[0]) {
        case "out" -> {
            if (vars.containsKey(line[1])) {
                if (types.get(line[1]) < 5) {
                    System.out.print(vars.get(line[1]));
                }
                else {
                    System.out.print(Arrays.toString((Object[]) vars.get(line[1])));
                }
            } else {
                System.out.print(line[1].substring(1, line[1].length() - 1));
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
                    if (vars.containsKey(line[4])) {
                        vars.put(line[2], vars.get(line[4]));
                        types.put(line[2],(short) 1);
                    } else {
                        vars.put(line[2], line[4]);
                        types.put(line[2],(short) 1);
                    }
                }
                case "add" -> {
                    if (vars.containsKey(line[4])) {
                        vars.put(line[2], (int) vars.get(line[4]) + (int) vars.get(line[2]));
                        types.put(line[2],(short) 1);
                    } else {
                        vars.put(line[2], Integer.parseInt(line[4]) + (int) vars.get(line[2]));
                        types.put(line[2],(short) 1);
                    }
                }
                case "sub" -> {
                    if (vars.containsKey(line[4])) {
                        vars.put(line[2], (int) vars.get(line[4]) - (int) vars.get(line[2]));
                        types.put(line[2],(short) 1);
                    } else {
                        vars.put(line[2], Integer.parseInt(line[4]) - (int) vars.get(line[2]));
                        types.put(line[2],(short) 1);
                    }
                }
                case "mlt" -> {
                    if (vars.containsKey(line[4])) {
                        vars.put(line[2], (int) vars.get(line[4]) * (int) vars.get(line[2]));
                        types.put(line[2],(short) 1);
                    } else {
                        vars.put(line[2], Integer.parseInt(line[4]) * (int) vars.get(line[2]));
                        types.put(line[2],(short) 1);
                    }
                }
                case "div" -> {
                    if (vars.containsKey(line[4])) {
                        vars.put(line[2], (int) vars.get(line[4]) / (int) vars.get(line[2]));
                        types.put(line[2],(short) 1);
                    } else {
                        vars.put(line[2], Integer.parseInt(line[4]) / (int) vars.get(line[2]));
                        types.put(line[2],(short) 1);
                    }
                }
                case "mod" -> {
                    if (vars.containsKey(line[4])) {
                        vars.put(line[2], (int) vars.get(line[4]) % (int) vars.get(line[2]));
                        types.put(line[2],(short) 1);
                    } else {
                        vars.put(line[2], Integer.parseInt(line[4]) % (int) vars.get(line[2]));
                        types.put(line[2],(short) 1);
                    }
                }
                case "pow" -> {
                    if (vars.containsKey(line[4])) {
                        vars.put(line[2], Math.pow((int) vars.get(line[4]), (int) vars.get(line[2])));
                        types.put(line[2],(short) 1);
                    } else {
                        vars.put(line[2], Math.pow(Integer.parseInt(line[4]), (int) vars.get(line[2])));
                        types.put(line[2],(short) 1);
                    }
                }
                case "rng" -> {
                    Random rng = new Random();
                    //var int test rng 1 2
                    if (vars.containsKey(line[4])) {
                        if (vars.containsKey(line[5])) {
                            vars.put(line[2], rng.nextInt((int) vars.get(line[5]) - (int) vars.get(line[4])) + (int) vars.get(line[4]));
                            types.put(line[2], (short) 1);
                        } else {
                            vars.put(line[2], rng.nextInt(Integer.parseInt(line[5]) - (int) vars.get(line[4])) + (int) vars.get(line[4]));
                            types.put(line[2], (short) 1);
                        }
                    } else {
                        if (vars.containsKey(line[5])) {
                            vars.put(line[2], rng.nextInt((int) vars.get(line[5]) - Integer.parseInt(line[4])) + Integer.parseInt(line[4]));
                            types.put(line[2], (short) 1);
                        } else {
                            vars.put(line[2], rng.nextInt(Integer.parseInt(line[5]) - Integer.parseInt(line[4])) + Integer.parseInt(line[4]));
                            types.put(line[2], (short) 1);
                        }
                    }
                }
                }
            }
            case "dbl" -> {
                switch (line[3]) {
                case "set" -> {
                    if (vars.containsKey(line[4])) {
                        vars.put(line[2], (double) vars.get(line[4]));
                        types.put(line[2],(short) 2);
                    } else {
                        vars.put(line[2],line[4]);
                        types.put(line[2],(short) 2);
                    }
                }
                case "add" -> {
                    if (vars.containsKey(line[4])) {
                        vars.put(line[2], (double) vars.get(line[4]) + (double) vars.get(line[2]));
                        types.put(line[2],(short) 2);
                    } else {
                        vars.put(line[2], Double.parseDouble(line[4]) + (double) vars.get(line[2]));
                        types.put(line[2],(short) 2);
                    }
                }
                case "sub" -> {
                    if (vars.containsKey(line[4])) {
                        vars.put(line[2], (double) vars.get(line[4]) - (double) vars.get(line[2]));
                        types.put(line[2],(short) 2);
                    } else {
                        vars.put(line[2], Double.parseDouble(line[4]) - (double) vars.get(line[2]));
                        types.put(line[2],(short) 2);
                    }
                }
                case "mlt" -> {
                    if (vars.containsKey(line[4])) {
                        vars.put(line[2], (double) vars.get(line[4]) * (double) vars.get(line[2]));
                        types.put(line[2],(short) 2);
                    } else {
                        vars.put(line[2], Double.parseDouble(line[4]) * (double) vars.get(line[2]));
                        types.put(line[2],(short) 2);
                    }
                }
                case "div" -> {
                    if (vars.containsKey(line[4])) {
                        vars.put(line[2], (double) vars.get(line[4]) / (double) vars.get(line[2]));
                        types.put(line[2],(short) 2);
                    } else {
                        vars.put(line[2], Double.parseDouble(line[4]) / (double) vars.get(line[2]));
                        types.put(line[2],(short) 2);
                    }
                }
                case "mod" -> {
                    if (vars.containsKey(line[4])) {
                        vars.put(line[2], (double) vars.get(line[4]) % (double) vars.get(line[2]));
                        types.put(line[2],(short) 2);
                    } else {
                        vars.put(line[2], Double.parseDouble(line[4]) % (double) vars.get(line[2]));
                        types.put(line[2],(short) 2);
                    }
                }
                case "pow" -> {
                    if (vars.containsKey(line[4])) {
                        vars.put(line[2], Math.pow((double) vars.get(line[4]), (double) vars.get(line[2])));
                        types.put(line[2],(short) 2);
                    } else {
                        vars.put(line[2], Math.pow(Double.parseDouble(line[4]), (double) vars.get(line[2])));
                        types.put(line[2],(short) 2);
                    }
                }
                case "rnd" -> {
                    if (vars.containsKey(line[4])) {
                        vars.put(line[2], Math.round((double) vars.get(line[4])));
                        types.put(line[2],(short) 2);
                    } else {
                        vars.put(line[2], Math.round(Double.parseDouble(line[4])));
                        types.put(line[2],(short) 2);
                    }
                }
            }
            }
            case "typ" -> { //var typ smth int
                switch (line[3]) {
                case "bln" -> {
                    types.put(line[2],(short) 0);
                }
                case "int" -> {
                    types.put(line[2],(short) 1);
                }
                case "dbl" -> {
                    types.put(line[2],(short) 2);
                }
                case "str" -> {
                    types.put(line[2],(short) 3);
                }
                case "arr" -> {
                    types.put(line[2],(short) 4);
                }
                }
            }
            case "str" -> {
                switch (line[3]) {
                case "set" -> {
                    if (vars.containsKey(line[4])) {
                        vars.put(line[2], vars.get(line[4]));
                        types.put(line[2],(short) 3);
                    } else if (line[4].startsWith("\"") && line[4].endsWith("\"")){
                        vars.put(line[2], line[4].subSequence(1, line[4].length()-1));
                        types.put(line[2],(short) 3);
                    }
                }
                case "cct" -> { //var str name cct first last
                    if (vars.containsKey(line[4])) {
                        if (vars.containsKey(line[5])) {
                            vars.put(line[2], ((String) vars.get(line[4])).concat((String) vars.get(line[5])));
                            types.put(line[2],(short) 3);
                        } else {
                            vars.put(line[2], ((String) vars.get(line[4])).concat(line[5].subSequence(1, line[5].length()-1).toString()));
                            types.put(line[2],(short) 3);
                        }
                    } else {
                        if (vars.containsKey(line[5])) {
                            vars.put(line[2], (line[4].subSequence(1, line[4].length()-1).toString()).concat((String) vars.get(line[5])));
                            types.put(line[2],(short) 3);
                        } else {
                            vars.put(line[2], (line[4].subSequence(1, line[4].length()-1).toString()).concat(line[5].subSequence(1, line[5].length()-1).toString()));
                            types.put(line[2],(short) 3);
                        }
                    }
                }
                case "rpl" -> { //var str name rpl old new
                    if (vars.containsKey(line[4])) {
                        if (vars.containsKey(line[5])) {
                            vars.put(line[2], ((String) vars.get(line[2])).replaceAll((String) vars.get(line[4]), (String) vars.get(line[5])));
                            types.put(line[2],(short) 3);
                        } else {
                            vars.put(line[2], ((String) vars.get(line[2])).replaceAll((String) vars.get(line[4]), line[5].subSequence(1, line[5].length()-1).toString()));
                            types.put(line[2],(short) 3);
                        }
                    } else {
                        if (vars.containsKey(line[5])) {
                            vars.put(line[2], ((String) vars.get(line[2])).replaceAll(line[4].subSequence(1, line[4].length()-1).toString(), (String) vars.get(line[5])));
                            types.put(line[2],(short) 3);
                        } else {
                            vars.put(line[2], ((String) vars.get(line[2])).replaceAll(line[4].subSequence(1, line[4].length()-1).toString(), line[5].subSequence(1, line[5].length()-1).toString()));
                            types.put(line[2],(short) 3);
                        }
                    }
                }
                case "len" -> { 
                    if (vars.containsKey(line[4])) {
                        vars.put(line[2], ((String) vars.get(line[4])).length());
                        types.put(line[2],(short) 1);
                    } else {
                        vars.put(line[2], line[4].length()-2);
                        types.put(line[2],(short) 1);
                    }
                }
                case "sub" -> { //var str name sub 1 2
                    if (vars.containsKey(line[4])) {
                        if (vars.containsKey(line[5])) {
                            vars.put(line[2], ((String) vars.get(line[4])).substring((int) vars.get(line[5]), (int) vars.get(line[2])));
                            types.put(line[2],(short) 3);
                        } else {
                            vars.put(line[2], ((String) vars.get(line[4])).substring(Integer.parseInt(line[5]), (int) vars.get(line[2])));
                            types.put(line[2],(short) 3);
                        }
                    } else {
                        if (vars.containsKey(line[5])) {
                            vars.put(line[2], line[4].subSequence(1, line[4].length()-1).toString().substring((int) vars.get(line[5]), (int) vars.get(line[2])));
                            types.put(line[2],(short) 3);
                        } else {
                            vars.put(line[2], line[4].subSequence(1, line[4].length()-1).toString().substring(Integer.parseInt(line[5]), (int) vars.get(line[2])));
                            types.put(line[2],(short) 3);
                        }
                    }
                }
                case "low" -> {
                    if (vars.containsKey(line[4])) {
                        vars.put(line[2], ((String) vars.get(line[4])).toLowerCase());
                        types.put(line[2],(short) 3);
                    } else {
                        vars.put(line[2], line[4].subSequence(1, line[4].length()-1).toString().toLowerCase());
                        types.put(line[2],(short) 3);
                    }
                }
                case "upp" -> {
                    if (vars.containsKey(line[4])) {
                        vars.put(line[2], ((String) vars.get(line[4])).toUpperCase());
                        types.put(line[2],(short) 3);
                    } else {
                        vars.put(line[2], line[4].subSequence(1, line[4].length()-1).toString().toUpperCase());
                        types.put(line[2],(short) 3);
                    }
                }
                }
            }
            case "inp" -> {
                if (vars.containsKey(line[3])) {System.out.println(vars.get(line[3]));}
                else {System.out.println(((String) line[3]).subSequence(1, line[3].length()-1));}
                vars.put(line[2], System.console().readLine());
                types.put(line[2],(short) 3);
            }
            
        }
        linenum++;
        }
        default -> {
            linenum++;
        }
    }
}
}