import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Set;

/**
 * This program reads a list of Bitcoin transactions and
 * removes all transactions whose identifier does not appear in a given list.
 * The output of the program is a new list that contains all original transactions
 * except those with the given identifiers.
 * 
 * @author Matteo Loporchio
 */
public class RemoveById {
    public static void main(String[] args) {
        // Read and parse arguments from the command line.
        if (args.length < 3) {
            System.err.println("Usage: RemoveById <inputFile> <idFile> <outputFile>");
            System.exit(1);
        }
        String inputFile = args[0], idFile = args[1], outputFile = args[2];
        // Open the input and output files.
        try (
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile)));
            PrintWriter out = new PrintWriter(outputFile);
        ) {
            // Load the list of identifiers.
            Set<String> idList = Utilities.readTxIdList(idFile);
            // For each TX (= line) in the data set...
            String line = null;
            while ((line = in.readLine()) != null) {
                String[] parts = line.split(":");
                String[] infos = parts[0].split(",");
                // Check if the TX identifier is not in the list.
                // If this is the case, write the TX to the output file.
                String txId = infos[2];
                if (!idList.contains(txId)) out.println(line);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }    
    }
}