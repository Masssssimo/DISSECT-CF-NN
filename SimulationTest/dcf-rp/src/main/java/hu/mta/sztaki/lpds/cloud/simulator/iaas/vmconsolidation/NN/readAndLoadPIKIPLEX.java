package hu.mta.sztaki.lpds.cloud.simulator.iaas.vmconsolidation.NN;

import hu.mta.sztaki.lpds.cloud.simulator.Timed;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.IaaSService;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.PhysicalMachine;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.constraints.ConstantConstraints;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.vmconsolidation.model.InfrastructureModel;
import hu.mta.sztaki.lpds.cloud.simulator.io.VirtualAppliance;
import hu.mta.sztaki.lpds.cloud.simulator.util.CloudLoader;

import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.Scanner;

public class readAndLoadPIKIPLEX {
    public static File configPIKIPLEX;
    public static void main(String args[]) throws Exception{
        try {
            configPIKIPLEX = new File("/home/mike/Data - VM Consolidation/Datasets (Uncleaned)/VM's-1 to16 (4000PMS-100KJobs) PIK-IPLEX/uniq/Formatted Uniq/finalUniqFormattedVM10.txt");
        }catch(Exception e){
            System.err.println(e);
        }

        String[] configs = getData(configPIKIPLEX).split("\n");
        String[][] splitConfigs = new String[configs.length][configs[0].split(",").length];

        for(int i=0;i<configs.length;i++){
            String[] split = configs[i].split(",");
            for(int j=0;j<split.length;j++){
                splitConfigs[i][j] = split[j];
            }
        }
        InfrastructureModel[] loadedModels = loader(splitConfigs);
        DataSetGeneratorNN generate = new DataSetGeneratorNN();
        StringBuilder results = new StringBuilder();
        generate.permuteManual(loadedModels[0].items.length,loadedModels[0].bins.length);
        for(InfrastructureModel im:loadedModels){
            //Build the string representations of each permutations of the IM
            for (String s : generate.permutations) generate.buildModel(s, im);
            //Creates a copy for the comparison
            generate.listIMcopy.addAll(generate.listIM);
            //Recursively removes each "non-optimal" mapping
            generate.optimalPerm(generate.listIMcopy, im);
            //Stored the optimized configurations generated from the dataset
            results.append(generate.dataSetRow());
            //Clears the ArrayLists on each iteration
            generate.listIM.clear();
            generate.listIMcopy.clear();
            generate.optIM.clear();
        }
        PrintWriter writer = new PrintWriter("PIK-IPLEX-VM10-sortThenUniq.txt", "UTF-8");
        writer.println(results);
        writer.flush();
    }
    private static String getData(File datasetRaw) throws Exception{
        StringBuilder uncleanData = new StringBuilder();
        Scanner sc = new Scanner(new FileReader(datasetRaw));
        while (sc.hasNext()) {
            String line = sc.nextLine();
            if (!line.isEmpty()) {
                uncleanData.append(line);
                uncleanData.append("\n");
            }
        }
        return uncleanData.toString();
    }
    private static InfrastructureModel[] loader(String[][] configurations)throws Exception{
        InfrastructureModel[] toPermute = new InfrastructureModel[configurations.length];
        for(int i=0;i<configurations.length;i++) {
            File xml = new File("/home/mike/SimulationMLP/SimulationTest/dcf-rp/config.xml");
            IaaSService cloud = CloudLoader.loadNodes(xml.toString());
            Timed.simulateUntilLastEvent();
            VirtualAppliance va = new VirtualAppliance("BASE-VA", 1000, 0, false, 10000l);
            cloud.repositories.get(0).registerObject(va);
            Timed.simulateUntilLastEvent();

            for (int j=0;j<configurations[i].length;j+=3) {
                ConstantConstraints caps = new ConstantConstraints(Double.parseDouble(configurations[i][j]), 0.001, true, Long.parseLong(configurations[i][j+1]));
                cloud.requestVM(va,caps, cloud.repositories.get(0), 1);
                Timed.simulateUntilLastEvent();
            }
            InfrastructureModel IM = new InfrastructureModel(cloud.machines.toArray(new PhysicalMachine[0]), 1, false, 1);
            toPermute[i] = IM;
        }
        return toPermute;
    }
}
