import au.com.bytecode.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ClientLog {
    StringBuilder log = new StringBuilder("productNum,amount\n");
    public void log(int productNum, int amount) {
        log.append(productNum).append(",").append(amount).append("\n");

    }
    public void exportAsCSV(File txtFile) throws IOException {
        var writer = new FileWriter(txtFile);
        writer.write(String.valueOf(log));
        writer.close();

//        // в кавычках получается крИветка - фтопку
//        try (CSVWriter writer2 = new CSVWriter(new FileWriter(txtFile))) {
//            writer2.writeNext(new String[]{log.toString()});
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}
