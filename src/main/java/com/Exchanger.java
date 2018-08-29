package com;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Exchanger {

    private static final String API_RESOURCE = "https://free.currencyconverterapi.com/";
    private static final String fromCurrencyName = "USD";

    public static double getRates(String toCurrencyName) {
        double rates = 0;
        URL url = null;

        if (toCurrencyName != null && !toCurrencyName.isEmpty()) {
            String currencyRelation = fromCurrencyName + "_" + toCurrencyName;
            String urlName = API_RESOURCE + "api/v5/convert?q=" + currencyRelation + "&compact=y";

            try {
                url = new URL(urlName);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String output = br.readLine();
                conn.disconnect();

                JSONObject jObject = new JSONObject(output);
                JSONObject data = jObject.getJSONObject(currencyRelation);
                rates = data.getDouble("val");

            } catch (MalformedURLException e) {
                System.out.println(1);
                e.printStackTrace();
            } catch (ProtocolException e) {
                System.out.println(2);
                e.printStackTrace();
            } catch (IOException e) {
                System.out.println("No connection to the server");
            }
        }
        return rates;
    }


    public static BigDecimal exchangeResult(String toCurrencyName, double amount) {
        BigDecimal result = new BigDecimal("0.0");
        List<String> currencies = Arrays.asList("UAH", "EUR", "GBP");
        toCurrencyName = toCurrencyName.toUpperCase();
        if (currencies.contains(toCurrencyName)) {
            result = new BigDecimal(amount * getRates(toCurrencyName));
        } else {
            System.out.println("Invalid currency name");
        }
        return result.setScale(2, BigDecimal.ROUND_HALF_EVEN);
    }





    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter currency name (uah, eur, gbp): =>");
        String toCurrencyName = scanner.nextLine();
        System.out.println("Enter currency amount: =>");
        Double amount = scanner.nextDouble();
        System.out.println(amount + " USD = "
                + exchangeResult(toCurrencyName, amount) + " " + toCurrencyName.toUpperCase());
        System.out.println("Press 'Enter' to exit");
        System.in.read();
    }
}
