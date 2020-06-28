import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Date;


public class FileCreator {

    String input;

    public FileCreator(String input) {
        this.input = input;
    }

    public boolean createFile() throws IOException, ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss-z");
        String format = dateFormat.format(new Date());
        String path = format + "_cars.txt";

        JSONArray data = getJSON(input);
        StringBuilder sb = new StringBuilder();
        data.forEach(o -> {
            String name1 = (String) ((JSONObject) o).get("name");
            String coordinats = ((JSONObject) o).get("latitude") +
                    ":"
                    + ((JSONObject) o).get("longitude");
            sb.append(name1).append(" ").append(coordinats).append("\n");
        });

        Files.createFile(Paths.get(path));
        Files.write(Paths.get(path), sb.toString().getBytes(), new StandardOpenOption[]{StandardOpenOption.APPEND});
        return Files.exists(Paths.get(path));
    }

    private JSONArray getJSON(String string) throws ParseException {
        String substring = string.substring(string.indexOf("["), string.indexOf("]") + 1);
        JSONParser jsonParser = new JSONParser();
        return (JSONArray) jsonParser.parse(substring);
    }
}
