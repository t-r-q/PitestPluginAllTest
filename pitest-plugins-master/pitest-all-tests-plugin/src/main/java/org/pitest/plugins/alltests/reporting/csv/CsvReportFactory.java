
package org.pitest.plugins.alltests.reporting.csv;

import org.pitest.mutationtest.ListenerArguments;
import org.pitest.mutationtest.MutationResultListener;
import org.pitest.mutationtest.MutationResultListenerFactory;
import org.pitest.mutationtest.report.csv.CSVReportListener;

import java.util.Properties;

public class CsvReportFactory implements MutationResultListenerFactory {

  public MutationResultListener getListener(Properties props,
      final ListenerArguments args) {
    return new CSVReportListener(args.getOutputStrategy());
  }

  public String name() {
    return "CSV";
  }

  public String description() {
    return "csv report ";
  }

}
