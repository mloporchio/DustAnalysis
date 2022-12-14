import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

/**
 * A Java class that can be used to classify the transactions in the Bitcoin data set.
 * More precisely, we divide all transactions in 4 different groups.
 * 
 * 1) Type 1: TXs with exactly 1 unique input address.
 * 2) Type 2+: TXs with at least 2 unique input addresses.
 * 3) Type C: coinbase TXs (with exactly 0 input addresses).
 * 4) Type S (Special): TXs whose fee is equal to the sum of all input amounts.
 * 
 * @author Matteo Loporchio
 */
public class Classify {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Usage: Classify <inputFile> <outputFile>");
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
                // Split the line and obtain info, inputs and outputs.
                String[] parts = line.split(":"),
                infos = parts[0].split(","),
                inputs = parts[1].split(";"),
                outputs = parts[2].split(";");
                // Parse basic TX information.
                // long timestamp = Long.parseLong(infos[0]);
                long blockId = Long.parseLong(infos[1]);
                long txId = Long.parseLong(infos[2]);
                // int isCoinbase = Integer.parseInt(infos[3]);
                // First we examine the inputs. We compute the sum of all
                // input amounts and collect all input addresses in a set.
                long inputSum = 0;
                Set<Long> inputAddresses = new HashSet<>();
                if (!parts[1].equals("")) {
                    for (int offset = 0; offset < inputs.length; offset++) {
                        String[] inputParts = inputs[offset].split(",");
                        inputAddresses.add(Long.parseLong(inputParts[0]));
                        inputSum += Long.parseLong(inputParts[1]);
                    }
                }
                // Then we examine the outputs. We simply sum all output amounts.
                long outputSum = 0;
                if (!parts[2].equals("")) {
                    for (int offset = 0; offset < outputs.length; offset++) {
                        String[] outputParts = outputs[offset].split(",");
                        outputSum += Long.parseLong(outputParts[1]);
                    }
                }
                // We classify the current transaction.
                // The transaction fee is the difference between the sum
                // of all inputs and the sum of all TX outputs.
                long fee = inputSum - outputSum;
                String txType;
                // Special type: fee is equal to the sum of all input amounts.
                if (fee == inputSum) txType = "S";
                // Type 2+: at least 2 unique input addresses.
                else if (inputAddresses.size() >= 2) txType = "2+";
                // Type 1: exactly 1 unique input address.
                else if (inputAddresses.size() == 1) txType = "1";
                // Type C: coinbase transactions with 0 input addresses.
                else txType = "C";
                out.printf("%d,%d,%s\n", txId, blockId, txType);
            }
        }
        catch (Exception e) {
            System.err.printf("Error: %s\n", e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
