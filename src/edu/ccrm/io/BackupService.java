package edu.ccrm.io;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class BackupService {
    private final Path backupRoot;

    public BackupService(String dataFolder) {
        this.backupRoot = Paths.get(dataFolder, "backups");
        try {
            if (!Files.exists(backupRoot)) {
                Files.createDirectories(backupRoot);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to create backup folder", e);
        }
    }

    public void runBackup(Path sourceFolder) throws IOException {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        Path target = backupRoot.resolve("backup_" + timestamp);
        Files.createDirectories(target);


        Files.walk(sourceFolder).forEach(path -> {
            try {
                Path rel = sourceFolder.relativize(path);
                Path dest = target.resolve(rel);
                if (Files.isDirectory(path)) {
                    Files.createDirectories(dest);
                } else {
                    Files.copy(path, dest, StandardCopyOption.REPLACE_EXISTING);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        System.out.println("Backup created at " + target);
        System.out.println("Total size: " + computeSize(target) + " bytes");
    }


    private long computeSize(Path folder) throws IOException {
        if (Files.isRegularFile(folder)) return Files.size(folder);
        long sum = 0;
        try (var stream = Files.list(folder)) {
            for (Path p : stream.toList()) {
                sum += computeSize(p);
            }
        }
        return sum;
    }
}
