package com.ys.datatool.service.tool;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ys.datatool.domain.entity.City;
import com.ys.datatool.domain.entity.Country;
import com.ys.datatool.domain.entity.Province;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mo on 2019/2/24
 */
public class JsonFormatTool {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testAreaJson() throws IOException {
        List<Province> provinces = new ArrayList<>();

        String provinceFilePath = "d:\\json\\province.json";
        String cityFilePath = "d:\\json\\City.json";
        String countryFilePath = "d:\\json\\Country.json";

        JsonNode provinceRootNode = MAPPER.readTree(new File(provinceFilePath));
        JsonNode cityRootNode = MAPPER.readTree(new File(cityFilePath));
        JsonNode countryRootNode = MAPPER.readTree(new File(countryFilePath));


        for (JsonNode provinceNode : provinceRootNode) {
            Province province = new Province();
            province.setId(provinceNode.get("id").asText());
            province.setName(provinceNode.get("name").asText());
            provinces.add(province);
        }

        if (provinces.size() > 0) {
            for (Province province : provinces) {
                String id = province.getId();
                List<City> cities = new ArrayList<>();

                JsonNode cityNodeArray = cityRootNode.get(id);
                for (JsonNode cityNode : cityNodeArray) {
                    City city = new City();
                    city.setId(cityNode.get("id").asText());
                    city.setName(cityNode.get("name").asText());
                    city.setProvince(cityNode.get("province").asText());
                    cities.add(city);
                }
                province.setCities(cities);
            }
        }

        if (provinces.size() > 0) {
            for (Province province : provinces) {
                List<City> cities = province.getCities();

                for (City city : cities) {
                    String id = city.getId();
                    List<Country> countries = new ArrayList<>();

                    System.out.println("id为" + id);
                    JsonNode countryNodeArray = countryRootNode.get(id);
                    if (countryNodeArray != null) {
                        for (JsonNode countryNode : countryNodeArray) {
                            Country country = new Country();
                            country.setId(countryNode.get("id").asText());
                            country.setName(countryNode.get("name").asText());
                            country.setCity(countryNode.get("city").asText());
                            countries.add(country);
                        }
                        city.setCountries(countries);
                    }
                }
            }
        }

        if (provinces.size()>0){
            String pathname = "d:\\json\\area.json";
            File file = new File(pathname);
            MAPPER.writeValue(file, provinces);
        }
    }
}
