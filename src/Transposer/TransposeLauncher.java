package Transposer;

import org.kohsuke.args4j.*;

import java.io.*;
import java.util.Scanner;

public class TransposeLauncher {
    private boolean writeToCMD = false;
    private void launch(String[] args) {
        CmdLineParser parser = new CmdLineParser(this);
        try {
            parser.parseArgument(args);
            if (wordLength <= 0 && cut) throw new CmdLineException("word length is always zero");
            if (outputFileName == null) {
                outputFileName = "files/outputFileName.txt";
                writeToCMD = true;
            }

            if (inputFileName == null) {
                inputFileName =  "files/inputFileName.txt";
                Scanner sc = new Scanner(System.in);
                try (FileOutputStream outputStream = new FileOutputStream((outputFileName))) {
                    readCmdToFile(sc, outputStream);
                } catch (IOException e) {
                    System.err.println(e.getMessage());
                }
            }

        } catch (CmdLineException e) {
            System.err.println(e.getMessage());
            System.err.println("java -jar transpose.jar [-a num] [-t] [-r] [-o outputFileName] [InputFile]");
            parser.printUsage(System.err);
            return;
        }
        Transposer transposer;
        if (wordLength == 0) transposer = new Transposer(cut, rightAlign);
        else transposer = new Transposer(wordLength, cut, rightAlign);
        try {
            transposer.transpose(inputFileName, outputFileName);
            if (writeToCMD) writeToCmd();
        } catch (IOException  e) {
            System.err.println(e.getMessage());
        }
    }

    @Option(name = "-a", metaVar = "num")
    private int wordLength;
    @Option(name = "-t")
    private boolean cut;
    @Option(name = "-r")
    private boolean rightAlign;
    @Option(name = "-o", metaVar = "oFile")
    private String outputFileName;
    @Argument()
    private String inputFileName;

    public static void main(String[] args) {
        new TransposeLauncher().launch(args);
    }

    private static void readCmdToFile(Scanner sc, OutputStream out) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out))) {
            int lineLength = 0;
            String line;
            while ((line = sc.nextLine()).length() > 0) {
                if (lineLength != 0) {
                    writer.write("\n");
                }
                if (line.length() > lineLength) lineLength = line.length();
                writer.write(line);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void writeToCmd() throws IOException {
        try (FileInputStream inputStream = new FileInputStream(("files/outputFileName.txt"))) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            }
        }
    }
}
