import gui.FileChooser;

/**
 * Created by niewinskip on 2017-01-12.
 */
public class main {

    public static void main(String[] args) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.run(new FileChooser(), 400, 250);
    }

}
