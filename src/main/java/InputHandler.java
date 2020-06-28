import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputHandler {

    private String input;

    public InputHandler(String string) {
        this.input = string;
    }

    public String handle() {
        String _cars = input.substring(input.indexOf("var cars ="), input.indexOf("]);") + 1);
        String decoded = decode(_cars);
        String parsed = parse(decoded);
        return parsed;
    }

    private String decode(String s) {
//        Charset cset = Charset.forName("windows-1251");
        Charset cset = Charset.forName("UTF-8");
        ByteBuffer buf = cset.encode(s);
        byte[] b = buf.array();
        return new String(b);
    }


    private String parse(String s) {

        StringBuilder sb = new StringBuilder();


        Pattern p = Pattern.compile("\\\\u([0-9a-fA-F]{4})");
        Matcher m = p.matcher(s);

        int lastIndex = 0;
        while (m.find()) {

            sb.append(s.substring(lastIndex, m.start()));
            lastIndex = m.end();

            sb.append((char) Integer.parseInt(m.group(1), 16));
        }

        if (lastIndex < s.length())
            sb.append(s.substring(lastIndex));

        return sb.toString();
    }
}
