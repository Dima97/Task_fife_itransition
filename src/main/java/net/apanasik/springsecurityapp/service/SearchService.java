package net.apanasik.springsecurityapp.service;

import net.apanasik.springsecurityapp.model.SearchComponent;

import java.util.List;

public interface SearchService {
    void search(String query);
    boolean hasNext();
    List<SearchComponent> next();
    boolean loading();
}
