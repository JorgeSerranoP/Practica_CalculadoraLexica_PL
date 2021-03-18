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
		// Creamos el objeto scanner y el arraylist para los símbolos
		Lexer scanner = new Lexer(dataStreamScanner);
		ArrayList<Symbol> simbolos = new ArrayList<Symbol>();

		// Mientras no alcancemos el fin del fichero almacenamos todos los símbolos
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
		
		//Arraylist para almacenar los numeros y operadores por separado
		ArrayList<Double> numeros = new ArrayList<Double>();
		ArrayList<Symbol> operadores = new ArrayList<Symbol>();
		double result = 0;

		// Impresión por pantalla
		System.out.println("RESULTADOS: ");
		if (simbolos.size() == 1) {
			System.out.println("No se han encontrado operaciones");
		} else {
			for (int i = 0; i < simbolos.size(); i++) {
				//Almacenamos los números
				if (simbolos.get(i).type() == 9 || simbolos.get(i).type() == 10) {
					//Almacenamos números precedidos por un - como números negativos
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
				//Almacenamos los operadores
				if (simbolos.get(i).type() == 3 || simbolos.get(i).type() == 5 || simbolos.get(i).type() == 6) {
					operadores.add(simbolos.get(i));
				}
				//Caso en el que hay un - entre dos números 
				if (i != 0 && i != simbolos.size() - 1
						&& (simbolos.get(i - 1).type() == 9 || simbolos.get(i - 1).type() == 10)
						&& (simbolos.get(i + 1).type() == 9 || simbolos.get(i + 1).type() == 10)
						&& simbolos.get(i).type() == 4) {
					operadores.add((new Symbol(3, "+")));
				}
				//Si se detecta ; se comienza a operar
				if (simbolos.get(i).type() == 2) {
					for (int j = 0; j < numeros.size() - 1;) {
						for (int n = 0; n < operadores.size(); n++) {
							//Suma y resta
							if (operadores.get(n).type() == 3) {
								result = numeros.get(j) + numeros.get(j + 1);
								System.out.println(
										"Operacion: " + numeros.get(j) + " + " + numeros.get(j + 1) + " = " + result);
								numeros.set(j + 1, result); //Almacenamos el resultado, de tal manera que funcione como primer operando del siguiente cálculo 
								j += 1;
							//Multiplicación	
							} else if (operadores.get(n).type() == 5) {
								result = numeros.get(j) * numeros.get(j + 1);
								System.out.println(
										"Operacion: " + numeros.get(j) + " * " + numeros.get(j + 1) + " = " + result);
								numeros.set(j + 1, result); //Almacenamos el resultado, de tal manera que funcione como primer operando del siguiente cálculo
								j += 1;
							//División	
							} else if (operadores.get(n).type() == 6) {
								result = numeros.get(j) / numeros.get(j + 1);
								System.out.println(
										"Operacion: " + numeros.get(j) + " / " + numeros.get(j + 1) + " = " + result);
								numeros.set(j + 1, result); //Almacenamos el resultado, de tal manera que funcione como primer operando del siguiente cálculo
								j += 1;
							}
						}

					}
					//Imprimimos resultado final y reseteamos los arraylist y resultado para comenzar la siguiente operación 
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
