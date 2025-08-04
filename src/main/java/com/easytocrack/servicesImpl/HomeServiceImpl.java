package com.easytocrack.servicesImpl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import org.springframework.stereotype.Service;

import com.easytocrack.services.HomeServices;

@Service
public class HomeServiceImpl implements HomeServices{
	
	@Override
	public String runProgram(String sourceCode) throws IOException, InterruptedException
	{
		// Full path to your HelloWorld.java file
        String javaFileName = "Main.java";
        String javaFilePath = "src\\main\\Programs\\";				//C:\\Users\\user\\Documents\\workspace-spring-tool-suite-4-4.20.0.RELEASE\\EasyToCrack\\
        
        try {
        createDirectory(javaFilePath, javaFileName, sourceCode);
        
        // Extract the directory and class name
        File javaFile = new File(javaFilePath+javaFileName);
        String className = javaFile.getName().replace(".java", "");
        String dir = javaFile.getParent();

        // Step 1: Compile the Java file
        Process compile = new ProcessBuilder("javac", javaFile.getAbsolutePath())
                //.inheritIO()
                .start();
        compile.waitFor();

        // Step 2: Run the compiled class from the same directory
        Process run = new ProcessBuilder("java", "-cp", dir, className)
                //.inheritIO()
                .start();
        
        BufferedReader reader = new BufferedReader(new InputStreamReader(run.getInputStream()));
        StringBuilder output = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            output.append(line).append("\n");
        }

        run.waitFor(); // wait until process ends
        System.out.println("🟢 Program Output:\n");
        System.out.println(output.toString());
        clearDirectory(javaFilePath);
        
        return output.toString();
        }
        catch(Exception e)
        {
        	System.out.println(e);
        	return e.toString();
        }
	}

	public void createDirectory(String directoryPath, String fileName,String sourceCode)
    {
    	try {
            // Create directory if it doesn't exist
            File directory = new File(directoryPath);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Create .java file
            File javaFile = new File(directoryPath, fileName);

            try (FileWriter writer = new FileWriter(javaFile)) {
                writer.write(sourceCode);
                System.out.println("✅ File created: " + javaFile.getAbsolutePath());
            }

        } catch (IOException e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }
    public void clearDirectory(String directoryPath)
    {
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
