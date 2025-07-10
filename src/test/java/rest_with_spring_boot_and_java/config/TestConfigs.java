package rest_with_spring_boot_and_java.config;

public interface TestConfigs {
    int SERVER_PORT = 8888;
    String HEADER_PARAM_AUTHORIZATION = "Authorization";
    String HEADER_PARAM_ORIGIN = "Origin";

    String ORIGIN_SUCCESS_TEST = "http://localhost:8888";
    String ORIGIN_FAIL_TEST = "https://www.google.com";
}