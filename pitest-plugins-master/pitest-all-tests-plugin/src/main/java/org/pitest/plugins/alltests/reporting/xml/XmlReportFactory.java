package org.pitest.plugins.alltests.reporting.xml;

import org.pitest.mutationtest.ListenerArguments;
import org.pitest.mutationtest.MutationResultListener;
import org.pitest.mutationtest.MutationResultListenerFactory;
import org.pitest.mutationtest.report.xml.XMLReportListener;

import java.util.Properties;

public class XmlReportFactory implements MutationResultListenerFactory{
    public MutationResultListener getListener(Properties props,
                                              final ListenerArguments args) {
        return new XMLReportListener(args.getOutputStrategy());
    }

    public String name() {
        return "XML";
    }

    public String description() {
        return "xml report";
    }

}
