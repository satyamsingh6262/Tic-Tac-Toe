package com.hoen_scanner;

import io.dropwizard.Application;
import io.dropwizard.setup.Environment;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hoen_scanner.core.SearchResult;
import com.hoen_scanner.resources.SearchResource;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class HoenScannerApplication extends Application<HoenScannerConfiguration> {

    public static void main(String[] args) throws Exception {
        new HoenScannerApplication().run(args);
    }

    @Override
    public void run(HoenScannerConfiguration configuration, Environment environment) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        List<SearchResult> searchResults = new ArrayList<>();

        try (InputStream carStream = getClass().getClassLoader().getResourceAsStream("rental_cars.json")) {
            if (carStream != null) {
                List<SearchResult> cars = mapper.readValue(carStream, new TypeReference<List<SearchResult>>() {});
                searchResults.addAll(cars);
            }
        }

        try (InputStream hotelStream = getClass().getClassLoader().getResourceAsStream("hotels.json")) {
            if (hotelStream != null) {
                List<SearchResult> hotels = mapper.readValue(hotelStream, new TypeReference<List<SearchResult>>() {});
                searchResults.addAll(hotels);
            }
        }

        environment.jersey().register(new SearchResource(searchResults));
    }
}
