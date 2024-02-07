package digital.erben.tomcat;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

import java.io.IOException;
import java.time.LocalDateTime;

public class TomcatService {

    @PostConstruct
    public void launch() throws LifecycleException {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8080);
        tomcat.getConnector().setProperty("address", "0.0.0.0");

        Context context = tomcat.addContext("", null);

        String dateServletName = "dateServlet";
        Tomcat.addServlet(context, dateServletName, CurrentDateServlet.class.getName());

        context.addServletMappingDecoded("/", dateServletName);

        tomcat.start();
    }

    public static class CurrentDateServlet extends HttpServlet {

        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            resp.getWriter().write("Current date: " + LocalDateTime.now());
        }
    }
}
