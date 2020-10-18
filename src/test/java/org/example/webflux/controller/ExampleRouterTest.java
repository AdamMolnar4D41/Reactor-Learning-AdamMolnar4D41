package org.example.webflux.controller;

import org.example.SpringApplication;
import org.example.webflux.controller.filter.ExampleWebFilter;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = SpringApplication.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ExampleRouterTest {

    @Autowired
    private ExampleRouter exampleRouter;
    @Autowired
    private ExampleWebFilter exampleWebFilter;

    @Test
    public void filterTestOne() {
        WebTestClient client = WebTestClient
                .bindToRouterFunction(exampleRouter.routeExample())
                .webFilter(exampleWebFilter)
                .build();

        client.get()
                .uri("/example")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueMatches("filter-name", "example-web-filter")
                .expectBody(String.class)
                .isEqualTo("foo");
    }

    @Test
    public void filterTestTwo() {
        WebTestClient client = WebTestClient
                .bindToRouterFunction(exampleRouter.route2Example())
                .build();

        client.get()
                .uri("/otherExample/bar")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("bar");
    }

    @Test
    public void filterForbiddenTest() {
        WebTestClient client = WebTestClient
                .bindToRouterFunction(exampleRouter.route2Example())
                .build();

        client.get()
                .uri("/otherExample/FORBIDDEN")
                .exchange()
                .expectStatus().isForbidden();
    }
}