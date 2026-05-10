// Author:Nadi May 2026
//IM TIRED OF BUILDING FOLDERS MANUALLY SO I MADE THIS PROGRAM TO DO IT FOR ME. HOPE IT HELPS YOU TOO! - Nadi


import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class FolderCreator {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        // WELCOME MESSAGE
        System.out.println("==================================");
        System.out.println("   WEBSITE PROJECT CREATOR");
        System.out.println("==================================");
        System.out.println();
        System.out.println("This program will:");
        System.out.println("- Create your project folder");
        System.out.println("- Create subfolders");
        System.out.println("- Open the folder in VS Code");
        System.out.println();

        // PROJECT NAME
        System.out.print("Enter project name: ");
        String projectName = input.nextLine();

        // CREATE MAIN PROJECT FOLDER
        File projectFolder = new File(projectName);

        if (projectFolder.mkdir()) {

            System.out.println();
            System.out.println(projectName + " folder created.");

        } else {

            System.out.println();
            System.out.println("Folder already exists.");

        }

        // HOW MANY SUBFOLDERS
        System.out.println();

        System.out.print("How many folders do you want inside the project? ");

        int folderCount = input.nextInt();

        input.nextLine();

        // CREATE SUBFOLDERS
        for (int i = 1; i <= folderCount; i++) {

            System.out.print("Enter folder name " + i + ": ");

            String folderName = input.nextLine();

            File subFolder =
                    new File(projectFolder, folderName);

            if (subFolder.mkdir()) {

                System.out.println(folderName + " created.");

            } else {

                System.out.println(folderName + " already exists.");

            }

        }

        // OPEN VS CODE
        System.out.println();
        System.out.println("Opening project in VS Code...");

        try {

            ProcessBuilder builder =
                    new ProcessBuilder(
                            "cmd.exe",
                            "/c",
                            "code " + projectFolder.getAbsolutePath()
                    );

            builder.start();

        } catch (IOException e) {

            System.out.println("Could not open VS Code.");
            System.out.println("Make sure 'code' is installed in PATH.");

        }

        System.out.println();
        System.out.println("Setup complete.");

        input.close();

    }

}