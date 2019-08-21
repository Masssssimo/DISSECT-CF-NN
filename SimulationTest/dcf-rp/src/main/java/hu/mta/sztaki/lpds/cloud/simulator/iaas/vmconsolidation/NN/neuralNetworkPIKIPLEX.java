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

public class neuralNetworkPIKIPLEX extends ModelBasedConsolidator{
    // List of Networks
    private ArrayList<MultiLayerNetwork> networks = new ArrayList<>();
    // List of Standard Deviations for normalization
    private ArrayList<INDArray> std = new ArrayList<>();
    // List of means for normalization
    private ArrayList<INDArray> mean = new ArrayList<>();
    // Building INDArray dimensions for NN input
    private Integer nRows = 1;
    private ArrayList<Integer> nColumns = new ArrayList<>();

    public Integer migrationCounter=0;

    /**
     * The constructor for VM consolidation. It expects an IaaSService, a value for
     * the upper threshold, a value for the lower threshold and a variable which
     * says how often the consolidation shall occur.
     *
     * @param toConsolidate The used IaaSService.
     * @param consFreq      This value determines, how often the consolidation
     */
    public neuralNetworkPIKIPLEX(IaaSService toConsolidate, long consFreq) throws Exception {
        super(toConsolidate, consFreq);
        //Configuration initialization
        try {
            //Add network configurations for 1 VM
            File modelVM1 = new File("/home/mike/SimulationMLP/SimulationTest/dcf-rp/src/main/resources/VM-1-NN.zip");
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
            File modelVM2 = new File("/home/mike/SimulationMLP/SimulationTest/dcf-rp/src/main/resources/PIK-IPLEX-VM2-Layers-8-(error rate<0.05).zip");
            MultiLayerNetwork networkVM2 = ModelSerializer.restoreMultiLayerNetwork(modelVM2);
            networks.add(networkVM2);
            // Set columns amount
            nColumns.add(7);
            // Set standard deviation for 2 VM
            INDArray std2 = Nd4j.create(new double[]{0.5000,0.9709,6.2135e10,0.4981,0.9709,6.2135e10,0.4981});
            std.add(std2);
            // Set mean for 2 VM
            INDArray mean2 = Nd4j.create(new double[]{0.5010,3.4754,2.2242e11,1.4560,3.4754,2.2242e11,1.4560});
            mean.add(mean2);

            //Add network for 3 VM
            File modelVM3 = new File("/home/mike/SimulationMLP/SimulationTest/dcf-rp/src/main/resources/PIK-IPLEX-VM3-Layers-8-(error rate<0.05).zip");
            MultiLayerNetwork networkVM3 = ModelSerializer.restoreMultiLayerNetwork(modelVM3);
            networks.add(networkVM3);
            // Set columns amount
            nColumns.add(10);
            // Set standard deviation for 3 VM
            INDArray std3 = Nd4j.create(new double[]{0.8164,    1.0870, 6.9568e10,    0.8061,    1.0870, 6.9568e10,    0.8061,    1.0870, 6.9568e10,    0.8061});
            std.add(std3);
            // Set mean for 3 VM
            INDArray mean3 = Nd4j.create(new double[]{1.0007,    3.4068, 2.1804e11,    1.8734,    3.4068, 2.1804e11,    1.8734,    3.4068, 2.1804e11,    1.8734});
            mean.add(mean3);

            //Add network for 4 VM
            File modelVM4 = new File("/home/mike/SimulationMLP/SimulationTest/dcf-rp/src/main/resources/PIK-IPLEX-VM4-Layers-8-(error rate<0.05).zip");
            MultiLayerNetwork networkVM4 = ModelSerializer.restoreMultiLayerNetwork(modelVM4);
            networks.add(networkVM4);
            // Set columns amount
            nColumns.add(13);
            // Set standard deviation for 4 VM
            INDArray std4 = Nd4j.create(new double[]{ 1.1180,    1.0944, 7.0044e10,    1.0498,    1.0944, 7.0044e10,    1.0498,    1.0944, 7.0044e10,    1.0498,    1.0944, 7.0044e10,    1.0498});
            std.add(std4);
            // Set mean for 4 VM
            INDArray mean4 = Nd4j.create(new double[]{1.5003,    3.3891,  2.169e11,    1.5384,    3.3891,  2.169e11,    1.5384,    3.3891,  2.169e11,    1.5384,    3.3891,  2.169e11,    1.5384});
            mean.add(mean4);

            //Add network for 5 VM
            File modelVM5 = new File("/home/mike/SimulationMLP/SimulationTest/dcf-rp/src/main/resources/PIK-IPLEX-VM5-Layers-8-(error rate<0.05).zip");
            MultiLayerNetwork networkVM5 = ModelSerializer.restoreMultiLayerNetwork(modelVM5);
            networks.add(networkVM5);
            // Set columns amount
            nColumns.add(16);
            // Set standard deviation for 5 VM
            INDArray std5 = Nd4j.create(new double[]{1.4140,    1.4168, 9.0678e10,    0.9505,    1.4168, 9.0678e10,    0.9505,    1.4168, 9.0678e10,    0.9505,    1.4168, 9.0678e10,    0.9505,    1.4168, 9.0678e10,    0.9505});
            std.add(std5);
            // Set mean for 5 VM
            INDArray mean5 = Nd4j.create(new double[]{ 2.0005,    2.3405, 1.4979e11,    1.5892,    2.3405, 1.4979e11,    1.5892,    2.3405, 1.4979e11,    1.5892,    2.3405, 1.4979e11,    1.5892,    2.3405, 1.4979e11,    1.5892});
            mean.add(mean5);

            //Add network for 6 VM
            File modelVM6 = new File("/home/mike/SimulationMLP/SimulationTest/dcf-rp/src/main/resources/PIK-IPLEX-VM6-Layers-8-(error rate<0.05).zip");
            MultiLayerNetwork networkVM6 = ModelSerializer.restoreMultiLayerNetwork(modelVM6);
            networks.add(networkVM6);
            // Set columns amount
            nColumns.add(19);
            // Set standard deviation for 6 VM
            INDArray std6 = Nd4j.create(new double[]{1.7077,    1.3682, 8.7563e10,    0.9603,    1.3682, 8.7563e10,    0.9603,    1.3682, 8.7563e10,    0.9603,    1.3682, 8.7563e10,    0.9603,    1.3682, 8.7563e10,    0.9603,    1.3682, 8.7563e10,    0.9603});
            std.add(std6);
            // Set mean for 6 VM
            INDArray mean6 = Nd4j.create(new double[]{2.5004,    2.0545, 1.3149e11,    1.6327,    2.0545, 1.3149e11,    1.6327,    2.0545, 1.3149e11,    1.6327,    2.0545, 1.3149e11,    1.6327,    2.0545, 1.3149e11,    1.6327,    2.0545, 1.3149e11,    1.6327});
            mean.add(mean6);

            //Add network for 7 VM
            File modelVM7 = new File("/home/mike/SimulationMLP/SimulationTest/dcf-rp/src/main/resources/PIK-IPLEX-VM7-Layers-8-(error rate<0.05).zip");
            MultiLayerNetwork networkVM7 = ModelSerializer.restoreMultiLayerNetwork(modelVM7);
            networks.add(networkVM7);
            // Set columns amount
            nColumns.add(22);
            // Set standard deviation for 7 VM
            INDArray std7 = Nd4j.create(new double[]{1.9998,    1.3203, 8.4501e10,    0.9955,    1.3203, 8.4501e10,    0.9955,    1.3203, 8.4501e10,    0.9955,    1.3203, 8.4501e10,    0.9955,    1.3203, 8.4501e10,    0.9955,    1.3203, 8.4501e10,    0.9955,    1.3203, 8.4501e10,    0.9955});
            std.add(std7);
            // Set mean for 7 VM
            INDArray mean7 = Nd4j.create(new double[]{3.0004,    1.8963, 1.2137e11,    1.5877,    1.8963, 1.2137e11,    1.5877,    1.8963, 1.2137e11,    1.5877,    1.8963, 1.2137e11,    1.5877,    1.8963, 1.2137e11,    1.5877,    1.8963, 1.2137e11,    1.5877,    1.8963, 1.2137e11,    1.5877});
            mean.add(mean7);

            //Add network for 8 VM
            File modelVM8 = new File("/home/mike/SimulationMLP/SimulationTest/dcf-rp/src/main/resources/PIK-IPLEX-VM8-Layers-8-(error rate<0.05).zip");
            MultiLayerNetwork networkVM8 = ModelSerializer.restoreMultiLayerNetwork(modelVM8);
            networks.add(networkVM8);
            // Set columns amount
            nColumns.add(25);
            // Set standard deviation for 8 VM
            INDArray std8 = Nd4j.create(new double[]{2.2910,    1.0872, 6.9583e10,    0.9316,    1.0872, 6.9583e10,    0.9316,    1.0872, 6.9583e10,    0.9316,    1.0872, 6.9583e10,    0.9316,    1.0872, 6.9583e10,    0.9316,    1.0872, 6.9583e10,    0.9316,    1.0872, 6.9583e10,    0.9316,    1.0872, 6.9583e10,    0.9316});
            std.add(std8);
            // Set mean for 8 VM
            INDArray mean8 = Nd4j.create(new double[]{3.5006,    1.5396, 9.8534e10,    1.6569,    1.5396, 9.8534e10,    1.6569,    1.5396, 9.8534e10,    1.6569,    1.5396, 9.8534e10,    1.6569,    1.5396, 9.8534e10,    1.6569,    1.5396, 9.8534e10,    1.6569,    1.5396, 9.8534e10,    1.6569,    1.5396, 9.8534e10,    1.6569});
            mean.add(mean8);

            //Add network for 9 VM
            File modelVM9 = new File("/home/mike/SimulationMLP/SimulationTest/dcf-rp/src/main/resources/PIK-IPLEX-VM9-Layers-8-(error rate<0.05).zip");
            MultiLayerNetwork networkVM9 = ModelSerializer.restoreMultiLayerNetwork(modelVM9);
            networks.add(networkVM9);
            // Set columns amount
            nColumns.add(28);
            // Set standard deviation for 9 VM
            INDArray std9 = Nd4j.create(new double[]{2.5817,    0.9956, 6.3719e10,    0.9526,    0.9956, 6.3719e10,    0.9526,    0.9956, 6.3719e10,    0.9526,    0.9956, 6.3719e10,    0.9526,    0.9956, 6.3719e10,    0.9526,    0.9956, 6.3719e10,    0.9526,    0.9956, 6.3719e10,    0.9526,    0.9956, 6.3719e10,    0.9526,    0.9956, 6.3719e10,    0.9526});
            std.add(std9);
            // Set mean for 9 VM
            INDArray mean9 = Nd4j.create(new double[]{4.0007,    1.4346, 9.1811e10,    1.7040,    1.4346, 9.1811e10,    1.7040,    1.4346, 9.1811e10,    1.7040,    1.4346, 9.1811e10,    1.7040,    1.4346, 9.1811e10,    1.7040,    1.4346, 9.1811e10,    1.7040,    1.4346, 9.1811e10,    1.7040,    1.4346, 9.1811e10,    1.7040,    1.4346, 9.1811e10,    1.7040});
            mean.add(mean9);
        }catch (Exception e){
            System.err.println(e);
        }
    }

