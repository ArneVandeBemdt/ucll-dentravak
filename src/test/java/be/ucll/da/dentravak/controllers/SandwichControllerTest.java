package be.ucll.da.dentravak.controllers;


import be.ucll.da.dentravak.model.Sandwich;
import be.ucll.da.dentravak.model.SandwichPreferences;
import be.ucll.da.dentravak.repositories.SandwichRepository;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestClientException;

import javax.naming.ServiceUnavailableException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static be.ucll.da.dentravak.model.SandwichTestBuilder.aSandwich;
import static org.assertj.core.api.Assertions.assertThat;

public class SandwichControllerTest {

    @Test
    public void testSorting() throws ServiceUnavailableException {
        SandwichRepository repositoryMock = Mockito.mock(SandwichRepository.class);

        UUID sandwichId1 = UUID.fromString("70fd479-f931-43b6-ab53-b37b973923b6");
        UUID sandwichId2 = UUID.fromString("888d39d5-1cac-40d7-a8e6-c34788ab9b84");
        UUID sandwichId3 = UUID.fromString("f563bb41-4d4c-4190-90cd-c1319a8cdbeb");

        Sandwich sandwich1 = aSandwich().withId(sandwichId1).withName("Smos gezond").withPrice(2.50).build();
        Sandwich sandwich2 = aSandwich().withId(sandwichId2).withName("Broodje zalm").withPrice(5.10).build();
        Sandwich sandwich3 = aSandwich().withId(sandwichId3).withName("Smos hesp").withPrice(3.30).build();

        List<Sandwich> sandwiches = new ArrayList<>();
        sandwiches.add(sandwich1);
        sandwiches.add(sandwich2);
        sandwiches.add(sandwich3);

        Mockito.when(repositoryMock.findAll()).thenReturn(sandwiches);

        SandwichController controller = new SandwichController(repositoryMock) {

            @Override
            public SandwichPreferences getPreferences(@PathVariable String phoneNr) throws RestClientException, ServiceUnavailableException {
                SandwichPreferences sandwichPreferences = new SandwichPreferences();
                sandwichPreferences.put(sandwichId1, new Float(4.2));
                sandwichPreferences.put(sandwichId2, new Float(1.2));
                sandwichPreferences.put(sandwichId3, new Float(3.6));
                return sandwichPreferences;
            }
        };

        List<Sandwich> sandwichesSortedByRecommendations = controller.getSandwichesSortedByRecommendations();

        assertThat(sandwichesSortedByRecommendations)
                .containsExactly(sandwich1, sandwich3, sandwich2);
    }
}
