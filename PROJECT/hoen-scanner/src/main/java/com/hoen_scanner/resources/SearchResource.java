package com.hoen_scanner.resources;

import com.hoen_scanner.core.Search;
import com.hoen_scanner.core.SearchResult;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.stream.Collectors;

@Path("/search")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SearchResource {

    private final List<SearchResult> searchResults;

    public SearchResource(List<SearchResult> searchResults) {
        this.searchResults = searchResults;
    }

    @POST
    public List<SearchResult> searchCity(Search search) {
        String cityQuery = search.getCity().toLowerCase();
        return searchResults.stream()
                .filter(result -> result.getCity().equalsIgnoreCase(cityQuery))
                .collect(Collectors.toList());
    }
}
