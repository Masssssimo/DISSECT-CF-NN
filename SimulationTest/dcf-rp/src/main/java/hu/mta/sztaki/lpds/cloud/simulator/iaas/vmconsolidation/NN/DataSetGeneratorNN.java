package hu.mta.sztaki.lpds.cloud.simulator.iaas.vmconsolidation.NN;

import hu.mta.sztaki.lpds.cloud.simulator.Timed;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.IaaSService;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.PhysicalMachine;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.constraints.ConstantConstraints;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.vmconsolidation.model.GenHelper;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.vmconsolidation.model.InfrastructureModel;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.vmconsolidation.model.improver.NonImprover;
import hu.mta.sztaki.lpds.cloud.simulator.io.VirtualAppliance;
import hu.mta.sztaki.lpds.cloud.simulator.util.CloudLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class DataSetGeneratorNN {

    private static InfrastructureModel IM;
    //  List of all permutations
    public ArrayList<String> permutations = new ArrayList<>();
    //  List of all IM's
    public ArrayList<InfrastructureModel> listIM = new ArrayList<>();
    //  List of all IM's
    public ArrayList<InfrastructureModel> listIMcopy = new ArrayList<>();
    //  List of optimal permutations
    public ArrayList<InfrastructureModel> optIM = new ArrayList<>();

    //Amount of VM's & PM's
    private static int amountVM = 7;
    private static int amountPM = 4;

    public static void main(String args[]) throws Exception{

        /**
         *  Cloud with 4 Physical Machine
        */
        //File xml = new File("/home/mike/DISSECT-CF-NN/dcf-rp/config.xml");

        /**
         *  Cloud with 16 Physical Machine
         */
        File xml = new File("/home/mike/SimulationMLP/SimulationTest/dcf-rp/config-Automated.xml");
        IaaSService cloud = CloudLoader.loadNodes(xml.toString());
        Timed.simulateUntilLastEvent();
        VirtualAppliance va = new VirtualAppliance("BASE-VA", 1000, 0, false, 10000l);
        cloud.repositories.get(0).registerObject(va);

        //Generate random constraints values
        //Random CPU Range
        double minCPU = 0.00000001;
        double maxCPU = 4.0;
        double randomCPU = ThreadLocalRandom.current().nextDouble(minCPU, maxCPU);
        double randomCPU1 = ThreadLocalRandom.current().nextDouble(minCPU, maxCPU);
        double randomCPU2 = ThreadLocalRandom.current().nextDouble(minCPU, maxCPU);
        double randomCPU3 = ThreadLocalRandom.current().nextDouble(minCPU, maxCPU);
        double randomCPU4 = ThreadLocalRandom.current().nextDouble(minCPU, maxCPU);


        //Random Memory Range
        long minMEM = 1;
        long maxMEM = 999999999;
        long randomMEM = ThreadLocalRandom.current().nextLong(minMEM, maxMEM);
        long randomMEM1 = ThreadLocalRandom.current().nextLong(minMEM, maxMEM);
        long randomMEM2 = ThreadLocalRandom.current().nextLong(minMEM, maxMEM);
        long randomMEM3 = ThreadLocalRandom.current().nextLong(minMEM, maxMEM);
        long randomMEM4 = ThreadLocalRandom.current().nextLong(minMEM, maxMEM);

        //Create Random constraints
        ConstantConstraints minCaps = new ConstantConstraints(randomCPU, 0.001, true, randomMEM);
        ConstantConstraints minCaps1 = new ConstantConstraints(randomCPU1, 0.001, true, randomMEM1);
        ConstantConstraints minCaps2 = new ConstantConstraints(randomCPU2, 0.001, true, randomMEM2);
        ConstantConstraints minCaps3 = new ConstantConstraints(randomCPU3, 0.001, true, randomMEM3);
        ConstantConstraints minCaps4 = new ConstantConstraints(randomCPU4, 0.001, true, randomMEM4);

        //Random amount of virutl machines
        Integer VMAmount = ThreadLocalRandom.current().nextInt(1, 10);
        Integer VMAmount1 = ThreadLocalRandom.current().nextInt(1, 10);
        Integer VMAmount2 = ThreadLocalRandom.current().nextInt(1, 10);
        Integer VMAmount3 = ThreadLocalRandom.current().nextInt(1, 10);
        Integer VMAmount4 = ThreadLocalRandom.current().nextInt(1, 10);



        //ConstantConstraints minCaps = new ConstantConstraints(2.5,0.001, true, 9999999999L);

        cloud.requestVM(va, minCaps, cloud.repositories.get(0), 4);
        /*
        cloud.requestVM(va, minCaps1, cloud.repositories.get(0), VMAmount1);
        cloud.requestVM(va, minCaps2, cloud.repositories.get(0), VMAmount2);
        cloud.requestVM(va, minCaps3, cloud.repositories.get(0), VMAmount3);
        cloud.requestVM(va, minCaps4, cloud.repositories.get(0), VMAmount4);
         */
        Timed.simulateUntilLastEvent();

        IM = new InfrastructureModel(cloud.machines.toArray(new PhysicalMachine[0]), 1, false, 1);

        //Execute optimization
        NeuralNetworkConsolidator consolidate = new NeuralNetworkConsolidator(cloud,0);
        IM = consolidate.consolidateSplit(IM);

        /*
        for(ModelPM PM : IM.bins){
            System.out.println(PM.getPM().getState());
        }


        for(int i=0;i<consolidate.ratio.size();i++){
            if(consolidate.ratio.get(i)!=0){
                System.out.println("VMs Hosted = "+i+"\t Count = "+consolidate.ratio.get(i));
            }
        }

        for(PhysicalMachine PM : cloud.machines){
            System.out.println(PM.isRunning());
            System.out.println(PM.publicVms.size());
        }
        */

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
        IM = consolidate.optimize(IM);

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
    /*
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
     */

    public void permuteManual(int amountVM,int amountPM){
        //Exhaustively search all permutations
        permuteHelper(amountVM,amountPM+1,"");
    }


    public void permuteIM(InfrastructureModel IM) {
        //Recursively create all permutations
        permuteHelper(IM.items.length, IM.bins.length + 1, "");
    }

    public void permuteHelper(int amountVM, int amountPM, String output) {
        if (amountVM == 0) {
            permutations.add(output);
        } else {
            //Loop through each PM and recursively calculate permutations
            for (int i = 1; i < amountPM; i++) {
                permuteHelper(amountVM - 1, amountPM, output + i);
            }
        }
    }

    public void buildModel(String perm,InfrastructureModel modelToCopy){
        InfrastructureModel x = new InfrastructureModel(modelToCopy, new GenHelper() {

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

    public void optimalPerm(ArrayList<InfrastructureModel> list, InfrastructureModel currentBest) {
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


    public String dataSetRow() {
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
                        optIM.get(randMap).items[i].getHostID() + "," + optIM.get(randMap).items[i].hashCode() + ",");
                //Appending all VM's hosts
                for (int j = 0; j < optIM.get(randMap).items.length; j++) {
                    data.append(optIM.get(randMap).items[i].basedetails.vm.getResourceAllocation().allocated.getRequiredCPUs() + "," +
                            optIM.get(randMap).items[i].basedetails.vm.getResourceAllocation().allocated.getRequiredMemory() + "," +
                            optIM.get(randMap).items[i].getHostID());
                    if(j<optIM.get(randMap).items.length-1){
                        data.append(",");
                    }else{
                        data.append("\n");
                    }
                }
            }
            return data.toString();
        }else{
            return "";
        }
    }

}
