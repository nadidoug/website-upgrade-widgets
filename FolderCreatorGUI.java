import javax.swing.*;
import java.awt.*;

public class FolderCreatorGUI {

    public static void main(String[] args) {

        JFrame window = new JFrame("Nadidoug Folder Creator");
        window.setSize(650, 500);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(18, 18, 18));

        JLabel title = new JLabel("Nadidoug Folder Creator");
        title.setBounds(40, 25, 550, 40);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 30));

        JLabel subtitle = new JLabel("Create project folders and open them in VS Code.");
        subtitle.setBounds(40, 65, 550, 30);
        subtitle.setForeground(Color.GRAY);
        subtitle.setFont(new Font("Arial", Font.PLAIN, 15));

        JTextField projectName = new JTextField();
        projectName.setBounds(40, 125, 550, 45);
        projectName.setFont(new Font("Arial", Font.PLAIN, 16));
        projectName.setBorder(BorderFactory.createTitledBorder("Project Name"));

        JTextField folderNames = new JTextField();
        folderNames.setBounds(40, 200, 550, 45);
        folderNames.setFont(new Font("Arial", Font.PLAIN, 16));
        folderNames.setBorder(BorderFactory.createTitledBorder("Folder Names separated by commas"));

        JButton createButton = new JButton("Create Project and Open VS Code");
        createButton.setBounds(40, 280, 550, 50);
        createButton.setFont(new Font("Arial", Font.BOLD, 17));
        createButton.setBackground(Color.WHITE);
        createButton.setForeground(Color.BLACK);
        createButton.setFocusPainted(false);

        JTextArea output = new JTextArea();
        output.setBounds(40, 360, 550, 70);
        output.setBackground(new Color(30, 30, 30));
        output.setForeground(Color.WHITE);
        output.setFont(new Font("Arial", Font.PLAIN, 14));
        output.setEditable(false);
        output.setLineWrap(true);
        output.setWrapStyleWord(true);

        createButton.addActionListener(e -> {

            String project = projectName.getText().trim();
            String folderText = folderNames.getText().trim();

            if (project.isEmpty()) {
                output.setText("Error: Project name cannot be empty.");
                return;
            }

            if (project.matches(".*[\\\\/:*?\"<>|].*")) {
                output.setText("Error: Project name contains invalid Windows characters.");
                return;
            }

            if (folderText.isEmpty()) {
                output.setText("Error: Please enter at least one folder name.");
                return;
            }

            ProjectBuilder builder = new ProjectBuilder(project);

            builder.createMainFolder();

            String[] folders = folderText.split(",");

            for (String folder : folders) {
                String cleanFolder = folder.trim();

                if (!cleanFolder.isEmpty()) {
                    builder.createSubFolder(cleanFolder);
                }
            }

            output.setText("Project created successfully:\n" + builder.getProjectPath());

            builder.openInVSCode();

        });

        panel.add(title);
        panel.add(subtitle);
        panel.add(projectName);
        panel.add(folderNames);
        panel.add(createButton);
        panel.add(output);

        window.add(panel);
        window.setVisible(true);
    }
}