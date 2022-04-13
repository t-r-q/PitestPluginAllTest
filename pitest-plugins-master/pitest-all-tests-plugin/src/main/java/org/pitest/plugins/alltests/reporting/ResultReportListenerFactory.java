package org.pitest.plugins.alltests.reporting;


import org.pitest.mutationtest.ListenerArguments;
import org.pitest.mutationtest.MutationResultListener;
import org.pitest.plugin.ToolClasspathPlugin;


import java.util.Properties;


public interface ResultReportListenerFactory extends ToolClasspathPlugin {

        MutationResultListener getListener(Properties props, ListenerArguments args);

        String name();


}
