import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**
 *  This class can be used to parse the Bitcoin data set and
 *  create a CSV file containing information about <strong>all</strong> TX inputs.
 *
 *  Each row of the output file has the following structure:
 *  
 *    <timestamp>,<blockId>,<txId>,<address>,<amount>,<prevTxId>,<prevTxOffset>
 *  
 *  @author Matteo Loporchio
 */
public class ExtractInputs {
  public static void main(String[] args) {
    if (args.length < 2) {
      System.err.println("Usage: ExtractInputs <inputFile> <outputFile>");
      System.exit(1);
    }
    String inputFile = args[0],
    outputFile = args[1];
    // Open input and output files.
    try (
      BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile)));
      PrintWriter out = new PrintWriter(outputFile);
    ) {
      // Write CSV header.
      out.println("timestamp,blockId,txId,address,amount,prevTxId,prevTxOffset");
      // Read and process the input file.
      String line = null;
      while ((line = in.readLine()) != null) {
        // Split the line and obtain info and inputs.
        String[] parts = line.split(":"),
        infos = parts[0].split(","),
        inputs = parts[1].split(";");
        // Parse the associated information.
        long timestamp = Long.parseLong(infos[0]);
        long blockId = Long.parseLong(infos[1]);
        long txId = Long.parseLong(infos[2]);
        // Extract inputs from TX data.
        if (!parts[1].equals("")) {
          for (int i = 0; i < inputs.length; i++) {
            String[] inputParts = inputs[i].split(",");
            long address = Long.parseLong(inputParts[0]),
            amount = Long.parseLong(inputParts[1]),
            prevTxId = Long.parseLong(inputParts[2]);
            int prevTxOffset = Integer.parseInt(inputParts[3]);
            // Print the TX input to file.
            out.printf("%d,%d,%d,%d,%d,%d,%d\n", timestamp, blockId,
            txId, address, amount, prevTxId, prevTxOffset);
          }
        }
      }
    }
    catch (Exception e) {
      System.err.printf("Error: %s\n", e.getMessage());
      e.printStackTrace();
      System.exit(1);
    }
  }
}
