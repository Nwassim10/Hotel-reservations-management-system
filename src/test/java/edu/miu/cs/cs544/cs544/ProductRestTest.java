package edu.miu.cs.cs544.cs544;

import edu.miu.cs.cs544.domain.ProductType;
import edu.miu.cs.cs544.domain.dto.ProductDTO;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@ExtendWith(RetryExtension.class)
class ProductRestTest {

    @BeforeAll
    public static void setUp() {
        RestAssured.port = 8080;
        RestAssured.baseURI = "http://localhost/api";
    }

    @Test
    public void testGetAllProducts() {
        given()
                .when()
                .get("/products")
                .then()
                .statusCode(200);
    }

    @Test
    public void testGetProduct() {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("Test Product");
        productDTO.setExcerpt("Test Product");
        productDTO.setDescription("This is a test product");
        productDTO.setType(ProductType.Room);
        productDTO.setMaxCapacity(2);
        productDTO.setNightlyRate(100.0);

        // Send POST request to add the product and capture the response
        Response response = given()
                .body(productDTO)
                .contentType(ContentType.JSON)
                .when()
                .post("/products")
                .then()
                .statusCode(201)
                .extract()
                .response();

        // Extract the ID of the newly created product from the response
        int id = response.jsonPath().getInt("id");

        // Send GET request to get the product using the extracted ID
        given()
                .when()
                .get("/products/" + id)
                .then()
                .statusCode(200)
                .body("name", equalTo("Test Product"))
                .body("excerpt", equalTo("Test Product"))
                .body("description", equalTo("This is a test product"))
                .body("type", equalTo("Room"))
                .body("maxCapacity", equalTo(2))
                .body("nightlyRate", equalTo(100.0f));

        // Send DELETE request to clean the new product
        given()
                .when()
                .delete("/products/" + id)
                .then()
                .statusCode(200);
    }

    @Test
    public void testAddProduct() {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("Test Product");
        productDTO.setExcerpt("Test Product");
        productDTO.setDescription("This is a test product");
        productDTO.setType(ProductType.Room);
        productDTO.setMaxCapacity(2);
        productDTO.setNightlyRate(100.0);

        // Send POST request to add the product and capture the response
        Response response = given()
                .body(productDTO)
                .contentType(ContentType.JSON)
                .when()
                .post("/products")
                .then()
                .statusCode(201)
                .extract()
                .response();

        // Extract the ID of the newly created product from the response
        int id = response.jsonPath().getInt("id");

        // Send GET request to get the product using the extracted ID
        given()
                .when()
                .get("/products/" + id)
                .then()
                .statusCode(200)
                .body("name", equalTo("Test Product"))
                .body("excerpt", equalTo("Test Product"))
                .body("description", equalTo("This is a test product"))
                .body("type", equalTo("Room"))
                .body("maxCapacity", equalTo(2))
                .body("nightlyRate", equalTo(100.0f));

        // Send DELETE request to clean the new product
        given()
                .when()
                .delete("/products/" + id)
                .then()
                .statusCode(200);
    }

    @Test
    public void testUpdateProduct() {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("Test Product");
        productDTO.setExcerpt("Test Product");
        productDTO.setDescription("This is a test product");
        productDTO.setType(ProductType.Room);
        productDTO.setMaxCapacity(2);
        productDTO.setNightlyRate(100.0);

        // Send POST request to add the product and capture the response
        Response response = given()
                .body(productDTO)
                .contentType(ContentType.JSON)
                .when()
                .post("/products")
                .then()
                .statusCode(201)
                .extract()
                .response();

        // Extract the ID of the newly created product from the response
        int id = response.jsonPath().getInt("id");

        // Update the product
        productDTO.setId(id);
        productDTO.setName("Updated Test Product");
        productDTO.setExcerpt("Updated Test Product");
        productDTO.setDescription("This is an updated test product");
        productDTO.setMaxCapacity(3);
        productDTO.setNightlyRate(200.0);

        // Send PUT request to update the product
        given()
                .body(productDTO)
                .contentType(ContentType.JSON)
                .when()
                .put("/products/" + id)
                .then()
                .statusCode(200);

        // Send GET request to get the product using the extracted ID
        given()
                .when()
                .get("/products/" + id)
                .then()
                .statusCode(200)
                .body("name", equalTo("Updated Test Product"))
                .body("excerpt", equalTo("Updated Test Product"))
                .body("description", equalTo("This is an updated test product"))
                .body("maxCapacity", equalTo(3))
                .body("nightlyRate", equalTo(200.0f));

        // Send DELETE request to clean the new product
        given()
                .when()
                .delete("/products/" + id)
                .then()
                .statusCode(200);
    }

    @Test
    public void testDeleteProduct() {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("Test Product");
        productDTO.setExcerpt("Test Product");
        productDTO.setDescription("This is a test product");
        productDTO.setType(ProductType.Room);
        productDTO.setMaxCapacity(2);
        productDTO.setNightlyRate(100.0);

        // Send POST request to add the product and capture the response
        Response response = given()
                .body(productDTO)
                .contentType(ContentType.JSON)
                .when()
                .post("/products")
                .then()
                .statusCode(201)
                .extract()
                .response();

        // Extract the ID of the newly created product from the response
        int id = response.jsonPath().getInt("id");

        // Send DELETE request to clean the new product
        given()
                .when()
                .delete("/products/" + id)
                .then()
                .statusCode(200);

        // Send GET request to get the product using the extracted ID
        given()
                .when()
                .get("/products/" + id)
                .then()
                .statusCode(404);
    }


}