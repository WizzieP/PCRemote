import me.wizziee.pilot.server.CommandData;
import org.junit.Test;

import java.util.HashMap;

import static com.jayway.restassured.RestAssured.*;
import static com.jayway.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

public class ResourceTest {

    /*
    @Test
    public void testExecuteResponseShouldBeOk(){
        HashMap<String, Object> params = new HashMap<>();
        params.put("timeout", 30);
        CommandData data = new CommandData("Shutdown", params);

        given().
                contentType("application/json").
                body(data).
        when().
                put("/commands/execute").then().
                assertThat().statusCode(200);
    }
    */
}
