package converter;

import com.google.gson.*;
import com.sun.istack.internal.Nullable;

import java.lang.reflect.Type;
import java.net.URL;
import java.util.Map;
import java.util.Set;
import java.util.Scanner;

public class TinkoffConverter {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Hello, Converter!");
        System.out.println("Enter from currency: ");
        String fromCurrency = scanner.next();

        System.out.println("Enter to currency: ");
        String toCurrency = scanner.next();

        URL url = makeApiUrl(fromCurrency, toCurrency);
        String response = getApiResponse(url);

        Gson gson = new GsonBuilder().registerTypeAdapter(RateObject.class, new RatesDeserializer()).create();
        gson.fromJson(null, ApiResponse.class);
    }

    public class ApiResponse {
        private String base;
        private RateObject rates;
    }

    public class RateObject {
        private String name;
        private double rate;

        public RateObject(String name, double rate) {
            this.name = name;
            this.rate = rate;
        }
    }

    public class RatesDeserializer implements JsonDeserializer<RateObject> {

        @Nullable
        public RateObject deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            RateObject rate = null;
            if (json.isJsonObject()) {
                Set<Map.Entry<String, JsonElement>> entries = json.getAsJsonObject().entrySet();
                if (entries.size() > 0) {
                    Map.Entry<String, JsonElement> entry = entries.iterator().next();
                    rate = new RateObject(entry.getKey(), entry.getValue().getAsDouble());
                }
            }
            return rate;
        }
    }


}
