package Utility;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static Resources.ImgurValues.CANT_FIND_AUTH_FILE;


/**
 * Helper method for reading and writing to-from files.
 */
public class FileHelper
{
    public FileHelper() { }

    /**
     * Retrieves the content from the designated file.
     * @param filePath A path to the file to be read.
     * @return A Reader object for streaming the content of the file.
     */
    public Reader getFileContent(Path filePath)
    {
        if (!isValidFilePath(filePath))
        {
            System.err.println(CANT_FIND_AUTH_FILE);
            return null;
        }

        try
        {
            return Files.newBufferedReader(filePath, StandardCharsets.UTF_8);
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Determines whether or not the given file path is for an existing file.
     * @param filePath The file path to be tested.
     * @return True if the file exists and the path isn't null, false if not.
     */
    public boolean isValidFilePath(Path filePath)
    {
        return (filePath != null || filePath.toFile().exists());
    }
}
