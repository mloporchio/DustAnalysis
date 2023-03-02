import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

/**
 * This class that can be used to classify transactions in the Bitcoin data set.
 * More precisely, we divide all transactions in 4 different groups.
 * 
 * 1) Type 1: TXs with exactly 1 unique input address.
 * 2) Type 2+: TXs with at least 2 unique input addresses.
 * 3) Type C: coinbase TXs (with exactly 0 input addresses).
 * 4) Type S (Special): TXs whose fee is equal to the sum of all input amounts.
 * 
 * The output of this program is a text file where each line corresponds
 * to a transaction. For each transaction we compute:
 * 
 * 1) total number of inputs;
 * 2) number of dust inputs (i.e., within the range [1, 545]);
 * 3) number of unique input addresses;
 * 4) transaction type, in accordance with the classification.
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
            out.println("txId,numIn,numDustIn,numUniqueInAddr,txType");
            String line = null;
            while ((line = in.readLine()) != null) {
                // Split the line and obtain info, inputs and outputs.
                String[] parts = line.split(":"),
                infos = parts[0].split(","),
                inputs = parts[1].split(";"),
                outputs = parts[2].split(";");
                // Parse basic TX information.
                // long timestamp = Long.parseLong(infos[0]);
                // long blockId = Long.parseLong(infos[1]);
                long txId = Long.parseLong(infos[2]);
                // int isCoinbase = Integer.parseInt(infos[3]);
                // First we examine the inputs. We compute the sum of all
                // input amounts and collect all input addresses in a set.
                int numInputs = 0, numDustInputs = 0; 
                long inputSum = 0;
                Set<Long> inputAddresses = new HashSet<>();
                if (!parts[1].equals("")) {
                    numInputs = inputs.length;
                    for (int offset = 0; offset < inputs.length; offset++) {
                        String[] inputParts = inputs[offset].split(",");
                        long address = Long.parseLong(inputParts[0]);
                        long amount = Long.parseLong(inputParts[1]);
                        inputAddresses.add(address);
                        if (Dust.isDust(amount)) numDustInputs++;
                        inputSum += amount;
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
                // We first compute the number of distinct input addresses.
                int numUniqueInAddr = inputAddresses.size();
                // The transaction fee is the difference between the sum
                // of all inputs and the sum of all TX outputs.
                long fee = inputSum - outputSum;
                String txType;
                // Special type: fee is equal to the sum of all input amounts.
                if (fee == inputSum) txType = "S";
                // Type 2+: at least 2 unique input addresses.
                else if (numUniqueInAddr >= 2) txType = "2+";
                // Type 1: exactly 1 unique input address.
                else if (numUniqueInAddr == 1) txType = "1";
                // Type C: coinbase transactions with 0 input addresses.
                else txType = "C";
                out.printf("%d,%d,%d,%d,%s\n", txId, numInputs, numDustInputs, numUniqueInAddr, txType);
            }
        }
        catch (Exception e) {
            System.err.printf("Error: %s\n", e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
