package hu.mta.sztaki.lpds.cloud.simulator.iaas.vmconsolidation.NN;

import hu.mta.sztaki.lpds.cloud.simulator.Timed;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.IaaSService;
import hu.mta.sztaki.lpds.cloud.simulator.io.VirtualAppliance;
import hu.mta.sztaki.lpds.cloud.simulator.util.CloudLoader;

import java.io.File;
import java.io.FileReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

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
        createThenPermutate(uniqueConfigs);
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

    private static void createThenPermutate(String[] uniqIMs) throws Exception{
        /**
         1) Create cloud infrastructure with 16 physical machines

         2) Create multidimensional-array structured according to the uniqIMs[]
                e.g. for 2 VMs:
                classification(targethost),vmidinquestion,vm0cpu,vm0mem,vm0host,vm1cpu,vm1mem,vm1host
            2.1) Populate with all the unique configurations from uniqIMs[]
         3)
         */
        File xml = new File("/home/mike/SimulationMLP/SimulationTest/dcf-rp/config-Automated.xml");
        IaaSService cloud = CloudLoader.loadNodes(xml.toString());
        Timed.simulateUntilLastEvent();
        VirtualAppliance va = new VirtualAppliance("BASE-VA", 1000, 0, false, 10000l);
        cloud.repositories.get(0).registerObject(va);

        int columns = uniqIMs[0].split(",").length;
        int rows =  uniqIMs.length;
        String[][] datasetMDSA = new String[columns][rows];

        for(int i=0;i<uniqIMs.length;i++){
            for(int j=0;j<columns;j++){
                String[] row = uniqIMs[i].split(",");
                datasetMDSA[j][i] = row[j];
            }
        }

        /**
         * Printout Multidimensional Array
            for(int i=0;i<rows;i++) {
                StringBuilder x = new StringBuilder();
                for(int j=0;j<columns;j++) {
                    x.append(datasetMDSA[j][i]);
                    if(j<columns-1)x.append(",");
                }
                System.out.println(x);
            }
         */

    }

}
