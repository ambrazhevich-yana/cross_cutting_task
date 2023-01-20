import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.*;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        IU calculator = new IU("");
        calculator.pack();
        calculator.setSize(600,300);
        calculator .setLocationRelativeTo(null);
        calculator.setVisible(true);
    }
}

public class IU extends JFrame {

    private ArrayList<String> strings;
    private Strategy method;
    private final JTextArea input;
    private final JTextArea output;

    IU(String name) {
        super(name);
        strings = new ArrayList<>();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        input = new JTextArea();
        add(input);
        input.setEditable(false);

        JButton read = new JButton("READ");
        add(read);

        JButton write = new JButton("WRITE");
        add(write);

        JRadioButton txt = new JRadioButton(".txt");
        add(txt);
        txt.setSelected(true);
        JRadioButton xml = new JRadioButton(".xml");
         add(xml);
        JRadioButton json = new JRadioButton(".json");
        add(json);


        output = new JTextArea();
        add(output);
        output.setEditable(false);

        read.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser(".");
            try {
                if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    if (fileChooser.getSelectedFile().getName().endsWith("txt")) {
                        setStrategy(new Text());
                        //   } else if (fileChooser.getSelectedFile().getName().endsWith("xml")) {
                        //    setStrategy(new XML());
                        //} else if (fileChooser.getSelectedFile().getName().endsWith("json")) {
                        //    setStrategy(new JSON());
                        //
                    } else throw new IllegalArgumentException();
                    strings = method.getStringArray(fileChooser.getSelectedFile());
                }
            } catch (IllegalArgumentException exception) {
                JOptionPane.showMessageDialog(null, "Error", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (IOException | ParserConfigurationException | SAXException exception) {
                JOptionPane.showMessageDialog(null, exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
            showInput();
            solve();
            showOutput();
        });

        write.addActionListener(e -> {
            try {
                String fileName = JOptionPane.showInputDialog("Enter output file name");
                if (fileName.isEmpty()) throw new IllegalArgumentException("!");
                if (txt.isSelected()) {
                    writeTXT(fileName + ".txt");
                }// else if (xml.isSelected()){
                //     writeToXML(fileName + ".xml");
                // }
                // else if (json.isSelected()){
                //     writeToJSON(fileName + ".json");
                // }
            } catch (IOException |
                     IllegalArgumentException exception) {
                JOptionPane.showMessageDialog(null, exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void writeTXT(String fileName) throws IOException {
        FileWriter fileWriter = new FileWriter(fileName);
        for (String string : strings) {
            fileWriter.write(string);
            fileWriter.write("\n");
        }
        fileWriter.close();
    }

    private void writeToXML(String fileName) {
      //
    }

    private void writeToJSON(String fileName) {
        //
    }

    private void solve() {
        Pattern p = Pattern.compile("(\\s*-?\\s*(\\(*\\s*(-?\\s*\\d+(\\.\\d+)?)\\s*\\)*\\s*)*" +
                "[-+*/^](\\s*\\(*\\s*(-?\\s*\\d+(\\.\\d+)?)\\s*\\)*\\s*)+)+");
        String oldSubstring;
        String newSubstring;
        for (int i = 0; i < strings.size(); i++) {
            Matcher matcher = p.matcher(strings.get(i));
            while (matcher.find()) {
                oldSubstring = strings.get(i).substring(matcher.start(), matcher.end());
                newSubstring = Calculator.calculate(oldSubstring);
                strings.set(i, strings.get(i).replace(oldSubstring, newSubstring));
            }
        }

    }

    private void setStrategy(Strategy handlingStrategy) {
        method = handlingStrategy;
    }

    private void showInput() {
        StringBuilder stringBuilder = new StringBuilder();
        for (String string : strings) {
            stringBuilder.append(string).append("\n");
        }
        input.setText(stringBuilder.toString());
    }

    private void showOutput() {
        StringBuilder stringBuilder = new StringBuilder();
        for (String string : strings) {
            stringBuilder.append(string).append("\n");
        }
        output.setText(stringBuilder.toString());
    }
}

