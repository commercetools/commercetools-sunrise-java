package setupwidget.controllers;

import com.google.inject.ImplementedBy;
import play.mvc.Call;

@ImplementedBy(ReflectionSetupReverseRouter.class)
public interface SetupReverseRouter {

    Call processSetupFormCall();
}
