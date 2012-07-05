package mafr.myprep;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RegistroPrepFactory {

	private static final String TRUE_INTEGER = "1";

	// public static final Acta create(String line) throws
	// NumberFormatException, ParseException {
	// String[] fields = line.split("\\|");
	// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	// return new Acta(Short.valueOf(fields[0]), Integer.valueOf(fields[1]),
	// Integer.valueOf(fields[2]), fields[3],
	// fields[4], Short.valueOf(fields[5]), Integer.valueOf(fields[6]),
	// Integer.valueOf(fields[7]),
	// Integer.valueOf(fields[8]), Integer.valueOf(fields[9]),
	// Integer.valueOf(fields[10]),
	// Integer.valueOf(fields[11]), Integer.valueOf(fields[12]),
	// Integer.valueOf(fields[13]),
	// Integer.valueOf(fields[14]), Integer.valueOf(fields[15]),
	// Integer.valueOf(fields[16]),
	// Integer.valueOf(fields[17]), Integer.valueOf(fields[18]),
	// Integer.valueOf(fields[19]),
	// Integer.valueOf(fields[20]), Integer.valueOf(fields[21]),
	// Integer.valueOf(fields[22]),
	// Integer.valueOf(fields[23]), fields[24], readBoolean(fields[25]),
	// fields[26], sdf.parse(fields[27]),
	// sdf.parse(fields[28]), sdf.parse(fields[28]));
	// }

	public static final RegistroPrep create(String line) throws NoSuchMethodException, SecurityException,
			InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		boolean legible = true;
		String[] fields = line.split("\\|");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		Constructor<?> constructor = RegistroPrep.class.getConstructors()[0];
		Class<?>[] parameterTypes = constructor.getParameterTypes();
		List<Object> args = new ArrayList<Object>();
		for (int i = 0; i < fields.length; i++) {
			Class<?> clazz = parameterTypes[i];
			Constructor<?> argConstructor = clazz.getConstructor(String.class);
			if (clazz.equals(Boolean.class)) {
				args.add(readBoolean(fields[i]));
			} else if (clazz.equals(Date.class)) {
				try {
					args.add(sdf.parse(fields[i]));
				} catch (Exception e) {
					args.add(null);
					legible = false;
				}
			} else {
				try {
					Object arg = argConstructor.newInstance(fields[i]);
					args.add(arg);
				} catch (Exception e) {
					args.add(null);
					legible = false;
				}
			}
		}
		RegistroPrep acta = (RegistroPrep) constructor.newInstance(args.get(0), args.get(1), args.get(2), args.get(3), args.get(4),
				args.get(5), args.get(6), args.get(7), args.get(8), args.get(9), args.get(10), args.get(11),
				args.get(12), args.get(13), args.get(14), args.get(15), args.get(16), args.get(17), args.get(18),
				args.get(19), args.get(20), args.get(21), args.get(22), args.get(23), args.get(24), args.get(25),
				args.get(26), args.get(27), args.get(28), args.get(29), legible, line);
		return acta;
	}

	private static Boolean readBoolean(String string) {
		return TRUE_INTEGER.equals(string.trim());
	}

}
