package net.apanasik.springsecurityapp.service;

import net.apanasik.springsecurityapp.model.SearchComponent;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


@Service
public class SearchServiceImpl implements SearchService {

    private final static Logger logger = LoggerFactory.getLogger(SearchServiceImpl.class);

    private final static String LOGGER_ERROR_MESSAGE_PATTERN = "Exception during processing search query : %s%nException: %s%n";
    private final static String BELCHIP_URL = "http://belchip.by/";
    private final static String CHIPDIP_URL = "https://www.ru-chipdip.by/";
    private final static String BELCHIP_SEARCH_URL_PREFIX = BELCHIP_URL + "search/?query=";
    private final static String CHIPDIP_SEARCH_URL_PREFIX = CHIPDIP_URL + "/search?searchtext=";
    private final static int FIRST_STEEP = 20;
    private final static int STEEP = 10;

    private ArrayList<String> categoryLinks = new ArrayList<>();
    private List<SearchComponent> resultsCash;
    private int lastIndex;

    private Elements pages;
    private int pageIndex = 0;

    @Override
    public void search(String query) {
        List<SearchComponent> result = searchOnBelchip(query);
        result.addAll(searchOnChipdip(query));
        resultsCash = result;
        lastIndex = 0;
    }

    @Override
    public boolean hasNext() {
        return lastIndex < resultsCash.size() - 1;
    }

    @Override
    public List<SearchComponent> next() {
        List<SearchComponent> nextPiece = resultsCash.subList(lastIndex,
                Math.min(lastIndex + (lastIndex == 0? FIRST_STEEP : STEEP),resultsCash.size()-1));
        lastIndex = Math.min(lastIndex + (lastIndex == 0 ? FIRST_STEEP : STEEP), resultsCash.size()-1);
        return nextPiece;
    }

    @Override
    public boolean loading() {
        if (pages == null) {
            return false;
        } else if (pageIndex >= pages.size()) {
            return false;
        } else {
            resultsCash.addAll(loadPage());
            return true;
        }
    }

    private List<SearchComponent> loadPage() {
        Element li = pages.get(pageIndex++);
        Element a  = li.getElementsByTag("a").first();
        String href = a.attr("href");
        return searchOnChipdip(href.substring(CHIPDIP_SEARCH_URL_PREFIX.length()));
    }

    private List<SearchComponent> searchOnBelchip(String query) {
        List<SearchComponent> result = new LinkedList<>();
        try {
            Document doc = Jsoup.connect(BELCHIP_SEARCH_URL_PREFIX + query).get();
            result.addAll(getBelchipComponents(doc));
        } catch (IOException e) {
            logger.error(String.format(LOGGER_ERROR_MESSAGE_PATTERN, query, e));
        }
        return result;
    }

    private List<SearchComponent> searchOnChipdip(String query) {
        List<SearchComponent> result = new LinkedList<>();
        try {
            Document doc = Jsoup.connect(CHIPDIP_SEARCH_URL_PREFIX + query).get();
            findCategory(doc);
            result.addAll(getChipdipComponents(doc));
            for (String pageNumber: categoryLinks) {
                doc = Jsoup.connect(CHIPDIP_URL + pageNumber).get();
                result.addAll(getChipdipComponents(doc));
            }
        } catch (IOException e) {
            logger.error(String.format(LOGGER_ERROR_MESSAGE_PATTERN, query, e));
        }
        return result;
    }

    private List<SearchComponent> getBelchipComponents(Document doc) throws UnsupportedEncodingException {
        List<SearchComponent> result = new LinkedList<>();
        Elements items = doc.getElementsByClass("cat-item");
        for (Element item : items) {
            SearchComponent current = new SearchComponent();
            Element image = item.getElementsByClass("cat-pic").first().getElementsByTag("a").get(1);
            image.attr("href", BELCHIP_URL + image.attr("href"));
            Element imageSource = image.getElementsByTag("img").first();
            imageSource.attr("src", BELCHIP_URL + imageSource.attr("src"));
            image.text(imageSource.toString());
            Element description = item.getElementsByTag("h3").first().getElementsByTag("a").first();
            description.attr("href",BELCHIP_URL + description.attr("href"));
            Elements price = item.getElementsByClass("denoPrice");
            current.setImage(image.text());
            current.setDescription(description.toString());
            if (price.size() == 0){
                current.setPrice("цена по запросу");
            }else current.setPrice(price.text());
            result.add(current);
        }
        return result;
    }
    private List<SearchComponent> getChipdipComponents(Document doc) {
        List<SearchComponent> result = new LinkedList<>();
        Elements items = doc.getElementsByClass("with-hover");
        for (Element item : items) {
            SearchComponent current = new SearchComponent();
            Element image = item.getElementsByClass("img").first()
                    .getElementsByClass("img75").first();
            if(image != null) {
                image.attr("class", "");
                current.setImage(image.toString());
            }
            else  current.setImage("");
            Element description = item.getElementsByClass("h_name").first()
                    .getElementsByClass("link").first();
            description.attr("href",
                    CHIPDIP_URL.substring(0, CHIPDIP_URL.length() - 1) + description.attr("href"));
            Elements price = item.getElementsByClass("price_mr");
            current.setDescription(description.toString());
            current.setPrice(price.text());
            result.add(current);
        }
        if (pageIndex == 0) {
            pages = doc.getElementsByClass("pager__page");
            pageIndex = 1;
        }
        return result;
    }
    private ArrayList<String> findCategory(Document doc){
        Elements items = doc.getElementsByClass("pager__page");
        for(Element item: items){
            Elements linkCategory = item.getElementsByAttribute("href");
            categoryLinks.add(linkCategory.attr("href"));
        }
        categoryLinks.remove(0);
        return categoryLinks;
    }
}
