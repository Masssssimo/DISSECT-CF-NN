package hu.mta.sztaki.lpds.cloud.simulator.iaas.vmconsolidation.NN;

import hu.mta.sztaki.lpds.cloud.simulator.iaas.IaaSService;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.vmconsolidation.ModelBasedConsolidator;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.vmconsolidation.model.InfrastructureModel;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.vmconsolidation.model.PreserveAllocations;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.vmconsolidation.model.improver.NonImprover;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.api.ops.impl.indexaccum.IAMax;
import org.nd4j.linalg.dataset.api.preprocessor.StandardizeStrategy;
import org.nd4j.linalg.dataset.api.preprocessor.stats.DistributionStats;
import org.nd4j.linalg.factory.Nd4j;

import java.io.File;
import java.util.ArrayList;

public class NerualNetworkConsolidator extends ModelBasedConsolidator {

    // List of Networks
    private ArrayList<MultiLayerNetwork> networks = new ArrayList<>();
    // List of Standard Deviations for normalization
    private ArrayList<INDArray> std = new ArrayList<>();
    // List of means for normalization
    private ArrayList<INDArray> mean = new ArrayList<>();

    // Building INDArray dimensions for NN input
    private Integer nRows = 1;
    private ArrayList<Integer> nColumns = new ArrayList<>();

