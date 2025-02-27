package xyz.lp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.nio.file.Path;

public class Context {

    private static Context instance;

    private String currentPath;

    private PrintStream originalStdout = System.out;
    private PrintStream originalStderr = System.err;
    private File redirectStdoutFile = null;
    private File redirectStderrFile = null;

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

    public void setRedirectStdoutFile(File file) {
        try {
            System.setOut(new PrintStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        redirectStdoutFile = file;
    }

    public void setRedirectStderrFile(File file) {
        try {
            System.setErr(new PrintStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        redirectStderrFile = file;
    }

    public File getRedirectStdoutFile() {
        return redirectStdoutFile;
    }


    public File getRedirectStderrFile() {
        return redirectStderrFile;
    }

    public void clear() {
        System.setOut(originalStdout);
        System.setErr(originalStderr);
        redirectStdoutFile = null;
        redirectStderrFile = null;
    }

}
