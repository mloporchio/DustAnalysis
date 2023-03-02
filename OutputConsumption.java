import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * This program reads a list of Bitcoin transactions and computes
 * the duration of all spent outputs of these transactions. 
 * The duration is defined as the difference between 
 * the block identifier where the output is spent (i.e., where it appears as an input)
 * and the block identifier where it was created. 
 * 
 * The output of this program is a CSV file where each row contains
 * the output amount and its duration.
 * 
 * @author Matteo Loporchio
 */
public class OutputConsumption {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Usage: OutputConsumption <inputFile> <outputFile>");
            System.exit(1);
        }
        String inputFile = args[0], 
        outputFile = args[1];
        // Open the input and output files.
        try (
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile)));
            PrintWriter out = new PrintWriter(outputFile);
        ) {
            // Initialize the UTXO set. 
            // We represent it as a map (output, blockId) where each output
            // is associated with the corresponding block identifier.
            Map<Output, Long> utxo = new HashMap<>();
            // Write CSV header to the results file.
            out.printf("amount,duration\n");
            // Read the input file line by line.
            String line = null;
            while ((line = in.readLine()) != null) {
                // Split the line and obtain info, inputs and outputs.
                String[] parts = line.split(":"),
                infos = parts[0].split(","),
                inputs = parts[1].split(";"),
                outputs = parts[2].split(";");
                // Parse basic TX information.
                long blockId = Long.parseLong(infos[1]);
                long txId = Long.parseLong(infos[2]);
                // We first examine the inputs of the transaction.
                if (!parts[1].equals("")) {
                    // For each input...
                    for (int offset = 0; offset < inputs.length; offset++) {
                        String[] inputParts = inputs[offset].split(",");
                        long amount = Long.parseLong(inputParts[1]);
                        long prevTxId = Long.parseLong(inputParts[2]);
                        int prevTxOffset = Integer.parseInt(inputParts[3]);
                        // We check if the corresponding output was in the UTXO set.
                        // If this is the case, we remove it from the set and 
                        // then print a line on the output file.
                        Output txOut = new Output(prevTxId, prevTxOffset);
                        Long prevBlockId = utxo.get(txOut);
                        if (prevBlockId != null) {
                            out.printf("%d,%d\n", amount, blockId-prevBlockId);
                            utxo.remove(txOut);
                        }
                    }
                }
                // Then we examine the outputs. Each output of the transaction
                // is inserted in the UTXO map, where it is associated with the
                // identifier of the block it appears in.
                if (!parts[2].equals("")) {
                    for (int offset = 0; offset < outputs.length; offset++) {
                        Output key = new Output(txId, offset);
                        utxo.put(key, blockId);
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}