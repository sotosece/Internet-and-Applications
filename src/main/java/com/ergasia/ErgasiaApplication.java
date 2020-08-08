package com.ergasia;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

@SpringBootApplication
@RestController
public class ErgasiaApplication {

    private static Logger logger = Logger.getLogger(ErgasiaApplication.class.getName());

    public static void main(String[] args) {
        SpringApplication.run(ErgasiaApplication.class, args);
    }

    @GetMapping("/")
    public String getDefault() {
        return "Hello motherfucker!";
    }

    @GetMapping("/exchange")
    public List<Exchange> getExchange(@RequestParam(value = "amount", defaultValue = "1.0") Double amount,
                                      @RequestParam(value = "from", defaultValue = "EUR") Currency from,
                                      @RequestParam(value = "to", defaultValue = "USD") List<Currency> to) {
        try {
            List<Exchange> exchanges = new ArrayList<>();
            for (Currency currency : to) {
                exchanges.add(createExchange(amount, from, currency));
            }
            return exchanges;
        } catch (IOException | NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }

    private Exchange createExchange(Double amount, Currency from, Currency to) throws IOException, NumberFormatException {
        OkHttpClient client = new OkHttpClient();
        String urlString = String.format("https://currency-exchange.p.rapidapi.com/exchange?from=%s&to=%s", from, to);
        Request request = new Request.Builder()
                .url(urlString)
                .get()
                .addHeader("x-rapidapi-host", "currency-exchange.p.rapidapi.com")
                .addHeader("x-rapidapi-key", "4940982bf2msh7cf1dbbb542bf54p1b0e17jsn22993130dd97")
                .build();
        Response response = client.newCall(request).execute();
        Double rate = Double.parseDouble(Objects.requireNonNull(response.body()).string());
        return new Exchange.Builder()
                .withAmount(amount)
                .withFromCurrency(from)
                .withToCurrency(to)
                .withRate(rate)
                .withExchangeAmount(rate*amount)
                .build();
    }
}
