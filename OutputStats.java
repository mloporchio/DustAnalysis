import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

/**
 * This program reads a list of Bitcoin transactions and computes, 
 * for each of them, statistics about their outputs.
 * In particular, for each transaction we compute the following:
 * 
 * 1) total number of outputs;
 * 2) number of dust outputs (within the range [1, 545]);
 * 3) number of unique output addresses
 * 
 * @author Matteo Loporchio
 */
public class OutputStats {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Usage: OutputStats <inputFile> <outputFile>");
            System.exit(1);
        }
        String inputFile = args[0], outputFile = args[1];
        // Open the input and output files.
        try (
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile)));
            PrintWriter out = new PrintWriter(outputFile);
        ) {
            out.println("txId,numOut,numDustOut,numUniqueOutAddr");
            String line = null;
            while ((line = in.readLine()) != null) {
                // Split the line and obtain info and outputs.
                String[] parts = line.split(":"),
                infos = parts[0].split(","),
                outputs = parts[2].split(";");
                // Obtain the TX identifier.
                long txId = Long.parseLong(infos[2]);
                // Otherwise, we need to examine the output addresses.
                int numOut = 0, numDustOut = 0;
                Set<Long> outputAddresses = new HashSet<>();
                if (!parts[2].equals("")) {
                    numOut = outputs.length;
                    // For each TX output...
                    for (int offset = 0; offset < outputs.length; offset++) {
                        String[] outputParts = outputs[offset].split(",");
                        // ...obtain the corresponding address and amount.
                        long address = Long.parseLong(outputParts[0]);
                        long amount = Long.parseLong(outputParts[1]);
                        outputAddresses.add(address);
                        // Check if it is within the dust range.
                        if (Dust.isDust(amount)) numDustOut++;
                    }
                }
                out.printf("%d,%d,%d,%d\n", txId, numOut, numDustOut, outputAddresses.size());
            }
        }
        catch (Exception e) {
            System.err.printf("Error: %s\n", e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}