import javafx.application.Platform;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Alex on 6/19/2016.
 */
public class Generator {
	private final File inputFolder;
	private final File outputFolder;
	private final TextArea log;
	private final ProgressBar progressBar;
	private final String jsonTemplate = new Scanner(getClass().getResourceAsStream("/template"), "UTF-8").useDelimiter("\\A").next();

	public Generator(File inputFolder, File outputFolder, TextArea log, ProgressBar progressBar) {
		this.inputFolder = inputFolder;
		this.outputFolder = outputFolder;
		this.log = log;
		this.progressBar = progressBar;
	}

	protected void run() {
		if(! inputFolder.isDirectory()) {
			log("ERROR: Input folder is not a directory!");
			return;
		}
		if(! outputFolder.isDirectory()) {
			log("Making output directory...");
			if(! outputFolder.mkdirs()) {
				log("ERROR: Could not make directory!");
				return;
			}
		}
		File[] files = inputFolder.listFiles();
		for (int i = 0; i < files.length; i++) {
			File f = files[i];
			progress(i + 1, files.length + 1);
			if(f.isDirectory()) {
				log("Ignoring Directory: " + f.getName());
			} else {
				parseFile(f, outputFolder);
			}
		}
		log("Done.");
		try {
			Desktop.getDesktop().open(outputFolder);
		} catch (IOException e) {}
	}
	private void parseFile(File inputFile, File outDir) {
		ArrayList<String> funcs = new ArrayList<String>();
		log("Reading File: " + inputFile.getName());
		String fContents;
		try {
			fContents = new String(Files.readAllBytes(inputFile.toPath()));
		} catch (IOException e) {
			log("ERROR: Error reading file: " + inputFile.getName());
			log("       " + e.getMessage());
			return;
		}
		String[] lines = fContents.split("\r\n|\r|\n");

		boolean isApi = false;
		int offset = 0;
		for (int i = 0; i < lines.length; i++) {
			if (lines[i].startsWith("Functions in the ") && lines[i].endsWith("API:")) {
				log("File appears to be API: " + inputFile.getName());
				isApi = true;
				offset = i;
				break;
			}
		}
		if(lines.length < 0 || !isApi) {
			log("Skipping non-API file: " + inputFile.getName());
			return;
		}
		for (int i = offset + 1; i < lines.length; i++) {
			String line = lines[i];
			if(line.endsWith(")") && line.indexOf("(") > 0) {
				funcs.add(line.substring(0, line.indexOf(")") + 1));
			} else {
				break;
			}
		}
		if(funcs.size() == 0) {
			return;
		}
		String apiName = funcs.get(0).substring(0, funcs.get(0).indexOf("."));
		StringBuilder json = new StringBuilder();
		for (int j = 0; j < funcs.size(); j++) {
			String func = funcs.get(j);
			int position = func.indexOf("(") + 1;
			String prefix = func.substring(0, position);
			String argArea = func.substring(position, func.length() - 1);
			String trigger = prefix + argArea + ")";
			String[] args = argArea.split(",");
			StringBuilder argN = new StringBuilder();
			argN.append(prefix);
			for (int i = 0; i < args.length; i++) {
				if(! args[i].trim().isEmpty()) {
					argN.append(" ${" + (i + 1) + ":" + args[i].trim() + "}");
				}
				if(i + 1 < args.length) {
					argN.append(",");
				} else {
					if (!args[i].trim().isEmpty()) {
						argN.append(" ");
					}
					argN.append(")");
				}
			}
			String completion = argN.toString();
			json.append("\t\t{ " +
					"\"trigger\": \"" + trigger + "\", " +
					"\"contents\": \"" + completion + "\"" +
					" }");
			if(j + 1 < funcs.size()) {
				json.append(",");
			}
			json.append("\r\n");
		}
		String finalJson = jsonTemplate
				.replace("%STUFF%", json.toString())
				.replace("%APINAME%", apiName);
		List<String> toWrite = Arrays.asList(finalJson);
		Path file = Paths.get(outDir.getPath() + File.separator + apiName + ".sublime-completions");
		log("Writing " + funcs.size() + " completions to " + file.toString());
		try {
			Files.write(file, toWrite, Charset.forName("UTF-8"));
		} catch (IOException e) {
			log("ERROR: Error writing file: " + file.getFileName().toString());
			log("       " + e.getMessage());
			return;
		}
	}

	private void log(String string) {
		Platform.runLater(() -> log.appendText(string + "\r\n"));
	}

	private void progress(int done, int total) {
		Platform.runLater(() -> progressBar.setProgress(total / done));
	}
}