        @Override
        protected void processProps() {

        }

        @Override
        protected InfrastructureModel optimize(InfrastructureModel input){
            if(input.bins.length>4){
                return consolidateSplit(input);
            }else{
                return consolidate(input);
            }
        }
        private InfrastructureModel consolidate(InfrastructureModel input) {
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
                default:
                    //System.err.println("Too many VMs too Consolidate");
                    return sol;
            }
            // Standard deviation and means
            DistributionStats DisStat = new DistributionStats(mean.get(index), std.get(index));
            // Set up normalization
            StandardizeStrategy normalize = new StandardizeStrategy();


            for (int i = 0; i < sol.items.length; i++) {

                // Before consolidation
                //System.err.println("B-VM-"+sol.items[i].hashCode()+"\t->\tB-HOST-"+sol.items[i].getHostID());

                // Create INDArray of zeros
                INDArray data = Nd4j.zeros(nRows, nColumns.get(index));

                // Adding VM Constraints
                data.put(0, 0, sol.items[i].hashCode());

                int x = 1;
                for (int j = 0; j < sol.items.length; j++) {
                        data.put(0, x, sol.items[j].basedetails.vm.getResourceAllocation().allocated.getRequiredCPUs());
                        x++;
                        data.put(0, x, sol.items[j].basedetails.vm.getResourceAllocation().allocated.getRequiredMemory());
                        x++;
                        data.put(0, x, sol.items[j].getHostID());
                        x++;
                }

                // Normalize Input
                normalize.preProcess(data, null, DisStat);

                // Output from Network
                INDArray result = networks.get(index).output(data, false).toDense();

                // Index of the maximum value in the array (Classification)
                int idx = Nd4j.getExecutioner().execAndReturn(new IAMax(result)).getFinalResult();

                if (sol.items[i].getHostID() != idx) {
                    migrationCounter++;
                    sol.items[i].migrate(sol.bins[idx]);
                }

                // After consolidation
                //System.out.println("A-VM-"+sol.items[i].hashCode()+"\t->\tA-HOST-"+sol.items[i].getHostID());

            }
            sol.calculateFitness();

            return sol;
        }
        public InfrastructureModel consolidateSplit(InfrastructureModel toConsolidate){
            //Split and Merge object for splitting the IaaSService
            SplitMerge sm = new SplitMerge();
            //Splitting the IaaSService
            ArrayList<InfrastructureModel> splitIMs = sm.splitBefore(toConsolidate, 4);
            //Consolidated split infrastructureModels
            ArrayList<InfrastructureModel> consolidatedIMs = new ArrayList<>();
            //Executing consolidation on each split infrastructure
            for(int i=0;i<splitIMs.size();i++){
                consolidatedIMs.add(optimize(splitIMs.get(i)));
            }
            InfrastructureModel merged = sm.mergeIMs(consolidatedIMs);
            return merged;
        }
    }
