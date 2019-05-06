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
