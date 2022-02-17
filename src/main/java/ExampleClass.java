import model.User;
import org.springframework.http.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ExampleClass {

    private static final String CREATE_User_ENDPOINT_URL = "http://91.241.64.178:7081/api/users";
    private static final String UPDATE_User_ENDPOINT_URL = "http://91.241.64.178:7081/api/users";
    private static final String DELETE_User_ENDPOINT_URL = "http://91.241.64.178:7081/api/users";

    private static RestTemplate restTemplate = new RestTemplate();

    public static void main(String[] args) {
        String cookies = getUsers();
        createUser(cookies);
        updateUser(cookies);
       deleteUser(cookies);
    }

    private static String getUsers() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(new MediaType[]{MediaType.APPLICATION_JSON}));
        RestTemplate template = new RestTemplate();
        ResponseEntity<String> forEntity = template.getForEntity("http://91.241.64.178:7081/api/users", String.class);
        forEntity.getHeaders().get("Set-Cookie").stream().forEach(System.out::println);
        return forEntity.getHeaders().get("Set-Cookie").stream().collect(Collectors.joining(";"));
    }

    private static void createUser(String cookies) {
        User user = new User(3L, "James", "Brown", (byte) 25);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", cookies);
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<User> requestBody = new HttpEntity<>(user, headers);
        ResponseEntity<String> response = restTemplate
                .exchange(CREATE_User_ENDPOINT_URL, HttpMethod.POST, requestBody, String.class);
        System.out.println(response.getBody());
    }

    private static void updateUser(String cookies) {
        Long userId = 3l;
        User updateUser = new User(userId, "Thomas", "Shelby", (byte) 25);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", cookies);
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<User> requestBody = new HttpEntity<>(updateUser, headers);
        ResponseEntity<String> response =
                restTemplate.exchange(UPDATE_User_ENDPOINT_URL, HttpMethod.PUT, requestBody, String.class);
        System.out.println(response.getBody());
    }

    private static void deleteUser(String cookies) {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", cookies);

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> requestBody = new HttpEntity<>(null, headers);
        String resourceUrl = DELETE_User_ENDPOINT_URL + "/" + 3;
        ResponseEntity<String> response =
                restTemplate.exchange(resourceUrl, HttpMethod.DELETE, requestBody, String.class);
        System.out.println(response.getBody());
    }

}
