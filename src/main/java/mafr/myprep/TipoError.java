package mafr.myprep;

public enum TipoError {
	
	ERROR_TIPO_ACTA("El acta no es de tipo presidencial."),
	ERROR_SUMA_VOTOS("El total calculado por el PREP es incorrecto."),
	ERROR_EXCEDE_LISTA_NOMINAL("El total de votos excede la lista nominal."),
	ERROR_VALOR_NEGATIVO("Se capturó un valor negativo.");
	
	final String descripcion;

	TipoError(String descripcion) {
		this.descripcion = descripcion;
	}
}