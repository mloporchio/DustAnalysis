import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.Sets;

/**
 * This program reads the Bitcoin data set and writes to a file
 * only those transactions that do not contain any Satoshi Dice address.
 * 
 * The program can be configured to ignore all transactions:
 * 
 * - with at least one Satoshi Dice address in the inputs;
 * - with at least one Satoshi Dice address in the outputs;
 * - with at least one Satoshi Dice address in the inputs AND in the outputs;
 * 
 * @author Matteo Loporchio
 */
public class FilterSD {

    // This is the set of all known Satoshi Dice addresses.
    public static final Set<Long> satoshiAddresses = Sets.newHashSet(
        3517234L,
        3517367L,
        3517636L,
        3521428L,
        3523985L,
        3524166L,
        3524243L,
        3524394L,
        3524641L,
        3525822L,
        3525858L,
        3526385L,
        3526418L,
        3532515L,
        3534212L,
        3534955L,
        3535501L,
        3541441L,
        3542581L,
        3545893L,
        3552405L,
        3557467L,
        3572642L,
        3577280L,
        5028176L,
        5028570L,
        5029468L
    );

    public static void main(String[] args) {
        // Read and parse arguments from the command line.
        if (args.length < 3) {
            System.err.println("Usage: FilterSD <inputFile> <outputFile> <mode>");
            System.exit(1);
        }
        String inputFile = args[0],
        outputFile = args[1],
        mode = args[2];
        if (!mode.equalsIgnoreCase("input") && !mode.equalsIgnoreCase("output") && 
        !mode.equalsIgnoreCase("both")) {
            System.err.println("Error: <mode> can only be \"input\", \"output\" or \"both\".");
            System.exit(1);
        }
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
                Set<Long> inputAddresses = new HashSet<>();
                if (!parts[1].equals("")) {
                    for (int offset = 0; offset < inputs.length; offset++) {
                        String[] inputParts = inputs[offset].split(",");
                        inputAddresses.add(Long.parseLong(inputParts[0]));
                    }
                }
                // Do the same thing for the TX outputs.
                Set<Long> outputAddresses = new HashSet<>();
                if (!parts[2].equals("")) {
                    for (int offset = 0; offset < outputs.length; offset++) {
                        String[] outputParts = outputs[offset].split(",");
                        outputAddresses.add(Long.parseLong(outputParts[0]));
                    }
                }
                Set<Long> commonInputAddresses = Sets.intersection(inputAddresses, satoshiAddresses),
                commonOutputAddresses = Sets.intersection(outputAddresses, satoshiAddresses);
                boolean noSDinputs = commonInputAddresses.isEmpty(), 
                noSDoutputs = commonOutputAddresses.isEmpty();
                // Write the TX to the output file only if it matches the desired condition.
                if (mode.equalsIgnoreCase("input") && noSDinputs) out.println(line);
                if (mode.equalsIgnoreCase("output") && noSDoutputs) out.println(line);
                if (mode.equalsIgnoreCase("both") && noSDinputs && noSDoutputs) out.println(line);
            }
        }
        catch (Exception e) {
            System.err.printf("Error: %s\n", e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}