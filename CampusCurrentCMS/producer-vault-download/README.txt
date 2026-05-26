PRODUCER VAULT - TEST DOWNLOAD

This folder is the producer test package.

Important: download the whole GitHub repository, not only this one folder.
The launcher uses the bundled Java runtime at:

   java-workflow/tools/jdk

WHAT THIS APP DOES

Producer Vault scans a folder for producer files and backs them up into:

- Audio
- Projects
- MIDI
- Presets
- Samples
- Review_Delete_Folder

It recognizes common beat, session, vocal, MIDI, preset, and sample file types.


HOW TO RUN ON WINDOWS

1. Open this folder:

   producer-vault-download

2. Double-click:

   RUN-PRODUCER-VAULT.bat

3. If Windows asks whether to run it, choose Run.

4. The activation box will show a Device ID.

5. Send the Device ID to the owner.

6. Paste the license key you receive.


HOW TO TEST SAFELY

Use the included fake test folders first.

Scan folder:

   sample-scan-folder

Backup folder:

   sample-backup-folder

Click:

   Start Backup

Expected result:

- fake_kick.wav goes into sample-backup-folder/Audio
- fake_beat.mp3 goes into sample-backup-folder/Audio
- fake_project.flp goes into sample-backup-folder/Projects
- fake_chords.mid goes into sample-backup-folder/MIDI
- fake_preset.fxp goes into sample-backup-folder/Presets
- fake_kit.nki goes into sample-backup-folder/Samples
- ignore_this.txt should NOT be backed up


IMPORTANT SAFETY NOTES

- Test with the sample folders before using real music files.
- Use Start Backup first.
- Only use "Move Originals to Review Folder" after confirming backups worked.
- Do not choose the same folder for scan and backup.
- Do not put the backup folder inside the scan folder.
- Do not put the scan folder inside the backup folder.


IF THE APP DOES NOT OPEN

Use PowerShell from the main project folder and run:

   .\java-workflow\tools\jdk\bin\java.exe -cp producer-vault-download\classes ProducerVaultApp


FILES IN THIS PACKAGE

- RUN-PRODUCER-VAULT.bat
- README.txt
- classes
- sample-scan-folder
- sample-backup-folder


OWNER NOTE

This producer package does not include the private license key generator.
The producer sends the Device ID shown on screen, and the owner generates
the matching license key privately.
