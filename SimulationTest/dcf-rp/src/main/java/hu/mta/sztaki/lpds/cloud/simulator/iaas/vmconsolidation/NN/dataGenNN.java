package hu.mta.sztaki.lpds.cloud.simulator.iaas.vmconsolidation.NN;

import hu.mta.sztaki.lpds.cloud.simulator.Timed;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.IaaSService;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.PhysicalMachine;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.VirtualMachine;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.constraints.ConstantConstraints;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.vmconsolidation.model.GenHelper;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.vmconsolidation.model.InfrastructureModel;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.vmconsolidation.model.improver.NonImprover;
import hu.mta.sztaki.lpds.cloud.simulator.io.VirtualAppliance;
import hu.mta.sztaki.lpds.cloud.simulator.util.CloudLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class dataGenNN {

    private static InfrastructureModel IM;
    //  List of all permutations
    private static ArrayList<String> permutations = new ArrayList<>();
    //  List of all IM's
    private static ArrayList<InfrastructureModel> listIM = new ArrayList<>();
    //  List of all IM's
    private static ArrayList<InfrastructureModel> listIMcopy = new ArrayList<>();
    //  List of optimal permutations
    private static ArrayList<InfrastructureModel> optIM = new ArrayList<>();

    //Amount of VM's & PM's
    private static int amountVM = 32;
    private static int amountPM = 4;

    public static void main(String args[]) throws Exception{

        /**
         *  Cloud with only 4 Physical Machine
        */
        //File xml = new File("/home/mike/DISSECT-CF-NN/dcf-rp/config.xml");

        File xml = new File("/home/mike/SimulationMLP/SimulationTest/dcf-rp/config-16.xml");
        IaaSService cloud = CloudLoader.loadNodes(xml.toString());
        Timed.simulateUntilLastEvent();
        VirtualAppliance va = new VirtualAppliance("BASE-VA", 1000, 0, false, 10000l);
        cloud.repositories.get(0).registerObject(va);
        ConstantConstraints minCaps = new ConstantConstraints(2, 0.001, true, 99999999L);
        cloud.requestVM(va, minCaps, cloud.repositories.get(0), amountVM);
        Timed.simulateUntilLastEvent();


        IM = new InfrastructureModel(cloud.machines.toArray(new PhysicalMachine[0]), 1, false, 1);
        IM.splitBefore(cloud);

        /**
         *  Executing Neural Network Consolidation with 4 PM's
         */
        /*
        IM = new InfrastructureModel(cloud.machines.toArray(new PhysicalMachine[0]), 1, false, 1);

        // Before consolidation
        for(int i=0;i<IM.items.length;i++){
            System.out.println("VM-"+IM.items[i].hashCode()+"\t->\t HOST-"+IM.items[i].getHostID());
        }

        NerualNetworkConsolidator consolidate = new NerualNetworkConsolidator(cloud,0);
        consolidate.optimize(IM);

        // After consolidation
        for(int i=0;i<IM.items.length;i++){
            System.out.println("VM-"+IM.items[i].hashCode()+"\t->\t HOST-"+IM.items[i].getHostID());
        }
        */


        /**
         *  Execute Generating Datasets
         */

        /*
        permuteManual(amountVM,amountPM);

        for(int i=0;i<199111;i++){
            generate();
            listIM.clear();
            listIMcopy.clear();
            optIM.clear();
        }
        */


    }

    private static void generate() throws Exception {
        File xml = new File("/home/mike/DISSECT-CF-NN/dcf-rp/config.xml");

        //Create IaaS based off .XML
        IaaSService cloud = CloudLoader.loadNodes(xml.toString());

        //Wait for cloud to set up
        Timed.simulateUntilLastEvent();

        //Set up VA
        VirtualAppliance va = new VirtualAppliance("BASE-VA", 1000, 0, false, 10000l);

        //Register VA on cloud repo
        cloud.repositories.get(0).registerObject(va);

        //Generate random constraints values
        //Random CPU
        double minCPU = 0.00000001;
        double maxCPU = 4.0;
        double randomCPU = ThreadLocalRandom.current().nextDouble(minCPU, maxCPU);

        //Random Processing
        double minPRO = 0.00000001;
        double maxPRO = 0.001;
        double randomPRO = ThreadLocalRandom.current().nextDouble(minPRO, maxPRO);

        //Random Memory
        long minMEM = 1;
        long maxMEM = 999999999;
        long randomMEM = ThreadLocalRandom.current().nextLong(minMEM, maxMEM);

        //Create Random constraints
        ConstantConstraints minCaps = new ConstantConstraints(randomCPU, randomPRO, true, randomMEM);
        //ConstantConstraints minCaps = new ConstantConstraints(2.5, 0.001, true, 64000000000L);


        //Request Virtual Machines
        //Request each VM individually with the above generated four constraints
        cloud.requestVM(va, minCaps, cloud.repositories.get(0), amountVM);
        Timed.simulateUntilLastEvent();

        //Instantiating original config of vm's to pm's
        IM = new InfrastructureModel(cloud.machines.toArray(new PhysicalMachine[0]), 1, false, 1);

        //Build IM's
        for(String s:permutations)buildModel(s);

        //Calculate optimal permutations
        listIMcopy.addAll(listIM);
        optimalPerm(listIMcopy, IM);

        //Printing the row
        dataSetRow();
    }

    private static void permuteManual(int amountVM,int amountPM){
        //Exhaustively search all permutations
        permuteHelper(amountVM,amountPM+1,"");
    }


    private static void permuteIM(InfrastructureModel IM) {
        //Recursively create all permutations
        permuteHelper(IM.items.length, IM.bins.length + 1, "");
    }

    private static void permuteHelper(int amountVM, int amountPM, String output) {
        if (amountVM == 0) {
            permutations.add(output);
        } else {
            //Loop through each PM and recursively calculate permutations
            for (int i = 1; i < amountPM; i++) {
                permuteHelper(amountVM - 1, amountPM, output + i);
            }
        }
    }

    private static void buildModel(String perm){
        InfrastructureModel x = new InfrastructureModel(IM, new GenHelper() {

            @Override
            public boolean shouldUseDifferent() {
                return true;
            }

            @Override
            public int whatShouldWeUse(InfrastructureModel im, int vm) {
                int pm = Integer.parseInt("" + perm.charAt(vm)) - 1;
                return pm;
            }
        }, NonImprover.singleton);
        listIM.add(x);
    }

    private static void optimalPerm(ArrayList<InfrastructureModel> list, InfrastructureModel currentBest) {
        //Calculate the optimal permutations
        list.removeIf(x -> currentBest.isBetterThan(x));
        InfrastructureModel check = null;
        for (InfrastructureModel x : list) {
            if (x.isBetterThan(currentBest)) {
                check = x;
                break;
            }
        }
        if (check != null) optimalPerm(list, check);
        optIM.addAll(list);
    }


    private static void dataSetRow() {
        //Select Random Mapping
        int minRandMap = 0;
        int randMap;
        if (optIM.size() > 1)
            randMap = ThreadLocalRandom.current().nextInt(minRandMap, optIM.size() - 1);
        else
            randMap = 0;

        if(!optIM.isEmpty()) {
            StringBuilder data = new StringBuilder();
            for (int i = 0; i < optIM.get(randMap).items.length; i++) {
                //Format of data
                //Classification
                //Which VM to classify?

                //VM-0-CPU	VM-0-MEMORY	VM-0-PROCESS VM-0-HOST...
                //VM-n-CPU	VM-n-MEMORY	VM-n-PROCESS VM-n-HOST

                //PM-0-HOSTING-AMOUNT...
                //PM-n-HOSTING-AMOUNT

                data.append(
                        optIM.get(randMap).items[i].getHostID() + "," +
                                optIM.get(randMap).items[i].hashCode() + "," +
                                optIM.get(randMap).items[i].basedetails.vm.getResourceAllocation().allocated.getRequiredCPUs() + "," +
                                optIM.get(randMap).items[i].basedetails.vm.getResourceAllocation().allocated.getRequiredMemory() + "," +
                                optIM.get(randMap).items[i].basedetails.vm.getResourceAllocation().allocated.getRequiredProcessingPower() + ",");
                //Appending all VM's hosts
                for (int j = 0; j < optIM.get(randMap).items.length; j++) {
                    data.append(optIM.get(randMap).items[j].getHostID() + ",");
                }
                //Appending all PM's hosting amount
                for (int k = 0; k < optIM.get(randMap).bins.length; k++) {
                    if (k == optIM.get(randMap).bins.length - 1) {
                        data.append(optIM.get(randMap).bins[k].getVMs().size());
                        break;
                    }
                    data.append(optIM.get(randMap).bins[k].getVMs().size() + ",");
                }
                if (i < optIM.get(randMap).items.length) data.append('\n');

            }
            System.out.print(data.toString());
        }
    }

}
