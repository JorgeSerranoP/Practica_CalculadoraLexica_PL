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
		System.out.println("Leyendo entrada de fichero... ");
		FileInputStream dataStreamScanner = new FileInputStream(args[0]);
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
		int contadorMenos = 0;
		int contadorMenos1 = 0;

		// Impresion por pantalla
		System.out.println("RESULTADO: ");
		if (simbolos.isEmpty()) {
			System.out.println("No se han encontrado operaciones");
		} else {
			for (int i = 0; i < simbolos.size(); i++) {
				if (simbolos.get(i).type() == 9) {
					if (i != 0) {
						if (simbolos.get(i - 1).type() == 4) {
							numeros.add(Double.valueOf((String) simbolos.get(i).value()) * -1);
						} else {
							numeros.add(Double.valueOf((String) simbolos.get(i).value()));
						}
					} else {
						numeros.add(Double.valueOf((String) simbolos.get(i).value()));
					}
				}
				if (simbolos.get(i).type() == 3 || simbolos.get(i).type() == 5 || simbolos.get(i).type() == 6) {
					operadores.add(simbolos.get(i));
				}
				if (i != 0 && i != simbolos.size() - 1 && simbolos.get(i - 1).type() == 9
						&& simbolos.get(i + 1).type() == 9 && simbolos.get(i).type() == 4) {
					operadores.add((new Symbol(3, "+")));
					contadorMenos1++;
				}
				if (simbolos.get(i).type() == 4) {
					contadorMenos++;
				}
				if (simbolos.get(i).type() == 2) {
					for (int j = 0; j < numeros.size() - 1;) {
						if (operadores.size() == 0) {
							for (int k = 0; k < contadorMenos; k++) {
								result = numeros.get(j) + numeros.get(j + 1);
								numeros.set(j + 1, result);
								System.out.println("Operacion: " + numeros.get(j) + " - " + numeros.get(j + 1) + " = " + result);
								j += 1;
							}
						}
						for (int n = 0; n < operadores.size() /* + contadorMenos1 */; n++) {
							if (operadores.get(n).type() == 3) {
								result = numeros.get(j) + numeros.get(j + 1);
								numeros.set(j + 1, result);
								System.out.println("Operacion: " + numeros.get(j) + " + " + numeros.get(j + 1) + " = " + result);
								j += 1;
							} else if (operadores.get(n).type() == 5) {
								result = numeros.get(j) * numeros.get(j + 1);
								System.out.println("Operacion: " + numeros.get(j) + " * " + numeros.get(j + 1) + " = " + result);
								numeros.set(j + 1, result);
								j += 1;
							} else if (operadores.get(n).type() == 6) {
								result = numeros.get(j) / numeros.get(j + 1);
								System.out.println("Operacion: " + numeros.get(j) + " / " + numeros.get(j + 1) + " = " + result);
								numeros.set(j + 1, result);
								j += 1;
							}
						}

					}
					System.out.println("Resultado: " + result);
					numeros.clear();
					operadores.clear();
					result = 0;
				}
			}
		}

		System.out.println("\n\n -- Bye-bye -- ");
	}
}
