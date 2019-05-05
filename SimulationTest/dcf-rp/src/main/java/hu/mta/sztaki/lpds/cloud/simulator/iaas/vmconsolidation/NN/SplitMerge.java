package hu.mta.sztaki.lpds.cloud.simulator.iaas.vmconsolidation.NN;

import hu.mta.sztaki.lpds.cloud.simulator.iaas.IaaSService;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.PhysicalMachine;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.vmconsolidation.model.InfrastructureModel;

import java.util.ArrayList;

public class SplitMerge {


    public static ArrayList<InfrastructureModel> splitIMs = new ArrayList<>();
    public static ArrayList<InfrastructureModel> consolidatedIMs = new ArrayList<>();

    /**
     * Splitting the infrastructure model for the NeuralNetworkConsolidator to execute distributed consolidation
     */
    public static InfrastructureModel splitBefore(IaaSService toConsolidate){
        for(int i=0;i<toConsolidate.machines.size();i+=4){
            PhysicalMachine[] splitPMs = {toConsolidate.machines.get(i),toConsolidate.machines.get(i+1),toConsolidate.machines.get(i+2),toConsolidate.machines.get(i+3)};
            InfrastructureModel x = new InfrastructureModel(splitPMs, 1, false, 1);
            splitIMs.add(x);
        }
        //Execute consolidation
        return consolidateSplit(toConsolidate);
    }

    /**
     * Consolidate the split infrastructure models via NeuralNetworkConsolidator
     */
    public static InfrastructureModel consolidateSplit(IaaSService toConsolidate){
        //Checking mapping before consolidation

		for(int j=0;j<splitIMs.size();j++){
			for(int i=0;i<splitIMs.get(j).bins.length;i++){
				for(int k=0;k<splitIMs.get(j).bins[i].getVMs().size();k++){
					System.out.println("IM-"+i+"\t->\t PM-"+splitIMs.get(j).bins[i].hashCode()+"\t->\t VMs-"+splitIMs.get(j).bins[i].getVMs().get(k).hashCode());
				}
			}
		}



        //Executing consolidation on each split infrastructure
        for(int i=0;i<splitIMs.size();i++){
            NerualNetworkConsolidator consolidate = new NerualNetworkConsolidator(toConsolidate,0);
            consolidatedIMs.add(consolidate.optimize(splitIMs.get(i)));
        }
        return mergeIMs(toConsolidate);
    }

    /**
     * Merge the split infrastructure models back
     */
    public static InfrastructureModel mergeIMs(IaaSService toConsolidate){
        //Check mapping after consolidation
        /*
        for(int j=0;j<consolidatedIMs.size();j++){
            for(int i=0;i<consolidatedIMs.get(j).bins.length;i++){
                for(int k=0;k<consolidatedIMs.get(j).bins[i].getVMs().size();k++){
                    System.out.println("IM-"+i+"\t->\t PM-"+consolidatedIMs.get(j).bins[i].hashCode()+"\t->\t VMs-"+consolidatedIMs.get(j).bins[i].getVMs().get(k).hashCode());
                }
            }
        }
        */

        //Get consolidated pms
        ArrayList<PhysicalMachine> list = new ArrayList<>();
        for(int i=0;i<consolidatedIMs.size();i++){
            for(int j=0;j<consolidatedIMs.get(i).bins.length;j++){
                list.add(consolidatedIMs.get(i).bins[j].getPM());
            }
        }

        //Convert ArrayList to Array to construct merged IM
        PhysicalMachine[] convert = list.toArray(new PhysicalMachine[list.size()]);
        //Merged IM
        InfrastructureModel merged = new InfrastructureModel(convert, 1, false, 1);

        //Checking mapping after merge
        /*
		for(int i=0;i<merged.bins.length;i++){
			for(int j=0;j<merged.bins[i].getVMs().size();j++){
				System.out.println("PM-"+merged.bins[i].hashCode()+"\t->\t VMs-"+merged.bins[i].getVMs().get(j).hashCode());
			}
		}
         */


        //Merged IM
        return merged;
    }
}
