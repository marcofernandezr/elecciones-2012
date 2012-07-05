package mafr.myprep;


public class Main {

	public static void main(String[] args) {
		if(args.length == 0) {
			System.err.println("Falta el parametro baseDir (Directorio donde se encuentra el archivo de texto presidente.txt con las actas)");
		}
		try {
			PrepAnalyzer analyzer = new PrepAnalyzer();
			System.out.println("Analizando...");
			analyzer.execute(args[0]);
			System.out.println("Los archivos con los resultados del análisis se encuentran en el siguiente directorio: " + args[0]);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
