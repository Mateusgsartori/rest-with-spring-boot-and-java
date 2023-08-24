package spring.project.restwithspringbootandjava;

public class Greetintgs {
    private final long id;
    private final String content;


    public Greetintgs(long id, String content) {
        this.id = id;
        this.content = content;
    }

    public long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }
}

