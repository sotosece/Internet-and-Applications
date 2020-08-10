package com.ergasia;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @GetMapping("/exchange")
    public List<Exchange> getExchange(@RequestParam(value = "amount", defaultValue = "1.0") Double amount,
                                      @RequestParam(value = "from", defaultValue = "EUR") Currency from,
                                      @RequestParam(value = "to", defaultValue = "SGD,MYR,EUR,USD,AUD,JPY,CNH,HKD,CAD,INR,DKK,GBP,RUB,NZD,MXN,IDR,TWD,THB,VND") List<Currency> to) {
        List<Exchange> exchanges = new ArrayList<>();
        if (to.contains(from)) {
            to.remove(from);
        }
        for (Currency currency : to) {
            try {
                exchanges.add(createExchange(amount, from, currency));
            } catch (IOException | NumberFormatException e) {
                logger.info(currency.toString());
//                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
            }
        }
        return exchanges;
    }

    private Exchange createExchange(Double amount, Currency from, Currency to) throws IOException, NumberFormatException {
        OkHttpClient client = new OkHttpClient();
        String urlString = String.format("https://currency-exchange.p.rapidapi.com/exchange?from=%s&to=%s", from, to);
        Request request = new Request.Builder()
                .url(urlString)
                .get()
                .addHeader("x-rapidapi-host", "currency-exchange.p.rapidapi.com")
                .addHeader("x-rapidapi-key", "1f564bf68fmsh0f09bfc790f0ba1p1140f4jsn4162c423f4fc")
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
