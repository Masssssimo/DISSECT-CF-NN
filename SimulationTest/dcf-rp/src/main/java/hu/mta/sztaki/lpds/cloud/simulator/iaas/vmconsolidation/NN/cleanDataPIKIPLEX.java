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
import java.util.*;

import static java.lang.Float.parseFloat;
import static java.lang.Long.parseLong;

public class cleanDataPIKIPLEX {

    public static File datasetRaw;
    public static String uncleanedDatasetString;

    public static void main(String args[]) throws Exception {
        try {
            datasetRaw = new File("/home/mike/Data - VM Consolidation/Datasets (Uncleaned)/VM's-1 to16 (4000PMS-100KJobs) PIK-IPLEX/VM2.txt");
        }catch(Exception e){
            System.err.println(e);
        }
        uncleanedDatasetString = getData(datasetRaw);
        String[] uniqueConfigs = cleanDataset(uncleanedDatasetString);
        createThenPermute(uniqueConfigs);
    }

    /**
     * @param datasetRaw File
     * @return dataset as a String - without linebreak's in-between rows
     * @throws Exception if file cannot be located
     */
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

    private static String[] cleanDataset(String datasetString){
        /**
         1) Create:
         - a String[] tokens from of each line within the datasetString
         - a String builder for storing unique rows
         - a HashSet to contain elements which have already been checked for quicker look-ups

         2) Loop through tokens String[] array

         2.1) Check if the element is the first within the array to append "\n"

         2.2) Check if the element exists within the alreadyPresent HashSet
         - if not then add the element to the uniqueRowsDatatset

         2.3) Add element to alreadyPresent HashSet

         3) Return String of unique configurations as String[] (split via "\n")
         */
        String[] tokens = datasetString.split("\n");
        StringBuilder uniqueRowsDataset = new StringBuilder();
        Set<String> alreadyPresent = new HashSet<String>();
        boolean first = true;
        for(String token : tokens) {

            if(!alreadyPresent.contains(token)) {
                if(first) first = false;
                else uniqueRowsDataset.append("\n");

                if(!alreadyPresent.contains(token))
                    uniqueRowsDataset.append(token);
            }

            alreadyPresent.add(token);
        }
        return uniqueRowsDataset.toString().split("\n");
    }

    private static void createThenPermute(String[] uniqIMs) throws Exception{
        /**
         1) Create cloud infrastructure with 16 physical machines

         2) Create multidimensional array structured according to the uniqIMs[]
         e.g. for 2 VMs dataset:
         classification(targethost),vmidinquestion,vm0cpu,vm0mem,vm0host,vm1cpu,vm1mem,vm1host

         3) Populate multidimensional array with unique configurations from uniqIMs[]

         4) Create an InfrastuctureModel for each unique configuration

         4.1) Permute the infrastructures models mapping of virtual machines to physical machines

         4.2) Build each string representation of the permuted of the InfrastructureModels

         4.3) Compare each model then optimize the results

         4.4) Collect the optimal models results and append each via a String Builder

         5) Re-clean the optimal models using the previously defined functions
         Therefore removing white-spaces and dupliate rows.
         */


        int columns = uniqIMs[0].split(",").length;
        int rows =  uniqIMs.length;
        String[][] datasetMDSA = new String[columns][rows];

        for(int i=0;i<uniqIMs.length;i++){
            for(int j=0;j<columns;j++){
                String[] row = uniqIMs[i].split(",");
                datasetMDSA[j][i] = row[j];
            }
        }

        //Create an object of DataSetGeneratorNN
        DataSetGeneratorNN generate = new DataSetGeneratorNN();
        //Calculates each permutations of the given configurations recursively
        generate.permuteHelper(2, 4, "");
        //File which contains the infrastructure set (i.e. nodes mapping to one another)
        File xml = new File("/home/mike/SimulationMLP/SimulationTest/dcf-rp/config-Automated.xml");
        //This string builder stores the "cleaned" dataset
        StringBuilder results = new StringBuilder();

        for(int j=0;j<rows;j++) {

            //Builds the cloud infrastructure  with the XML set-up
            IaaSService cloud = CloudLoader.loadNodes(xml.toString());
            Timed.simulateUntilLastEvent();
            //Assign the virtual appliance to the cloud
            VirtualAppliance va = new VirtualAppliance("BASE-VA", 1000, 0, false, 10000l);
            cloud.repositories.get(0).registerObject(va);

            for (int i = 2; i < columns; ) {
                //Looping through the unique configurations from the "cleaned" dataset
                //Using the configurations to construct/request a virtual machine instance
                ConstantConstraints VMCaps = new ConstantConstraints(parseFloat(datasetMDSA[i][j]), 0.001, true, parseLong(datasetMDSA[i + 1][j]));
                cloud.requestVM(va, VMCaps, cloud.repositories.get(0), 1);
                Timed.simulateUntilLastEvent();
                //This conditional check ensures the loop jumps 3 iterations
                //Due to the formatting of the dataset this is required
                if (!(i + 3 < columns)) {
                    break;
                } else {
                    i += 3;
                }
            }

            //Generate an Infrastructure Model based off the cloud previously constructed
            InfrastructureModel IM = new InfrastructureModel(cloud.machines.toArray(new PhysicalMachine[0]), 1, false, 1);

            //Build the string representations of each permutations of the IM
            for (String s : generate.permutations) generate.buildModel(s, IM);

            //Creates a copy for the comparison
            generate.listIMcopy.addAll(generate.listIM);

            //Recursively removes each "non-optimal" mapping
            generate.optimalPerm(generate.listIMcopy, IM);

            //Stored the optimized configurations generated from the dataset
            results.append(generate.dataSetRow());

            //Clears the ArrayLists on each iteration
            generate.listIM.clear();
            generate.listIMcopy.clear();
            generate.optIM.clear();
        }
        //The cleaned results is the generated "optimal" configurations -  re-cleaned
        // (i.e. removed blank spaces and duplicate rows)
        String cleanedResults = String.join("\n", cleanDataset(results.toString()));
        System.out.println(cleanedResults);
    }
}
