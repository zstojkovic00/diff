package diff;

public record Operation(Type type, String text) {

    public enum Type { EQUAL, INSERT, DELETE }

    @Override
    public String toString() {
        return switch (type) {
            case EQUAL  -> "  " + text;
            case INSERT -> "+ " + text;
            case DELETE -> "- " + text;
        };
    }
}
