package hu.mta.sztaki.lpds.cloud.simulator.iaas.vmconsolidation.NN;

import hu.mta.sztaki.lpds.cloud.simulator.iaas.PhysicalMachine;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.vmconsolidation.model.InfrastructureModel;

import java.util.ArrayList;

/**
 * This class is intended for dividing an InfrastructureModel
 * instance into smaller InfrastructureModels.
 *
 * These InfrastructureModels can further be consolidated
 * individually and then merged back into one InfrastructureModel
 * instance.
 *
 * This merged InfrastructureModel can be used to reconstruct
 * the original IaaSService instance.
 */

public class SplitMerge {

    /**
     * The splitBefore function checks whether the PhysicalMachines
     * (that build the InfrastructureModel instance) can be divided
     * evenly via the requested split value.
     *
     * The InfrastructureModel is divided accordingly - an ArrayList of
     * InfrastructureModels is then returned.
     *
     * @param toSplit
     *            the InfrastructureModel being split up into smaller
     *            InfrastructureModels
     *
     * @param split
     *            the amount of PhysicalMachines that construct each
     *            InfrastructureModel
     */

    public ArrayList<InfrastructureModel> splitBefore(InfrastructureModel toSplit, int split){
        if(split==1){
            //Individually split PhysicalMachines into separate InfrastructureModels
            return individualSplit(toSplit,split);
        }else if(toSplit.bins.length%split==0) {
            //Infrastructure split evenly
            return evenSplit(toSplit, split);
        }
        //Infrastructure cannot be evenly split - there is a remainder
        return remainderSplit(toSplit,split);
    }

    /**
     * The individualSplit function divides the PhysicalMachines (that
     * build the InfrastructureModel instance toSplit) into individual
     * InfrastructureModels.
     *
     * An ArrayList of each PhysicalMachine as an InfrastructureModel
     * is returned.
     *
     * @param toSplit
     *            the InfrastructureModel being split up into smaller
     *            InfrastructureModels
     *
     * @param split
     *            the amount of PhysicalMachines that construct each
     *            InfrastructureModel (i.e. this value is always 1 as
     *            each PhysicalMachine instance constructs an individual
     *            InfrastructureModel)
     */

    private ArrayList<InfrastructureModel> individualSplit(InfrastructureModel toSplit, int split){
        //Split up InfrastructureModel individually
        ArrayList<InfrastructureModel> splitIMs = new ArrayList<>();
        for(int i=0;i<toSplit.bins.length;i+=split){
            ArrayList<PhysicalMachine> splitPMs = new ArrayList<>();
            //Split each PhysicalMachine into an InfrastructureModel
            splitPMs.add(toSplit.bins[i].getPM());
            //Convert ArrayList to Array to construct Infrastructure Model
            PhysicalMachine[] convert = splitPMs.toArray(new PhysicalMachine[splitPMs.size()]);
            InfrastructureModel x = new InfrastructureModel(convert, 1, false, 1);
            splitIMs.add(x);
        }
        return splitIMs;
    }

    /**
     * The evenSplit function divides the PhysicalMachines (that build
     * the InfrastructureModel instance) into smaller sized InfrastructureModels.
     *
     * The PhysicalMachines can be divided evenly by the split value
     * requested. (i.e. there is no remaining PhysicalMachines)
     *
     * An ArrayList of evenly split PhysicalMachines (constructed
     * into InfrastructureModels) is returned.
     *
     * @param toSplit
     *            The InfrastructureModel being split up into smaller
     *            InfrastructureModels
     *
     * @param split
     *            The amount of PhysicalMachines that construct each
     *            InfrastructureModel
     */

    private ArrayList<InfrastructureModel> evenSplit(InfrastructureModel toSplit, int split){
        //Split up InfrastructureModel into segments
        ArrayList<InfrastructureModel> splitIMs = new ArrayList<>();
        for(int i=0;i<toSplit.bins.length;i+=split){
            ArrayList<PhysicalMachine> splitPMs = new ArrayList<>();
            for(int j=0;j<split;j++){
                splitPMs.add(toSplit.bins[i+j].getPM());
            }
            //Convert ArrayList to Array to construct Infrastructure Model
            PhysicalMachine[] convert = splitPMs.toArray(new PhysicalMachine[splitPMs.size()]);
            InfrastructureModel x = new InfrastructureModel(convert, 1, false, 1);
            splitIMs.add(x);
        }
        return splitIMs;
    }

    /**
     * The remainderSplit function divides the PhysicalMachines
     * (that build the IaaSService instance) into smaller sized
     * InfrastructureModels.
     *
     * This function is called when the PhysicalMachines can not
     * be divided evenly by the split value requested. (i.e. there
     * is remainder after dividing the PhysicalMachines via the
     * split value)
     *
     * This function evenly divides the PhysicalMachines into
     * InfrastructureModels until the remaining value of
     * PhysicalMachines is reached.
     *
     * The remaining PhysicalMachines are constructed into an
     * InfrastructureModel.
     *
     * An ArrayList of split PhysicalMachines (constructed into
     * InfrastructureModels) is returned.
     *
     * (i.e. each InfrastructureModel within the ArrayList
     * consists of evenly split PhysicalMachines - except
     * the last item within the ArrayList which consists of
     * the remaining PhysicalMachine constructed into an
     * InfrastructureModel)
     *
     * @param toSplit
     *            The InfrastructureModel being split up into
     *            smaller InfrastructureModels
     *
     * @param split
     *            The amount of PhysicalMachines that construct
     *            each InfrastructureModel
     */

    private ArrayList<InfrastructureModel> remainderSplit(InfrastructureModel toSplit, int split){
        //Split up IaaSService into segments
        ArrayList<InfrastructureModel> splitIMs = new ArrayList<>();
        for(int i=0;i<toSplit.bins.length;i+=split){
            //Split accordingly - as currently no remainder
            if(!((i+split) > (toSplit.bins.length))){
                ArrayList<PhysicalMachine> splitPMs = new ArrayList<>();
                for (int j = 0; j < split; j++) {
                    splitPMs.add(toSplit.bins[i + j].getPM());
                }
                //Convert ArrayList to Array to construct Infrastructure Model
                PhysicalMachine[] convert = splitPMs.toArray(new PhysicalMachine[splitPMs.size()]);
                InfrastructureModel x = new InfrastructureModel(convert, 1, false, 1);
                splitIMs.add(x);
            }else{
                //Add remaining PhysicalMachines into one InfrastructureModel
                ArrayList<PhysicalMachine> splitPMs = new ArrayList<>();
                for (; i < toSplit.bins.length;i++) {
                    splitPMs.add(toSplit.bins[i].getPM());
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
     * The mergeIMs function merges each InfrastructureModel within
     * the splitIMs ArrayList into one InfrastructureModel.
     *
     * @param splitIMs
     *            The InfrastructureModel ArrayList being merged into
     *            one InfrastructureModel
     *
     */

    public static InfrastructureModel mergeIMs(ArrayList<InfrastructureModel> splitIMs){
        InfrastructureModel[] x = splitIMs.toArray(new InfrastructureModel[splitIMs.size()]);
         //Construct new InfrastructureModel
        InfrastructureModel merged = new InfrastructureModel(x);
        return merged;
    }
}
