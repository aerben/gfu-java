package digital.erben.completablefuture.demo;

import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.asciitable.CWC_LongestLine;
import digital.erben.completablefuture.MovieResult;
import digital.erben.completablefuture.ReleaseDate;
import java.util.List;

public class AsciiTableRenderer {

    static void render(List<MovieResult> lst) {
        AsciiTable table = new AsciiTable();
        table.getRenderer().setCWC(new CWC_LongestLine());
        lst.forEach(movie -> {
            ReleaseDate releaseDate = movie.releaseDate();
            String releaseYear = releaseDate != null
                ? String.valueOf(releaseDate.year())
                : "UNKNOWN";
            table.addRow(movie.titleText().text(), releaseYear);
        });
        System.out.println(table.render());
    }
}
