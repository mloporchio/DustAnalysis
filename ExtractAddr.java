import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Set;
import java.util.TreeSet;

/**
 * This program reads a list of Bitcoin transactions and extracts 
 * all their addresses (i.e., those contained in both inputs and outputs). 
 * The addresses are written to an output file in text format, one by line.
 * 
 * @author Matteo Loporchio
 */
public class ExtractAddr {
    public static final Set<Long> addresses = new TreeSet<>();
    public static void main(String[] args) {
        // Read and parse arguments from the command line.
        if (args.length < 2) {
            System.err.println("Usage: ExtractAddr <inputFile> <outputFile>");
            System.exit(1);
        }
        String inputFile = args[0],
        outputFile = args[1];
        // Open the input and output files.
        try (
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile)));
            PrintWriter out = new PrintWriter(outputFile);
        ) {
            String line = null;
            while ((line = in.readLine()) != null) {
                // Split the line and obtain inputs and outputs.
                String[] parts = line.split(":"),
                inputs = parts[1].split(";"),
                outputs = parts[2].split(";");
                // Examine the TX inputs and collect their addresses.
                if (!parts[1].equals("")) {
                    for (int offset = 0; offset < inputs.length; offset++) {
                        String[] inputParts = inputs[offset].split(",");
                        addresses.add(Long.parseLong(inputParts[0]));
                    }
                }
                // Do the same thing for the TX outputs.
                if (!parts[2].equals("")) {
                    for (int offset = 0; offset < outputs.length; offset++) {
                        String[] outputParts = outputs[offset].split(",");
                        addresses.add(Long.parseLong(outputParts[0]));
                    }
                }
            }
            for (long address : addresses) out.println(address);
        }
        catch (Exception e) {
            System.err.printf("Error: %s\n", e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}