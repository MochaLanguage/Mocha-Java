import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
public class Mocha {
    public static void main(String[] args) throws Exception {
        File mochafile = new File("");
        Scanner myReader = new Scanner(mochafile);
        while (myReader.hasNextLine()) {
            String data = myReader.nextLine();
        }
        myReader.close();
    }
    public String readFile(File file, Charset charset) throws IOException {
        return new String(Files.readAllBytes(file.toPath()), charset);
    }
}
