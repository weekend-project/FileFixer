import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class FileFixer {

    final static String IL10 = ",IL-10,";
    final static String IL60 = ",IL-60,";
    final static String CLOSER = ",(CL),";
    final static String MINORS = ",MINORS,";
    public static ArrayList<String> list = new ArrayList<>();
    public static ArrayList<String> bullpen = new ArrayList<>();
    public static ArrayList<String> tempArr = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        Scanner reader = new Scanner(System.in);
        System.out.print("Enter file name without extension: ");
        String fileName = reader.nextLine();

        try {
            parseBullpen(fileName);
            parseRemainder();
        } catch (FileNotFoundException e) {
            System.out.println(e + ": Check spelling");
        }
//        for (String s : list)
//            System.out.println(s);
//        for (String s : bullpen)
//            System.out.println(s);
//        System.out.println("exit");
        writeCSV();
    }

    public static void parseBullpen(String fileName) throws IOException {
        String file = "FileRead\\" + fileName + ".csv";
        BufferedReader in = new BufferedReader(new FileReader(file));
        String str;
        int bullpenIndex = 0;
        int catcherIndex = 0;
        while ((str = in.readLine()) != null)
            tempArr.add(str);
        for (int i = 0; i < tempArr.size(); i++) {
            if (tempArr.get(i).startsWith("Bullpen"))
                bullpenIndex = i;
            if (tempArr.get(i).startsWith("Catcher"))
                catcherIndex = i;
        }
        for (int i = bullpenIndex; i < catcherIndex; i++)
            bullpen.add(tempArr.get(i));
        for (int i = bullpenIndex, j = i; i < catcherIndex; i++)
            tempArr.remove(j);
        for (int i = 0; i < bullpen.size(); i++) {
            String s = bullpen.get(i);
            if (i == 0) {
                s = s.replace("B/T,HT,WT,DOB", "BATS,THROWS,OVERALL,H/9,K/9,BB/9,HR/9,STAMINA,VELOCITY,CONTROL,BREAK,FIELDING,BUNTING,REPERTOIRE");
                s = s.replace(",,", ",NUM,");
                bullpen.set(i, s);
            } else if (s.contains("IL-10,") || s.contains("IL-60,") || s.contains("(CL),") || s.contains("MINORS,")) {
                s = removeFirstComma(s);
                s = cleanUpName(s);
                s = removeExcessName(s);
                s = separateBatThrow(s);
                s = removeHeightWeightBday(s);
                bullpen.set(i, s);
            } else {
                s = removeFirstComma(s);
                s = cleanUpName(s);
                s = separateBatThrow(s);
                s = removeHeightWeightBday(s);
                bullpen.set(i, s);
            }
        }
    }

    public static void parseRemainder() {

        for (String str : tempArr) {
            if (str.startsWith("Rotation")) {
                str = str.replace("B/T,HT,WT,DOB", "BATS,THROWS,OVERALL,H/9,K/9,BB/9,HR/9,STAMINA,VELOCITY,CONTROL,BREAK,FIELDING,BUNTING,REPERTOIRE");
                str = str.replace(",,", ",NUM,");
                list.add(str);
            } else if (str.startsWith("Catcher")
                    || str.startsWith("First Base")
                    || str.startsWith("Second Base")
                    || str.startsWith("Third Base")
                    || str.startsWith("Shortstop")
                    || str.startsWith("Left Field")
                    || str.startsWith("Center Field")
                    || str.startsWith("Right Field")
                    || str.startsWith("Designated Hitter")) {
                str = str.replace("B/T,HT,WT,DOB", "BATS,THROWS,OVERALL,CONTACT R,CONTACT L,POWER R,POWER L,VISION,FIELDING,ARM STR,REACTION,SPEED,STEALING");
                str = str.replace(",,", ",NUM,");
                list.add(str);
            } else if (str.contains("IL-10,") || str.contains("IL-60,") || str.contains("(CL),") || str.contains("MINORS,")) {
                str = removeFirstComma(str);
                str = cleanUpName(str);
                str = removeExcessName(str);
                str = separateBatThrow(str);
                str = removeHeightWeightBday(str);
                list.add(str);
            } else {
                str = removeFirstComma(str);
                str = cleanUpName(str);
                str = separateBatThrow(str);
                str = removeHeightWeightBday(str);
                list.add(str);
            }
        }
    }

    public static void writeCSV() {
        try {
            File file = new File("FileWrite\\boston red sox.csv");
            if (file.createNewFile()) {
                System.out.println("File created: " + file.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred while creating a new file within writeCSV().");
            e.printStackTrace();
        }
        try {
            FileWriter writer = new FileWriter("FileWrite\\boston red sox.csv");
            for (String s : list) {
                writer.write(s);
                writer.write("\r\n");
            }
            for (String s : bullpen) {
                writer.write(s);
                writer.write("\r\n");
            }
            writer.write("exit");
            writer.close();
            System.out.println("Successfully wrote to the file and closed file.");
        } catch (IOException e) {
            System.out.println("An error occurred while writing to a new file within writeCSV().");
            e.printStackTrace();
        }
    }

    public static String removeHeightWeightBday(String str) {
        int start = str.indexOf(",\"");
        if (str.charAt(start + 1) == ',')
            return str.substring(0, start - 1);
        return str.substring(0, start + 1);
    }

    public static String separateBatThrow(String str) {
        return str.replace("/", ",");
    }

    public static String removeFirstComma(String str) {
        char[] tempList = str.toCharArray();
        ArrayList<Character> charList = new ArrayList<>();
        for (Character c : tempList)
            charList.add(c);
        charList.remove(0);
        StringBuilder builder = new StringBuilder();
        for (Character c : charList)
            builder.append(c);
        return builder.toString();
    }

    public static String cleanUpName(String str) {
        char[] tempList = str.toCharArray();
        ArrayList<Character> charList = new ArrayList<>();
        for (Character c : tempList)
            charList.add(c);
        int commaCounter = 0;
        int i = 0;
        do {
            if (Character.isSpaceChar(charList.get(i))
                    && (Character.isDigit(charList.get(i + 1))
                    && (Character.isDigit(charList.get(i + 2))))) {
                charList.set(i, ',');
                commaCounter++;
                charList.set((i + 3), ',');
//                int j = i + 3;
//                while (!charList.get(j).equals(',')) {
//                    charList.remove(j); // what does this even do?
//                    j++;
//                }
            } else if (charList.get(i + 2) == ',') {
                charList.set(i, ',');
                commaCounter++;
            } else
                i++;
        } while (commaCounter < 1);
        StringBuilder builder = new StringBuilder();
        for (Character c : charList)
            builder.append(c);
        return builder.toString();
    }

    public static String removeExcessName(String str) {
        String processedString = "";
        if (str.contains(IL60))
            processedString = str.replace(IL60, ",");
        else if (str.contains(IL10))
            processedString = str.replace(IL10, ",");
        else if (str.contains(CLOSER))
            processedString = str.replace(CLOSER, ",");
        else if (str.contains(MINORS))
            processedString = str.replace(MINORS, ",");
        return processedString;
    }
}


