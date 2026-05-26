import javax.swing.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.security.MessageDigest;

public class LicenseManager {

    private static final String SECRET_WORD = "NADIDOUG_PRODUCER_VAULT_2026";
    private static final Path LICENSE_FILE = Paths.get(
            System.getProperty("user.home"),
            ".producer-vault",
            "producer_vault_license.txt"
    );
    private static final Path LEGACY_LICENSE_FILE = Paths.get("producer_vault_license.txt");

    public static boolean checkLicenseUI() {
        String deviceId = getDeviceId();

        String savedLicense = readSavedLicense();

        if (savedLicense != null && savedLicense.equals(generateLicenseKey(deviceId))) {
            return true;
        }

        String message = "Device ID:\n" + deviceId + "\n\nSend this Device ID to get your license key.";

        String enteredKey = JOptionPane.showInputDialog(
                null,
                message,
                "Producer Vault Activation",
                JOptionPane.PLAIN_MESSAGE
        );

        if (enteredKey == null) {
            return false;
        }

        enteredKey = enteredKey.trim();
        String correctKey = generateLicenseKey(deviceId);

        if (enteredKey.equals(correctKey)) {
            try {
                Files.createDirectories(LICENSE_FILE.getParent());
                Files.writeString(LICENSE_FILE, enteredKey, StandardCharsets.UTF_8);
            } catch (IOException ignored) {}

            JOptionPane.showMessageDialog(null, "Activation successful.");
            return true;
        }

        JOptionPane.showMessageDialog(null, "Invalid license key.");
        return false;
    }

    private static String readSavedLicense() {
        String savedLicense = readLicenseFile(LICENSE_FILE);

        if (savedLicense != null) {
            return savedLicense;
        }

        return readLicenseFile(LEGACY_LICENSE_FILE);
    }

    private static String readLicenseFile(Path licenseFile) {
        if (!Files.exists(licenseFile)) {
            return null;
        }

        try {
            return Files.readString(licenseFile, StandardCharsets.UTF_8).trim();
        } catch (IOException e) {
            return null;
        }
    }

    public static String getDeviceId() {
        String username = System.getProperty("user.name");
        String os = System.getProperty("os.name");
        String home = System.getProperty("user.home");
        String computerName = System.getenv("COMPUTERNAME");

        if (computerName == null) {
            computerName = System.getenv("HOSTNAME");
        }

        String rawDeviceId = username + "|" + os + "|" + home + "|" + computerName;

        return hash(rawDeviceId).substring(0, 16).toUpperCase();
    }

    public static String generateLicenseKey(String deviceId) {
        String rawLicense = deviceId + "|" + SECRET_WORD;
        String hash = hash(rawLicense).toUpperCase();

        return hash.substring(0, 4) + "-" +
                hash.substring(4, 8) + "-" +
                hash.substring(8, 12) + "-" +
                hash.substring(12, 16);
    }

    private static String hash(String text) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(text.getBytes());

            StringBuilder hex = new StringBuilder();

            for (byte b : hashBytes) {
                hex.append(String.format("%02x", b));
            }

            return hex.toString();

        } catch (Exception e) {
            throw new RuntimeException("Hashing error");
        }
    }
}
