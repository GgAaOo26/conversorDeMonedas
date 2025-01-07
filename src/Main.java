import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        CurrencyConverter converter = new CurrencyConverter();
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        // Mapa de monedas disponibles
        Map<String, String> currencyCodes = new HashMap<>();
        currencyCodes.put("ARS", "Peso argentino");
        currencyCodes.put("BRL", "Real brasileño");
        currencyCodes.put("COP", "Peso colombiano");
        currencyCodes.put("USD", "Dólar estadounidense");
        // Agrega más monedas según sea necesario

        while (!exit) {
            System.out.println("=== Conversor de Monedas ===");
            System.out.println("1. Convertir moneda");
            System.out.println("2. Salir");
            System.out.print("Seleccione una opción: ");

            int option = 0;
            try {
                option = scanner.nextInt();
                scanner.nextLine(); // Limpiar el buffer
            } catch (Exception e) {
                System.out.println("Entrada no válida. Por favor, ingrese un número.");
                scanner.nextLine(); // Limpiar el buffer para evitar un bucle infinito
                continue; // Volver al inicio del bucle
            }

            switch (option) {
                case 1:
                    System.out.println("Monedas disponibles para conversión:");
                    for (Map.Entry<String, String> entry : currencyCodes.entrySet()) {
                        System.out.println(entry.getKey() + " - " + entry.getValue());
                    }

                    System.out.print("Ingrese la moneda base (ej. USD): ");
                    String fromCurrency = scanner.nextLine().toUpperCase();

                    // Validar moneda base
                    if (!currencyCodes.containsKey(fromCurrency)) {
                        System.out.println("La moneda base no es válida. Por favor, seleccione una moneda válida.");
                        continue; // Volver al inicio del bucle
                    }

                    System.out.print("Ingrese la moneda a convertir (ej. ARS): ");
                    String toCurrency = scanner.nextLine().toUpperCase();

                    // Validar moneda a convertir
                    if (!currencyCodes.containsKey(toCurrency)) {
                        System.out.println("La moneda a convertir no es válida. Por favor, seleccione una moneda válida.");
                        continue; // Volver al inicio del bucle
                    }

                    ExchangeRateResponse response = converter.getExchangeRate(fromCurrency);

                    if (response != null) {
                        Double exchangeRate = response.getRates().get(toCurrency);
                        if (exchangeRate != null) {
                            double amount = 0;
                            boolean validAmount = false;

                            // Manejo de entrada para el monto a convertir
                            while (!validAmount) {
                                System.out.print("Ingrese el monto a convertir: ");
                                try {
                                    amount = scanner.nextDouble();
                                    if (amount < 0) {
                                        System.out.println("El monto no puede ser negativo. Intente nuevamente.");
                                    } else {
                                        validAmount = true; // Salir del bucle si la entrada es válida
                                    }
                                } catch (Exception e) {
                                    System.out.println("Entrada no válida. Por favor, ingrese un número.");
                                    scanner.nextLine(); // Limpiar el buffer
                                }
                            }

                            double convertedAmount = converter.convertCurrency(amount, exchangeRate);
                            System.out.printf("El monto convertido de %.2f %s a %s es: %.2f%n", amount, fromCurrency, toCurrency, convertedAmount);
                        } else {
                            System.out.println("La moneda a convertir no está disponible.");
                        }
                    } else {
                        System.out.println("No se pudo obtener la tasa de cambio.");
                    }
                    break;

                case 2:
                    exit = true;
                    System.out.println("Saliendo del programa...");
                    break;

                default:
                    System.out.println("Opción no válida. Por favor, seleccione una opción válida.");
            }
        }

        scanner.close();
    }
}