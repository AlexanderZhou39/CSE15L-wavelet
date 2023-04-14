import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Random;

class SearchHandler implements URLHandler {
    ArrayList<String> dictionary = new ArrayList<>();

    final String[] catAscii = new String[] {
        " /\\_/\\\n( o.o ) \n > ^ <",
        "      |\\      _,,,---,,_\nZZZzz /,`.-'`'    -.  ;-;;,_\n     |,4-  ) )-,_. ,\\ (  `'-'\n    '---''(_/--'  `-'\\_)",
        " _._     _,-\'\"\"`-._\n(,-.`._,\'(       |\\`-/|\n    `-.-\' \\ )-`( , o o)\n          `-    \\`_`\"\'-\n",
        "    /\\_/\\           ___\n   = o_o =_______    \\ \\  \n    __^      __(  \\.__) )\n(@)<_____>__(_____)____/\n"
    };

    private String addCatAscii(String text) {
        int rnd = new Random().nextInt(this.catAscii.length);
        String ascii = catAscii[rnd];
        return "https://www.asciiart.eu/animals/cats\n\n" + ascii + "\n\n" + text;
    }

    private ArrayList<String> getSearch(String query) {
        ArrayList<String> matches = new ArrayList<>();
        for (String s: this.dictionary) {
            if (s.contains(query)) {
                matches.add(s);
            }
        }
        return matches;
    }

    private String matchesToString(ArrayList<String> matches) {
        String result = "";
        for (int i = 0; i < matches.size(); i++) {
            result += matches.get(i);
            if (i < matches.size() - 1) {
                result += ", ";
            }
        }
        return result;
    }

    public String handleRequest(URI url) {
        String path = url.getPath();

        if (path.equals("/")) {
            return addCatAscii("Alex's Search Engine");
        } else if (path.contains("/add")) {
            String[] parameters = url.getQuery().split("=");
            if (parameters[0].equals("s")) {
                String newWord = parameters[1];
                this.dictionary.add(newWord);
                return addCatAscii("Added " + newWord + " to dictionary");
            } else {
                return addCatAscii("400 Invalid parameters");
            }
        } if (path.contains("/search")) {
            String[] parameters = url.getQuery().split("=");
            if (parameters[0].equals("s")) {
                String query = parameters[1];
                ArrayList<String> matches = this.getSearch(query);
                String response = matchesToString(matches);
                return addCatAscii("Found: " + response);
            } else {
                return addCatAscii("400 Invalid query");
            }
        } else {
            return addCatAscii("404 Not found");
        }
    }
}

class SearchEngine {
    public static void main(String[] args) throws IOException {
        if(args.length == 0){
            System.out.println("Missing port number! Try any number between 1024 to 49151");
            return;
        }

        int port = Integer.parseInt(args[0]);

        Server.start(port, new SearchHandler());
    }
}