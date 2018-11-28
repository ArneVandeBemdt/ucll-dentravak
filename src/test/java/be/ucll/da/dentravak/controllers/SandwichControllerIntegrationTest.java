package be.ucll.da.dentravak.controllers;

import be.ucll.da.dentravak.Application;
import be.ucll.da.dentravak.model.Sandwich;
import be.ucll.da.dentravak.repositories.SandwichRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.UUID;

import static be.ucll.da.dentravak.model.SandwichTestBuilder.aSandwich;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SandwichControllerIntegrationTest extends AbstractControllerIntegrationTest {

    @Autowired
    private SandwichRepository sandwichRepository;

    @Before
    public void setUpASavedSandwich() {
        sandwichRepository.deleteAll();
    }

    @Test
    public void testGetSandwiches_NoSavedSandwiches_EmptyList() throws JSONException {
        String actualSandwiches = httpGet("/sandwiches");
        String expectedSandwiches = "[]";

        assertThatJson(actualSandwiches).isEqualTo(expectedSandwiches);
    }

    @Test
    public void testPostSandwich() throws JSONException {
        Sandwich sandwich = aSandwich().withName("Americain").withIngredients("Vlees").withPrice(4.0).build();

        String actualSandwichAsJson = httpPost("/sandwiches", sandwich);
        String expectedSandwichAsJson = "{\"id\":\"${json-unit.ignore}\",\"name\":\"Americain\",\"ingredients\":\"Vlees\",\"price\":4}";

        assertThatJson(actualSandwichAsJson).isEqualTo(expectedSandwichAsJson);
    }

    @Test
    public void testPutSandwich() throws JSONException, IOException {
        Sandwich sandwich = aSandwich().withName("Americain").withIngredients("Vlees").withPrice(4.0).build();
        String actualSandwichAsJson = httpPost("/sandwiches", sandwich);

        Sandwich updatedSandwich = new ObjectMapper().readValue(actualSandwichAsJson, Sandwich.class);
        updatedSandwich.setPrice(new BigDecimal("42"));

        String result = httpPut("/sandwiches/" + updatedSandwich.getId(), updatedSandwich);
        String expected = "{\"id\":\"" + updatedSandwich.getId() + "\",\"name\":\"Americain\",\"ingredients\":\"Vlees\",\"price\":42}";

        assertThatJson(result).isEqualTo(expected);
    }

    @Test
    public void testGetSandwiches_WithSavedSandwiches_ListWithSavedSandwich() {
        ArrayList<Sandwich> sandwiches = new ArrayList<>();
        sandwiches.add(aSandwich().withName("Americain").withIngredients("Vlees").withPrice(4.0).build());
        sandwiches.add(aSandwich().withName("bob").withIngredients("lekker").withPrice(3.0).build());
        sandwiches.add(aSandwich().withName("Americain").withIngredients("Vlees").withPrice(4.0).build());

        for (Sandwich sandwich : sandwiches) {
            httpPost("/sandwiches", sandwich);
        }

        String actualSandwiches = httpGet("/sandwiches");
        String expectedSandwiches = jsonTestFile("testGetSandwiches.json");

        assertThatJson(actualSandwiches).isEqualTo(expectedSandwiches);
    }
}
