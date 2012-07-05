package mafr.myprep;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PrepAnalyzer {

	private final FileManager fileManager = new FileManager();

	public synchronized PrepAnalysis execute(final String baseDir) throws Exception {
		fileManager.start(baseDir);
		PrepAnalysis analysis = new PrepAnalysis();
		String registroStr = null;
		while ((registroStr = fileManager.readRegistro()) != null) {
			RegistroPrep registro = null;
			try {
				registro = RegistroPrepFactory.create(registroStr);
			} catch (Exception e) {
				e.printStackTrace();
				System.err.println("\nEsta linea no corresponde a un registro del PREP > " + registroStr);
				continue;
			}
			addCriterios(registro, analysis);
		}
		analysis.finish();
		fileManager.writeAnalysis(analysis);
		fileManager.stop();
		return analysis;
	}

	private void addCriterios(RegistroPrep registro, PrepAnalysis analysis) throws IOException {
		List<Criterio> criteriosRegistro = new ArrayList<Criterio>();
		if (registro.isCompleto()) {
			criteriosRegistro.add(Criterio.COMPLETO);
		} else {
			criteriosRegistro.add(Criterio.INCOMPLETO);
		}
		addAndSaveCriterio(new TipoRegistro(criteriosRegistro), registro, analysis);
		if (registro.isContabilizado()) {
			criteriosRegistro.add(Criterio.CONTABILIZADO);
		} else {
			criteriosRegistro.add(Criterio.SIN_CONTABILIZAR);
		}
		addAndSaveCriterio(new TipoRegistro(criteriosRegistro), registro, analysis);
		if (registro.isCasillaEspecial()) {
			criteriosRegistro.add(Criterio.CASILLA_ESPECIAL);
		} else if (registro.isCasillaExtraordinaria()) {
			criteriosRegistro.add(Criterio.CASILLA_EXTRAORDINARIA);
		} else {
			criteriosRegistro.add(Criterio.CASILLA_NORMAL);
		}
		addAndSaveCriterio(new TipoRegistro(criteriosRegistro), registro, analysis);
		if (registro.isValido()) {
			criteriosRegistro.add(Criterio.VALIDO);
		} else {
			criteriosRegistro.add(Criterio.INVALIDO);
		}
		addAndSaveCriterio(new TipoRegistro(criteriosRegistro), registro, analysis);
	}

	private void addAndSaveCriterio(TipoRegistro tipoRegistro, RegistroPrep registro, PrepAnalysis analysis)
			throws IOException {
		analysis.addRegistro(tipoRegistro, registro);
		fileManager.writeRegistro(tipoRegistro, registro.getRegistroOriginal());
	}
}
