import org.restlet.Application;
import org.restlet.Context;
import org.restlet.Restlet;
import org.restlet.routing.Router;

/**
 * Created by eryshev-alexey on 08/07/15.
 */

public class PirateDictionaryApp extends Application {
    public PirateDictionaryApp() {}

    public PirateDictionaryApp(Context context) {
        super(context);
    }

    public Restlet createInboundRoot() {
        Router router = new Router(this.getContext());
        router.attach("/terms/{termId}", TermResource.class);
        return router;
    }
}
