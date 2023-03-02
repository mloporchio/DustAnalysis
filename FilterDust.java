import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**
 * This program reads a list of Bitcoin transactions and
 * extracts all dust-creating and dust-consuming transactions it contains.
 * We define dust-creating (resp. dust-consuming) transactions 
 * as those containing at least one dust output (resp. input).
 * 
 * @author Matteo Loporchio
 */
public class FilterDust {
    public static void main(String[] args) {
        // Read and parse arguments from the command line.
        if (args.length < 2) {
            System.err.println("Usage: FilterDust <inputFile> <outputFile>");
            System.exit(1);
        }
        String inputFile = args[0], outputFile = args[1];
        // Open the input and output files.
        try (
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile)));
            PrintWriter out = new PrintWriter(outputFile);
        ) {
            String line = null;
            while ((line = in.readLine()) != null) {
                int inputCount = 0, outputCount = 0;
                // Split the line and obtain info and inputs.
                String[] parts = line.split(":"),
                //infos = parts[0].split(","),
                inputs = parts[1].split(";"),
                outputs = parts[2].split(";");
                // Examine the inputs and count how many of them have an amount
                // that is included in the range.
                if (!parts[1].equals("")) {
                    for (int offset = 0; offset < inputs.length; offset++) {
                        String[] inputParts = inputs[offset].split(",");
                        long amount = Long.parseLong(inputParts[1]);
                        if (Dust.isDust(amount)) inputCount++;
                    }
                }
                // Do the same thing for the TX outputs.
                if (!parts[2].equals("")) {
                    for (int offset = 0; offset < outputs.length; offset++) {
                        String[] outputParts = outputs[offset].split(",");
                        long amount = Long.parseLong(outputParts[1]);
                        if (Dust.isDust(amount)) outputCount++;
                    }
                }
                // Write the TX to the output file only if it has at least
                // one input and/or output within the given range.
                if (inputCount > 0 || outputCount > 0) out.println(line);
            }
        }
        catch (Exception e) {
            System.err.printf("Error: %s\n", e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}