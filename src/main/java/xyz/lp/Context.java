package xyz.lp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.nio.file.Path;

public class Context {

    private static Context instance;

    private String currentPath;

    private PrintStream originalOutput = System.out;
    private File redirectOutputFile = null;

    public static Context getInstance() {
        if (instance == null) {
            synchronized (Context.class) {
                if (instance == null) {
                    instance = new Context();
                }
            }
        }
        return instance;
    }

    Context() {
        // https://stackoverflow.com/questions/4871051/how-to-get-the-current-working-directory-in-java
        this.currentPath = Path.of("").toAbsolutePath().toString();
    }

    public void setCurrentPath(String currentPath) {
        this.currentPath = currentPath;
    }

    public String getCurrentPath() {
        return this.currentPath;
    }

    public void setRedirectOutputFile(File file) {
        try {
            System.setOut(new PrintStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        redirectOutputFile = file;
    }

    public File getRedirectOutputFile() {
        return redirectOutputFile;
    }

    public void clear() {
        System.setOut(originalOutput);
        redirectOutputFile = null;
    }

}
