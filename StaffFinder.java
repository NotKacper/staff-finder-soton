import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Objects;

public class StaffFinder {
    public static void main(String[] args) throws IOException {
        boolean retry = true;
        InputStreamReader inputReader;
        BufferedReader bufferedInputReader;
        while (retry) {
            StaffFinder staffFinder = new StaffFinder();
            String name;
            try {
                name = staffFinder.getName();
                System.out.println(name);
                retry = false;
            }
            catch (Exception IOException) {
                System.out.println("This person could not be found. --- :(");
                System.out.println("Would you like to search again? y/n");
                inputReader = new InputStreamReader(System.in);
                bufferedInputReader = new BufferedReader(inputReader);
                if (Objects.equals(bufferedInputReader.readLine().toLowerCase(), "n")) {
                    retry = false;
                }
            }
        }
    }

    public String getName() throws IOException {
        String input = this.getUserInput();
        URLConnection urlConnection = getUrlConnection(input);
        InputStreamReader urlInputStreamReader = new InputStreamReader(urlConnection.getInputStream());
        String currentLine = getTargetHtmlLine(urlInputStreamReader);
        int indexLower = currentLine.indexOf("content=\"") + 9;  // + 9 is added as it is the length of the substring "content=\"".
        int indexUpper = currentLine.indexOf("\" />"); // "\" />" indicates the end of the line.
        // substring from the end of "content=\"" to the beginning of "\" />" is returned as that contains the target name.
        return currentLine.substring(indexLower, indexUpper);
    }

    private String getTargetHtmlLine(InputStreamReader urlInputStreamReader) throws IOException {
        BufferedReader urlReader = new BufferedReader(urlInputStreamReader);
        // this is the substring which denotes the name of a person on the website
        // used to target the name of the individual's email id
        String targetHtmlSubstring = "property=\"og:title\"";
        // linear search until the html string containing the name of the individual is found
        boolean found = false;
        String currentLine = "";
        while(!found){
            currentLine = urlReader.readLine();
            if (currentLine.contains(targetHtmlSubstring)) {
                found = true;
            };
        }
        return currentLine;
    }

    private URLConnection getUrlConnection(String input) throws IOException {
        String url = "https://www.ecs.soton.ac.uk/people/" + input;
        // create a URL for the target website
        URL targetURL = new URL(url);
        // create a connection to the website targeted by the URL
        return targetURL.openConnection();
    }

    // NOTE: ADD A REGEX CHECK TO THIS FOR THE EMAIL ID EXPRESSION
    private String getUserInput() throws IOException {
        InputStreamReader inputReader = new InputStreamReader(System.in);
        // creating a buffered reader to convert Stream to strings
        BufferedReader bufferedInputReader = new BufferedReader(inputReader);
        // ask user to provide an email id
        // setting up an input reader (input in java is stored as Streams, linked list type objects with no indices)
        // this reads the input from the System.in Stream field
        System.out.println("Please enter an email id:");
        return bufferedInputReader.readLine();
    }
}
