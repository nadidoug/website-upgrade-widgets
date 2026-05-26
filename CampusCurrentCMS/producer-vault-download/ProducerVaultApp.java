import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class ProducerVaultApp extends JFrame {

    private JTextField scanField;
    private JTextField backupField;
    private JTextArea logArea;

    static String[] audioFiles = {".wav", ".mp3", ".aiff", ".aif", ".flac", ".m4a", ".ogg"};
    static String[] projectFiles = {".flp", ".als", ".logic", ".logicx", ".ptx", ".cpr", ".rpp"};
    static String[] midiFiles = {".mid", ".midi"};
    static String[] presetFiles = {".fst", ".fxp", ".fxb", ".adg", ".adv", ".vstpreset"};
    static String[] sampleFiles = {".kit", ".xpm", ".nki", ".sf2", ".sfz"};

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            if (!LicenseManager.checkLicenseUI()) {
                return;
            }

            new ProducerVaultApp();
        });
    }

    public ProducerVaultApp() {
        setTitle("Producer Vault");
        setSize(780, 560);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel root = new JPanel();
        root.setBackground(new Color(12, 12, 14));
        root.setBorder(new EmptyBorder(28, 28, 28, 28));
        root.setLayout(new BorderLayout(20, 20));

        JLabel title = new JLabel("Producer Vault");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("SansSerif", Font.BOLD, 34));

        JLabel subtitle = new JLabel("Secure backup for beats, sessions, vocals, samples, MIDI, and presets.");
        subtitle.setForeground(new Color(160, 160, 170));
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 15));

        JPanel header = new JPanel(new GridLayout(2, 1));
        header.setOpaque(false);
        header.add(title);
        header.add(subtitle);

        JPanel card = new JPanel();
        card.setBackground(new Color(24, 24, 28));
        card.setBorder(new EmptyBorder(24, 24, 24, 24));
        card.setLayout(new GridLayout(4, 1, 10, 10));

        scanField = createField("Choose folder or drive to scan");
        backupField = createField("Choose backup destination");

        JButton scanButton = createButton("Browse Scan Folder");
        JButton backupButton = createButton("Browse Backup Folder");
        JButton startButton = createPrimaryButton("Start Backup");
        JButton reviewButton = createDangerButton("Move Originals to Review Folder");

        scanButton.addActionListener(e -> chooseFolder(scanField));
        backupButton.addActionListener(e -> chooseFolder(backupField));
        startButton.addActionListener(e -> startBackup(false));
        reviewButton.addActionListener(e -> startBackup(true));

        card.add(row(scanField, scanButton));
        card.add(row(backupField, backupButton));
        card.add(startButton);
        card.add(reviewButton);

        logArea = new JTextArea();
        logArea.setBackground(new Color(8, 8, 10));
        logArea.setForeground(new Color(210, 210, 215));
        logArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        logArea.setEditable(false);
        logArea.setBorder(new EmptyBorder(16, 16, 16, 16));

        JScrollPane scrollPane = new JScrollPane(logArea);
        scrollPane.setBorder(null);

        root.add(header, BorderLayout.NORTH);
        root.add(card, BorderLayout.CENTER);
        root.add(scrollPane, BorderLayout.SOUTH);

        add(root);
        setVisible(true);
    }

    private JTextField createField(String placeholder) {
        JTextField field = new JTextField(placeholder);
        field.setBackground(new Color(34, 34, 38));
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        field.setBorder(new EmptyBorder(12, 14, 12, 14));
        return field;
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(48, 48, 54));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("SansSerif", Font.BOLD, 13));
        return button;
    }

    private JButton createPrimaryButton(String text) {
        JButton button = createButton(text);
        button.setBackground(new Color(0, 122, 255));
        return button;
    }

    private JButton createDangerButton(String text) {
        JButton button = createButton(text);
        button.setBackground(new Color(185, 48, 48));
        return button;
    }

    private JPanel row(JTextField field, JButton button) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setOpaque(false);
        panel.add(field, BorderLayout.CENTER);
        panel.add(button, BorderLayout.EAST);
        return panel;
    }

    private void chooseFolder(JTextField field) {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int result = chooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            field.setText(chooser.getSelectedFile().getAbsolutePath());
        }
    }

    private void startBackup(boolean moveOriginals) {
        boolean confirmedMove = false;

        if (moveOriginals) {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Backup files first, then move only successfully copied originals to Review_Delete_Folder?",
                    "Confirm Review Move",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }

            confirmedMove = true;
        }

        boolean shouldMoveOriginals = confirmedMove;

        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                runBackup(shouldMoveOriginals);
                return null;
            }
        }.execute();
    }

    private void runBackup(boolean moveOriginals) {
        try {
            Path scanFolder = normalizeFolder(scanField.getText());
            Path backupFolder = normalizeFolder(backupField.getText());

            if (!Files.isDirectory(scanFolder)) {
                log("Scan folder does not exist or is not a folder.");
                return;
            }

            validateBackupLocation(scanFolder, backupFolder);
            createBackupFolders(backupFolder);

            List<Path> copiedOriginals = new ArrayList<>();

            try (Stream<Path> files = Files.walk(scanFolder)) {
                files.filter(Files::isRegularFile)
                        .filter(ProducerVaultApp::isProducerFile)
                        .filter(file -> !file.normalize().toAbsolutePath().startsWith(backupFolder))
                        .forEach(file -> {
                            if (copyFile(file, backupFolder)) {
                                copiedOriginals.add(file);
                            }
                        });
            }

            if (moveOriginals) {
                moveOriginals(copiedOriginals, backupFolder);
            }

            log("Backup complete.");

        } catch (IOException e) {
            log("Error: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            log(e.getMessage());
        }
    }

    private static Path normalizeFolder(String folderText) {
        if (folderText == null || folderText.trim().isEmpty()) {
            throw new IllegalArgumentException("Please choose both a scan folder and a backup folder.");
        }

        return Paths.get(folderText.trim()).normalize().toAbsolutePath();
    }

    private static void validateBackupLocation(Path scanFolder, Path backupFolder) {
        if (scanFolder.equals(backupFolder)) {
            throw new IllegalArgumentException("Backup folder cannot be the same as the scan folder.");
        }

        if (backupFolder.startsWith(scanFolder)) {
            throw new IllegalArgumentException("Backup folder cannot be inside the scan folder.");
        }

        if (scanFolder.startsWith(backupFolder)) {
            throw new IllegalArgumentException("Scan folder cannot be inside the backup folder.");
        }
    }

    private static void createBackupFolders(Path backupFolder) throws IOException {
        Files.createDirectories(backupFolder.resolve("Audio"));
        Files.createDirectories(backupFolder.resolve("Projects"));
        Files.createDirectories(backupFolder.resolve("MIDI"));
        Files.createDirectories(backupFolder.resolve("Presets"));
        Files.createDirectories(backupFolder.resolve("Samples"));
        Files.createDirectories(backupFolder.resolve("Review_Delete_Folder"));
    }

    private boolean copyFile(Path file, Path backupFolder) {
        try {
            String category = getCategory(file);
            Path destination = backupFolder.resolve(category).resolve(file.getFileName());
            destination = avoidDuplicateName(destination);

            Files.copy(file, destination, StandardCopyOption.COPY_ATTRIBUTES);

            if (Files.size(file) != Files.size(destination)) {
                log("Copy verification failed: " + file.getFileName());
                return false;
            }

            log("Copied: " + file.getFileName());
            return true;

        } catch (IOException e) {
            log("Could not copy: " + file);
            return false;
        }
    }

    private void moveOriginals(List<Path> copiedOriginals, Path backupFolder) throws IOException {
        Path reviewFolder = backupFolder.resolve("Review_Delete_Folder");

        for (Path file : copiedOriginals) {
            try {
                Path destination = reviewFolder.resolve(file.getFileName());
                destination = avoidDuplicateName(destination);
                Files.move(file, destination);
                log("Moved original: " + file.getFileName());
            } catch (IOException e) {
                log("Could not move: " + file);
            }
        }
    }

    private static boolean isProducerFile(Path file) {
        return !getCategory(file).equals("Unknown");
    }

    private static String getCategory(Path file) {
        String name = file.toString().toLowerCase();

        if (hasExtension(name, audioFiles)) return "Audio";
        if (hasExtension(name, projectFiles)) return "Projects";
        if (hasExtension(name, midiFiles)) return "MIDI";
        if (hasExtension(name, presetFiles)) return "Presets";
        if (hasExtension(name, sampleFiles)) return "Samples";

        return "Unknown";
    }

    private static boolean hasExtension(String fileName, String[] extensions) {
        for (String ext : extensions) {
            if (fileName.endsWith(ext)) return true;
        }
        return false;
    }

    private static Path avoidDuplicateName(Path destination) throws IOException {
        if (!Files.exists(destination)) return destination;

        String fileName = destination.getFileName().toString();
        String name = fileName;
        String extension = "";

        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex != -1) {
            name = fileName.substring(0, dotIndex);
            extension = fileName.substring(dotIndex);
        }

        int count = 1;
        Path newDestination;

        do {
            newDestination = destination.getParent().resolve(name + "_" + count + extension);
            count++;
        } while (Files.exists(newDestination));

        return newDestination;
    }

    private void log(String message) {
        SwingUtilities.invokeLater(() -> logArea.append(message + "\n"));
    }
}
