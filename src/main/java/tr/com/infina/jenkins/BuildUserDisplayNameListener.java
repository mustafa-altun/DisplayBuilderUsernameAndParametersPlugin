package tr.com.infina.jenkins;

import hudson.Extension;
import hudson.model.Cause;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.model.listeners.RunListener;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import hudson.model.ParameterValue;
import hudson.model.ParametersAction;
import java.util.List;
import java.util.ArrayList;

/**
 * Intercepts builds when they start and automatically appends 
 * the triggering user's name and build parameters to the build display name.
 */
@Extension
public class BuildUserDisplayNameListener extends RunListener<Run<?, ?>> {

    private static final Logger LOGGER = Logger.getLogger(BuildUserDisplayNameListener.class.getName());

    @Override
    public void onStarted(Run<?, ?> r, TaskListener listener) {
        // Fetch the user cause to find out who triggered the build
        Cause.UserIdCause userIdCause = r.getCause(Cause.UserIdCause.class);
        String triggerUser;

        if (userIdCause != null) {
            triggerUser = userIdCause.getUserId();
            // Fallback to username if userId is not present
            if (triggerUser == null || triggerUser.trim().isEmpty()) {
                triggerUser = userIdCause.getUserName();
            }
        } else {
            triggerUser = "Auto";
        }
        
        // Handle case where it's still null somehow
        if (triggerUser == null || triggerUser.trim().isEmpty()) {
            triggerUser = "Auto";
        }

        // Format is: #5616 [mustafa.altun]
        StringBuilder newDisplayName = new StringBuilder("#" + r.getNumber() + " [" + triggerUser + "]");

        // Fetch build parameters
        ParametersAction paramsAction = r.getAction(ParametersAction.class);
        if (paramsAction != null) {
            List<ParameterValue> parameters = paramsAction.getParameters();
            if (parameters != null && !parameters.isEmpty()) {
                List<String> paramStrings = new ArrayList<>();
                for (ParameterValue pv : parameters) {
                    // Sadece hassas olmayan parametreleri yazdirmak guvenlidir (PasswordParameterValue haric tutulabilir, ama toString genellikle maskeler)
                    if (!pv.isSensitive()) {
                        paramStrings.add(pv.getName() + ":" + pv.getValue());
                    } else {
                        paramStrings.add(pv.getName() + ":***");
                    }
                }
                
                if (!paramStrings.isEmpty()) {
                    // Add parameters with a newline or space. Note: Jenkins UI might strip newlines, so spaces are safer.
                    newDisplayName.append(" [Params: ");
                    newDisplayName.append(String.join(" - ", paramStrings));
                    newDisplayName.append("]");
                }
            }
        }

        try {
            r.setDisplayName(newDisplayName.toString());
            LOGGER.log(Level.INFO, "Updated build display name to {0}", newDisplayName.toString());
        } catch (IOException e) {
            listener.getLogger().println("[BuildUserDisplayPlugin] Error setting build display name: " + e.getMessage());
            LOGGER.log(Level.SEVERE, "Failed to set display name for build " + r.getNumber(), e);
        }
    }
}
