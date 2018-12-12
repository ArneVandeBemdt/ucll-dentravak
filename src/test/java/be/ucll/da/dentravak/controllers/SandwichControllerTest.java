package be.ucll.da.dentravak.controllers;


import be.ucll.da.dentravak.model.Sandwich;
import be.ucll.da.dentravak.model.SandwichPreferences;
import be.ucll.da.dentravak.repositories.SandwichRepository;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.mockito.Mockito;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestClientException;

import javax.naming.ServiceUnavailableException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class SandwichControllerTest {

    public void testSorting() throws ServiceUnavailableException {
        SandwichRepository repositoryMock = Mockito.mock(SandwichRepository.class);
        Mockito.when(repositoryMock.findAll()).thenReturn(
                //TODO: add 3 sandwiches with same id
                new ArrayList<>()
        );
        SandwichController controller = new SandwichController(repositoryMock) {

            @Override
            public SandwichPreferences getPreferences(@PathVariable String phoneNr) throws RestClientException, ServiceUnavailableException {
                SandwichPreferences sandwichPreferences = new SandwichPreferences();
                sandwichPreferences.put(id broodje 1, 4.2);
                sandwichPreferences.put(id broodje 2, 1.2);
                sandwichPreferences.put(id broodje 3, 3.6);
                return sandwichPreferences;
            }
        };

        List<Sandwich> sandwichesSortedByRecommendations = controller.getSandwichesSortedByRecommendations();

        assertThat(sandwichesSortedByRecommendations)
                .containsExactly(sandwich1, sandwich3, sandwich2);
    }
}
