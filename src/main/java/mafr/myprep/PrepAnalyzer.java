package mafr.myprep;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PrepAnalyzer {

	private final FileManager fileManager = new FileManager();
	private final PrepAnalysis analysis = new PrepAnalysis();

	public synchronized PrepAnalysis execute(final String baseDir) throws Exception {
		fileManager.start(baseDir);
		analysis.reset();
		processRegistros();
		createAdditionalInfoStats();
		fileManager.writeAnalysis(analysis);
		fileManager.stop();
		return new PrepAnalysis(analysis);
	}

	private void processRegistros() throws IOException {
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
			addCriterios(registro);
		}
	}

	private void addAndSaveCriterio(TipoRegistro tipoRegistro, RegistroPrep registro)
			throws IOException {
		analysis.addRegistro(tipoRegistro, registro);
		fileManager.writeRegistro(tipoRegistro, registro.getRegistroOriginal());
	}

	private void addCriterios(RegistroPrep registro) throws IOException {
		List<Criterio> criteriosRegistro = new ArrayList<Criterio>();
		if (registro.isCompleto()) {
			criteriosRegistro.add(Criterio.COMPLETO);
		} else {
			criteriosRegistro.add(Criterio.INCOMPLETO);
		}
		addAndSaveCriterio(new TipoRegistro(criteriosRegistro), registro);
		if (registro.isContabilizado()) {
			criteriosRegistro.add(Criterio.CONTABILIZADO);
		} else {
			criteriosRegistro.add(Criterio.SIN_CONTABILIZAR);
		}
		addAndSaveCriterio(new TipoRegistro(criteriosRegistro), registro);
		if (registro.isCasillaEspecial()) {
			criteriosRegistro.add(Criterio.CASILLA_ESPECIAL);
		} else if (registro.isCasillaExtraordinaria()) {
			criteriosRegistro.add(Criterio.CASILLA_EXTRAORDINARIA);
		} else {
			criteriosRegistro.add(Criterio.CASILLA_NORMAL);
		}
		addAndSaveCriterio(new TipoRegistro(criteriosRegistro), registro);
		if (registro.isValido()) {
			criteriosRegistro.add(Criterio.VALIDO);
		} else {
			criteriosRegistro.add(Criterio.INVALIDO);
		}
		addAndSaveCriterio(new TipoRegistro(criteriosRegistro), registro);
	}

	private void createAdditionalInfoStats() {
		analysis.createStats(new TipoRegistro(Criterio.TODOS), 
				new TipoRegistro(Criterio.COMPLETO), 
				new TipoRegistro(Criterio.INCOMPLETO));

		analysis.createStats(new TipoRegistro(Criterio.CONTABILIZADO), 
				new TipoRegistro(Criterio.COMPLETO,Criterio.CONTABILIZADO), 
				new TipoRegistro(Criterio.INCOMPLETO, Criterio.CONTABILIZADO));

		analysis.createStats(new TipoRegistro(Criterio.SIN_CONTABILIZAR), 
				new TipoRegistro(Criterio.COMPLETO, Criterio.SIN_CONTABILIZAR), 
				new TipoRegistro(Criterio.INCOMPLETO, Criterio.SIN_CONTABILIZAR));

		analysis.createStats(new TipoRegistro(Criterio.CASILLA_ESPECIAL, Criterio.CONTABILIZADO), 
				new TipoRegistro(Criterio.COMPLETO, Criterio.CONTABILIZADO, Criterio.CASILLA_ESPECIAL), 
				new TipoRegistro(Criterio.INCOMPLETO, Criterio.CONTABILIZADO, Criterio.CASILLA_ESPECIAL));

		analysis.createStats(new TipoRegistro(Criterio.CASILLA_EXTRAORDINARIA, Criterio.CONTABILIZADO),
				new TipoRegistro(Criterio.COMPLETO, Criterio.CONTABILIZADO, Criterio.CASILLA_EXTRAORDINARIA),
				new TipoRegistro(Criterio.INCOMPLETO, Criterio.CONTABILIZADO, Criterio.CASILLA_EXTRAORDINARIA));

		analysis.createStats(new TipoRegistro(Criterio.SIN_CONTABILIZAR, Criterio.INVALIDO), 
				new TipoRegistro(Criterio.COMPLETO, Criterio.SIN_CONTABILIZAR, Criterio.CASILLA_NORMAL, Criterio.INVALIDO),
				new TipoRegistro(Criterio.COMPLETO, Criterio.SIN_CONTABILIZAR, Criterio.CASILLA_EXTRAORDINARIA,Criterio.INVALIDO), 
				new TipoRegistro(Criterio.COMPLETO, Criterio.SIN_CONTABILIZAR, Criterio.CASILLA_ESPECIAL, Criterio.INVALIDO), 
				new TipoRegistro(Criterio.INCOMPLETO, Criterio.SIN_CONTABILIZAR, Criterio.CASILLA_NORMAL, Criterio.INVALIDO), 
				new TipoRegistro(Criterio.INCOMPLETO, Criterio.SIN_CONTABILIZAR, Criterio.CASILLA_EXTRAORDINARIA, Criterio.INVALIDO), 
				new TipoRegistro(Criterio.INCOMPLETO, Criterio.SIN_CONTABILIZAR,Criterio.CASILLA_ESPECIAL, Criterio.INVALIDO));
	}

}
