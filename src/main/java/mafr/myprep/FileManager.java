package mafr.myprep;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FileManager {

	private BufferedReader reader;
	private Map<TipoRegistro, BufferedWriter> writers = new HashMap<TipoRegistro, BufferedWriter>();
	private BufferedWriter analysisWriter;
	private String baseDir;
	private boolean initialized;

	public synchronized void start(String baseDir) throws IOException {
		if (initialized) {
			throw new IllegalStateException("This FileManager instance has already been started.");
		}
		this.baseDir = baseDir;
		this.reader = new BufferedReader(new FileReader(baseDir + File.separator + "presidente.txt"));
		this.analysisWriter = new BufferedWriter(new FileWriter(baseDir + File.separator + "resultados.txt"));
		this.initialized = true;
	}

	public synchronized void stop() throws IOException {
		if (!initialized) {
			throw new IllegalStateException("This FileManager instance has not been started yet.");
		}
		this.reader.close();
		this.analysisWriter.close();
		for (BufferedWriter writer : writers.values()) {
			writer.close();
		}
		this.baseDir = null;
		this.initialized = false;
	}

	public void writeRegistro(TipoRegistro tipoRegistro, String registroStr) throws IOException {
		if (!initialized) {
			throw new IllegalStateException("This FileManager instance has not been started yet.");
		}
		if (!writers.containsKey(tipoRegistro)) {
			writers.put(tipoRegistro,
					new BufferedWriter(new FileWriter(baseDir + File.separator + tipoRegistro.toString() + ".txt")));
		}
		writers.get(tipoRegistro).write(registroStr + "\n");
	}

	public String readRegistro() throws IOException {
		return reader.readLine();
	}

	public void writeAnalysis(PrepAnalysis analysis) throws IOException {
		analysisWriter.write(analysis.toString());
	}

}
