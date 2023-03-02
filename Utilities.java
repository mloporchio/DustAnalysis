import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

/**
 * This class contains a set of utility methods used by other classes.
 * @author Matteo Loporchio
 */
public final class Utilities {

    /**
     * Reads a set of transaction identifiers from a given file.
     * @param filename path of the file
     * @return a set containing all transaction identifiers
     */
    public static Set<String> readTxIdList(String filename) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
        Set<String> result = new HashSet<>();
        String line = null;
        while ((line = in.readLine()) != null) result.add(line);
        in.close();
        return result;
    }
}
