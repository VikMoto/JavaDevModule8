package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebServlet(value = "/time")
public class TimeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();

        int timezoneOffset = getTimezoneOffset(request);


        // We get the current time in LocalDateTime format with UTC time zone
        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC).plusHours(timezoneOffset);

        // We form a line with time and time zone
        String time = now
                .format((DateTimeFormatter.ofPattern("yyyy-MM-dd  HH:mm:ss"))) + " UTC";


        out.println("<html>");
        out.println("<head><title>Поточний час</title></head>");
        out.println("<body>");
        out.println("<h1>Поточний час:</h1>");
        out.println("<h1>" + time + "</h1>");
        out.println("</body></html>");

        out.close();
    }

    private int getTimezoneOffset(HttpServletRequest request) {
        String timeZone = getAllParametersUrlEncoded(request);
        String timezoneString = "timezone = [2]";

        int timezoneOffset = 0;
        Pattern pattern = Pattern.compile("[-+]?\\d+");
        Matcher matcher = pattern.matcher(timeZone);
        if (matcher.find()) {
            timezoneOffset = Integer.parseInt(matcher.group());
        } else {
            // handle the case where no numeric value was found
        }
        System.out.println("timezoneOffset = " + timezoneOffset);
        return timezoneOffset;
    }

    private String getAllParametersUrlEncoded(HttpServletRequest request){
        //
        StringJoiner result = new StringJoiner("<br>");

        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()){
            String parameterName = parameterNames.nextElement();

            String parameterValues = Arrays.toString(request.getParameterValues(parameterName));
            result.add(parameterName + " = " + parameterValues);

        }
        return result.toString();
    }
}
