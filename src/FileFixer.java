import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileFixer {

    public static void main(String[] args) throws IOException {
        parseCSV("Boston Red Sox");
        System.out.println();
    }

    public static ArrayList<String> parseCSV(String fileName) throws IOException {
        String file = "FileIn\\" + fileName + ".csv";
        BufferedReader in = new BufferedReader(new FileReader(file));
        String str;

        ArrayList<String> list = new ArrayList<>();
        while ((str = in.readLine()) != null) {
            if (str.startsWith("Rotation") || str.startsWith("Bullpen")) {
                str.concat(",OVERALL,H/9,K/9,BB/9,HR/9,STAMINA,VELOCITY,CONTROL,BREAK,FIELDING,BUNTING,REPERTOIRE");
                list.add(str);
            } else {
                removeFirstCommas(str);
            }
        }
        return list;
    }

    public static void removeFirstCommas(String str) {
        char[] tempList = str.toCharArray();
        ArrayList<Character> charList = new ArrayList<>();
        for (Character c : tempList)
            charList.add(c);
        charList.remove(0); // removes first comma
        int commaCounter = 0;
        int i = 0;
        int indexTracker;
        do {
            if (charList.get(i) == ' ' && (charList.get(i + 1) == '1'
                    || charList.get(i + 1) == '2'
                    || charList.get(i + 1) == '3'
                    || charList.get(i + 1) == '4'
                    || charList.get(i + 1) == '5'
                    || charList.get(i + 1) == '6'
                    || charList.get(i + 1) == '7'
                    || charList.get(i + 1) == '8'
                    || charList.get(i + 1) == '9')) {
                charList.set(i, ',');
                commaCounter++;
                indexTracker = i;
                charList.set((indexTracker + 3), ',');
                int j = i+3;
                while (!charList.get(j).equals(',')) {
                    charList.remove(j);
                    j++;
                }
            } else
                i++;
        } while (commaCounter < 1);
        for (Character c : charList)
            System.out.print(c);
    }

}
