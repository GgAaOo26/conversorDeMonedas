import com.google.gson.Gson;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.logging.Logger;

public class CurrencyConverter {
    private final HttpClient client;
    private final Gson gson;
    private static final Logger logger = Logger.getLogger(CurrencyConverter.class.getName());

    public CurrencyConverter() {
        this.client = HttpClient.newHttpClient();
        this.gson = new Gson();
    }

    public ExchangeRateResponse getExchangeRate(String fromCurrency) {
        String apiKey = "bc80374f193344d8a5e0f053"; // Reemplaza con tu clave API
        String url = "https://api.exchangerate-api.com/v4/latest/" + fromCurrency + "?apikey=" + apiKey;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return gson.fromJson(response.body(), ExchangeRateResponse.class); // Deserializa a ExchangeRateResponse
            } else {
                logger.severe("Error en la respuesta: " + response.statusCode());
                return null; // Retorna null en caso de error
            }
        } catch (Exception e) {
            logger.severe("Error al obtener la tasa de cambio: " + e.getMessage());
            return null; // Retorna null en caso de error
        }
    }

    public double convertCurrency(double amount, double exchangeRate) {
        return amount * exchangeRate; // Calcula el monto convertido
    }
}
