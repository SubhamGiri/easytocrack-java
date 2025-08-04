package com.easytocrack.servicesImpl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import org.springframework.stereotype.Service;
import com.easytocrack.services.HomeServices;

@Service
public class HomeServiceImpl implements HomeServices {

    @Override
    public String runProgram(String sourceCode) throws IOException, InterruptedException {
        String javaFileName = "Main.java";
        String javaFilePath = "src/main/Programs/";

        try {
            createDirectory(javaFilePath, javaFileName, sourceCode);

            File javaFile = new File(javaFilePath + javaFileName);
            String className = javaFile.getName().replace(".java", "");
            String dir = javaFile.getParent();

            // Step 1: Compile the Java file
            Process compile = new ProcessBuilder("javac", javaFile.getAbsolutePath()).start();
            compile.waitFor();

            // Capture compilation errors (if any)
            BufferedReader compileErrorReader = new BufferedReader(new InputStreamReader(compile.getErrorStream()));
            StringBuilder compileErrorOutput = new StringBuilder();
            String line;
            while ((line = compileErrorReader.readLine()) != null) {
                compileErrorOutput.append(line).append("\n");
            }

            if (compile.exitValue() != 0) {
                System.out.println("🔴 Compilation Errors:\n" + compileErrorOutput.toString());
                clearDirectory(javaFilePath);
                return "❌ Compilation failed:\n" + compileErrorOutput.toString();
            }

            // Step 2: Run the compiled class
            Process run = new ProcessBuilder("java", "-cp", dir, className).start();

            // Capture standard output
            BufferedReader reader = new BufferedReader(new InputStreamReader(run.getInputStream()));
            StringBuilder output = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            // Wait for program to finish
            run.waitFor();

            // Capture runtime errors (stderr)
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(run.getErrorStream()));
            StringBuilder errorOutput = new StringBuilder();
            while ((line = errorReader.readLine()) != null) {
                errorOutput.append(line).append("\n");
            }

            if (errorOutput.length() > 0) {
                System.out.println("🔴 Runtime Errors:\n" + errorOutput.toString());
                output.append("\nRuntime Errors:\n").append(errorOutput);
            }

            System.out.println("🟢 Program Output:\n");
            System.out.println(output.toString());

            clearDirectory(javaFilePath);

            if (run.exitValue() != 0) {
                return "❌ Program execution failed.\n" + output.toString();
            }

            return output.toString();

        } catch (Exception e) {
        	e.printStackTrace();
            System.out.println("❌ Exception: " + e.getMessage());           
            return "❌ Exception occurred: " + e.getMessage();
        }
    }

    public void createDirectory(String directoryPath, String fileName, String sourceCode) {
        try {
            File directory = new File(directoryPath);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            File javaFile = new File(directoryPath, fileName);
            try (FileWriter writer = new FileWriter(javaFile)) {
                writer.write(sourceCode);
                System.out.println("✅ File created: " + javaFile.getAbsolutePath());
            }

        } catch (IOException e) {
            System.out.println("❌ Error while creating file: " + e.getMessage());
        }
    }

    public void clearDirectory(String directoryPath) {
        File directory = new File(directoryPath);
        File[] files = directory.listFiles();

        if (files == null || files.length == 0) {
            System.out.println("📂 No files found or directory doesn't exist.");
            return;
        }

        for (File file : files) {
            if (file.delete()) {
                System.out.println("🗑 Deleted: " + file.getName());
            } else {
                System.out.println("❌ Failed to delete: " + file.getName());
            }
        }

        System.out.println("✅ Cleanup complete.");
    }
}
