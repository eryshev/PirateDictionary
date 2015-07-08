import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

/**
 * Created by eryshev-alexey on 08/07/15.
 */
public class TermResource extends ServerResource {
    String termName;

    @Override
    public void doInit() {
        this.termName = getAttribute("termId");
    }

    @Get("txt")
    public String toString() {
        return "Term is \"" + this.termName + "\"";
    }

}
