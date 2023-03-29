import au.com.bytecode.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ClientLog {
    StringBuilder log = new StringBuilder("productNum,amount\n");

    public void log(int productNum, int amount) {
        log.append(productNum).append(",").append(amount).append("\n");

    }

    // CSVWriter - отказ из-за кавычек
    public void exportAsCSV(File txtFile) throws IOException {
        var writer = new FileWriter(txtFile);
        writer.write(String.valueOf(log));
        writer.close();
    }
}
