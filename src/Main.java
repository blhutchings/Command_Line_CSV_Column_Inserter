import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Main {
    private static final Scanner STDIO = new Scanner(System.in);

    public static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final String DELIMITER = ","; //Delimiter of the column reader


    public static String FILE_NAME;

    public static final String USES_HELP = "Usage: java -jar <jar file> [args..]\n";
    public static final String ARGS_HELP = "Arguments following the the -jar <jar file>" +
            "<file> \n" +
            "Example: java -jar <jar file> /home/<user>/documents/log.csv";

    
    public static void main(String[] args){

        File file;
        if (args.length == 1) {
            FILE_NAME = args[0];
            file = new File(FILE_NAME);
        } else {
            System.err.println("Invalid arguments!");
            System.out.println(USES_HELP + ARGS_HELP);
            return;
        }


            try {
                if (file.createNewFile()) {
                    System.out.println("Created new  file called: " + FILE_NAME);
                }
            } catch (IOException e) {
                System.out.println("Could not create file: " + FILE_NAME);
            }


        //Open Scanner to read heading columns
        try (Scanner headerReader = new Scanner(new FileReader(file));
             BufferedWriter fileWriter = new BufferedWriter(new FileWriter(file, true)))
        {
            StringBuilder newLine = new StringBuilder("\n"); //String that will be added to new row
            String headerString = headerReader.nextLine().trim();
            String[] columnRow = headerString.split(DELIMITER);

            for (int column = 0; column < columnRow.length; column++)
            {

            /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                                                                        INSERT NEW CASE ALIAS HERE
            *///~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                final String DATE_ALIAS = "date";

                switch (columnRow[column]) {

                    //If a column header is date, insert the current date using dateTimeFormatter format
                    case DATE_ALIAS:
                        newLine.append(dateTimeFormatter.format(LocalDateTime.now())).append(DELIMITER);
                        break;

                    //If not an alias, ask for input for the row
                    default:
                        //Asks for input for the column
                        System.out.println("Enter: " + columnRow[column]);
                        String input = STDIO.nextLine();
                        newLine.append(input);

                        //Add delimiter to end of input if not final column
                        if (column != columnRow.length-1) {
                            newLine.append(DELIMITER);
                        }
                        break;

                }
            }

            //If final column contains a ',' at the end, add it to the end.
            //--Keeps the format in unison depending on the person writing the columns
            if (headerString.endsWith(",")) {
                newLine.append(",");
            }

            //Add new line and append to the file
            fileWriter.append(newLine.toString());
            fileWriter.flush();

        } catch (IOException e) {
            System.out.println("ERROR: Can not find or read file.");
            e.printStackTrace();
        } catch (NoSuchElementException e) {
            System.out.println("ERROR: No column headers");
            e.printStackTrace();
        }

    }
}
