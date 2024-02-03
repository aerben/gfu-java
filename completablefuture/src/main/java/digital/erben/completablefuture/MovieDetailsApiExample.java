package digital.erben.completablefuture;

import static com.github.skjolber.jackson.jsh.AnsiSyntaxHighlight.*;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.skjolber.jackson.jsh.DefaultSyntaxHighlighter;
import com.github.skjolber.jackson.jsh.SyntaxHighlightingJsonGenerator;
import java.io.IOException;
import java.util.List;

public class MovieDetailsApiExample {

    public static void main(String[] args) throws IOException {
        MovieDetailsApi
            .instance()
            .loadDetailsForMovie("Spider")
            .thenAccept(MovieDetailsApiExample::writeObjectSafe)
            .join();
    }

    private static void writeObjectSafe(List<MovieResult> movies) {
        try {
            try (JsonGenerator generator = buildGenerator()) {
                generator.writeObject(movies);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static JsonGenerator buildGenerator() throws IOException {
        DefaultSyntaxHighlighter highlighter = DefaultSyntaxHighlighter
            .newBuilder()
            .withField(GREEN)
            .withNumber(BLUE)
            .withString(GREEN)
            .withNull(BLUE, HIGH_INTENSITY)
            .build();
        return new SyntaxHighlightingJsonGenerator(
            new ObjectMapper()
                .writerWithDefaultPrettyPrinter()
                .createGenerator(System.out),
            highlighter,
            true
        );
    }
}
