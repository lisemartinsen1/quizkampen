import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Printer {
    List<PrintWriter> messages = new ArrayList<>();
    protected void addWriter(PrintWriter pw) {
        messages.add(pw);
    }
    public List<PrintWriter> sendMessage() {
        return messages;
    }
}

