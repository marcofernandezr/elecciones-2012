package mafr.myprep;

public interface PrepStats {

	long getListaNominal();
	long getVotos();
	double getParticipacionCiudadana();
	double getPorcentajeEPN();
	double getPorcentajeAMLO();
	double getPorcentajeJVM();
	double getPorcentajeGQT();
	double getPorcentajeNulos();
	double getPorcentajeNoRegistrados();
	long getRegistros();
	long getEPN();
	long getAMLO();
	long getJVM();
	long getGQT();
	long getNulos();
	long getNoRegistrados();		
	long getCasosFaltantesEPN();
	long getCasosFaltantesAMLO();
	long getCasosFaltantesJVM();
	long getCasosFaltantesGQT();
	long getCasosFaltantesNulos();
	long getCasosFaltantesNoRegistrados();
	long getCasosErrorExcedeListaNominal();
	long getCasosErrorTipoActa();
}
