package at.aberger.smallrye.config;

public record HelloWorldModel(String greeting) {
    public HelloWorldModel() {
        this("");
    }
}
