package Transposer;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Transposer {
    private final int wordLength;
    private final boolean cut;
    private final boolean rightAlign;


    Transposer(int newWordLength, boolean changeCut, boolean changeRightAlign) {
        wordLength = newWordLength;
        cut = changeCut;
        rightAlign = changeRightAlign;
    }

    Transposer(boolean changeCut, boolean changeRightAlign) {
        if (changeCut && changeRightAlign) wordLength = 10;
        else wordLength = 0;
        cut = changeCut;
        rightAlign = changeRightAlign;
    }

    public void transpose(String inputNameFile, String outputNameFile) throws IOException {
        try (FileInputStream inputStream = new FileInputStream((inputNameFile))) {
            try (FileOutputStream outputStream = new FileOutputStream((outputNameFile))) {
                transpose(inputStream, outputStream);
            }
        }
    }

    private static ArrayList<String> filter(ArrayList<String> words) {
        for (String str : words) {
            if (str.equals("")) words.remove(str);
        }
        return words;
    }

    private void transpose(InputStream in, OutputStream out) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out))) {
                int maxLength = 0;
                List<List<String>> text = new LinkedList<>();
                String line = reader.readLine();
                while (line != null) {
                    line = line.trim();
                    String[] words = line.split(" ");
                    ArrayList<String> newLine = filter(new ArrayList<>(Arrays.asList(words)));
                    text.add(newLine);
                    if (newLine.size() > maxLength) maxLength = newLine.size();
                    line = reader.readLine();
                }

                text = normalizeWords(text);
                text = transposeText(text, maxLength);
                if (rightAlign) text = alignToRight(text);
                writeToFile(text, writer);

            }
        }
    }

    private void writeToFile(List<List<String>> text, BufferedWriter writer) throws IOException {
        if (text.isEmpty()) System.out.println();
        for (int i = 0; i < text.size(); i++) {
            List<String> line = text.get(i);
            for (int j = 0; j < line.size(); j++) {
                writer.write(line.get(j));
                if (j < line.size() - 1) writer.write(" ");
            }
            if (i < text.size() - 1) writer.newLine();
        }
    }

    private List<List<String>> alignToRight(List<List<String>> text) {
        int maxLength = text.get(0).size();
        for (int i = 1; i < text.size(); i++) {
            if (text.get(i).size() < maxLength) {
                String spaces = new String(new char[wordLength]).replace('\0', ' ');
                int length = maxLength - text.get(i).size();
                for (int j = 0; j < length; j++) {
                    text.get(i).add(0, spaces);
                }
            }
        }
        return text;
    }

    private static List<List<String>> transposeText(List<List<String>> text, int maxLength) {
        List<List<String>> newText = new LinkedList<>();
        for (int i = 0; i < maxLength; i++) {
            List<String> newLine = new LinkedList<>();
            for (List<String> line : text) {
                if (line.size() > i) newLine.add(line.get(i));
            }
            newText.add(newLine);
        }
        return newText;
    }

    private List<List<String>> normalizeWords(List<List<String>> text) {
        List<List<String>> newText = new LinkedList<>();

        for (List<String> line : text) {
            List<String> newLine = new LinkedList<>();
            for (String word : line) {
                if (word.length() < wordLength) {
                    String spaces = new String(new char[wordLength - word.length()]).replace('\0', ' ');
                    if (rightAlign) newLine.add(spaces + word);
                    else newLine.add(word + spaces);
                } else if (word.length() > wordLength && cut) {
                    newLine.add(word.substring(0, wordLength));
                } else newLine.add(word);
            }
            newText.add(newLine);
        }

        return newText;
    }

}
