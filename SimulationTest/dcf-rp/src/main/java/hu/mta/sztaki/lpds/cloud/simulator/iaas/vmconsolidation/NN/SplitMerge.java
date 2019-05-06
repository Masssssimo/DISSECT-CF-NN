package hu.mta.sztaki.lpds.cloud.simulator.iaas.vmconsolidation.NN;

import hu.mta.sztaki.lpds.cloud.simulator.iaas.IaaSService;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.PhysicalMachine;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.vmconsolidation.model.InfrastructureModel;

import java.util.ArrayList;

public class SplitMerge {

    /**
     * Splitting the IaaSService into InfrastructureModels to execute distributed consolidation
     */

    public ArrayList<InfrastructureModel> splitBefore(IaaSService toConsolidate, int split){
        if(split==1){
            return individualSplit(toConsolidate,split);
        }else if(toConsolidate.machines.size()%split==0) {
            //Infrastructure split evenly
            return evenSplit(toConsolidate, split);
        }
        //Infrastructure cannot be evenly split - there is a remainder
        return remainderSplit(toConsolidate,split);
    }

    private ArrayList<InfrastructureModel> individualSplit(IaaSService toConsolidate, int split){
        //Split up IaaSService individually
        ArrayList<InfrastructureModel> splitIMs = new ArrayList<>();
        for(int i=0;i<toConsolidate.machines.size();i+=split){
            ArrayList<PhysicalMachine> splitPMs = new ArrayList<>();
            //Split each PhysicalMachine into an InfrastructureModel
            splitPMs.add(toConsolidate.machines.get(i));
            //Convert ArrayList to Array to construct Infrastructure Model
            PhysicalMachine[] convert = splitPMs.toArray(new PhysicalMachine[splitPMs.size()]);
            InfrastructureModel x = new InfrastructureModel(convert, 1, false, 1);
            splitIMs.add(x);
        }
        return splitIMs;
    }

    private ArrayList<InfrastructureModel> evenSplit(IaaSService toConsolidate, int split){
        //Split up IaaSService into segments
        ArrayList<InfrastructureModel> splitIMs = new ArrayList<>();
        for(int i=0;i<toConsolidate.machines.size();i+=split){
            ArrayList<PhysicalMachine> splitPMs = new ArrayList<>();
            for(int j=0;j<split;j++){
                splitPMs.add(toConsolidate.machines.get(i+j));
            }
            //Convert ArrayList to Array to construct Infrastructure Model
            PhysicalMachine[] convert = splitPMs.toArray(new PhysicalMachine[splitPMs.size()]);
            InfrastructureModel x = new InfrastructureModel(convert, 1, false, 1);
            splitIMs.add(x);
        }
        return splitIMs;
    }

    private ArrayList<InfrastructureModel> remainderSplit(IaaSService toConsolidate, int split){
        //Split up IaaSService into segments
        ArrayList<InfrastructureModel> splitIMs = new ArrayList<>();
        for(int i=0;i<toConsolidate.machines.size();i+=split){
            //Split accordingly - as currently no remainder
            if(!((i+split) > (toConsolidate.machines.size()))){
                ArrayList<PhysicalMachine> splitPMs = new ArrayList<>();
                for (int j = 0; j < split; j++) {
                    splitPMs.add(toConsolidate.machines.get(i + j));
                }
                //Convert ArrayList to Array to construct Infrastructure Model
                PhysicalMachine[] convert = splitPMs.toArray(new PhysicalMachine[splitPMs.size()]);
                InfrastructureModel x = new InfrastructureModel(convert, 1, false, 1);
                splitIMs.add(x);
            }else{
                    //Add remainingcd 
                ArrayList<PhysicalMachine> splitPMs = new ArrayList<>();
                for (; i < toConsolidate.machines.size();i++) {
                    splitPMs.add(toConsolidate.machines.get(i));
                }
                //Convert ArrayList to Array to construct Infrastructure Model
                PhysicalMachine[] convert = splitPMs.toArray(new PhysicalMachine[splitPMs.size()]);
                InfrastructureModel x = new InfrastructureModel(convert, 1, false, 1);
                splitIMs.add(x);

            }
        }
        return splitIMs;
    }


    /**
     * Merge split InfrastructureModels
     */

    public InfrastructureModel mergeIMs(ArrayList<InfrastructureModel> splitIMs){
        //Get consolidated PhysicalMachines
        ArrayList<PhysicalMachine> splitPMs = new ArrayList<>();
        for(int i=0;i<splitIMs.size();i++){
            for(int j=0;j<splitIMs.get(i).bins.length;j++){
                splitPMs.add(splitIMs.get(i).bins[j].getPM());
            }
        }

        //Convert ArrayList to Array to construct merged InfrastructureModel
        PhysicalMachine[] convert = splitPMs.toArray(new PhysicalMachine[splitPMs.size()]);
        //Merged InfrastructureModel
        InfrastructureModel merged = new InfrastructureModel(convert, 1, false, 1);

        //Merged InfrastructureModel
        return merged;
    }
}
