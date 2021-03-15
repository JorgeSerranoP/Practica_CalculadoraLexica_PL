package lex.manual;

import lex.generado.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

class Driver {
	public static void main(String args[]) throws IOException {
		// Entrada de datos: fichero especificado en los argumentos
		FileInputStream dataStreamBuffer = new FileInputStream(args[0]);
		FileInputStream dataStreamScanner = new FileInputStream(args[0]);

		System.out.println("Leyendo entrada de fichero... ");
		InputStreamReader isr = new InputStreamReader(dataStreamBuffer, "utf8");
		BufferedReader br = new BufferedReader(isr);

		String linea;
		int lineas = 0;
		int caracteres = 0;
		while ((linea = br.readLine()) != null) {
			lineas++;
			caracteres += linea.length();
		}

		// Creamos el objeto scanner y los arraylist para cada tipo de simbolo
		Lexer scanner = new Lexer(dataStreamScanner);
		ArrayList<Symbol> simbolos = new ArrayList<Symbol>();

		// Mientras no alcancemos el fin del fichero
		boolean end = false;
		while (!end) {
			try {
				Symbol token = scanner.next_token();
				end = (token.value() == null);
				simbolos.add(token);
			} catch (Exception x) {
				System.out.println("Ups... algo ha ido mal");
				x.printStackTrace();
			}
		}

		simbolos.trimToSize();
		ArrayList<Double> numeros = new ArrayList<Double>();
		ArrayList<Symbol> operadores = new ArrayList<Symbol>();
		double result = 0;

		// Impresion por pantalla
		System.out.println("RESULTADO: ");
		if (simbolos.isEmpty()) {
			System.out.println("No se han encontrado operaciones");
		} else {
			for (int i = 0; i < simbolos.size(); i++) {
				if (simbolos.get(i).type() == 9) {
					numeros.add(Double.valueOf((String) simbolos.get(i).value()));
				}
				if (simbolos.get(i).type() == 3 || simbolos.get(i).type() == 4 || simbolos.get(i).type() == 5
						|| simbolos.get(i).type() == 6) {
					operadores.add(simbolos.get(i));
				}
				if (simbolos.get(i).type() == 2) {
					for (int j = 0; j < numeros.size() - 1;) {
						for (int n = 0; n < operadores.size(); n++) {
							if (operadores.get(n).type() == 3) {
								result = numeros.get(j) + numeros.get(j + 1);
								numeros.set(j + 1, result);
								j += 1;
							} else if (operadores.get(n).type() == 4) {
								result = numeros.get(j) - numeros.get(j + 1);
								numeros.set(j + 1, result);
								j += 1;
							} else if (operadores.get(n).type() == 5) {
								result = numeros.get(j) * numeros.get(j + 1);
								numeros.set(j + 1, result);
								j += 1;
							} else if (operadores.get(n).type() == 6) {
								result = numeros.get(j) / numeros.get(j + 1);
								numeros.set(j + 1, result);
								j += 1;
							}
						}
						
					}					
				}				
			}
			System.out.println(result);
		}

		System.out.println("\n\n -- Bye-bye -- ");
	}
}
