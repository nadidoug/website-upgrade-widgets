import java.io.File;
import java.io.IOException;

public class ProjectBuilder {

    private File projectFolder;

    public ProjectBuilder(String projectName) {

        projectFolder = new File(projectName);

    }

    public void createMainFolder() {

        projectFolder.mkdir();

    }

    public void createSubFolder(String folderName) {

        File subFolder =
                new File(projectFolder, folderName);

        subFolder.mkdir();

    }

    public void openInVSCode() {

        try {

            ProcessBuilder builder =
                    new ProcessBuilder(
                            "cmd.exe",
                            "/c",
                            "code \"" +
                                    projectFolder.getAbsolutePath()
                                    + "\""
                    );

            builder.start();

        } catch (IOException e) {

            System.out.println("Could not open VS Code.");

        }

    }

    public String getProjectPath() {

        return projectFolder.getAbsolutePath();

    }

}