    public NerualNetworkConsolidator(final IaaSService toConsolidate, final long consFreq) {
        super(toConsolidate, consFreq);
        //Configuration initialization
        try {
            //Add network configurations for 1 VM
            File modelVM1 = new File("src/main/resources/VM-1-NN.zip");
            MultiLayerNetwork networkVM1 = ModelSerializer.restoreMultiLayerNetwork(modelVM1);
            networks.add(networkVM1);
            // Set columns amount
            nColumns.add(9);
            // Set standard deviation for 1 VM
            INDArray std1 = Nd4j.create(new double[]{1e-5, 1.1545, 2.8851e8, 1e-5, 1e-5, 1e-5, 1e-5, 1e-5, 1e-5});
            std.add(std1);
            // Set mean for 1 VM
            INDArray mean1 = Nd4j.create(new double[]{0, 2.0013, 4.9954e8, 0.0010, 1.0000, 0, 1.0000, 0, 0});
            mean.add(mean1);

            //Add network for 2 VM
            File modelVM2 = new File("src/main/resources/VM-2-NN.zip");
            MultiLayerNetwork networkVM2 = ModelSerializer.restoreMultiLayerNetwork(modelVM2);
            networks.add(networkVM2);
            // Set columns amount
            nColumns.add(7);
            // Set standard deviation for 2 VM
            INDArray std2 = Nd4j.create(new double[]{0.5000, 1.1269, 2.8952e8, 0.5000, 1.1271, 2.8973e8, 0.5000});
            std.add(std2);
            // Set mean for 2 VM
            INDArray mean2 = Nd4j.create(new double[]{0.5000, 1.9998, 5.3624e8, 0.5000, 2.0005, 5.3759e8, 2.5000});
            mean.add(mean2);

            //Add network for 3 VM
            File modelVM3 = new File("src/main/resources/VM-3-NN.zip");
            MultiLayerNetwork networkVM3 = ModelSerializer.restoreMultiLayerNetwork(modelVM3);
            networks.add(networkVM3);
            // Set columns amount
            nColumns.add(10);
            // Set standard deviation for 3 VM
            INDArray std3 = Nd4j.create(new double[]{0.8165, 1.1255, 2.8996e8, 0.4330, 1.1238, 2.9029e8, 0.5000, 1.1261, 2.8982e8, 0.4330});
            std.add(std3);
            // Set mean for 3 VM
            INDArray mean3 = Nd4j.create(new double[]{1.0000, 2.0005, 5.3574e8, 0.2500, 2.0019, 5.3625e8, 1.5000, 1.9971, 5.3644e8, 2.7500});
            mean.add(mean3);

            //Add network for 4 VM
            File modelVM4 = new File("src/main/resources/VM-4-NN.zip");
            MultiLayerNetwork networkVM4 = ModelSerializer.restoreMultiLayerNetwork(modelVM4);
            networks.add(networkVM4);
            // Set columns amount
            nColumns.add(13);
            // Set standard deviation for 4 VM
            INDArray std4 = Nd4j.create(new double[]{1.1180, 1.1263, 2.9034e8, 1e-5, 1.1260, 2.8974e8, 1e-5, 1.1259, 2.8996e8, 1e-5, 1.1250, 2.8983e8, 1e-5});
            std.add(std4);
            // Set mean for 4 VM
            INDArray mean4 = Nd4j.create(new double[]{1.5000, 2.0002, 5.3674e8, 0, 2.0022, 5.3672e8, 1.0000, 1.9988, 5.3672e8, 2.0000, 1.9975, 5.3602e8, 3.0000});
            mean.add(mean4);

            //Add network for 5 VM
            File modelVM5 = new File("src/main/resources/VM-5-NN.zip");
            MultiLayerNetwork networkVM5 = ModelSerializer.restoreMultiLayerNetwork(modelVM5);
            networks.add(networkVM5);
            // Set columns amount
            nColumns.add(16);
            // Set standard deviation for 5 VM
            INDArray std5 = Nd4j.create(new double[]{1.4142, 1.1135, 2.9025e8, 0.0020, 1.0974, 2.9002e8, 0.4330, 1.0874, 2.899e8, 0.5000, 1.1000, 2.9005e8, 0.4343, 1.1132, 2.899e8, 1e-5});
            std.add(std5);
            // Set mean for 5 VM
            INDArray mean5 = Nd4j.create(new double[]{2.0000, 2.0393, 5.3674e8, 4e-6, 1.8484, 5.3632e8, 0.7501, 1.8196, 5.366e8, 1.5010, 1.8513, 5.3683e8, 2.2522, 2.0414, 5.3701e8, 3.0000});
            mean.add(mean5);

            //Add network for 6 VM
            File modelVM6 = new File("src/main/resources/VM-6-NN.zip");
            MultiLayerNetwork networkVM6 = ModelSerializer.restoreMultiLayerNetwork(modelVM6);
            networks.add(networkVM6);
            // Set columns amount
            nColumns.add(19);
            // Set standard deviation for 6 VM
            INDArray std6 = Nd4j.create(new double[]{1.7078, 1.1230, 2.8986e8, 1e-5, 1.0320, 2.9015e8, 0.4999, 1.0882, 2.896e8, 0.3423, 1.0886, 2.8984e8, 0.3336, 1.0333, 2.9017e8, 0.5000, 1.1172, 2.9027e8, 0.0040});
            std.add(std6);
            // Set mean for 6 VM
            INDArray mean6 = Nd4j.create(new double[]{2.5000, 2.0045, 5.3739e8, 0, 1.6273, 5.3756e8, 0.5097, 1.8486, 5.3716e8, 1.1179, 1.8598, 5.3683e8, 1.8905, 1.6243, 5.3675e8, 2.4971, 2.0254, 5.3685e8, 3.0000});
            mean.add(mean6);

            //Add network for 7 VM
            File modelVM7 = new File("src/main/resources/VM-7-NN.zip");
            MultiLayerNetwork networkVM7 = ModelSerializer.restoreMultiLayerNetwork(modelVM7);
            networks.add(networkVM7);
            // Set columns amount
            nColumns.add(22);
            // Set standard deviation for 7 VM
            INDArray std7 = Nd4j.create(new double[]{2.0000, 1.1386, 2.8984e8, 0.0035, 0.9863, 2.8987e8, 0.4586, 1.1017, 2.8984e8, 0.2818, 1.0173, 2.8951e8, 0.5016, 1.1030, 2.901e8, 0.2815, 0.9841, 2.9015e8, 0.4610, 1.1372, 2.9054e8, 0.0049});
            std.add(std7);
            // Set mean for 7 VM
            INDArray mean7 = Nd4j.create(new double[]{3.0000, 1.9254, 5.3669e8, 1.2e-5, 1.5320, 5.3702e8, 0.3007, 1.7879, 5.3623e8, 0.9741, 1.6058, 5.3773e8, 1.4961, 1.7925, 5.3695e8, 2.0230, 1.5241, 5.3758e8, 2.6936, 1.9334, 5.376e8, 3.0000});
            mean.add(mean7);

            //Add network for 8 VM
            File modelVM8 = new File("src/main/resources/VM-8-NN.zip");
            MultiLayerNetwork networkVM8 = ModelSerializer.restoreMultiLayerNetwork(modelVM8);
            networks.add(networkVM8);
            // Set columns amount
            nColumns.add(25);
            // Set standard deviation for 8 VM
            INDArray std8 = Nd4j.create(new double[]{2.2913, 1.1526, 2.9012e8, 0.0057, 0.9952, 2.9025e8, 0.3785, 1.0641, 2.8977e8, 0.3940, 1.0497, 2.8987e8, 0.3994, 1.0407, 2.9028e8, 0.4026, 1.0727, 2.9047e8, 0.3911, 0.9738, 2.9034e8, 0.3843, 1.1449, 2.8984e8, 0.0045});
            std.add(std8);
            // Set mean for 8 VM
            INDArray mean8 = Nd4j.create(new double[]{3.5000, 1.7719, 5.3705e8, 3.2e-5, 1.5219, 5.3713e8, 0.1733, 1.5712, 5.3635e8, 0.8344, 1.5993, 5.3734e8, 1.1831, 1.5856, 5.3823e8, 1.8126, 1.5979, 5.3587e8, 2.1594, 1.4703, 5.3745e8, 2.8199, 1.8582, 5.3691e8, 3.0000});
            mean.add(mean8);

            //Add network for 9 VM
            File modelVM9 = new File("src/main/resources/VM-9-NN.zip");
            MultiLayerNetwork networkVM9 = ModelSerializer.restoreMultiLayerNetwork(modelVM9);
            networks.add(networkVM9);
            // Set columns amount
            nColumns.add(28);
            // Set standard deviation for 9 VM
            INDArray std9 = Nd4j.create(new double[]{2.5820, 1.1493, 2.8955e8, 0.0040, 1.0022, 2.901e8, 0.3210, 0.9991, 2.9034e8, 0.4805, 1.0784, 2.8979e8, 0.3080, 0.9848, 2.9001e8, 0.5032, 1.0734, 2.8974e8, 0.3099, 1.0062, 2.8985e8, 0.4810, 0.9893, 2.9008e8, 0.3225, 1.1453, 2.9015e8, 0.0040});
            std.add(std9);
            // Set mean for 9 VM
            INDArray mean9 = Nd4j.create(new double[]{4.0000, 1.6690, 5.3644e8, 1.6e-5, 1.4701, 5.3681e8, 0.1167, 1.3842, 5.3684e8, 0.6602, 1.5533, 5.3708e8, 1.0466, 1.4164, 5.3687e8, 1.4991, 1.5571, 5.3657e8, 1.9524, 1.3925, 5.3505e8, 2.3396, 1.4441, 5.3679e8, 2.8821, 1.7291, 5.3669e8, 3.0000});
            mean.add(mean9);

            //Add network for 10 VM
            File modelVM10 = new File("src/main/resources/VM-10-NN.zip");
            MultiLayerNetwork networkVM10 = ModelSerializer.restoreMultiLayerNetwork(modelVM10);
            networks.add(networkVM10);
            // Set columns amount
            nColumns.add(31);
            // Set standard deviation for 10 VM
            INDArray std10 = Nd4j.create(new double[]{2.8723, 1.1431, 2.8975e8, 1e-5, 1.0125, 2.8947e8, 0.2727, 0.9398, 2.903e8, 0.5029, 1.0600, 2.8985e8, 0.3257, 1.0006, 2.902e8, 0.4425, 0.9979, 2.898e8, 0.4428, 1.0615, 2.9027e8, 0.3255, 0.9450, 2.9032e8, 0.5030, 1.0060, 2.8975e8, 0.2716, 1.1406, 2.9017e8, 0.0028});
            std.add(std10);
            // Set mean for 10 VM
            INDArray mean10 = Nd4j.create(new double[]{4.5000, 1.5720, 5.3605e8, 0, 1.4049, 5.372e8, 0.0809, 1.2549, 5.3725e8, 0.5025, 1.4297, 5.3657e8, 0.9536, 1.3681, 5.3782e8, 1.2473, 1.3546, 5.3657e8, 1.7534, 1.4425, 5.3776e8, 2.0472, 1.2539, 5.3674e8, 2.4973, 1.3963, 5.3662e8, 2.9198, 1.6006, 5.3659e8, 3.0000});
            mean.add(mean10);

            //Add network for 11 VM
            File modelVM11 = new File("src/main/resources/VM-11-NN.zip");
            MultiLayerNetwork networkVM11 = ModelSerializer.restoreMultiLayerNetwork(modelVM11);
            networks.add(networkVM11);
            // Set columns amount
            nColumns.add(34);
            // Set standard deviation for 11 VM
            INDArray std11 = Nd4j.create(new double[]{3.1623, 1.1286, 2.8951e8, 0.0020, 1.0160, 2.9001e8, 0.2337, 0.9067, 2.9026e8, 0.4868, 1.0148, 2.8981e8, 0.3968, 1.0205, 2.8955e8, 0.3620, 0.9452, 2.9048e8, 0.5046, 1.0196, 2.9018e8, 0.3608, 1.0153, 2.8968e8, 0.3968, 0.9075, 2.8997e8, 0.4862, 1.0132, 2.8977e8, 0.2320, 1.1265, 2.9028e8, 1e-5});
            std.add(std11);
            // Set mean for 11 VM
            INDArray mean11 = Nd4j.create(new double[]{5.0000, 1.4805, 5.3637e8, 4e-6, 1.3381, 5.3711e8, 0.0580, 1.1699, 5.3715e8, 0.3794, 1.2830, 5.3686e8, 0.8492, 1.3187, 5.3699e8, 1.1069, 1.2272, 5.3683e8, 1.5017, 1.3209, 5.3574e8, 1.8937, 1.2852, 5.3611e8, 2.1520, 1.1666, 5.3754e8, 2.6229, 1.3385, 5.3614e8, 2.9429, 1.4758, 5.3632e8, 3.0000});
            mean.add(mean11);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void processProps() {
    }

    @Override
    public InfrastructureModel optimize(final InfrastructureModel input) {
        InfrastructureModel sol=new InfrastructureModel(input, PreserveAllocations.singleton, NonImprover.singleton);
        //Selecting what network
        Integer index = null;

        switch (sol.items.length) {
            case 0:
                //Do nothing
                return sol;
            case 1:
                index = 0;
                break;
            case 2:
                index = 1;
                break;
            case 3:
                index = 2;
                break;
            case 4:
                index = 3;
                break;
            case 5:
                index = 4;
                break;
            case 6:
                index = 5;
                break;
            case 7:
                index = 6;
                break;
            case 8:
                index = 7;
                break;
            case 9:
                index = 8;
                break;
            case 10:
                index = 9;
                break;
            case 11:
                index = 10;
                break;
            default:
                System.err.println("Too many VMs too Consolidate");
                return sol;
        }

        /* Check how many virtual machine instances for JobDispatchDemo
        if(sol.items.length==11){
            System.err.println("VM amount -\t"+sol.items.length);
        }else{
            System.out.println("VM amount -\t"+sol.items.length);
        }
        */


        // Standard deviation and means
        DistributionStats DisStat = new DistributionStats(mean.get(index), std.get(index));
        // Set up normalization
        StandardizeStrategy normalize = new StandardizeStrategy();


        //System.out.println("SOL\t"+sol.bins);

        for (int i = 0; i < sol.items.length; i++) {

            // Before consolidation
            //System.err.println("B-VM-"+sol.items[i].hashCode()+"\t->\tB-HOST-"+sol.items[i].getHostID());

            // Create INDArray of zeros
            INDArray data = Nd4j.zeros(nRows, nColumns.get(index));

            // Adding VM Constraints
            data.put(0, 0, sol.items[i].hashCode());

            int x = 0;
            for (int j = 0; j < sol.items.length; j++) {
                if (j != i) {
                    data.put(0, x, sol.items[j].basedetails.vm.getResourceAllocation().allocated.getRequiredCPUs());
                    x++;
                    data.put(0, x, sol.items[j].basedetails.vm.getResourceAllocation().allocated.getRequiredMemory());
                    x++;
                    data.put(0, x, sol.items[j].getHostID());
                    x++;
                }
            }

            // Normalize Input
            normalize.preProcess(data, null, DisStat);

            // Output from Network
            INDArray result = networks.get(index).output(data, false).toDense();

            // Index of the maximum value in the array (Classification)
            int idx = Nd4j.getExecutioner().execAndReturn(new IAMax(result)).getFinalResult();


            if (sol.items[i].getHostID() != idx) {
                sol.items[i].migrate(sol.bins[idx]);
            }

            // After consolidation
            //System.out.println("A-VM-"+sol.items[i].hashCode()+"\t->\tA-HOST-"+sol.items[i].getHostID());

        }
        sol.calculateFitness();
        return sol;
    }

    /**
     * Consolidate the split InfrastructureModels individually via NeuralNetworkConsolidator
     */
    public InfrastructureModel consolidateSplit(IaaSService toConsolidate){
        //Split and Merge object for splitting the IaaSService
        SplitMerge sm = new SplitMerge();
        //Splitting the IaaSService
        ArrayList<InfrastructureModel> splitIMs = sm.splitBefore(toConsolidate,4);

        //Checking mapping before consolidation
        /*
		for(int j=0;j<splitIMs.size();j++){
			for(int i=0;i<splitIMs.get(j).bins.length;i++){
				for(int k=0;k<splitIMs.get(j).bins[i].getVMs().size();k++){
					System.out.println("IM-"+i+"\t->\t PM-"+splitIMs.get(j).bins[i].hashCode()+"\t->\t VMs-"+splitIMs.get(j).bins[i].getVMs().get(k).hashCode());
				}
			}
		}
		*/


        //Consolidated split infrastructureModels
        ArrayList<InfrastructureModel> consolidatedIMs = new ArrayList<>();

        //Executing consolidation on each split infrastructure
        for(int i=0;i<splitIMs.size();i++){
            consolidatedIMs.add(optimize(splitIMs.get(i)));
        }

        //Check mapping after consolidation
        /*
        for(int j=0;j<consolidatedIMs.size();j++){
            for(int i=0;i<consolidatedIMs.get(j).bins.length;i++){
                for(int k=0;k<consolidatedIMs.get(j).bins[i].getVMs().size();k++){
                    System.err.println("IM-"+i+"\t->\t PM-"+consolidatedIMs.get(j).bins[i].hashCode()+"\t->\t VMs-"+consolidatedIMs.get(j).bins[i].getVMs().get(k).hashCode());
                }
            }
        }
        */



        //Merge InfrastructureModel into one instance
        InfrastructureModel merged = sm.mergeIMs(consolidatedIMs);

        //Checking mapping after merge
        /*
		for(int i=0;i<merged.bins.length;i++){
			for(int j=0;j<merged.bins[i].getVMs().size();j++){
				System.out.println("PM-"+merged.bins[i].hashCode()+"\t->\t VMs-"+merged.bins[i].getVMs().get(j).hashCode());
			}
		}
		*/

        return merged;
    }
}
