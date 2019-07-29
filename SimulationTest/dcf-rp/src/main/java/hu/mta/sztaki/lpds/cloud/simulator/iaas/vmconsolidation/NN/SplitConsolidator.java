package hu.mta.sztaki.lpds.cloud.simulator.iaas.vmconsolidation.NN;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.IaaSService;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.vmconsolidation.ModelBasedConsolidator;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.vmconsolidation.model.InfrastructureModel;

import java.io.PrintWriter;
import java.util.ArrayList;

public class SplitConsolidator extends ModelBasedConsolidator{

    //Files to write to
    ArrayList<PrintWriter> Files = new ArrayList<>();
    //Function calls counter
    Integer callsCounter=0;

    public SplitConsolidator(final IaaSService toConsolidate, final long consFreq){
        super(toConsolidate, consFreq);
        try {
            PrintWriter writer2 = new PrintWriter("VM2.txt", "UTF-8");
            PrintWriter writer3 = new PrintWriter("VM3.txt", "UTF-8");
            PrintWriter writer4 = new PrintWriter("VM4.txt", "UTF-8");
            PrintWriter writer5 = new PrintWriter("VM5.txt", "UTF-8");
            PrintWriter writer6 = new PrintWriter("VM6.txt", "UTF-8");
            PrintWriter writer7 = new PrintWriter("VM7.txt", "UTF-8");
            PrintWriter writer8 = new PrintWriter("VM8.txt", "UTF-8");
            PrintWriter writer9 = new PrintWriter("VM9.txt", "UTF-8");
            PrintWriter writer10 = new PrintWriter("VM10.txt", "UTF-8");
            PrintWriter writer11 = new PrintWriter("VM11.txt", "UTF-8");
            PrintWriter writer12 = new PrintWriter("VM12.txt", "UTF-8");
            PrintWriter writer13 = new PrintWriter("VM13.txt", "UTF-8");
            PrintWriter writer14 = new PrintWriter("VM14.txt", "UTF-8");
            PrintWriter writer15 = new PrintWriter("VM15.txt", "UTF-8");
            PrintWriter writer16 = new PrintWriter("VM16.txt", "UTF-8");

            Files.add(writer2);
            Files.add(writer3);
            Files.add(writer4);
            Files.add(writer5);
            Files.add(writer6);
            Files.add(writer7);
            Files.add(writer8);
            Files.add(writer9);
            Files.add(writer10);
            Files.add(writer11);
            Files.add(writer12);
            Files.add(writer13);
            Files.add(writer14);
            Files.add(writer15);
            Files.add(writer16);

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void processProps() {
    }

    @Override
    public InfrastructureModel optimize(final InfrastructureModel toConsolidate){
        System.err.print(".");
        System.err.flush();

        //Split and Merge object for splitting the IaaSService
        SplitMerge sm = new SplitMerge();
        //Splitting the IaaSService
        ArrayList<InfrastructureModel> splitIMs = sm.splitBefore(toConsolidate, 4);

        //Checking how many function calls
        callsCounter++;

        //Writing to files for data sets
        for(InfrastructureModel input: splitIMs) {
            StringBuilder data = new StringBuilder();
            for (int i = 0; i < input.items.length; i++) {
                data.append(
                        input.items[i].getHostID() + "," + input.items[i].hashCode() + ",");
                //Appending all VM's hosts
                for (int j = 0; j < input.items.length; j++) {
                    data.append(input.items[i].basedetails.vm.getResourceAllocation().allocated.getRequiredCPUs() + "," +
                            input.items[i].basedetails.vm.getResourceAllocation().allocated.getRequiredMemory() + "," +
                            input.items[i].getHostID());
                    if (j < input.items.length - 1) {
                        data.append(",");
                    } else {
                        data.append("\n");
                    }
                }
            }
            if ((input.items.length <= 16) && (input.items.length > 1)) {
                Files.get(input.items.length - 2).println(data.toString());
                Files.get(input.items.length - 2).flush();
            }
        }
        System.err.print("/");
        System.err.flush();
        return toConsolidate;
    }
}


