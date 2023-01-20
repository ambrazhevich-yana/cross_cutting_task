import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public interface Strategy {
    ArrayList<String> getStringArray(File file) throws IOException, ParserConfigurationException, SAXException;
}
class Text implements Strategy {
    @Override
    public ArrayList<String> getStringArray(File file) throws FileNotFoundException {
        ArrayList<String> text = new ArrayList<>();
        Scanner scanner = new Scanner(file);
        while (scanner.hasNext()) {
            text.add(scanner.nextLine());
        }
        return text;
    }
}

class XML implements Strategy {
    @Override
    public ArrayList<String> getStringArray(File file) {
        ArrayList<String> text = new ArrayList<>();
       //
        return text;
    }
}

class JSON implements Strategy {
    @Override
    public ArrayList<String> getStringArray(File file) {
        ArrayList<String> text = new ArrayList<>();
        //
        return text;
    }
}