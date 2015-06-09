import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by SeongYong on 2015-06-09.
 */
public class LogCorrector {
    private int segCount = 14;

    public void setSegCount(int segCount) {
        if (segCount > 0)
            this.segCount = segCount;
    }

    private void correctStep1(String fileName) {
        try {
            ArrayList<String> strList = new ArrayList<>();
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line = null;
            while ( (line = reader.readLine()) != null ) {
                if (!line.equals("")) {
                    strList.add(line);
                }
            }
            reader.close();
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
            for (String s : strList) {
                writer.write(s + "\n");
            }
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
/*
    private void correctStep2New(String fileName) {
        try {
            File file = new File(fileName);
            BufferedReader reader = new BufferedReader(new FileReader(file));

            String line = null;
            int count = 0;
            char[] chs;
            while ( (line = reader.readLine()) != null ) {
                chs = line.
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    private void correctStep2(String fileName) {
        try {
            File file = new File(fileName);
            BufferedReader reader = new BufferedReader(new FileReader(file));

            char[] buf = new char[(int)file.length()];
            reader.read(buf);
            reader.close();

            int count = 0;
            for (int i = 0; i < buf.length; ++i) {
                if (buf[i] == '|') {
                    ++count;
                } else if (buf[i] == '\n') {
                    if (count == segCount - 1)
                        count = 0;
                    else
                        buf[i] = ' ';
                }
            }

            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            String str = new String(buf);
            writer.write(str);
            writer.close();

            RandomAccessFile f = new RandomAccessFile(fileName, "rw");
            long length = f.length() - 1;
            byte b = 0;
            do {
                length -= 1;
                f.seek(length);
                b = f.readByte();
            } while(b != 10);
            f.setLength(length+1);
            f.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void correctFile(String fileName) {
        correctStep1(fileName);
        correctStep2(fileName);
    }

    public void correctFiles(ArrayList<String> fileNames) {
        for (String fileName : fileNames)
            correctFile(fileName);
    }

    public static void main(String[] args) throws Exception{
        LogCorrector corrector = new LogCorrector();
        ArrayList<String> fileNames = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-M-d");
        Date startDate = dateFormat.parse("2015-5-1");
        Date endDate = dateFormat.parse("2015-6-5");
        for (; startDate.compareTo(endDate) <= 0; startDate.setTime(startDate.getTime() + 24 * 60 * 60 * 1000) ) {
            fileNames.add("Gmail\\" + dateFormat.format(startDate) + ".dat");
        }
        corrector.correctFiles(fileNames);
        return;
    }
}
